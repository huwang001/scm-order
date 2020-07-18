package com.lyf.scm.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum SyncTmsBStatusEnum {

    /**
     * 无需同步
     */
    NO_REQUIRED(0, "无需同步"),

    /**
     * 未同步
     */
    UNSYNCHRONIZED(1, "未同步"),

    /**
     * 已同步
     */
    SYNCHRONIZED(2, "已同步");

    /**
     * 类型
     */
    private Integer status;

    /**
     * 描述
     */
    private String desc;

    SyncTmsBStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public static SyncTmsBStatusEnum findByStatus(Integer status) {
        if (status == null) {
            return null;
        }
        for (SyncTmsBStatusEnum currVo : values()) {
            if (currVo.getStatus().equals(status)) {
                return currVo;
            }
        }
        return null;
    }

    /**
     * 获取状态集合
     *
     * @return
     */
    public static Map<Integer, String> getWmsSyncStatusList() {
        Map<Integer, String> map = new HashMap<>();
        for (SyncTmsBStatusEnum ele : values()) {
            map.put(ele.getStatus(), ele.getDesc());
        }
        return map;
    }
}