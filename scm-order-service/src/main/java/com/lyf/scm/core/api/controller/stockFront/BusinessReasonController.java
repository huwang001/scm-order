package com.lyf.scm.core.api.controller.stockFront;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.api.dto.stockFront.BusinessReasonDTO;
import com.lyf.scm.core.service.stockFront.BusinessReasonService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author zhangtuo
 * @Date 2019/5/13 15:45
 * @Version 1.0
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/businessReason")
@Api(tags = { "业务原因服务接口" })
public class BusinessReasonController {

	@Resource
	BusinessReasonService businessReasonService;

	@ApiOperation(value = "根据单据类型查询业务原因列表", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/queryBusinessReasonByRecordType/{recordType}", method = RequestMethod.GET)
	public Response<List<BusinessReasonDTO>> queryBusinessReasonByRecordType(
			@ApiParam(name = "recordType", value = "单据类型") @PathVariable Integer recordType) {
		try {
			log.info("根据单据类型查询业务原因列表 <<< {}", recordType);
			List<BusinessReasonDTO> businessReasonList = businessReasonService
					.queryBusinessReasonByRecordType(recordType);
			return Response.builderSuccess(businessReasonList);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

}