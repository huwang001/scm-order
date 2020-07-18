package com.lyf.scm.core.api.dto.stockFront;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/3
 */
@Data
public class CommonFrontRecordDTO implements Serializable {

    @ApiModelProperty(value = "唯一主键")
    private Long id;

    @ApiModelProperty(value = "单据编号")
    private String recordCode;

    @ApiModelProperty(value = "sap采购单号")
    private String sapPoNo;

    @ApiModelProperty(value = " 单据类型 22调拨单 15-直营采购, 30-加盟商采购 19-冷链门店补货单 21-直送门店补货单")
    private Integer recordType;

    @ApiModelProperty(value = "0已新建 1已确认 2已取消 4已派车 10已出库 11已入库")
    private Integer recordStatus;

    @ApiModelProperty(value = "入向实仓外部编码")
    private String inRealWarehouseCode;

    @ApiModelProperty(value = "入向工厂编码")
    private String inFactoryCode;

    @ApiModelProperty(value = "出向实仓外部编码")
    private String outRealWarehouseCode;

    @ApiModelProperty(value = "出向工厂编码")
    private String outFactoryCode;

    //============== 调拨字段 ==============
    @ApiModelProperty(value = "1内部调拨 2RDC调拨 3退货调拨 4电商仓调拨 5质量问题调拨")
    private Integer businessType;

    @ApiModelProperty(value = "1出入都是中台 2出中台入非中台 3出非中台入中台")
    private Integer whType;

    @ApiModelProperty(value = "是否差异入库 0没有差异 1有差异")
    private Integer isDiffIn;

    @ApiModelProperty(value = "调拨日期")
    private Date allotTime;

    @ApiModelProperty(value = "预计到货日期")
    private Date expeAogTime;

    @ApiModelProperty(value = "审核人")
    private Long auditor;

    @ApiModelProperty(value = "审核时间")
    private Date auditTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否退货调拨 1是 0不是")
    private Integer isReturnAllotcate;

    @ApiModelProperty(value = "是否存在差异 1存在差异 0不存在差异")
    private Integer isDisparity;

    @ApiModelProperty(value = "原始单据Id")
    private Long orginId;

    @ApiModelProperty(value = "原始单据号")
    private String orginRecord;

    @ApiModelProperty(value = "是否质量问题调拨 1是 0不是")
    private Integer isQualityAllotcate;

    //============== 门店补货字段 ==============
    @ApiModelProperty(value = "渠道编号")
    private String channelCode;

    @ApiModelProperty(value = "店铺编码")
    private String shopCode;

    @ApiModelProperty(value = "门店性质：1 直营 3 加盟 5 加盟托管")
    private Integer shopType;

    @ApiModelProperty(value = "工厂code")
    private String factoryCode;

    @ApiModelProperty(value = "配货类型 1普通配货 2指定仓库配货")
    private Integer requireType;

    @ApiModelProperty(value = "外部系统数据创建时间")
    private Date outCreateTime;

    @ApiModelProperty(value = "外部系统单据编号")
    private String outRecordCode;

    @ApiModelProperty(value = "要货时间")
    private Date replenishTime;

    //============== 差异字段 ==============
    @ApiModelProperty(value = "入库单据编码")
    private String inWarehouseRecordCode;

    @ApiModelProperty(value = "出库单据编码")
    private String outWarehouseRecordCode;

    @ApiModelProperty(value = "是否逆向：true是逆向；false是正向")
    private Boolean isReverse;

    //============== 门店调拨 ==============
    @ApiModelProperty(value = "调出店铺编号")
    private String outShopCode;

    @ApiModelProperty(value = "调入门店编号")
    private String inShopCode;

    //============== 门店退货 ==============
    @ApiModelProperty(value = "cmp退货单号")
    private String cmpRecordCode;

    @ApiModelProperty(value = "前置单明细")
    private List<CommonFrontRecordDetailDTO> frontRecordDetails;
}
