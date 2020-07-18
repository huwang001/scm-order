package com.lyf.scm.core.domain.model.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.ShopAllocationDetailE;
import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zys
 * @Description
 * @date 2020/6/15 15:15
 * @Version
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class ShopAllocationDO extends BaseDO implements Serializable {

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
     * 调出渠道编号
     */
    private String outChannelCode;

    /**
     * 调出店铺id
     */
    private String outShopCode;

    /**
     * 调入门店编号
     */
    private String inShopCode;

    /**
     * 调出商家id
     */
    private Long merchantId;
    /**
     *  类型
     */
    private Integer recordType;

    /**
     * 状态
     */
    private Integer recordStatus;

    /**
     * 1.直营-直营 2.加盟-加盟 3.直营-直营（跨组织）4.直营-加盟
     */
    private Integer businessType;

    /**
     * 入向实体仓库id
     */
    private Long inRealWarehouseId;

    /**
     * 调入工厂
     */
    private String inFactoryCode;

    /**
     * 调出工厂
     */
    private String outFactoryCode;
    /**
     * 调入仓库
     */
    private String inRealWarehouseCode;

    /**
     *调出仓库
     */
    private String outRealWarehouseCode;

    /**
     * 出向实体仓库id
     */
    private Long outRealWarehouseId;

    /**
     * 外部系统数据创建时间
     */
    private Date outCreateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;


}
