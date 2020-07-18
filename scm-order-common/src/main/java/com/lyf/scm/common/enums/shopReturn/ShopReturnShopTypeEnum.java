package com.lyf.scm.common.enums.shopReturn;

public enum ShopReturnShopTypeEnum {

    /**
     * 直营
     */
    JOIN_SALE(1, "加盟"),
    /**
     * 加盟非托管
     */
    DIRECT_SALE(2, "直营"),
    /**
     * 加盟托管
     */
    JOIN_HOSTING_SALE(3, "加盟联营");


    //类型
    private Integer type;
    //描述
    private String desc;

    ShopReturnShopTypeEnum(Integer type, String desc) {
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
