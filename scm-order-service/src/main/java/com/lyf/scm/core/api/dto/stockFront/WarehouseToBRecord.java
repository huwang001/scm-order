package com.lyf.scm.core.api.dto.stockFront;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description
 * @date 2020/6/15
 * @Version
 */
@Data
@EqualsAndHashCode
public class WarehouseToBRecord {
    /**
     * 单据编码
     */
    @ApiModelProperty(value = "单据编码")
    private String recordCode;
    /**
     * 业务类型：
     */
    @ApiModelProperty(value = "业务类型")
    private Integer businessType;
    /**
     * do单状态
     */
    @ApiModelProperty(value = "do单状态")
    private Integer recordStatus;
    /**
     * 单据类型
     */
    @ApiModelProperty(value = "单据类型")
    private Integer recordType;
    /**
     * 渠道id
     */
    @ApiModelProperty(value = "渠道id")
    private String channelCode;
    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private String shopCode;
    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private Long merchantId;

    @ApiModelProperty(value = "sku数量及明细")
    private List<WarehouseRecordDetailDTO> warehouseRecordDetail;
}
