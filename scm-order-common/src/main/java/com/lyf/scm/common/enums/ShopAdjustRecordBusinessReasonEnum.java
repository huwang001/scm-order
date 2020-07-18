package com.lyf.scm.common.enums;

/**
 * 门店调整单，业务原因枚举
 */
public enum ShopAdjustRecordBusinessReasonEnum {

    //初始状态
    PROMOTIONS_FORETASTE(5001, "促销试尝");

    //类型
    private Integer reason;
    //描述
    private String desc;

    ShopAdjustRecordBusinessReasonEnum(Integer reason, String desc) {
        this.reason = reason;
        this.desc = desc;
    }

    public Integer getReason() {
        return reason;
    }

    public String getDesc() {
        return desc;
    }


    public static String getDescByType(Integer reason) {
        for (ShopAdjustRecordBusinessReasonEnum ele : values()) {
            if (ele.getReason().equals(reason)) {
                return ele.getDesc();
            }
        }
        return null;
    }

}
