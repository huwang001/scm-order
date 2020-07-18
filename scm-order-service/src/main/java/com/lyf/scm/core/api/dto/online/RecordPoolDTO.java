package com.lyf.scm.core.api.dto.online;

import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Remarks
 * @date 2020/7/2
 */
@Data
public class RecordPoolDTO {

    //主键
    @ApiModelProperty(value = "主键")
    private Long id;
    //do单号
    @ApiModelProperty(value = "do单号")
    private String doCode;
    //前置单据编号
    @ApiModelProperty(value = "前置单据编号")
    private String frontRecordCode;
    //仓库（wms合单后）的单据编号
    @ApiModelProperty(value = "仓库（wms合单后）的单据编号")
    private String warehouseRecordCode;
    //前置单据id
    @ApiModelProperty(value = "前置单据id")
    private Long frontRecordId;
    //仓库（wms合单后）的单据id
    @ApiModelProperty(value = "仓库（wms合单后）的单据id")
    private Long warehouseRecordId;
    //实仓ID
    @ApiModelProperty(value = "实仓ID")
    private Long realWarehouseId;
    //虚仓ID
    @ApiModelProperty(value = "虚仓ID")
    private Long virtualWarehouseId;
    //用于合并的MD5指纹信息
    @ApiModelProperty(value = "用于合并的MD5指纹信息")
    private String mergeFingerprint;
    //渠道编码
    @ApiModelProperty(value = "渠道编码")
    private String channelCode;
    //商户id
    @ApiModelProperty(value = "商户Id")
    private Long merchantId;
    //创建时间
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    //订单状态
    @ApiModelProperty("订单状态：0-初始,2-已取消,99-待合单,100-已合单")
    private Integer recordStatus;
    //sku明细
    @ApiModelProperty(value = "sku明细")
    private List<RecordPoolDetailDTO> rwRecordPoolDetails;
    @ApiModelProperty(value = "实仓信息")
    private RealWarehouse realWarehouse;
}
