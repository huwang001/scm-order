package com.lyf.scm.core.remote.item.dto;

import lombok.Data;
import org.springframework.boot.convert.DataSizeUnit;

import java.util.List;

/**
 * 查询返回组合商品对象
 *
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/9
 */
@Data
public class SkuUnitAndCombineExtDTO {
    /**
     * sku类型 0单sku，1组合sku，2组装sku
     */
    private Integer combineType;
    /**
     * 商家id
     */
    private Integer merchantId;
    /**
     * skuCode
     */
    private String skuCode;
    /**
     * sku主键id
     */
    private Integer skuId;
    /**
     * sku名称
     */
    private String skuName;
    /**
     * 单位信息
     */
    private List<UnitAndBaseUnitInfoDTO> unitInfo;
    /**
     * 组合品信息
     */
    private List<SkuCombineSimpleDTO> combineInfo;
}
