package com.lyf.scm.core.domain.model.online;

import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/2
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class WDTSaleDO extends BaseDO {
    //columns START
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
     * 单据类型：99-电商零售
     */
    private Integer recordType;
    /**
     * 单据状态：18-已支付，2-已取消 10已出库
     */
    private Integer recordStatus;
    /**
     * 拆单状态：0 正常单 1异常单 需要拆单
     */
    private Integer splitType;
    /**
     * 分配状态：0 还需分配  1无需再分配（正常单或拆单结束）
     */
    private Integer allotStatus;
    /**
     * 实体仓库id
     */
    private Long realWarehouseId;
    /**
     * 虚拟仓库id
     */
    private Long virtualWarehouseId;
    /**
     * 实体仓库code
     */
    private String realWarehouseCode;
    /**
     * 虚拟仓库code
     */
    private String virtualWarehouseCode;
    /**
     * 物流公司编码
     */
    private String logisticsCode;
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
     * 商家id
     */
    private Long merchantId;
    /**
     * 1-普通,2-预售,3-拼团,4-拼券,5-旺店通,6-POS门店,7-外卖自营,8-外卖第三方营,9-电商超市,10-2B分销,11-加盟商 12 虚拟商品
     */
    private Integer transType;
    /**
     * 原始订单号
     */
    private String originOrderCode;

}
