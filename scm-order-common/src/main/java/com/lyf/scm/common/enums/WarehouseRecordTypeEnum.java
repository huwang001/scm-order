package com.lyf.scm.common.enums;


import java.util.HashMap;
import java.util.Map;


/**
 * 该值对象只维护出入库单据类型
 */
public enum WarehouseRecordTypeEnum {

    SHOP_RETAIL_WAREHOUSE_RECORD(1, "门店零售出库单", "SRWR", 1, 9),

    SHOP_RETURN_GOODS_WAREHOUSE_RECORD(2, "门店退货入库单", "SRGWR", 2, 9),

    SHOP_INVENTORY_OUT_WAREHOUSE_RECORD(3, "门店盘点出库单", "SIOWR", 1, 1),

    SHOP_INVENTORY_IN_WAREHOUSE_RECORD(4, "门店盘点入库单", "SIIWR", 2, 2),

    SHOP_ASSEMBLE_OUT_WAREHOUSE_RECORD(5, "门店包装出库单", "SHOWR", 1, 1),

    SHOP_ASSEMBLE_IN_WAREHOUSE_RECORD(6, "门店包装入库单", "SHIWR", 2, 9),

    SHOP_SPIT_OUT_WAREHOUSE_RECORD(7, "门店反拆出库单", "SSOWR", 1, 1),

    SHOP_SPIT_IN_WAREHOUSE_RECORD(8, "门店反拆入库单", "SSIWR", 2, 9),

    SHOP_FORETASTE_OUT_WAREHOUSE_RECORD(9, "门店试吃出库单", "SFOWR", 1, 1),

    DS_REPLENISH_OUT_WAREHOUSE_RECORD(10, "直营门店补货出库单", "DROWR", 1, 9),

    DS_REPLENISH_IN_SHOP_RECORD(11, "直营门店补货入库单", "DRISR", 2, 9),

    LS_REPLENISH_OUT_WAREHOUSE_RECORD(12, "加盟门店补货大仓出库单", "LROWR", 1, 9),

    LS_REPLENISH_IN_ZWAREHOUSE_RECORD(13, "加盟门店补货Z入库单", "LRIZW", 2, 9),

    LS_REPLENISH_OUT_ZWAREHOUSE_RECORD(14, "加盟门店补货Z出库单", "LROZW", 1, 9),

    LS_REPLENISH_IN_SHOP_RECORD(15, "加盟门店补货入库单", "LRISR", 2, 9),

    PURCHASE_IN_WAREHOUSE_RECORD(16, "大仓采购入库单", "PIWR", 2, 9),

    PURCHASE_OUT_WAREHOUSE_RECORD(17, "大仓采购退库单", "POWR", 1, 9),

    WAREHOUSE_INVENTORY_OUT_WAREHOUSE_RECORD(18, "仓库盘点出库单", "WIOWR", 1, 9),

    WAREHOUSE_INVENTORY_IN_WAREHOUSE_RECORD(19, "仓库盘点入库单", "WIIWR", 2, 9),

    SHOP_ALLOCATION_OUT_WAREHOUSE_RECORD(20, "门店调拨出库单", "SAOWR", 1, 1),

    SHOP_ALLOCATION_IN_WAREHOUSE_RECORD(21, "门店调拨入库单", "SAIWR", 2, 2),

    DS_RETURN_OUT_WAREHOUSE_RECORD(22, "直营门店退货出库单", "DREOW", 1, 9),

    DS_RETURN_IN_WAREHOUSE_RECORD(23, "直营门店退货入库单", "DREIW", 2, 9),

    LS_RETURN_OUT_WAREHOUSE_RECORD(24, "加盟门店退货出库单", "LREOW", 1, 9),

    LS_RETURN_IN_ZWAREHOUSE_RECORD(25, "加盟门店退货入Z库单", "LREZI", 2, 9),

    LS_RETURN_OUT_ZWAREHOUSE_RECORD(26, "加盟门店退货出Z库单", "LREZO", 1, 9),

    LS_RETURN_IN_WAREHOUSE_RECORD(27, "加盟门店退货入库单", "LREIW", 2, 9),

    CONSUME_OUT_WAREHOUSE_RECORD(28, "仓库报废出库单", "COWR", 1, 9),

    WH_ALLOCATION_OUT_WAREHOUSE_RECORD(29, "仓库调拨出库单", "WAOWR", 1, 9),

    WH_ALLOCATION_IN_WAREHOUSE_RECORD(30, "仓库调拨入库单", "WAIWR", 2, 9),

    OUTSOURCE_IN_RECORD(31, "委外入库", "OSIR", 2, 9),

    OUTSOURCE_OUT_RECORD(32, "委外出库", "OSOR", 1, 9),

    OUTSOURCE_VIRTUAL_IN_RECORD(33, "委外虚拟入库", "OSVIR", 2, 9),

    OUTSOURCE_VIRTUAL_OUT_RECORD(34, "委外虚拟出库", "OSVOR", 1, 9),

    WAREHOUSE_CONSUME_ADJUST_RECORD(35, "仓库损耗调整单", "WCAR", 1, 9),

    WAREHOUSE_ASSEMBLE_OUT_RECORD(36, "仓库组装加工出库", "WAOR", 1, 1),

    WAREHOUSE_ASSEMBLE_IN_RECORD(37, "仓库组装加工入库", "WAAIR", 2, 2),

    WAREHOUSE_SPIT_OUT_RECORD(38, "仓库反拆加工出库", "WSOR", 1, 1),

    WAREHOUSE_SPIT_IN_RECORD(39, "仓库反拆加工入库", "WSIR", 2, 2),

    WH_COLD_CHAIN_IN_RECORD(40, "冷链越库入库单", "WHCCI", 2, 9),

    WH_COLD_CHAIN_OUT_RECORD(41, "冷链越库出库单", "WHCCO", 1, 9),//**

    SHOP_COLD_CHAIN_IN_RECORD(42, "门店冷链入库单", "SCCIR", 2, 9),

    WH_CHAIN_DIRECT_IN_RECORD(43, "直送越库入库单", "WHCDI", 2, 9),

    WH_CHAIN_DIRECT_OUT_RECORD(44, "直送越库出库单", "WHCDO", 1, 9),//**

    SHOP_CHAIN_DIRECT_IN_RECORD(45, "门店直送入库单", "SCDIR", 2, 9),

    ONLINE_RETAILERS_OUT_RECORD(99, "电商零售出库单", "OROR", 1, 9),

    PREDICT_RETURN_DIRECT_IN_RECORD(47, "退货预入库单", "PRDIR", 2, 9),

    SHOP_OUT_CONSUME_RECORD(48, "门店报废出库单", "SOCR", 1, 9),

    WAREHOUSE_OUT_USE_RECORD(49, "仓库领用出库单", "WOUR", 1, 9),

    INVENTORY_PROFIT_ADJUST_IN_RECORD(52, "仓库损益调整入库单", "IPAIR", 2, 9),

    INVENTORY_PROFIT_ADJUST_OUT_RECORD(53, "仓库损益调整出库单", "IPAOR", 1, 9),

    AB_REPLENISH_OUT_WAREHOUSE_RECORD(54, "加盟商补货大仓出库单", "AROWR", 1, 9),

    AB_REPLENISH_IN_ZWAREHOUSE_RECORD(55, "加盟商补货Z入库单", "ARIZW", 2, 9),

    AB_REPLENISH_OUT_ZWAREHOUSE_RECORD(56, "加盟商店补货Z出库单", "AROZW", 1, 9),

    AB_RETURN_IN_WAREHOUSE_RECORD(57, "加盟商退货入库单", "AREIW", 2, 9),

    AB_RETURN_IN_ZWAREHOUSE_RECORD(58, "加盟商退货入Z库单", "AREZI", 2, 9),

    /**
     * 销售中心退货单
     */
    RETURN_OUT_RECORD(91, "销售中心退货单", "RETU", 1, 9),

    AB_RETURN_OUT_ZWAREHOUSE_RECORD(59, "加盟商退货出Z库单", "AREZO", 1, 9),

    /**
     * 销售中心预约do单
     */
    RESERVATION_DO_RECORD(92, "销售中心预约do单", "REDO", 1, 9),

    /**
     * 包装需求完成单组装出库
     */
    PACKAGE_FINISH_PACK_OUT_RECORD(61, "包装需求完成单组装出库", "PFO", 1, 9),

    /**
     * 包装需求完成单组装入库
     */
    PACKAGE_FINISH_PACK_IN_RECORD(62, "包装需求完成单组装入库", "PFI", 2, 9),

    /**
     * 包装需求完成单反拆出库
     */
    PACKAGE_FINISH_UNPACK_OUT_RECORD(63, "包装需求完成单反拆出库", "PFUO", 1, 9),

    /**
     * 包装需求完成单反拆入库
     */
    PACKAGE_FINISH_UNPACK_IN_RECORD(64, "包装需求完成单反拆入库", "DRCOWR", 2, 9),

    /**
     * 直营门店冷链退货出库单
     */
    DS_RETURN_COLD_OUT_WAREHOUSE_RECORD(65, "直营门店冷链退货出库单", "DRCOWR", 1, 9),

    /**
     * 直营门店冷链退货入库单
     */
    DS_RETURN_COLD_IN_WAREHOUSE_RECORD(66, "直营门店冷链退货入库单", "PFUI", 2, 9),

    /**
     * 加盟门店冷链退货出库单
     */
    LS_RETURN_COLD_OUT_WAREHOUSE_RECORD(67, "加盟门店冷链退货出库单", "DRCOWR", 1, 9),

    /**
     * 加盟门店冷链退货入库单
     */
    LS_RETURN_COLD_IN_WAREHOUSE_RECORD(68, "加盟门店冷链退货入库单", "PFUI", 2, 9),

    DISPARITY_SHOP_IN_RECOED11(70, "差异管理-增加门店库存[补货.门店责任]", "D11SI", 2, 9),

    DISPARITY_TRANSFER_WAREHOUSE_IN_RECOED12(76, "差异管理-增加退货中转仓库存[补货.仓库责任]", "D12TI", 2, 9),

    DISPARITY_TRANSFER_WAREHOUSE_IN_RECOED13(77, "差异管理-增加退货中转仓库存[补货.物流责任]", "D13TI", 2, 9),

    DISPARITY_TRANSFER_WAREHOUSE_OUT_RECOED13(78, "差异管理-减少退货中转仓库存[补货.物流责任]", "D13TO", 1, 9),

    DISPARITY_SHOP_IN_RECOED21(73, "差异管理-增加门店库存[退货.门店责任]", "D21SI", 2, 9),

    DISPARITY_WAREHOUSE_IN_RECOED22(80, "差异管理-增加收货仓仓库存[退货.仓库责任]", "D22WI", 2, 9),

    DISPARITY_WAREHOUSE_IN_RECOED23(81, "差异管理-增加收货仓仓库存[退货.物流责任]", "D23WI", 2, 9),

    DISPARITY_WAREHOUSE_OUT_RECOED23(83, "差异管理-减少收货仓库存[退货.物流责任]", "D23WO", 1, 9),


    REVERSE_IN_RECORD(90, "出入库冲销单入库", "RIR", 1, 9),
    REVERSE_OUT_RECORD(91, "出入库冲销单出库", "ROR", 2, 9);

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

    /**
     * 区分出库和入库流水页面显示 9暂时不确定 1出库 2入库
     */
    private Integer businessType;

    /**
     * 区分页面分类
     */
    private Integer pageType;

    WarehouseRecordTypeEnum(Integer type, String desc, String code, Integer businessType, Integer pageType) {
        this.type = type;
        this.desc = desc;
        this.code = code;
        this.businessType = businessType;
        this.pageType = pageType;
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

    public Integer getBusinessType() {
        return businessType;
    }

    public Integer getPageType() {
        return pageType;
    }

    /**
     * 根据类型查询描述
     *
     * @param type
     * @return
     */
    public static WarehouseRecordTypeEnum getEnumByType(Integer type) {
        for (WarehouseRecordTypeEnum ele : values()) {
            if (ele.getType().equals(type)) {
                return ele;
            }
        }
        return null;
    }

    public static String getDescByType(Integer type) {
        for (WarehouseRecordTypeEnum ele : values()) {
            if (ele.getType().equals(type)) {
                return ele.getDesc();
            }
        }
        return null;
    }

    /**
     * 获取类型集合
     *
     * @return
     */
    public static Map<Integer, String> getWarehouseRecordTypeList() {
        Map<Integer, String> map = new HashMap<>();
        for (WarehouseRecordTypeEnum ele : values()) {
            map.put(ele.getType(), ele.getDesc());
        }
        return map;
    }

    /**
     * 获取仓库类型
     *
     * @param type out出库  in入库  all 出入库
     *             1出库    2入库
     * @return
     */
    public static Map<Integer, Object> getRealWareHouseType(String type) {
        Map<Integer, Object> realWareHouseType = new HashMap<>();
        for (WarehouseRecordTypeEnum typeVO : WarehouseRecordTypeEnum.values()) {
            if ("in".equals(type) && 2 == typeVO.getBusinessType()) {
                realWareHouseType.put(typeVO.getType(), typeVO.getDesc());
            } else if ("out".equals(type) && 1 == typeVO.getBusinessType()) {
                realWareHouseType.put(typeVO.getType(), typeVO.getDesc());
            } else if ("all".equals(type)) {
                realWareHouseType.put(typeVO.getType(), typeVO.getDesc());
            }
        }
        return realWareHouseType;
    }

    /**
     * 获取入库单页面类型集合
     *
     * @return
     */
    public static Map<Integer, String> getInWarehouseRecordPageTypeList() {
        Map<Integer, String> map = new HashMap<>();
        for (WarehouseRecordTypeEnum ele : values()) {
            if (2 == ele.getBusinessType()) {
                map.put(ele.getType(), ele.getDesc());
            }
        }
        return map;
    }

    /**
     * 获取出库单页面类型集合
     *
     * @return
     */
    public static Map<Integer, String> getOutWarehouseRecordPageTypeList() {
        Map<Integer, String> map = new HashMap<>();
        for (WarehouseRecordTypeEnum ele : values()) {
            if (1 == ele.getBusinessType()) {
                map.put(ele.getType(), ele.getDesc());
            }
        }
        return map;
    }

    /**
     * 非Z补货出库单列表
     *
     * @return
     */
    public static Map<Integer, String> getReplenishOutWarehouseRecordList() {
        Map<Integer, String> map = new HashMap<>();
        for (WarehouseRecordTypeEnum ele : values()) {
            if (ele.getDesc().indexOf("补货") == -1) {
                continue;
            }
            if (ele.getDesc().indexOf("出库单") == -1) {
                continue;
            }
            if (ele.getDesc().indexOf("Z") != -1) {
                continue;
            }
            map.put(ele.getType(), ele.getDesc());
        }
        return map;
    }

}