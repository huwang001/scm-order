package com.lyf.scm.core.api.dto.reverse;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description 收货单DTO
 * @Author wuyuanhang
 * @Date 2020/7/18
 */
@Data
public class ReceiverRecordDTO implements Serializable {

    private static final long serialVersionUID = 5425114750079096624L;

    @ApiModelProperty("单据编号")
    private String recordCode;

    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("前置单编号")
    private String frontRecordCode;

    @ApiModelProperty("后置单编号")
    private String warehouseRecordCode;

    @ApiModelProperty("")
    private String outRecordCode;

    @ApiModelProperty("收货单编码")
    private String wmsRecordCode;

    @ApiModelProperty("")
    private String qualityStatus;

    @ApiModelProperty("")
    private String syncOrderStatus;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("收货单明细")
    private List<ReceiverRecordDetailDTO> details;
}
