package com.lyf.scm.admin.remote.facade;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.disparity.DisparityRecordDetailDTO;
import com.lyf.scm.admin.dto.disparity.DisparityRecordDetail;
import com.lyf.scm.admin.dto.disparity.DisparityRecordParamDTO;
import com.lyf.scm.admin.remote.DisparityRecordRemoteService;
import com.lyf.scm.admin.remote.dto.BatchRefusedBackDTO;
import com.lyf.scm.admin.remote.dto.EmployeeInfoDTO;
import com.lyf.scm.common.util.validate.ParamValidator;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.user.common.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DisparityRecordFacade {

    @Resource
    private DisparityRecordRemoteService disparityRecordRemoteService;

    @Resource
    private UserCoreFacade userCoreFacade;
    private ParamValidator validator = ParamValidator.INSTANCE;

    public PageInfo queryByCondition(DisparityRecordParamDTO paramDTO) {
        Response<PageInfo<DisparityRecordDetailDTO>> response = disparityRecordRemoteService.queryByCondition(paramDTO);
        if (!validator.validResponse(response)) {
            throw new RomeException(response.getCode(), response.getMsg());
        }
        PageInfo<DisparityRecordDetailDTO> res = response.getData();
        //根据员工id查询员工账号
        List<DisparityRecordDetailDTO> list = res.getList();
        int maxSize = 200;
        if (list != null && list.size() > 0) {
            Set<Long> modifierIdSet = new HashSet<>();
            for(DisparityRecordDetailDTO detail : list){
                if(detail.getModifier()!= null  && !detail.getModifier().equals(0L)){
                    modifierIdSet.add(detail.getModifier());
                }
                if(detail.getCreator()!= null  && !detail.getCreator().equals(0L)){
                    modifierIdSet.add(detail.getCreator());
                }
            }
            List <Long> modifierIds = new ArrayList<>(modifierIdSet);
            List<EmployeeInfoDTO> employeeInfos = new ArrayList<>();
            int count = modifierIds.size() / maxSize ;
            if (modifierIds.size() % maxSize > 0) {
                count = count + 1;
            }
            //根据员工id查询员工信息入参最大为200,需要分段查询
            for (int i = 0; i < count; i++) {
                int size = maxSize > modifierIds.size() ? modifierIds.size(): maxSize;
                employeeInfos.addAll(userCoreFacade.getEmployeeInfoByUserIds(modifierIds.subList(0, size)));
                modifierIds.subList(0, size).clear();
            }
            Map<Long, EmployeeInfoDTO> employeeInfosMap = employeeInfos.stream().collect(Collectors.toMap(
                    EmployeeInfoDTO::getId,
                    item -> item
            ));
            for (DisparityRecordDetailDTO detail : list) {
                if (employeeInfosMap.containsKey(detail.getModifier())) {
                    detail.setEmployeeNumber(employeeInfosMap.get(detail.getModifier()).getEmployeeNumber());
                } else {
                    detail.setEmployeeNumber("0");
                }
                if (employeeInfosMap.containsKey(detail.getCreator())) {
                    detail.setCreateEmployeeNumber(employeeInfosMap.get(detail.getCreator()).getEmployeeNumber());
                } else {
                    detail.setCreateEmployeeNumber("0");
                }
            }
        }
        return res;
    }


    public void  disparityDuty(List<DisparityRecordDetail> details) {
        Long userId = UserContext.getUserId();
        if (!CollectionUtils.isEmpty(details) && userId != null) {
            details.forEach(dto -> {
                dto.setModifier(userId);
            });
        }
        Response response = disparityRecordRemoteService.disparityDuty(details);
        if (! validator.validResponse(response)) {
            throw new RomeException(response.getCode(),response.getMsg());
        }
    }

    public void  handlerDisparity(List<Long> ids) {
        Long userId = UserContext.getUserId();
        Response response = disparityRecordRemoteService.handlerDisparity(ids,userId);
        if (! validator.validResponse(response)) {
            throw new RomeException(response.getCode(),response.getMsg());
        }
    }

    
    /**
     * @Description 批量拒收
     * @Author Lin.Xu
     * @Date 14:41 2020/7/17
     * @Param [sapRecordCodes]
     * @return java.lang.String
     **/
    public String rejectConfirmDisparityBySapCode(String sapRecordCodes) {
        Long userId = UserContext.getUserId();
        //忽略换行符
        String replaceStr = sapRecordCodes.replaceAll("\\n|\\r", ",");
        String[] split = replaceStr.split(",");
        List<String> sapDeliveryCodes = Arrays.asList(split);
        sapDeliveryCodes = sapDeliveryCodes.stream().distinct().collect(Collectors.toList());
        if (sapDeliveryCodes.size() > 100) {
            throw new RomeException("2", "参数异常：批处理单号不能超过100");
        }
        //发送处理请求
        Response<List<BatchRefusedBackDTO>> dealBackResp = disparityRecordRemoteService.rejectConfirmDisparityBySapCode(sapDeliveryCodes, userId);
        if(!validator.validResponse(dealBackResp)) {
        	throw new RomeException("5", "系统异常：批量拒单调用接口返回：" + (null == dealBackResp ? "空":dealBackResp.getMsg()));
        }
        //凭借返回信息
        StringBuffer resBuf = new StringBuffer("处理结果：<br/>");
        for(BatchRefusedBackDTO brbDto : dealBackResp.getData()) {
        	resBuf.append("  ->")
        		.append(brbDto.getRecordCode())
        		.append(":")
        		.append(brbDto.getStatus())
        		.append("<br/>");
        }
        return resBuf.toString();
    }


}
