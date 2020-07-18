package com.lyf.scm.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 该值对象只维护前置单据类型
 *
 * @author
 */
public enum FrontRecordTypeEnum {

    /**
     * 大仓采购入库
     */
    WAREHOUSE_PURCHASE_RECORD(1, "大仓采购入库", "WPR"),

    /**
     * 大仓采购退货
     */
    WAREHOUSE_RETURN_GOODS_RECORD(2, "大仓采购退货", "WRGR"),

    /**
     * 仓库反拆任务单
     */
    WAREHOUSE_REVERSE_DISASSEMBLY_TASK_RECORD(3, "仓库反拆任务单", "WRDTR"),

    /**
     * 仓库加工任务单
     */
    WAREHOUSE_ASSEMBLE_TASK_RECORD(6, "仓库加工任务单", "WATR"),

    /**
     * 门店组装反拆任务单
     */
//    SHOP_REVERSE_DISASSEMBLY_TASK_RECORD(9, "门店组装反拆任务单", "SRDTR"),

    /**
     * 门店组装加工任务单
     */
//    SHOP_ASSEMBLE_TASK_RECORD(12, "门店组装加工任务单", "SATR"),

    /**
     * 直营门店补货单
     */
    SHOP_REPLENISHMENT_RECORD(15, "直营门店补货单", "SHRR"),

    /**
     * 电商零售
     */
    ONLINE_SALE_RECORD(99, "电商零售", "OSR"),

    /**
     * 冷链采购单
     */
    COLD_CHAIN_SURPASS_WAREHOUSE_RECORD(18, "冷链采购单", "CCSWR"),

    /**
     * 门店冷链交货单
     */
    SHOP_COLD_CHAIN_DELIVERY_RECORD(19, "门店冷链交货单", "SCCDR"),

    /**
     * 供应商直送采购
     */
    SUPPLIER_DIRECT_DELIVERY_RECORD(20, "供应商直送采购", "SDDR"),

    /**
     * 门店供应商直送交货单
     */
    SHOP_SUPPLIER_DIRECT_DELIVERY_RECORD(21, "门店供应商直送交货单", "SSDDR"),

    /**
     * 仓库调拨
     */
    WAREHOUSE_ALLOCATION_RECORD(22, "仓库调拨", "WAR"),

    /**
     * 门店调拨
     */
    SHOP_ALLOCATION_RECORD(23, "门店调拨", "S"),

    /**
     * 门店试吃调整单
     */
    SHOP_FORETASTE_RECORD(24, "门店试吃调整单", "SFR"),

    /**
     * 仓库盘点单
     */
    WAREHOUSE_INVENTORY_RECORD(25, "仓库盘点单", "WIR"),

    /**
     * 门店盘点单
     */
    SHOP_INVENTORY_RECORD(26, "门店盘点单", "SIR"),

    /**
     * 直营门店退货
     */
    DIRECT_SHOP_RETURN_GOODS_RECORD(27, "直营门店退货", "DSRGR"),

    /**
     * 加盟门店退货
     */
    JOIN_SHOP_RETURN_GOODS_RECORD(28, "加盟门店退货", "JSRGR"),

    /**
     * 加盟门店补货单
     */
    JOIN_SHOP_REPLENISHMENT_RECORD(30, "加盟门店补货单", "JSIR"),

    /**
     * 门店零售
     */
    SHOP_RETAIL_RECORD(31, "门店零售", "SRR"),

    /**
     * 门店退货
     */
    SHOP_RETURN_GOODS_RECORD(32, "门店零售退货", "SRRGR"),

    /**
     * 委外入库
     */
//    OUTSOURCE_IN_RECORD(33, "委外入库", "OUSIR"),

    /**
     * 委外出库
     */
//    OUTSOURCE_OUT_RECORD(34, "委外出库", "OUSOR"),

    /**
     * 委外虚拟入库
     */
//    OUTSOURCE_VIRTUAL_IN_RECORD(35, "委外虚拟入库", "OUVIR"),

    /**
     * 委外虚拟出库
     */
//    OUTSOURCE_VIRTUAL_OUT_RECORD(36, "委外虚拟出库", "OUVOR"),

    /**
     * 仓库报废调整单
     */
    WAREHOUSE_CONSUME_ADJUST_RECORD(37, "仓库报废调整单", "WACAR"),

    /**
     * 仓库领用调整单
     */
    WAREHOUSE_USE_ADJUST_RECORD(38, "仓库领用调整单", "WUAR"),
    /**
     * 门店报废调整单
     */
//    SHOP_CONSUME_ADJUST_RECORD(39, "门店报废调整单", "SCAR"),

    /**
     * 仓库损益调整单
     */
//    WAREHOUSE_INCREASE_OR_DECREASE_ADJUST_RECORD(40, "仓库损益调整单", "WIODAR"),

    /**
     * 退货预入库
     */
    PREDICT_RETURN_RECORD(42, "退货预入库", "PR"),

    /**
     * 加盟商补货
     */
//    ALLIANCE_BUSINESS_REPLENISHMENT_RECORD(44, "加盟商补货", "ABRR"),

    /**
     * 加盟托管补货单
     */
//    JOIN_TRUSTEESHIP_REPLENISHMENT_RECORD(45, "加盟托管补货单", "JTRR"),

    /**
     * 加盟商退货单
     */
//    ALLIANCE_BUSINESS_RETURN_GOOD_RECORD(46, "加盟商退货单", "ABRG"),
    /**
     * 仓库质量变更调整单
     */
//    QUALITY_CHANGE_ADJUST_RECORD(47, "仓库质量变更调整单", "QCAR"),

    /**
     * 直营门店补货差异单
     */
    DIRECT_SHOP_REPLENISH_DISPARITY_RECORD(48, "直营门店补货差异单", "DISREP"),

    /**
     * 冷链采购差异单
     */
//    COLD_CHAIN_REPLENISH_DISPARITY_RECORD(49, "冷链采购差异单", "CCRDR"),

    /**
     * 直营门店退货差异单
     */
    DIRECT_SHOP_RETURN_DISPARITY_RECORD(51, "直营门店退货差异单", "DISRET"),

    /**
     * 仓库调拨差异单
     */
//    WAREHOUSE_ALLOCATION_DISPARITY_RECORD(52, "仓库调拨差异单", "WADR"),

    /**
     * 门店确认收货补偿单
     */
//    SHOP_RECEIPT_COMPENSATE_RECORD(53, "门店确认收货补偿单", "SRCR"),

    /**
     * 商家库存调整单
     */
//    ADJUST_MERCHANT_RECORD(55, "商家库存调整单", "AMR"),

    /**
     * 包材出库调整单
     */
//    PACKAGE_MATERIAL_OUT_RECORD(56, "包材出库调整单", "POR"),

    /**
     * 包材出库调整单
     */
//    PACKAGE_MATERIAL_IN_RECORD(57, "包材入库调整单", "PIR"),

    /**
     * 库存对比调整单
     */
//    ADJUST_CHECK_STOCK_RECORD(58, "库存对比调整单", "ACSR"),

    /**
     * 门店退货调整单
     */
//    SHOP_RETURN_INVENTORY_RECORD(59, "门店退货调整单", "SRIR"),

    /**
     * 销售中心预约do单
     */
    RESERVATION_DO_RECORD(90, "销售中心预约do单", "REDO"),

    /**
     * 包装需求单
     */
    PACKAGE_NEED_RECORD(61, "包装需求单", "PA"),

    /**
     * 包装需求完成单
     */
    PACKAGE_FINISH_RECORD(62, "包装需求完成单", "PF"),

    /**
     * 出入库冲销单
     */
    OUT_IN_REVERSE(63, "出入库冲销单", "OIR"),

    /**
     * 预约退货单
     */
    GROUP_RETURN_RECORD(91, "预约退货单", "GPRN");

    /**
     * 类型
     */
    private Integer type;

    /**
     * 描述
     */
    private String desc;

    /**
     * 编号
     */
    private String code;

    FrontRecordTypeEnum(Integer type, String desc, String code) {
        this.type = type;
        this.desc = desc;
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }

    public static String getDescByType(Integer type) {
        for (FrontRecordTypeEnum ele : values()) {
            if (ele.getType().equals(type)) {
                return ele.getDesc();
            }
        }
        return null;
    }
    
    public static String getCodeByType(Integer type) {
        for (FrontRecordTypeEnum ele : values()) {
            if (ele.getType().equals(type)) {
                return ele.getCode();
            }
        }
        return null;
    }
    
    public static FrontRecordTypeEnum getEnumByType(Integer type) {
        for (FrontRecordTypeEnum ele : values()) {
            if (ele.getType().equals(type)) {
                return ele;
            }
        }
        return null;
    }

    /**
     * 获取类型集合
     *
     * @return
     */
    public static Map<Integer, String> getFrontRecordTypeList() {
        Map<Integer, String> map = new HashMap<>();
        for (FrontRecordTypeEnum ele : values()) {
            map.put(ele.getType(), ele.getDesc());
        }
        return map;
    }

    /**
     * 获取加工单类型集合
     *
     * @return
     */
    public static Map<Integer, String> getWarehouseAssembleRecordTypeList() {
        Map<Integer, String> map = new HashMap<>();
        map.put(FrontRecordTypeEnum.WAREHOUSE_REVERSE_DISASSEMBLY_TASK_RECORD.getType(), FrontRecordTypeEnum.WAREHOUSE_REVERSE_DISASSEMBLY_TASK_RECORD.getDesc());
        map.put(FrontRecordTypeEnum.WAREHOUSE_ASSEMBLE_TASK_RECORD.getType(), FrontRecordTypeEnum.WAREHOUSE_ASSEMBLE_TASK_RECORD.getDesc());
        return map;
    }


    /**
     * 获取前置单类型和code集合
     *
     * @return
     */
    public static Map<String, Integer> getFrontRecordTypeCodeList() {
        Map<String, Integer> map = new HashMap<>();
        for (FrontRecordTypeEnum ele : values()) {
            map.put(ele.getCode(), ele.getType());
        }
        return map;
    }

}