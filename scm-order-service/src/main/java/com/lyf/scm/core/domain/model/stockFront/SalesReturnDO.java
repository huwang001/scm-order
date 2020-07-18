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
public class SalesReturnDO extends BaseDO implements Serializable {

    /**
     * 唯一主键
     */
    private Long id;

    /**
     * '单据编号'
     */
    private String recordCode;

    /**
     * '渠道code'
     */
    private String channelCode;

    /**
     * 单据类型：32-门店零售退货
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
     * 入向实体仓库id
     */
    private Long inRealWarehouseId;

    /**
     * 调入工厂编码
     */
    private String inFactoryCode;

    /**
     * 调入仓库编码
     */
    private String inRealWarehouseCode;

    /**
     * 出向实体仓库id
     */
    private Long outRealWarehouseId;

    /**
     * 调出工厂编码
     */
    private String outFactoryCode;

    /**
     * 调出仓库
     */
    private String outRealWarehouseCode;

    /**
     * 退货原因
     */
    private String reason;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 外部系统单据编号:退货编码
     */
    private String outRecordCode;

    /**
     * 外部系统数据创建时间:退货时间
     */
    private Date outCreateTime;

    /**
     * 商家id
     */
    private Long merchantId;

    /**
     * 用户code
     */
    private String userCode;

    /**
     * 店铺编码
     */
    private String shopCode;

    private String ext;
}