package com.lyf.scm.core.remote.stock.facade;

import com.alibaba.fastjson.JSON;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.core.api.dto.reverse.ReceiverRecordDTO;
import com.lyf.scm.core.remote.stock.StockRemoteService;
import com.lyf.scm.core.remote.stock.dto.*;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * com.lyf.scm.core.remote.stock.facade
 *
 * @author zhangxu
 * @date 2020/4/15
 */
@Slf4j
@Component
@AllArgsConstructor
public class StockFacade {

	private final StockRemoteService stockRemoteService;

    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
    	ReservationDTO reservation = new ReservationDTO();
    	if(reservationDTO != null) {
			Response<ReservationDTO> resp = stockRemoteService.createReservation(reservationDTO);
			ResponseValidateUtils.validResponse(resp);
			reservation = resp.getData();
    	}
    	return reservation;
    }

    public ReservationDTO lockReservationByRecordCode(String recoreCode) {
    	ReservationDTO reservationDTO = null;
    	if(StringUtils.isNotBlank(recoreCode)) {
			Response<ReservationDTO> resp = stockRemoteService.lockReservationByRecordCode(recoreCode);
			ResponseValidateUtils.validResponse(resp);
			reservationDTO = resp.getData();
    	}
    	return reservationDTO;
    }

    public Response cancelOrder(String orderCode) {
    	if(StringUtils.isBlank(orderCode)) {
    		throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
    	}
		Response resp = stockRemoteService.cancelReservation(orderCode);
		ResponseValidateUtils.validResponse(resp);
		return resp;
    }

    /**
     * @param dto
     * @return
     * @Description: 保存虚仓调拨 <br>
     * @Author chuwenchao 2020/4/13
     */
    public String saveVirtualWarehouseMoveRecord(VwMoveRecordDTO dto) {
    	String vmMoveCode = null;
    	if(dto != null) {
			log.info("保存虚仓调拨调库存接口:参数为=>{}", JSON.toJSONString(dto));
			Response<Object> resp = stockRemoteService.saveVirtualWarehouseMoveRecord(dto);
			log.info("保存虚仓调拨调库存接口:返回值为=>{}", JSON.toJSONString(resp));
			ResponseValidateUtils.validResponse(resp);
			vmMoveCode = (String) resp.getData();
    	}
        return vmMoveCode;
    }
    
    /**
     * 根据预约单号释放预约单锁定库存
     * 
     * @param orderCode
     */
	public void unlockOrderStock(String orderCode) {
		if(StringUtils.isBlank(orderCode)) {
			throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
		}
		 Response resp = stockRemoteService.unlockOrderStock(orderCode);
         ResponseValidateUtils.validResponse(resp);
	}

}