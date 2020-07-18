package com.lyf.scm.common.enums.shopReturn;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/15
 */
public enum ShopReturnRecordTypeEnum {

    /**
     * 直营退货
     */
    DIRECT_RETURN(1, "直营退货"),
    /**
     * 加盟退货
     */
    JOIN_RETURN(2, "加盟退货"),
    /**
     * 加盟商退货
     */
    ALLIANCE_BUSINESS_RETURN(5, "加盟商退货");

    //类型
    private Integer type;
    //描述
    private String desc;

    ShopReturnRecordTypeEnum(Integer type, String desc) {
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
