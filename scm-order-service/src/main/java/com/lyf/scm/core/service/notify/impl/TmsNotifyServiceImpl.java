package com.lyf.scm.core.service.notify.impl;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.constants.TmsNotifyConstant;
import com.lyf.scm.common.enums.WarehouseRecordBusinessTypeEnum;
import com.lyf.scm.common.enums.WarehouseRecordTypeEnum;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.notify.DispatchNoticeDTO;
import com.lyf.scm.core.api.dto.notify.TmsNotifyDTO;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.remote.stock.dto.CancelResultDTO;
import com.lyf.scm.core.remote.stock.dto.UpdateTmsCodeDTO;
import com.lyf.scm.core.remote.stock.facade.StockRecordFacade;
import com.lyf.scm.core.service.notify.TmsNotifyService;
import com.lyf.scm.core.service.shopReturn.ShopReturnService;
import com.lyf.scm.core.service.stockFront.ShopReplenishService;
import com.lyf.scm.core.service.stockFront.WhAllocationService;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("tmsNotifyService")
public class TmsNotifyServiceImpl implements TmsNotifyService {

    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;

    @Resource
    private StockRecordFacade stockRecordFacade;

    @Resource
    private ShopReturnService shopReturnService;

    @Resource
    private WhAllocationService whAllocationService;

    @Resource
    private ShopReplenishService shopReplenishService;

    @Value("${spring.application.name}")
    private String sourceSystem;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dispatchTMSNotify(TmsNotifyDTO tmsNotifyDTO) {
        List<DispatchNoticeDTO> dispatchNoticeDTOList = new ArrayList<>();
        //根据出入库单据编号查询出入库单
        WarehouseRecordE warehouseRecordE = warehouseRecordMapper.queryByRecordCode(tmsNotifyDTO.getRecordCode());
        AlikAssert.isNotNull(warehouseRecordE, ResCode.ORDER_ERROR_6026, ResCode.ORDER_ERROR_6026_DESC);
        //判断是否是出库单
        AlikAssert.isTrue(WarehouseRecordBusinessTypeEnum.OUT_WAREHOUSE_RECORD.getType().equals(warehouseRecordE.getBusinessType()), ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + ":单据接口调用错误" + tmsNotifyDTO.getRecordCode());
        Integer isDispatch = tmsNotifyDTO.getIsDispatch();
        if (isDispatch.equals(TmsNotifyConstant.NEED_DISPATCH) && StringUtils.isBlank(tmsNotifyDTO.getTmsRecordCode())) {
            throw new RomeException(ResCode.ORDER_ERROR_6030, ResCode.ORDER_ERROR_6030_DESC);
        }
        //根据出入库单据编号修改TMS派车单号、 派车状态
        int result = warehouseRecordMapper.updateTmsRecordCodeAndDispatchStatus(tmsNotifyDTO.getRecordCode(),
                tmsNotifyDTO.getTmsRecordCode(),isDispatch);
        if(result < 1) {
            throw new RomeException(ResCode.ORDER_ERROR_7304, ResCode.ORDER_ERROR_7304_DESC);
        }
        //转发各个Service
        Integer recordType = warehouseRecordE.getRecordType();
        if (WarehouseRecordTypeEnum.WH_ALLOCATION_OUT_WAREHOUSE_RECORD.getType().equals(recordType)) {
            //仓库调拨派车通知
            try {
                whAllocationService.tmsNotify(tmsNotifyDTO);
            } catch (Exception e) {
                log.error("仓库调拨派车通知回调异常，recordCode：{}，异常：{}", warehouseRecordE.getRecordCode(), e);
                throw new RomeException(ResCode.ORDER_ERROR_1001, e.getMessage());
            }
        } else if (WarehouseRecordTypeEnum.DS_REPLENISH_OUT_WAREHOUSE_RECORD.getType().equals(recordType)
                || WarehouseRecordTypeEnum.LS_REPLENISH_OUT_WAREHOUSE_RECORD.getType().equals(recordType)
                || WarehouseRecordTypeEnum.WH_COLD_CHAIN_OUT_RECORD.getType().equals(recordType)) {
            //直营门店 + 加盟门店 + 冷链
            try {
                shopReplenishService.dispatchResultReplenishComplete(tmsNotifyDTO.getRecordCode());
            } catch (Exception e) {
                log.error("仓库调拨派车通知回调异常-修改前置单状态失败，recordCode：{}，异常：{}", warehouseRecordE.getRecordCode(), e);
                throw new RomeException(ResCode.ORDER_ERROR_1001, e.getMessage());
            }
        }else if(WarehouseRecordTypeEnum.DS_RETURN_OUT_WAREHOUSE_RECORD.getType().equals(recordType)
                || WarehouseRecordTypeEnum.LS_RETURN_OUT_WAREHOUSE_RECORD.getType().equals(recordType)
                || WarehouseRecordTypeEnum.DS_RETURN_COLD_OUT_WAREHOUSE_RECORD.getType().equals(recordType)
                || WarehouseRecordTypeEnum.LS_RETURN_COLD_OUT_WAREHOUSE_RECORD.getType().equals(recordType)){
                 //直营门店退货 + 加盟门店退货 + 直营门店冷链退货
            try{
                shopReturnService.dispatchResultShopReturnComplete(tmsNotifyDTO.getRecordCode());
            }catch (Exception e) {
                log.error("门店退货派车通知回调异常，recordCode：{}，异常：{}", warehouseRecordE.getRecordCode(), e);
                throw new RomeException(ResCode.ORDER_ERROR_1001, e.getMessage());
            }
        } else {
            throw new RomeException(ResCode.ORDER_ERROR_1001, "暂不支持该订单类型" + tmsNotifyDTO.getRecordCode());
        }
        //通知库存派车结果
        this.createDispatchNoticeDTO(dispatchNoticeDTOList,isDispatch,tmsNotifyDTO);
        List<CancelResultDTO> cancelResultDTOList = stockRecordFacade.batchDispatchingNotify(dispatchNoticeDTOList);
        if (CollectionUtils.isEmpty(cancelResultDTOList) ||  !cancelResultDTOList.get(0).getStatus()) {
            log.info("仓库调拨派车通知库存回调异常，recordCode：{}，异常：{}", tmsNotifyDTO.getRecordCode());
            throw new RomeException(ResCode.ORDER_ERROR_1001, "仓库调拨派车通知库存异常" + tmsNotifyDTO.getRecordCode());
        }
    }

    private void createDispatchNoticeDTO(List<DispatchNoticeDTO> dispatchNoticeDTOList,Integer isDispatch,TmsNotifyDTO tmsNotifyDTO){
        DispatchNoticeDTO dispatchNoticeDTO = new DispatchNoticeDTO();
        if (isDispatch == TmsNotifyConstant.NEED_DISPATCH ) {
            dispatchNoticeDTO.setIsDispatch(true);
        } else if (isDispatch == TmsNotifyConstant.NOT_DISPATCH){
            dispatchNoticeDTO.setIsDispatch(false);
        }
        dispatchNoticeDTO.setRecordCode(tmsNotifyDTO.getRecordCode());
        dispatchNoticeDTO.setThirdRecordCode(tmsNotifyDTO.getTmsRecordCode());
        dispatchNoticeDTO.setSourceSystem(sourceSystem);
        dispatchNoticeDTOList.add(dispatchNoticeDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDispatchNotify(TmsNotifyDTO tmsNotifyDTO) {
        WarehouseRecordE warehouseRecordE = warehouseRecordMapper.queryByRecordCode(tmsNotifyDTO.getRecordCode());
        AlikAssert.isNotNull(warehouseRecordE, ResCode.ORDER_ERROR_6026, ResCode.ORDER_ERROR_6026_DESC);
        //判断是否是出库单
        AlikAssert.isTrue(WarehouseRecordBusinessTypeEnum.OUT_WAREHOUSE_RECORD.getType().equals(warehouseRecordE.getBusinessType()), ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + ":单据接口调用错误" + tmsNotifyDTO.getRecordCode());
        Integer isDispatch = tmsNotifyDTO.getIsDispatch();
        if (isDispatch.equals(TmsNotifyConstant.NEED_DISPATCH) && StringUtils.isBlank(tmsNotifyDTO.getTmsRecordCode())) {
            throw new RomeException(ResCode.ORDER_ERROR_6030, ResCode.ORDER_ERROR_6030_DESC);
        }
        int result = warehouseRecordMapper.updateNotifyTmsRecordCode(tmsNotifyDTO.getRecordCode(),
                tmsNotifyDTO.getTmsRecordCode());
        if(result < 1) {
            throw new RomeException(ResCode.ORDER_ERROR_7304, ResCode.ORDER_ERROR_7304_DESC);
        }
        //通知库存派车结果
        List<CancelResultDTO> cancelResultDTOList = stockRecordFacade.updateWarehouseSaleTobTmsOrder(createUpdateTmsCodeDTO(tmsNotifyDTO));
        if (CollectionUtils.isEmpty(cancelResultDTOList) ||  !cancelResultDTOList.get(0).getStatus()) {
            log.info("仓库调拨派车通知库存回调异常，recordCode：{}，异常：{}", tmsNotifyDTO.getRecordCode());
            throw new RomeException(ResCode.ORDER_ERROR_1001, "仓库调拨派车通知库存异常" + tmsNotifyDTO.getRecordCode());
        }
    }

    private UpdateTmsCodeDTO createUpdateTmsCodeDTO(TmsNotifyDTO tmsNotifyDTO){
        UpdateTmsCodeDTO updateTmsCodeDTO = new UpdateTmsCodeDTO();
        updateTmsCodeDTO.setTmsCode(tmsNotifyDTO.getTmsRecordCode());
        List<String> recordCodeList = new ArrayList<>();
        recordCodeList.add(tmsNotifyDTO.getRecordCode());
        updateTmsCodeDTO.setRecordCodeList(recordCodeList);
        return updateTmsCodeDTO;
    }


}
