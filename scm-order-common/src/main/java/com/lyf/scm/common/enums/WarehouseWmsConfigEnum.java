package com.lyf.scm.common.enums;

/**
 * 实仓wms配置
 */
public enum WarehouseWmsConfigEnum {
    /**
     * 大福
     */
    DF(1, "大福"),
    /**
     * 旺店通
     */
    WDT(2,"旺店通"),
    /**
     * SAP
     */
    SAP(3,"SAP"),
    /**
     * 欧电云
     */
    ODY(4,"欧电云"),
    /**
     * 邮政
     */
    EMS(5,"邮政"),
    /**
     *
     */
    SJ(6,"商家"),
    /**
     *
     */
    VR(7,"虚拟商品")
    ;

    /**
     * 类型
     */
    private Integer type;
    /**
     * 描述
     */
    private String desc;

    WarehouseWmsConfigEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
    public Integer getType() {
        return type;
    }
    public String getDesc() {
        return desc;
    }

    public static String getDescByType(Integer type) {
        for (WarehouseWmsConfigEnum ele : values()) {
            if(ele.getType().equals(type)) {
                return ele.getDesc();
            }
        }
        return null;
    }
}
