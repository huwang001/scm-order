package com.lyf.scm.core.remote.stock.facade;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.core.api.dto.stockFront.RealWarehouseParamDTO;
import com.lyf.scm.core.domain.entity.common.CallRecordLogE;
import com.lyf.scm.core.remote.log.CallLogEvent;
import com.lyf.scm.core.remote.stock.StockWhAllocationRemoteService;
import com.lyf.scm.core.remote.stock.dto.*;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 仓库调拨单
 * @author WWH
 *
 */
@Slf4j
@Component
@AllArgsConstructor
public class StockWhAllocationFacade {

    private final int PAGE_SIZE = 100;
    private final ApplicationEventPublisher publisher;
    
    @Resource
    private StockWhAllocationRemoteService stockWhAllocationRemoteService;
    
	/**
	 * 根据skuId集合、实仓编码、工厂编码、是否质量问题调拨查询实仓库存列表
	 *
	 * @param queryRealWarehouseStockDTO
	 * @return
	 */
	public List<RealWarehouseStockDTO> listStockBySkuWhIdForWhAllot(QueryRealWarehouseStockDTO queryRealWarehouseStockDTO) {
		List<RealWarehouseStockDTO> realWarehouseStockList = new ArrayList<RealWarehouseStockDTO>();
		if (queryRealWarehouseStockDTO != null) {
			// 集合分割处理
			List<List<Long>> partList = ListUtils.partition(queryRealWarehouseStockDTO.getSkuIds(), PAGE_SIZE);
			Response<List<RealWarehouseStockDTO>> resp;
			for (int i = 0; i < partList.size(); i++) {
				List<Long> subList = partList.get(i);
				queryRealWarehouseStockDTO.setSkuIds(subList);
				resp = stockWhAllocationRemoteService.listStockBySkuWhIdForWhAllot(queryRealWarehouseStockDTO);
				ResponseValidateUtils.validResponse(resp);
				realWarehouseStockList.addAll(resp.getData());
			}
		}
		return realWarehouseStockList;
	}

	/**
	 * 根据条件查询实仓库存列表
	 *
	 * @param realWarehouseStockDTO
	 * @return
	 */
	public PageInfo<RealWarehouseStockDTO> queryStockByWhIdForWhAllot(RealWarehouseStockDTO realWarehouseStockDTO) {
		PageInfo<RealWarehouseStockDTO> pageInfo = new PageInfo<RealWarehouseStockDTO>();
		if (realWarehouseStockDTO != null) {
			Response<PageInfo<RealWarehouseStockDTO>> resp = stockWhAllocationRemoteService.queryStockByWhIdForWhAllot(realWarehouseStockDTO);
			ResponseValidateUtils.validResponse(resp);
			pageInfo = resp.getData();
		}
		return pageInfo;
	}
	
	/**
	 * 根据条件查询实仓信息列表-运营平台查询接口
	 * 
	 * @param paramDTO
	 * @return
	 */
	public List<RealWarehouse> queryForAdmin(RealWarehouseParamDTO paramDTO) {
		List<RealWarehouse> realWarehouseList = new ArrayList<RealWarehouse>();
		if (paramDTO != null) {
			Response<List<RealWarehouse>> resp = stockWhAllocationRemoteService.queryForAdmin(paramDTO);
			ResponseValidateUtils.validResponse(resp);
			realWarehouseList = resp.getData();
		}
		return realWarehouseList;
	}
	
	/**
	 * 同步sapPoNo给库存中心
	 * 
	 * @param poNoList
	 * @return
	 */
	public Response updateLineNoAndSapPoNo(List<PoNoDTO> poNoList) {
		if (CollectionUtils.isEmpty(poNoList)) {
			throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
		}
		String uri = "/stock-inner-app/stock/wh_allocation/updateLineNoAndSapPoNo";
		CallRecordLogE callRecordLog = new CallRecordLogE();
		try {
			Response resp = stockWhAllocationRemoteService.updateLineNoAndSapPoNo(poNoList);
			callRecordLog.setResponseContent(JSON.toJSONString(resp));
			ResponseValidateUtils.validResponse(resp);
			callRecordLog.setStatus(1);
			return resp;
		} catch (Exception e) {
			callRecordLog.setStatus(0);
			log.error("调用库存中心接口异常，参数：{}，响应：{}", JSON.toJSONString(poNoList), e);
			throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		} finally {
			callRecordLog.setRecordCode(poNoList.get(0).getRecordCode());
			callRecordLog.setRequestUrl(uri);
			callRecordLog.setRequestContent(JSON.toJSONString(poNoList));
			publisher.publishEvent(new CallLogEvent(callRecordLog));
		}
	}

}