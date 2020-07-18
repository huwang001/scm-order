package com.lyf.scm.common.enums;

/**
 * 库存单据信息自定义枚举
 */
public enum StockRecordInfoTypeEnum {

    SHOP_REPLENISH_CREATE(1, "创建门店补货单"),

    SHOP_REPLENISH_CONFIRM(2, "门店确认收货"),

    SHOP_RETURN_CREATE(3, "创建门店退货单"),

    SHOP_RETURN_CONFIRM(4, "门店退货单确认"),

    SHOP_ALLOCATION(5, "创建门店调拨单"),

    SHOP_INVENTORY(6, "创建门店盘点单"),

    SHOP_FORETASTE(7, "创建门店试吃单"),

    SHOP_CONSUME_CREATE(8, "创建门店报废单"),

    SHOP_CONSUME_CONFIRM(9, "确认门店报废单"),

    PURCHASE_ORDER(10, "仓库采购"),

    PURCHASE_RETURN(11, "采购退货");

    private Integer type;
    private String desc;

    StockRecordInfoTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() { return desc; }
}