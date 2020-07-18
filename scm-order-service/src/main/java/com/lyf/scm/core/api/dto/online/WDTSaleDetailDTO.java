package com.lyf.scm.core.api.dto.online;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description 旺店通so单列表明细
 * @Author: liuyao
 * @Date: 2020/7/2
 */
@Data
@EqualsAndHashCode
public class WDTSaleDetailDTO {


    /**
     * 商品sku编码
     */
    private Long skuId;
    /**
     * 商品sku编码
     */
    private String skuCode;
    /**
     * 商品sku编码
     */
    private String skuName;
    /**
     * 数量
     */
    private BigDecimal skuQty;
    /**
     * 单位
     */
    private String unit;
    /**
     * 单位code
     */
    private String unitCode;

    /**
     * 拆单状态： 0待分拆到do单 1 已分拆到do单
     */
    private Integer splitStatus;

    private String lineNo;

    /**
     * 1赠品 2非赠品
     */
    private Integer giftType;

    /**
     * 所属主品的商品编码,为null则表示非组合品
     */
    private String parentSkuCode;
    private String parentSkuName;

    private String doCode;
    private BigDecimal allotQty;
    private Integer doRecordStatus;


    private String wareHouseRecordCode;
    private String wmsSyncStatus;
    private String wareHouseRecordStatus;
    private String fulfillmentSyncStatus;


    private Long realWarehouseId;
    private String realWarehouseCode;
    private String realWarehouseName;
    private Date mergeTime;

    public String getWmsSyncStatus() {
        if (wareHouseRecordCode == null) {
            wmsSyncStatus = null;
        }
        return wmsSyncStatus;
    }

    public String getWareHouseRecordStatus() {
        if (wareHouseRecordCode == null) {
            wareHouseRecordStatus = null;
        }
        return wareHouseRecordStatus;
    }

    public String getFulfillmentSyncStatus() {
        if (wareHouseRecordCode == null) {
            fulfillmentSyncStatus = null;
        }
        return fulfillmentSyncStatus;
    }


    public void setWareHouseRecordStatus(int recordStatus) {
        if (recordStatus == 0) {
            this.wareHouseRecordStatus = "初始状态";
        } else if (recordStatus == 11) {
            this.wareHouseRecordStatus = "已出库";
        } else if (recordStatus == 2) {
            this.wareHouseRecordStatus = "已取消";
        } else {
            this.wareHouseRecordStatus = "";
        }
    }

    public void setWmsSyncStatus(int wmsSyncStatus) {
        if (wmsSyncStatus == 0) {
            this.wmsSyncStatus = "无需下发";
        } else if (wmsSyncStatus == 1) {
            this.wmsSyncStatus = "待下发";
        } else if (wmsSyncStatus == 2) {
            this.wmsSyncStatus = "已下发";
        } else {
            this.wmsSyncStatus = "已撤回";
        }
    }

    public void setFulfillmentSyncStatus(int syncStatus) {
        if ("已出库".equals(this.wareHouseRecordStatus)) {
            if (syncStatus == 0) {
                this.fulfillmentSyncStatus = "无需同步";
            } else if (syncStatus == 1) {
                this.fulfillmentSyncStatus = "待同步";
            } else if (syncStatus == 2) {
                this.fulfillmentSyncStatus = "已同步";
            } else {
                this.fulfillmentSyncStatus = "";
            }
        } else {
            this.fulfillmentSyncStatus = "无需同步";
        }
    }


}
