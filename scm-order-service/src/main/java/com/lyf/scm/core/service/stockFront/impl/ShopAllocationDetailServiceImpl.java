package com.lyf.scm.core.service.stockFront.impl;

import com.lyf.scm.core.domain.entity.stockFront.ShopAllocationDetailE;
import com.lyf.scm.core.domain.entity.stockFront.ShopAllocationE;
import com.lyf.scm.core.mapper.stockFront.ShopAllocationDetailMapper;
import com.lyf.scm.core.service.stockFront.ShopAllocationDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zys
 * @Description
 * @date 2020/6/15 17:49
 * @Version
 */
@Service
public class ShopAllocationDetailServiceImpl implements ShopAllocationDetailService {

    @Resource
    private ShopAllocationDetailMapper shopAllocationDetailMapper;

    @Override
    public boolean saveAllocationDetail(ShopAllocationE frontRecordE) {
        List<ShopAllocationDetailE> frontRecordDetailList = frontRecordE.getFrontRecordDetails();
        //调拨详情关联主数据
        frontRecordDetailList.forEach(detail -> {
            detail.setFrontRecordId(frontRecordE.getId());
            detail.setRecordCode(frontRecordE.getRecordCode());
        });
        //插入调拨单据详情
        shopAllocationDetailMapper.saveShopAllocationRecordDetails(frontRecordDetailList);
        return true;
    }
}
