package com.lyf.scm.common.enums.pack;

public enum PackTypeEnum {

    /**
     * 组装
     */
    PACKAGING(1, "组装"),

    /**
     * 反拆
     */
    UN_PACKAGING(2, "反拆"),

    /**
     * 自定义组合
     */
    SELF_COMPOSE(3, "自定义组合"),

    /**
     * 自定义反拆
     */
    UN_SELF_COMPOSE(4, "自定义反拆"),

    /**
     * 拆箱
     */
    DEVAN(5, "拆箱");

    /**
     * 类型
     */
    private Integer type;

    /**
     * 描述
     */
    private String desc;

    PackTypeEnum(Integer type, String desc) {
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
     * 根据状态获取描述
     *
     * @param type
     * @return
     */
    public static String getDescByType(Integer type) {
        for (PackTypeEnum ele : values()) {
            if (ele.getType().equals(type)) {
                return ele.getDesc();
            }
        }
        return null;
    }

    /**
     * 根据描述获取状态
     *
     * @param desc
     * @return
     */
    public static PackTypeEnum getPackTypeEnumByDesc(String desc) {
        for (PackTypeEnum ele : values()) {
            if (ele.getDesc().equals(desc)) {
                return ele;
            }
        }
        return null;
    }

    /**
     * 是否是自定义组合和自定义反拆
     *
     * @param type
     * @return
     */
    public static boolean isCompose(Integer type) {
        if (PackTypeEnum.SELF_COMPOSE.getType().equals(type) ||
                PackTypeEnum.UN_SELF_COMPOSE.getType().equals(type)) {
            return true;
        }
        return false;
    }
}