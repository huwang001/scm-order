package com.lyf.scm.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 该值对象只维护实仓的启用和停用，冻结相关的业务以后新加字段处理
 * @author
 *
 */
public enum FrontRecordStatusEnum {

    INIT(0, "初始"),
    
    ENABLED(1, "审核通过"),
    
    DISABLED(2, "取消订单"),

    LOCK(3, "已锁定"),

    TMS(4, "已派车"),

    CONFIRM_TMS(5, "确认派车"),

    DELIVERY(6, "已发货"),

    HALF_TMS(7, "已部分派车"),
    
    HALF_DELIVERY(8, "已部分派车"),

    WAIT_ALLOCATION(9, "待处理"),

    OUT_ALLOCATION(10, "已出库"),

    IN_ALLOCATION(11, "已入库"),

    HALF_OUT_ALLOCATION(12, "已部分出库"),

    HALF_IN_ALLOCATION(13, "已部分入库"),

    COMPLETE(14, "已完成"),
    
    ALLOT_SUCCESS(15, "分配成功"),
    
    ALLOT_FAIL(16, "分配失败"),
    
    /**
     * C端销售前置单状态
     */
    SO_UNPAID(17, "待支付"),
    
    SO_PAID(18, "已支付"),
    
    SO_PREDICT_RETURN_PART_MATCH(19, "部分匹配"),
    
    SO_PREDICT_RETURN_FULL_MATCH(20, "完全匹配"),
    
    ENABLED_FAIL(21,"确认失败"),
    
    EXCEPTION_RECORD(22,"异常单据");

    /**
     * 类型
     */
    private Integer status;
    
    /**
     * 类型
     */
    private String desc;
    
    FrontRecordStatusEnum(Integer status, String desc) {
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
        for (FrontRecordStatusEnum ele : values()) {
            if(ele.getStatus().equals(status)) {
                return ele.getDesc();
            }
        }
        return null;
    }

    /**
     * 获取状态集合
     * 
     * @return
     */
    public static Map<Integer,String> getFrontRecordStatusList(){
        Map<Integer,String> map=new HashMap<>();
        for (FrontRecordStatusEnum ele : values()) {
            map.put(ele.getStatus() , ele.getDesc());
        }
        return map;
    }
    
}