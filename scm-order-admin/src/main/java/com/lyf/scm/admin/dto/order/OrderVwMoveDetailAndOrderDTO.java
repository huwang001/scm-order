package com.lyf.scm.admin.dto.order;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.remote.dto.VirtualWarehouse;
import lombok.Data;

import java.util.List;

@Data
public class OrderVwMoveDetailAndOrderDTO {


    /**
     * 实仓Code
     */
    private String realWarehouseCode;

    private String realWarehouseName;

    /**
     * 调入虚仓编码
     */
    private String joinVmWarehouseCode;

    private String joinVmWarehouseName;



    /**
     * 调出虚仓编码
     */
    private  List<VirtualWarehouse>  exitVmWarehouseCodeList;


    PageInfo<OrderVwMoveDetailDTO> pagelist;

    List<OrderVwMoveDetailDTO> list;

    /**
     * 工厂编号
     */
    private String factoryCode;

    /**
     * 是否需要包装 0:不需要 1:需要
     */
    private Integer needPackage;
}
