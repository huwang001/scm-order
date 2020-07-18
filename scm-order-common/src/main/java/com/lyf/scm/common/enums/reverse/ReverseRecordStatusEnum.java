package com.lyf.scm.common.enums.reverse;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description: 冲销单单据状态Enum <br>
 *
 * @Author wwh 2020/7/16
 */
public enum ReverseRecordStatusEnum {
	
	IS_CREATE(1, "已新建"),
	
	IS_CANCEL(2, "已取消"),
	 
	IS_CONFIRM(3, "已确认"),
	 
	IS_OUT_STOCK(11, "已出库"),
	
	IS_IN_STOCK(12, "已入库"),
	
	IS_POST(13, "已过账");
	
	/**
	 * 状态
	 */
	private Integer status;

	/**
	 * 描述
	 */
    private String desc;
    
	private ReverseRecordStatusEnum(Integer status, String desc) {
		this.status = status;
		this.desc = desc;
	}

	public Integer getStatus() {
		return status;
	}

	public String getDesc() {
		return desc;
	}
	
	public static ReverseRecordStatusEnum queryReverseRecordStatus(Integer status) {
        if(status == null) {
            return null;
        }
        for(ReverseRecordStatusEnum reverseRecordStatusEnum: values()) {
            if(reverseRecordStatusEnum.getStatus().equals(status)) {
                return reverseRecordStatusEnum;
            }
        }
        return null;
    }
	
    public static Map<Integer, String> queryAllReverseRecordStatus() {
    	Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        for (ReverseRecordStatusEnum reverseRecordStatusEnum : values()) {
            map.put(reverseRecordStatusEnum.getStatus(), reverseRecordStatusEnum.getDesc());
        }
        return map;
    }
    
}