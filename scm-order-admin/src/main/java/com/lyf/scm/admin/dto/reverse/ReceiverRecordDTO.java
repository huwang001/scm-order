package com.lyf.scm.admin.dto.reverse;

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

    private static final long serialVersionUID = 8725265297415100148L;

    @ApiModelProperty("单据编号")
    private String recordCode;

    @ApiModelProperty("收货单编号")
    private String receiverCode;

    @ApiModelProperty(value = "实仓CODE")
    private String realWarehouseCode;

    @ApiModelProperty(value = "实仓名称")
    private String realWarehouseName;

    @ApiModelProperty("收货时间")
    private Date receiverDate;

    @ApiModelProperty("外部单号")
    private String outRecordCode;

    @ApiModelProperty("收货单明细")
    private List<ReceiverRecordDetailDTO> receiverRecordDetailDTOList;
}
