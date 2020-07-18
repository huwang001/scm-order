package com.lyf.scm.core.api.controller.online;

import com.lyf.scm.common.constants.CommonConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.util.validate.ParamValidator;
import com.lyf.scm.core.api.dto.online.RecordPoolDTO;
import com.lyf.scm.core.service.online.OnlineOrderQueryService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 电商相关服务(外接交易中心)--查询类
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1")
@Api(tags = {"电商服务接口-查询"})
public class OnlineOrderQueryController {

    @Resource
    private OnlineOrderQueryService onlineOrderQueryService;

    private ParamValidator validator = ParamValidator.INSTANCE;

    /**
     * 根据单据池DO单号查询单据池信息
     *
     * @param poolRecordCodes
     * @return
     */
    @ApiOperation(value = "根据单据池DO单号查询单据池信息", nickname = "queryPoolRecordByPoolRecordCode", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = String.class)
    @GetMapping("/queryPoolRecordByPoolRecordCode")
    public Response<RecordPoolDTO> queryPoolRecordByPoolRecordCode(String[] poolRecordCodes) {
        if (poolRecordCodes == null || poolRecordCodes.length == 0 || poolRecordCodes.length > CommonConstants.BATCH_PARAM_MAX_LENGTH) {
            return Response.builderFail(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        List<String> codeList = Arrays.asList(poolRecordCodes);
        try {
            List<RecordPoolDTO> res = onlineOrderQueryService.queryPoolRecordByPoolRecordCode(codeList);
            return Response.builderSuccess(res);
        } catch (RomeException e) {
            log.info(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            return Response.builderFail(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 根据父doCode查询子do列表
     *
     * @param parentCode 电商下单相关参数
     * @return
     */
    @ApiOperation(value = "根据父doCode查询子do列表", nickname = "queryChildDoCodeByParentDo", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = String.class)
    @PostMapping("/queryChildDoCodeByParentDo")
    public Response<List<String>> queryChildDoCodeByParentDo(@ApiParam(name = "parentCode", value = "parentCode") @RequestParam String parentCode) {
        if (parentCode == null) {
            return Response.builderFail(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        try {
            List<String> res = onlineOrderQueryService.queryChildDoCodeByParentDo(parentCode);
            return Response.builderSuccess(res);
        } catch (RomeException e) {
            log.info(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }
}
