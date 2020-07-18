package com.lyf.scm.common.enums;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 该值对象只维护实仓的启用和停用，冻结相关的业务以后新加字段处理
  */
public enum WarehouseRecordStatusEnum {

    /**
     * 初始状态
     */
    INIT(0, "初始", 9),
    
    /**
     * 取消订单
     */
    DISABLED(2, "取消订单", 9),
    
    /**
     * 已出库
     */
    OUT_ALLOCATION(11, "已出库", 1),
    
    /**
     * 已入库
     */
    IN_ALLOCATION(12, "已入库", 2),
    
    /**
     * 已完成
     */
    COMPLETE(15, "已完成", 9);
	

    /**
     * 类型
     */
    private Integer status;
    
    /**
     * 描述
     */
    private String desc;
    
    /**
     * 页面状态
     */ 
    private Integer pageType;

    WarehouseRecordStatusEnum(Integer status, String desc, Integer pageType) {
        this.status = status;
        this.desc = desc;
        this.pageType = pageType;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getPageType() {
        return pageType;
    }

    /**
     * 根据状态获取文字描述
     *
     * @param status
     * @return
     */
    public static String getDescByType(Integer status) {
        for (WarehouseRecordStatusEnum ele : values()) {
            if (ele.getStatus().equals(status)) {
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
    public static Map<Integer, String> getWarehouseRecordStatusList() {
        Map<Integer, String> map = new HashMap<>();
        for (WarehouseRecordStatusEnum ele : values()) {
            map.put(ele.getStatus(), ele.getDesc());
        }
        return map;
    }

    /**
     * 获取入库单状态集合
     *
     * @return
     */
    public static Map<Integer, String> getInWarehouseRecordStatusList() {
        Map<Integer, String> map = new HashMap<>();
        for (WarehouseRecordStatusEnum ele : values()) {
            if (2 == ele.pageType) {
                map.put(ele.getStatus(), ele.getDesc());
            }
        }
        return map;
    }
    
    /**
     * 根据数据库中已存在的所有入库单的状态统计出库单状态集合
     *
     * @param inWarehouseRecordStatus
     * @return
     */
    public static Map<Integer, String> getInWarehouseRecordStatusList(List<Integer> inWarehouseRecordStatus) {
        Map<Integer, String> map = new HashMap<>();
        for (WarehouseRecordStatusEnum ele : values()) {
            if (inWarehouseRecordStatus.contains(ele.getStatus())) {
                map.put(ele.getStatus(), ele.getDesc());
            }
        }
        return map;
    }
    
    /**
     * 获取出库单状态集合
     *
     * @return
     */
    public static Map<Integer, String> getOutWarehouseRecordStatusList() {
        Map<Integer, String> map = new HashMap<>();
        for (WarehouseRecordStatusEnum ele : values()) {
            if (1 == ele.pageType) {
                map.put(ele.getStatus(), ele.getDesc());
            }
        }
        return map;
    }

    /**
     * 根据数据库中已存在的所有出库单的状态统计出库单状态集合
     *
     * @param outWarehouseRecordStatus
     * @return
     */
    public static Map<Integer, String> getOutWarehouseRecordStatusList(List<Integer> outWarehouseRecordStatus) {
        Map<Integer, String> map = new HashMap<>();
        for (WarehouseRecordStatusEnum ele : values()) {
            if (outWarehouseRecordStatus.contains(ele.getStatus())) {
                map.put(ele.getStatus(), ele.getDesc());
            }
        }
        return map;
    }
    
}
