package com.lyf.scm.admin.dto.reverse;

import com.lyf.scm.admin.remote.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 冲销单传输对象
 * <p>
 * @Author: wwh 2020/7/16
 */
@Data
public class ReverseDTO extends BaseDTO implements Serializable {

    @ApiModelProperty(value = "唯一主键")
    private Long id;

    @ApiModelProperty(value = "单据编号")
    private String recordCode;

    @ApiModelProperty(value = "单据类型 1出库单冲销 2入库单冲销")
    private Integer recordType;

    @ApiModelProperty(value = "单据状态 1已新建 2已取消 3已确认 11已出库 12已入库 13已过账")
    private Integer recordStatus;

    @ApiModelProperty(value = "原始出/入库单据编号")
    private String originRecordCode;

    @ApiModelProperty(value = "外部单据编号（出入库单对应的前置单号）")
    private String outRecordCode;

    @ApiModelProperty(value = "收货单据编号（入库单冲销时必填）")
    private String receiptRecordCode;

    @ApiModelProperty(value = "SAP凭证号")
    private String voucherCode;

    @ApiModelProperty(value = "实仓编码")
    private String realWarehouseCode;

    @ApiModelProperty(value = "工厂编码")
    private String factoryCode;

    @ApiModelProperty(value = "冲销日期")
    private Date reverseDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "更新人")
    private Long modifier;

    @ApiModelProperty(value = "仓库名称", hidden = true)
    private String realWarehouseName;

    @ApiModelProperty(value = "冲销单明细列表")
    private List<ReverseDetailDTO> reverseDetailDTOList;

}