package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * WarehouseQueryDTO，
 * 根据工厂编号和类型查询对应仓库列表
 */
@Data
@EqualsAndHashCode
public class WarehouseQueryDTO {
    /**
     * 工厂编号
     */
    private String factoryCode;


    /**
     * 类型
     */
    private Integer type;

}
