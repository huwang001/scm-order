package com.lyf.scm.admin.controller.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.shop.ShopAdjustRecordDTO;
import com.lyf.scm.admin.remote.stockFront.facade.ShopAdjustFacade;
import com.lyf.scm.common.constants.ResCode;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/order/v1/shopAdjustRecord")
public class ShopAdjustRecordController {

    @Resource
    private ShopAdjustFacade shopAdjustFacade;

    @ApiOperation(value = "批量查询门店调整单", nickname = "find_shop_adjust_record_condition", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryCondition", method = RequestMethod.POST)
    public Response findShopForetasteCondition(@RequestBody ShopAdjustRecordDTO paramDTO) {
        try {
            PageInfo<ShopAdjustRecordDTO> salesReturnRecordPageInfo = shopAdjustFacade.findShopForetasteCondition(paramDTO,
                    paramDTO.getPageNum(), paramDTO.getPageSize());
            return Response.builderSuccess(salesReturnRecordPageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据试吃单id查询门店调整单", nickname = "getAdjustForetasteByRecordId", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/getAdjustForetasteByRecordId", method = RequestMethod.GET)
    public Response getAdjustForetasteByRecordId(@RequestParam("recordId") Long recordId) {
        try {
            ShopAdjustRecordDTO adjustRecordDTO = shopAdjustFacade.getAdjustForetasteByRecordId(recordId);
            return Response.builderSuccess(adjustRecordDTO);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }
}
