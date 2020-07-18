package com.lyf.scm.core.remote.item;

import static java.math.BigDecimal.ROUND_DOWN;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lyf.scm.common.constants.CommonConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.remote.item.dto.ParamExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuIdUnitCodeDTO;
import com.lyf.scm.core.remote.item.dto.SkuQtyUnitBase;
import com.lyf.scm.core.remote.item.dto.SkuQtyUnitDTO;
import com.lyf.scm.core.remote.item.dto.SkuUnitExtDTO;
import com.lyf.scm.core.remote.item.dto.UnitAndBaseUnitInfoDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.rome.arch.core.exception.RomeException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Component
public class SkuQtyUnitTool {

	@Resource
	private ItemFacade itemFacade;

	@Value("${app.merchantId}")
    private Long merchantId;

	/**
	 * 根据sku明细，批量将sku实际单位及数量转换为库存基础单位及数量
	 * 将转换结果分别存储至basicSkuQty、basicUnit、basicUnitCode
	 * 
	 * @param squList
	 *            sku明细
	 */
	public void convertRealToBasic(List<? extends SkuQtyUnitBase> squList) {
		convertRealToBasic(squList, merchantId);
	}

	/**
	 * 根据sku明细，批量将sku实际单位及数量转换为库存基础单位及数量
	 * 将转换结果分别存储至basicSkuQty、basicUnit、basicUnitCode
	 * 
	 * @param squList
	 *            sku明细
	 */
	public void convertRealToBasic(List<? extends SkuQtyUnitBase> squList, Long merchantId) {
		Map<SkuIdUnitCode, SkuUnitExtDTO> results = wrapSkuUnitResults(squList, merchantId);
		Iterator<? extends SkuQtyUnitBase> iterator = squList.iterator();
		while (iterator.hasNext()) {
			SkuQtyUnitBase squ = iterator.next();
			SkuUnitExtDTO sue = results.get(new SkuIdUnitCode(squ.getSkuId(), squ.getUnitCode()));
			// 脏数据过滤，如果该条商品查询结果有误，则直接从结果集中剔除
			if (null == sue || BigDecimal.ZERO.compareTo(sue.getScale()) == 0) {
				continue;
			}
			BigDecimal basicSkuQty = squ.getSkuQty().multiply(sue.getScale())
					.setScale(CommonConstants.DECIMAL_POINT_NUM, ROUND_DOWN);
			squ.setScale(sue.getScale());
			squ.setUnit(sue.getUnitName());
			squ.setBasicSkuQty(basicSkuQty);
			squ.setBasicUnit(sue.getBasicUnitName());
			squ.setBasicUnitCode(sue.getBasicUnitCode());
		}
	}

	/**
	 * 根据sku明细，只需要得到换算比例，用于将实际收货数量的基本单位换算成采购单位 无需计算
	 *
	 * @param squList
	 *            sku明细
	 */
	public void setRealToBasicScale(List<? extends SkuQtyUnitBase> squList) {
		this.setRealToBasicScale(squList, merchantId);
	}

	/**
	 * 根据sku明细，只需要得到换算比例，用于将实际收货数量的基本单位换算成采购单位 无需计算
	 *
	 * @param squList
	 *            sku明细
	 */
	public void setRealToBasicScale(List<? extends SkuQtyUnitBase> squList, Long merchantId) {
		Map<SkuIdUnitCode, SkuUnitExtDTO> results = wrapSkuUnitResults(squList, merchantId);
		Iterator<? extends SkuQtyUnitBase> iterator = squList.iterator();
		while (iterator.hasNext()) {
			SkuQtyUnitBase squ = iterator.next();
			SkuUnitExtDTO sue = results.get(new SkuIdUnitCode(squ.getSkuId(), squ.getUnitCode()));
			// 脏数据过滤，如果该条商品查询结果有误，则直接从结果集中剔除
			if (null == sue || BigDecimal.ZERO.compareTo(sue.getScale()) == 0) {
				continue;
			}
			squ.setScale(sue.getScale());
		}
	}

	/**
	 * 根据sku明细，批量将库存基础单位及数量转换为sku实际单位及数量
	 * 将转换结果分别存储至basicSkuQty、basicUnit、basicUnitCode
	 * 
	 * @param squList
	 *            sku明细
	 */
	public void convertBasicToReal(List<? extends SkuQtyUnitBase> squList) {
		this.convertBasicToReal(squList, merchantId);
	}

	/**
	 * 根据sku明细，批量将库存基础单位及数量转换为sku实际单位及数量
	 * 将转换结果分别存储至basicSkuQty、basicUnit、basicUnitCode
	 * 
	 * @param squList
	 *            sku明细
	 */
	public void convertBasicToReal(List<? extends SkuQtyUnitBase> squList, Long merchantId) {
		Map<SkuIdUnitCode, SkuUnitExtDTO> results = wrapSkuUnitResults(squList, merchantId);
		Iterator<? extends SkuQtyUnitBase> iterator = squList.iterator();
		while (iterator.hasNext()) {
			SkuQtyUnitBase squ = iterator.next();
			SkuUnitExtDTO sue = results.get(new SkuIdUnitCode(squ.getSkuId(), squ.getUnitCode()));
			// 脏数据过滤，如果该条商品查询结果有误，则直接从结果集中剔除
			if (null == sue || BigDecimal.ZERO.compareTo(sue.getScale()) == 0) {
				continue;
			}
			BigDecimal realSkuQty = squ.getBasicSkuQty().divide(sue.getScale(), CommonConstants.DECIMAL_POINT_NUM,
					ROUND_DOWN);
			squ.setScale(sue.getScale());
			squ.setSkuQty(realSkuQty);
			squ.setUnit(sue.getUnitName());
			squ.setUnitCode(sue.getUnitCode());
			squ.setBasicUnit(sue.getBasicUnitName());
			squ.setBasicUnitCode(sue.getBasicUnitCode());
		}
	}

	/**
	 * 根据sku明细，批量将库存基础单位及数量转换为sku实际单位及数量
	 * 将转换结果分别存储至basicSkuQty、basicUnit、basicUnitCode
	 * 
	 * @param squList
	 *            sku明细
	 */
	public void convertBasicToRealWithRemove(List<? extends SkuQtyUnitBase> squList) {
		this.convertBasicToRealWithRemove(squList, merchantId);
	}

	/**
	 * 根据sku明细，批量将库存基础单位及数量转换为sku实际单位及数量
	 * 将转换结果分别存储至basicSkuQty、basicUnit、basicUnitCode
	 * 
	 * @param squList
	 *            sku明细
	 */
	public void convertBasicToRealWithRemove(List<? extends SkuQtyUnitBase> squList, Long merchantId) {
		Map<SkuIdUnitCode, SkuUnitExtDTO> results = wrapSkuUnitResults(squList, merchantId);
		Iterator<? extends SkuQtyUnitBase> iterator = squList.iterator();
		while (iterator.hasNext()) {
			SkuQtyUnitBase squ = iterator.next();
			SkuUnitExtDTO sue = results.get(new SkuIdUnitCode(squ.getSkuId(), squ.getUnitCode()));
			// 脏数据过滤，如果该条商品查询结果有误，则直接从结果集中剔除
			if (null == sue || BigDecimal.ZERO.compareTo(sue.getScale()) == 0) {
				iterator.remove();
				continue;
			}
			BigDecimal realSkuQty = squ.getBasicSkuQty().divide(sue.getScale(), CommonConstants.DECIMAL_POINT_NUM,
					ROUND_DOWN);
			squ.setScale(sue.getScale());
			squ.setSkuQty(realSkuQty);
			squ.setUnit(sue.getUnitName());
			squ.setUnitCode(sue.getUnitCode());
			squ.setBasicSkuQty(squ.getBasicSkuQty());
			squ.setBasicUnit(sue.getBasicUnitName());
			squ.setBasicUnitCode(sue.getBasicUnitCode());
		}
	}

	/**
	 * 包装商品批量查询结果集
	 * 
	 * @param squList
	 * @return
	 */
	private Map<SkuIdUnitCode, SkuUnitExtDTO> wrapSkuUnitResults(List<? extends SkuQtyUnitBase> squList,
			Long merchantId) {
		List<ParamExtDTO> speList = new ArrayList<>();
		ParamExtDTO spe;
		for (SkuQtyUnitBase squ : squList) {
			if (null != squ.getSkuId() && StringUtils.isNotBlank(squ.getUnitCode())) {
				spe = new ParamExtDTO();
				spe.setSkuId(squ.getSkuId());
				spe.setUnitCode(squ.getUnitCode());
				spe.setMerchantId(merchantId);
				speList.add(spe);
			}
		}
		List<SkuUnitExtDTO> sueList = itemFacade.unitsBySkuIdAndUnitCode(speList, merchantId);
		Map<SkuIdUnitCode, SkuUnitExtDTO> resultMap = new HashMap<>();
		if (null == sueList) {
			return resultMap;
		}
		for (SkuUnitExtDTO sue : sueList) {
			resultMap.put(new SkuIdUnitCode(sue.getSkuId(), sue.getUnitCode()), sue);
		}
		return resultMap;
	}

	/**
	 * 查询sku基础单位 将转换结果分别存储至basicSkuQty、basicUnit、basicUnitCode
	 * 
	 * @param squList
	 *            sku明细
	 */
	public void queryBasicUnit(List<? extends SkuQtyUnitBase> squList) {
		this.queryBasicUnit(squList, merchantId);
	}

	/**
	 * 查询sku基础单位，不改变原有单位 将转换结果分别存储至basicSkuQty、basicUnit、basicUnitCode
	 * 
	 * @param squList
	 *            sku明细
	 */
	public void queryBasicUnitWithNoChange(List<? extends SkuQtyUnitBase> squList) {
		Map<SkuIdUnitCode, SkuUnitExtDTO> results = wrapSkuUnitResults(squList, merchantId);
		Iterator<? extends SkuQtyUnitBase> iterator = squList.iterator();
		while (iterator.hasNext()) {
			SkuQtyUnitBase squ = iterator.next();
			SkuUnitExtDTO sue = results.get(new SkuIdUnitCode(squ.getSkuId(), squ.getUnitCode()));
			squ.setUnit(sue.getUnitName());
			squ.setUnitCode(sue.getUnitCode());
			squ.setScale(sue.getScale());
			squ.setBasicUnit(sue.getBasicUnitName());
			squ.setBasicUnitCode(sue.getBasicUnitCode());
		}
	}

	/**
	 * 查询sku基础单位 将转换结果分别存储至basicSkuQty、basicUnit、basicUnitCode
	 * 
	 * @param squList
	 *            sku明细
	 */
	public void queryBasicUnit(List<? extends SkuQtyUnitBase> squList, Long merchantId) {
		// 获取skuCode列表
		List<String> skuCodeList = squList.stream().map(SkuQtyUnitBase::getSkuCode).collect(Collectors.toList());
		List<SkuUnitExtDTO> skuList = itemFacade.querySkuUnits(skuCodeList);
		if (skuList == null || skuList.size() == 0) {
			throw new RomeException(ResCode.ORDER_ERROR_6013, ResCode.ORDER_ERROR_6013_DESC);
		}
		Iterator<? extends SkuQtyUnitBase> iterator = squList.iterator();
		while (iterator.hasNext()) {
			SkuQtyUnitBase skuQty = iterator.next();
			SkuUnitExtDTO skuUnitDTO = skuList.stream()
					.filter(sku -> sku.getSkuId().equals(skuQty.getSkuId()) && sku.getType() == 5).findFirst()
					.orElse(null);
			if (skuUnitDTO == null) {
				iterator.remove();
				continue;
			}
			skuQty.setUnit(skuUnitDTO.getUnitName());
			skuQty.setUnitCode(skuUnitDTO.getUnitCode());
			skuQty.setScale(skuUnitDTO.getScale());
			skuQty.setBasicUnit(skuUnitDTO.getUnitName());
			skuQty.setBasicUnitCode(skuUnitDTO.getUnitCode());
			skuQty.setBasicSkuQty(skuQty.getSkuQty());
		}
	}

	/**
	 * 查询sku基础单位 商品中心无基础单位赋值为空字符串
	 * 
	 * @param squList
	 *            sku明细
	 */
	public void queryBasicUnitNoChecked(List<? extends SkuQtyUnitBase> squList) {
		this.queryBasicUnitNoChecked(squList, merchantId);
	}

	/**
	 * 查询sku基础单位 商品中心无基础单位赋值为空字符串
	 * 
	 * @param squList
	 *            sku明细
	 */
	public void queryBasicUnitNoChecked(List<? extends SkuQtyUnitBase> squList, Long merchantId) {
		// 获取skuCode列表
		List<String> skuCodeList = squList.stream().map(SkuQtyUnitBase::getSkuCode).collect(Collectors.toList());
		List<SkuUnitExtDTO> skuList = itemFacade.querySkuUnits(skuCodeList);
		if (skuList == null || skuList.size() == 0) {
			throw new RomeException(ResCode.ORDER_ERROR_6013, ResCode.ORDER_ERROR_6013_DESC);
		}
		Iterator<? extends SkuQtyUnitBase> iterator = squList.iterator();
		while (iterator.hasNext()) {
			SkuQtyUnitBase skuQty = iterator.next();
			SkuUnitExtDTO skuUnitDTO = skuList.stream()
					.filter(sku -> sku.getSkuId().equals(skuQty.getSkuId()) && sku.getType() == 5).findFirst()
					.orElse(null);
			if (skuUnitDTO == null) { 
				skuQty.setUnit("");
				skuQty.setUnitCode("");
				skuQty.setBasicSkuQty(skuQty.getSkuQty());
				continue;
			}
			skuQty.setUnit(skuUnitDTO.getUnitName());
			skuQty.setUnitCode(skuUnitDTO.getUnitCode());
			skuQty.setBasicSkuQty(skuQty.getSkuQty());
		}
	}

	/**
	 * 根据sku明细，批量将库存基础单位及数量转换为sku实际单位及数量
	 * 将转换结果分别存储至basicSkuQty、basicUnit、basicUnitCode
	 * 
	 * @param squList
	 *            sku明细
	 */
	public void convertBasicForQueryStock(List<SkuQtyUnitDTO> squList,
			Map<SkuIdUnitCodeDTO, UnitAndBaseUnitInfoDTO> unitMap) {
		Iterator<? extends SkuQtyUnitBase> iterator = squList.iterator();
		while (iterator.hasNext()) {
			SkuQtyUnitBase squ = iterator.next();
			UnitAndBaseUnitInfoDTO sue = unitMap.get(new SkuIdUnitCodeDTO(squ.getSkuId(), squ.getUnitCode()));
			// 脏数据过滤，如果该条商品查询结果有误，则直接从结果集中剔除
			if (null == sue || BigDecimal.ZERO.compareTo(sue.getScale()) == 0) {
				iterator.remove();
				continue;
			}
			BigDecimal realSkuQty = squ.getBasicSkuQty().divide(sue.getScale(), CommonConstants.DECIMAL_POINT_NUM,
					ROUND_DOWN);
			squ.setScale(sue.getScale());
			squ.setSkuQty(realSkuQty);
			squ.setUnit(sue.getUnitName());
			squ.setUnitCode(sue.getUnitCode());
			squ.setBasicSkuQty(squ.getBasicSkuQty());
			squ.setBasicUnit(sue.getBasicUnitName());
			squ.setBasicUnitCode(sue.getBasicUnitCode());
		}
	}

	@Data
	@AllArgsConstructor
	@EqualsAndHashCode(callSuper = false)
	public class SkuIdUnitCode {

		private Long skuId;

		private String unitCode;

	}
	
	/**
	 * 指定单位向上/向下取整后再转换成基础单位
	 * 
	 * @param squList
	 * @param type	单位类型
	 */
	public void convertRealToBasicSku(List<? extends SkuQtyUnitBase> squList, Long type, int upOrDown) {
		this.convertRealToBasicSku(squList, type, upOrDown, merchantId);
	}
	
	/**
	 * 指定单位向上取整后再转换成基础单位及数量
	 * 
	 * @param squList
	 * @param type
	 * @param merchantId
	 */
	public void convertRealToBasicSku(List<? extends SkuQtyUnitBase> squList, Long type, int upOrDown, Long merchantId) {
		List<String> skuCodeList = squList.stream().map(SkuQtyUnitBase :: getSkuCode).collect(Collectors.toList());
		List<SkuUnitExtDTO> skuList = itemFacade.querySkuUnits(skuCodeList);
		if (skuList == null || skuList.size() == 0) {
			throw new RomeException(ResCode.ORDER_ERROR_6013, ResCode.ORDER_ERROR_6013_DESC);
		}
		Iterator<? extends SkuQtyUnitBase> iterator = squList.iterator();
		while (iterator.hasNext()) {
			SkuQtyUnitBase skuQty = iterator.next();
			SkuUnitExtDTO sue = skuList.stream().filter(sku -> sku.getSkuUnitCodeByPrimaryKey().equals(skuQty.getKey())).findFirst().orElse(null);
			SkuUnitExtDTO skuUnitDTO = skuList.stream().filter(sku -> sku.getSkuCode().equals(skuQty.getSkuCode()) && sku.getType() == type).findFirst().orElse(null);
			if (sue == null || skuUnitDTO == null) {
				throw new RomeException(ResCode.ORDER_ERROR_6013, ResCode.ORDER_ERROR_6013_DESC);
			}
			BigDecimal realSkuQty = skuQty.getSkuQty().divide(skuUnitDTO.getScale(), 0, upOrDown);
			BigDecimal basicSkuQty = realSkuQty.multiply(skuUnitDTO.getScale()).setScale(CommonConstants.DECIMAL_POINT_NUM, BigDecimal.ROUND_DOWN);
			skuQty.setScale(sue.getScale());
			skuQty.setBasicUnit(sue.getBasicUnitName());
			skuQty.setBasicUnitCode(sue.getBasicUnitCode());
			skuQty.setBasicSkuQty(basicSkuQty);
		}
	}

}