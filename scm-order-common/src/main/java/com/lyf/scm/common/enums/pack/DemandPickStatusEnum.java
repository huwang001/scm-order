package com.lyf.scm.common.enums.pack;

/**
 * @Desc: 领料状态
 * @author:Huangyl
 * @date: 2020/7/6
 */
public enum DemandPickStatusEnum {

        NOT_PICK(1,"未领料"),
        PICKED(2,"已领料");
        private  Integer status;
        private String desc;

    DemandPickStatusEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static String getDescByStatus(Integer status) {
        for (DemandPickStatusEnum ele : values()) {
            if (ele.getStatus().equals(status)) {
                return ele.getDesc();
            }
        }
        return null;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

}
