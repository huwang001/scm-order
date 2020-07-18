package com.lyf.scm.admin.controller.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.remote.dto.ShopInventoryDetailDTO;
import com.lyf.scm.admin.remote.dto.ShopInventoryPageDTO;
import com.lyf.scm.admin.remote.stockFront.facade.ShopInventoryFacade;
import com.lyf.scm.common.constants.ResCode;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 门店盘点单据
 *
 * @author jl
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/record")
public class ShopInventoryController {

    @Resource
    private ShopInventoryFacade shopInventoryFacade;

    @ApiOperation(value = "查询门店盘点列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = List.class)
    @RequestMapping(value = "/shopInventory/list", method = RequestMethod.POST)
    public Response<PageInfo<ShopInventoryPageDTO>> query(@RequestBody ShopInventoryPageDTO frontRecord) {
        try {
            PageInfo<ShopInventoryPageDTO> result = shopInventoryFacade.queryShopInventoryList(frontRecord);
            Map<String, Object> res = new HashMap<>();
            if (null != result) {
                res.put("list", result.getList());
                res.put("total", result.getTotal());
            }
            return Response.builderSuccess(res);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询门店盘点详情列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = List.class)
    @RequestMapping(value = "/shopInventoryDetail/list", method = RequestMethod.GET)
    public Response<List<ShopInventoryDetailDTO>> queryDetails(@RequestParam Long frontRecordId) {
        try {
            List<ShopInventoryDetailDTO> result = shopInventoryFacade.queryShopInventoryDetailList(frontRecordId);
            return Response.builderSuccess(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }
}
