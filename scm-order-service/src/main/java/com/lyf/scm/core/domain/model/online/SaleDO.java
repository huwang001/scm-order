package com.lyf.scm.core.domain.model.online;

import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class SaleDO extends BaseDO implements Serializable {
    /**
     * 唯一主键
     */
    private Long id;
    /**
     * 单据编号
     */
    private String recordCode;
    /**
     * 渠道id
     */
    private String channelCode;
    /**
     * 单据类型：1-门店零售 2-电商零售 3-待续
     */
    private Integer recordType;
    /**
     * 单据状态：0-新建，1-审核 2-取消
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
     * 外部仓库编码
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
     * 交易类型
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
