package com.lyf.scm.core.remote.stock;

import static java.math.BigDecimal.ROUND_DOWN;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.lyf.scm.common.constants.CommonConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.constants.WhAllocationConstants;
import com.lyf.scm.common.enums.RealWarehouseTypeEnum;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.domain.entity.stockFront.RecordRealVirtualStockSyncRelationE;
import com.lyf.scm.core.mapper.stockFront.RecordRealVirtualStockSyncRelationMapper;
import com.lyf.scm.core.remote.item.dto.SkuUnitExtDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.stock.dto.AllocationCalQtyParam;
import com.lyf.scm.core.remote.stock.dto.AllocationCalQtyRes;
import com.lyf.scm.core.remote.stock.dto.QueryVmSkuPermitDTO;
import com.lyf.scm.core.remote.stock.dto.VirtualWarehouse;
import com.lyf.scm.core.remote.stock.dto.VmSkuPermit;
import com.lyf.scm.core.remote.stock.dto.VwAllocationQty;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.rome.arch.core.exception.RomeException;

import lombok.extern.slf4j.Slf4j;

/**
 * @Doc:预计算虚仓分配数工具类
 * @Author: lchy
 * @Date: 2020/3/25
 * @Version 1.0
 */
@Slf4j
@Component
public class AllocationTool {

	@Resource
	private RecordRealVirtualStockSyncRelationMapper recordRealVirtualStockSyncRelationMapper;

	@Resource
	private ItemFacade itemFacade;

	@Resource
	private StockRealWarehouseFacade stockRealWarehouseFacade;


	/**
	 * 发货单位取整以及虚仓预计算都要做,出库逻辑不考虑虚仓权限
	 * 
	 * @param inRealHouseType
	 *            outRealHouseType outRealHouseId
	 * @param paramList
	 * @param recordCode
	 * @return
	 */
	public List<AllocationCalQtyRes> calculateVmQtyAndRound(Integer inRealHouseType, Integer outRealHouseType,
			Long outRealHouseId, List<AllocationCalQtyParam> paramList, String recordCode) {
		List<RecordRealVirtualStockSyncRelationE> configList = recordRealVirtualStockSyncRelationMapper
				.queryByRecordCode(recordCode);
		configList = configList.stream().filter(v -> outRealHouseId.equals(v.getRealWarehouseId()))
				.collect(Collectors.toList());
		return this.calculateVmQtyAndRound(inRealHouseType, outRealHouseType, paramList, configList, true, false);
	}

	/**
	 * 只做发货单位取整问题
	 * 
	 * @param inRealHouseType,
	 *            outRealHouseType
	 * @param paramList
	 * @return
	 */
	public List<AllocationCalQtyRes> calculateRound(Integer inRealHouseType, Integer outRealHouseType,
			List<AllocationCalQtyParam> paramList) {
		return this.calculateVmQtyAndRound(inRealHouseType, outRealHouseType, paramList, null, true, false);
	}

	/**
	 * 只做虚仓分配预计算,需用实仓过滤分配关系，一个单据对应多个仓必传，一对一也可以用，不过建议用下面的那个接口
	 * 
	 * @param paramList
	 * @return
	 */
	public List<AllocationCalQtyRes> calculateVmQty(List<AllocationCalQtyParam> paramList, String recordCode,
			Long rwId) {
		List<RecordRealVirtualStockSyncRelationE> configList = recordRealVirtualStockSyncRelationMapper
				.queryByRecordCode(recordCode);
		configList = configList.stream().filter(v -> rwId.equals(v.getRealWarehouseId())).collect(Collectors.toList());
		return this.calculateVmQtyAndRound(-1, -1, paramList, configList, false, true);
	}

	/**
	 * 只做虚仓分配预计算,无需用实仓过滤分配关系，一个单据对应一个仓
	 * 
	 * @param paramList
	 * @return
	 */
	public List<AllocationCalQtyRes> calculateVmQty(List<AllocationCalQtyParam> paramList, String recordCode) {

		List<RecordRealVirtualStockSyncRelationE> configList = recordRealVirtualStockSyncRelationMapper
				.queryByRecordCode(recordCode);
		return this.calculateVmQtyAndRound(-1, -1, paramList, configList, false, true);
	}

	/**
	 * @param inRealHouseType
	 *            仓库类型，总仓需要考虑发货单位取整问题
	 * @param outRealHouseType
	 *            仓库类型，总仓需要考虑发货单位取整问题
	 * @param paramList
	 *            计算入参 各sku的初始需求
	 * @param isConsiderRound
	 *            是否考虑取整计算
	 * @param isConsiderPermit
	 *            是否考虑虚仓进货权或渠道sku,这里只做配了单据级比例或绝对数的sku，没有配的在库存模型底层处理
	 * @return 返回 各sku虚仓分配数
	 */
	private List<AllocationCalQtyRes> calculateVmQtyAndRound(Integer inRealHouseType, Integer outRealHouseType,
			List<AllocationCalQtyParam> paramList, List<RecordRealVirtualStockSyncRelationE> configList,
			boolean isConsiderRound, boolean isConsiderPermit) {
		List<AllocationCalQtyRes> result = new ArrayList<>();
		// 是否考虑发货单单位取整问题
		boolean isNeedConsiderRoundInner = RealWarehouseTypeEnum.RW_TYPE_16.getType().equals(outRealHouseType)
				|| RealWarehouseTypeEnum.RW_TYPE_16.getType().equals(inRealHouseType);

		List<String> skuCodeList = paramList.stream().map(AllocationCalQtyParam::getSkuCode).distinct()
				.collect(Collectors.toList());
		List<SkuUnitExtDTO> skuInfos = itemFacade.querySkuUnits(skuCodeList);

		Map<String, SkuUnitExtDTO> skuInfoUnitMap = new HashMap<>();
		Map<String, SkuUnitExtDTO> skuInfoTypeMap = new HashMap<>();
		for (SkuUnitExtDTO dto : skuInfos) {
			String typeKey = dto.getSkuCode() + "_" + dto.getType();
			String unitKey = dto.getSkuCode() + "_" + dto.getUnitCode();
			if (!skuInfoUnitMap.containsKey(unitKey)) {
				skuInfoUnitMap.put(unitKey, dto);
			}

			// 一个单位可能会有多个，但是这里默认取第一个
			if (!skuInfoTypeMap.containsKey(typeKey)) {
				skuInfoTypeMap.put(typeKey, dto);
			}
		}

		Map<Long, List<RecordRealVirtualStockSyncRelationE>> relationMap = new HashMap<>();
		// sku权限map,key=skuId，value为有权限的虚仓id、列表
		Map<Long, List<Long>> permitMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(configList)) {
			// allotType=1按比例分配
			List<RecordRealVirtualStockSyncRelationE> rateAllotList = configList.stream()
					.filter(v -> Integer.valueOf(1).equals(v.getAllotType())).collect(Collectors.toList());
			// allotType=2按绝对值分配
			List<RecordRealVirtualStockSyncRelationE> absoluteAllotList = configList.stream()
					.filter(v -> Integer.valueOf(2).equals(v.getAllotType())).collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(rateAllotList) && rateAllotList.size() != configList.size()) {
				throw new RomeException(ResCode.ORDER_ERROR_1001, ResCode.ORDER_ERROR_1001_DESC + "：虚仓分配类型不一致");
			}
			if (CollectionUtils.isNotEmpty(absoluteAllotList) && absoluteAllotList.size() != configList.size()) {
				throw new RomeException(ResCode.ORDER_ERROR_1001, ResCode.ORDER_ERROR_1001_DESC + "：虚仓分配类型不一致");
			}
			relationMap = configList.stream()
					.collect(Collectors.groupingBy(RecordRealVirtualStockSyncRelationE::getSkuId));
			// 计算组装虚仓权限map
			if (isConsiderPermit) {
				List<String> vwCodes = configList.stream()
						.map(RecordRealVirtualStockSyncRelationE::getVirtualWarehouseCode).distinct()
						.collect(Collectors.toList());
				List<VirtualWarehouse> vHouseList = stockRealWarehouseFacade.queryVirtualWarehouseByCodes(vwCodes);
				// 组跟虚仓的对应关系map
				Map<Long, List<Long>> gvidMap = vHouseList.stream()
						.collect(Collectors.groupingBy(VirtualWarehouse::getVirtualWarehouseGroupId,
								Collectors.mapping(VirtualWarehouse::getId, Collectors.toList())));

				QueryVmSkuPermitDTO queryVmSkuPermitDTO = new QueryVmSkuPermitDTO();
				queryVmSkuPermitDTO.setGroupIds(new ArrayList<>(gvidMap.keySet()));
				queryVmSkuPermitDTO.setSkuIds(new ArrayList<>(relationMap.keySet()));
				queryVmSkuPermitDTO.setIsPermit(1);
				List<VmSkuPermit> permitDTOList = stockRealWarehouseFacade.queryVmSkuPermitByGroupIdsAndSkuIds(queryVmSkuPermitDTO);
				if (permitDTOList != null) {
					for (VmSkuPermit permitDTO : permitDTOList) {
						List<Long> oldVidList = permitMap.get(permitDTO.getSkuId());
						if (oldVidList == null) {
							oldVidList = new ArrayList<>();
						}
						oldVidList.addAll(gvidMap.get(permitDTO.getVirtualWarehouseGroupId()));
						permitMap.put(permitDTO.getSkuId(), oldVidList);
					}
				}
			}
		}

		for (AllocationCalQtyParam param : paramList) {
			SkuUnitExtDTO basicDto = skuInfoTypeMap.get(param.getSkuCode() + "_" + WhAllocationConstants.BASIC_TYPE);
			if (basicDto == null) {
				throw new RomeException(ResCode.ORDER_ERROR_1002,
						ResCode.ORDER_ERROR_1002_DESC + ":查询不到基本为单位 " + param.getSkuCode());
			}
			if (param.getPlanQty() == null) {
				throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + ":planQty不能为空 ");

			}
			if (param.getUnitCode() == null) {
				// 如果入参没有传单位，则默认为是按基本单位分配，库存这边大部分场景都是基本单位预计算，没有必要专门去查基本单位，这里统一处理
				param.setUnitCode(basicDto.getUnitCode());
			}
			AllocationCalQtyRes resDto = this.buildBasicAllocationCalQtyResDto(param);
			SkuUnitExtDTO dto = getBaseSkuInitExDTOByUnitCode(skuInfoUnitMap, param);

			// 1、第一步先转换为基本单位数量
			BigDecimal planQty = param.getPlanQty();
			BigDecimal basicQty = planQty.multiply(dto.getScale()).setScale(CommonConstants.DECIMAL_POINT_NUM,
					ROUND_DOWN);
			resDto.setPlanBasicQty(basicQty);
			// 按发货单位取整后的基本单位数量
			BigDecimal realBasicQty = basicQty;
			// 唯一的作用就是给 reCalculateVmQtyForOneSku 这个方法用
			resDto.setConsiderRound(isConsiderRound && isNeedConsiderRoundInner);
			String actualUnitCode = dto.getUnitCode();
			String actualUnitName = dto.getUnitName();
			BigDecimal actualScale = dto.getScale();
			if (isConsiderRound && isNeedConsiderRoundInner && "KAR".equals(dto.getUnitCode())) {
				// 如果是箱单位，直接按箱单位取整，无需按发货单位取整
				planQty = planQty.setScale(0, ROUND_DOWN);
				realBasicQty = planQty.multiply(dto.getScale()).setScale(CommonConstants.DECIMAL_POINT_NUM, ROUND_DOWN);
			} else if (isConsiderRound && isNeedConsiderRoundInner) {
				// 2、发货单位取整计算
				SkuUnitExtDTO transDto = skuInfoTypeMap
						.get(param.getSkuCode() + "_" + WhAllocationConstants.TRANS_TYPE);
				if (transDto == null) {
					throw new RomeException(ResCode.ORDER_ERROR_1002,
							ResCode.ORDER_ERROR_1002_DESC + " 发货单位在商品中心不存在 " + param.getSkuCode());

				}
				resDto.setScale(transDto.getScale());
				// 发货单位的数量
				BigDecimal skuQty = basicQty.divide(transDto.getScale(), 0, ROUND_DOWN);
				realBasicQty = skuQty.multiply(transDto.getScale()).setScale(CommonConstants.DECIMAL_POINT_NUM,
						ROUND_DOWN);

				// 4、为了避免小数除不尽的问题，只要是考虑了发货单位取整的问题，就一律转换为发货单位
				planQty = skuQty;
				// 按发货单位取整后，原前置单位调整成发货单位
				actualUnitCode = transDto.getUnitCode();
				actualUnitName = transDto.getUnitName();
				actualScale = transDto.getScale();

			}
			// 3、判断是否调整了库存
			if (realBasicQty.compareTo(basicQty) < 0) {
				resDto.setIsChangedPlan(true);
			}
			// 实际前置单单位用这个单位
			resDto.setActualUnitCode(actualUnitCode);
			resDto.setActualUnitName(actualUnitName);
			resDto.setActualScale(actualScale);
			// 实际的数量用这个数量
			resDto.setActualQty(planQty);
			resDto.setActualBasicQty(realBasicQty);

			resDto.setBasicUnitCode(basicDto.getUnitCode());
			resDto.setBasicUnitName(basicDto.getUnitName());

			// 5、虚仓分配数预计算
			List<VwAllocationQty> vwAllocationQtyList = new ArrayList<>();
			// 单据级别sku配置
			if (relationMap.containsKey(param.getSkuId())) {
				List<RecordRealVirtualStockSyncRelationE> list = relationMap.get(param.getSkuId());
				resDto.setList(list);
				this.vwCalculate(resDto.getList(), realBasicQty, vwAllocationQtyList,
						isConsiderPermit ? permitMap.get(param.getSkuId()) : null);
				resDto.setVwAllocationQtyList(vwAllocationQtyList);
			}
			result.add(resDto);
		}
		return result;
	}

	/**
	 * 单位换算取整操作
	 * 
	 * @param paramList
	 *            入参信息
	 * @return
	 */
	public List<AllocationCalQtyRes> calculateQtyAndRound(List<AllocationCalQtyParam> paramList) {
		List<AllocationCalQtyRes> result = new ArrayList<>();
		List<String> skuCodeList = paramList.stream().map(x -> x.getSkuCode()).collect(Collectors.toList());
		List<SkuUnitExtDTO> skuInfos = itemFacade.querySkuUnits(skuCodeList);
		if (CollectionUtils.isEmpty(skuInfos)) {
			throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + "未查询到商品单位信息 ");
		}
		// 构建sku和type组成的集合
		Map<String, SkuUnitExtDTO> skuInfoTypeMap = skuInfos.stream()
				.collect(Collectors.toMap(SkuUnitExtDTO::getSkuTypePrimaryKey, Function.identity(), (v1, v2) -> v2));
		// 构建skuCode和unitCode组成的集合信息
		Map<String, SkuUnitExtDTO> skuInfoUnitMap = skuInfos.stream().collect(
				Collectors.toMap(SkuUnitExtDTO::getSkuUnitCodeByPrimaryKey, Function.identity(), (v1, v2) -> v2));
		for (AllocationCalQtyParam param : paramList) {
			// 获取基础单位转换比例信息
			SkuUnitExtDTO dto = this.getBaseSkuInitExDTOByUnitCode(skuInfoUnitMap, param);
			// 根据基础单位转换比例计算基础单位数量
			BigDecimal basicQty = this.convertBasicQty(param, dto);
			// 获取发货单位转换比例信息
			SkuUnitExtDTO transDto = this.getTransSkuUnitExtDTO(skuInfoTypeMap, param);
			// 发货单位取整
			BigDecimal skuQty = basicQty.divide(transDto.getScale(), 0, ROUND_DOWN);
			// 转换后的发货单位数量
			BigDecimal realBasicQty = this.convertTransQty(skuQty, transDto);
			// 转换成销售单位取整
			BigDecimal saleQty = realBasicQty.divide(param.getScale(), CommonConstants.DECIMAL_POINT_NUM, ROUND_DOWN);
			// 转换成实际销售数量
			BigDecimal saleBasicQty = saleQty.multiply(param.getScale()).setScale(CommonConstants.DECIMAL_POINT_NUM,
					ROUND_DOWN);
			// 构建返回对象信息
			AllocationCalQtyRes allocationCalQtyRes = this.buildResDto(skuInfoTypeMap, param, saleQty, basicQty,
					transDto, saleBasicQty);
			result.add(allocationCalQtyRes);
		}
		return result;
	}

	/**
	 * 获取商品基础单位信息
	 * 
	 * @param param
	 * @return
	 */
	public SkuUnitExtDTO getBaseSkuUnitExtDTO(AllocationCalQtyParam param) {
		List<SkuUnitExtDTO> skuUnitExtDTOS = itemFacade.querySkuUnits(Arrays.asList(param.getSkuCode()));
		AlikAssert.isNotEmpty(skuUnitExtDTOS, ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + "未查询到商品单位信息");
		Map<String, SkuUnitExtDTO> skuInfoUnitMap = skuUnitExtDTOS.stream().collect(
				Collectors.toMap(SkuUnitExtDTO::getSkuUnitCodeByPrimaryKey, Function.identity(), (v1, v2) -> v2));
		return this.getBaseSkuInitExDTOByUnitCode(skuInfoUnitMap, param);

	}

	/**
	 * 构建返回对象
	 * 
	 * @param skuInfoTypeMap
	 *            商品类型转换map
	 * @param param
	 *            输入前置单信息
	 * @param skuQty
	 *            基础单位取整的数量
	 * @param basicQty
	 *            基础单位数量
	 * @param transDto
	 *            转换对象信息
	 * @param realBasicQty
	 *            发货单位数量
	 * @return
	 */
	private AllocationCalQtyRes buildResDto(Map<String, SkuUnitExtDTO> skuInfoTypeMap, AllocationCalQtyParam param,
			BigDecimal skuQty, BigDecimal basicQty, SkuUnitExtDTO transDto, BigDecimal realBasicQty) {
		AllocationCalQtyRes resDto = new AllocationCalQtyRes();
		resDto.setSkuId(param.getSkuId());
		resDto.setSkuCode(param.getSkuCode());
		resDto.setPlanQty(param.getPlanQty());
		resDto.setUnitCode(param.getUnitCode());
		resDto.setActualQty(skuQty);
		// 判断是否调整了库存
		if (realBasicQty.compareTo(basicQty) < 0) {
			resDto.setIsChangedPlan(true);
		}
		resDto.setScale(transDto.getScale());
		// 实际前置单单位用这个单位
		resDto.setActualUnitCode(transDto.getUnitCode());
		resDto.setActualUnitName(transDto.getUnitName());
		resDto.setActualScale(transDto.getScale());
		// 实际的数量用这个数量
		resDto.setActualBasicQty(realBasicQty);
		SkuUnitExtDTO basicDto = this.getBaseSkuUnitExtDTOByType(skuInfoTypeMap, param);
		resDto.setBasicUnitCode(basicDto.getUnitCode());
		resDto.setBasicUnitName(basicDto.getUnitName());
		resDto.setPlanBasicQty(basicQty);
		return resDto;
	}

	/**
	 * 基础单位数量转换成发货数量
	 * 
	 * @param skuQty
	 * @param transDto
	 * @return
	 */
	private BigDecimal convertTransQty(BigDecimal skuQty, SkuUnitExtDTO transDto) {
		return skuQty.multiply(transDto.getScale()).setScale(CommonConstants.DECIMAL_POINT_NUM, ROUND_DOWN);
	}

	/**
	 * 获取发货单位转换信息
	 * 
	 * @param skuInfoTypeMap
	 * @param param
	 * @return
	 */
	private SkuUnitExtDTO getTransSkuUnitExtDTO(Map<String, SkuUnitExtDTO> skuInfoTypeMap,
			AllocationCalQtyParam param) {
		SkuUnitExtDTO transDto = skuInfoTypeMap.get(param.getTransSkuTypeKey());
		if (Objects.isNull(transDto)) {
			throw new RomeException(ResCode.ORDER_ERROR_1002,
					ResCode.ORDER_ERROR_1002_DESC + " 发货单位在商品中心不存在 " + param.getSkuCode());
		}
		return transDto;
	}

	/**
	 * 将前置单位转出基础单位
	 * 
	 * @param param
	 *            入参
	 * @param dto
	 *            商品转换信息
	 * @return
	 */
	private BigDecimal convertBasicQty(AllocationCalQtyParam param, SkuUnitExtDTO dto) {
		BigDecimal planQty = param.getPlanQty();
		return planQty.multiply(dto.getScale()).setScale(CommonConstants.DECIMAL_POINT_NUM, ROUND_DOWN);
	}

	/**
	 * 根据商品编码和unitCode 获取商品转换信息
	 * 
	 * @param skuInfoUnitMap
	 * @param param
	 * @return
	 */
	private SkuUnitExtDTO getBaseSkuInitExDTOByUnitCode(Map<String, SkuUnitExtDTO> skuInfoUnitMap,
			AllocationCalQtyParam param) {
		SkuUnitExtDTO dto = skuInfoUnitMap.get(param.getSkuUnitCodeKey());
		if (dto == null) {
			throw new RomeException(ResCode.ORDER_ERROR_1002,
					ResCode.ORDER_ERROR_1002_DESC + "入参单位在商品中心不存在 " + param.getSkuCode());
		}
		return dto;
	}

	/**
	 * 根据skuType获取指定商品基础单位转换信息
	 * 
	 * @param skuInfoTypeMap
	 * @param param
	 * @return
	 */
	private SkuUnitExtDTO getBaseSkuUnitExtDTOByType(Map<String, SkuUnitExtDTO> skuInfoTypeMap,
			AllocationCalQtyParam param) {
		SkuUnitExtDTO basicDto = skuInfoTypeMap.get(param.getBaseSkuTypeKey());
		if (Objects.isNull(basicDto)) {
			throw new RomeException(ResCode.ORDER_ERROR_1002,
					ResCode.ORDER_ERROR_1002_DESC + ":查询不到基本为单位 " + param.getSkuCode());
		}
		if (Objects.isNull(param.getPlanQty())) {
			throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + ":planQty不能为空 ");
		}
		if (Objects.isNull(param.getUnitCode())) {
			// 如果入参没有传单位，则默认为是按基本单位分配，库存这边大部分场景都是基本单位预计算，没有必要专门去查基本单位，这里统一处理
			param.setUnitCode(basicDto.getUnitCode());
		}
		return basicDto;
	}

	/**
	 * 实仓库存不足时，需要重新计算发货单位取整问题，并重新预计算虚仓预分配数
	 * 
	 * @param resDto
	 */
	public void reCalculateVmQtyForOneSku(AllocationCalQtyRes resDto) {
		if (resDto.getPlanBasicQty() == null) {
			throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + ":planBasicQty 不能空");
		}
		// 1、第一步先转换为前置单位数量
		BigDecimal planQty = resDto.getPlanBasicQty().divide(resDto.getActualScale(), CommonConstants.DECIMAL_POINT_NUM,
				ROUND_DOWN);
		BigDecimal basicQty = resDto.getPlanBasicQty();
		resDto.setPlanQty(planQty);
		// 按发货单位取整后的基本单位数量
		BigDecimal realBasicQty = basicQty;
		if (resDto.getConsiderRound() && "KAR".equals(resDto.getActualUnitCode())) {
			// 如果是箱单位，直接按箱单位取整，无需按发货单位取整
			planQty = planQty.setScale(0, ROUND_DOWN);
			realBasicQty = planQty.multiply(resDto.getActualScale()).setScale(CommonConstants.DECIMAL_POINT_NUM,
					ROUND_DOWN);
		} else if (resDto.getConsiderRound() && resDto.getScale() != null) {
			// 发货单位的数量
			BigDecimal skuQty = basicQty.divide(resDto.getScale(), 0, ROUND_DOWN);
			realBasicQty = skuQty.multiply(resDto.getScale()).setScale(CommonConstants.DECIMAL_POINT_NUM, ROUND_DOWN);

			// 4、换算发货单位取整后的前置单单位数量，这里getActualScale() 正常情况下是等于getScale()即planQty = skuQty
			// 这里没有直接使用planQty=skuQty赋值，是考虑到不是通过calculateVmQtyAndRound方法的结果再来重取整计算的场景，给调用方导致诡异的结果，即前置单位并不是发货单位
			planQty = realBasicQty.divide(resDto.getActualScale(), CommonConstants.DECIMAL_POINT_NUM, ROUND_DOWN);
		}
		// 3、判断是否调整了库存
		if (realBasicQty.compareTo(basicQty) < 0) {
			resDto.setIsChangedPlan(true);
		}
		resDto.setActualBasicQty(realBasicQty);
		resDto.setActualQty(planQty);
		// 5、虚仓分配数预计算
		List<VwAllocationQty> vwAllocationQtyList = new ArrayList<>();
		// 单据级别sku配置
		if (resDto.getList() != null) {
			this.vwCalculate(resDto.getList(), realBasicQty, vwAllocationQtyList, null);
			resDto.setVwAllocationQtyList(vwAllocationQtyList);
		}
	}

	/**
	 * 根据实际数量以及设置比例或绝对数预计算虚仓分配数
	 * 
	 * @param list
	 * @param realBasicQty
	 * @param vwAllocationQtyList
	 */
	private void vwCalculate(List<RecordRealVirtualStockSyncRelationE> list, BigDecimal realBasicQty,
			List<VwAllocationQty> vwAllocationQtyList, List<Long> vIds) {
		// 实际总库存
		BigDecimal countStock = realBasicQty;
		// 计划分配总库存
		BigDecimal planCountStock = BigDecimal.ZERO;
		// 已分配库存
		BigDecimal allotCountStock = BigDecimal.ZERO;
		// 计划分配总库存[有权限的虚仓之和]
		BigDecimal planCountStockWithPermit = BigDecimal.ZERO;

		// 设置为0的虚仓
		List<RecordRealVirtualStockSyncRelationE> tempListWithZero = new ArrayList<>();
		// 设置不为0且有权限
		List<RecordRealVirtualStockSyncRelationE> tempListWithPermit = new ArrayList<>();
		// 设置不为0但无权限
		List<RecordRealVirtualStockSyncRelationE> tempListWithNoPermit = new ArrayList<>();

		for (RecordRealVirtualStockSyncRelationE item : list) {
			if (item.getSyncRate() == null || item.getSyncRate().compareTo(BigDecimal.ZERO) <= 0) {
				// == 0 的虚仓先处理掉，一定不参与分配
				item.setSyncRate(BigDecimal.ZERO);
				tempListWithZero.add(item);
			} else {
				planCountStock = planCountStock.add(item.getSyncRate());
				if (vIds != null && vIds.contains(item.getVirtualWarehouseId())) {
					planCountStockWithPermit = planCountStockWithPermit.add(item.getSyncRate());
					tempListWithPermit.add(item);
				} else {
					tempListWithNoPermit.add(item);
				}
			}
		}
		if (tempListWithPermit.size() > 0 && tempListWithNoPermit.size() > 0) {
			// 2个size都大于0 表示是部分虚仓有权限，则需要扩大比例或绝对数
			// 只要有1个size=0 表示无需重置设置的比例，因为都有或者都没有权限都要用默认的比例
			for (RecordRealVirtualStockSyncRelationE item : tempListWithPermit) {
				// 有权限的扩大比例或绝对数
				if (Integer.valueOf(1).equals(item.getAllotType())) {
					item.setSyncRate(item.getSyncRate().multiply(new BigDecimal(100)).divide(planCountStockWithPermit,
							0, BigDecimal.ROUND_DOWN));
				} else if (Integer.valueOf(2).equals(item.getAllotType())) {
					item.setSyncRate(planCountStock.multiply(item.getSyncRate().divide(planCountStockWithPermit,
							CommonConstants.DECIMAL_POINT_NUM, BigDecimal.ROUND_DOWN)));
				}
			}
			for (RecordRealVirtualStockSyncRelationE item : tempListWithNoPermit) {
				// 没有权限的直接归入到为0的虚仓列表
				item.setSyncRate(BigDecimal.ZERO);
			}
		} else if (planCountStock.compareTo(new BigDecimal(100)) != 0) {
			for (RecordRealVirtualStockSyncRelationE item : tempListWithPermit) {
				// 对于比例不等于100%的需要扩大或缩小处理，避免不满100或超过100的错误数据
				if (Integer.valueOf(1).equals(item.getAllotType())) {
					item.setSyncRate(item.getSyncRate().multiply(new BigDecimal(100)).divide(planCountStock, 0,
							BigDecimal.ROUND_DOWN));
				}
			}
		}
		// 重置好了后三个list归队，一起计算预分配数,为0放在队列前面,避免成为最后一个用减法导致错误
		tempListWithZero.addAll(tempListWithNoPermit);
		tempListWithZero.addAll(tempListWithPermit);

		BigDecimal temp = BigDecimal.ZERO;
		for (int i = 0; i < tempListWithZero.size(); i++) {
			VwAllocationQty vwAllocationQty = new VwAllocationQty();
			RecordRealVirtualStockSyncRelationE relation = tempListWithZero.get(i);
			vwAllocationQty.setVirtualWarehouseId(relation.getVirtualWarehouseId());
			BigDecimal syncRate = relation.getSyncRate() == null ? BigDecimal.ZERO : relation.getSyncRate();
			// 按比例分配
			if (Integer.valueOf(1).equals(relation.getAllotType())) {
				temp = countStock.multiply(syncRate).divide(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN);
			} else if (Integer.valueOf(2).equals(relation.getAllotType())) {
				temp = syncRate.multiply(countStock).divide(planCountStock, 0, BigDecimal.ROUND_DOWN);
			}
			// 如果是最后一个
			if (i == tempListWithZero.size() - 1) {
				vwAllocationQty.setSkuQty(countStock.subtract(allotCountStock));
				allotCountStock = allotCountStock.add(countStock.subtract(allotCountStock));
			} else {
				vwAllocationQty.setSkuQty(temp);
				allotCountStock = allotCountStock.add(temp);
			}
			vwAllocationQtyList.add(vwAllocationQty);
		}
	}

	/**
	 * 构建返回结果对象
	 * 
	 * @param param
	 *            入参部分参数
	 * @return
	 */
	private AllocationCalQtyRes buildBasicAllocationCalQtyResDto(AllocationCalQtyParam param) {
		AllocationCalQtyRes resDto = new AllocationCalQtyRes();
		resDto.setLineNo(param.getLineNo());
		resDto.setSkuId(param.getSkuId());
		resDto.setSkuCode(param.getSkuCode());
		resDto.setPlanQty(param.getPlanQty());
		resDto.setUnitCode(param.getUnitCode());
		return resDto;
	}

}