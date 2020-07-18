package com.lyf.scm.admin.remote.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.SaleTobWarehouseRecordCondition;
import com.lyf.scm.admin.remote.stockFront.dto.SaleTobWarehouseRecordDTO;
import com.rome.arch.core.clientobject.Response;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value="scm-order-service")
public interface SaleTobWarehouseRecordRemoteService {

	//查询发货单
	@RequestMapping(value = "/order/v1/warehouse_sale_tob/list", method = RequestMethod.POST)
	Response<PageInfo<SaleTobWarehouseRecordDTO>> queryByCondition(@ApiParam(name = "condition", value = "查询条件") @RequestBody SaleTobWarehouseRecordCondition condition) ;

	//获取已有所需前置单据门店信息
	@RequestMapping(value = "/order/v1/warehouse_sale_tob/getShopInfo", method = RequestMethod.GET)
    Response<List<Map<String,String>>> getShopInfo();

	//根据发货单id查看详情
	@RequestMapping(value = "/order/v1/warehouse_sale_tob/getWarehouseSaleTobDetail", method = RequestMethod.POST)
    Response<SaleTobWarehouseRecordDTO> getWarehouseSaleTobDetail(@RequestParam("warehouseRecordId") Long warehouseRecordId);
}


