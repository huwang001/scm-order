package com.lyf.scm.core.domain.entity.stockFront;

import com.lyf.scm.core.domain.model.stockFront.ReplenishDetailDO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: ReplenishDetailE
 * <p>
 * @Author: chuwenchao  2020/6/10
 */
@Data
public class ReplenishDetailE extends ReplenishDetailDO {

    /**
     * 门店编号(寻源专用)
     */
    private String shopCode;

    /**
     * 采购单类型(寻源专用)
     */
    private Integer recordType;

    /**
     * 渠道编码
     */
    private String channelCode;

    /**
     *  门店现有库存(寻源专用)
     */
    private BigDecimal shopStock;

    /**
     * 门店等级(寻源专用)
     */
    private String shopLevel;

    /**
     * 要货时间
     */
    private Date replenishTime;

}
