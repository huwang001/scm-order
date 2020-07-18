package com.lyf.scm.core.remote.cmp.facade;


import com.alibaba.fastjson.JSON;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.remote.cmp.CmpRemoteService;
import com.lyf.scm.core.remote.cmp.dto.ShopAllocationRecordDTO;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description cmp下发数据
 * @date 2020/6/17 14:27
 * @Version
 */
@Slf4j
@Component
public class CmpFacade {

    @Autowired
    private CmpRemoteService cmpRemoteService;
    /**
     * 门店调拨下发cmp
     * @param inRecordE
     * @return
     */
    public void shopAllocationRecordsPushCmp(WarehouseRecordE inRecordE , WarehouseRecordE outRecordE){
        List<WarehouseRecordDetailE> inDetails = inRecordE.getWarehouseRecordDetailList();
        List<WarehouseRecordDetailE> outDetails = outRecordE.getWarehouseRecordDetailList();
        List<ShopAllocationRecordDTO> recordList = new ArrayList<>();
        for(WarehouseRecordDetailE detail : inDetails){
            ShopAllocationRecordDTO record = new ShopAllocationRecordDTO();
            record.setOutCreateTime(inRecordE.getOutCreateTime());
            record.setRecordCode(inRecordE.getFrontRecordCode());
            record.setShopCode(inRecordE.getShopCode());
            record.setSkuCode(detail.getSkuCode());
            record.setSkuQty(detail.getActualQty());
            record.setDetailId(detail.getId());
            record.setUnitCode(detail.getUnitCode());
            recordList.add(record);
        }
        for(WarehouseRecordDetailE detail : outDetails){
            ShopAllocationRecordDTO record = new ShopAllocationRecordDTO();
            record.setOutCreateTime(outRecordE.getOutCreateTime());
            record.setRecordCode(outRecordE.getFrontRecordCode());
            record.setShopCode(outRecordE.getShopCode());
            record.setSkuCode(detail.getSkuCode());
            record.setSkuQty(detail.getActualQty().multiply(new BigDecimal(-1.0)));
            record.setDetailId(detail.getId());
            record.setUnitCode(detail.getUnitCode());
            recordList.add(record);
        }
        log.info("调cmp入参：{}", JSON.toJSONString(recordList));
        Response response = cmpRemoteService.shopAllocationPushCMP(recordList);
        log.info("调cmp出参：{}", JSON.toJSONString(response));
        ResponseValidateUtils.validResponse(response);

    }
}
