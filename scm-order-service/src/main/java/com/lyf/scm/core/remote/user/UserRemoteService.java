package com.lyf.scm.core.remote.user;


import com.lyf.scm.core.remote.user.dto.EmployeeInfoDTO;
import com.lyf.scm.core.remote.user.dto.OrganizationDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户中心远程接口
 */
@FeignClient(value = "user-core")
public interface UserRemoteService {

    /**
     * 根据用户ID集合查询员工信息列表
     *
     * @param userIds
     * @return
     */
    @RequestMapping(value = "/emp/v1/search/userIdList",method = RequestMethod.POST)
    Response<List<EmployeeInfoDTO>> getEmployeeInfoByUserIds(@RequestBody List<Long> userIds);

    /**
     * 根据员工编号查询用户信息
     * @param empNum
     * @return
     */
    @RequestMapping(value = "/emp/v1/search/empNum",method = RequestMethod.GET)
    Response<EmployeeInfoDTO> getUserIdByEmployeeNum(@RequestParam("empNum") String empNum);

    /**
     * 查询组织架构树
     *
     * @return
     */
    @RequestMapping(value = "/organization/v1/organizations",method = RequestMethod.GET)
    Response<List<OrganizationDTO>> getOrganizationTree();

    /**
     * 递归查询组织架构
     *
     * @param parentCode
     * @return
     */
    @RequestMapping(value = "/organization/parent/{parentCode}",method = RequestMethod.GET)
    Response<List<OrganizationDTO>> getOrganizationByParentCode(@PathVariable ("parentCode") String parentCode);


}
