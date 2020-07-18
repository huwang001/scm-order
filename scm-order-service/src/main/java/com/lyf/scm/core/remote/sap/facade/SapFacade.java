package com.lyf.scm.core.remote.sap.facade;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.util.date.DateUtil;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.core.api.dto.stockFront.InventoryDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopInventoryDTO;
import com.lyf.scm.core.config.RestClient;
import com.lyf.scm.core.domain.entity.common.CallRecordLogE;
import com.lyf.scm.core.domain.entity.stockFront.ShopInventoryDetailE;
import com.lyf.scm.core.domain.entity.stockFront.ShopInventoryE;
import com.lyf.scm.core.remote.log.CallLogEvent;
import com.lyf.scm.core.remote.sap.SapRemoteService;
import com.lyf.scm.core.remote.sap.dto.SyncSapPoDTO;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * com.lyf.scm.core.remote.trade.facade
 *
 * @author zhangxu
 * @date 2020/4/13
 */
@Slf4j
@Component
@AllArgsConstructor
public class SapFacade {

    private final RestClient restClient;

    private final ApplicationEventPublisher publisher;

    @Autowired
    private SapRemoteService sapRemoteService;

    /**
     * 下发PO至SAP
     *
     * @param recordCode
     * @param list
     * @return
     */
    public Response syncPoRecordToSap(String recordCode, List<SyncSapPoDTO> list) {
        String uri = "/RESTAdapter/BS_MID/CreatePo";
        CallRecordLogE callRecordLog = new CallRecordLogE();
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(list));
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("items", jsonArray);
        try {
            Response resp = restClient.postSap(uri, jsonParam, new TypeReference<Response>() {
            });
            callRecordLog.setResponseContent(JSON.toJSONString(resp));
            if (resp == null || !"S".equals(resp.getCode())) {
				throw new RomeException(ResCode.ORDER_ERROR_6017,
						ResCode.ORDER_ERROR_6017_DESC + ":wmsRecordCode=" + recordCode);
			}
            callRecordLog.setStatus(1);
            return resp;
        } catch (Exception e) {
            callRecordLog.setStatus(0);
            log.error("调用SAP接口异常，参数：{}，异常：{}", JSON.toJSONString(jsonParam), e);
            throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        } finally {
            callRecordLog.setRequestUrl(uri);
            callRecordLog.setRecordCode(recordCode);
            callRecordLog.setRequestContent(JSON.toJSONString(list));
            publisher.publishEvent(new CallLogEvent(callRecordLog));
        }
    }


    /**
     * 批量门店盘点数据推送SAP
     *
     * @param recordList
     */
    public void transferInventorys(List<ShopInventoryE> recordList) {
        List<ShopInventoryDTO> list = new ArrayList<>();
        recordList.forEach(frontRecord -> {
            ShopInventoryDTO shopInventoryRecordDTO = new ShopInventoryDTO();
            shopInventoryRecordDTO.setBusinessType(frontRecord.getBusinessType());
            shopInventoryRecordDTO.setCreateTime(new Date().getTime());
            shopInventoryRecordDTO.setOutCreateTime(frontRecord.getOutCreateTime().getTime());
            shopInventoryRecordDTO.setShopCode(frontRecord.getShopCode());
            List<ShopInventoryDetailE> frontRecordDetails = frontRecord.getFrontRecordDetails();
            List<InventoryDetailDTO> frontRecordDetailList = new ArrayList<>();
            for (ShopInventoryDetailE detail : frontRecordDetails) {
                InventoryDetailDTO detailDTO = new InventoryDetailDTO();
                if (frontRecord.getBusinessType() == 9) {
                    detailDTO.setSkuQty(detail.getAccQty());
                } else {
                    detailDTO.setSkuQty(detail.getSkuQty());
                }
                detailDTO.setSkuCode(detail.getSkuCode());
                detailDTO.setUnitCode(detail.getUnitCode());
                frontRecordDetailList.add(detailDTO);
            }
            shopInventoryRecordDTO.setFrontRecordDetails(frontRecordDetailList);
            list.add(shopInventoryRecordDTO);
        });
        if (list.size() > 0) {
            try {
                log.info("门店盘点-推SAP数据：{}", JSON.toJSONString(list));
                Response<Boolean> response = sapRemoteService.pushShopInventoryList(list);
                log.info("门店盘点-推SAP响应：{}", JSON.toJSONString(response));
                ResponseValidateUtils.validResponse(response);
                return;
            } catch (Exception e) {
                log.info("调用SAP接口异常，参数：{}，异常：{}", JSON.toJSONString(list), e);
                throw new RomeException(ResCode.STOCK_ERROR_7203, ResCode.STOCK_ERROR_7203_DESC);
            }
        }
    }
}