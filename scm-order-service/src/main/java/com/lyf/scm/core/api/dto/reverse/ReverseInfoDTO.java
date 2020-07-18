package com.lyf.scm.core.api.dto.reverse;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Desc:冲销单创建对象
 * @author:Huangyl
 * @date: 2020/7/17
 */
@Data
public class ReverseInfoDTO {

    @ApiModelProperty(value = "主键编辑必传")
    private Long id;

    @ApiModelProperty(value = "单据编号")
    private String recordCode;

    @ApiModelProperty(value = "单据类型 1出库单冲销 2入库单冲销")
    private Integer recordType;

    @ApiModelProperty(value = "原始出/入库单据编号")
    private String originRecordCode;

    @ApiModelProperty(value = "外部单据编号（出入库单对应的前置单号）")
    private List<String> outRecordCodeList;

    @ApiModelProperty(value = "收货单据编号（入库单冲销时必填）")
    private String receiptRecordCode;

    @ApiModelProperty(value = "实仓编码")
    private String realWarehouseCode;

    @ApiModelProperty(value = "工厂编码")
    private String factoryCode;

    @ApiModelProperty(value = "冲销日期")
    private Date reverseDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "操作人")
    private Long userId;

    @ApiModelProperty("冲销单明细")
    private List<ReverseDetailDTO> reverseDetailDTOList;
}
