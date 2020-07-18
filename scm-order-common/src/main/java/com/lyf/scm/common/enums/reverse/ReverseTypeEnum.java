package com.lyf.scm.common.enums.reverse;

import lombok.Data;

/**
 * @Desc:冲销单单据类型
 * @author:Huangyl
 * @date: 2020/7/17
 */
public enum  ReverseTypeEnum {

    OUT_STOCK_REVERSE(1,"出库单冲销"),
    IN_STOCK_REVERSE(2,"入库单冲销");

    /**
     * 类型
     */
    private Integer type;

    /**
     * 描述
     */
    private String desc;

    ReverseTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
