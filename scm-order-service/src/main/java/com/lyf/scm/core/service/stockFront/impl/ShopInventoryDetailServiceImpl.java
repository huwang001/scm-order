package com.lyf.scm.core.service.stockFront.impl;

import com.lyf.scm.core.domain.entity.stockFront.ShopInventoryDetailE;
import com.lyf.scm.core.domain.entity.stockFront.ShopInventoryE;
import com.lyf.scm.core.mapper.stockFront.ShopInventoryDetailMapper;
import com.lyf.scm.core.service.stockFront.ShopInventoryDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhanlong
 */
@Slf4j
@Service("shopInventoryDetailService")
public class ShopInventoryDetailServiceImpl implements ShopInventoryDetailService {

    @Resource
    private ShopInventoryDetailMapper saveShopInventoryRecordDetails;

    @Override
    public boolean saveShopInventoryDetail(ShopInventoryE frontRecordE) {
        List<ShopInventoryDetailE> shopInventoryDetailEList = frontRecordE.getFrontRecordDetails();
        //盘点详情关联主数据
        shopInventoryDetailEList.forEach(detail -> {
            detail.setFrontRecordId(frontRecordE.getId());
            detail.setRecordCode(frontRecordE.getRecordCode());
        });
        //插入盘点单据详情
        saveShopInventoryRecordDetails.insertShopInventoryDetails(shopInventoryDetailEList);
        return Boolean.TRUE;
    }
}
