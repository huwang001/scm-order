package com.lyf.scm.core.api.dto.pack;

import com.lyf.scm.core.api.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description 包装需求单查询对象
 * @Author: liuyao
 * @Date: 2020/7/7
 */
@Data
public class QueryPackDemandDTO extends BaseDTO implements Serializable {

    @ApiModelProperty(value = "需求单号")
    private List<String> recordCodes;

    @ApiModelProperty(value = "渠道编码集合")
    private List<String> channelCodes;

    @ApiModelProperty(value = "出仓工厂编码")
    private String outFactoryCode;

    @ApiModelProperty(value = "出向实仓编码")
    private String outRealWarehouseCode;

    @ApiModelProperty(value = "单据状态 0:初始1:已确认2:已计划3:部分包装4:已包装完成")
    private Integer recordStatus;

    @ApiModelProperty(value = "包装类型 1:组装2:反拆3:自定义组合4:自定义反拆5:拆箱")
    private Integer packType;

    @ApiModelProperty(value = "包含商品")
    private List<String> skuCodes;

    @ApiModelProperty(value = "销售单号")
    private List<String> saleCodes;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "需求完成开始时间")
    private Date requiredStartTime;

    @ApiModelProperty(value = "需求完成结束时间")
    private Date requiredEndTime;

    @ApiModelProperty(value = "领料状态 0:未领料1:领料中2:已部分领料3:已全部领料")
    private Integer pickStatus;

}
