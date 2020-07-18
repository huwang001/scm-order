package com.lyf.scm.core.service.common.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.lyf.scm.common.constants.KibanaLogConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.constants.WhAllocationConstants;
import com.lyf.scm.common.enums.FrontRecordTypeEnum;
import com.lyf.scm.common.enums.OrderStatusEnum;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.pack.DemandFromSoDTO;
import com.lyf.scm.core.config.ServiceKibanaLog;
import com.lyf.scm.core.domain.entity.common.RecordStatusLogE;
import com.lyf.scm.core.domain.entity.order.OrderDetailE;
import com.lyf.scm.core.domain.entity.order.OrderE;
import com.lyf.scm.core.domain.entity.stockFront.WhAllocationDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WhAllocationE;
import com.lyf.scm.core.domain.model.OrderDO;
import com.lyf.scm.core.domain.model.OrderDetailDO;
import com.lyf.scm.core.mapper.order.OrderDetailMapper;
import com.lyf.scm.core.mapper.order.OrderMapper;
import com.lyf.scm.core.remote.pack.facade.PackFacade;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.dto.WarehouseQueryDTO;
import com.lyf.scm.core.remote.stock.facade.StockFacade;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.service.common.RealWarehouseAllocationService;
import com.lyf.scm.core.service.common.RecordStatusLogService;
import com.lyf.scm.core.service.order.OrderService;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.pack.PackDemandService;
import com.lyf.scm.core.service.stockFront.WhAllocationService;
import com.rome.arch.core.exception.RomeException;

import lombok.extern.slf4j.Slf4j;

/**
 * 实仓调拨
 */
@Slf4j
@Service("realWarehouseAllocationService")
public class RealWarehouseAllocationServiceImpl implements RealWarehouseAllocationService {

    @Resource
    private StockFacade stockFacade;
    
    @Resource
    private OrderMapper orderMapper;
    
    @Resource
    private OrderDetailMapper orderDetailMapper;
    
    @Resource
    private RecordStatusLogService recordStatusLogService;
    
    @Resource
    private OrderService orderService;
    
    @Resource
    private WhAllocationService whAllocationService;
    
    @Resource
    private OrderUtilService orderUtilService;
    
    @Resource
    private PackDemandService packDemandService;
    
    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;
    
    @Resource
    private PackFacade packFacade;
    
    @Override
    public List<RealWarehouse> queryRealWarehouseByFactoryCodeAndType(String factoryCode, Integer type) {
        WarehouseQueryDTO warehouseQueryDTO=new WarehouseQueryDTO();
        warehouseQueryDTO.setFactoryCode(factoryCode);
        warehouseQueryDTO.setType(type);
        return stockRealWarehouseFacade.queryRealWarehouseByFactoryCodeAndType(warehouseQueryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void realWarehouseAllocation(String orderCode, String allotRealWarehouseCode, Long userId) {
        //必要参数校验
        this.validRealWarehouseAllocation(orderCode,allotRealWarehouseCode);
        //根据预约单号orderCode查询预约单
        OrderE orderE = orderService.queryOrderWithDetail(orderCode);
        AlikAssert.isNotNull(orderE, ResCode.ORDER_ERROR_1004, ResCode.ORDER_ERROR_1004_DESC);
        //预约单状态orderStatus是否等于调拨通过，直接返回
        if(orderE.getOrderStatus().equals(OrderStatusEnum.ALLOT_AUDIT_STATUS_PASSED.getStatus())){
            return;
        }
        //预约单状态orderStatus是否等于1（部分锁定）或2（全部锁定）
        if(!OrderStatusEnum.LOCK_STATUS_ALL.getStatus().equals(orderE.getOrderStatus()) && !OrderStatusEnum.LOCK_STATUS_PART.getStatus().equals(orderE.getOrderStatus())){
            throw new RomeException(ResCode.ORDER_ERROR_1005, ResCode.ORDER_ERROR_1005_DESC);
        }
        //交易审核通过hasTradeAudit是否等于1（已通过）
        if(orderE.getHasTradeAudit() != 1){
        	throw new RomeException(ResCode.ORDER_ERROR_1038, ResCode.ORDER_ERROR_1038_DESC);
        }
        //预约单创建调拨hasAllot是否等于1（已创建）
        if(orderE.getHasAllot() == 1){
            throw new RomeException(ResCode.ORDER_ERROR_1006, ResCode.ORDER_ERROR_1006_DESC);
        }
        List<OrderDetailE> orderDetailList=orderDetailMapper.queryOrderDetailByRecordCode(orderCode);
        if(CollectionUtils.isEmpty(orderDetailList)){
            throw new RomeException(ResCode.ORDER_ERROR_1007, ResCode.ORDER_ERROR_1007_DESC);
        }
        WarehouseQueryDTO warehouseQueryDTO=new WarehouseQueryDTO();
        warehouseQueryDTO.setFactoryCode(orderE.getFactoryCode());
        warehouseQueryDTO.setType(orderE.getNeedPackage());
        List<RealWarehouse> realWarehouseList=stockRealWarehouseFacade.queryRealWarehouseByFactoryCodeAndType(warehouseQueryDTO);
        List<String> realWarehouseCodeList=realWarehouseList.stream().distinct().map(RealWarehouse :: getKey).collect(Collectors.toList());
        if(!realWarehouseCodeList.contains(orderE.getFactoryCode() +"-"+ allotRealWarehouseCode)){
            throw new RomeException(ResCode.ORDER_ERROR_1008, ResCode.ORDER_ERROR_1008_DESC);
        }
        
        orderE.setAllotFactoryCode(orderE.getFactoryCode());
        orderE.setAllotRealWarehouseCode(allotRealWarehouseCode);
        orderE.setModifier(userId);
        //生成调拨单号
        String allotCode = orderUtilService.queryOrderCode(FrontRecordTypeEnum.WAREHOUSE_ALLOCATION_RECORD.getCode());
        //设置创建调拨hasAllot=1(已创建)、实仓调拨单号allotCode、调入工厂编码allotFactoryCode、调入实仓CODE allotRealWarehouseCode，根据预约单号orderCode修改预约单
        if(OrderStatusEnum.LOCK_STATUS_ALL.getStatus().equals(orderE.getOrderStatus())) {
        	orderE.setOrderStatus(OrderStatusEnum.ALLOT_AUDIT_STATUS_PASSED.getStatus());
        }
        orderE.setAllotCode(allotCode);
        orderE.setHasAllot(1);
        // 前置条件预约单状态orderStatus=1（部分锁定）或2（全部锁定）、交易审核通过hasTradeAudit=1（已通过）、创建调拨hasAllot=0（未创建）
        int row = orderMapper.updateOrderAllocationStatus(orderE);
        if(row < 1) {
        	throw new RomeException(ResCode.ORDER_ERROR_1010, ResCode.ORDER_ERROR_1010_DESC);
        }
        if(OrderStatusEnum.LOCK_STATUS_ALL.getStatus().equals(orderE.getOrderStatus())) {
        	//添加调用日志
        	recordStatusLogService.insertRecordStatusLog(new RecordStatusLogE(orderE.getOrderCode(),orderE.getOrderStatus()));
        }
        if(OrderStatusEnum.LOCK_STATUS_PART.getStatus().equals(orderE.getOrderStatus())) {
        	orderService.updateOrderStatusAndSyncTradeStatus(orderE.getOrderCode());
        }
        
        
        //是否需要包装0否 1是
        Integer needPackage = orderE.getNeedPackage();
        //需求单号
        String recordCode = null;
        if(Integer.valueOf(1).equals(needPackage)) { 
        	//需要包装，创建包装需求单
        	DemandFromSoDTO demandFromSoDTO = new DemandFromSoDTO();
        	demandFromSoDTO.setUserId(userId);
        	demandFromSoDTO.setOrderCode(orderE.getOrderCode());
        	recordCode = packDemandService.createPackDemandBySo(demandFromSoDTO);
        }
        boolean isSucc = false;
        try {
			//根据预约单创建调拨单
			this.createWhallotOrder(orderE, allotCode);
			isSucc = true;
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			if(!isSucc && Integer.valueOf(1).equals(needPackage) && StringUtils.isNotBlank(recordCode)) {
				try {
					//包装系统取消需求单
					packFacade.batchCancelRequire(Arrays.asList(recordCode));
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.ROLL_BACK_EXCEPTION, "cancelRequireOrder", "取消包装系统需求单：" + recordCode, recordCode));
				}
				
			}
		}
    }
    
    /**
     * 根据预约单创建调拨单
     * 
     * @param orderE
     * @param allotCode
     * @return
     */
    private void createWhallotOrder(OrderE orderE, String allotCode) {
        //1、查询入库仓库是否存在
    	RealWarehouse inRw = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(orderE.getAllotRealWarehouseCode(), orderE.getAllotFactoryCode());
        AlikAssert.isNotNull(inRw, ResCode.ORDER_ERROR_6004, ResCode.ORDER_ERROR_6004_DESC);
        orderE.setInRealWarehouse(inRw);
        //2、查询出库仓库是否存在
        RealWarehouse outRw = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(orderE.getRealWarehouseCode(), orderE.getFactoryCode());
        AlikAssert.isNotNull(outRw, ResCode.ORDER_ERROR_6005, ResCode.ORDER_ERROR_6005_DESC);
        orderE.setOutRealWarehouse(outRw);
        //3、转换预约单为调拨单对象
        WhAllocationE whAllocationE =  this.coverWhallot(orderE, outRw, inRw);
        //4、修改前置单的状态和调拨单号
        whAllocationE.setRecordCode(allotCode);
        whAllocationService.createWhallotByOrder(orderE, whAllocationE);
    }
    
    /**
     * 转换预约单为调拨单
     * @param userId
     * @param warehouseRecordE
     * @param outRw
     * @param inRw
     * @return
     */
    private WhAllocationE coverWhallot(OrderE orderE, RealWarehouse outRw, RealWarehouse inRw) {
        Integer businessType = null;
        if(outRw.getFactoryCode().equals(inRw.getFactoryCode())){
            //如果出库工厂和入库工厂设置一致,设置为内部调拨
            businessType = WhAllocationConstants.INNER_ALLOCATION;
        }else {
            businessType = WhAllocationConstants.RDC_ALLOCATION;
        }
        WhAllocationE whAllocationE = new  WhAllocationE();
        whAllocationE.setBusinessType(businessType);
        whAllocationE.setInWarehouse(inRw);
        whAllocationE.setInWarehouseId(inRw.getId());
        whAllocationE.setInRealWarehouseCode(inRw.getRealWarehouseOutCode());
        whAllocationE.setInFactoryCode(inRw.getFactoryCode());
        whAllocationE.setOutWarehouse(outRw);
        whAllocationE.setOutWarehouseId(outRw.getId());
        whAllocationE.setOutRealWarehouseCode(outRw.getRealWarehouseOutCode());
        whAllocationE.setOutFactoryCode(outRw.getFactoryCode());
        whAllocationE.setAllotTime(new Date());
        whAllocationE.setExpeAogTime(new Date());
        whAllocationE.setIsQualityAllotcate(WhAllocationConstants.NOT_QUALITY_ALLOCATE);
        whAllocationE.setIsReturnAllotcate(WhAllocationConstants.NOT_RETURN_ALLOCATE);
        whAllocationE.setCreator(orderE.getModifier());
        whAllocationE.setModifier(orderE.getModifier());
        List<WhAllocationDetailE> detailList = new ArrayList<>();
        for (OrderDetailE orderDetailE : orderE.getOrderDetailEList()) {
            WhAllocationDetailE detail = new WhAllocationDetailE();
            //已锁定数量
            detail.setSkuQty(orderDetailE.getHasLockQty());
            detail.setSkuCode(orderDetailE.getSkuCode());
            detail.setUnit(orderDetailE.getUnit());
            detail.setUnitCode(orderDetailE.getUnitCode());
            detailList.add(detail);
        }
        whAllocationE.setFrontRecordDetails(detailList);
        return whAllocationE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void realWarehouseAllocationOutNotify(String allotCode) {
        if(StringUtils.isEmpty(allotCode)){
            throw new RomeException(ResCode.ORDER_ERROR_1011, ResCode.ORDER_ERROR_1011_DESC);
        }
        //根据调拨单号allotCode查询预约单
        OrderE orderE = orderMapper.queryOrderByAllotCode(allotCode);
        AlikAssert.isNotNull(orderE, ResCode.ORDER_ERROR_1004, ResCode.ORDER_ERROR_1004_DESC);
        //预约单状态orderStatus是否等于调拨出库,直接返回
        if(orderE.getOrderStatus().equals(OrderStatusEnum.ALLOT_STATUS_OUT.getStatus()) || orderE.getOrderStatus().equals(OrderStatusEnum.ALLOT_STATUS_IN.getStatus())){
           return;
        }
        //预约单状态orderStatus是否等于10（调拨审核通过）
        if(!orderE.getOrderStatus().equals(OrderStatusEnum.ALLOT_AUDIT_STATUS_PASSED.getStatus())){
            throw new RomeException(ResCode.ORDER_ERROR_1005, "预约单状态不是调拨审核通过");
        }
        //设置预约单状态orderStatus=11（调拨出库），根据预约单号orderCode修改预约单，前置条件预约单状态orderStatus=10（调拨审核通过）
        int j=orderMapper.updateOrderAllocationOutStatusByRecordCode(orderE.getOrderCode());
        if(j<=0){
            throw new RomeException(ResCode.ORDER_ERROR_1012, ResCode.ORDER_ERROR_1012_DESC);
        }
        //添加调用日志
        recordStatusLogService.insertRecordStatusLog(new RecordStatusLogE(orderE.getOrderCode(),11));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void realWarehouseAllocationInNotify(String allotCode) {
        if(StringUtils.isEmpty(allotCode)){
            throw new RomeException(ResCode.ORDER_ERROR_1011, ResCode.ORDER_ERROR_1011_DESC);
        }
        //根据调拨单号allotCode查询预约单
        OrderE orderE = orderMapper.queryOrderByAllotCode(allotCode);
        AlikAssert.isNotNull(orderE, ResCode.ORDER_ERROR_1004, ResCode.ORDER_ERROR_1004_DESC);
        //预约单状态orderStatus是否等于调拨入库/待加工，直接返回
        if(orderE.getOrderStatus().equals(OrderStatusEnum.ALLOT_STATUS_IN.getStatus())){
            return;
        }
        //预约单状态orderStatus是否等于调拨出库
        if(!orderE.getOrderStatus().equals(OrderStatusEnum.ALLOT_STATUS_OUT.getStatus())){
            throw new RomeException(ResCode.ORDER_ERROR_1005, "预约单状态不是调拨出库");
        }
        //设置预约单状态orderStatus=11（调拨出库），根据预约单号orderCode修改预约单，前置条件预约单状态orderStatus=10（调拨审核通过）
        int j=orderMapper.updateOrderAllocationInStatusByRecordCode(orderE.getOrderCode());
        if(j<=0){
            throw new RomeException(ResCode.ORDER_ERROR_1012, ResCode.ORDER_ERROR_1012_DESC);
        }
        //添加调用日志
        recordStatusLogService.insertRecordStatusLog(new RecordStatusLogE(orderE.getOrderCode(),12));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deliverOutNotifyToGroupPurchase(String orderCode) {
        if(StringUtils.isEmpty(orderCode)){
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC+": 预约单号不能为空");
        }
        //根据预约单号orderCode查询预约单
        OrderE orderE = orderMapper.queryOrderByRecordCode(orderCode);
        AlikAssert.isNotNull(orderE, ResCode.ORDER_ERROR_1004, ResCode.ORDER_ERROR_1004_DESC);
        //如果是已发货状态，直接返回
        if(orderE.getOrderStatus().equals(OrderStatusEnum.DELIVERY_STATUS_DONE.getStatus())){
            return;
        }
        //在预约单状态为30的前提下，更新预约单状态为已发货31
        if(!orderE.getOrderStatus().equals(OrderStatusEnum.DELIVERY_STATUS_WAIT.getStatus())){
            throw new RomeException(ResCode.ORDER_ERROR_1025, ResCode.ORDER_ERROR_1025_DESC);
        }
        int j=orderMapper.updateOrderDeliveryStatusByRecordCode(orderCode);
        if(j<=0){
            throw new RomeException(ResCode.ORDER_ERROR_1026, ResCode.ORDER_ERROR_1026_DESC);
        }
        // 更新预约单同步交易状态为10
        int row = orderMapper.updateSyncTradeStatusDoWaitByOrderCode(orderE.getOrderCode());
        if (row != 1) {
            throw new RomeException(ResCode.ORDER_ERROR_1023, ResCode.ORDER_ERROR_1023_DESC);
        }
        //添加调用日志
        recordStatusLogService.insertRecordStatusLog(new RecordStatusLogE(orderE.getOrderCode(),orderE.getOrderStatus()));
    }

    private void validRealWarehouseAllocation(String orderCode, String allotRealWarehouseCode){
        if(StringUtils.isEmpty(orderCode)){
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC+": 预约单号不能为空");
        }
        if(StringUtils.isEmpty(allotRealWarehouseCode)){
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC+": 调拨仓库编号不能为空");
        }
    }
}