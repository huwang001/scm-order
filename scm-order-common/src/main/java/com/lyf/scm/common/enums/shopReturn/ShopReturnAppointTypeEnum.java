package com.lyf.scm.common.enums.shopReturn;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/15
 */
public enum ShopReturnAppointTypeEnum {

    /**
     * 指定门店仓
     */
    IS_APPOINT(1, "指定门店仓"),
    /**
     * 不指定门店仓
     */
    IS_NOT_APPOINT(0, "不指定门店仓");

    //类型
    private Integer type;
    //描述
    private String desc;

    ShopReturnAppointTypeEnum(Integer type, String desc) {
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
