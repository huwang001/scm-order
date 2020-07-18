package com.lyf.scm.common.enums;

public enum ShopTypeEnum {

    /**
     * 直营
     */
    DIRECT_SALE(1, "直营"),
    /**
     * 加盟非托管
     */
    JOIN_SALE(3, "加盟非托管"),
    /**
     * 加盟托管
     */
    JOIN_HOSTING_SALE(4, "加盟托管");


    //类型
    private Integer type;
    //描述
    private String desc;
    ShopTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
    public Integer getType() {
        return type;
    }
    public String getDesc() {
        return desc;
    }

}
