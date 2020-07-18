package com.lyf.scm.core.service.pack.impl;

import com.lyf.scm.common.constants.CommonConstants;
import com.lyf.scm.common.constants.KibanaLogConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.FrontRecordStatusEnum;
import com.lyf.scm.common.enums.FrontRecordTypeEnum;
import com.lyf.scm.common.enums.YesOrNoEnum;
import com.lyf.scm.common.enums.pack.DemandRecordStatusEnum;
import com.lyf.scm.common.enums.pack.PackTypeEnum;
import com.lyf.scm.core.config.ServiceKibanaLog;
import com.lyf.scm.core.domain.convert.stockFront.StockInRecordDTOConvert;
import com.lyf.scm.core.domain.convert.stockFront.StockRecordDTOConvert;
import com.lyf.scm.core.domain.entity.pack.*;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.pack.*;
import com.lyf.scm.core.remote.item.ItemInfoTool;
import com.lyf.scm.core.remote.item.SkuQtyUnitTool;
import com.lyf.scm.core.remote.stock.dto.CancelRecordDTO;
import com.lyf.scm.core.remote.stock.dto.InWarehouseRecordDTO;
import com.lyf.scm.core.remote.stock.dto.OutWarehouseRecordDTO;
import com.lyf.scm.core.remote.stock.facade.StockRecordFacade;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.pack.PackTaskOperationToWhRecordService;
import com.lyf.scm.core.service.pack.TaskFinishRecordService;
import com.lyf.scm.core.service.stockFront.WarehouseRecordCommService;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ROUND_DOWN;

/**
 * @author zhanglong
 */
@Slf4j
@Service("taskFinishRecordService")
public class TaskFinishRecordServiceImpl implements TaskFinishRecordService {

    @Resource
    private PackDemandMapper packDemandMapper;
    @Resource
    private PackDemandDetailMapper packDemandDetailMapper;
    @Resource
    private PackDemandComponentMapper packDemandComponentMapper;
    @Resource
    private PackTaskFinishMapper packTaskFinishMapper;
    @Resource
    private PackTaskFinishDetailMapper packTaskFinishDetailMapper;
    @Resource
    private ItemInfoTool itemInfoTool;
    @Resource
    private SkuQtyUnitTool skuQtyUnitTool;
    @Resource
    private PackTaskOperationToWhRecordService packTaskOperationToWhRecordService;
    @Resource
    private StockRecordDTOConvert stockRecordDTOConvert;
    @Resource
    private StockInRecordDTOConvert stockInRecordDTOConvert;
    @Resource
    private WarehouseRecordCommService warehouseRecordCommService;
    @Resource
    private OrderUtilService orderUtilService;
    @Resource
    private StockRecordFacade stockRecordFacade;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTaskFinishRecord(PackTaskFinishE finishE, PackDemandE demandE) {
        //校验包装数量大于0
        if (finishE.getPackNum().compareTo(BigDecimal.ZERO) < 1) {
            log.info("包装任务完成清单-包装数量错误：{}", finishE.getPackNum());
            throw new RomeException(ResCode.ORDER_ERROR_7708, ResCode.ORDER_ERROR_7708_DESC + "-包装数量不能小于等于0");
        }
        //校验需求单是否存在
        if (null == demandE) {
            log.info("需求单不存在，需求单号：{}", finishE.getRequireCode());
            throw new RomeException(ResCode.ORDER_ERROR_7701, ResCode.ORDER_ERROR_7701_DESC);
        }
        if (!DemandRecordStatusEnum.CONFIRMED.getStatus().equals(demandE.getRecordStatus()) &&
                !DemandRecordStatusEnum.PART_PACK.getStatus().equals(demandE.getRecordStatus())) {
            log.info("需求单状态异常，不为“已确认”“部分包装”");
            throw new RomeException(ResCode.ORDER_ERROR_7706, ResCode.ORDER_ERROR_7706_DESC);
        }
        //查询需求单明细
        List<PackDemandDetailE> packDemandDetailEList = packDemandDetailMapper.queryDemandDetailByRequireCode(demandE.getRecordCode());
        if (CollectionUtils.isEmpty(packDemandDetailEList)) {
            log.info("需求单明细不存在，需求单号：{}", finishE.getRequireCode());
            throw new RomeException(ResCode.ORDER_ERROR_7702, ResCode.ORDER_ERROR_7702_DESC);
        }
        Map<String, List<PackDemandDetailE>> skuCode2demandDetailMap = null;
        if (PackTypeEnum.isCompose(demandE.getPackType())) {
            skuCode2demandDetailMap = packDemandDetailEList.stream().collect(Collectors.groupingBy(demandDetailE -> demandDetailE.getCustomGroupCode()));
        } else {
            skuCode2demandDetailMap = packDemandDetailEList.stream().collect(Collectors.groupingBy(demandDetailE -> demandDetailE.getSkuCode()));
        }
        //获取当前sku对应的明细
        List<PackDemandDetailE> demandDetailEList = skuCode2demandDetailMap.get(finishE.getSkuCode());
        if (CollectionUtils.isEmpty(demandDetailEList)) {
            log.info("任务完成操作单成品编码不存在当前需求单中");
            throw new RomeException(ResCode.ORDER_ERROR_7703, ResCode.ORDER_ERROR_7703_DESC);
        }

        //创建前置单和明细 更新需求单明细中 已包装数量
        savePackTaskFinishFrontRecord(finishE, demandE, demandDetailEList);

        //更新需求单状态 由 已确认 -- >部分包装
        if (DemandRecordStatusEnum.CONFIRMED.getStatus().equals(demandE.getRecordStatus())) {
            packDemandMapper.updatePackDemandPartPacked(demandE.getId());
        }

        // 组装和反拆 生成出入库单 并推库存
        if (PackTypeEnum.PACKAGING.getType().equals(finishE.getPackType()) ||
                PackTypeEnum.UN_PACKAGING.getType().equals(finishE.getPackType())) {
            createWarehouseRecord(finishE, demandDetailEList.get(0));
        }
    }

    //保存包装任务完成清单 前置单和明细
    private void savePackTaskFinishFrontRecord(PackTaskFinishE finishE, PackDemandE demandE, List<PackDemandDetailE> demandDetailEList) {
        String code = orderUtilService.queryOrderCode(FrontRecordTypeEnum.PACKAGE_FINISH_RECORD.getCode());
        finishE.setRecordCode(code);
        finishE.setRecordType(FrontRecordTypeEnum.PACKAGE_FINISH_RECORD.getType());
        finishE.setRecordStatus(FrontRecordStatusEnum.COMPLETE.getStatus());
        finishE.setPackType(demandE.getPackType());
        finishE.setChannelCode(demandE.getChannelCode());
        finishE.setRequireFinishTime(demandE.getDemandDate());
        finishE.setModifier(finishE.getCreator());
        packTaskFinishMapper.saveTaskOperationOrder(finishE);
        //创建明细
        PackTaskFinishDetailE packTaskFinishDetailE = null;
        List<PackTaskFinishDetailE> taskFinishDetailEList = new ArrayList<>();
        for (PackDemandDetailE demandDetailE : demandDetailEList) {
            packTaskFinishDetailE = new PackTaskFinishDetailE();
            packTaskFinishDetailE.setRecordCode(finishE.getRecordCode());
            packTaskFinishDetailE.setSkuCode(demandDetailE.getSkuCode());
            BigDecimal skuQty = finishE.getPackNum();
            if (PackTypeEnum.isCompose(finishE.getPackType())) {
                skuQty = BigDecimal.ZERO;
                if (null != demandDetailE.getRequireQty() && null != demandDetailE.getCompositeQty() &&
                        demandDetailE.getCompositeQty().compareTo(BigDecimal.ZERO) == 1) {
                    skuQty = finishE.getPackNum().multiply(demandDetailE.getRequireQty())
                            .divide(demandDetailE.getCompositeQty(), CommonConstants.DECIMAL_POINT_NUM, BigDecimal.ROUND_DOWN);
                }
            }
            packTaskFinishDetailE.setSkuQty(skuQty);
            packTaskFinishDetailE.setUnit(demandDetailE.getUnit());
            packTaskFinishDetailE.setUnitCode(demandDetailE.getUnitCode());
            taskFinishDetailEList.add(packTaskFinishDetailE);

            if (null == demandDetailE.getActualPackedQty()) {
                demandDetailE.setActualPackedQty(BigDecimal.ZERO);
            }
            demandDetailE.setActualPackedQty(demandDetailE.getActualPackedQty().add(skuQty));
        }
        //更新需求明细单中 实际已包装数量
        packDemandDetailMapper.batchUpdateDemandDetailActualPackedQty(demandDetailEList);
        //新增任务完成单明细
        packTaskFinishDetailMapper.saveTaskFinishDetail(taskFinishDetailEList);
        finishE.setOperationDetails(taskFinishDetailEList);
    }

    //物料变动 创建出入库单 并推库存
    private void createWarehouseRecord(PackTaskFinishE finishE, PackDemandDetailE demandDetailE) {
        if (null == finishE) {
            throw new RomeException(ResCode.ORDER_ERROR_7709, ResCode.ORDER_ERROR_7709_DESC);
        }
        List<PackDemandComponentE> componentList = packDemandComponentMapper.queryDemandComponentByPackedType(demandDetailE.getRecordCode(), demandDetailE.getSkuCode());
        if (CollectionUtils.isEmpty(componentList)) {
            log.info("需求单明细对应的组件为空,需求单明细：{}", demandDetailE.getId());
            throw new RomeException(ResCode.ORDER_ERROR_7704, ResCode.ORDER_ERROR_7704_DESC);
        }
        List<PackTaskFinishDetailE> inFinishDetailList = new ArrayList<>();
        List<PackTaskFinishDetailE> outFinishDetailList = new ArrayList<>();
        //组装品 出库：原料  入库：成品   反拆品 出库：成品  入库：原料
        PackTaskFinishDetailE base = null;
        for (PackDemandComponentE componentE : componentList) {
            base = new PackTaskFinishDetailE();
            base.setSkuCode(componentE.getSkuCode());
            base.setUnit(componentE.getUnit());
            base.setUnitCode(componentE.getUnitCode());
            BigDecimal skuQty = BigDecimal.ZERO;
            if (null != componentE.getRequireQty() && null != demandDetailE.getRequireQty() &&
                    demandDetailE.getRequireQty().compareTo(BigDecimal.ZERO) == 1) {
                skuQty = componentE.getRequireQty().multiply(finishE.getPackNum()).divide(demandDetailE.getRequireQty(), CommonConstants.DECIMAL_POINT_NUM, ROUND_DOWN);
            }
            base.setSkuQty(skuQty);
            if (PackTypeEnum.PACKAGING.getType().equals(finishE.getPackType())) {
                outFinishDetailList.add(base);
            } else {
                inFinishDetailList.add(base);
            }
        }
        for (PackTaskFinishDetailE detailE : finishE.getOperationDetails()) {
            base = new PackTaskFinishDetailE();
            base.setSkuCode(detailE.getSkuCode());
            base.setUnit(detailE.getUnit());
            base.setUnitCode(detailE.getUnitCode());
            base.setSkuQty(detailE.getSkuQty());
            base.setId(detailE.getId());
            if (PackTypeEnum.PACKAGING.getType().equals(finishE.getPackType())) {
                inFinishDetailList.add(base);
            } else {
                outFinishDetailList.add(base);
            }
        }
        //设置商品code或id
        itemInfoTool.convertSkuCode(inFinishDetailList);
        itemInfoTool.convertSkuCode(outFinishDetailList);
        //单位转换
        skuQtyUnitTool.convertRealToBasic(inFinishDetailList);
        skuQtyUnitTool.convertRealToBasic(outFinishDetailList);
        //生成入库单和明细 落本地数据
        WarehouseRecordE inWarehouseRecord = packTaskOperationToWhRecordService.createInWhRecordByTaskOperation(finishE, inFinishDetailList);
        //生成出库单和明细 落本地数据
        WarehouseRecordE outWarehouseRecord = packTaskOperationToWhRecordService.createOutWhRecordByTaskOperation(finishE, outFinishDetailList);
        boolean isSuccess = false;
        //调用库存 创建出库单接口
        if (null != outWarehouseRecord) {
            OutWarehouseRecordDTO outRecord = stockRecordDTOConvert.convertE2OutDTO(outWarehouseRecord);
            for (WarehouseRecordDetailE detail : outWarehouseRecord.getWarehouseRecordDetailList()) {
                outRecord.getDetailList().stream().filter(recordDetail -> recordDetail.getSkuCode().equals(detail.getSkuCode())).forEach(
                        rDetail -> {
                            rDetail.setLineNo(detail.getId() + "");
                            if (StringUtils.isEmpty(rDetail.getDeliveryLineNo())) {
                                rDetail.setDeliveryLineNo(detail.getId() + "");
                            }
                        }
                );
            }
            stockRecordFacade.createOutRecord(outRecord);
        }
        try {
            if (null != inWarehouseRecord) {
                //调用库存接口 创建入库单
                InWarehouseRecordDTO inRecordDto = stockInRecordDTOConvert.convertE2InDTO(inWarehouseRecord);
                for (WarehouseRecordDetailE detail : inWarehouseRecord.getWarehouseRecordDetailList()) {
                    inRecordDto.getDetailList().stream().filter(recordDetail -> recordDetail.getSkuCode().equals(detail.getSkuCode()))
                            .forEach(rDetail -> {
                                rDetail.setLineNo(detail.getId() + "");
                                if (StringUtils.isEmpty(rDetail.getDeliveryLineNo())) {
                                    rDetail.setDeliveryLineNo(detail.getId() + "");
                                }
                            });
                }
                stockRecordFacade.createInRecord(inRecordDto);
            }
            isSuccess = true;
            log.info("taskDetailOperateCode:{}，包装任务完成单处理成功", finishE.getTaskDetailOperateCode());
        } catch (Exception e) {
            log.info("包装任务完成清单，创建入库单异常：", e);
            throw new RomeException(ResCode.ORDER_ERROR_7705, ResCode.ORDER_ERROR_7705_DESC + "-操作单号:" + finishE.getTaskDetailOperateCode());
        } finally {
            if (!isSuccess) {
                //取消出库单
                if (null != outWarehouseRecord) {
                    String msg = String.format("包装任务完成清单-同步入库单异常，操作单号：%s 后置单号：%s 取消出库单",
                            finishE.getTaskDetailOperateCode(), outWarehouseRecord.getRecordCode());
                    log.error(ServiceKibanaLog.getServiceLog(KibanaLogConstants.PACK_TASK_OPERATION, "packTaskOperationFinish", msg, inWarehouseRecord));
                    CancelRecordDTO cancelRecord = new CancelRecordDTO();
                    cancelRecord.setRecordCode(outWarehouseRecord.getRecordCode());
                    cancelRecord.setRecordType(outWarehouseRecord.getRecordType());
                    cancelRecord.setIsForceCancel(YesOrNoEnum.YES.getType());
                    warehouseRecordCommService.cancelWarehouseRecordToStock(cancelRecord);
                }
            }
        }
    }
}
