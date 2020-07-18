package com.lyf.scm.core.service.notify.impl;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.FrontRecordTypeEnum;
import com.lyf.scm.common.enums.WarehouseRecordBusinessTypeEnum;
import com.lyf.scm.common.enums.WarehouseRecordStatusEnum;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.notify.StockNotifyDTO;
import com.lyf.scm.core.api.dto.notify.StockNotifyDetailDTO;
import com.lyf.scm.core.domain.entity.stockFront.FrontWarehouseRecordRelationE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.stockFront.FrontWarehouseRecordRelationMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordDetailMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.service.notify.StockNotifyService;
import com.lyf.scm.core.service.order.OrderService;
import com.lyf.scm.core.service.orderReturn.OrderReturnService;
import com.lyf.scm.core.service.shopReturn.ShopReturnService;
import com.lyf.scm.core.service.stockFront.ShopReplenishService;
import com.lyf.scm.core.service.stockFront.WhAllocationService;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 库存回调通知服务
 * <p>
 * @Author: chuwenchao  2020/6/19
 */
@Slf4j
@Service("stockNotifyService")
public class StockNotifyServiceImpl implements StockNotifyService {

    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;
    @Resource
    private WarehouseRecordDetailMapper warehouseRecordDetailMapper;
    @Resource
    private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;
    @Resource
    private WhAllocationService whAllocationService;
    @Resource
    private ShopReplenishService shopReplenishService;
    @Resource
    private OrderReturnService orderReturnService;
    @Resource
    private OrderService orderService;
    @Resource
    private ShopReturnService shopReturnService;

    /**
     * @Description: 库存出库结果通知 <br>
     *
     * @Author chuwenchao 2020/6/19
     * @param stockNotifyDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void outRecordNotify(StockNotifyDTO stockNotifyDTO) {
        WarehouseRecordE warehouseRecordE = warehouseRecordMapper.queryByRecordCode(stockNotifyDTO.getRecordCode());
        AlikAssert.isNotNull(warehouseRecordE, ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + ":单号不存在" + stockNotifyDTO.getRecordCode());
        AlikAssert.isTrue(WarehouseRecordBusinessTypeEnum.OUT_WAREHOUSE_RECORD.getType().equals(warehouseRecordE.getBusinessType()), ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + ":单据接口调用错误" + stockNotifyDTO.getRecordCode());
        AlikAssert.isTrue(!WarehouseRecordStatusEnum.DISABLED.getStatus().equals(warehouseRecordE.getRecordStatus()), ResCode.ORDER_ERROR_1001, "单据已取消" + stockNotifyDTO.getRecordCode());
        //单据状态为已出库，直接返回处理成功
        if (WarehouseRecordStatusEnum.OUT_ALLOCATION.getStatus().equals(warehouseRecordE.getRecordStatus())) {
            return;
        }
        //查询前置单
        List<FrontWarehouseRecordRelationE> frontRelationList = frontWarehouseRecordRelationMapper.getFrontRelationByRecordCode(warehouseRecordE.getRecordCode());
        AlikAssert.isNotEmpty(frontRelationList, ResCode.ORDER_ERROR_5120, ResCode.ORDER_ERROR_5120_DESC);
        FrontWarehouseRecordRelationE recordRelationE = frontRelationList.get(0);
        //更新出入库单明细
        warehouseRecordMapper.updateRecordInfoToOut(stockNotifyDTO.getRecordCode(), stockNotifyDTO.getOperateTime());
        //更新出入库单状态
        List<StockNotifyDetailDTO> detailEList = stockNotifyDTO.getDetailDTOList();
        for (StockNotifyDetailDTO notifyDetailDTO : detailEList) {
            Integer flag = warehouseRecordDetailMapper.increaseActualQtyByDeliveryLineNo(notifyDetailDTO.getActualQty(), notifyDetailDTO.getDeliveryLineNo(), notifyDetailDTO.getSkuCode(), warehouseRecordE.getRecordCode());
            AlikAssert.isTrue(flag.intValue() == 1, ResCode.ORDER_ERROR_1002, "单据" + warehouseRecordE.getRecordCode() + "行信息错误" + notifyDetailDTO.getDeliveryLineNo());
        }

        //转发各个Service
        Integer frontRecordType = recordRelationE.getFrontRecordType();
        if (FrontRecordTypeEnum.WAREHOUSE_ALLOCATION_RECORD.getType().equals(frontRecordType)) {
        	try {
        		//仓库调拨
				whAllocationService.outRecordNotify(stockNotifyDTO);
			} catch (Exception e) {
				log.error("仓库调拨出库库存中心回调异常，recordCode：{}，异常：{}", warehouseRecordE.getRecordCode(), e);
       		 	throw new RomeException(ResCode.ORDER_ERROR_1001, e.getMessage());
			}
        } else if (FrontRecordTypeEnum.SHOP_REPLENISHMENT_RECORD.getType().equals(frontRecordType)
                || FrontRecordTypeEnum.JOIN_SHOP_REPLENISHMENT_RECORD.getType().equals(frontRecordType)
                || FrontRecordTypeEnum.SHOP_COLD_CHAIN_DELIVERY_RECORD.getType().equals(frontRecordType)) {
            //门店补货（直营、加盟、冷链）
            shopReplenishService.warehouseOutNotify(stockNotifyDTO);
        } else if(FrontRecordTypeEnum.RESERVATION_DO_RECORD.getType().equals(frontRecordType)){
            //预约单出库回调
            orderService.orderOutNotify(stockNotifyDTO);
        } else if (FrontRecordTypeEnum.DIRECT_SHOP_RETURN_GOODS_RECORD.getType().equals(frontRecordType)
                || FrontRecordTypeEnum.JOIN_SHOP_RETURN_GOODS_RECORD.getType().equals(frontRecordType)) {
            //门店退货（直营、加盟）
            shopReturnService.warehouseOutNotify(stockNotifyDTO);
        } else if (FrontRecordTypeEnum.OUT_IN_REVERSE.getType().equals(frontRecordType)) {
            //冲销单
            //shopReturnService.warehouseOutNotify(stockNotifyDTO);
        } else {
            throw new RomeException(ResCode.ORDER_ERROR_1001, "暂不支持该出库订单类型" + stockNotifyDTO.getRecordCode());
        }
    }

    /**
     * @Description: 库存入库结果通知 <br>
     *
     * @Author chuwenchao 2020/6/19
     * @param stockNotifyDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inRecordNotify(StockNotifyDTO stockNotifyDTO) {
        WarehouseRecordE warehouseRecordE = warehouseRecordMapper.queryByRecordCode(stockNotifyDTO.getRecordCode());
        AlikAssert.isNotNull(warehouseRecordE, ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + ":单号不存在" + stockNotifyDTO.getRecordCode());
        AlikAssert.isTrue(WarehouseRecordBusinessTypeEnum.IN_WAREHOUSE_RECORD.getType().equals(warehouseRecordE.getBusinessType()), ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + ":单据接口调用错误" + stockNotifyDTO.getRecordCode());
        AlikAssert.isTrue(!WarehouseRecordStatusEnum.DISABLED.getStatus().equals(warehouseRecordE.getRecordStatus()), ResCode.ORDER_ERROR_1001, "单据已取消" + stockNotifyDTO.getRecordCode());
        //查询前置单
        List<FrontWarehouseRecordRelationE> frontRelationList = frontWarehouseRecordRelationMapper.getFrontRelationByRecordCode(warehouseRecordE.getRecordCode());
        AlikAssert.isNotEmpty(frontRelationList, ResCode.ORDER_ERROR_5120, ResCode.ORDER_ERROR_5120_DESC);
        FrontWarehouseRecordRelationE recordRelationE = frontRelationList.get(0);
        //更新出入库单明细
        warehouseRecordMapper.updateRecordInfoToIn(stockNotifyDTO.getRecordCode(), stockNotifyDTO.getOperateTime());
        //更新出入库单状态
        List<StockNotifyDetailDTO> detailEList = stockNotifyDTO.getDetailDTOList();
        for (StockNotifyDetailDTO notifyDetailDTO : detailEList) {
            Integer flag = warehouseRecordDetailMapper.increaseActualQtyByDeliveryLineNo(notifyDetailDTO.getActualQty(), notifyDetailDTO.getDeliveryLineNo(), notifyDetailDTO.getSkuCode(), warehouseRecordE.getRecordCode());
            AlikAssert.isTrue(flag.intValue() == 1, ResCode.ORDER_ERROR_1002, "单据" + warehouseRecordE.getRecordCode() + "行信息错误" + notifyDetailDTO.getDeliveryLineNo());
        }

        //转发各个Service
        Integer frontRecordType = recordRelationE.getFrontRecordType();
        if (FrontRecordTypeEnum.WAREHOUSE_ALLOCATION_RECORD.getType().equals(frontRecordType)) {
            //仓库调拨
        	try {
				whAllocationService.inRecordNotify(stockNotifyDTO);
			} catch (Exception e) {
				log.error("仓库调拨入库存中心回调异常，recordCode：{}，异常：{}", warehouseRecordE.getRecordCode(), e);
				throw new RomeException(ResCode.ORDER_ERROR_1001, e.getMessage());
			}
        } else if (FrontRecordTypeEnum.SHOP_REPLENISHMENT_RECORD.getType().equals(frontRecordType)
                || FrontRecordTypeEnum.JOIN_SHOP_REPLENISHMENT_RECORD.getType().equals(frontRecordType)
                || FrontRecordTypeEnum.SHOP_COLD_CHAIN_DELIVERY_RECORD.getType().equals(frontRecordType)) {
            //门店补货（直营、加盟、冷链、直送）
            shopReplenishService.warehouseInNotify(stockNotifyDTO);
        } else if(FrontRecordTypeEnum.GROUP_RETURN_RECORD.getType().equals(frontRecordType)){
                orderReturnService.receipt(stockNotifyDTO);
        } else if (FrontRecordTypeEnum.DIRECT_SHOP_RETURN_GOODS_RECORD.getType().equals(frontRecordType)
                || FrontRecordTypeEnum.JOIN_SHOP_RETURN_GOODS_RECORD.getType().equals(frontRecordType)) {
            //门店退货（直营、加盟）
            shopReturnService.warehouseInNotify(stockNotifyDTO);
        }else {
            throw new RomeException(ResCode.ORDER_ERROR_1001, "暂不支持该入库订单类型" + stockNotifyDTO.getRecordCode());
        }
    }

}
