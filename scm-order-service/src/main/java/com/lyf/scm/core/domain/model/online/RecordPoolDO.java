package com.lyf.scm.core.domain.model.online;

import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 单据池
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class RecordPoolDO extends BaseDO implements Serializable {
    /**
     * 唯一主键
     */
    private Long id;
    /**
     * 前置单据编号
     */
    private String frontRecordCode;
    /**
     * 仓库（wms合单后）的单据编号
     */
    private String warehouseRecordCode;
    /**
     * 前置单据id
     */
    private Long frontRecordId;
    /**
     * 仓库（wms合单后）的单据id
     */
    private Long warehouseRecordId;
    /**
     * 渠道编码
     */
    private String channelCode;
    /**
     * 实仓ID
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
     * 虚仓ID
     */
    private Long virtualWarehouseId;
    /**
     * 虚仓编码
     */
    private String virtualWarehouseCode;
    /**
     * 用于合并的MD5指纹信息
     */
    private String mergeFingerprint;
    /**
     * 商家id
     */
    private Long merchantId;
    /**
     * 单据状态：0-初始 1-完成，2-待合单，3 已同步 10 拣货 11 打包 12 装车 13 发运 21 接单 22 配送
     */
    private Integer recordStatus;
    /**
     * do单号
     */
    private String doCode;
    /**
     * 物流公司编码
     */
    private String logisticsCode;
    /**
     * 单据类型
     */
    private Integer recordType;
    /**
     * 是否需要合单
     */
    private Integer needCombine;
    /**
     * 推送交易状态 0 无需推送  1 待推送 2已推送
     */
    private Integer syncStatus;
}
