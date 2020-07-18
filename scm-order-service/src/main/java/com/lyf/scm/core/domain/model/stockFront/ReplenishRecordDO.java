package com.lyf.scm.core.domain.model.stockFront;

import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 门店补货 <br>
 *
 * @Author chuwenchao 2020/6/10
 */
@Data
public class ReplenishRecordDO extends BaseDO {

    /**
     * 唯一主键
     */
    private Long id;
    /**
     * 单据编号
     */
    private String recordCode;
    /**
     * 外部系统单据编号
     */
    private String outRecordCode;
    /**
     * 渠道编号
     */
    private String channelCode;
    /**
     * 店铺编码
     */
    private String shopCode;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 门店性质：1 直营 3 加盟 5 加盟托管
     */
    private Integer shopType;
    /**
     * 工厂code
     */
    private String factoryCode;
    /**
     * 配货类型 1普通配货 2指定仓库配货
     */
    private Integer requireType;
    /**
     * 采购单类型：15-直营采购, 30-加盟商采购 19-冷链门店补货单 21-直送门店补货单
     */
    private Integer recordType;
    /**
     * 0-初始化(已确认)，10-已出库  11-已入库，2-已取消
     */
    private Integer recordStatus;
    /**
     * 出向实体仓库id
     */
    private Long outRealWarehouseId;

    /**
     * 调出工厂
     */
    private String outFactoryCode;

    /**
     * 调出仓库
     */
    private String outRealWarehouseCode;

    /**
     * 入向实体仓库id
     */
    private Long inRealWarehouseId;

    /**
     * 调入工厂
     */
    private String inFactoryCode;

    /**
     * 调入仓库
     */
    private String inRealWarehouseCode;

    /**
     * 外部系统数据创建时间
     */
    private Date outCreateTime;
    /**
     * 要货时间
     */
    private Date replenishTime;

    /**
     * 是否需要寻源分配(0: 无需分配, 1.需要分配 2. 分配完成)
     */
    private Integer isNeedAllot;

    /**
     * 是否需要派车(1: 派车 , 2. 自提)
     */
    private Integer isNeedDispatch;

    /**
     * sap采购单号
     */
    private String sapPoNo;

}
