package com.lyf.scm.common.enums;

import com.lyf.scm.common.enums.pack.PackTypeEnum;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/9
 */
public enum CombineTypeEnum {
    /**
     * 原料
     */
    RAW_MATERIAL(0, "原料"),

    /**
     * 组合
     */
    COMBINATION(1, "组合"),

    /**
     * 组装
     */
    ASSEMBLE(2, "组装");


    /**
     * 类型
     */
    private Integer type;

    /**
     * 描述
     */
    private String desc;

    CombineTypeEnum(Integer type, String desc) {
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
    public static CombineTypeEnum getDescByType(Integer type) {
        for (CombineTypeEnum ele : values()) {
            if (ele.getType().equals(type)) {
                return ele;
            }
        }
        return null;
    }

}
