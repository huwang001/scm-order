package com.lyf.scm.job.api.dto.stockFront;

import com.lyf.scm.common.model.Pagination;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class WarehouseRecordPageDTO extends Pagination {

    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 单据编号
     */
    private String recordCode;

    /**
     * sap单号
     */
    private String sapOrderCode;

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


    private Long realWarehouseId;

    /**
     * 实仓仓库名称
     */
    private String realWarehouseName;

    /**
     * 工厂代码
     */
    private String factoryCode;

    /**
     * 仓库外部编号-wms
     */
    private String realWarehouseOutCode;

    /**
     * 出库时间
     */
    private Date deliveryTime;

    /**
     * 入库时间
     */
    private Date receiverTime;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * tms派车单号
     */
    private String tmsRecordCode;

}
