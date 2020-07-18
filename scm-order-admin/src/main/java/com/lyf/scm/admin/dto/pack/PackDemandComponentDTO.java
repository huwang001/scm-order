package com.lyf.scm.admin.dto.pack;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/7
 */
@Data
public class PackDemandComponentDTO {


    /**
     * 唯一主键
     */
    @ApiModelProperty("唯一主键")
    private Long id;

    /**
     * 成品商品编码
     */
    @ApiModelProperty("成品商品编码")
    private String parentSkuCode;

    /**
     * 需求单code
     */
    @ApiModelProperty("需求单code")
    private String recordCode;

    /**
     * 自定义组合码
     */
    @ApiModelProperty("自定义组合码")
    private String customGroupCode;

    /**
     * 组件编码
     */
    @ApiModelProperty("组件编码")
    private String skuCode;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String skuName;

    /**
     * 需求数量
     */
    @ApiModelProperty("需求数量")
    private BigDecimal requireQty;

    /**
     * BOM数量=需求数量/成品数量
     */
    @ApiModelProperty("BOM数量=需求数量/成品数量")
    private BigDecimal bomQty;

    /**
     * 实际移库数量
     */
    @ApiModelProperty("实际移库数量")
    private BigDecimal actualMoveQty;

    /**
     * 组合份数量
     */
    @ApiModelProperty("组合份数量")
    private BigDecimal compositeQty;

    /**
     * 需求箱单位数量=需求数量/箱单位换算率
     */
    @ApiModelProperty("需求箱单位数量=需求数量/箱单位换算率")
    private BigDecimal requireBoxQty;

    /**
     * 箱单位换算比率
     */
    @ApiModelProperty("箱单位换算比率")
    private BigDecimal boxUnitRate;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String unit;

    /**
     * 单位编号
     */
    @ApiModelProperty("单位编号")
    private String unitCode;

    /**
     * 明细备注
     */
    @ApiModelProperty("明细备注")
    private String remark;

    /**
     * 移库类型：1出库 2入库
     */
    @ApiModelProperty("移库类型：1出库 2入库")
    private Integer moveType;

    /**
     * 移库类型名称
     */
    @ApiModelProperty("移库类型名称")
    private String moveTypeName;

    /**
     * 是否领料 :1 是 0 否；默认是1
     */
    @ApiModelProperty("是否领料 :1 是 0 否；默认是1")
    private Boolean isPick = true;
    
    /**
	 * 已锁定数量
	 */
    @ApiModelProperty("已锁定数量")
	private BigDecimal lockQty;

    /**
     * 待移库数量/调拨数量
     */
    @ApiModelProperty("待移库数量/调拨数量")
    private BigDecimal allotQty;

    /**
     * 渠道编码
     */
    @ApiModelProperty("渠道编码")
    private String channelCode;

    /**
     * 渠道名称
     */
    @ApiModelProperty("渠道名称")
    private String channelName;

    /**
     * 入向工厂编码
     */
    @ApiModelProperty("入向工厂编码")
    private String inFactoryCode;

    /**
     * 入向实仓编码
     */
    @ApiModelProperty("入向实仓编码")
    private String inRealWarehouseCode;

    /**
     * 调拨仓库
     */
    @ApiModelProperty("调拨仓库")
    private String pickWorkshopCode;

    /**
     * 包装类型 1:组装2:反拆3:自定义组合4:自定义反拆5:拆箱
     */
    @ApiModelProperty("包装类型 1:组装2:反拆3:自定义组合4:自定义反拆5:拆箱")
    private Integer packType;

    /**
     * 包装类型名称
     */
    @ApiModelProperty("包装类型名称")
    private String packTypeName;

    /**
     * 运输单位名称
     */
    @ApiModelProperty("运输单位名称")
    private String transportUnitName;
}
