package com.lyf.scm.admin.remote.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lyf.scm.admin.remote.UserRemoteService;
import com.lyf.scm.admin.remote.dto.EmployeeInfoDTO;
import com.lyf.scm.admin.remote.dto.OrganizationDTO;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserFacade {

	@Autowired
	private UserRemoteService userRemoteService;

	/**
	 * 根据组织结构code查询组织架构树
	 * 
	 * @return
	 */
	public List<OrganizationDTO> getOrganizationTree() {
		Response<List<OrganizationDTO>> response = userRemoteService.getOrganizationTree();
		ResponseValidateUtils.validResponse(response);
		return response.getData();
	}

	/**
	 * 根据用户ID查询员工信息
	 * 
	 * @param userIds
	 * @return
	 */
	public List<EmployeeInfoDTO> getEmployeeInfoByUserIds(List<Long> userIds) {
		Response<List<EmployeeInfoDTO>> response = userRemoteService.getEmployeeInfoByUserIds(userIds);
		ResponseValidateUtils.validResponse(response);
		return response.getData();
	}

	/**
	 * 根据员工编号查询用户ID
	 * 
	 * @param employeeNum
	 * @return
	 */
	public EmployeeInfoDTO getUserIdByEmployeeNum(String employeeNum) {
		Response<EmployeeInfoDTO> response = userRemoteService.getUserIdByEmployeeNum(employeeNum);
		ResponseValidateUtils.validResponse(response);
		return response.getData();
	}

}