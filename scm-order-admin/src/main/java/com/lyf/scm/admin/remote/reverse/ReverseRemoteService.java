package com.lyf.scm.admin.remote.reverse;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.reverse.*;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
  * @Description 冲销单
  * @author huwang
  * @Date 2020/7/17 16:10
  **/
@FeignClient(value = "scm-order-service")
public interface ReverseRemoteService {

    @RequestMapping(value = "/order/v1/reverse/queryReversePage", method = RequestMethod.POST)
    Response<PageInfo<ReverseReponseDTO>> queryReversePage(@RequestBody QueryReverseDTO queryReverseDTO);

    @RequestMapping(value = "/order/v1/reverse/queryReverseDetail", method = RequestMethod.GET)
    Response<ReverseDTO> queryReverseDetail(@RequestParam("recordCode") String recordCode);

    @RequestMapping(value = "/order/v1/reverse/cancelReverse", method = RequestMethod.POST)
    Response<List<CancelReverseDTO>> cancelReverse(@RequestBody List<String> recordCodes);

    /**
     * 根据出库单据编码查询出库单(包含明细)
     * @param recordCode
     * @return
     */
    @GetMapping(value = "/order/v1/reverse/queryWarehouseRecordByRecordCode/{recordCode}")
    Response<ReverseDTO> queryWarehouseRecordByRecordCode(@PathVariable String recordCode);

    /**
     * 根据入库单据编码查询收货单(包含明细)
     * @param recordCode
     * @return
     */
    @GetMapping(value = "/order/v1/reverse/queryReceiverRecordByRecordCode/{recordCode}")
    Response<List<ReceiverRecordDTO>> queryReceiverRecordByRecordCode(@PathVariable String recordCode);

    /**
     * @Description 创建/编辑冲销单
     * @author huangyl
     * @Date 2020/7/18
     * @param reverseInfoDTO
     * @return
     */
    @RequestMapping(value = "/order/v1/reverse/createReverse", method = RequestMethod.POST)
    Response<String> createReverse(@RequestBody ReverseInfoDTO reverseInfoDTO);


    /**
     * @Description 确认冲销单
     * @author huangyl
     * @Date 2020/7/18
     * @param reverseConfirmFromPageDTO
     * @return
     */
    @RequestMapping(value = "/order/v1/reverse/confirmReverse", method = RequestMethod.POST)
    Response<List<ReverseConfirmedInfoDTO>> confirmReverse(@RequestBody ReverseConfirmFromPageDTO reverseConfirmFromPageDTO);
}
