package com.lyf.scm.common.enums;


/**
 * 该值对象只维护出入库页面展示类型
 * @author
 *
 */
public enum WarehouseRecordBusinessTypeEnum {

    /**
     * 出库单
     */
    OUT_WAREHOUSE_RECORD(1, "出库单"),

    /**
     * 入库单
     */
    IN_WAREHOUSE_RECORD(2,"入库单")
    ;

    /**
     * 类型
     */
    private Integer type;
    
    /**
     * 类型
     */
    private String desc;

    WarehouseRecordBusinessTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
    
    public Integer getType() {
        return type;
    }
    
    public String getDesc() {
        return desc;
    }

    public static String getDescByType(Integer type) {
        for (WarehouseRecordBusinessTypeEnum ele : values()) {
            if(ele.getType().equals(type)) {
                return ele.getDesc();
            }
        }
        return null;
    }
    
}