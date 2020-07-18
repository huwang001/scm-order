package com.lyf.scm.core.remote.pack.facade;

import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.core.remote.pack.PackRemoteService;
import com.lyf.scm.core.remote.pack.dto.CancelRequireDTO;
import com.lyf.scm.core.remote.pack.dto.ObtainOrderDTO;
import com.rome.arch.core.clientobject.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Desc:包装系统对外接口
 * @author:Huangyl
 * @date: 2020/7/7
 */
@Slf4j
@Component
@AllArgsConstructor
public class PackFacade {

    @Resource
    private PackRemoteService packRemoteService;

    /**
     * 需求单下发包装系统
     *
     * @param obtainOrderDTO
     */
    public void releasePackSystem(ObtainOrderDTO obtainOrderDTO) {
        Response response = packRemoteService.releasePackSystem(obtainOrderDTO);
        ResponseValidateUtils.validResponse(response);
    }

    /**
     * 包装系统 取消需求单
     *
     * @param requireCodeList
     * @return
     */
    public List<CancelRequireDTO> batchCancelRequire(List<String> requireCodeList) {
        Response<List<CancelRequireDTO>> response = packRemoteService.cancelRequireOrder(requireCodeList);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }
}
