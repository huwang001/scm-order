package com.lyf.scm.core.api.dto.stockFront;

import com.lyf.scm.core.api.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 门店盘点
 */
@Data
@EqualsAndHashCode
public class ShopInventoryPageDTO extends BaseDTO {

    /**
     * 唯一主键
     */
    private Long id;
    /**
     * 单据编号
     */
    private String recordCode;

    /**
     * 关联单据
     */
    private String outRecordCode;

    /**
     * 门店ID
     */
    private String shopCode;
    /**
     * 门店名称
     */
    private String shopName;

    /**
     * 单据类型
     */
    private Integer recordType;
    /**
     * 抽盘类型：1-抽盘，2-全盘
     */
    private Integer businessType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 单据状态：已入库
     */
    private Integer recordStatus;

    /**
     * 外部系统数据创建时间
     */
    private Date outCreateTime;

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
