package com.lyf.scm.common.enums;

/**
 * @Description: 寻源类型
 * @author: yaoleiming
 * @data: 2019/9/18 16:34
 */
public enum AllotTypeEnum {
    /**
     * 指定单据寻源
     */
    BY_ORDER(1, "指定单据寻源"),
    /**
     * 当日寻源
     */
    BY_DATE(2, "当日寻源"),
    /**
     * 指定工厂代码
     */
    BY_FACTORY_CODE(3, "按地区寻源");

    //类型
    private Integer type;
    //描述
    private String desc;

    AllotTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
    public Integer getType() {
        return type;
    }
    public String getDesc() {
        return desc;
    }

    public static AllotTypeEnum findByType(Integer type) {
        if(type == null) {
            return null;
        }
        for(AllotTypeEnum curr: values()) {
            if(curr.getType().equals(type)) {
                return curr;
            }
        }
        return null;
    }
}
