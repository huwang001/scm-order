package com.lyf.scm.admin.controller.pack;

import javax.annotation.Resource;

import com.lyf.scm.admin.dto.pack.PackDemandComponentDTO;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSON;
import com.lyf.scm.admin.dto.pack.DemandAllotDTO;
import com.lyf.scm.admin.remote.pack.facade.PackDemandComponentFacade;
import com.lyf.scm.common.constants.ResCode;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import com.rome.user.common.utils.UserContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@RomeController
@RequestMapping("/order/v1/pack/demandComponent")
@Api(tags = {"包装需求单明细原料管理"})
public class PackDemandComponentController {

	@Resource
	private PackDemandComponentFacade packDemandComponentFacade;
	
	@ApiOperation(value = "根据需求单明细原料创建调拨单", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/createDemandAllot", method = RequestMethod.POST)
	public Response createDemandAllot(@ApiParam(name = "demandAllotDTO", value = "需求调拨对象") @RequestBody @Validated DemandAllotDTO demandAllotDTO) {
		try {
			log.info("根据需求单明细原料创建调拨单 <<< {}", JSON.toJSONString(demandAllotDTO));
			Long userId = UserContext.getUserId();
			demandAllotDTO.setUserId(userId);
			packDemandComponentFacade.createDemandAllot(demandAllotDTO);
			return Response.builderSuccess(null);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "调拨时根据需求编码查询需求单明细原料列表")
	@GetMapping(value = "/queryDemandComponentByRecordCodeAllot")
	public Response queryDemandComponentByRecordCodeAllot(@ApiParam(name = "recordCode", value = "需求单编号") @RequestParam("recordCode") String recordCode) {
		try {
			log.info("调拨时根据需求编码查询需求单明细原料列表 <<< {}", JSON.toJSONString(recordCode));
			List<PackDemandComponentDTO> packDemandComponentDtos = packDemandComponentFacade.queryDemandComponentByRecordCodeAllot(recordCode);
			return Response.builderSuccess(packDemandComponentDtos);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}
   
}