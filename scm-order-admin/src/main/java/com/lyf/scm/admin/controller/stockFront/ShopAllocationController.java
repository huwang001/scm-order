package com.lyf.scm.admin.controller.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.remote.dto.ShopAllocationDetailDTO;
import com.lyf.scm.admin.remote.dto.ShopAllocationRecordPageDTO;
import com.lyf.scm.admin.remote.stockFront.facade.ShopAllocationFacade;
import com.lyf.scm.common.constants.ResCode;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @date 2020/6/18
 * @Version
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/allocation")
public class ShopAllocationController {

    @Autowired
    private ShopAllocationFacade shopAllocationFacade;

    @ApiOperation(value = "查询门店调拨列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = List.class)
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<PageInfo<ShopAllocationRecordPageDTO>> query(@RequestBody ShopAllocationRecordPageDTO frontRecord) {
        try {
            PageInfo<ShopAllocationRecordPageDTO> result = shopAllocationFacade.queryShopAllocationList(frontRecord);
            Map<String , Object> res = new HashMap<>();
            if(null != result){
                res.put("list" , result.getList());
                res.put("total" ,result.getTotal() );
            }
            return Response.builderSuccess(res);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC,ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询门店调拨详情列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = List.class)
    @RequestMapping(value = "/detail/list", method = RequestMethod.GET)
    public Response<List<ShopAllocationDetailDTO>> queryDetails(@RequestParam Long frontRecordId) {
        try {
            List<ShopAllocationDetailDTO> result = shopAllocationFacade.queryShopAllocationDetailList(frontRecordId);
            return Response.builderSuccess(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC,ResCode.ORDER_ERROR_1003_DESC);
        }
    }




}
