package com.lyf.scm.common.enums.pack;

/**
 * @Desc:
 * @author:Huangyl
 * @date: 2020/7/6
 */
public enum MoveTypeEnum {

    /**
     * 出库
     */
    OUT_STOCK(1,"出库"),

    /**
     * 入库
     */
    IN_STOCK(2,"入库");


    private Integer type;
    private String desc;

    MoveTypeEnum(Integer type, String desc) {
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
        for (MoveTypeEnum ele : values()) {
            if (ele.getType().equals(type)) {
                return ele.getDesc();
            }
        }
        return null;
    }

}
