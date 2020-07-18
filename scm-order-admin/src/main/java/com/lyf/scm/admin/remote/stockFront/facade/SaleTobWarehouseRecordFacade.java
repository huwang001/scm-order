package com.lyf.scm.admin.remote.stockFront.facade;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.SaleTobWarehouseRecordCondition;
import com.lyf.scm.admin.remote.dto.EmployeeInfoDTO;
import com.lyf.scm.admin.remote.facade.UserFacade;
import com.lyf.scm.admin.remote.stockFront.SaleTobWarehouseRecordRemoteService;
import com.lyf.scm.admin.remote.stockFront.dto.SaleTobWarehouseRecordDTO;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SaleTobWarehouseRecordFacade {
	
	@Resource
	private SaleTobWarehouseRecordRemoteService saleTobWarehouseRecordRemoteService;
	
    @Resource
    private UserFacade userFacade;
    
	public PageInfo<SaleTobWarehouseRecordDTO> queryByCondition(SaleTobWarehouseRecordCondition condition){
		Response<PageInfo<SaleTobWarehouseRecordDTO>> response = saleTobWarehouseRecordRemoteService.queryByCondition(condition);
		ResponseValidateUtils.validResponse(response);
		PageInfo<SaleTobWarehouseRecordDTO> pageInfo = response.getData();
        List<SaleTobWarehouseRecordDTO> list = pageInfo.getList() == null? new ArrayList<>() : pageInfo.getList();
        List<Long> modifiers = list.stream().filter(v -> v.getModifier() != null).map(SaleTobWarehouseRecordDTO :: getModifier).distinct().collect(Collectors.toList());
		//根据modifiers获取员工信息
        List<EmployeeInfoDTO> employeeInfoDTOList = userFacade.getEmployeeInfoByUserIds(modifiers);
		Map<Long, EmployeeInfoDTO> employeeInfoDTOMap = employeeInfoDTOList.stream().collect(Collectors.toMap(EmployeeInfoDTO :: getId, Function.identity(), (v1, v2) -> v1));
		this.setValue(list,employeeInfoDTOMap);
		return response.getData();
	}

	private void setValue(List<SaleTobWarehouseRecordDTO> list,Map<Long, EmployeeInfoDTO> employeeInfoDTOMap){
		list.forEach(item -> {
			if(employeeInfoDTOMap.containsKey(item.getModifier())) {
				String modifyEmpNum = employeeInfoDTOMap.get(item.getModifier()).getEmployeeNumber();
				if(StringUtils.isNotBlank(modifyEmpNum)) {
					item.setModifyEmpNum(modifyEmpNum);
				} else {
					item.setModifyEmpNum(item.getModifier() == null? null : String.valueOf(item.getModifier()));
				}
			} else {
				item.setModifyEmpNum(item.getModifier() == null? null : String.valueOf(item.getModifier()));
			}
		});
	}

    public List<Map<String,String>> getShopInfo() {
		Response<List<Map<String,String>>> response = saleTobWarehouseRecordRemoteService.getShopInfo();
		ResponseValidateUtils.validResponse(response);
		return response.getData();
    }

    public SaleTobWarehouseRecordDTO getWarehouseSaleTobDetail(Long warehouseRecordId) {
		Response<SaleTobWarehouseRecordDTO> response = saleTobWarehouseRecordRemoteService.getWarehouseSaleTobDetail(warehouseRecordId);
		ResponseValidateUtils.validResponse(response);
		return response.getData();
	}


}
