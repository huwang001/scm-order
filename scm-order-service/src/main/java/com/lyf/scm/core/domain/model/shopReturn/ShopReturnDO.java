package com.lyf.scm.core.domain.model.shopReturn;

import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;

import java.util.Date;

@Data
public class ShopReturnDO extends BaseDO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 单据编号
     */
    private String recordCode;

    /**
     * 交易中心退货编码
     */
    private String outRecordCode;

    /**
     * cmp退货单号
     */
    private String cmpRecordCode;

    /**
     * sap单据编号
     */
    private String sapPoNo;

    /**
     * 门店code
     */
    private String shopCode;

    /**
     * 门店类型
     */
    private Integer shopType;

    /**
     * 商家id
     */
    private Long merchantId;

    /**
     * 单据类型：27-直营门店采购退货 28-加盟门店采购退货
     */
    private Integer recordType;

    /**
     * 单据状态：0-初始化(已确认) 4-已派车 10-已出库 11-已入库，2-已取消
     */
    private Integer recordStatus;

    /**
     * 渠道code
     */
    private String channelCode;

    /**
     * 入向实体仓库id
     */
    private Long inRealWarehouseId;

    /**
     * 入向工厂编码
     */
    private String inFactoryCode;

    /**
     * 入向仓库编码
     */
    private String inRealWarehouseCode;

    /**
     * 入向冷链实体仓库id
     */
    private Long inColdRealWarehouseId;

    /**
     * 入向冷链工厂编码
     */
    private String inColdFactoryCode;

    /**
     * 入向冷链仓库编码
     */
    private String inColdRealWarehouseCode;

    /**
     * 出向仓库编码
     */
    private String outRealWarehouseCode;

    /**
     * 出向工厂编码
     */
    private String outFactoryCode;

    /**
     * 出向实体仓库id
     */
    private Long outRealWarehouseId;

    /**
     * 派车状态(0: 待指定 , 1: 派车 , 2. 自提)
     */
    private Integer isNeedDispatch;

    /**
     * 退货时间
     */
    private Date outCreateTime;

    /**
     * 推送交易中心状态:0-未推送,1-待推送,2-推送完成
     */
    private Integer transStatus;

    /**
     * 是否指定收货仓：0-否，1-是
     */
    private Integer isAppoint;

    /**
     * 期望时间
     */
    private Date expectDate;

    /**
     * 货被提走的时间
     */
    private Date pickedTime;

    /**
     * POS退货信息
     */
    private String posReturnInfo;

    /**
     * 请求号
     */
    private String requestNo;
}