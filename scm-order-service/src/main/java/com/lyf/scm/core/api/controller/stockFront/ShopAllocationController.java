package com.lyf.scm.core.api.controller.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.KibanaLogConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.ResponseMsg;
import com.lyf.scm.core.api.dto.stockFront.*;
import com.lyf.scm.core.config.ScmCallLog;
import com.lyf.scm.core.config.ServiceKibanaLog;
import com.lyf.scm.core.service.stockFront.ShopAllocationService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description 门店调拨
 * @date 2020/6/15
 * @Version
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/shopAllocation")
@Api(tags={"门店调拨"})
public class ShopAllocationController {

    @Autowired
    private ShopAllocationService shopAllocationService;


    @ApiOperation(value = "批量处理门店调拨推送cmp", nickname = "ShopAllocationRecord", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = Response.class)
    @RequestMapping(value = "/warehouseRecord/handleShopAllocationRecordsPushCmp", method = RequestMethod.POST)
    public Response<String> handleShopAllocationRecordsPushCmp() {
        try {
            shopAllocationService.handleShopAllocationRecordsPushCmp();
            return Response.builderSuccess("");
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC,ResCode.ORDER_ERROR_1003_DESC);
        }
    }
    @ScmCallLog(systemName = "inner-trade", recordCode = "#frontRecord.outRecordCode")
    @ApiOperation(value = "门店调拨单创建", nickname = "ShopAllocationRecord", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = ShopAllocationRecordDTO.class)
    @RequestMapping(value = "/frontRecord/addShopAllocationRecord", method = RequestMethod.POST)
    public Response<ShopAllocationRecordDTO> addShopAllocationRecord(@ApiParam(name = "frontRecord", value = "门店调拨单创建") @RequestBody @Validated ShopAllocationRecordDTO frontRecord) {
        try {
            //输出kibana日志
            log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.SHOP_ALLOCATION,"addShopAllocationRecord","创建门店调拨单"
                    +frontRecord.getOutRecordCode(),frontRecord));
            //不允许相同门店调拨
            if(frontRecord.getInShopCode().trim().equals(frontRecord.getOutShopCode().trim())){
                return Response.builderFail(ResCode.ORDER_ERROR_7301,ResCode.ORDER_ERROR_7301_DESC);
            }
            frontRecord= shopAllocationService.addShopAllocationRecord(frontRecord);
            return Response.builderSuccess(frontRecord);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC,ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询门店调拨单列表", nickname = "query_shop_allocation_list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = List.class)
    @RequestMapping(value = "/frontRecord/queryShopAllocationList", method = RequestMethod.POST)
    public Response<PageInfo<ShopAllocationRecordPageDTO>>  queryShopAllocationList(@ApiParam(name = "shopInventory", value = "dto") @RequestBody ShopAllocationRecordPageDTO frontRecord) {
        try {
            PageInfo<ShopAllocationRecordPageDTO> personPageInfo = shopAllocationService.queryShopAllocationList(frontRecord);
            return Response.builderSuccess(personPageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC,ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询调拨单详情列表", nickname = "query_shop_allocation_detail_list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = List.class)
    @RequestMapping(value = "/frontRecord/queryShopAllocationDetailList", method = RequestMethod.GET)
    public Response<List<ShopInventoryDetailDTO>>  queryShopAllocationDetailList(@ApiParam(name = "前置单id", value = "frontRecordId") @RequestParam Long frontRecordId) {
        try {
            List<ShopAllocationDetailDTO> list = shopAllocationService.queryShopAllocationDetailList(frontRecordId);
            return Response.builderSuccess(list);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC,ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "设置是否直接通过sap查询权限", nickname = "getCode", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = WarehouseToBRecord.class)
    @RequestMapping(value = "/setStatus", method = RequestMethod.POST)
    public Response<String> setStatus(@RequestParam("status") boolean status) {
        try {
            return ResponseMsg.SUCCESS.buildMsg(shopAllocationService.setCheckStatus(status));
        } catch (RomeException e) {
            return Response.builderFail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC,ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据出入库单据编号查询门店调拨单", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryShopAllocationByRecordCode", method = RequestMethod.GET)
    public Response<ShopAllocationRecordDTO> queryShopAllocationByRecordCode(@ApiParam(name = "recordCode", value = "出入库单据编号") @RequestParam("recordCode") String recordCode){

            try{
                log.info("根据出入库单据编号查询门店调拨单 <<< {}",recordCode);
                ShopAllocationRecordDTO shopAllocationRecordDTO = shopAllocationService.queryAllocationByRecordCode(recordCode);
                return Response.builderSuccess(shopAllocationRecordDTO);
            }catch (RomeException e) {
                log.error(e.getMessage(), e);
                return Response.builderFail(e.getCode(), e.getMessage());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
            }
    }
}
