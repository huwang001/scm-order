package com.lyf.scm.core.remote.user.facade;

import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.core.remote.user.UserRemoteService;
import com.lyf.scm.core.remote.user.dto.EmployeeInfoDTO;
import com.lyf.scm.core.remote.user.dto.OrganizationDTO;
import com.rome.arch.core.clientobject.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author zhangtuo
 * @Date 2019/6/19 15:19
 * @Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class UserFacade {

	@Resource
	private UserRemoteService userRemoteService;

	/**
	 * 根据用户ID集合查询员工信息列表
	 *
	 * @param userIds
	 * @return
	 */
	public List<EmployeeInfoDTO> getEmployeeInfoByUserIds(List<Long> userIds) {
		if (CollectionUtils.isEmpty(userIds)) {
			return new ArrayList<>();
		}
		Response<List<EmployeeInfoDTO>> response = userRemoteService.getEmployeeInfoByUserIds(userIds);
		ResponseValidateUtils.validResponse(response);
		return response.getData();
	}

	/**
	 * 根据员工编号查询用户信息
	 *
	 * @param employeeNum
	 * @return
	 */
	public EmployeeInfoDTO getUserIdByEmployeeNum(String employeeNum) {
		if (StringUtils.isBlank(employeeNum)) {
			return  new EmployeeInfoDTO();
		}
		Response<EmployeeInfoDTO> response = userRemoteService.getUserIdByEmployeeNum(employeeNum);
		ResponseValidateUtils.validResponse(response);
		return response.getData();
	}

	/**
	 * 查询组织架构树
	 *
	 * @return
	 */
	public List<OrganizationDTO> getOrganizationTree() {
		Response<List<OrganizationDTO>> response = userRemoteService.getOrganizationTree();
		ResponseValidateUtils.validResponse(response);
		return response.getData();
	}

	/**
	 * 递归查询组织架构
	 *
	 * @param parentCode
	 * @return
	 */
	List<OrganizationDTO> getOrganizationByParentCode(String parentCode) {
		if (StringUtils.isBlank(parentCode)) {
			return  new ArrayList<>();
		}
		Response<List<OrganizationDTO>> response = userRemoteService.getOrganizationByParentCode(parentCode);
		ResponseValidateUtils.validResponse(response);
		List<OrganizationDTO> childrens = response.getData();
		if (CollectionUtils.isNotEmpty(childrens)) {
			for (OrganizationDTO organizationDTO : childrens) {
				List<OrganizationDTO> list = getOrganizationByParentCode(organizationDTO.getOrgCode());
				if (null != list && 0 != list.size()) {
					organizationDTO.setChildrenNodes(list);
				}
			}
		}
		return childrens;
	}

	/**
	 * 所有的组织结构拆解为树状
	 *
	 * @param dtoList
	 */
	public List<OrganizationDTO> orgListToTree(List<OrganizationDTO> dtoList) {
		Map<String, List<OrganizationDTO>> orgListMap = new HashMap<>();
		List<OrganizationDTO> retList = new ArrayList<>();

		for (OrganizationDTO dto : dtoList) {
			if (null != dto.getParentOrgCode()) {
				if (!orgListMap.containsKey(dto.getParentOrgCode())) {
					List<OrganizationDTO> childList = new ArrayList<>();
					orgListMap.put(dto.getParentOrgCode(), childList);
				} else {
					orgListMap.get(dto.getParentOrgCode()).add(dto);
				}
			}
		}

		for (OrganizationDTO dto : dtoList) {
			if (orgListMap.containsKey(dto.getOrgCode()) && 0 != orgListMap.get(dto.getOrgCode()).size()) {
				dto.setChildrenNodes(orgListMap.get(dto.getOrgCode()));
			}
			if ("2".equals(dto.getOrgType())) {
				retList.add(dto);
			}
		}
		return retList;
	}

}