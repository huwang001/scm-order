package com.lyf.scm.core.remote.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * ChannelDTO
 *
 * @author mawenlong
 * @date 2019/4/16
 */
@Data
public class ChannelDTO {

    private String channelCode;

    @ApiModelProperty(value = "渠道名称", example = "自动生成，不可修改")
    private String channelName;

    @ApiModelProperty(value = "销售单元id")
    private String saleUnitCode;

    @ApiModelProperty(value = "渠道类型")
    private String channelType;

    @ApiModelProperty(value = "是否有效")
    private Integer isAvailable;

    /**
     * 标签
     */
    private Set<LabelCodeDTO> label;

    /**
     * 公司code
     */
    private String orgCode;

    /**
     * 商家ID
     */
    private Long merchantId;

    /**
     * 销售单元类型
     */
    private Integer saleUnitType;

    /**
     * 商家
     */
    private String franchisee;
}
