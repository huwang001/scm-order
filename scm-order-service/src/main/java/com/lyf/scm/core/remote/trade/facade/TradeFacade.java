package com.lyf.scm.core.remote.trade.facade;

import com.alibaba.fastjson.JSON;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.core.api.dto.orderReturn.PushReturnNoticeDTO;
import com.lyf.scm.core.config.RestClient;
import com.lyf.scm.core.domain.entity.common.CallRecordLogE;
import com.lyf.scm.core.remote.log.CallLogEvent;
import com.lyf.scm.core.remote.trade.TradeCoreRemoteService;
import com.lyf.scm.core.remote.trade.TradeRemoteService;
import com.lyf.scm.core.remote.trade.dto.DoDTO;
import com.lyf.scm.core.remote.trade.dto.PosDaySummaryCondition;
import com.lyf.scm.core.remote.trade.dto.PosDaySummaryDTO;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
public class TradeFacade {

    private final TradeRemoteService tradeRemoteService;
    private final ApplicationEventPublisher publisher;
    @Resource
    private final RestClient restClient;
    @Resource
    private TradeCoreRemoteService tradeCoreRemoteService;

    public void stockLockedNotify(String saleCode) {
        if (StringUtils.isBlank(saleCode)) {
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        String uri = "/trade-core-order-fulfillment/v1/trade/order-fulfillment/stockLockedNotify?orderNo=" + saleCode;
        CallRecordLogE callRecordLog = new CallRecordLogE();
        try {
            Response resp = tradeRemoteService.stockLockedNotify(saleCode);
            ResponseValidateUtils.validResponse(resp);
            callRecordLog.setResponseContent(JSON.toJSONString(resp));
            callRecordLog.setStatus(1);
        } catch (Exception e) {
            callRecordLog.setStatus(0);
            log.error("调用交易中心完全锁定通知接口异常，参数：{}", saleCode, e);
            throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        } finally {
            callRecordLog.setRequestUrl(uri);
            callRecordLog.setRequestContent(saleCode);
            publisher.publishEvent(new CallLogEvent(callRecordLog));
        }
    }

    public void deliverNotify(List<DoDTO> doDTOList) {
        if (CollectionUtils.isEmpty(doDTOList)) {
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        String uri = "/trade-core-order-fulfillment/v1/trade/order-fulfillment/deliverNotify";
        CallRecordLogE callRecordLog = new CallRecordLogE();
        try {
            Response resp = tradeRemoteService.deliverNotify(doDTOList);
            ResponseValidateUtils.validResponse(resp);
            callRecordLog.setResponseContent(JSON.toJSONString(resp));
            callRecordLog.setStatus(1);
        } catch (Exception e) {
            callRecordLog.setStatus(0);
            log.error("调用交易中心发货通知接口异常，参数：{}", JSON.toJSONString(doDTOList), e);
            throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        } finally {
            callRecordLog.setRequestUrl(uri);
            callRecordLog.setRequestContent(JSON.toJSONString(doDTOList));
            publisher.publishEvent(new CallLogEvent(callRecordLog));
        }
    }

    public Response reverseInBoundNotify(PushReturnNoticeDTO pushReturnNoticeDTO) {
        if (pushReturnNoticeDTO == null) {
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        String uri = "/trade-core-order-machine/v1/trade/reverse/inBound/notify";
        CallRecordLogE callRecordLog = new CallRecordLogE();
        try {
            Response resp = tradeRemoteService.reverseInBoundNotify(pushReturnNoticeDTO);
            ResponseValidateUtils.validResponse(resp);
            callRecordLog.setResponseContent(JSON.toJSONString(resp));

            callRecordLog.setStatus(1);
            return resp;
        } catch (Exception e) {
            callRecordLog.setStatus(0);
            log.error("调用交易中心接口异常，参数：{}", JSON.toJSONString(pushReturnNoticeDTO), e);
            throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        } finally {
            callRecordLog.setRequestUrl(uri);
            callRecordLog.setRequestContent(JSON.toJSONString(pushReturnNoticeDTO));
            publisher.publishEvent(new CallLogEvent(callRecordLog));
        }
    }

    /**
     * 查询日结信息
     */
    public PosDaySummaryDTO posDaySummary(String shopCode, Date date) {
        PosDaySummaryCondition posDaySummaryCondition = new PosDaySummaryCondition();
        posDaySummaryCondition.setShopCode(shopCode);
        posDaySummaryCondition.setSummaryDate(date.getTime());
        try {
            Response<PosDaySummaryDTO> resp = tradeCoreRemoteService.posDaySumary(posDaySummaryCondition);
            ResponseValidateUtils.validResponse(resp);
            return resp.getData();
        } catch (Exception e) {
            log.error("调用交易中心接口异常，参数：{}", JSON.toJSONString(posDaySummaryCondition), e);
            throw new RomeException(ResCode.ORDER_ERROR_7201, ResCode.ORDER_ERROR_7201_DESC);
        }
    }
}