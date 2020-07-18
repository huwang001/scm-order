package com.lyf.scm.admin.controller.reverse;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.reverse.*;
import com.lyf.scm.admin.remote.reverse.facade.ReverseFacade;
import com.lyf.scm.common.constants.ResCode;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import com.rome.user.common.utils.UserContext;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RomeController
@RequestMapping("/order/v1/reverse")
@Api(tags={"冲销单接口管理"})
public class ReverseController {
	
	@Resource
	private ReverseFacade reverseFacade;

    @ApiOperation(value = "分页查询冲销单列表", nickname = "queryReversePage", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryReversePage", method = RequestMethod.POST)
    public Response<PageInfo<ReverseReponseDTO>> queryReversePage(@ApiParam(name = "queryReverseDTO", value = "查询条件") @RequestBody @Validated QueryReverseDTO queryReverseDTO) {
        try {
            return Response.builderSuccess(reverseFacade.queryReversePage(queryReverseDTO));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查看冲销单详情", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryReverseDetail", method = RequestMethod.GET)
    public Response<ReverseDTO> queryReverseDetail(@ApiParam(name = "recordCode", value = "冲销单号") @RequestParam(name = "recordCode", required = false) String recordCode) {
        try {
            return Response.builderSuccess(reverseFacade.queryReverseDetail(recordCode));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "取消冲销单", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/cancelReverse", method = RequestMethod.POST)
    public Response<List<CancelReverseDTO>> cancelReverse(@ApiParam(name = "recordCodes", value = "冲销单号") @RequestBody List<String> recordCodes) {
        try {
            return Response.builderSuccess(reverseFacade.cancelReverse(recordCodes));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据出库单据编码查询出库单(包含明细)", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recordCode", value = "单据编码", required = true)
    })
    @GetMapping(value = "/queryWarehouseRecordByRecordCode/{recordCode}")
    public Response<ReverseDTO> queryWarehouseRecordByRecordCode(@PathVariable String recordCode){
        try {
            ReverseDTO reverseDTO = reverseFacade.queryWarehouseRecordByRecordCode(recordCode);
            return Response.builderSuccess(reverseDTO);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据入库单据编码查询收货单(包含明细)", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recordCode", value = "单据编码", required = true)
    })
    @GetMapping(value = "/queryReceiverRecordByRecordCode/{recordCode}")
    public Response<List<ReceiverRecordDTO>> queryReceiverRecordByRecordCode(@PathVariable String recordCode){
        try {
            List<ReceiverRecordDTO> receiverRecordDTOList = reverseFacade.queryReceiverRecordByRecordCode(recordCode);
            return Response.builderSuccess(receiverRecordDTOList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "创建/编辑冲销单", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/createReverse", method = RequestMethod.POST)
    public Response<String> createReverse(@RequestBody  ReverseInfoDTO reverseInfoDTO) {
        try {
            reverseInfoDTO.setUserId(UserContext.getUserId());
            return Response.builderSuccess(reverseFacade.createReverse(reverseInfoDTO));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }




    @ApiOperation(value = "确认冲销单", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/confirmReverse", method = RequestMethod.POST)
    public Response<List<ReverseConfirmedInfoDTO>> confirmReverse(@ApiParam(name = "recordCodes", value = "冲销单号") @RequestBody List<String> recordCodes) {
        try {

            ReverseConfirmFromPageDTO  reverseConfirmFromPageDTO = new ReverseConfirmFromPageDTO();
            reverseConfirmFromPageDTO.setUserId(UserContext.getUserId());
            reverseConfirmFromPageDTO.setRecordCodeList(recordCodes);
            return Response.builderSuccess(reverseFacade.confirmReverse(reverseConfirmFromPageDTO));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

}