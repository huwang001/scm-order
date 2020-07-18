package com.lyf.scm.core.service.reverse.impl;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.WarehouseRecordBatchStatusEnum;
import com.lyf.scm.common.enums.WarehouseRecordBusinessTypeEnum;
import com.lyf.scm.common.enums.WarehouseRecordStatusEnum;
import com.lyf.scm.common.enums.WarehouseRecordTypeEnum;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.domain.convert.reverse.ReverseDetailConvert;
import com.lyf.scm.core.domain.convert.stockFront.StockInRecordDTOConvert;
import com.lyf.scm.core.domain.convert.stockFront.StockRecordDTOConvert;
import com.lyf.scm.core.domain.entity.reverse.ReverseDetailE;
import com.lyf.scm.core.domain.entity.reverse.ReverseE;
import com.lyf.scm.core.domain.entity.stockFront.FrontWarehouseRecordRelationE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.reverse.ReverseDetailMapper;
import com.lyf.scm.core.mapper.stockFront.FrontWarehouseRecordRelationMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordDetailMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.remote.item.ItemInfoTool;
import com.lyf.scm.core.remote.item.SkuQtyUnitTool;
import com.lyf.scm.core.remote.stock.dto.InWarehouseRecordDTO;
import com.lyf.scm.core.remote.stock.dto.OutWarehouseRecordDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.remote.stock.facade.StockRecordFacade;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.reverse.ReverseOperationToWhRecordService;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Desc:
 * @author:Huangyl
 * @date: 2020/7/17
 */
@Slf4j
@Service("reverseOperationToWhRecordService")
public class ReverseOperationToWhRecordServiceImpl implements ReverseOperationToWhRecordService {


    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;

    @Resource
    private ReverseDetailMapper reverseDetailMapper;

    @Resource
    private OrderUtilService orderUtilService;

    @Resource
    private ItemInfoTool itemInfoTool;

    @Resource
    private SkuQtyUnitTool skuQtyUnitTool;

    @Resource
    private StockRecordDTOConvert stockRecordDTOConvert;

    @Resource
    private StockRecordFacade stockRecordFacade;

    @Resource
    private StockInRecordDTOConvert stockInRecordDTOConvert;

    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;

    @Resource
    private WarehouseRecordDetailMapper warehouseRecordDetailMapper;

    @Resource
    private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;

    /**
     * 生成出库单和明细 落本地数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WarehouseRecordE createOutWhRecordByTaskOperation(ReverseE reverseE) {
        WarehouseRecordE warehouseRecord = new WarehouseRecordE();
        WarehouseRecordTypeEnum type = WarehouseRecordTypeEnum.REVERSE_OUT_RECORD;
        String code = orderUtilService.queryOrderCode(type.getCode());
        warehouseRecord.setRecordCode(code);
        // 出库单
        warehouseRecord.setBusinessType(WarehouseRecordBusinessTypeEnum.OUT_WAREHOUSE_RECORD.getType());
        warehouseRecord.setRecordType(type.getType());
        warehouseRecord.setOutCreateTime(new Date());
        //
        //warehouseRecord.setChannelCode(warehouseRecordE.getChannelCode());
        //是否需要过账
        //warehouseRecord.setSyncTransferStatus(WarehouseRecordConstant.INIT_SYNC_TRADE);
        warehouseRecord.setBatchStatus(WarehouseRecordBatchStatusEnum.INIT.getStatus());
        //状态为已出库状态
        warehouseRecord.setRecordStatus(WarehouseRecordStatusEnum.OUT_ALLOCATION.getStatus());
        warehouseRecord.setFactoryCode(reverseE.getFactoryCode());
        warehouseRecord.setRealWarehouseCode(reverseE.getRealWarehouseCode());
        RealWarehouse realWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(reverseE.getRealWarehouseCode(),reverseE.getFactoryCode());
        AlikAssert.isNotNull(realWarehouse, ResCode.ORDER_ERROR_8008, ResCode.ORDER_ERROR_8008_DESC + reverseE.getRealWarehouseCode()+","+reverseE.getFactoryCode());
        warehouseRecord.setRealWarehouseId(realWarehouse.getId());
        //构建后置单明细
        warehouseRecord.setWarehouseRecordDetailList(this.createRecordDetailByActualFrontRecord(reverseE.getRecordCode()));
        this.addWarehouseRecord(warehouseRecord,reverseE);

        //调用库存接口 创建出库库单 同步库存中心
        OutWarehouseRecordDTO outRecord = stockRecordDTOConvert.convertE2OutDTO(warehouseRecord);
        for (WarehouseRecordDetailE detail : warehouseRecord.getWarehouseRecordDetailList()) {
            outRecord.getDetailList().stream().filter(recordDetail -> recordDetail.getSkuCode().equals(detail.getSkuCode())).forEach(
                    rDetail -> { rDetail.setLineNo(detail.getId() + "");
                        if (StringUtils.isEmpty(rDetail.getDeliveryLineNo())) {
                            rDetail.setDeliveryLineNo(detail.getId() + "");
                        }
                    }
            );
        }
        stockRecordFacade.createOutRecord(outRecord);
        return warehouseRecord;
    }

    /**
     * 生成入库单和明细 落本地数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WarehouseRecordE createInWhRecordByTaskOperation(ReverseE reverseE) {
        WarehouseRecordE warehouseRecord = new WarehouseRecordE();
        WarehouseRecordTypeEnum type = WarehouseRecordTypeEnum.REVERSE_IN_RECORD;
        String code = orderUtilService.queryOrderCode(type.getCode());
        warehouseRecord.setRecordCode(code);
        // 入库单
        warehouseRecord.setBusinessType(WarehouseRecordBusinessTypeEnum.IN_WAREHOUSE_RECORD.getType());
        warehouseRecord.setRecordType(type.getType());
        warehouseRecord.setOutCreateTime(new Date());
        //warehouseRecord.setChannelCode(finishE.getChannelCode());
        //是否需要过账
        //warehouseRecord.setSyncTransferStatus(WarehouseRecordConstant.INIT_SYNC_TRADE);
        warehouseRecord.setBatchStatus(WarehouseRecordBatchStatusEnum.INIT.getStatus());
        //状态为已入库状态
        warehouseRecord.setRecordStatus(WarehouseRecordStatusEnum.IN_ALLOCATION.getStatus());
        warehouseRecord.setFactoryCode(reverseE.getFactoryCode());
        warehouseRecord.setRealWarehouseCode(reverseE.getRealWarehouseCode());
        RealWarehouse realWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(reverseE.getRealWarehouseCode(),reverseE.getFactoryCode());
        AlikAssert.isNotNull(realWarehouse, ResCode.ORDER_ERROR_8008, ResCode.ORDER_ERROR_8008_DESC + reverseE.getRealWarehouseCode()+","+reverseE.getFactoryCode());
        warehouseRecord.setRealWarehouseId(realWarehouse.getId());
        //构建后置单明细
        warehouseRecord.setWarehouseRecordDetailList(this.createRecordDetailByActualFrontRecord(reverseE.getRecordCode()));
        //保存入库单 详情 关系
        addWarehouseRecord(warehouseRecord, reverseE);

        //调用库存接口 创建入库单 同步库存中心
        InWarehouseRecordDTO inRecordDto = stockInRecordDTOConvert.convertE2InDTO(warehouseRecord);
        for (WarehouseRecordDetailE detail : warehouseRecord.getWarehouseRecordDetailList()) {
            inRecordDto.getDetailList().stream().filter(recordDetail -> recordDetail.getSkuCode().equals(detail.getSkuCode()))
                    .forEach(rDetail -> {
                        rDetail.setLineNo(detail.getId() + "");
                        if (StringUtils.isEmpty(rDetail.getDeliveryLineNo())) {
                            rDetail.setDeliveryLineNo(detail.getId() + "");
                        }
                    });
        }
        stockRecordFacade.createInRecord(inRecordDto);
        return warehouseRecord;
    }

    /**
     * 根据前置单明细-生成后置单明细
     *
     * @param recordCode
     * @return
     */
    public List<WarehouseRecordDetailE> createRecordDetailByActualFrontRecord(String recordCode) {

        List<ReverseDetailE> reverseDetailES = reverseDetailMapper.queryByRecordCode(recordCode);
        if (CollectionUtils.isEmpty(reverseDetailES)){
            throw new RomeException(ResCode.ORDER_ERROR_8009, ResCode.ORDER_ERROR_8009_DESC+recordCode);
        }
        reverseDetailES.forEach(reverseDetailE -> {
            reverseDetailE.setSkuQty(reverseDetailE.getReverseQty());
        });
        //设置商品code或id
        itemInfoTool.convertSkuCode(reverseDetailES);
        //单位转换
        skuQtyUnitTool.convertRealToBasic(reverseDetailES);

        return reverseDetailES.stream().map(reverseDetailE -> {
            WarehouseRecordDetailE warehouseRecordDetail = new WarehouseRecordDetailE();
            warehouseRecordDetail.setSkuId(reverseDetailE.getSkuId());
            warehouseRecordDetail.setUnit(reverseDetailE.getBasicUnit());
            warehouseRecordDetail.setUnitCode(reverseDetailE.getBasicUnitCode());
            warehouseRecordDetail.setSkuCode(reverseDetailE.getSkuCode());
            warehouseRecordDetail.setPlanQty(reverseDetailE.getBasicSkuQty());
            //需要回调时 设置为0
            warehouseRecordDetail.setActualQty(BigDecimal.ZERO);
            //后置单明细交货单行号 = 前置单明细Id
            warehouseRecordDetail.setDeliveryLineNo(String.valueOf(reverseDetailE.getId()));
            return warehouseRecordDetail;
        }).collect(Collectors.toList());
    }


    /**
     * 保存出库单(入库单) 明细 关系
     *
     * @param warehouseRecord
     * @param reverseE
     */
    private void addWarehouseRecord(WarehouseRecordE warehouseRecord, ReverseE reverseE) {
        //保存后置单
        warehouseRecordMapper.insertWarehouseRecord(warehouseRecord);

        //保存后置单明细
        List<WarehouseRecordDetailE> recordDetailEList = warehouseRecord.getWarehouseRecordDetailList();
        recordDetailEList.forEach(record -> {
            record.setWarehouseRecordId(warehouseRecord.getId());
            record.setRecordCode(warehouseRecord.getRecordCode());
        });
        warehouseRecordDetailMapper.insertWarehouseRecordDetails(recordDetailEList);

        //保存前置单 + 后置单关系
        FrontWarehouseRecordRelationE relation = new FrontWarehouseRecordRelationE();
        //前置单的类型
        relation.setFrontRecordType(warehouseRecord.getRecordType());
        // 后置单单号
        relation.setRecordCode(warehouseRecord.getRecordCode());
        //前置单冲销单单号
        relation.setFrontRecordCode(reverseE.getRecordCode());
        //后置单id
        relation.setWarehouseRecordId(warehouseRecord.getId());
        //前置单冲销单id
        relation.setFrontRecordId(reverseE.getId());

        frontWarehouseRecordRelationMapper.insertFrontWarehouseRecordRelation(relation);
    }
}
