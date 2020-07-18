package com.lyf.scm.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lyf.scm.admin.remote.dto.BaseDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author zys
 * @Remark
 * @date 2020/7/8
 */
@Data
public class SkuInfoExtDTO extends BaseDTO {

    /** sku简称 */
    private String abbreviation;

    /** 审核状态，0不通过，1通过 */
    private Integer auditStatus;

    /** SKU 69码 */
    private String barCode;

    /** 开始有效期 */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date beginTime;

    /** 品牌id */
    private Long brandId;

    /** 品牌名称 */
    private String brandName;

    /** 类目编码Code */
    private Long categoryCode;

    /** 从根节点到本类目的id */
    private String categoryFullPathId;

    /** 从根节点到本类目的名称 */
    private String categoryFullPathName;

    /** 类目id */
    private Long categoryId;

    /** 类目名称 */
    private String categoryName;

    /** 渠道编码 */
    private String channelCode;

    /** 渠道sku简称(别名) */
    private String channelSkuAbbreviation;

    /** 渠道sku名称 */
    private String channelSkuName;

    /** 类型，0单sku，1组合sku，2组装sku */
    private Integer combineType;

    /** 集装箱需求 01整箱，02拆零 */
    private String container;

    private Long deliveryTemplateId;

    private String deliveryTemplateName;

    private String describe;

    /** sku描述 */
    private String description;

    /** 结束有效期 */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date endTime;

    /** sku主键id */
    private Long id;

    /** sku名称 */
    private String name;

    /** 是否需要质检 */
    private Boolean needRequired;

    /** 物料组包装物料,01 散包02 散装03 罐装04 袋装05 礼盒06 支07 盒08 袋 */
    private Integer packageMaterialType;

    /** 剩余货架寿命 */
    private Integer remainShelfLife;

    /** 备注 */
    private String remark;

    /** sku编号 */
    private String skuCode;

    /** sku主图 */
    private String skuMasterGraphUrl;

    /** sku类型id */
    private Long skuTypeId;

    /** sku类型编码 */
    private String skuTypeCode;

    /** sku类型名称 */
    private String skuTypeName;

    /** 来源0：sap */
    private Integer source;

    /** spu简称 */
    private String spuAbbreviation;

    /** spu审核状态 0待审核 1通过 */
    private Integer spuAuditStatus;

    /** spu编码 */
    private String spuCode;

    /** spu主键id */
    private Long spuId;

    /** spu名称 */
    private String spuName;

    /** spu来源 */
    private Integer spuSource;

    /** spu类型 Z001一般商品(食品) Z002一般商品(非食品) Z003组装商品… */
    private String spuType;

    /** 基础单位code */
    private String spuUnitCode;

    /** spu基础单位id */
    private Long spuUnitId;

    /** spu基础单位名称 */
    private String spuUnitName;

    /** 标准价，也叫采购价 */
    private BigDecimal standardPrice;

    /** 存储条件 01格斗 02壁柜 03展架 04墙柜 */
    private String storageCondition;

    /** 门店最佳销售天数 */
    private Integer storeBestSaleDay;

    /** 门店号 */
    private String storeCode;

    /** 税码 JA 13%进项税,中国JB 9%进项税,中国X0 0% 销项税 */
    private String taxCode;

    /** 税率 */
    private BigDecimal taxRate;

    /** 总货架寿命 */
    private Integer totalShelfLife;

    /** SKU版本号 eg. V1 V2 */
    private String version;

    /** 虚拟品回调地址 */
    private String virtualCallUrl;

    /** 仓库最晚发货天数 */
    private Integer warehouseLatestDeliveryDay;
}
