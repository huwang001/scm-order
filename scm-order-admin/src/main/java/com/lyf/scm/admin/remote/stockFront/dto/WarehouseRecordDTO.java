package com.lyf.scm.admin.remote.stockFront.dto;

import com.lyf.scm.common.model.Pagination;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 类ShopAssembleSpitPage的实现描述：门店盘点
 *
 * @author sunyj 2019/4/23 20:29
 */
@Data
@EqualsAndHashCode
public class WarehouseRecordDTO extends Pagination {

    /**
     * 唯一主键
     */
    private Long id;
    /**
     * 单据编号
     */
    private String recordCode;

    /**
     * 单据类型
     */
    private Integer recordType;

    /**
     * 出入库类型
     */
    private Integer businessType;

    /**
     * 单据状态：已入库
     */
    private Integer recordStatus;

    /**
     * 实仓仓库编号
     */
    private String realWarehouseCode;

    /**
     * 实仓仓库名称
     */
    private String realWarehouseName;
    /**
     * 工厂代码
     */
    private String factoryCode;

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
     * 前置单
     */
    private FrontRecordDTO frontRecord;

    /**
     * 出库时间
     */
    private Date deliveryTime;

    /**
     * 入库时间
     */
    private Date receiverTime;

    /**
     * 创建人
     */
    private Long creator;
}
