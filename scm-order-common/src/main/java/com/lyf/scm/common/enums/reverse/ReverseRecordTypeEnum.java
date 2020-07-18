package com.lyf.scm.common.enums.reverse;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description: 冲销单单据类型Enum <br>
 *
 * @Author wwh 2020/7/16
 */
public enum ReverseRecordTypeEnum {

    DS_REPLENISH_OUT_WAREHOUSE_RECORD(10, "直营门店补货出库单"),

    DS_REPLENISH_IN_SHOP_RECORD(11, "直营门店补货入库单"),
    
    DS_RETURN_OUT_WAREHOUSE_RECORD(22, "直营门店退货出库单"),

    DS_RETURN_IN_WAREHOUSE_RECORD(23, "直营门店退货入库单"),

    WH_ALLOCATION_OUT_WAREHOUSE_RECORD(29, "仓库调拨出库单"),

    WH_ALLOCATION_IN_WAREHOUSE_RECORD(30, "仓库调拨入库单"),

    WH_COLD_CHAIN_IN_RECORD(40, "冷链越库入库单"),

    WH_COLD_CHAIN_OUT_RECORD(41, "冷链越库出库单"),
    
    WH_CHAIN_DIRECT_OUT_RECORD(44, "直送越库出库单"),

    SHOP_CHAIN_DIRECT_IN_RECORD(45, "门店直送入库单"),

    PACKAGE_FINISH_PACK_OUT_RECORD(61, "包装需求完成单组装出库"),

    PACKAGE_FINISH_PACK_IN_RECORD(62, "包装需求完成单组装入库"),

    PACKAGE_FINISH_UNPACK_OUT_RECORD(63, "包装需求完成单反拆出库"),

    PACKAGE_FINISH_UNPACK_IN_RECORD(64, "包装需求完成单反拆入库");

    /**
     * 类型
     */
    private Integer type;

    /**
     * 描述
     */
    private String desc;

    ReverseRecordTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static ReverseRecordTypeEnum queryReverseRecordType(Integer type) {
        if(type == null) {
            return null;
        }
        for(ReverseRecordTypeEnum reverseRecordTypeEnum: values()) {
            if(reverseRecordTypeEnum.getType().equals(type)) {
                return reverseRecordTypeEnum;
            }
        }
        return null;
    }
	
    public static Map<Integer, String> queryAllReverseRecordType() {
    	Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        for (ReverseRecordTypeEnum reverseRecordTypeEnum : values()) {
            map.put(reverseRecordTypeEnum.getType(), reverseRecordTypeEnum.getDesc());
        }
        return map;
    }

}