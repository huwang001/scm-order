package com.lyf.scm.core.api.dto.stockFront;

import com.lyf.scm.core.api.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @Description 门店补货报表查询条件
 * @date 2020/6/20
 */
@Data
@EqualsAndHashCode
public class ShopReplenishReportDTO extends BaseDTO {

    @ApiModelProperty(value = "SAP采购单号")
    private String sapPoNo;
    @ApiModelProperty(value = "SAP交货单号")
    private String sapOrderCode;
    @ApiModelProperty(value = "工厂编号")
    private String factoryCode;
    @ApiModelProperty(value = "仓库编号")
    private String realWarehouseCode;
    @ApiModelProperty(value = "商品编号")
    private String skuCode;
    @ApiModelProperty(value = "门店编号")
    private String shopCode;
    @ApiModelProperty(value = "补货单创建时间（查询起始时间）")
    private Date startTime;
    @ApiModelProperty(value = "补货单创建时间（查询截止时间）")
    private Date endTime;
    @ApiModelProperty(value = "单据状态")
    private Integer recordStatus;
    @ApiModelProperty(value = "前置单CODE")
    private List<String> frontRecordCodeList;
    @ApiModelProperty(value = "出仓ID")
    private Long outRWId;
    @ApiModelProperty(value = "入仓ID")
    private Long inRWId;
    @ApiModelProperty(value = "前置单类型")
    private Integer frontRecordType;

}
