package com.lyf.scm.admin.dto.order;

import com.lyf.scm.admin.remote.dto.RealWarehouse;
import lombok.Data;

import java.util.List;

@Data
public class OrderDetailAndAllotDTO {


    /**
     * 调出实仓Code
     */
    private String realWarehouseCode;

    private String realWarehouseName;

    /**
     * 调入调入仓库列表
     */
    private List<RealWarehouse> warehouseList;


    /**
     * 工厂编号
     */
    private String factoryCode;

    /**
     * 是否需要包装 0:不需要 1:需要
     */
    private Integer needPackage;


    List<OrderDetailRespDTO> list;

}
