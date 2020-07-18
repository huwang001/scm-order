package com.lyf.scm.core.api.dto.stockFront;

import com.lyf.scm.common.model.Pagination;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class FrontRecordDTO extends Pagination {

    @ApiModelProperty(value = "渠道编码")
    private String channelCode;

    @ApiModelProperty(value = "店铺编码")
    private String shopCode;

    @ApiModelProperty(value = "单据编码")
    private String recordCode;

    @ApiModelProperty(value = "单据类型")
    private String recordType;
    /**
     * 外部单号
     */
    private  String outRecordCode;

    /**
     * 备注
     */
    private String remark;
    /**
     * 门店名称
     */
    private String shopName;

    @ApiModelProperty(value = "sku数量及明细")
    private List<OrderDetailDTO> frontRecordDetails;

}
