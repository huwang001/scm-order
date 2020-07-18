package com.lyf.scm.core.remote.item;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.remote.item.dto.SkuInfoExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuQtyUnitBase;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.rome.arch.core.exception.RomeException;

/**
 * 商品信息
 * 
 * @author
 */
@Component
public class ItemInfoTool {

	@Resource
	private ItemFacade itemFacade;

	/**
	 * 设置skuCode或者skuId
	 * 
	 * @param skuList
	 */
	public void convertSkuCode(List<? extends SkuQtyUnitBase> skuList) {
		// 判断是否有skuCode,没有则取商品中心获取并设置
		List<Long> skuIds = skuList.stream().filter(sku -> StringUtils.isBlank(sku.getSkuCode()))
				.map(SkuQtyUnitBase::getSkuId).collect(Collectors.toList());
		if (skuIds != null && skuIds.size() > 0) {
			List<SkuInfoExtDTO> list = itemFacade.skuBySkuIds(skuIds);
			for (SkuInfoExtDTO skuInfo : list) {
				skuList.stream().filter(sku -> sku.getSkuId().equals(skuInfo.getId())).forEach(sku -> {
					sku.setSkuCode(skuInfo.getSkuCode());
					sku.setContainer(skuInfo.getContainer());
				});
			}
		}
		// 判断是否有skuId,没有则取商品中心获取并设置
		List<String> skuCodes = skuList.stream().filter(sku -> sku.getSkuId() == null || sku.getSkuId() == 0)
				.map(SkuQtyUnitBase::getSkuCode).collect(Collectors.toList());
		if (skuCodes != null && skuCodes.size() > 0) {
			List<SkuInfoExtDTO> list = itemFacade.skuBySkuCodes(skuCodes);
			for (SkuInfoExtDTO skuInfo : list) {
				skuList.stream().filter(sku -> sku.getSkuCode().equals(skuInfo.getSkuCode())).forEach(sku -> {
					sku.setSkuId(skuInfo.getId());
					sku.setContainer(skuInfo.getContainer());
				});
			}
		}
		skuList.forEach(sku -> {
			if (sku.getSkuId() == null || sku.getSkuId() == 0) {
				throw new RomeException(ResCode.ORDER_ERROR_6009, ResCode.ORDER_ERROR_6009_DESC + sku.getSkuCode());
			}
			if (StringUtils.isBlank(sku.getSkuCode())) {
				throw new RomeException(ResCode.ORDER_ERROR_6010, ResCode.ORDER_ERROR_6010_DESC + sku.getSkuId());
			}
		});
	}

	/**
	 * 设置skuCode或者skuId（过滤商品中心没有商品）
	 * 
	 * @param skuList
	 */
	public void convertSkuCodeFilter(List<? extends SkuQtyUnitBase> skuList) {
		// 判断是否有skuCode,没有则取商品中心获取并设置
		List<Long> skuIds = skuList.stream().filter(sku -> StringUtils.isBlank(sku.getSkuCode()))
				.map(SkuQtyUnitBase::getSkuId).collect(Collectors.toList());
		if (skuIds != null && skuIds.size() > 0) {
			List<SkuInfoExtDTO> list = itemFacade.skuBySkuIds(skuIds);
			for (SkuInfoExtDTO skuInfo : list) {
				skuList.stream().filter(sku -> sku.getSkuId().equals(skuInfo.getId())).forEach(sku -> {
					sku.setSkuCode(skuInfo.getSkuCode());
					sku.setContainer(skuInfo.getContainer());
				});
			}
		}
		// 判断是否有skuId,没有则取商品中心获取并设置
		List<String> skuCodes = skuList.stream().filter(sku -> sku.getSkuId() == null || sku.getSkuId() == 0)
				.map(SkuQtyUnitBase::getSkuCode).collect(Collectors.toList());
		if (skuCodes != null && skuCodes.size() > 0) {
			List<SkuInfoExtDTO> list = itemFacade.skuBySkuCodes(skuCodes);
			for (SkuInfoExtDTO skuInfo : list) {
				skuList.stream().filter(sku -> sku.getSkuCode().equals(skuInfo.getSkuCode())).forEach(sku -> {
					sku.setSkuId(skuInfo.getId());
					sku.setContainer(skuInfo.getContainer());
				});
			}
		}
		skuList.removeIf(sku -> sku.getSkuId() == null || sku.getSkuId() == 0 || StringUtils.isBlank(sku.getSkuCode()));
	}

}
