package com.lyf.scm.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum RealWarehouseTypeEnum {

    /**
     * 门店仓
     */
    RW_TYPE_1(1, "门店仓", true),

    /**
     * 包装仓库
     */
    RW_TYPE_2(2, "包装仓库", true),

    /**
     * 电商备货仓
     */
    RW_TYPE_3(3, "电商备货仓", true),

    /**
     * 海信物资仓
     */
    RW_TYPE_4(4, "海信物资仓", true),

    /**
     * 行政仓
     */
    RW_TYPE_5(5, "行政仓", false),

    /**
     * 冷库仓
     */
    RW_TYPE_6(6, "冷库仓", true),

    /**
     * 散零仓
     */
    RW_TYPE_7(7, "散零仓", true),

    /**
     * 团购仓
     */
    RW_TYPE_8(8, "团购仓", true),

    /**
     * 团购备货仓
     */
    RW_TYPE_9(9, "团购备货仓", true),

    /**
     * 超市备货仓
     */
    RW_TYPE_10(10, "超市备货仓", true),

    /**
     * 退厂仓
     */
    RW_TYPE_11(11, "退厂仓", false),

    /**
     * 线下退货仓
     */
    RW_TYPE_12(12, "线下退货仓", true),

    /**
     * 退货中转仓
     */
    RW_TYPE_13(13, "退货中转仓", true),

    /**
     * 销售中心发货仓
     */
    RW_TYPE_14(14, "销售中心发货仓", true),

    /**
     * 虚拟物品仓
     */
    RW_TYPE_15(15, "虚拟物品仓", true),

    /**
     * 总仓
     */
    RW_TYPE_16(16, "总仓", false),

    /**
     * 电商发货仓
     */
    RW_TYPE_17(17, "电商发货仓", false),

    /**
     * 电商自营仓
     */
    RW_TYPE_18(18, "电商自营仓", false),

    /**
     * 电商退货仓
     */
    RW_TYPE_19(19, "电商退货仓", true),

    /**
     * 其他
     */
    RW_TYPE_20(20, "其他", false),

    /**
     * 商家仓
     */
    RW_TYPE_21(21, "商家仓", true),

    /**
     * 商家仓
     */
    RW_TYPE_23(23, "门店冷链退货仓", true);


    /**
     * 类型
     */
    private Integer type;

    /**
     * 描述
     */
    private String desc;

    /**
     * 是否唯一(工厂下只有一个同类型仓库)
     */
    private boolean unique;

    RealWarehouseTypeEnum(Integer type, String desc, boolean unique) {
        this.type = type;
        this.desc = desc;
        this.unique = unique;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public boolean getUnique() {
        return unique;
    }

    public static Map<Integer, String> getRealWarehouseTypeList() {
        Map<Integer, String> map = new HashMap<>();
        for (RealWarehouseTypeEnum vo : values()) {
            map.put(vo.getType(), vo.getDesc());
        }
        return map;
    }

    public static RealWarehouseTypeEnum getTypeVOByType(Integer type) {
        if (null == type) {
            return null;
        }
        for (RealWarehouseTypeEnum vo : values()) {
            if (vo.type.equals(type)) {
                return vo;
            }
        }
        return null;
    }

}