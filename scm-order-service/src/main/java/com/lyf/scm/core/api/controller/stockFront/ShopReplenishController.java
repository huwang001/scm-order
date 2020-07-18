package com.lyf.scm.core.api.controller.stockFront;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.KibanaLogConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.ResponseMsg;
import com.lyf.scm.core.api.dto.stockFront.*;
import com.lyf.scm.core.config.ScmCallLog;
import com.lyf.scm.core.config.ServiceKibanaLog;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.service.stockFront.ShopReplenishService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 门店补货Controller
 * <p>
 * @Author: chuwenchao  2020/6/10
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/shopReplenish")
@Api(tags = { "门店补货" })
public class ShopReplenishController {

    @Resource
    private ShopReplenishService shopReplenishService;

    @ApiOperation(value = "创建门店补货需求单", nickname = "createReplenishRecordBatch", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @ScmCallLog(systemName = "inner-trade", recordCode = "#frontRecord!=null?#frontRecord.outRecordCode:#arg0.outRecordCode")
    @RequestMapping(value = "/createReplenishRecord", method = RequestMethod.POST)
    public Response<RealWarehouse> createReplenishRecord(@RequestBody @Valid ShopReplenishDTO frontRecord) {
        log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.SHOP_REPLENISH, "createReplenishRecord", "创建门店补货需求单: " + frontRecord.getOutRecordCode(), frontRecord));
        try {
            RealWarehouse realWarehouse = shopReplenishService.createReplenishRecord(frontRecord);
            return Response.builderSuccess(realWarehouse);
        } catch (RomeException e) {
            log.error("innerTrade调用 : " + e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error("innerTrade调用 : " + e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "修改门店补货需求单", nickname = "createReplenishRecordBatch", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @ScmCallLog(systemName = "inner-trade", recordCode = "#frontRecord!=null?#frontRecord.outRecordCode:#arg0.outRecordCode")
    @RequestMapping(value = "/updateReplenishRecord", method = RequestMethod.POST)
    public Response updateReplenishRecord(@RequestBody @Valid ShopReplenishDTO frontRecord) {
        log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.SHOP_REPLENISH, "updateReplenishRecord", "修改门店补货需求单: " + frontRecord.getOutRecordCode(), frontRecord));
        try {
            shopReplenishService.updateReplenishRecord(frontRecord);
            return Response.builderSuccess(null);
        } catch (RomeException e) {
            log.error("innerTrade调用 : " + e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error("innerTrade调用 : " + e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "校验是否可以修改门店补货需求单", nickname = "createReplenishRecordBatch", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @ScmCallLog(systemName = "inner-trade", recordCode = "#poCode")
    @RequestMapping(value = "/checkUpdateReplenishRecord", method = RequestMethod.POST)
    public Response<Boolean> checkUpdateReplenishRecord(@RequestParam("poCode") String poCode) {
        log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.SHOP_REPLENISH, "checkUpdateReplenishRecord", "校验是否可以修改门店补货需求单: " + poCode, poCode));
        try {
            boolean flag = shopReplenishService.checkUpdateReplenishRecord(poCode);
            return Response.builderSuccess(flag);
        } catch (RomeException e) {
            log.error("innerTrade调用 : " + e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error("innerTrade调用 : " + e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "加盟店补货确认(扣减额度成功后调用)", nickname = "confimJoinReplenish", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @ScmCallLog(systemName = "inner-trade", recordCode = "#poCode")
    @RequestMapping(value = "/confirmJoinReplenish", method = RequestMethod.POST)
    public Response confirmJoinReplenish(@RequestParam("poCode") String poCode, @RequestParam("sapPoNo") String sapPoNo){
        log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.SHOP_REPLENISH, "confirmJoinReplenish", "加盟店补货确认: " + poCode, sapPoNo));
        try {
            shopReplenishService.confirmJoinReplenish(poCode, sapPoNo);
            return ResponseMsg.SUCCESS.buildMsg();
        } catch (RomeException e) {
            log.error("innerTrade调用 : " + e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error("innerTrade调用 : " + e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "取消申请", nickname = "cancelReplenish", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @ScmCallLog(systemName = "inner-trade", recordCode = "#poCode")
    @RequestMapping(value = "/cancelReplenish", method = RequestMethod.POST)
    public Response cancelReplenish(@RequestParam("poCode") String poCode){
        log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.SHOP_REPLENISH, "cancelReplenish", "取消申请: " + poCode, poCode));
        try {
            shopReplenishService.cancelReplenish(poCode, 0, -1L);
            return ResponseMsg.SUCCESS.buildMsg();
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.EXCEPTION.buildMsg();
        }
    }

    @ApiOperation(value = "定时处理直营门店需求单--寻源分配", nickname = "allotShopReplenish", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/allotShopReplenish", method = RequestMethod.POST)
    public Response allotShopReplenish(@RequestBody ShopReplenishAllotDTO allotDTO) {
        log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.SHOP_REPLENISH, "allotShopReplenish", "寻源分配: ", allotDTO));
        String message ="";
        Response response = null;
        try {
            Integer times = shopReplenishService.allotShopReplenish(allotDTO);
            response = ResponseMsg.SUCCESS.buildMsg(times);
        } catch (RomeException e) {
            log.error("innerTrade调用 : " + e.getMessage(), e);
            response = Response.builderFail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error("innerTrade调用 : " + e.getMessage(), e);
            response = Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
        log.info("寻源分配参数==> {}, 结果==> {}", JSON.toJSONString(allotDTO), JSON.toJSONString(response));
        return response;
    }

    @ApiOperation(value = "批量取消申请", nickname = "cancelReplenishBatch", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/cancelReplenishBatch", method = RequestMethod.POST)
    public Response cancelReplenishBatch(@RequestBody BatchCancleDTO dto){
        String list = dto.getList();
        log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.SHOP_REPLENISH, "cancelReplenishBatch", "批量取消申请: ", list));
        List<String> errorList =  new ArrayList<>();
        try {
            if(StringUtils.isNotBlank(list)){
                String [] array = list.split(",");
                if(array.length > 0){
                    for (String poCode : array) {
                        try {
                            shopReplenishService.cancelReplenish(poCode, 1, dto.getUserId());
                        }catch (Exception e){
                            errorList.add(poCode);
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            }
            if(CollectionUtils.isNotEmpty(errorList)){
                return Response.builderFail(ResCode.ORDER_ERROR_1001, StringUtils.join(errorList.toArray(), ",") + "取消失败");
            }else{
                return ResponseMsg.SUCCESS.buildMsg("全部取消成功");
            }
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.EXCEPTION.buildMsg();
        }
    }

    @ApiOperation(value = "批量取消申请(支持url的取消)", nickname = "cancelBatchGet", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/cancelBatchGet", method = RequestMethod.GET)
    public Response cancelBatchGet(String list){
        log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.SHOP_REPLENISH, "cancelBatchGet", "批量取消申请: ", list));
        List<String> errorList =  new ArrayList<>();
        try {
            if(StringUtils.isNotBlank(list)){
                String [] array = list.split(",");
                if(array.length > 0){
                    for (String poCode : array) {
                        try {
                            shopReplenishService.cancelReplenish(poCode, 1, -1L);
                        }catch (Exception e){
                            errorList.add(poCode);
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            }
            if(CollectionUtils.isNotEmpty(errorList)){
                return Response.builderFail(ResCode.ORDER_ERROR_1001, StringUtils.join(errorList.toArray(), ",") + "取消失败");
            }else{
                return ResponseMsg.SUCCESS.buildMsg("全部取消成功");
            }
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.EXCEPTION.buildMsg();
        }
    }

    @ApiOperation(value = "查询寻源日志", nickname = "queryReplenishAllotLogCondition", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryReplenishAllotLogCondition", method = RequestMethod.POST)
    public Response<PageInfo<ReplenishAllotLogDTO>> queryReplenishAllotLogCondition(@ApiParam(name = "condition", value = "查询条件") @RequestBody ReplenishAllotLogDTO condition) {
        try {
            return Response.builderSuccess(shopReplenishService.queryReplenishAllotLogDTO(condition));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }


    @ApiOperation(value = "寻源结果报表", nickname = "queryReplenishReportCondition", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryReplenishReportCondition", method = RequestMethod.POST)
    public Response<PageInfo<ShopReplenishReportDetailDTO>> queryReplenishReportCondition(@ApiParam(name = "condition", value = "查询条件") @RequestBody ShopReplenishReportDTO condition) {
        log.info("寻源结果报表, condition: {}", JSONObject.toJSONString(condition));
        try {
            return Response.builderSuccess(shopReplenishService.queryReplenishReportDTO(condition));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "交货单汇总", nickname = "statReplenishReport", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/statReplenishReport", method = RequestMethod.POST)
    public Response<PageInfo<ShopReplenishReportStatDTO>> statReplenishReport(@ApiParam(name = "condition", value = "查询条件") @RequestBody ShopReplenishReportDTO condition) {
        log.info("交货单汇总, condition: {}", JSONObject.toJSONString(condition));
        try {
            return Response.builderSuccess(shopReplenishService.statReplenishReport(condition));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC, ResCode.ORDER_ERROR_1003_DESC);
        }
    }
}
