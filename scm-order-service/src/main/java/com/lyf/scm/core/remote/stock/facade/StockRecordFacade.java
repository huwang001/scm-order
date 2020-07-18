package com.lyf.scm.core.remote.stock.facade;

import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.CheckVirWarehouseCodeEnum;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.core.api.dto.notify.DispatchNoticeDTO;
import com.lyf.scm.core.domain.entity.common.CallRecordLogE;
import com.lyf.scm.core.remote.log.CallLogEvent;
import com.lyf.scm.core.remote.stock.StockRecordRemoteService;
import com.lyf.scm.core.remote.stock.dto.CancelRecordDTO;
import com.lyf.scm.core.remote.stock.dto.CancelResultDTO;
import com.lyf.scm.core.remote.stock.dto.InWarehouseRecordDTO;
import com.lyf.scm.core.remote.stock.dto.OutWarehouseRecordDTO;
import com.lyf.scm.core.remote.stock.dto.UpdateTmsCodeDTO;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 库存 创建出入库单 接口
 */
@Slf4j
@Component
@AllArgsConstructor
public class StockRecordFacade {

    private final ApplicationEventPublisher publisher;

    @Resource
    private StockRecordRemoteService stockRecordRemoteService;

    /**
     * 创建入库单
     */
    public Boolean createInRecord(InWarehouseRecordDTO inRecordDto) {
        if (null != inRecordDto) {
        	log.info("创建入库单，单号：{}，入参：{}", inRecordDto.getRecordCode(), JSON.toJSONString(inRecordDto));
        	//校验出库单虚仓编码是否可设置
        	CheckVirWarehouseCodeEnum.checkVirWarehouseCode(inRecordDto.getVirWarehouseCode(), inRecordDto.getRecordType());
            Response<String> resp = stockRecordRemoteService.createInRecord(inRecordDto);
            log.info("创建入库单，单号：{}，出参：{}", inRecordDto.getRecordCode(), JSON.toJSONString(resp));
            ResponseValidateUtils.validResponse(resp);
            return true;
        }
        return false;
    }

    /**
     * 创建出库单
     */
    public Boolean createOutRecord(OutWarehouseRecordDTO outRecordDto) {
        if (null != outRecordDto) {
        	//校验出库单虚仓编码是否可设置
            log.info("创建出库单，单号：{}，入参：{}", outRecordDto.getRecordCode(), JSON.toJSONString(outRecordDto));
            CheckVirWarehouseCodeEnum.checkVirWarehouseCode(outRecordDto.getVirWarehouseCode(), outRecordDto.getRecordType());
            Response<String> resp = stockRecordRemoteService.createOutRecord(outRecordDto);
            log.info("创建出库单，单号：{}，出参：{}", outRecordDto.getRecordCode(), JSON.toJSONString(resp));
            ResponseValidateUtils.validResponse(resp);
            return true;
            }
        return false;
    }

    /**
     * 根据出入库单据编号、是否强制取消、出入库单据类型取消出入库单
     *
     * @param cancelRecordList
     * @return
     */
    public List<CancelResultDTO> cancelRecord(List<CancelRecordDTO> cancelRecordList) {
        List<CancelResultDTO> cancelResultList = null;
        if (CollectionUtils.isEmpty(cancelRecordList)) {
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        String uri = "/stock-inner-app/stock/v1/warehouseRecord/batchCancel";
        CallRecordLogE callRecordLog = new CallRecordLogE();
        try {
            log.info("取消出入库单，入参：{}", JSON.toJSONString(cancelRecordList));
            Response<List<CancelResultDTO>> resp = stockRecordRemoteService.cancelRecord(cancelRecordList);
            log.info("取消出入库单，出参：{}", JSON.toJSONString(resp));
            callRecordLog.setResponseContent(JSON.toJSONString(resp));
            ResponseValidateUtils.validResponse(resp);
            cancelResultList = resp.getData();
            callRecordLog.setStatus(1);
        } catch (Exception e) {
            callRecordLog.setStatus(0);
            log.info("调用库存中心接口异常，参数：{}，异常：{}", JSON.toJSONString(cancelRecordList), e);
            throw e;
        } finally {
            callRecordLog.setRequestUrl(uri);
            callRecordLog.setRequestContent(JSON.toJSONString(cancelRecordList));
            publisher.publishEvent(new CallLogEvent(callRecordLog));
        }
        return cancelResultList;
    }

    /**
     * 取消 出入库单接口
     *
     * @param cancelRecord
     * @return
     */
    public CancelResultDTO cancelSingleRecord(CancelRecordDTO cancelRecord) {
        CancelResultDTO cancelResult = new CancelResultDTO();
        if (null == cancelRecord) {
            cancelResult.setStatus(false);
            return cancelResult;
        }
        List<CancelResultDTO> resultList = cancelRecord(Lists.newArrayList(cancelRecord));
        if (null != resultList && resultList.size() > 0) {
            cancelResult = resultList.get(0);
        }
        return cancelResult;
    }

    /**
     * 创建仓库调拨单派车通知
     *
     * @param dispatchNoticeDTOList
     * @return
     */
    public List<CancelResultDTO> batchDispatchingNotify(List<DispatchNoticeDTO> dispatchNoticeDTOList) {
        if (CollectionUtils.isEmpty(dispatchNoticeDTOList)) {
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        Response<List<CancelResultDTO>> response = stockRecordRemoteService.batchDispatchingNotify(dispatchNoticeDTOList);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 批量更新派车单号
     *
     * @param updateTmsCodeDTO
     * @return
     */
    public List<CancelResultDTO> updateWarehouseSaleTobTmsOrder(UpdateTmsCodeDTO updateTmsCodeDTO) {
        List<CancelResultDTO> cancelResultList = null;
        if (null == updateTmsCodeDTO) {
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        String uri = "/stock-inner-app/stock/v1/warehouseRecord/updateWarehouseSaleTobTmsOrder";
        CallRecordLogE callRecordLog = new CallRecordLogE();
        try {
            log.info("批量更新派车单号，入参：{}", JSON.toJSONString(updateTmsCodeDTO));
            Response<List<CancelResultDTO>> resp = stockRecordRemoteService.updateWarehouseSaleTobTmsOrder(updateTmsCodeDTO);
            log.info("批量更新派车单号，出参：{}", JSON.toJSONString(resp));
            callRecordLog.setResponseContent(JSON.toJSONString(resp));
            ResponseValidateUtils.validResponse(resp);
            cancelResultList = resp.getData();
            callRecordLog.setStatus(1);
        } catch (Exception e) {
            callRecordLog.setStatus(0);
            log.info("调用库存中心接口异常，参数：{}，响应：{}", JSON.toJSONString(updateTmsCodeDTO), e);
            throw e;
        } finally {
            callRecordLog.setRequestUrl(uri);
            callRecordLog.setRequestContent(JSON.toJSONString(updateTmsCodeDTO));
            publisher.publishEvent(new CallLogEvent(callRecordLog));
        }
        return cancelResultList;
    }
    
}