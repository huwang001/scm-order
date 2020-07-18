package com.lyf.scm.core.remote.pack.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Desc:组装，拆箱时的成品列表信息
 * @author:Huangyl
 * @date: 2020/7/7
 */
@Data
public class ObtainDemandGoodsDTO {
    @ApiModelProperty("商品编码")
    private String goodsCode;
    @ApiModelProperty("单位")
    private String unitCode;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("需求数量")
    private BigDecimal needAmount;
    @ApiModelProperty("优先级")
    private Integer priority;
    @ApiModelProperty("组合、反拆时的sku列表信息")
    private List<ObtainSkuDTO> skuList;
}
