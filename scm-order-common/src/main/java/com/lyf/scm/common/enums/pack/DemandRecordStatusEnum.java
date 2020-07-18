package com.lyf.scm.common.enums.pack;

/**
 * @Desc:需求单单据状态
 * @author:Huangyl
 * @date: 2020/7/6
 */
public enum DemandRecordStatusEnum {

    INIT(0, "初始"),
    CONFIRMED(1, "已确认"),
    CANCEL(2, "已取消"),
    PART_PACK(3, "部分包装"),
    COMPLETE_PACK(4, "已完成包装");

    private Integer status;

    private String desc;

    DemandRecordStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static String getDescByStatus(Integer status) {
        for (DemandRecordStatusEnum ele : values()) {
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
