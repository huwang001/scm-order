package com.lyf.scm.core.api.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.remote.user.dto.EmployeeInfoDTO;
import com.lyf.scm.core.remote.user.dto.OrganizationDTO;
import com.lyf.scm.core.remote.user.facade.UserFacade;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 公司组织部门控制层
 * 
 * @author WWH
 *
 */
@Slf4j
@RestController
@RequestMapping("/order/v1/user")
public class UserCoreController {

	@Resource
	private UserFacade userFacade;

	@ApiOperation(value = "根据组织结构code查询组织架构树", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/getOrganizationTree", method = RequestMethod.GET)
	public Response<PageInfo<OrganizationDTO>> getOrganizationTree() {
		try {
			List<OrganizationDTO> organizationTree = userFacade.getOrganizationTree();
			return Response.builderSuccess(organizationTree);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "根据用户ID查询员工信息", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/getEmployeeInfoByUserIds", method = RequestMethod.POST)
	public Response<List<EmployeeInfoDTO>> getEmployeeInfoByUserIds(@RequestBody List<Long> userIds) {
		try {
			List<EmployeeInfoDTO> employeeInfos = userFacade.getEmployeeInfoByUserIds(userIds);
			return Response.builderSuccess(employeeInfos);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "根据员工编号查询用户ID", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/getUserIdByEmployeeNum", method = RequestMethod.GET)
	public Response<EmployeeInfoDTO> getUserIdByEmployeeNum(@RequestParam("employeeNum") String employeeNum) {
		try {
			EmployeeInfoDTO employeeInfo = userFacade.getUserIdByEmployeeNum(employeeNum);
			return Response.builderSuccess(employeeInfo);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

}