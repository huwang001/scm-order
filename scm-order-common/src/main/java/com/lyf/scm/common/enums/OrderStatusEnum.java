package com.lyf.scm.common.enums;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @Description: 预约单单据状态状态Enum <br>
 *
 * @Author wwh 2020/4/10
 */
public enum OrderStatusEnum {
	
	INIT_STATUS(0, "初始"),
	
	LOCK_STATUS_PART(1, "部分锁定"),
	 
	LOCK_STATUS_ALL(2, "全部锁定"),
	 
	ALLOT_AUDIT_STATUS_PASSED(10, "调拨审核通过"),
	
	ALLOT_STATUS_OUT(11, "调拨出库"),
	
	ALLOT_STATUS_IN(12, "调拨入库(待加工)"),
	
	PROCESS_STATUS_DONE(20, "已加工"),
	
	DELIVERY_STATUS_WAIT(30, "待发货"),
	
	DELIVERY_STATUS_DONE(31, "已发货"),
	
	CANCEL_STATUS(40, "已取消");
	
	private Integer status;

    private String desc;
    
	private OrderStatusEnum(Integer status, String desc) {
		this.status = status;
		this.desc = desc;
	}

	public Integer getStatus() {
		return status;
	}

	public String getDesc() {
		return desc;
	}
	
	public static OrderStatusEnum getDescByStatus(Integer status) {
        if(status == null) {
            return null;
        }
        for(OrderStatusEnum orderStatusEnum: values()) {
            if(orderStatusEnum.getStatus().equals(status)) {
                return orderStatusEnum;
            }
        }
        return null;
    }
	
    public static Map<Integer, String> getAllOrderStatusList() {
    	Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        for (OrderStatusEnum orderStatusEnum : values()) {
            map.put(orderStatusEnum.getStatus(), orderStatusEnum.getDesc());
        }
        return map;
    }
    
}