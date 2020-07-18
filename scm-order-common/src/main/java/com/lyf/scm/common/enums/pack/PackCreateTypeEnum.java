package com.lyf.scm.common.enums.pack;

import com.lyf.scm.common.enums.CombineTypeEnum;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/9
 */
public enum PackCreateTypeEnum {
    /**
     * 接口创建
     */
    API(1, "接口创建"),

    /**
     * 页面新增
     */
    PAGE(2, "页面新增"),

    /**
     * 导入新增
     */
    IMPORT(3, "导入新增"),

    /**
     * 预约单创建
     */
    ORDER_CREATE(4, "预约单创建");

    /**
     * 类型
     */
    private Integer type;

    /**
     * 描述
     */
    private String desc;

    PackCreateTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据状态获取枚举
     *
     * @param type
     * @return
     */
    public static PackCreateTypeEnum getDescByType(Integer type) {
        for (PackCreateTypeEnum ele : values()) {
            if (ele.getType().equals(type)) {
                return ele;
            }
        }
        return null;
    }

}
