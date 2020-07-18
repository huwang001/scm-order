package com.lyf.scm.core.api.dto.stockFront;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 门店补货寻源
 * @author: yaoleiming
 * @data: 2019/9/20 20:02
 */
@Data
@EqualsAndHashCode
public class ShopReplenishAllotDTO implements Serializable {
    @ApiModelProperty(value = "单据编号")
    private List<String> poList;
    @ApiModelProperty(value = "工厂代码")
    private String factoryCode;
    @ApiModelProperty(value = "操作人ID")
    private Long operator;
    @ApiModelProperty(value = "渠道编号")
    private String channelCode;
    /**
     * 苏南渠道编码
     */
    private static String defaultChannelCode = "G21200000_119";
}
