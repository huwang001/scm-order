package com.lyf.scm.common.enums;

public enum RwRecordPoolStatusEnum {

    /**
     * 初始
     */
    INIT(0, "初始"),
    /**
     * 待合单
     */
    PRE_MERGE(99, "待合单"),
    /**
     * 已合单
     */
    MERGED(100, "已合单"),
    /**
     * 已取消
     */
    CANCELED(2, "已取消");

    //类型
    private Integer status;
    //描述
    private String desc;

    RwRecordPoolStatusEnum(Integer status, String desc) {
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
        for (RwRecordPoolStatusEnum ele : values()) {
            if (ele.getStatus().equals(status)) {
                return ele.getDesc();
            }
        }
        return null;
    }
}
