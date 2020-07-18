package com.lyf.scm.common.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public enum OnlineStockTransTypeEnum {

    TRANS_TYPE_1(1, 1, "普通"),
    TRANS_TYPE_2(2, 1, "预售"),
    TRANS_TYPE_3(3, 1, "拼团"),
    TRANS_TYPE_4(4, 1, "拼券"),
    TRANS_TYPE_5(5, 1, "旺店通"),
    TRANS_TYPE_6(6, 1, "POS门店"),
    TRANS_TYPE_8(8, 1, "POS外卖第三方"),
    TRANS_TYPE_7(7, 0, "外卖自营"),
    TRANS_TYPE_9(9, 1, "电商超市"),
    TRANS_TYPE_10(10, 1, "2B分销"),
    TRANS_TYPE_11(11, 1, "加盟商"),
    TRANS_TYPE_12(12, 0, "虚拟商品");

    //类型
    private Integer transType;
    //是否需要合单 0-不需要 1-需要
    private Integer needCombine;
    //描述
    private String desc;

    private static final Map<Integer, OnlineStockTransTypeEnum> MAP = new HashMap<>();

    static {
        for (OnlineStockTransTypeEnum item : OnlineStockTransTypeEnum.values()) {
            MAP.put(item.getTransType(), item);
        }
    }

    OnlineStockTransTypeEnum(Integer transType, Integer needCombine, String desc) {
        this.transType = transType;
        this.needCombine = needCombine;
        this.desc = desc;
    }

    public Integer getTransType() {
        return transType;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getNeedCombine() {
        return needCombine;
    }

    public static boolean typeExist(Integer transType) {
        Stream<OnlineStockTransTypeEnum> stream = Stream.of(values());
        return stream.anyMatch(e -> e.getTransType().equals(transType));
    }

    public boolean getNeedCombineFlag() {
        return (1 == needCombine);
    }

    public static OnlineStockTransTypeEnum getByTransType(Integer transType) {
        return MAP.get(transType);
    }
}
