package com.lyf.scm.admin.remote;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.disparity.DisparityRecordDetailDTO;
import com.lyf.scm.admin.dto.disparity.DisparityRecordDetail;
import com.lyf.scm.admin.dto.disparity.DisparityRecordParamDTO;
import com.lyf.scm.admin.remote.dto.BatchRefusedBackDTO;
import com.rome.arch.core.clientobject.Response;

@FeignClient(value = "scm-order-service", url = "http://127.0.0.1:8082")
public interface DisparityRecordRemoteService {

    @RequestMapping(value = "/order/v1/disparity/record/queryByCondition", method = RequestMethod.POST)
    Response<PageInfo<DisparityRecordDetailDTO>> queryByCondition(@RequestBody DisparityRecordParamDTO paramDTO);


    @RequestMapping(value = "/order/v1/disparity/record/disparityDuty",method = RequestMethod.POST)
    Response disparityDuty(@RequestBody List<DisparityRecordDetail> details) ;

    @RequestMapping(value = "/order/v1/disparity/record/confirmPosting",method = RequestMethod.POST)
    Response handlerDisparity(@RequestBody List<Long> ids, @RequestParam(value = "modifier") Long modifier) ;

    
    /**
     * @Method: rejectConfirmDisparityBySapCode  
     * @Description: 批量拒收订单
     * @param putInNos
     * @param modifier
     * @author: Lin.Xu 
     * @date: 2020-7-13 16:19:57 
     * @return: Response<List<BatchRefusedBackDTO>>
     * @throws
     */
    @RequestMapping(value = "/order/v1/disparity/record/rejectConfirmDisparityBySapCode",method = RequestMethod.POST)
    Response<List<BatchRefusedBackDTO>> rejectConfirmDisparityBySapCode(@RequestBody List<String> putInNos, @RequestParam("modifier") Long modifier);


}
