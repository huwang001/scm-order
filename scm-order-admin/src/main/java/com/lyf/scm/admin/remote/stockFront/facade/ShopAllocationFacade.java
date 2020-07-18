package com.lyf.scm.admin.remote.stockFront.facade;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.remote.dto.ShopAllocationDetailDTO;
import com.lyf.scm.admin.remote.dto.ShopAllocationRecordPageDTO;
import com.lyf.scm.admin.remote.stockFront.ShopAllocationRemoteService;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description
 * @date 2020/6/18
 * @Version
 */
@Component
@Slf4j
public class ShopAllocationFacade {

    @Autowired
    private ShopAllocationRemoteService shopAllocationRemoteService;

    /**
     * 查询门店调拨单列表
     * @param frontRecord
     * @return
     */
    public PageInfo<ShopAllocationRecordPageDTO> queryShopAllocationList(ShopAllocationRecordPageDTO frontRecord){
        Response<PageInfo<ShopAllocationRecordPageDTO>> response = shopAllocationRemoteService.queryShopAllocationList(frontRecord);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 查询门店调拨详情列表
     * @param frontRecordId
     * @return
     */
    public List<ShopAllocationDetailDTO> queryShopAllocationDetailList(Long frontRecordId){
        Response<List<ShopAllocationDetailDTO>> response = shopAllocationRemoteService.queryShopAllocationDetailList(frontRecordId);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

}
