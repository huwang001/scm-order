package com.lyf.scm.core.common.constant;

public enum DisparityResponsibleTypeVO {


    SHOP(1, "门店责任"),

    WAREHOUSE(2, "仓库责任"),

    LOGISTIC(3, "物流责任");



    //类型
    private Integer type;
    //描述
    private String desc;

    DisparityResponsibleTypeVO(Integer type, String desc) {
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
     * @param status
     * @return
     */
    public static String getDescByType(Integer status) {
        for (DisparityResponsibleTypeVO ele : values()) {
            if (ele.getType().equals(status)) {
                return ele.getDesc();
            }
        }
        return null;
    }
}
