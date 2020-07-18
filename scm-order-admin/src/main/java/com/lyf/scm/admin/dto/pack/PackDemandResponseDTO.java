package com.lyf.scm.admin.dto.pack;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lyf.scm.admin.remote.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class PackDemandResponseDTO extends BaseDTO implements Serializable {

    @ApiModelProperty(value = "主键唯一id")
    private Long id;

    @ApiModelProperty(value = "销售单号")
    private String saleCode;

    @ApiModelProperty(value = "需求提出人")
    private String introducer;

    @ApiModelProperty(value = "需求编码")
    private String recordCode;

    @ApiModelProperty(value = "出仓id")
    private Long outRealWarehouseId;

    @ApiModelProperty(value = "出向实仓编码")
    private String outRealWarehouseCode;

    @ApiModelProperty(value = "单据状态 0:初始1:已确认2:已计划3:部分包装4:已包装完成")
    private Integer recordStatus;

    @ApiModelProperty(value = "包装类型 1:组装2:反拆3:自定义组合4:自定义反拆5:拆箱")
    private Integer packType;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "渠道编码")
    private String channelCode;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "需求部门")
    private String department;

    @ApiModelProperty(value = "需求日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date demandDate;

    @ApiModelProperty(value = "入向工厂编码")
    private String inFactoryCode;

    @ApiModelProperty(value = "入向实仓编码")
    private String inRealWarehouseCode;

    @ApiModelProperty(value = "包装仓ID")
    private Long inRealWarehouseId;

    @ApiModelProperty(value = "包装车间")
    private String packWorkshopCode;

    @ApiModelProperty(value = "出向工厂编码")
    private String outFactoryCode;

    @ApiModelProperty(value = "领料仓库")
    private String pickWorkshopCode;

    @ApiModelProperty(value = "包装车间名称")
    private String inRealWarehouseName;

    @ApiModelProperty(value = "领料仓库名称")
    private String outRealWarehouseName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "领料状态 0:未领料1:领料中2:已部分领料3:已全部领料")
    private Integer pickStatus;

    @ApiModelProperty(value = "是否外采 0:否 1:是")
    private Integer isOut;

    @ApiModelProperty(value = "外部单号")
    private String outCode;

    @ApiModelProperty(value = "优先级")
    private Integer priority;

    @ApiModelProperty(value = "更新人")
    private Long modifier;

    @ApiModelProperty(value = "创建类型1：接口创建2：页面创建3：导入创建")
    private Integer createType;

    @ApiModelProperty(value = "包装需求单成品明细列表")
    private List<PackDemandDetailDTO> packDemandDetailDTOList;

    @ApiModelProperty(value = "组件单明细列表")
    private List<PackDemandComponentDTO> packDemandComponentDTOList;
}
