package com.lyf.scm.core.api.controller.reverse;

import javax.annotation.Resource;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.YesOrNoEnum;
import com.lyf.scm.core.api.dto.reverse.*;
import com.lyf.scm.core.api.dto.reverse.ReverseConfirmFromPageDTO;
import com.lyf.scm.core.api.dto.reverse.ReverseConfirmedInfoDTO;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.lyf.scm.core.service.reverse.ReverseService;
import com.rome.arch.util.controller.RomeController;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RomeController
@RequestMapping("/order/v1/reverse")
@Api(tags={"冲销单接口管理"})
public class ReverseController {
	
	@Resource
	private ReverseService reverseService;



	@ApiOperation(value = "创建冲销单", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "success")
	@RequestMapping(value = "/createReverse", method = RequestMethod.POST)
	public Response<String> createReverse(@ApiParam(name = "demandDTO", value = "冲销单参数") @RequestBody ReverseInfoDTO reverseInfoDTO) {
		try {

			return Response.builderSuccess(reverseService.createReverse(reverseInfoDTO));
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

    @ApiOperation(value = "分页查询冲销单列表", nickname = "queryReversePage", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryReversePage", method = RequestMethod.POST)
    public Response<PageInfo<ReverseReponseDTO>> queryReversePage(@ApiParam(name = "queryReverseDTO", value = "查询条件") @RequestBody @Validated QueryReverseDTO queryReverseDTO) {
        try {
            return Response.builderSuccess(reverseService.queryReversePage(queryReverseDTO));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }


    @ApiOperation(value = "确认冲销单单据状态", nickname = "confirmReverse", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/confirmReverse", method = RequestMethod.POST)
    public Response<List<ReverseConfirmedInfoDTO>> confirmReverse(@RequestBody ReverseConfirmFromPageDTO reverseConfirmFromPageDTO) {

        log.info("页面确认需求单单据状态，参数 <<< {}", JSON.toJSONString(reverseConfirmFromPageDTO.toString()));
        // 返回信息
        List<ReverseConfirmedInfoDTO> list = new ArrayList(reverseConfirmFromPageDTO.getRecordCodeList().size());
        for (String recordCode : reverseConfirmFromPageDTO.getRecordCodeList()){
            ReverseConfirmedInfoDTO reverseConfirmedInfoDTO = new  ReverseConfirmedInfoDTO();
            reverseConfirmedInfoDTO.setRecordCode(recordCode);
            reverseConfirmedInfoDTO.setStatus(1);
            try {
                //修改单个单据状态为已确认，并并创建出入库后置单，同步库存中心
                reverseService.updateReverseRecordStatusConfirmed(recordCode,reverseConfirmFromPageDTO.getUserId());
                reverseConfirmedInfoDTO.setStatus(0);
            }catch (RomeException ex) {
                reverseConfirmedInfoDTO.setMessage(ex.getMessage());
                log.error(ex.getMessage(), ex);
            }catch (Exception e) {
                reverseConfirmedInfoDTO.setMessage(ResCode.ORDER_ERROR_1003_DESC);
                log.error(e.getMessage(), e);
            }finally {
                list.add(reverseConfirmedInfoDTO);
            }
        }
        return Response.builderSuccess(list);

    }



    @ApiOperation(value = "根据出库单据编码查询出库单(包含明细)", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recordCode", value = "单据编码", required = true)
    })
    @GetMapping(value = "/queryWarehouseRecordByRecordCode/{recordCode}")
    public Response<ReverseDTO> queryWarehouseRecordByRecordCode(@PathVariable String recordCode){
        try {
            ReverseDTO reverseDTO = reverseService.queryWarehouseRecordByRecordCode(recordCode);
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
            @ApiImplicitParam(name = "recordCode", value = "单据编码", required = true),
            @ApiImplicitParam(name = "wmsRecordCode", value = "收货单编码")
    })
    @GetMapping(value = "/queryReceiverRecordByRecordCode")
    public Response<List<ReceiverRecordDTO>> queryReceiverRecordByRecordCode(@RequestParam("recordCode") String recordCode, @RequestParam(value = "wmsRecordCode", required = false) String wmsRecordCode){
        try {
            List<ReceiverRecordDTO> receiverRecordDTOList = reverseService.queryReceiverRecordByRecordCode(recordCode, wmsRecordCode);
            return Response.builderSuccess(receiverRecordDTOList);
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
            return Response.builderSuccess(reverseService.queryReverseDetail(recordCode));
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
            List<CancelReverseDTO> cancelReverseDTOList = new ArrayList<>();
            recordCodes.forEach(recordCode -> {
                CancelReverseDTO cancelReverseDTO = new CancelReverseDTO();
                cancelReverseDTO.setRecordCode(recordCode);
                try {
                    reverseService.cancelReverse(recordCode);
                    cancelReverseDTO.setStatus(YesOrNoEnum.YES.getType());
                    cancelReverseDTO.setMessage("取消成功");
                } catch (Exception e) {
                    log.info("取消需求单异常：", e);
                    cancelReverseDTO.setStatus(YesOrNoEnum.NO.getType());
                    cancelReverseDTO.setMessage(e.getMessage());
                } finally {
                    cancelReverseDTOList.add(cancelReverseDTO);
                }
            });
            return Response.builderSuccess(cancelReverseDTOList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "冲销过账回调", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recordCode", value = "单据编号", required = true),
            @ApiImplicitParam(name = "voucherCode", value = "SAP凭证号", required = true)
    })
    @PutMapping(value = "/reverseSapNotify")
    public Response reverseSapNotify(@RequestParam(name = "recordCode") String recordCode, @RequestParam(name = "voucherCode") String voucherCode){
        try {
            return Response.builderSuccess(reverseService.reverseSapNotify(recordCode, voucherCode));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

}