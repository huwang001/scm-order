package com.lyf.scm.core.remote.stock;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.api.dto.stockFront.RealWarehouseParamDTO;
import com.lyf.scm.core.remote.stock.dto.PoNoDTO;
import com.lyf.scm.core.remote.stock.dto.QueryRealWarehouseStockDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.dto.RealWarehouseStockDTO;
import com.lyf.scm.core.remote.stock.dto.UpdateTmsCodeDTO;
import com.lyf.scm.core.remote.stock.dto.UpdateTmsOrderItem;
import com.rome.arch.core.clientobject.Response;

/**
 * @Description: 仓库调拨远程调用接口
 * <p>
 * @Author: wwh  2020/6/23
 */
@FeignClient(name = "stock-inner-app")
public interface StockWhAllocationRemoteService {

    @RequestMapping(value = "/stock/wh_allocation/listStockBySkuWhIdForWhAllot", method = RequestMethod.POST)
    Response<List<RealWarehouseStockDTO>> listStockBySkuWhIdForWhAllot(@RequestBody QueryRealWarehouseStockDTO queryRealWarehouseStockDTO);

    @RequestMapping(value = "/stock/wh_allocation/queryStockByWhIdForWhAllot", method = RequestMethod.POST)
    Response<PageInfo<RealWarehouseStockDTO>> queryStockByWhIdForWhAllot(@RequestBody RealWarehouseStockDTO realWarehouseStockDTO);
    
    @RequestMapping(value = "/stock/wh_allocation/findByWhConditionForAdmin", method = RequestMethod.POST)
    Response<List<RealWarehouse>> queryForAdmin(@RequestBody RealWarehouseParamDTO paramDTO);

    @RequestMapping(value = "/stock/wh_allocation/updateWarehouseSaleTobTmsOrder", method = RequestMethod.POST)
    Response<List<UpdateTmsOrderItem>> updateTmsOrder(@RequestBody UpdateTmsCodeDTO updateTmsCodeDTO);

    @RequestMapping(value = "/stock/wh_allocation/updateLineNoAndSapPoNo", method = RequestMethod.POST)
    Response updateLineNoAndSapPoNo(@RequestBody List<PoNoDTO> poNoList);

}