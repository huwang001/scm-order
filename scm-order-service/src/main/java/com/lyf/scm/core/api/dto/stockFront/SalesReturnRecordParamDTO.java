package com.lyf.scm.core.api.dto.stockFront;

import com.lyf.scm.core.api.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class SalesReturnRecordParamDTO extends BaseDTO implements Serializable {

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

    private List<Long> warehouseRecordIds;

    private Integer recordType;

    private String factoryCode;
}