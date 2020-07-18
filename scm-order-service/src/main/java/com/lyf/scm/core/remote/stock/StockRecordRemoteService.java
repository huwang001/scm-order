package com.lyf.scm.core.remote.stock;

import java.util.List;

import com.lyf.scm.core.remote.stock.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lyf.scm.core.api.dto.notify.DispatchNoticeDTO;
import com.lyf.scm.core.config.ScmCallLog;
import com.rome.arch.core.clientobject.Response;

/**
 * @Description: 库存中心出入库单远程调用接口
 * <p>
 * @Author: wwh  2020/6/23
 */
@FeignClient(name = "stock-inner-app")
public interface StockRecordRemoteService {

    @ScmCallLog(systemName = "stock-inner-app", recordCode = "#inRecordDto!=null?#inRecordDto.recordCode:#arg0.recordCode")
    @RequestMapping(value = "/stock/v1/warehouseRecord/createInRecord", method = RequestMethod.POST)
    Response<String> createInRecord(@RequestBody InWarehouseRecordDTO inRecordDto);

    @ScmCallLog(systemName = "stock-inner-app", recordCode = "#outRecordDto!=null?#outRecordDto.recordCode:#arg0.recordCode")
    @RequestMapping(value = "/stock/v1/warehouseRecord/createOutRecord", method = RequestMethod.POST)
    Response<String> createOutRecord(@RequestBody OutWarehouseRecordDTO outRecordDto);

    @RequestMapping(value = "/stock/v1/warehouseRecord/batchCancel", method = RequestMethod.POST)
    Response<List<CancelResultDTO>> cancelRecord(@RequestBody List<CancelRecordDTO> cancelRecordList);

    @ScmCallLog(systemName = "stock-inner-app", recordCode = "#dispatchNoticeDTOList!=null?#dispatchNoticeDTOList[0].recordCode:#arg0[0].recordCode")
    @RequestMapping(value = "/stock/v1/warehouseRecord/batchDispatchingNotify", method = RequestMethod.POST)
    Response<List<CancelResultDTO>> batchDispatchingNotify(@RequestBody List<DispatchNoticeDTO> dispatchNoticeDTOList);

    @RequestMapping(value = "/stock/v1/warehouseRecord/updateWarehouseSaleTobTmsOrder", method = RequestMethod.POST)
    Response<List<CancelResultDTO>> updateWarehouseSaleTobTmsOrder(@RequestBody UpdateTmsCodeDTO updateTmsCodeDTO);

    @RequestMapping(value = "/stock/v1/disparity/handleDisparity", method = RequestMethod.POST)
    Response<String> handleDisparity(@RequestBody List<DisparityParamDTO> disparityParamDTOList);

}