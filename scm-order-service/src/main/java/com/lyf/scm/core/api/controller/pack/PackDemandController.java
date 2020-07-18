package com.lyf.scm.core.api.controller.pack;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.YesOrNoEnum;
import com.lyf.scm.common.util.CollectorsUtil;
import com.lyf.scm.core.api.dto.pack.*;
import com.lyf.scm.core.config.ScmCallLog;
import com.lyf.scm.core.service.pack.PackDemandService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * @Desc:
 * @author:Huangyl
 * @date: 2020/7/6
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/pack/demand")
@Api(tags = {"包装需求单"})
public class PackDemandController {

    @Resource
    private PackDemandService packDemandService;

    @ApiOperation(value = "包装系统-同步需求单包装完成状态", nickname = "packComplete", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/packComplete", method = RequestMethod.POST)
    @ScmCallLog(systemName = "scm-package-service", recordCode = "#recordCode!=null?#recordCode:#arg0")
    public Response packComplete(@ApiParam(name = "requireCode", value = "包装需求单code") @RequestBody  String recordCode) {

        try {
            log.info("包装系统-同步需求单包装完成状态，参数 <<< {}", JSON.toJSONString(recordCode));
            packDemandService.updatePackComplete(recordCode);
            return Response.builderSuccess(true);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }

    }

    @ApiOperation(value = "根据SO（预约单）创建需求单Test", nickname = "createPackDemandBySO", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/createPackDemandBySO", method = RequestMethod.POST)
    public Response createPackDemandBySO(@RequestBody DemandFromSoDTO demandFromSoDTO) {

        try {
            log.info("根据SO（预约单）创建需求单，参数 <<< {}", JSON.toJSONString(demandFromSoDTO.toString()));
            packDemandService.createPackDemandBySo(demandFromSoDTO);
            return Response.builderSuccess(true);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }


    @ApiOperation(value = "页面确认需求单单据状态", nickname = "demandRecordStatusConfirmed", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/demandRecordStatusConfirmed", method = RequestMethod.POST)
    public Response<List<DemandConfirmedInfoDTO>> demandRecordStatusConfirmed(@RequestBody DemandConfirmFromPageDTO demandConfirmFromPageDTO) {

         log.info("页面确认需求单单据状态，参数 <<< {}", JSON.toJSONString(demandConfirmFromPageDTO.toString()));
            if (CollectionUtils.isEmpty(demandConfirmFromPageDTO.getRecordCodeList())) {
                throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
            }
            // 记录信息
            List<DemandConfirmedInfoDTO> list = new ArrayList(demandConfirmFromPageDTO.getRecordCodeList().size());
            for (String requireCode : demandConfirmFromPageDTO.getRecordCodeList()){
                DemandConfirmedInfoDTO demandConfirmedInfoDTO = new  DemandConfirmedInfoDTO();
                demandConfirmedInfoDTO.setRecordCode(requireCode);
                demandConfirmedInfoDTO.setStatus(1);
                try {
                    //单个修改单据状态为已确认，并下发包装系统
                    packDemandService.updateDemandRecordStatusConfirmed(requireCode,demandConfirmFromPageDTO.getUserId());
                    demandConfirmedInfoDTO.setStatus(0);
                }catch (RomeException ex) {
                    demandConfirmedInfoDTO.setMessage(ex.getMessage());
                    log.error(ex.getMessage(), ex);
                }catch (Exception e) {
                    demandConfirmedInfoDTO.setMessage(ResCode.ORDER_ERROR_1003_DESC);
                    log.error(e.getMessage(), e);
                }finally {
                    list.add(demandConfirmedInfoDTO);
                }
            }
            return Response.builderSuccess(list);

    }

    @ApiOperation(value = "包装系统-需求单创建", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/createDemand", method = RequestMethod.POST)
    public Response<String> createDemand(@ApiParam(name = "demandDTO", value = "包装需求参数") @RequestBody DemandDTO demandDTO) {
        try {
            String result = packDemandService.createPackDemand(demandDTO);
            return Response.builderSuccess(result);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "包装系统-根据历史需求号返回成品明细和组件明细", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryDemandDetailAndComponent", method = RequestMethod.GET)
    public Response<PackDemandResponseDTO> queryDemandDetailAndComponent(@ApiParam(name = "recordCode", value = "包装需求单号") @RequestParam(name = "recordCode", required = false) String recordCode) {
        try {
            PackDemandResponseDTO result = packDemandService.queryDemandDetailAndComponent(recordCode);
            return Response.builderSuccess(result);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "包装系统-批量导入需求单,反拆类型的不适用", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/batchCreateDemand", method = RequestMethod.POST)
    public Response<String> batchCreateDemand(@ApiParam(name = "recordCode", value = "包装需求单号") @RequestBody List<DemandBatchDTO> demandBatchDTOList, @RequestParam("userId") Long userId) {
        try {
            packDemandService.batchCreatePackDemand(demandBatchDTOList, userId);
            return Response.builderSuccess(true);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "需求单列表查询-分页", nickname = "queryPackDemandPage", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryPackDemandPage", method = RequestMethod.POST)
    public Response<PageInfo<PackDemandResponseDTO>> queryPackDemandPage(@ApiParam(name = "condition", value = "查询条件") @RequestBody @Validated QueryPackDemandDTO condition) {
        try {
            //在后台再做一次时间校验
            if (condition.getStartTime() == null || condition.getEndTime() == null) {
                throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + ":创建时间必填");
            }
            long max = 3600 * 24 * 30 * 1000L;
            if (condition.getEndTime().getTime() - condition.getStartTime().getTime() > max) {
                throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + ":创建时间查询时跨度不能大于30天");
            }
            return Response.builderSuccess(packDemandService.queryPackDemandPage(condition));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "需求单列表-导出", nickname = "exportPackDemand", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryPackDemandExport", method = RequestMethod.POST)
    public Response<List<PackDemandResponseDTO>> queryPackDemandExport(@ApiParam(name = "condition", value = "查询条件") @RequestBody @Validated QueryPackDemandDTO condition) {
        try {
            if (condition.getStartTime() == null || condition.getEndTime() == null) {
                throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + ":创建时间必填");
            }
            long max = 3600 * 24 * 30 * 1000L;
            if (condition.getEndTime().getTime() - condition.getStartTime().getTime() > max) {
                throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + ":创建时间查询时跨度不能大于30天");
            }
            List<PackDemandResponseDTO> list = packDemandService.queryPackDemandExport(condition);
            return Response.builderSuccess(list);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询需求单详情", nickname = "queryPackDemandDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryPackDemandDetail", method = RequestMethod.POST)
    public Response<PackDemandResponseDTO> queryPackDemandDetail(@RequestParam("recordCode") String recordCode) {
        try {
            return Response.builderSuccess(packDemandService.queryPackDemandDetail(recordCode));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "批量取消需求单", nickname = "batchCancelDemand", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/batchCancelDemand", method = RequestMethod.POST)
    public Response<List<DemandCancelResponseDTO>> batchCancelDemand(@RequestBody List<String> requiredCodeList) {
        try {
            log.info("批量取消需求单，取消参数：{}", JSON.toJSONString(requiredCodeList));
            DemandCancelResponseDTO cancelResponse = null;
            List<DemandCancelResponseDTO> demandCancelResponseList = new ArrayList<>();
            for (String recordCode : requiredCodeList) {
                cancelResponse = new DemandCancelResponseDTO();
                cancelResponse.setRequireCode(recordCode);
                try {
                    packDemandService.cancelPackDemand(recordCode);
                    cancelResponse.setStatus(YesOrNoEnum.NO.getType());
                    cancelResponse.setMessage("取消成功");
                } catch (Exception e) {
                    log.info("取消需求单异常：", e);
                    cancelResponse.setStatus(YesOrNoEnum.YES.getType());
                    cancelResponse.setMessage(e.getMessage());
                } finally {
                    demandCancelResponseList.add(cancelResponse);
                }
            }
            return Response.builderSuccess(demandCancelResponseList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "需求单列表查询", nickname = "queryPackDemandList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryPackDemandList", method = RequestMethod.POST)
    public Response<PageInfo<PackDemandResponseDTO>> queryPackDemandList(@ApiParam(name = "condition", value = "查询条件") @RequestBody @Validated QueryPackDemandDTO condition) {
        try {
            return Response.builderSuccess(packDemandService.queryPackDemandList(condition));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }
}
