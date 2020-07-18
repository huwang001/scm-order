package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class CoreChannelOrderDTO {

    /**
     * 渠道ID
     */
    private String channelCode;

    /**
     * 单据编号
     */
    private String recordCode;

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 区/县城市编码
     */
    private String countyCode;

    /**
     * 库存交易类型
     */
    private Integer transType;

    /**
     * 商家ID
     */
    private Long merchantId;

    /**
     * 渠道销售订单明细
     */
    private List<CoreOrderDetailDO> orderDetailDOs;

    /**
     * 虚仓库存详细--从上向下
     */
    private List<CoreVirtualStockOpDO> virtualStockOpDetailDOs;
}
