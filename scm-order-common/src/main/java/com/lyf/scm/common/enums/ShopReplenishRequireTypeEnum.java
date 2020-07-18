package com.lyf.scm.common.enums;

/**
 * @Remark 门店补货配货类型
 * @Date 2020/6/22
 */
public enum ShopReplenishRequireTypeEnum {

    COMMON_REPLENISH(1, "普通配货"),
    ASSIGN_WH_REPLENISH(2, "指定仓库配货");

    //类型
    private Integer type;

    //描述
    private String desc;

    ShopReplenishRequireTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static ShopReplenishRequireTypeEnum findByType(Integer type) {
        if(type == null) {
            return null;
        }
        for(ShopReplenishRequireTypeEnum vo: values()) {
            if(vo.getType().equals(type)) {
                return vo;
            }
        }

        return null;
    }
}
