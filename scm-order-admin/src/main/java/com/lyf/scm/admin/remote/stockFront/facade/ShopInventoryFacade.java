package com.lyf.scm.admin.remote.stockFront.facade;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.remote.dto.ShopInventoryDetailDTO;
import com.lyf.scm.admin.remote.dto.ShopInventoryPageDTO;
import com.lyf.scm.admin.remote.stockFront.ShopInventoryRemoteService;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ShopInventoryFacade {

    @Autowired
    private ShopInventoryRemoteService shopInventoryRemoteService;

    /**
     * 查询门店盘点列表
     *
     * @param frontRecord
     * @return
     */
    public PageInfo<ShopInventoryPageDTO> queryShopInventoryList(ShopInventoryPageDTO frontRecord) {
        Response<PageInfo<ShopInventoryPageDTO>> response = shopInventoryRemoteService.queryShopInventoryList(frontRecord);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 查询门店详情列表
     *
     * @param frontRecordId
     * @return
     */
    public List<ShopInventoryDetailDTO> queryShopInventoryDetailList(Long frontRecordId) {
        Response<List<ShopInventoryDetailDTO>> response = shopInventoryRemoteService.queryShopInventoryDetailList(frontRecordId);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }
}
