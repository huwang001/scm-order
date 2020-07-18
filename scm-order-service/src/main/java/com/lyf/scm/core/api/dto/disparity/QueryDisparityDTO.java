package com.lyf.scm.core.api.dto.disparity;

import com.lyf.scm.common.model.Pagination;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class QueryDisparityDTO extends Pagination {

    /**
     * 单据类
     */
    private Integer recordType;
    /**
     * 入仓名称
     */
    private String inRealWarehouseName;
    /**
     * 仓库名称
     */
    private String outRealWarehouseName;

    /**
     * 入仓编码
     */
    private String inRealWarehouseCode;

    /**
     * 出向仓库编码
     */
    private String outRealWarehouseCode;
    /**
     * 前置单据编码
     */
    private String frontRecordCode;

    /**
     * 责任方类型
     */
    private Integer responsibleType;
    private List<Long> inRealWarehouseIds;
    private List<Long> outRealWarehouseIds;

    /**
     * 出向仓库id
     */
    private Long outWarehouseId;

    /**
     * 入向仓库id
     */
    private Long inWarehouseId;

    /**
     * sap采购单号
     */
    private String sapPoNo;

    /**
     * sap交货单号
     */
    private String sapDeliveryCode;

    private List<String> sapDeliveryCodeList;

    /**
     * 创建人工号
     */
    private String empNum;

    private Long modifier;

    /**
     * 单据流转状态 1:部分锁定 2:全部锁定 10:调拨审核通过 11:调拨出库 12:调拨入库/待加工 20:已加工 30:待发货 31:已发货 40:已取消
     */
    private Integer recordStatus;

    /**
     * 工厂编号
     */
    private String factoryCode;

    private Date createStartDate;

    private Date createEndDate;

    private Date updateStartDate;

    private Date updateEndDate;

    /**
     * 商品编号
     */
    private String skuCode;

}
