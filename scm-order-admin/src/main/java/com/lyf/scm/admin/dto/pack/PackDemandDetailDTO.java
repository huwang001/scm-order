package com.lyf.scm.admin.dto.pack;

import com.lyf.scm.admin.dto.SkuUnitExtDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/7
 */
@Data
public class PackDemandDetailDTO {

    /**
     * 唯一主键
     */
    @ApiModelProperty(value = "唯一主键")
    private Long id;

    /**
     * 需求单code
     */
    @ApiModelProperty(value = "需求单code")
    private String recordCode;

    /**
     * 自定义组合码
     */
    @ApiModelProperty(value = "自定义组合码")
    private String customGroupCode;

    /**
     * 组件编码
     */
    @ApiModelProperty(value = "组件编码")
    private String skuCode;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String skuName;

    /**
     * 需求数量
     */
    @ApiModelProperty(value = "需求数量")
    private BigDecimal requireQty;

    /**
     * 组合份数量
     */
    @ApiModelProperty(value = "组合份数量")
    private BigDecimal compositeQty;

    /**
     * 实际已包装数量
     */
    @ApiModelProperty(value = "实际已包装数量")
    private BigDecimal actualPackedQty;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 单位编号
     */
    @ApiModelProperty(value = "单位编号")
    private String unitCode;

    /**
     * 明细备注
     */
    @ApiModelProperty(value = "明细备注")
    private String remark;


    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long cerator;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private Long modifier;

    /**
     * 基础单位转换比
     */
    @ApiModelProperty(value = "基础单位转换比")
    private BigDecimal basicScale;

    /**
     * 基础单位code
     */
    @ApiModelProperty(value = "基础单位code")
    private String basicUnitCode;

    /**
     * 基础单位名称
     */
    @ApiModelProperty(value = "基础单位名称")
    private String basicUnitName;

    /**
     * 包装类型 1:组装2:反拆3:自定义组合4:自定义反拆5:拆箱
     */
    @ApiModelProperty(value = "包装类型")
    private Integer packType;

    /**
     * 包装类型名称
     */
    @ApiModelProperty(value = "包装类型名称")
    private String packTypeName;

    /**
     *单位列表
     */
    @ApiModelProperty(value = "单位列表")
    private List<SkuUnitExtDTO> skuUnitList;
}
