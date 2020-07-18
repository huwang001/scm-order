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
public class ShopInventoryDO extends BaseDO implements Serializable {

    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 单据编号
     */
    private String recordCode;

    /**
     * 商家Id
     */
    private Long merchantId;

    /**
     * 门店编码
     */
    private String shopCode;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 业务类型：1-抽盘，2-全盘
     */
    private Integer businessType;

    /**
     * 单据类型
     */
    private Integer recordType;

    /**
     * 单据状态
     */
    private Integer recordStatus;

    /**
     * 盘点备注
     */
    private String remark;

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
     * 外部系统单据编号
     */
    private String outRecordCode;

    /**
     * 盘点时间
     */
    private Date outCreateTime;
}
