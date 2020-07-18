package com.lyf.scm.admin.remote.stockFront.facade;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.shop.ShopAdjustRecordDTO;
import com.lyf.scm.admin.remote.stockFront.ShopAdjustRemoteService;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 门店  调整单
 */
@Component
@Slf4j
public class ShopAdjustFacade {

    @Autowired
    private ShopAdjustRemoteService shopAdjustRemoteService;

    /**
     * 查询门店  试吃  调整单
     */
    public PageInfo<ShopAdjustRecordDTO> findShopForetasteCondition(ShopAdjustRecordDTO adjustRecordDTO, int pageNum, int pageSize) {
        Response<PageInfo<ShopAdjustRecordDTO>> response = shopAdjustRemoteService.findShopForetasteCondition(adjustRecordDTO, pageNum, pageSize);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 查询门店试吃调整单 明细
     */
    public ShopAdjustRecordDTO getAdjustForetasteByRecordId(Long recordId) {
        Response<ShopAdjustRecordDTO> response = shopAdjustRemoteService.getAdjustForetasteByRecordId(recordId);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }
}
