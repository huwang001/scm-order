package com.lyf.scm.common.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum OrderTypeEnum {



    COMMON(1, "普通订单"),

    CARD(2, "卡券订单");


    private Integer status;

    private String desc;

    private OrderTypeEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public static OrderTypeEnum getDescByStatus(Integer status) {
        if(status == null) {
            return null;
        }
        for(OrderTypeEnum orderTypeEnum: values()) {
            if(orderTypeEnum.getStatus().equals(status)) {
                return orderTypeEnum;
            }
        }
        return null;
    }

    public static Map<Integer, String> getAllOrderStatusList() {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        for (OrderTypeEnum orderTypeEnum : values()) {
            map.put(orderTypeEnum.getStatus(), orderTypeEnum.getDesc());
        }
        return map;
    }
}
