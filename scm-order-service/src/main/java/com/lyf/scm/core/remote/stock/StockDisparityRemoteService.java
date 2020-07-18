package com.lyf.scm.core.remote.stock;

import com.lyf.scm.core.remote.stock.dto.BatchCreateRecordDTO;
import com.lyf.scm.core.remote.stock.dto.BatchResultDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Description 差异订单对接库存中心
 * @Author Lin.Xu
 * @Date 16:27 2020/7/16
 * @return
 **/
@FeignClient(name = "stock-inner-app")
public interface StockDisparityRemoteService {

    /**
     * @Method: rejectShopReceipt
     * @Description: 门店确认收货批量拒收
     * @param putInNos
     * @author: Lin.Xu
     * @date: 2020-7-13 9:16:13
     * @return: Response<BatchResultDTO>
     * @throws
     */
    @PostMapping("/stock/v1/shopReplenish/rejectShopReceipt")
    public Response<List<BatchResultDTO>> rejectShopReceipt(@RequestBody List<String> putInNos);

    /**
     * @Description 差异处理:批量创建出入库单
     * @Author Lin.Xu
     * @Date 17:43 2020/7/16
     * @Param [batchCreateRecordDTO]
     * @return com.rome.arch.core.clientobject.Response
     **/
    @PostMapping("/stock/v1/disparity/createOutAndInRecordBatch")
    public Response createOutRecord(@RequestBody BatchCreateRecordDTO batchCreateRecordDTO);

}
