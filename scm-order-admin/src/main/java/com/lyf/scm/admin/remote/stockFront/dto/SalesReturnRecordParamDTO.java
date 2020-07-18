package com.lyf.scm.admin.remote.stockFront.dto;

import com.lyf.scm.common.model.Pagination;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SalesReturnRecordParamDTO extends Pagination {

    private String recordCode;

    private String outRecordCode;

    private String channelCodes;

    private List<String> channelCodeList;

    private String shopCode;

    private Integer recordStatus;

    @ApiModelProperty(required = true, dataType = "date", value = "时间（yyyy-MM-dd）")
    private Date startCrateTime;

    @ApiModelProperty
    private Date endCreateTime;

    private String reason;

    private Long realWarehouseId;

    private String realWarehouseCode;
}
