package com.lyf.scm.common.enums;

import org.apache.commons.lang3.StringUtils;

import com.lyf.scm.common.constants.ResCode;
import com.rome.arch.core.exception.RomeException;

/**
 * @Description: 校验出入库单虚仓编码是否可设置Enum <br>
 *
 * @Author wwh 2020/7/17
 */
public enum CheckVirWarehouseCodeEnum {

    DS_REPLENISH_OUT_WAREHOUSE_RECORD(10, "直营门店补货出库单"),
    
    LS_REPLENISH_OUT_WAREHOUSE_RECORD(12, "加盟门店补货大仓出库单"),

    WH_ALLOCATION_OUT_WAREHOUSE_RECORD(29, "仓库调拨出库单");

    /**
     * 类型
     */
    private Integer type;

    /**
     * 描述
     */
    private String desc;

    CheckVirWarehouseCodeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static CheckVirWarehouseCodeEnum queryCheckVirWarehouseCode(Integer type) {
        if(type == null) {
            return null;
        }
        for(CheckVirWarehouseCodeEnum checkVirWarehouseCodeEnum: values()) {
            if(checkVirWarehouseCodeEnum.getType().equals(type)) {
                return checkVirWarehouseCodeEnum;
            }
        }
        return null;
    }
    
    public static void checkVirWarehouseCode(String virWarehouseCode, Integer recordType) {
		if(StringUtils.isNotBlank(virWarehouseCode)) {
			CheckVirWarehouseCodeEnum vwEnum = queryCheckVirWarehouseCode(recordType);
			if(vwEnum == null) {
				throw new RomeException(ResCode.ORDER_ERROR_6044, ResCode.ORDER_ERROR_6044_DESC);
			}
		}
	}
	
}