package com.lyf.scm.core.remote.stock;

import com.lyf.scm.core.remote.stock.dto.*;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * com.lyf.scm.core.remote.stock
 *
 * @author zhangxu
 * @date 2020/4/15
 */
@FeignClient(value = "stock-inner-app")
public interface StockRemoteService {

    /**
     * 创建预约单接口(第一次锁定库存)
     * @param reservationInDTO 预约单及明细
     * @return
     */
    @RequestMapping(value = "/stock/v1/groupbuy/createReservation", method = RequestMethod.POST)
    Response<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationInDTO);

    /**
     * 库存二次轮询锁定
     * @param recoreCode 预约单号
     * @return
     */
    @RequestMapping(value = "/stock/v1/groupbuy/lockReservationByRecordCode/{outRecordCode}", method = RequestMethod.GET)
    Response<ReservationDTO> lockReservationByRecordCode(@PathVariable("recoreCode") String recoreCode);

    /**
     * 根据预约单号取消预约单，释放锁定库存
     * @param outRecordCode
     * @return
     */
    @RequestMapping(value = "/stock/v1/groupbuy/cancelReservation/{outRecordCode}", method = RequestMethod.POST)
	Response cancelReservation(@PathVariable("outRecordCode") String outRecordCode);
    
    @RequestMapping(value = "/stock/v1/groupbuy/unLockStock/{outRecordCode}", method = RequestMethod.POST)
	Response unlockOrderStock(@PathVariable("outRecordCode") String orderCode);

    @RequestMapping(value = "/stock/v1/groupbuy/saveVirtualWarehouseMoveRecord", method = RequestMethod.POST)
    Response saveVirtualWarehouseMoveRecord(@RequestBody VwMoveRecordDTO dto);

    @PostMapping(value = "/stock/v1/groupBy/queryRealWarehouseByFactoryCodeAndRealWarehouseType")
	Response<List<RealWarehouse>> queryRealWarehouseByFactoryCodeAndRealWarehouseType(@RequestParam("factoryCode")String factoryCode, @RequestParam("type") Integer type);

    @PostMapping(value = "/stock/v1/groupBy/queryByOutCodeAndFactoryCodeList")
    Response<List<RealWarehouse>> queryByOutCodeAndFactoryCodeList(@RequestBody List<QueryRealWarehouseDTO> queryRealWarehouseDTOS);

    @PostMapping(value = "/stock/v1/groupBy/queryVirtualWarehouseByCodes")
    Response<List<VirtualWarehouse>> queryVirtualWarehouseByCodes(@RequestParam("virtualWarehouseCodes") List<String> virtualWarehouseCodes);
}
