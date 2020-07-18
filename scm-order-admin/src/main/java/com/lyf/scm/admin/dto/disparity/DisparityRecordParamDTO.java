package com.lyf.scm.admin.dto.disparity;

import com.lyf.scm.common.model.Pagination;
import lombok.Data;

import java.util.Date;

@Data
public class DisparityRecordParamDTO extends Pagination {

    /**
     * 单据类型
     */
    private Integer recordType;
    /**
     * 入向实体仓库名称
     */
    private String inRealWarehouseName;

    /**
     * 出向实体仓库名称
     */
    private String outRealWarehouseName;

    /**
     * 入向实仓编码（仓库外部编码）
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

    /**
     * 出向仓库id
     */
    private Long outWarehouseId;

    /**
     * 入向仓库id
     */
    private Long inWarehouseId;

    /**
     * 责任方
     */
    private String responsible;
    /**
     * sap采购单号
     */
    private String sapPoNo;

    /**
     * sap交货单号
     */
    private String sapDeliveryCode;

    /**
     * 创建人工号
     */
    private String empNum;

    private Long modifier;

    /**
     * 单据状态
     */
    private Integer recordStatus;

    /**
     * 工厂编码
     */
    private String factoryCode;

    private Date createStartDate;

    private Date createEndDate;

    private Date updateStartDate;

    private Date updateEndDate;

    /**
     * 商品sku编码
     */
    private String skuCode;

}
