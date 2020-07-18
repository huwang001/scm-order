package com.lyf.scm.core.service.disparity.impl;

import com.lyf.scm.common.constants.WarehouseRecordConstant;
import com.lyf.scm.common.enums.*;
import com.lyf.scm.core.domain.entity.disparity.DisparityDetailE;
import com.lyf.scm.core.domain.entity.disparity.DisparityRecordE;
import com.lyf.scm.core.domain.entity.stockFront.FrontWarehouseRecordRelationE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.domain.model.disparity.DisparityDetailDO;
import com.lyf.scm.core.domain.model.disparity.DisparityRecordDO;
import com.lyf.scm.core.remote.stock.dto.InWarehouseRecordDTO;
import com.lyf.scm.core.remote.stock.dto.OutWarehouseRecordDTO;
import com.lyf.scm.core.remote.stock.dto.RecordDetailDTO;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**  
 * @ClassName: DisparityBeanUtil  
 * @Description: 差异订单对象构建工具  
 * @author: Lin.Xu  
 * @date: 2020-7-11 10:56:37
 * @version: v1.0
 */
public class DisparityBeanUtil {
	
	/**
	 * @Method: createdisparityRecordDO  
	 * @Description: 构建差异单记录
	 * @param orderNo 订单编号
	 * @param putInRecords 入库订单
	 * @param outRecords 出库订单
	 * @param frontRecord 前置订单随机
	 * @param frtype 前置订单类型
	 * @author: Lin.Xu 
	 * @date: 2020-7-11 16:37:11 
	 * @return: disparityRecordDO
	 * @throws
	 */
	public static DisparityRecordDO createDisparityRecordDO(String orderNo, WarehouseRecordE putInRecords,
			WarehouseRecordE outRecords, FrontWarehouseRecordRelationE frontRecord, FrontRecordTypeEnum frtype) {
		if(StringUtils.isEmpty(orderNo) || null == putInRecords || null == outRecords || null == frontRecord) {
			return null;
		}
		DisparityRecordDO disparityRecordDO = new DisparityRecordDO();
		disparityRecordDO.setFrontRecordId(frontRecord.getId());
		disparityRecordDO.setFrontRecordCode(frontRecord.getRecordCode());
        disparityRecordDO.setInWarehouseRecordId(putInRecords.getId());
        disparityRecordDO.setInWarehouseRecordCode(putInRecords.getRecordCode());
        disparityRecordDO.setOutWarehouseRecordId(outRecords.getId());
        disparityRecordDO.setOutWarehouseRecordCode(outRecords.getRecordCode());
        disparityRecordDO.setRecordType(frtype.getType());
        //仓库信息数据 
        disparityRecordDO.setInFactoryCode(putInRecords.getFactoryCode()); 
        disparityRecordDO.setInRealWarehouseId(putInRecords.getRealWarehouseId());
        disparityRecordDO.setInRealWarehouseOutCode(putInRecords.getRealWarehouseCode());
        disparityRecordDO.setOutFactoryCode(outRecords.getFactoryCode());
        disparityRecordDO.setOutRealWarehouseId(outRecords.getRealWarehouseId());
        disparityRecordDO.setOutRealWarehouseOutCode(outRecords.getRealWarehouseCode());
        //SAP 采购单号和交货单号 ???
        //disparityRecordDO.setSapPoNo(?);
        //disparityRecordDO.setSapDeliveryCode(?);
        disparityRecordDO.setRecordStatus(FrontRecordStatusEnum.INIT.getStatus());
        disparityRecordDO.setRecordCode(orderNo);
		return disparityRecordDO;
	}
	
	/**
	 * @Description 构建差异单明细信息
	 * @Author Lin.Xu
	 * @Date 15:10 2020/7/16
	 * @Param [putInRecordDetail, outRecordDetail, frontRecord]
	 * @return com.lyf.scm.core.domain.entity.disparity.disparityDetailDO
	 **/
	public static DisparityDetailDO createDisparityDetailDO(WarehouseRecordDetailE putInRecordDetail,
														   WarehouseRecordDetailE outRecordDetail,
														   FrontWarehouseRecordRelationE frontRecord) {
		//检验出入库单明细参数
		if(null == putInRecordDetail && null == outRecordDetail) {
			return null;
		}
		//1.如果出库单明细和入库单明细都不为空，且存在实出数量和实收数量一致的情况,不构建差异明细
		if(null != putInRecordDetail && null != outRecordDetail 
				&& putInRecordDetail.getActualQty().equals(outRecordDetail.getActualQty())) {
			return null;
		}
		//2.根据出入库单明细是否存在及差异构建参数对象和差异值
		WarehouseRecordDetailE paramRecordE = null;
		BigDecimal disparitySkuQty = BigDecimal.ZERO;
		//2.1出入库单都不为空且存在出库数量和入库数量存在差异
		if(null != outRecordDetail && null != putInRecordDetail) {
			paramRecordE = putInRecordDetail;
			disparitySkuQty = outRecordDetail.getActualQty().subtract(putInRecordDetail.getActualQty());
		//2.2出库单明细不为空，入库单明细为空
		}else if(null != outRecordDetail && null == putInRecordDetail) {
			paramRecordE = outRecordDetail;
			disparitySkuQty = outRecordDetail.getActualQty().subtract(BigDecimal.ZERO);
		//2.2出库单明细为空，入库单明细不为空
		}else if(null == outRecordDetail && null != putInRecordDetail) {
			paramRecordE = putInRecordDetail;
			disparitySkuQty = BigDecimal.ZERO.subtract(putInRecordDetail.getActualQty());
		}else {
			return null;
		}
		//构建差异单明细信息
		DisparityDetailDO disparityDetailDO = new DisparityDetailDO();
		disparityDetailDO.setSkuId(paramRecordE.getSkuId());
		disparityDetailDO.setFrontRecordCode(frontRecord.getFrontRecordCode());
        disparityDetailDO.setFrontRecordId(frontRecord.getFrontRecordId());
        disparityDetailDO.setSapPoNo(paramRecordE.getSapPoNo());
        disparityDetailDO.setLineNo(paramRecordE.getLineNo());
        disparityDetailDO.setDeliveryLineNo(paramRecordE.getDeliveryLineNo());
        disparityDetailDO.setSkuCode(paramRecordE.getSkuCode());
        disparityDetailDO.setUnit(paramRecordE.getBasicUnit());
        disparityDetailDO.setUnitCode(paramRecordE.getBasicUnitCode());
        disparityDetailDO.setOutSkuQty(paramRecordE.getPlanQty());
        disparityDetailDO.setInSkuQty(paramRecordE.getActualQty());
        disparityDetailDO.setScale(paramRecordE.getScale());
        disparityDetailDO.setRealUnit(paramRecordE.getUnit());
        disparityDetailDO.setRealUnitCode(paramRecordE.getUnitCode());
        disparityDetailDO.setSkuQty(disparitySkuQty);
        return disparityDetailDO;
	}

	/**
	 * @Description //TODO
	 * @Author Lin.Xu
	 * @Date 21:59 2020/7/16
	 * @Param [
	 * orderNo(订单号),
	 * disparityDetailList(差异明细),
	 * disparityRecordE(差异记录),
	 * warehouseRecordTypeEnum(后置单据类型),
	 * syncTrade(同步通知交易状态)]
	 * @return com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE
	 **/
	public static WarehouseRecordE createWarehouseRecordE(String orderNo,
														  List<DisparityDetailE> disparityDetailList,
														  DisparityRecordE disparityRecordE,
														  WarehouseRecordTypeEnum warehouseRecordTypeEnum,
														  Integer syncTrade){
		//创建后置单记录信息
		WarehouseRecordE warehouseRecordE = new WarehouseRecordE();
		warehouseRecordE.setBatchStatus(syncTrade);
		warehouseRecordE.setRecordCode(orderNo);
		//差异管理的出入单单据都是一步即到已出库或已入库状态
		if(WarehouseRecordBusinessTypeEnum.IN_WAREHOUSE_RECORD.getType().equals(warehouseRecordTypeEnum.getBusinessType())) {
			//入库单
			warehouseRecordE.setRecordStatus(WarehouseRecordStatusEnum.IN_ALLOCATION.getStatus());
		}else{
			//出库单
			warehouseRecordE.setRecordStatus(WarehouseRecordStatusEnum.OUT_ALLOCATION.getStatus());
		}
		warehouseRecordE.setRealWarehouseCode(disparityDetailList.get(0).getHandlerInRealWarehouseOutCode());
		warehouseRecordE.setFactoryCode(disparityDetailList.get(0).getHandlerInFactoryCode());
		warehouseRecordE.setRealWarehouseId(disparityRecordE.getInRealWarehouseId());
		warehouseRecordE.setBusinessType(warehouseRecordTypeEnum.getBusinessType());
		warehouseRecordE.setRecordType(warehouseRecordTypeEnum.getType());
		//warehouseRecordDO.setOutCreateTime(frontRecord.getOutCreateTime());
		//warehouseRecordDO.setWarehouseRecordDetails(new ArrayList<>(detailES.size()));
		//默认无需同步wms
		warehouseRecordE.setSyncWmsStatus(WmsSyncStatusEnum.NO_REQUIRED.getStatus());
		//默认无需派车
		warehouseRecordE.setSyncDispatchStatus(WarehouseRecordConstant.INIT_DISPATCH);
		//默认无需过账
		warehouseRecordE.setSyncTransferStatus(WarehouseRecordConstant.INIT_TRANSFER);
		//默认无需同步交易中心，需要同步交易中心的 调用方自己处理
		//warehouseRecord.setSyncTradeStatus(WarehouseRecordConstant.INIT_SYNC_TRADE);
		//默认无需处理批次库存 需要出库批次库存的 调用方自己处理
		warehouseRecordE.setBatchStatus(WarehouseRecordBatchStatusEnum.NO_HANDLING.getStatus());
		List<WarehouseRecordDetailE> warehouseRecordDetailEList = new ArrayList<WarehouseRecordDetailE>();
		for (DisparityDetailE detailE : disparityDetailList) {
			WarehouseRecordDetailE warehouseRecordDetailE = new WarehouseRecordDetailE();
			warehouseRecordDetailE.setLineNo(detailE.getLineNo());
			warehouseRecordDetailE.setDeliveryLineNo(detailE.getDeliveryLineNo());
			warehouseRecordDetailE.setSapPoNo(detailE.getSapPoNo());
			warehouseRecordDetailE.setSkuId(detailE.getSkuId());
			warehouseRecordDetailE.setSkuCode(detailE.getSkuCode());
			warehouseRecordDetailE.setPlanQty(detailE.getSkuQty());
			warehouseRecordDetailE.setUnit(detailE.getUnit());
			warehouseRecordDetailE.setUnitCode(detailE.getUnitCode());
			warehouseRecordDetailE.setSkuQty(detailE.getSkuQty());
			warehouseRecordDetailE.setActualQty(detailE.getSkuQty());
			warehouseRecordDetailEList.add(warehouseRecordDetailE);
		}
		warehouseRecordE.setWarehouseRecordDetailList(warehouseRecordDetailEList);
		return warehouseRecordE;
	}

	/**
	 * @Description 本地后置单据转换库存接口入库单据列表
	 * @Author Lin.Xu
	 * @Date 10:55 2020/7/17
	 * @Param [rsList]
	 * @return java.util.List<com.lyf.scm.core.remote.stock.dto.InWarehouseRecordDTO>
	 **/
	public static List<InWarehouseRecordDTO> convertPutInStockWarehouseRs(List<WarehouseRecordE> rsList, Map<String, String> outOrderNoMap){
		if(null == rsList){
			return null;
		}
		//对象转换
		List<InWarehouseRecordDTO> inWarehouseRecordDTOS = new ArrayList<InWarehouseRecordDTO>();
		for(WarehouseRecordE recordE : rsList){
			InWarehouseRecordDTO inStockRecord = new InWarehouseRecordDTO();
			inStockRecord.setRecordCode(recordE.getRecordCode());
			inStockRecord.setFactoryCode(recordE.getFactoryCode());
			//获取出库单号
			String outOrderNo = outOrderNoMap.get(recordE.getRecordCode());
			if(null != outOrderNo) {
				inStockRecord.setOutWarehouseRecordCode(outOrderNo);
			}
			inStockRecord.setChannelCode(recordE.getChannelCode());
			inStockRecord.setWarehouseCode(recordE.getRealWarehouseCode());
			inStockRecord.setRecordType(recordE.getRecordType());
			inStockRecord.setSapPoNo(recordE.getSapOrderCode());
			inStockRecord.setOutRecordCode(recordE.getFrontRecordCode());
			inStockRecord.setDetailList(convertRecordDetailRs(recordE.getWarehouseRecordDetailList()));
			inWarehouseRecordDTOS.add(inStockRecord);
		}
		return inWarehouseRecordDTOS;
	}
	
	/**
	 * @Description 本地后置单据转换库存接口入库单据列表
	 * @Author Lin.Xu
	 * @Date 11:15 2020/7/17
	 * @Param [rsList]
	 * @return java.util.List<com.lyf.scm.core.remote.stock.dto.OutWarehouseRecordDTO>
	 **/
	public static List<OutWarehouseRecordDTO> convertOutStockWarehouseRs(List<WarehouseRecordE> rsList){
		if(null == rsList){
			return null;
		}
		//对象转换
		List<OutWarehouseRecordDTO> outWarehouseRecordDTOS = new ArrayList<OutWarehouseRecordDTO>();
		for (WarehouseRecordE recordE : rsList){
			OutWarehouseRecordDTO outWarehouseRecordDTO = new OutWarehouseRecordDTO();
			outWarehouseRecordDTO.setRecordCode(recordE.getRecordCode());
			outWarehouseRecordDTO.setFactoryCode(recordE.getFactoryCode());
			outWarehouseRecordDTO.setWarehouseCode(recordE.getRealWarehouseCode());
			outWarehouseRecordDTO.setChannelCode(recordE.getChannelCode());
			outWarehouseRecordDTO.setRecordType(recordE.getRecordType());
			outWarehouseRecordDTO.setSapPoNo(recordE.getSapOrderCode());
			outWarehouseRecordDTO.setOutRecordCode(recordE.getFrontRecordCode());
			outWarehouseRecordDTO.setDetailList(convertRecordDetailRs(recordE.getWarehouseRecordDetailList()));
			outWarehouseRecordDTOS.add(outWarehouseRecordDTO);
		}
		return outWarehouseRecordDTOS;
	}


	/**
	 * @Description 明细对象转换
	 * @Author Lin.Xu
	 * @Date 11:13 2020/7/17
	 * @Param [recordDetailES]
	 * @return java.util.List<com.lyf.scm.core.remote.stock.dto.RecordDetailDTO>
	 **/
	public static List<RecordDetailDTO> convertRecordDetailRs(List<WarehouseRecordDetailE> recordDetailES){
		if(null == recordDetailES){
			return null;
		}
		//对象转换
		List<RecordDetailDTO> detailDTOS = new ArrayList<RecordDetailDTO>();
		for(WarehouseRecordDetailE detail : recordDetailES){
			RecordDetailDTO recordDetailDTO = new RecordDetailDTO();
			recordDetailDTO.setBasicSkuQty(detail.getActualQty());
			recordDetailDTO.setBasicUnit(detail.getUnit());
			recordDetailDTO.setBasicUnitCode(detail.getUnitCode());
			recordDetailDTO.setDeliveryLineNo(detail.getDeliveryLineNo());
			recordDetailDTO.setLineNo(detail.getLineNo());
			recordDetailDTO.setSkuCode(detail.getSkuCode());
			recordDetailDTO.setSapPoNo(detail.getSapPoNo());
			detailDTOS.add(recordDetailDTO);
		}
		return detailDTOS;
	}

}
