package com.lyf.scm.core.service.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.api.dto.stockFront.ShopAllocationDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopAllocationRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopAllocationRecordPageDTO;

import java.util.List;

/**
 * @Description 门店调拨
 * @date 2020/6/15
 * @Version
 */
public interface ShopAllocationService {

    /**
     * 创建门店调拨单
     * @param frontRecord
     */
    ShopAllocationRecordDTO addShopAllocationRecord(ShopAllocationRecordDTO frontRecord);

    /**
     * 查询门店调拨单列表
     * @param frontRecord
     * @return
     */
    PageInfo<ShopAllocationRecordPageDTO> queryShopAllocationList(ShopAllocationRecordPageDTO frontRecord);

    /**
     * 根据id获取门店调拨单详情列表
     * @param frontRecordId
     * @return
     */
    List<ShopAllocationDetailDTO> queryShopAllocationDetailList(Long frontRecordId);

    /**
     * 根据出入库单据编号查询门店调拨单
     * @param recordCode
     * @return
     */
    ShopAllocationRecordDTO queryAllocationByRecordCode(String recordCode);

    /**
     * 推送CMP调拨单
     */
    void handleShopAllocationRecordsPushCmp();

    /**
     * 修改走sap还是走商品中心的开关
     * @param checkStatus
     * @return
     */
    boolean setCheckStatus(boolean checkStatus);
}
