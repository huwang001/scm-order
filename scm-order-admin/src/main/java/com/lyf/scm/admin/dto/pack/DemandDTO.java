package com.lyf.scm.admin.dto.pack;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description 包装需求单传输对象
 * @Author: liuyao
 * @Date: 2020/7/7
 */
@Data
public class DemandDTO {

    @ApiModelProperty(value = "主键编辑的必传")
    private Long id;

    @ApiModelProperty(value = "需求编码")
    private String recordCode;

    @ApiModelProperty(value = "销售单号")
    private String saleCode;

    @ApiModelProperty(value = "包装类型 1:组装2:反拆3:自定义组合4:自定义反拆5:拆箱")
    private Integer packType;

    @ApiModelProperty(value = "渠道编码")
    private String channelCode;

    @ApiModelProperty(value = "需求日期")
    private Date demandDate;

    @ApiModelProperty(value = "需求部门")
    private String department;

    @ApiModelProperty(value = "是否外采 0:否 1:是")
    private Integer isOut;

    @ApiModelProperty(value = "外部单号")
    private String outCode;

    @ApiModelProperty(value = "包装仓ID")
    private Long inRealWarehouseId;

    @ApiModelProperty(value = "入向工厂编码")
    private String inFactoryCode;

    @ApiModelProperty(value = "入向实仓编码")
    private String inRealWarehouseCode;

    @ApiModelProperty(value = "入虚仓编码")
    private String inVirtualWarehouseCode;

    @ApiModelProperty(value = "领料仓ID")
    private Long outRealWarehouseId;

    @ApiModelProperty(value = "出向工厂编码")
    private String outFactoryCode;

    @ApiModelProperty(value = "出向实仓编码")
    private String outRealWarehouseCode;

    @ApiModelProperty(value = "出虚仓编码")
    private String outVirtualWarehouseCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "优先级")
    private Integer priority;

    @ApiModelProperty(value = "操作人")
    private Long userId;

    @ApiModelProperty(value = "需求提出人")
    private String introducer;

    @ApiModelProperty(value = "包装需求单明细原料列表")
    private List<PackDemandDetailDTO> packDemandDetailDTOList;

    @ApiModelProperty(value = "包装需求单明细列表")
    private List<PackDemandComponentDTO> packDemandComponentDTOList;
}
