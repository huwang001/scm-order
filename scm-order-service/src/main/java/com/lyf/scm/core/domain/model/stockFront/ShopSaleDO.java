package com.lyf.scm.core.domain.model.stockFront;

import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class ShopSaleDO extends BaseDO implements Serializable {

    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 单据编号
     */
    private String recordCode;

    /**
     * 渠道code
     */
    private String channelCode;

    /**
     * 单据类型：31-门店零售
     */
    private Integer recordType;

    /**
     * 单据状态：17-待支付，18-已支付，2-已取消 10已出库
     */
    private Integer recordStatus;

    /**
     * 单据状态审核原因
     */
    private String recordStatusReason;

    /**
     * 实体仓库id
     */
    private Long realWarehouseId;

    /**
     * 工厂编码
     */
    private String factoryCode;

    /**
     * 仓库编码
     */
    private String realWarehouseCode;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 外部系统单据编号:订单编码
     */
    private String outRecordCode;

    /**
     * 外部系统数据创建时间:下单时间
     */
    private Date outCreateTime;

    /**
     * 用户code
     */
    private String userCode;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 商家id
     */
    private Long merchantId;

    /**
     * 1-普通,2-预售,3-拼团,4-拼券,5-旺店通,6-POS门店,7-外卖自营,8-外卖第三方营,9-电商超市,10-2B分销,11-加盟商 12 虚拟商品
     */
    private Integer transType;

    /**
     * 期望收货日期_开始
     */
    private Date expectReceiveDateStart;

    /**
     * 期望收货日期_截止
     */
    private Date expectReceiveDateEnd;
}