package com.lyf.scm.admin.remote;

import com.lyf.scm.admin.remote.dto.EmployeeInfoDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description
 * @Author zhangtuo
 * @Date 2019/6/19 15:27
 * @Version 1.0
 */
@FeignClient(value = "user-core", url = "${feignClient.stock.user-core.url:}")
public interface UserCoreRemoteService {

//    /**
//     * 根据父级组织code查询子集
//     * @param parentCode
//     * @return
//     */
//    @RequestMapping(value = "/organization/parent/{parentCode}",method = RequestMethod.GET)
//    Response<List<OrganizationDTO>> getOrganizationByParentCode(@PathVariable("parentCode") String parentCode);
//
//    /**
//     * 根据组织code查询组织信息
//     * @param orgCode
//     * @return
//     */
//    @RequestMapping(value = "/organization/code/{orgCode}",method = RequestMethod.GET)
//    Response<OrganizationDTO> getOrganizationByOrgCode(@PathVariable("orgCode") String orgCode);
//
//    /**
//     * 查询所有组织结构
//     * @return
//     */
//    @RequestMapping(value = "/organization/v1/organizations",method = RequestMethod.GET)
//    Response<List<OrganizationDTO>> getAllOrganization();
//
    /**
     * 根据用户id批量查询员工信息
     * @param userIds
     * @return
     */
    @RequestMapping(value = "/emp/v1/search/userIdList",method = RequestMethod.POST)
    Response<List<EmployeeInfoDTO>> getEmployeeInfoByUserIds(@RequestBody List<Long> userIds);

    /**
     * 根据员工编号查询用户id
     * @param empNum
     * @return
     */
    @RequestMapping(value = "/emp/v1/search/empNum",method = RequestMethod.GET)
    Response<EmployeeInfoDTO> getUserIdByEmployeeNum(@RequestParam("empNum") String empNum);
}
