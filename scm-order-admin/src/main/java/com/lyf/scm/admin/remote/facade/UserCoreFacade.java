package com.lyf.scm.admin.remote.facade;


import com.lyf.scm.admin.remote.UserCoreRemoteService;
import com.lyf.scm.admin.remote.dto.EmployeeInfoDTO;
import com.lyf.scm.admin.remote.dto.OrganizationDTO;
import com.lyf.scm.common.enums.ResponseMsg;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
public class UserCoreFacade {

    @Resource
    private UserCoreRemoteService userCoreRemoteService;

    /**
     * 根据用户id查询员工信息
     * @param userIds
     * @return
     */
    public List<EmployeeInfoDTO> getEmployeeInfoByUserIds(List<Long> userIds){
    	if(CollectionUtils.isEmpty(userIds)) {
    		 return new ArrayList<EmployeeInfoDTO>();
    	}
        Response<List<EmployeeInfoDTO>> response = userCoreRemoteService.getEmployeeInfoByUserIds(userIds);
        if (ResponseMsg.SUCCESS.getCode().equals(response.getCode())) {
            return response.getData();
        } else {
            log.error(response.getCode() + ":" + response.getMsg());
            return new ArrayList<EmployeeInfoDTO>();
        }
    }

    /**
     * 根据员工编号查询用户id
     * @param employeeNum
     * @return
     */
    public EmployeeInfoDTO getUserIdByEmployeeNum(String employeeNum){
        Response<EmployeeInfoDTO> response = userCoreRemoteService.getUserIdByEmployeeNum(employeeNum);
        return response.getData();
    }
//
//    /**
//     * 查询组织架构树
//     * @return
//     */
//    public List<OrganizationDTO> getOrganizationTree(){
//        Response<List<OrganizationDTO>> response = userCoreRemoteService.getAllOrganization();
//        return orgListToTree(response.getData());
//    }
//
//    /**
//     * 递归查询组织架构
//     * @param parentCode
//     * @return
//     */
//    List<OrganizationDTO> getOrganizationByParentCode(String parentCode){
//        Response<List<OrganizationDTO>> response = userCoreRemoteService.getOrganizationByParentCode(parentCode);
//        List<OrganizationDTO> childrens = response.getData();
//        if(null != childrens){
//            for (OrganizationDTO organizationDTO : childrens) {
//                List<OrganizationDTO> list = getOrganizationByParentCode(organizationDTO.getOrgCode());
//                if(null != list && 0 != list.size()){
//                    organizationDTO.setChildrenNodes(list);
//                }
//            }
//        }
//        return childrens;
//    }
//
//    /**
//     * 所有的组织结构拆解为树状
//     * @param dtoList
//     */
//    public List<OrganizationDTO> orgListToTree(List<OrganizationDTO> dtoList){
//        Map<String,List<OrganizationDTO>> orgListMap = new HashMap<>();
//        List<OrganizationDTO> retList = new ArrayList<>();
//
//        for (OrganizationDTO dto : dtoList) {
//            if(null != dto.getParentOrgCode()){
//                if(!orgListMap.containsKey(dto.getParentOrgCode())){
//                    List<OrganizationDTO> childList = new ArrayList<>();
//                    orgListMap.put(dto.getParentOrgCode(),childList);
//                }else {
//                    orgListMap.get(dto.getParentOrgCode()).add(dto);
//                }
//            }
//        }
//
//        for (OrganizationDTO dto : dtoList) {
//            if(orgListMap.containsKey(dto.getOrgCode()) && 0 != orgListMap.get(dto.getOrgCode()).size()){
//                dto.setChildrenNodes(orgListMap.get(dto.getOrgCode()));
//            }
//            if("2".equals(dto.getOrgType())){
//                retList.add(dto);
//            }
//        }
//
//        return retList;
//    }
}
