package com.lyf.scm.core.domain.model.pack;

import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PackTaskFinishDO extends BaseDO implements Serializable {
    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 包装需求单号
     */
    private String requireCode;

    /**
     * 包装任务单号
     */
    private String taskCode;

    /**
     * 包装任务明细操作单号
     */
    private String taskDetailOperateCode;

    /**
     * 单据编号
     */
    private String recordCode;

    /**
     * 单据编号
     */
    private Integer recordType;

    /**
     * 单据状态：0-初始， 2-取消，14-完成
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
     * 包装组线
     */
    private String packLine;

    /**
     * 包装类型-1:组装2:反拆3:自定义组合4:自定义反拆5:拆箱
     */
    private Integer packType;

    /**
     * 渠道
     */
    private String channelCode;

    /**
     * 商品编码
     */
    private String skuCode;

    /**
     * 本次包装数量
     */
    private BigDecimal packNum;

    /**
     * 实际包装日期
     */
    private Date packTime;

    /**
     * 需求完成日期
     */
    private Date requireFinishTime;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 备注
     */
    private String remark;
}