package com.lyf.scm.common.enums;

/**
 * @Description: 是否的枚举类
 * @author: yaoleiming
 * @data: 2019/9/19 9:27
 */
public enum YesOrNoEnum {

    /**
     * 否
     */
    NO(0, "否"),
    /**
     * 是
     */
    YES(1, "是");

    //类型
    private Integer type;
    //描述
    private String desc;

    YesOrNoEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
    public Integer getType() {
        return type;
    }
    public String getDesc() {
        return desc;
    }

    public static YesOrNoEnum findByType(Integer type) {
        if(type == null) {
            return null;
        }
        for(YesOrNoEnum curr: values()) {
            if(curr.getType().equals(type)) {
                return curr;
            }
        }
        return null;
    }
}
