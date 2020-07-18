package com.lyf.scm.admin.remote;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lyf.scm.admin.remote.dto.EmployeeInfoDTO;
import com.lyf.scm.admin.remote.dto.OrganizationDTO;
import com.rome.arch.core.clientobject.Response;

@FeignClient(value = "scm-order-service")
public interface UserRemoteService {

	@RequestMapping(value = "/order/v1/user/getOrganizationTree", method = RequestMethod.GET)
	Response<List<OrganizationDTO>> getOrganizationTree();

	@RequestMapping(value = "/order/v1/user/getEmployeeInfoByUserIds", method = RequestMethod.POST)
	Response<List<EmployeeInfoDTO>> getEmployeeInfoByUserIds(@RequestBody List<Long> userIds);

	@RequestMapping(value = "/order/v1/user/getUserIdByEmployeeNum", method = RequestMethod.GET)
	Response<EmployeeInfoDTO> getUserIdByEmployeeNum(@RequestParam("employeeNum") String employeeNum);

}