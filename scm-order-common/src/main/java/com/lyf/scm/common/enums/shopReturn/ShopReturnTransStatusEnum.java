package com.lyf.scm.common.enums.shopReturn;

public enum ShopReturnTransStatusEnum {

    INIT(0, "未推送"),
    UN_PUSH(1, "待推送"),
    PUSHED(2, "推送完成");

    private Integer status;

    private String desc;

    ShopReturnTransStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
