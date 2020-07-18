package com.lyf.scm.core.remote.stock;

import java.util.List;
import com.lyf.scm.core.api.dto.reverse.ReceiverRecordDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import com.lyf.scm.core.api.dto.stockFront.BatchStockChangeFlowDTO;
import com.lyf.scm.core.remote.stock.dto.QueryRealStockDTO;
import com.lyf.scm.core.remote.stock.dto.QueryVirtualWarehouseStockDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouseStockDTO;
import com.lyf.scm.core.remote.stock.dto.SkuInfoForVw;
import com.lyf.scm.core.remote.stock.dto.SkuStockDTO;
import com.lyf.scm.core.remote.stock.dto.SkuStockForVw;
import com.lyf.scm.core.remote.stock.dto.VirtualWarehouseStock;
import com.rome.arch.core.clientobject.Response;

/**
 * @Description: 通用库存查询远程调用接口
 * <p>
 * @Author: wwh  2020/6/23
 */
@FeignClient(name = "stock-inner-app")
public interface StockQueryRemoteService {

    @RequestMapping(value = "/stock/v1/stockQuery/queryVwStockByVwCode", method = RequestMethod.POST)
    Response<List<VirtualWarehouseStock>> queryVwStockByVwCode(@RequestBody QueryVirtualWarehouseStockDTO queryVirtualWarehouseStockDTO);

    @RequestMapping(value = "/stock/v1/stockQuery/queryRealStockBySkuInfo", method = RequestMethod.POST)
    Response<List<SkuStockDTO>> queryRealStockBySkuInfo(@RequestBody QueryRealStockDTO realStock);

    @RequestMapping(value = "/stock/v1/stockQuery/queryAllWarehouseStock", method = RequestMethod.GET)
    Response<List<RealWarehouseStockDTO>> queryRealStockByFactoryAndRealWarehouseCode(@RequestParam("realWarehouseOutCode") String realWarehouseOutCode, @RequestParam("factoryCode") String factoryCode);
    
    @RequestMapping(value = "/stock/v1/stockQuery/queryVwStockById", method = RequestMethod.POST)
    Response<List<SkuStockForVw>> queryVmListByVmGroupCodes(@RequestBody List<SkuInfoForVw> skuInfoList);
    
    @RequestMapping(value = "/stock/v1/shop_retail/queryBatchInfoByRecordCode", method = RequestMethod.GET)
    Response<List<BatchStockChangeFlowDTO>> queryBatchInfoByRecordCode(@RequestParam("recordCode") String recordCode);

    /**
     * 根据入库单据编码或收货单编码查询收货单(包含明细)
     * @param recordCode        入库单编码
     * @param wmsRecordCode     收货单编码
     * @return
     */
    @PostMapping(value = "/stock/v1/stockQuery/queryReceiptByRecordCode")
    Response<List<ReceiverRecordDTO>> getReceiverRecordListByRecordCode(@RequestParam(value = "recordCode") String recordCode, @RequestParam(value = "wmsRecordCode", required = false) String wmsRecordCode);

}