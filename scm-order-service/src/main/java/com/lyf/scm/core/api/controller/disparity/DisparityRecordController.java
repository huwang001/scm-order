package com.lyf.scm.core.api.controller.disparity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.util.validate.ParamValidator;
import com.lyf.scm.core.api.dto.disparity.BatchRefusedBackDTO;
import com.lyf.scm.core.api.dto.disparity.DisparityDetailDTO;
import com.lyf.scm.core.api.dto.disparity.DisparityRecordDetailDTO;
import com.lyf.scm.core.api.dto.disparity.QueryDisparityDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.service.disparity.DisparityRecordService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: DisparityRecordController  
 * @Description: 差异订单管理接口  
 * @author: Lin.Xu  
 * @date: 2020-7-10  
 * @version: v1.0
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/disparity/record")
@Api(tags={"差异单管理接口"})
public class DisparityRecordController {

    private ParamValidator validator = ParamValidator.INSTANCE;

    @Autowired
    private DisparityRecordService disparityRecordService;


    @ApiOperation(value = "根据条件查询差异单信息,分页", nickname = "query_by_condition", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = RealWarehouse.class)
    @PostMapping(value = "/queryByCondition")
    public Response<PageInfo<DisparityRecordDetailDTO>> queryByCondition(@RequestBody QueryDisparityDTO paramDTO) {
        if(! validator.validParam(paramDTO)) {
            return Response.builderFail(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        try {
            PageInfo<DisparityRecordDetailDTO> pageInfo = disparityRecordService.queryByCondition(paramDTO);
            return Response.builderSuccess(pageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003,  ResCode.ORDER_ERROR_1003_DESC);
        }
    }


    @ApiOperation(value = "差异定责", nickname = "disparityDuty", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = RealWarehouse.class)
    @PostMapping(value = "/disparityDuty")
    public Response<String> disparityDuty(@RequestBody List<DisparityDetailDTO> details) {
        if (details == null || details.size() == 0) {
            return Response.builderFail(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC+":"+"判责明细不能为空");
        }
        try {
            disparityRecordService.disparityDuty(details);
            return Response.builderSuccess("定责成功");
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "确认过账", nickname = "confirmPosting", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = RealWarehouse.class)
    @PostMapping(value = "/confirmPosting" )
    public Response confirmPosting(@RequestBody List<Long> detailsIds, @RequestParam Long modifier) {
        if (detailsIds == null || detailsIds.size() == 0L) {
            return Response.builderFail(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC+":"+"过账明细不能为空");
        }
        try {
            disparityRecordService.confirmPosting(detailsIds,modifier);
            return Response.builderSuccess("过账成功");
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }
    
    @ApiOperation(value = "批量整单拒收", nickname = "confirmPosting", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
    	@ApiImplicitParam(value = "modifier", name = "修改人", required = true, dataType = "Long")
    })
    @PostMapping(value = "/overallRejection")
    public Response<List<BatchRefusedBackDTO>> overallRejection(@RequestBody List<String> putInNos, @RequestParam("modifier") Long modifier) {
    	List<BatchRefusedBackDTO> callBackRs = disparityRecordService.overallRejection(putInNos, modifier);
    	return Response.builderSuccess(callBackRs);
    }

    @ApiOperation(value = "门店补货PO推送交易", nickname = "pushTransactionStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @PostMapping("/pushTransactionStatus")
    public Response pushTransactionStatus() {
        try {
            disparityRecordService.pushTransactionStatus();
            return Response.builderSuccess("推送成功");
        } catch (RomeException e) {
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

}
