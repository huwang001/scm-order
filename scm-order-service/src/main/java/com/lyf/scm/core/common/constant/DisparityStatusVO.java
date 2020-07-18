package com.lyf.scm.core.common.constant;

public enum DisparityStatusVO {


    INIT(0, "初始"),

    HAS_DUTY(1, "已全部定责"),

    WAIT(2, "待过账"),

    COMPLETE(3, "处理成功"),

    FAIL(4, "处理失败");

    //类型
    private Integer status;
    //描述
    private String desc;

    DisparityStatusVO(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
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
        for (DisparityStatusVO ele : values()) {
            if (ele.getStatus().equals(status)) {
                return ele.getDesc();
            }
        }
        return null;
    }
}
