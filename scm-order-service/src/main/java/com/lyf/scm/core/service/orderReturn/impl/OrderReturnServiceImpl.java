package com.lyf.scm.core.service.orderReturn.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.CommonConstants;
import com.lyf.scm.common.constants.OrderReturnConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.constants.StockCoreConstant;
import com.lyf.scm.common.constants.WarehouseRecordConstant;
import com.lyf.scm.common.enums.FrontRecordTypeEnum;
import com.lyf.scm.common.enums.OrderStatusEnum;
import com.lyf.scm.common.enums.SkuUnitTypeEnum;
import com.lyf.scm.common.enums.WarehouseRecordBatchStatusEnum;
import com.lyf.scm.common.enums.WarehouseRecordBusinessTypeEnum;
import com.lyf.scm.common.enums.WarehouseRecordStatusEnum;
import com.lyf.scm.common.enums.WarehouseRecordTypeEnum;
import com.lyf.scm.common.enums.WmsSyncStatusEnum;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.notify.StockNotifyDTO;
import com.lyf.scm.core.api.dto.notify.StockNotifyDetailDTO;
import com.lyf.scm.core.api.dto.orderReturn.OrderReturnDTO;
import com.lyf.scm.core.api.dto.orderReturn.OrderReturnDetailDTO;
import com.lyf.scm.core.api.dto.orderReturn.PushReturnDTO;
import com.lyf.scm.core.api.dto.orderReturn.PushReturnDetailDTO;
import com.lyf.scm.core.api.dto.orderReturn.PushReturnDetailNoticeDTO;
import com.lyf.scm.core.api.dto.orderReturn.PushReturnNoticeDTO;
import com.lyf.scm.core.api.dto.orderReturn.ReturnDTO;
import com.lyf.scm.core.api.dto.orderReturn.ReturnDetailDTO;
import com.lyf.scm.core.api.dto.orderReturn.ReturnDetailNoticeDTO;
import com.lyf.scm.core.api.dto.orderReturn.ReturnNoticeDTO;
import com.lyf.scm.core.domain.convert.online.AddressConvertor;
import com.lyf.scm.core.domain.convert.orderReturn.OrderReturnConvert;
import com.lyf.scm.core.domain.convert.orderReturn.OrderReturnDetailConvert;
import com.lyf.scm.core.domain.convert.stockFront.StockInRecordDTOConvert;
import com.lyf.scm.core.domain.entity.online.AddressE;
import com.lyf.scm.core.domain.entity.order.OrderDetailE;
import com.lyf.scm.core.domain.entity.order.OrderE;
import com.lyf.scm.core.domain.entity.orderReturn.OrderReturnDetailE;
import com.lyf.scm.core.domain.entity.orderReturn.OrderReturnE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.domain.model.OrderDetailDO;
import com.lyf.scm.core.mapper.online.AddressMapper;
import com.lyf.scm.core.mapper.order.OrderDetailMapper;
import com.lyf.scm.core.mapper.order.OrderMapper;
import com.lyf.scm.core.mapper.orderReturn.OrderReturnDetailMapper;
import com.lyf.scm.core.mapper.orderReturn.OrderReturnMapper;
import com.lyf.scm.core.mapper.stockFront.FrontWarehouseRecordRelationMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordDetailMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.remote.item.ItemInfoTool;
import com.lyf.scm.core.remote.item.dto.ParamExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuInfoExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuUnitExtDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.stock.dto.InWarehouseRecordDTO;
import com.lyf.scm.core.remote.stock.dto.QueryRealWarehouseDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockFacade;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.remote.stock.facade.StockRecordFacade;
import com.lyf.scm.core.remote.trade.facade.TradeFacade;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.orderReturn.OrderReturnService;
import com.lyf.scm.core.service.stockFront.FrontWarehouseRecordRelationService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 回调记录接口实现对象
 * <p>
 * @Author: wwh 2020/4/8
 */
@Slf4j
@Service("orderReturnService")
public class OrderReturnServiceImpl implements OrderReturnService {

	@Resource
	private OrderReturnMapper orderReturnMapper;

	@Resource
	private OrderReturnDetailMapper orderReturnDetailMapper;

	@Resource
	private OrderMapper orderMapper;

	@Resource
	private OrderDetailMapper orderDetailMapper;

	@Resource
	private WarehouseRecordMapper warehouseRecordMapper;
	
	@Resource
	private WarehouseRecordDetailMapper warehouseRecordDetailMapper;

	@Resource
	private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;
	
	@Resource
	private AddressMapper addressMapper;

	@Resource
	private OrderUtilService orderUtilService;
	
	@Resource
	private FrontWarehouseRecordRelationService frontWarehouseRecordRelationService;
	
	@Resource
	private OrderReturnConvert orderReturnConvert;

	@Resource
	private StockInRecordDTOConvert stockInRecordDTOConvert;
	
	@Resource
	private OrderReturnDetailConvert orderReturnDetailConvert;

	@Resource
	private AddressConvertor addressConvertor;
	
	@Resource
	private ItemInfoTool itemInfoTool;

	@Resource
	private ItemFacade itemFacade;

	@Resource
	private StockFacade stockFacade;

	@Resource
	private StockRecordFacade stockRecordFacade;

	@Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;
	
	@Resource
	private TradeFacade tradeFacade;

	@Value("${app.merchantId}")
    private Long merchantId;

	/**
	 * 据条件查询退货单列表-分页
	 */
	@Override
	public PageInfo<OrderReturnDTO> queryOrderReturnPageByCondition(OrderReturnDTO orderReturnDTO) {
		Page page = PageHelper.startPage(orderReturnDTO.getPageIndex(), orderReturnDTO.getPageSize());
		List<OrderReturnE> orderReturnEList = orderReturnMapper.queryOrderReturnByCondition(orderReturnDTO);
		if(CollectionUtils.isEmpty(orderReturnEList)) {
			return new PageInfo<>();
		}
		List<OrderReturnDTO> orderReturnDTOList = orderReturnConvert.convertEList2DTOList(orderReturnEList);
		//设置仓库名称、仓库地址
		List<QueryRealWarehouseDTO> queryRealWarehouseDTOList = new ArrayList<QueryRealWarehouseDTO>();
		orderReturnDTOList.forEach(returnDTO -> {
			QueryRealWarehouseDTO queryRealWarehouseDTO = new QueryRealWarehouseDTO();
			queryRealWarehouseDTO.setFactoryCode(returnDTO.getFactoryCode());
			queryRealWarehouseDTO.setWarehouseOutCode(returnDTO.getRealWarehouseCode());
			queryRealWarehouseDTOList.add(queryRealWarehouseDTO);
		});
		List<RealWarehouse> realWarehouseEs = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(queryRealWarehouseDTOList);

		//仓库id---仓库名称
		Map<String, String> realWarehouseInfo = realWarehouseEs.stream().collect(Collectors.toMap(RealWarehouse::getKey , RealWarehouse::getRealWarehouseName, (oldValue,newValue) -> oldValue));
		orderReturnDTOList.forEach(orderReturn -> {
			if(Integer.valueOf(1).equals(orderReturn.getOrderStatus())) {
				orderReturn.setOrderStatusDesc("待入库");
			}
			if(Integer.valueOf(2).equals(orderReturn.getOrderStatus())) {
				orderReturn.setOrderStatusDesc("已入库");
			}
			//设置仓库名称
			if (realWarehouseInfo.containsKey(orderReturn.getFactoryCode() + "-" + orderReturn.getRealWarehouseCode())) {
				orderReturn.setRealWarehouseName(realWarehouseInfo.get(orderReturn.getFactoryCode() + "-" + orderReturn.getRealWarehouseCode()));
			}
		});
		PageInfo<OrderReturnDTO> pageInfo = new PageInfo<OrderReturnDTO>(orderReturnDTOList);
		pageInfo.setTotal(page.getTotal());
		pageInfo.setPageNum(orderReturnDTO.getPageIndex());
		pageInfo.setPageSize(orderReturnDTO.getPageSize());
		return pageInfo;
	}

	/**
	 * 根据售后单号查询退货单详情列表-分页
	 */
	@Override
	public OrderReturnDTO queryOrderReturnDetailPageByAfterSaleCode(String afterSaleCode, Integer pageNum,
																	Integer pageSize) {
		OrderReturnDTO orderReturnDTO = new OrderReturnDTO();
		OrderReturnE orderReturnE = orderReturnMapper.queryOrderReturnByAfterSaleCode(afterSaleCode);
		if(orderReturnE == null) {
			orderReturnDTO.setOrderReturnDetailPageInfo(new PageInfo<>());
			return orderReturnDTO;
		}
		orderReturnDTO = orderReturnConvert.convertE2DTO(orderReturnE);
		//设置仓库名称、仓库地址
		RealWarehouse realWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(orderReturnDTO.getRealWarehouseCode(), orderReturnDTO.getFactoryCode());
		if (null != realWarehouse){
			orderReturnDTO.setRealWarehouseName(realWarehouse.getRealWarehouseName());
			orderReturnDTO.setWarehouseAddress(realWarehouse.getRealWarehouseAddress());
		}

		AddressE addressE = addressMapper.queryByRecordCode(orderReturnE.getRecordCode());
		if(addressE != null) {
			orderReturnDTO.setProvinceName(addressE.getProvince());
			orderReturnDTO.setProvinceCode(addressE.getProvinceCode());
			orderReturnDTO.setCityName(addressE.getCity());
			orderReturnDTO.setCityCode(addressE.getCityCode());
			orderReturnDTO.setCountyName(addressE.getCountry());
			orderReturnDTO.setCountyCode(addressE.getCountryCode());
			orderReturnDTO.setCustomAddress(addressE.getAddress());
			orderReturnDTO.setCustomName(addressE.getName());
		}

		//分页查询退货单详情列表
		Page page = PageHelper.startPage(pageNum, pageSize);
		List<OrderReturnDetailE> orderReturnDetailEList = orderReturnDetailMapper.queryOrderReturnDetailByAfterSaleCode(afterSaleCode);
		if(CollectionUtils.isEmpty(orderReturnDetailEList)) {
			orderReturnDTO.setOrderReturnDetailPageInfo(new PageInfo<>());
			return orderReturnDTO;
		}
		List<OrderReturnDetailDTO> orderReturnDetailDTOList = orderReturnDetailConvert.convertEList2DTOList(orderReturnDetailEList);
		List<String> skuCodeList = orderReturnDetailDTOList.stream().map(OrderReturnDetailDTO :: getSkuCode).distinct().collect(Collectors.toList());
		List<SkuInfoExtDTO> SkuInfoExtDTOList = new ArrayList<SkuInfoExtDTO>();
		try {
			SkuInfoExtDTOList = itemFacade.skuListBySkuCodes(skuCodeList);
		} catch (Exception e1) {
			log.error(e1.getMessage(), e1);
		}
		Map<String, SkuInfoExtDTO> skuInfoExtDTOMap = SkuInfoExtDTOList.stream().collect(Collectors.toMap(SkuInfoExtDTO :: getSkuCode, Function.identity(), (v1, v2) -> v1));
		orderReturnDetailDTOList.forEach(e -> {
			if(skuInfoExtDTOMap.containsKey(e.getSkuCode())) {
				e.setSkuName(skuInfoExtDTOMap.get(e.getSkuCode()).getName());
			}
		});
		PageInfo<OrderReturnDetailDTO> pageInfo = new PageInfo<>(orderReturnDetailDTOList);
		pageInfo.setTotal(page.getTotal());
		pageInfo.setPageNum(pageNum);
		pageInfo.setPageSize(pageSize);
		orderReturnDTO.setOrderReturnDetailPageInfo(pageInfo);
		return orderReturnDTO;
	}

	/**
	 * 根据售后单号查询退货单（包含退货单详情）
	 */
	@Override
	public OrderReturnDTO queryOrderReturnWithDetailByAfterSaleCode(String afterSaleCode) {
		OrderReturnE orderReturnE = orderReturnMapper.queryOrderReturnByAfterSaleCode(afterSaleCode);
		if(orderReturnE == null) {
			return null;
		}
		OrderReturnDTO orderReturnDTO = orderReturnConvert.convertE2DTO(orderReturnE);
		//批量根据实仓CODE查询实仓名称（需库存中心提供接口）
		//设置仓库名称、仓库地址
		//
		List<OrderReturnDetailDTO> orderReturnDetailDTOList = this.queryOrderReturnDetailByAfterSaleCode(afterSaleCode);
		List<String> skuCodeList = orderReturnDetailDTOList.stream().map(OrderReturnDetailDTO :: getSkuCode).distinct().collect(Collectors.toList());
		List<SkuInfoExtDTO> SkuInfoExtDTOList = new ArrayList<SkuInfoExtDTO>();
		try {
			SkuInfoExtDTOList = itemFacade.skuListBySkuCodes(skuCodeList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		Map<String, SkuInfoExtDTO> skuInfoExtDTOMap = SkuInfoExtDTOList.stream().collect(Collectors.toMap(SkuInfoExtDTO :: getSkuCode, Function.identity(), (v1, v2) -> v1));
		orderReturnDetailDTOList.forEach(e -> {
			if(skuInfoExtDTOMap.containsKey(e.getSkuCode())) {
				e.setSkuName(skuInfoExtDTOMap.get(e.getSkuCode()).getName());
			}
		});
		orderReturnDTO.setOrderReturnDetailList(orderReturnDetailDTOList);
		return orderReturnDTO;
	}

	/**
	 * 根据售后单号查询退货单详情列表
	 */
	@Override
	public List<OrderReturnDetailDTO> queryOrderReturnDetailByAfterSaleCode(String afterSaleCode) {
		List<OrderReturnDetailE> orderReturnDetailEList = orderReturnDetailMapper.queryOrderReturnDetailByAfterSaleCode(afterSaleCode);
		List<OrderReturnDetailDTO> orderReturnDetailDTOList = orderReturnDetailConvert.convertEList2DTOList(orderReturnDetailEList);
		List<String> skuCodeList = orderReturnDetailDTOList.stream().map(OrderReturnDetailDTO :: getSkuCode).distinct().collect(Collectors.toList());
		List<SkuInfoExtDTO> SkuInfoExtDTOList = new ArrayList<SkuInfoExtDTO>();
		try {
			SkuInfoExtDTOList = itemFacade.skuListBySkuCodes(skuCodeList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		Map<String, SkuInfoExtDTO> skuInfoExtDTOMap = SkuInfoExtDTOList.stream().collect(Collectors.toMap(SkuInfoExtDTO :: getSkuCode, Function.identity(), (v1, v2) -> v1));
		orderReturnDetailDTOList.forEach(e -> {
			if(skuInfoExtDTOMap.containsKey(e.getSkuCode())) {
				e.setSkuName(skuInfoExtDTOMap.get(e.getSkuCode()).getName());
			}
		});
		return orderReturnDetailDTOList;
	}

	/**
	 * 根据销售单号查询退货单列表（包含退货单详情）
	 */
	@Override
	public List<OrderReturnDTO> queryOrderReturnWithDetailBySaleCode(String saleCode) {
		List<OrderReturnDTO> orderReturnDTOList = new ArrayList<>();
		List<OrderReturnE> orderReturnEList = orderReturnMapper.queryOrderReturnBySaleCode(saleCode);
		if(CollectionUtils.isEmpty(orderReturnEList)) {
			return orderReturnDTOList;
		}
		orderReturnDTOList = orderReturnConvert.convertEList2DTOList(orderReturnEList);
		//批量根据实仓CODE查询实仓名称（需库存中心提供接口）
		//设置仓库名称、仓库地址
		//
		orderReturnDTOList.forEach(e -> {
			List<OrderReturnDetailE> orderReturnDetailEList = orderReturnDetailMapper.queryOrderReturnDetailByAfterSaleCode(e.getAfterSaleCode());
			List<OrderReturnDetailDTO> orderReturnDetailDTOList = orderReturnDetailConvert.convertEList2DTOList(orderReturnDetailEList);
			List<String> skuCodeList = orderReturnDetailDTOList.stream().map(OrderReturnDetailDTO :: getSkuCode).distinct().collect(Collectors.toList());
			List<SkuInfoExtDTO> SkuInfoExtDTOList = new ArrayList<SkuInfoExtDTO>();
			try {
				SkuInfoExtDTOList = itemFacade.skuListBySkuCodes(skuCodeList);
			} catch (Exception e2) {
				log.error(e2.getMessage(), e2);
			}
			Map<String, SkuInfoExtDTO> skuInfoExtDTOMap = SkuInfoExtDTOList.stream().collect(Collectors.toMap(SkuInfoExtDTO :: getSkuCode, Function.identity(), (v1, v2) -> v1));
			orderReturnDetailDTOList.forEach(e1 -> {
				if(skuInfoExtDTOMap.containsKey(e1.getSkuCode())) {
					e1.setSkuName(skuInfoExtDTOMap.get(e1.getSkuCode()).getName());
				}
			});
			e.setOrderReturnDetailList(orderReturnDetailDTOList);
		});
		return orderReturnDTOList;
	}


	/**
	 * 接收交易退货单
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void receiveReturn(ReturnDTO returnDTO) {
		//1、根据售后单查询退货单
		OrderReturnE orderReturnE = orderReturnMapper.queryOrderReturnByAfterSaleCode(returnDTO.getAfterSaleCode());
		//幂等校验
		if(orderReturnE != null) {
			return;
		}
		//2、根据销售单号查询预约单
		OrderE orderE = orderMapper.queryOrderBySaleCode(returnDTO.getSaleCode());
		if(orderE == null) {
			throw new RomeException(ResCode.ORDER_ERROR_1004, ResCode.ORDER_ERROR_1004_DESC);
		}
		//3、获取预约单单据状态
		Integer orderStatus = orderE.getOrderStatus();
		if(!OrderStatusEnum.DELIVERY_STATUS_DONE.getStatus().equals(orderStatus)) {
			throw new RomeException(ResCode.ORDER_ERROR_4001, ResCode.ORDER_ERROR_4001_DESC);
		}
		//4、根据预约单号查询预约单详情
		List<OrderDetailE> orderDetailEList = orderDetailMapper.queryOrderDetailByRecordCode(orderE.getOrderCode());
		if(CollectionUtils.isEmpty(orderDetailEList)) {
			throw new RomeException(ResCode.ORDER_ERROR_1007, ResCode.ORDER_ERROR_1007_DESC);
		}
		List<ReturnDetailDTO> newReturnDetailList = returnDTO.getReturnDetailList();
		List<ReturnDetailDTO> oldReturnDetailList = new ArrayList<ReturnDetailDTO>();
		newReturnDetailList.forEach(e -> {
			ReturnDetailDTO returnDetailDTO = new ReturnDetailDTO();
			BeanUtils.copyProperties(e, returnDetailDTO);
			oldReturnDetailList.add(returnDetailDTO);
		});
		//5、校验退货单详情重复商品
		this.checkReturnDetailSkuCode(newReturnDetailList);
		//6、校验退货单详情商品是否存在
		this.checkReturnDetailSkuCodeExist(newReturnDetailList);
		//7、校验退货单详情商品基础单位
		this.checkReturnDetailSkuCodeBaseUnit(returnDTO);
		//8、校验退货单详情退货数量
		this.checkReturnDetailReturnQty(returnDTO.getSaleCode(), newReturnDetailList, orderDetailEList);
		//9、根据预约单工厂编号查询团购退货仓列表（团购退货仓类型type=22）
		List<RealWarehouse> realWarehouseList = stockRealWarehouseFacade.queryRealWarehouseByFactoryCodeAndRealWarehouseType(orderE.getFactoryCode(), StockCoreConstant.realWarehouseType);
		if(CollectionUtils.isEmpty(realWarehouseList)) {
			throw new RomeException(ResCode.ORDER_ERROR_4005, ResCode.ORDER_ERROR_4005_DESC);
		}
		RealWarehouse realWarehouse = realWarehouseList.get(0);
		//10、保存退货单、退货单详情
		OrderReturnE orderReturn = this.saveOrderReturnWithDetail(returnDTO, orderE, orderDetailEList, oldReturnDetailList, realWarehouse);
		//11保存地址信息
		this.saveAddress(orderReturn);

		orderReturn.setRecordType(FrontRecordTypeEnum.GROUP_RETURN_RECORD.getType());
		orderReturn.setChannelCode(orderE.getChannelCode());
		orderReturn.setOutCreateTime(new Date());
		orderReturn.setRealWarehouse(realWarehouse);
		//12、生成退货入库单给库存中心
		WarehouseRecordE warehouseRecord = this.createWarehouseRecord(orderReturn);
		AlikAssert.isNotNull(warehouseRecord, ResCode.ORDER_ERROR_6026, ResCode.ORDER_ERROR_6026_DESC);
		//13、//同步出库单 到 库存中心
		try {
			InWarehouseRecordDTO inWarehouseRecordDTO = stockInRecordDTOConvert.convertE2InDTO(warehouseRecord);
			inWarehouseRecordDTO.setWarehouseCode(warehouseRecord.getRealWarehouseCode());
			stockRecordFacade.createInRecord(inWarehouseRecordDTO);
		} catch (RomeException e) {
			log.info(e.getMessage(), e);
			throw new RomeException(ResCode.ORDER_ERROR_7306, ResCode.ORDER_ERROR_7306_DESC);
		} catch (Exception e) {
			log.info(e.getMessage(), e);
			throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	private WarehouseRecordE createWarehouseRecord(OrderReturnE orderReturn) {
		WarehouseRecordE warehouseRecordE = new WarehouseRecordE();
		String recordCode = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.RETURN_OUT_RECORD.getCode());
		warehouseRecordE.setRecordCode(recordCode);

		RealWarehouse realWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(orderReturn.getRealWarehouseCode(), orderReturn.getFactoryCode());
		AlikAssert.isNotNull(realWarehouse, ResCode.ORDER_ERROR_6005, ResCode.ORDER_ERROR_6005_DESC);
		warehouseRecordE.setRealWarehouseId(realWarehouse.getId());

		warehouseRecordE.setFactoryCode(orderReturn.getFactoryCode());
		warehouseRecordE.setRealWarehouseCode(orderReturn.getRealWarehouseCode());
		warehouseRecordE.setBusinessType(WarehouseRecordBusinessTypeEnum.IN_WAREHOUSE_RECORD.getType());
		warehouseRecordE.setRecordType(WarehouseRecordTypeEnum.RETURN_OUT_RECORD.getType());
		warehouseRecordE.setRecordStatus(WarehouseRecordStatusEnum.INIT.getStatus());
		warehouseRecordE.setOutCreateTime(orderReturn.getOutCreateTime());
		warehouseRecordE.setSyncWmsStatus(WmsSyncStatusEnum.NO_REQUIRED.getStatus());
		warehouseRecordE.setBatchStatus(WarehouseRecordBatchStatusEnum.INIT.getStatus());
		List<OrderReturnDetailE> frontRecordDetails = orderReturn.getOrderReturnDetailEList();
		List<WarehouseRecordDetailE> warehouseRecordDetailList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(frontRecordDetails)) {
			frontRecordDetails.forEach(returnDetailDTO -> {
				WarehouseRecordDetailE warehouseRecordDetailE = new WarehouseRecordDetailE();
				warehouseRecordDetailE.setSkuCode(returnDetailDTO.getSkuCode());
				//此处设置计划数量和实际数量一致，wms回调再更新实际数量，无需wms回调的业务，直接设置成一致即可
				warehouseRecordDetailE.setPlanQty(returnDetailDTO.getReturnQty());
				warehouseRecordDetailE.setUnit(returnDetailDTO.getUnit());
				warehouseRecordDetailE.setUnitCode(returnDetailDTO.getUnitCode());
				warehouseRecordDetailE.setActualQty(BigDecimal.ZERO);
				warehouseRecordDetailE.setRealWarehouseId(orderReturn.getRealWarehouse().getId());
				warehouseRecordDetailE.setLineNo(String.valueOf(returnDetailDTO.getId()));
				warehouseRecordDetailE.setDeliveryLineNo(String.valueOf(returnDetailDTO.getId()));
				warehouseRecordDetailList.add(warehouseRecordDetailE);
			});
			//设置skuCode和skuID
			itemInfoTool.convertSkuCode(warehouseRecordDetailList);
			warehouseRecordE.setWarehouseRecordDetailList(warehouseRecordDetailList);
		}
		if(Objects.isNull(warehouseRecordE.getSyncDispatchStatus())){
			warehouseRecordE.setSyncDispatchStatus(WarehouseRecordConstant.INIT_DISPATCH);
		}
		if(Objects.isNull(warehouseRecordE.getSyncTransferStatus())){
			warehouseRecordE.setSyncTransferStatus(WarehouseRecordConstant.INIT_TRANSFER);
		}
		if(Objects.isNull(warehouseRecordE.getSyncTradeStatus())){
			warehouseRecordE.setSyncTradeStatus(WarehouseRecordConstant.INIT_SYNC_TRADE);
		}
		//创建后置单
		warehouseRecordMapper.insertWarehouseRecord(warehouseRecordE);
		warehouseRecordE.getWarehouseRecordDetailList().forEach(detailE -> {
			detailE.setWarehouseRecordId(warehouseRecordE.getId());
			detailE.setRecordCode(warehouseRecordE.getRecordCode());
		});
		//保存后置单明细
		warehouseRecordDetailMapper.insertWarehouseRecordDetails(warehouseRecordE.getWarehouseRecordDetailList());
		//保存前置单 + 后置单关系
		frontWarehouseRecordRelationService.saveAddFrontRecordAndWarehouseRelation(warehouseRecordE, orderReturn);
		return warehouseRecordE;
	}

	/**
	 * 保存地址信息
	 * @param orderReturnE
	 */
	private void saveAddress(OrderReturnE orderReturnE){
		AddressE addressE = addressConvertor.orderReturnEToAddressE(orderReturnE);
		addressE.setProvince(orderReturnE.getProvinceName());
		addressE.setCity(orderReturnE.getCityName());
		addressE.setCounty(orderReturnE.getCountyName());
		addressE.setAddress(orderReturnE.getCustomAddress());
		addressE.setName(orderReturnE.getCustomName());
		addressE.setMobile(orderReturnE.getCustomMobile());
		addressE.setUserType((byte) 0);
		addressE.setAddressType((byte) 0);
		addressE.setRecordCode(orderReturnE.getRecordCode());
		addressMapper.saveAddress(addressE);
	}


	/**
	 * 组装下发库存中心的退货单数据
	 * @param returnDTO
	 * @param orderE
	 * @param returnDetailList
	 * @param realWarehouse
	 * @return
	 */
	private PushReturnDTO buildPushReturn(ReturnDTO returnDTO, OrderE orderE, List<ReturnDetailDTO> returnDetailList,
										  RealWarehouse realWarehouse) {
		PushReturnDTO pushReturnDTO = new PushReturnDTO();
		pushReturnDTO.setFactoryCode(realWarehouse.getFactoryCode());
		pushReturnDTO.setRealWarehouseOutCode(realWarehouse.getRealWarehouseOutCode());
		pushReturnDTO.setReservationNo(orderE.getOrderCode());
		pushReturnDTO.setSaleCode(returnDTO.getSaleCode());
		pushReturnDTO.setOutRecordCode(returnDTO.getAfterSaleCode());
		pushReturnDTO.setCustomName(returnDTO.getCustomName());
		pushReturnDTO.setCustomMobile(returnDTO.getCustomMobile());
		pushReturnDTO.setReason(returnDTO.getReason());
		pushReturnDTO.setExpressNo(returnDTO.getExpressNo());
		pushReturnDTO.setProvince(returnDTO.getProvinceName());
		pushReturnDTO.setProvinceCode(returnDTO.getProvinceCode());
		pushReturnDTO.setCity(returnDTO.getCityName());
		pushReturnDTO.setCityCode(returnDTO.getCityCode());
		pushReturnDTO.setCounty(returnDTO.getCountyName());
		pushReturnDTO.setCountyCode(returnDTO.getCountyCode());
		pushReturnDTO.setAddress(returnDTO.getCustomAddress());
		pushReturnDTO.setName(returnDTO.getCustomName());
		List<PushReturnDetailDTO> pushReturnDetailDTOList = new ArrayList<PushReturnDetailDTO>();
		returnDetailList.forEach(e -> {
			PushReturnDetailDTO pushReturnDetailDTO = new PushReturnDetailDTO();
			pushReturnDetailDTO.setSkuCode(e.getSkuCode());
			pushReturnDetailDTO.setSkuQty(e.getReturnQty());
			pushReturnDetailDTO.setUnit(e.getUnit());
			pushReturnDetailDTO.setUnitCode(e.getUnitCode());
			pushReturnDetailDTOList.add(pushReturnDetailDTO);
		});
		pushReturnDTO.setReturnDetails(pushReturnDetailDTOList);
		return pushReturnDTO;
	}

	/**
	 * 校验退货单详情商品基础单位
	 *
	 * @param returnDTO
	 */
	private void checkReturnDetailSkuCodeBaseUnit(ReturnDTO returnDTO) {
		List<ReturnDetailDTO> returnDetailList = returnDTO.getReturnDetailList();
		List<String> skuCodeList = returnDetailList.stream().map(ReturnDetailDTO :: getSkuCode).distinct().collect(Collectors.toList());
		List<ParamExtDTO> paramExtDTO = returnDetailList.stream().map(d -> {
			ParamExtDTO paramExt = new ParamExtDTO();
			paramExt.setMerchantId(merchantId);
			paramExt.setSkuCode(d.getSkuCode());
			return paramExt;
		}).collect(Collectors.toList());
		List<SkuUnitExtDTO> skuUnitExts = itemFacade.unitsBySkuCodeAndMerchantId(paramExtDTO);
		Map<String, String> skuBasisUnitExtsMap = skuUnitExts.stream()
				.filter(u -> u.getType().equals(Long.valueOf(SkuUnitTypeEnum.BASIS_UNIT.getId())))
				.collect(Collectors.toMap(SkuUnitExtDTO::getSkuCode, SkuUnitExtDTO::getUnitCode, (v1, v2) -> v1));
		if(skuCodeList.size() < skuBasisUnitExtsMap.size()) {
			throw new RomeException(ResCode.ORDER_ERROR_4010, ResCode.ORDER_ERROR_4010_DESC);
		}
		returnDetailList.forEach(e -> {
			if(!skuBasisUnitExtsMap.containsKey(e.getSkuCode())) {
				throw new RomeException(ResCode.ORDER_ERROR_4011, "商品[" +e.getSkuCode()+ "]不是基本单位");
			}
		});
	}

	/**
	 * 校验退货单详情商品是否存在
	 *
	 * @param returnDetailList
	 */
	private void checkReturnDetailSkuCodeExist(List<ReturnDetailDTO> returnDetailList) {
		List<String> skuCodeList = returnDetailList.stream().map(ReturnDetailDTO::getSkuCode).collect(Collectors.toList());
		List<SkuInfoExtDTO> skuInfoExts = itemFacade.skuListBySkuCodes(skuCodeList);
		if (skuInfoExts.size() < skuCodeList.size()) {
			throw new RomeException(ResCode.ORDER_ERROR_4010, ResCode.ORDER_ERROR_4010_DESC);
		}
	}

	/**
	 * 保存退货单、退货单详情
	 *
	 * @param returnDTO
	 * @param orderE
	 * @param orderDetailDOList
	 * @param returnDetailList
	 * @param realWarehouse
	 */
	private OrderReturnE saveOrderReturnWithDetail(ReturnDTO returnDTO, OrderE orderE, List<OrderDetailE> orderDetailEList, List<ReturnDetailDTO> returnDetailList,
												   RealWarehouse realWarehouse) {
		OrderReturnE orderReturnE = orderReturnConvert.convertDTO2E(returnDTO);
		String recordCode = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.RETURN_OUT_RECORD.getCode());
		orderReturnE.setRecordCode(recordCode);
		orderReturnE.setOrderCode(orderE.getOrderCode());
		orderReturnE.setOrderStatus(1);
		orderReturnE.setFactoryCode(realWarehouse.getFactoryCode());
		orderReturnE.setRealWarehouseCode(realWarehouse.getRealWarehouseOutCode());
		orderReturnMapper.insertOrderReturn(orderReturnE);

		List<OrderReturnDetailE> orderReturnDetailEList = orderReturnDetailConvert.convertDTOList2EList2(returnDetailList);
		orderReturnDetailEList.forEach(e -> {
			e.setAfterSaleCode(returnDTO.getAfterSaleCode());
			orderDetailEList.forEach(d -> {
				if(d.getSkuCode().equals(e.getSkuCode())) {
					e.setDeliveryQty(d.getOrderQty());
				}
			});
		});
		orderReturnDetailMapper.batchInsertOrderReturnDetail(orderReturnDetailEList);
		orderReturnE.setOrderReturnDetailEList(orderReturnDetailEList);
		return orderReturnE;
	}

	/**
	 * 校验退货单详情重复商品
	 *
	 * @param returnDetailList
	 */
	private void checkReturnDetailSkuCode(List<ReturnDetailDTO> returnDetailList) {
		List<String> skuCodeList = returnDetailList.stream().map(ReturnDetailDTO :: getSkuCode).distinct().collect(Collectors.toList());
		if(skuCodeList.size() < returnDetailList.size()) {
			throw new RomeException(ResCode.ORDER_ERROR_4004, ResCode.ORDER_ERROR_4004_DESC);
		}
	}

	/**
	 * 校验退货单详情退货数量
	 *
	 * @param saleCode
	 * @param returnDetailList
	 * @param orderDetailDOList
	 */
	private void checkReturnDetailReturnQty(String saleCode, List<ReturnDetailDTO> returnDetailList, List<OrderDetailE> orderDetailEList) {
		Map<String, OrderDetailDO> orderDetailDOMap = orderDetailEList.stream().collect(Collectors.toMap(OrderDetailE :: getSkuCode, Function.identity(), (v1, v2) -> v1));
		//根据销售单号查询售后单号列表
		List<Map<String, String>> afterSaleCodeList = orderReturnMapper.queryAfterSaleCodeListBySaleCode(saleCode);
		//接收交易中心退货单详情
		if(CollectionUtils.isEmpty(afterSaleCodeList)) {
			returnDetailList.forEach(e -> {
				if(orderDetailDOMap.containsKey(e.getSkuCode())) {
					//预约单下单数量
					BigDecimal orderQty = orderDetailDOMap.get(e.getSkuCode()).getOrderQty();
					if(orderQty.compareTo(e.getReturnQty()) == -1) {
						throw new RomeException(ResCode.ORDER_ERROR_4002, ResCode.ORDER_ERROR_4002_DESC);
					}
				} else {
					throw new RomeException(ResCode.ORDER_ERROR_4003, "商品[" +e.getSkuCode()+ "]不在退货范围");
				}
			});
		} else {
			//退货单单据状态待入库的售后单号列表
			List<String> afterSaleCodeList1 = new ArrayList<>();
			//退货单单据状态已入库的售后单号列表
			List<String> afterSaleCodeList2 = new ArrayList<>();
			for (Map<String, String> map : afterSaleCodeList) {
				if("1".equals(MapUtils.getString(map, "orderStatus"))) {
					afterSaleCodeList1.add(MapUtils.getString(map, "afterSaleCode"));
				}
				if("2".equals(MapUtils.getString(map, "orderStatus"))) {
					afterSaleCodeList2.add(MapUtils.getString(map, "afterSaleCode"));
				}
			}
			List<Map<String, String>> detailReturnQtyList = new ArrayList<Map<String, String>>();
			if(CollectionUtils.isNotEmpty(afterSaleCodeList1)) {
				//根据售后单号列表统计退货单详情skuCode退货数量
				detailReturnQtyList = orderReturnDetailMapper.countDetailReturnQty(afterSaleCodeList1);
			}
			List<Map<String, String>> detailEntryQtyList = new ArrayList<Map<String, String>>();
			if(CollectionUtils.isNotEmpty(afterSaleCodeList2)) {
				//根据售后单号列表统计skuCode退货单详情实际收货数量
				detailEntryQtyList = orderReturnDetailMapper.countDetailEntryQty(afterSaleCodeList2);
			}
			for (ReturnDetailDTO returnDetailDTO : returnDetailList) {
				//退货数量
				BigDecimal returnQty = returnDetailDTO.getReturnQty();
				BigDecimal tempQty = BigDecimal.ZERO;
				for (Map<String, String> map : detailReturnQtyList) {
					if(returnDetailDTO.getSkuCode().equals(MapUtils.getString(map, "skuCode"))) {
						tempQty = tempQty.add(new BigDecimal(MapUtils.getString(map, "qtySum")));
						break;
					}
				}
				for (Map<String, String> map : detailEntryQtyList) {
					if(returnDetailDTO.getSkuCode().equals(MapUtils.getString(map, "skuCode"))) {
						tempQty = tempQty.add(new BigDecimal(MapUtils.getString(map, "qtySum")));
					}
					break;
				}
				returnDetailDTO.setReturnQty(returnQty.add(tempQty));
			}
			returnDetailList.forEach(e -> {
				if(orderDetailDOMap.containsKey(e.getSkuCode())) {
					//预约单下单数量
					BigDecimal orderQty = orderDetailDOMap.get(e.getSkuCode()).getOrderQty();
					if(orderQty.compareTo(e.getReturnQty()) == -1) {
						throw new RomeException(ResCode.ORDER_ERROR_4002, ResCode.ORDER_ERROR_4002_DESC);
					}
				} else {
					throw new RomeException(ResCode.ORDER_ERROR_4003, "商品[" +e.getSkuCode()+ "]不在退货范围");
				}
			});
		}
	}

	/**
	 * 接收库存中心退货入库通知
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void receiveReturnNotice(ReturnNoticeDTO returnNoticeDTO) {
		//1、根据售后单查询退货单
		OrderReturnE orderReturnE = orderReturnMapper.queryOrderReturnByAfterSaleCode(returnNoticeDTO.getAfterSaleCode());
		if(orderReturnE == null) {
			throw new RomeException(ResCode.ORDER_ERROR_4007, ResCode.ORDER_ERROR_4007_DESC);
		}
		//2、获取退货单单据状态
		Integer orderStatus = orderReturnE.getOrderStatus();
		//已入库直接返回
		if(Integer.valueOf(2).equals(orderStatus)) {
			log.info("退货单状态已入库，售后单号[{}]",returnNoticeDTO.getAfterSaleCode());
			return;
		}
		//3、据售后单号查询退货 单详情列表
		List<OrderReturnDetailE> orderReturnDetailEList = orderReturnDetailMapper.queryOrderReturnDetailByAfterSaleCode(orderReturnE.getAfterSaleCode());
		if(CollectionUtils.isEmpty(orderReturnDetailEList)) {
			throw new RomeException(ResCode.ORDER_ERROR_4008, ResCode.ORDER_ERROR_4008_DESC);
		}
		List<ReturnDetailNoticeDTO> returnDetailNoticeList = returnNoticeDTO.getReturnDetailNoticeList();
		//4、校验退货入库通知详情重复商品
		this.checkReturnDetailNoticeSkuCode(returnDetailNoticeList);
		//5、校验退货入库通知详情商品是否存在
		this.checkReturnDetailNoticeSkuCodeExist(returnDetailNoticeList);
		//6、校验退货入库通知详情商品基础单位
		this.checkReturnDetailNoticeSkuCodeBaseUnit(returnDetailNoticeList);
		//7、校验退货入库通知详情实际收货数量
		this.checkReturnDetailNoticeEntryQty(returnDetailNoticeList, orderReturnDetailEList);
		//8、修改退货单、退货单详情
		this.updateOrderReturnWithDetail(returnNoticeDTO);
		//9、组装下发交易中心的退货入库通知数据
		PushReturnNoticeDTO pushReturnNoticeDTO = this.buildPushReturnNotice(returnNoticeDTO);
		//10、下发交易中心的退货入库通知数据给交易中心
		Response resp = null;
		try {
			resp = tradeFacade.reverseInBoundNotify(pushReturnNoticeDTO);
		} catch (Exception e) {
			log.error("交易中心退货通知失败");
		}
		if(resp != null && CommonConstants.CODE_SUCCESS.equals(resp.getCode())) {
			//根据售后单号、状态=2修改通知交易中心状态
			orderReturnMapper.updateSyncTradeStatus(returnNoticeDTO.getAfterSaleCode(), 2, 0);
		} else {
			//修改通知交易中心状态=1（待通知）
			orderReturnMapper.updateSyncTradeStatus(returnNoticeDTO.getAfterSaleCode(), 1, 0);
		}
	}

	/**
	 * 组装下发交易中心的退货入库通知数据
	 *
	 * @param returnNoticeDTO
	 * @return
	 */
	private PushReturnNoticeDTO buildPushReturnNotice(ReturnNoticeDTO returnNoticeDTO) {
		PushReturnNoticeDTO pushReturnNoticeDTO = new PushReturnNoticeDTO();
		pushReturnNoticeDTO.setReverseOrderNo(returnNoticeDTO.getAfterSaleCode());
		List<PushReturnDetailNoticeDTO> pushReturnDetailNoticeDTOList = new ArrayList<PushReturnDetailNoticeDTO>();
		returnNoticeDTO.getReturnDetailNoticeList().forEach(e -> {
			PushReturnDetailNoticeDTO pushReturnDetailNoticeDTO = new PushReturnDetailNoticeDTO();
			pushReturnDetailNoticeDTO.setSkuCode(e.getSkuCode());
			pushReturnDetailNoticeDTO.setSkuQty(e.getEntryQty());
			pushReturnDetailNoticeDTO.setSalesUnitName(e.getUnit());
			pushReturnDetailNoticeDTO.setSalesUnit(e.getUnitCode());
			pushReturnDetailNoticeDTOList.add(pushReturnDetailNoticeDTO);
		});
		pushReturnNoticeDTO.setItemList(pushReturnDetailNoticeDTOList);
		return pushReturnNoticeDTO;
	}

	/**
	 * 修改退货单、退货单详情
	 *
	 * @param returnNoticeDTO
	 */
	private void updateOrderReturnWithDetail(ReturnNoticeDTO returnNoticeDTO) {
		//根据售后单号修改退货单单据状态=2（已入库）
		int row = orderReturnMapper.updateOrderStatusByAfterSaleCode(returnNoticeDTO.getAfterSaleCode());
		if(row < 1) {
			throw new RomeException(ResCode.ORDER_ERROR_4015, ResCode.ORDER_ERROR_4015_DESC);
		}
		returnNoticeDTO.getReturnDetailNoticeList().forEach(e -> {
			//根据售后单号、商品编码修改实际收货数量
			int row1 = orderReturnDetailMapper.updateEntryQtyByAfterSaleCodeAndSkuCode(e.getEntryQty(), returnNoticeDTO.getAfterSaleCode(), e.getSkuCode());
			if(row1 < 1) {
				throw new RomeException(ResCode.ORDER_ERROR_4016, ResCode.ORDER_ERROR_4016_DESC);
			}
		});
	}

	/**
	 * 校验退货入库通知详情实际收货数量
	 *
	 * @param returnDetailNoticeList
	 * @param orderReturnDetailEList
	 */
	private void checkReturnDetailNoticeEntryQty(List<ReturnDetailNoticeDTO> returnDetailNoticeList,
												 List<OrderReturnDetailE> orderReturnDetailEList) {
		Map<String, OrderReturnDetailE> orderReturnDetailEMap = orderReturnDetailEList.stream().collect(Collectors.toMap(OrderReturnDetailE :: getSkuCode, Function.identity(), (v1, v2) -> v1));
		returnDetailNoticeList.forEach(e -> {
			if(orderReturnDetailEMap.containsKey(e.getSkuCode())) {
				//退货数量
				BigDecimal returnQty = orderReturnDetailEMap.get(e.getSkuCode()).getReturnQty();
				if(returnQty.compareTo(e.getEntryQty()) == -1) {
					throw new RomeException(ResCode.ORDER_ERROR_4012, ResCode.ORDER_ERROR_4012_DESC);
				}
			} else {
				throw new RomeException(ResCode.ORDER_ERROR_4003, "商品[" +e.getSkuCode()+ "]不在退货范围");
			}
		});
	}

	/**
	 * 校验退货入库通知详情商品是否存在
	 *
	 * @param returnDetailNoticeList
	 */
	private void checkReturnDetailNoticeSkuCodeExist(List<ReturnDetailNoticeDTO> returnDetailNoticeList) {
		List<String> skuCodeList = returnDetailNoticeList.stream().map(ReturnDetailNoticeDTO::getSkuCode).collect(Collectors.toList());
		List<SkuInfoExtDTO> skuInfoExts = itemFacade.skuListBySkuCodes(skuCodeList);
		if (skuInfoExts.size() < skuCodeList.size()) {
			throw new RomeException(ResCode.ORDER_ERROR_4010, ResCode.ORDER_ERROR_4010_DESC);
		}
	}

	/**
	 * 校验退货入库通知详情商品基础单位
	 *
	 * @param returnDetailNoticeList
	 */
	private void checkReturnDetailNoticeSkuCodeBaseUnit(List<ReturnDetailNoticeDTO> returnDetailNoticeList) {
		List<String> skuCodeList = returnDetailNoticeList.stream().map(ReturnDetailNoticeDTO :: getSkuCode).distinct().collect(Collectors.toList());
		List<ParamExtDTO> paramExtDTO = returnDetailNoticeList.stream().map(d -> {
			ParamExtDTO paramExt = new ParamExtDTO();
			paramExt.setMerchantId(merchantId);
			paramExt.setSkuCode(d.getSkuCode());
			return paramExt;
		}).collect(Collectors.toList());
		List<SkuUnitExtDTO> skuUnitExts = itemFacade.unitsBySkuCodeAndMerchantId(paramExtDTO);
		Map<String, String> skuBasisUnitExtsMap = skuUnitExts.stream()
				.filter(u -> u.getType().equals(Long.valueOf(SkuUnitTypeEnum.BASIS_UNIT.getId())))
				.collect(Collectors.toMap(SkuUnitExtDTO::getSkuCode, SkuUnitExtDTO::getUnitCode, (v1, v2) -> v1));
		if(skuCodeList.size() < skuBasisUnitExtsMap.size()) {
			throw new RomeException(ResCode.ORDER_ERROR_4010, ResCode.ORDER_ERROR_4010_DESC);
		}
		returnDetailNoticeList.forEach(e -> {
			if(!skuBasisUnitExtsMap.containsKey(e.getSkuCode())) {
				throw new RomeException(ResCode.ORDER_ERROR_4011, "商品[" +e.getSkuCode()+ "]不是基本单位");
			}
		});
	}

	/**
	 * 校验退货入库通知详情重复商品
	 *
	 * @param returnDetailNoticeList
	 */
	private void checkReturnDetailNoticeSkuCode(List<ReturnDetailNoticeDTO> returnDetailNoticeList) {
		List<String> skuCodeList = returnDetailNoticeList.stream().map(ReturnDetailNoticeDTO :: getSkuCode).distinct().collect(Collectors.toList());
		if(skuCodeList.size() < returnDetailNoticeList.size()) {
			throw new RomeException(ResCode.ORDER_ERROR_4009, ResCode.ORDER_ERROR_4009_DESC);
		}
	}

	/**
	 * 查询待推送给交易中心的退库单列表
	 */
	@Override
	public List<OrderReturnDTO> queryOrderReturnToTrade() {
		Date endTime = new Date();
		Date startTime = DateUtils.addDays(endTime,-30);
		return orderReturnConvert.convertEList2DTOList(orderReturnMapper.queryOrderReturnToTrade(startTime, endTime));
	}

	/**
	 * 组装下发库存中心的退货单数据（定时任务）
	 *
	 * @param orderReturnE
	 * @param orderReturnDetailEList
	 * @param realWarehouse
	 * @return
	 */
	private PushReturnDTO buildPushReturn(OrderReturnE orderReturnE, List<OrderReturnDetailE> orderReturnDetailEList,
										  RealWarehouse realWarehouse) {
		PushReturnDTO pushReturnDTO = new PushReturnDTO();
		pushReturnDTO.setFactoryCode(realWarehouse.getFactoryCode());
		pushReturnDTO.setRealWarehouseOutCode(realWarehouse.getRealWarehouseOutCode());
		pushReturnDTO.setReservationNo(orderReturnE.getOrderCode());
		pushReturnDTO.setSaleCode(orderReturnE.getSaleCode());
		pushReturnDTO.setOutRecordCode(orderReturnE.getAfterSaleCode());
		pushReturnDTO.setCustomName(orderReturnE.getCustomName());
		pushReturnDTO.setCustomMobile(orderReturnE.getCustomMobile());
		pushReturnDTO.setReason(orderReturnE.getReason());
		pushReturnDTO.setExpressNo(orderReturnE.getExpressNo());
		pushReturnDTO.setProvince(orderReturnE.getProvinceName());
		pushReturnDTO.setProvinceCode(orderReturnE.getProvinceCode());
		pushReturnDTO.setCity(orderReturnE.getCityName());
		pushReturnDTO.setCityCode(orderReturnE.getCityCode());
		pushReturnDTO.setCounty(orderReturnE.getCountyName());
		pushReturnDTO.setCountyCode(orderReturnE.getCountyCode());
		pushReturnDTO.setAddress(orderReturnE.getCustomAddress());
		pushReturnDTO.setName(orderReturnE.getCustomName());
		List<PushReturnDetailDTO> pushReturnDetailDTOList = new ArrayList<PushReturnDetailDTO>();
		orderReturnDetailEList.forEach(e -> {
			PushReturnDetailDTO pushReturnDetailDTO = new PushReturnDetailDTO();
			pushReturnDetailDTO.setSkuCode(e.getSkuCode());
			pushReturnDetailDTO.setSkuQty(e.getReturnQty());
			pushReturnDetailDTO.setUnit(e.getUnit());
			pushReturnDetailDTO.setUnitCode(e.getUnitCode());
			pushReturnDetailDTOList.add(pushReturnDetailDTO);
		});
		pushReturnDTO.setReturnDetails(pushReturnDetailDTOList);
		return pushReturnDTO;
	}

	/**
	 * 处理待推送给交易中心的退库单
	 */
	@Override
	public void handleOrderReturnToTrade(String afterSaleCode) {
		//1、根据售后单查询退货单
		OrderReturnE orderReturnE = orderReturnMapper.queryOrderReturnByAfterSaleCode(afterSaleCode);
		if(orderReturnE == null) {
			throw new RomeException(ResCode.ORDER_ERROR_4007, ResCode.ORDER_ERROR_4007_DESC);
		}
		//2、获取通知交易中心状态
		Integer syncTradeStatus = orderReturnE.getSyncTradeStatus();
		if(Integer.valueOf(0).equals(syncTradeStatus)) {
			throw new RomeException(ResCode.ORDER_ERROR_4019, ResCode.ORDER_ERROR_4019_DESC);
		}
		if(Integer.valueOf(2).equals(syncTradeStatus)) {
			return;
		}
		//3、据售后单号查询退货单详情列表
		List<OrderReturnDetailE> orderReturnDetailEList = orderReturnDetailMapper.queryOrderReturnDetailByAfterSaleCode(orderReturnE.getAfterSaleCode());
		if(CollectionUtils.isEmpty(orderReturnDetailEList)) {
			throw new RomeException(ResCode.ORDER_ERROR_4008, ResCode.ORDER_ERROR_4008_DESC);
		}
		//4、组装下发交易中心的退货入库通知数据
		PushReturnNoticeDTO pushReturnNoticeDTO = this.buildPushReturnNotice(orderReturnE, orderReturnDetailEList);
		//5、下发交易中心的退货入库通知数据给交易中心
		Response resp = null;
		try {
			resp = tradeFacade.reverseInBoundNotify(pushReturnNoticeDTO);
		} catch (Exception e) {
			log.error("交易中心退货通知失败");
		}
		if(resp != null && CommonConstants.CODE_SUCCESS.equals(resp.getCode())) {
			//根据售后单号、状态=2修改通知交易中心状态
			orderReturnMapper.updateSyncTradeStatus(orderReturnE.getAfterSaleCode(), 2, 1);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void receipt(StockNotifyDTO stockNotifyDTO) {
		OrderReturnE orderReturnE = this.queryWithDetailByRecordCode(stockNotifyDTO.getRecordCode());
		AlikAssert.isNotNull(orderReturnE, ResCode.ORDER_ERROR_4007, ResCode.ORDER_ERROR_4007_DESC );
		//2、获取退货单单据状态
		Integer orderStatus = orderReturnE.getOrderStatus();
		//已入库直接返回
		if(Integer.valueOf(OrderReturnConstants.STORAGED_STATUS).equals(orderStatus)) {
			log.info("退货单状态已入库，售后单号[{}]",stockNotifyDTO.getRecordCode());
			return;
		}
		List<OrderReturnDetailE> orderReturnDetailEList = orderReturnE.getOrderReturnDetailEList();
		if(CollectionUtils.isEmpty(orderReturnDetailEList)) {
			throw new RomeException(ResCode.ORDER_ERROR_4008, ResCode.ORDER_ERROR_4008_DESC);
		}
		List<StockNotifyDetailDTO> stockNotifyDetailDTOList = stockNotifyDTO.getDetailDTOList();
		//4、校验退货入库通知详情重复商品
		this.checkStockNotifyDetailSkuCode(stockNotifyDetailDTOList);
		//5、校验退货入库通知详情商品是否存在
		this.checkStockNotifyDetailSkuCodeExist(stockNotifyDetailDTOList);
		//6、校验退货入库通知详情商品基础单位
		this.checkStockNotifyDetailSkuCodeBaseUnit(stockNotifyDetailDTOList);
		//7、校验退货入库通知详情实际收货数量
		this.checkStockNotifyDetailEntryQty(stockNotifyDetailDTOList, orderReturnDetailEList);
		//8、修改退货单、退货单详情
		this.updateOrderReturnWithDetail(orderReturnE.getAfterSaleCode(), stockNotifyDTO);
		//9、组装下发交易中心的退货入库通知数据
		PushReturnNoticeDTO pushReturnNoticeDTO = this.buildPushReturnNotice(orderReturnE,stockNotifyDTO, orderReturnDetailEList);
		//10、下发交易中心的退货入库通知数据给交易中心
		Response resp = null;
		try {
			resp = tradeFacade.reverseInBoundNotify(pushReturnNoticeDTO);
		} catch (Exception e) {
			log.error("交易中心退货通知失败");
		}
		if(resp != null && CommonConstants.CODE_SUCCESS.equals(resp.getCode())) {
			//根据售后单号、状态=2修改通知交易中心状态
			orderReturnMapper.updateSyncTradeStatus(orderReturnE.getAfterSaleCode(), 2, 0);
		} else {
			//修改通知交易中心状态=1（待通知）
			orderReturnMapper.updateSyncTradeStatus(orderReturnE.getAfterSaleCode(), 1, 0);
		}
	}

	/**
	 * 组装下发交易中心的退货入库通知数据
	 * @param stockNotifyDTO
	 * @return
	 */
	private PushReturnNoticeDTO buildPushReturnNotice(OrderReturnE orderReturnE, StockNotifyDTO stockNotifyDTO, List<OrderReturnDetailE> orderReturnDetailEList) {
		Map<String, OrderReturnDetailE> orderReturnDetailEMap = orderReturnDetailEList.stream().collect(Collectors.toMap(OrderReturnDetailE :: getSkuCode, Function.identity(), (v1, v2) -> v1));
		PushReturnNoticeDTO pushReturnNoticeDTO = new PushReturnNoticeDTO();
		pushReturnNoticeDTO.setReverseOrderNo(orderReturnE.getAfterSaleCode());
		List<PushReturnDetailNoticeDTO> pushReturnDetailNoticeDTOList = new ArrayList<PushReturnDetailNoticeDTO>();
		stockNotifyDTO.getDetailDTOList().forEach(e -> {
			PushReturnDetailNoticeDTO pushReturnDetailNoticeDTO = new PushReturnDetailNoticeDTO();
			pushReturnDetailNoticeDTO.setSkuCode(e.getSkuCode());
			pushReturnDetailNoticeDTO.setSkuQty(e.getActualQty());
			pushReturnDetailNoticeDTO.setSalesUnitName(e.getUnit());
			if (orderReturnDetailEMap.containsKey(e.getSkuCode())) {
				String unitCode = orderReturnDetailEMap.get(e.getSkuCode()).getUnitCode();
				pushReturnDetailNoticeDTO.setSalesUnit(unitCode);
			}

			pushReturnDetailNoticeDTOList.add(pushReturnDetailNoticeDTO);
		});
		pushReturnNoticeDTO.setItemList(pushReturnDetailNoticeDTOList);
		return pushReturnNoticeDTO;
	}

	/**
	 * 修改退货单、退货单详情
	 *
	 * @param stockNotifyDTO
	 */
	private void updateOrderReturnWithDetail(String afterSaleCode, StockNotifyDTO stockNotifyDTO) {
		//根据售后单号修改退货单单据状态=2（已入库）
		int row = orderReturnMapper.updateOrderStatusByAfterSaleCode(afterSaleCode);
		if(row < 1) {
			throw new RomeException(ResCode.ORDER_ERROR_4015, ResCode.ORDER_ERROR_4015_DESC);
		}
		stockNotifyDTO.getDetailDTOList().forEach(e -> {
			//根据售后单号、商品编码修改实际收货数量
			int row1 = orderReturnDetailMapper.updateEntryQtyByAfterSaleCodeAndSkuCode(e.getActualQty(), afterSaleCode, e.getSkuCode());
			if(row1 < 1) {
				throw new RomeException(ResCode.ORDER_ERROR_4016, ResCode.ORDER_ERROR_4016_DESC);
			}
			//
		});
	}


	/**
	 * 校验退货入库通知详情实际收货数量
	 *
	 * @param stockNotifyDetailDTOList
	 * @param orderReturnDetailEList
	 */
	private void checkStockNotifyDetailEntryQty(List<StockNotifyDetailDTO> stockNotifyDetailDTOList,
												List<OrderReturnDetailE> orderReturnDetailEList) {
		Map<String, OrderReturnDetailE> orderReturnDetailEMap = orderReturnDetailEList.stream().collect(Collectors.toMap(OrderReturnDetailE :: getSkuCode, Function.identity(), (v1, v2) -> v1));
		stockNotifyDetailDTOList.forEach(e -> {
			if(orderReturnDetailEMap.containsKey(e.getSkuCode())) {
				//退货数量
				BigDecimal returnQty = orderReturnDetailEMap.get(e.getSkuCode()).getReturnQty();
				if(returnQty.compareTo(e.getActualQty()) == -1) {
					throw new RomeException(ResCode.ORDER_ERROR_4012, ResCode.ORDER_ERROR_4012_DESC);
				}
			} else {
				throw new RomeException(ResCode.ORDER_ERROR_4003, "商品[" +e.getSkuCode()+ "]不在退货范围");
			}
		});
	}

	/**
	 * 校验退货入库通知详情商品基础单位
	 * @param stockNotifyDetailDTOList
	 */
	private void checkStockNotifyDetailSkuCodeBaseUnit(List<StockNotifyDetailDTO> stockNotifyDetailDTOList) {
		List<String> skuCodeList = stockNotifyDetailDTOList.stream().map(StockNotifyDetailDTO :: getSkuCode).distinct().collect(Collectors.toList());
		List<ParamExtDTO> paramExtDTO = stockNotifyDetailDTOList.stream().map(d -> {
			ParamExtDTO paramExt = new ParamExtDTO();
			paramExt.setMerchantId(merchantId);
			paramExt.setSkuCode(d.getSkuCode());
			return paramExt;
		}).collect(Collectors.toList());
		List<SkuUnitExtDTO> skuUnitExts = itemFacade.unitsBySkuCodeAndMerchantId(paramExtDTO);
		Map<String, String> skuBasisUnitExtsMap = skuUnitExts.stream()
				.filter(u -> u.getType().equals(Long.valueOf(SkuUnitTypeEnum.BASIS_UNIT.getId())))
				.collect(Collectors.toMap(SkuUnitExtDTO::getSkuCode, SkuUnitExtDTO::getUnitCode, (v1, v2) -> v1));
		if(skuCodeList.size() < skuBasisUnitExtsMap.size()) {
			throw new RomeException(ResCode.ORDER_ERROR_4010, ResCode.ORDER_ERROR_4010_DESC);
		}
		stockNotifyDetailDTOList.forEach(e -> {
			if(!skuBasisUnitExtsMap.containsKey(e.getSkuCode())) {
				throw new RomeException(ResCode.ORDER_ERROR_4011, "商品[" +e.getSkuCode()+ "]不是基本单位");
			}
		});
	}

	/**
	 * 校验退货入库通知详情商品是否存在
	 *
	 * @param stockNotifyDetailDTOList
	 */
	private void checkStockNotifyDetailSkuCodeExist(List<StockNotifyDetailDTO> stockNotifyDetailDTOList) {
		List<String> skuCodeList = stockNotifyDetailDTOList.stream().map(StockNotifyDetailDTO::getSkuCode).collect(Collectors.toList());
		List<SkuInfoExtDTO> skuInfoExts = itemFacade.skuListBySkuCodes(skuCodeList);
		if (skuInfoExts.size() < skuCodeList.size()) {
			throw new RomeException(ResCode.ORDER_ERROR_4010, ResCode.ORDER_ERROR_4010_DESC);
		}
	}

	/**
	 * 校验退货入库通知详情重复商品
	 * @param stockNotifyDetailDTOList
	 */
	private void checkStockNotifyDetailSkuCode(List<StockNotifyDetailDTO> stockNotifyDetailDTOList) {
		List<String> skuCodeList = stockNotifyDetailDTOList.stream().map(StockNotifyDetailDTO :: getSkuCode).distinct().collect(Collectors.toList());
		if(skuCodeList.size() < stockNotifyDetailDTOList.size()) {
			throw new RomeException(ResCode.ORDER_ERROR_4009, ResCode.ORDER_ERROR_4009_DESC);
		}
	}

	/**
	 * 根据出入库单据编号查询仓库调拨单（包含明细）
	 */
	private OrderReturnE queryWithDetailByRecordCode(String recordCode) {
		String frontRecord = frontWarehouseRecordRelationMapper.getFrontRecordCodeByRecordCode(recordCode);
		if (StringUtils.isBlank(frontRecord)) {
			return null;
		}
		OrderReturnE orderReturnE = orderReturnMapper.queryReturnByRecordCode(frontRecord);
		if (orderReturnE == null) {
			return null;
		}
		List<OrderReturnDetailE> orderReturnDetailES = orderReturnDetailMapper.queryOrderReturnDetailByAfterSaleCode(orderReturnE.getAfterSaleCode());
		if (CollectionUtils.isEmpty(orderReturnDetailES)) {
			return null;
		}
		orderReturnE.setOrderReturnDetailEList(orderReturnDetailES);
		return orderReturnE;
	}

	/**
	 * 退货单参数校验
	 * @param stockNotifyDTO
	 */
	private void validateReturnParam(StockNotifyDTO stockNotifyDTO) {
		if(StringUtils.isBlank(stockNotifyDTO.getRecordCode())) {
			throw new RomeException(ResCode.ORDER_ERROR_1002,ResCode.ORDER_ERROR_1002_DESC);
		}
		List<StockNotifyDetailDTO> detailDTOList = stockNotifyDTO.getDetailDTOList();
		if(CollectionUtils.isEmpty(detailDTOList)) {
			throw new RomeException(ResCode.ORDER_ERROR_1002,ResCode.ORDER_ERROR_1002_DESC);
		}
		detailDTOList.forEach(e -> {
			if(StringUtils.isBlank(e.getSkuCode()) || e.getActualQty() == null) {
				throw new RomeException(ResCode.ORDER_ERROR_1002,ResCode.ORDER_ERROR_1002_DESC);
			}
		});
	}

	/**
	 * 校验退货单详情实际收货数量
	 *
	 * @param orderReturnE
	 * @param orderReturnDetailES
	 */
	private void checkReturnDetailActualQty(OrderReturnE orderReturnE, List<OrderReturnDetailE> orderReturnDetailES) {
		Map<String, OrderReturnDetailE> returnDetailMap = orderReturnE.getOrderReturnDetailEList().stream().collect(Collectors.toMap(OrderReturnDetailE :: getSkuCode, Function.identity(), (v1, v2) -> v1));
		orderReturnDetailES.forEach(e -> {
			if(returnDetailMap.containsKey(e.getSkuCode())) {
				//实际退货数量
				BigDecimal actualQty = returnDetailMap.get(e.getSkuCode()).getEntryQty();
				if(e.getReturnQty().compareTo(actualQty) == -1) {
					throw new RomeException(ResCode.ORDER_ERROR_4012, "商品【"+ e.getSkuCode() +"】" + ResCode.ORDER_ERROR_4012_DESC);
				} else {
					e.setEntryQty(actualQty);
				}
			} else {
				e.setEntryQty(e.getReturnQty());
			}
		});
	}

	/**
	 * 组装下发交易中心的退货入库通知数据（定时任务）
	 *
	 * @param orderReturnE
	 * @param orderReturnDetailEList
	 * @return
	 */
	private PushReturnNoticeDTO buildPushReturnNotice(OrderReturnE orderReturnE, List<OrderReturnDetailE> orderReturnDetailEList) {
		PushReturnNoticeDTO pushReturnNoticeDTO = new PushReturnNoticeDTO();
		pushReturnNoticeDTO.setReverseOrderNo(orderReturnE.getAfterSaleCode());
		List<PushReturnDetailNoticeDTO> pushReturnDetailNoticeDTOList = new ArrayList<PushReturnDetailNoticeDTO>();
		orderReturnDetailEList.forEach(e -> {
			PushReturnDetailNoticeDTO pushReturnDetailNoticeDTO = new PushReturnDetailNoticeDTO();
			pushReturnDetailNoticeDTO.setSkuCode(e.getSkuCode());
			pushReturnDetailNoticeDTO.setSkuQty(e.getEntryQty());
			pushReturnDetailNoticeDTO.setSalesUnitName(e.getUnit());
			pushReturnDetailNoticeDTO.setSalesUnit(e.getUnitCode());
			pushReturnDetailNoticeDTOList.add(pushReturnDetailNoticeDTO);
		});
		pushReturnNoticeDTO.setItemList(pushReturnDetailNoticeDTOList);
		return pushReturnNoticeDTO;
	}

}