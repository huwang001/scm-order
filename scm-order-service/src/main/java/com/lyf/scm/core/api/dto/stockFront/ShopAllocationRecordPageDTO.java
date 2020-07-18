package com.lyf.scm.core.api.dto.stockFront;

import com.lyf.scm.core.api.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Description 调拨前置单据
 * @date 2020/6/15
 * @Version
 */
@Data
@EqualsAndHashCode
public class ShopAllocationRecordPageDTO extends BaseDTO {
    /**
     * 唯一主键
     */
    private Long id;
    /**
     * 单据编号
     */
    private String recordCode;

    /**
     * 外部单据
     */
    private String outRecordCode;

    /**
     * 调出渠道编号
     */
    private String outChannelCode;

    /**
     * 调入渠道编号
     */
    private String inChannelCode;

    /**
     * 调出渠道类型
     */
    private Long outChannelType;

    /**
     * 调入渠道类型
     */
    private Long inChannelType;

    /**
     * 调出店铺id
     */
    private String outShopCode;

    /**
     * 调入门店编号
     */
    private String inShopCode;

    /**
     * 调出门店名字
     */
    private String outShopName;

    /**
     * 调入门店名字
     */
    private String inShopName;

    /**
     * 调出商家id
     */
    private Long outMerchantId;

    /**
     * 调入商家id
     */
    private Long inMerchantId;

    /**
     * 入向实体仓库id
     */
    private Long inRealWarehouseId;

    /**
     * 出向实体仓库id
     */
    private Long outRealWarehouseId;

    /**
     * 1.直营-直营 2.加盟-加盟 3.直营-直营（跨组织）4.直营-加盟
     */
    private Integer businessType;

    /**
     * 单据状态
     */
    private Integer recordStatus;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long creator;

}
