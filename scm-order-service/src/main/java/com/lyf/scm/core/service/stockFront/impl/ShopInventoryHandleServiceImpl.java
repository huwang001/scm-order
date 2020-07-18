package com.lyf.scm.core.service.stockFront.impl;

import com.lyf.scm.common.constants.KibanaLogConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.FrontRecordStatusEnum;
import com.lyf.scm.common.enums.YesOrNoEnum;
import com.lyf.scm.core.config.ServiceKibanaLog;
import com.lyf.scm.core.domain.convert.stockFront.StockInRecordDTOConvert;
import com.lyf.scm.core.domain.convert.stockFront.StockRecordDTOConvert;
import com.lyf.scm.core.domain.entity.stockFront.ShopInventoryDetailE;
import com.lyf.scm.core.domain.entity.stockFront.ShopInventoryE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.stockFront.ShopInventoryMapper;
import com.lyf.scm.core.remote.item.SkuQtyUnitTool;
import com.lyf.scm.core.remote.stock.dto.*;
import com.lyf.scm.core.remote.stock.facade.StockQueryFacade;
import com.lyf.scm.core.remote.stock.facade.StockRecordFacade;
import com.lyf.scm.core.service.stockFront.ShopInventoryHandleService;
import com.lyf.scm.core.service.stockFront.ShopInventoryToWareHouseRecordService;
import com.lyf.scm.core.service.stockFront.WarehouseRecordCommService;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhanlong
 */
@Slf4j
@Service("shopInventoryHandleService")
public class ShopInventoryHandleServiceImpl implements ShopInventoryHandleService {

    @Resource
    private SkuQtyUnitTool skuQtyUnitTool;
    @Resource
    private ShopInventoryMapper shopInventoryMapper;
    @Resource
    private ShopInventoryToWareHouseRecordService shopInventoryToWareHouseRecordService;
    @Resource
    private StockQueryFacade stockQueryFacade;
    @Resource
    private StockRecordFacade stockRecordFacade;
    @Resource
    private StockRecordDTOConvert stockRecordDTOConvert;
    @Resource
    private StockInRecordDTOConvert stockInRecordDTOConvert;
    @Resource
    private WarehouseRecordCommService warehouseRecordCommService;

    /**
     * 全盘
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void allInventory(ShopInventoryE frontRecord) throws Exception {
        List<ShopInventoryDetailE> outFrontRecordDetails = new ArrayList<>();
        List<ShopInventoryDetailE> inFrontRecordDetails = new ArrayList<>();

        //查询仓库全部库存信息
        List<RealWarehouseStockDTO> realWarehouseStockList = stockQueryFacade.queryRealStockByFactoryAndRealWarehouseCode(
                frontRecord.getFactoryCode(), frontRecord.getRealWarehouseCode());
        List<SkuStockDTO> stockList = new ArrayList<>();
        realWarehouseStockList.forEach(stock -> {
            SkuStockDTO skuStock = new SkuStockDTO();
            skuStock.setSkuId(stock.getSkuId());
            skuStock.setSkuCode(stock.getSkuCode());
            skuStock.setAvailableQty(stock.getRealQty());
            stockList.add(skuStock);
        });
        List<Long> skuIds = frontRecord.getFrontRecordDetails().stream().map(ShopInventoryDetailE::getSkuId).collect(Collectors.toList());
        //库存数量不在全盘中置为0
        stockList.stream().filter(stock -> !skuIds.contains(stock.getSkuId())).forEach(stock -> {
            ShopInventoryDetailE detailE = new ShopInventoryDetailE();
            detailE.setSkuCode(stock.getSkuCode() == null ? "" : stock.getSkuCode());
            detailE.setSkuId(stock.getSkuId());
            detailE.setSkuQty(stock.getAvailableQty().abs());
            if (stock.getAvailableQty().compareTo(new BigDecimal(0.0)) == 1) {
                outFrontRecordDetails.add(detailE);
                stock.setAvailableQty(new BigDecimal(0.0));
            }
            if (stock.getAvailableQty().compareTo(new BigDecimal(0.0)) == -1) {
                inFrontRecordDetails.add(detailE);
                stock.setAvailableQty(new BigDecimal(0.0));
            }
        });
        //计算库存差异
        gainDifferStockQty(frontRecord.getBusinessType(), stockList, outFrontRecordDetails, inFrontRecordDetails, frontRecord);
        //生成单据并扣减库存
        this.createWarehouseRecord(outFrontRecordDetails, inFrontRecordDetails, frontRecord);
    }

    /**
     * 账面库存盘点
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void accountInventory(ShopInventoryE frontRecord) throws Exception {
        List<ShopInventoryDetailE> outFrontRecordDetails = new ArrayList<>();
        List<ShopInventoryDetailE> inFrontRecordDetails = new ArrayList<>();
        frontRecord.getFrontRecordDetails().forEach(detail -> detail.setSkuQty(detail.getAccQty()));
        //查询基础单位，单位转换
        skuQtyUnitTool.queryBasicUnit(frontRecord.getFrontRecordDetails());
        //查询商品 库存 数量（从库存中心查询）
        QueryRealStockDTO queryRealStock = new QueryRealStockDTO();
        queryRealStock.setFactoryCode(frontRecord.getFactoryCode());
        queryRealStock.setWarehouseOutCode(frontRecord.getRealWarehouseCode());
        List<BaseSkuInfoDTO> baseSkuInfoDTOS = new ArrayList<>();
        frontRecord.getFrontRecordDetails().forEach(detail -> {
            BaseSkuInfoDTO skuInfo = new BaseSkuInfoDTO();
            skuInfo.setSkuId(detail.getSkuId());
            baseSkuInfoDTOS.add(skuInfo);
        });
        queryRealStock.setBaseSkuInfoDTOS(baseSkuInfoDTOS);
        List<SkuStockDTO> stockList = stockQueryFacade.queryRealStockBySkuInfo(queryRealStock);
        //计算库存差异
        this.gainDifferStockQty(frontRecord.getBusinessType(), stockList, outFrontRecordDetails, inFrontRecordDetails, frontRecord);
        //生成单据并扣减库存
        this.createWarehouseRecord(outFrontRecordDetails, inFrontRecordDetails, frontRecord);
    }

    /**
     * 盘点库存差异
     */
    private void gainDifferStockQty(Integer type, List<SkuStockDTO> stockList, List<ShopInventoryDetailE> outFrontRecordDetails,
                                    List<ShopInventoryDetailE> inFrontRecordDetails, ShopInventoryE frontRecord) {
        for (ShopInventoryDetailE shopInventoryRecordDetailE : frontRecord.getFrontRecordDetails()) {
            BigDecimal differenceValue;
            SkuStockDTO stockE = stockList.stream().filter(stock -> stock.getSkuId().equals(shopInventoryRecordDetailE.getSkuId())).findFirst().orElse(null);
            if (stockE == null) {
                //门店仓没有库存，盘新库存
                differenceValue = type == 9 ? shopInventoryRecordDetailE.getAccQty() : shopInventoryRecordDetailE.getSkuQty();
            } else if (type == 9) {
                //账面库存盘点，账面库存计算盘差
                differenceValue = shopInventoryRecordDetailE.getAccQty().subtract(stockE.getAvailableQty());
            } else {
                //全盘盘点，实盘数量计算盘差
                differenceValue = shopInventoryRecordDetailE.getSkuQty().subtract(stockE.getAvailableQty());
            }
            ShopInventoryDetailE detailE = new ShopInventoryDetailE();
            detailE.setId(shopInventoryRecordDetailE.getId());
            //库存大于0，增加库存
            if (differenceValue.compareTo(new BigDecimal(0.0)) == 1) {
                detailE.setSkuId(shopInventoryRecordDetailE.getSkuId());
                detailE.setSkuCode(shopInventoryRecordDetailE.getSkuCode() == null ? "" : shopInventoryRecordDetailE.getSkuCode());
                detailE.setSkuQty(differenceValue);
                inFrontRecordDetails.add(detailE);
            }
            //库存小于0，减少库存
            if (differenceValue.compareTo(new BigDecimal(0.0)) == -1) {
                detailE.setSkuId(shopInventoryRecordDetailE.getSkuId());
                detailE.setSkuCode(shopInventoryRecordDetailE.getSkuCode() == null ? "" : shopInventoryRecordDetailE.getSkuCode());
                detailE.setSkuQty(differenceValue.abs());
                outFrontRecordDetails.add(detailE);
            }
            if (outFrontRecordDetails.size() > 0) {
                //查询基础单位
                skuQtyUnitTool.queryBasicUnitNoChecked(outFrontRecordDetails);
            }
            if (inFrontRecordDetails.size() > 0) {
                //查询基础单位
                skuQtyUnitTool.queryBasicUnitNoChecked(inFrontRecordDetails);
            }
        }
    }

    /**
     * 生成出入库单并扣减库存
     */
    private void createWarehouseRecord(List<ShopInventoryDetailE> outFrontRecordDetails, List<ShopInventoryDetailE> inFrontRecordDetails, ShopInventoryE frontRecord) throws Exception {
        if (null == frontRecord) {
            throw new RomeException(ResCode.STOCK_ERROR_7204, ResCode.STOCK_ERROR_7204_DESC);
        }
        boolean isSuccess = false;
        WarehouseRecordE outWarehouseRecord = null;
        WarehouseRecordE inWarehouseRecord = null;
        //实盘数量与门店实仓数量比较，有差异产生出入库单
        if (outFrontRecordDetails != null && outFrontRecordDetails.size() > 0) {
            //基础单位赋值
            this.unitConvertBasicUnit(outFrontRecordDetails);
            frontRecord.setFrontRecordDetails(outFrontRecordDetails);
            //生成出库单
            outWarehouseRecord = shopInventoryToWareHouseRecordService.createOutWarehouseRecordByShopInventory(frontRecord);
        }
        if (inFrontRecordDetails != null && inFrontRecordDetails.size() > 0) {
            //基础单位赋值
            this.unitConvertBasicUnit(inFrontRecordDetails);
            frontRecord.setFrontRecordDetails(inFrontRecordDetails);
            //生成入库单
            inWarehouseRecord = shopInventoryToWareHouseRecordService.createInWarehouseRecordByShopInventory(frontRecord);
        }
        //修改前置状态为完成
        shopInventoryMapper.updateCompleteStatus(frontRecord.getId(), FrontRecordStatusEnum.INIT.getStatus(), FrontRecordStatusEnum.COMPLETE.getStatus());
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
            log.info("id:{}，盘点单处理成功", frontRecord.getId());
        } catch (RomeException e) {
            log.info("门店盘点RomeException错误，同步入库单异常：", e);
            throw new RomeException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.info("门店盘点Exception错误，同步入库单异常：", e);
            throw new Exception();
        } finally {
            if (!isSuccess) {
                //取消出库单
                if (null != outWarehouseRecord) {
                    String msg = String.format("门店盘点-同步入库单异常，外部单据号：%s 后置单号：%s 取消出库单",
                            frontRecord.getOutRecordCode(), outWarehouseRecord.getRecordCode());
                    log.error(ServiceKibanaLog.getServiceLog(KibanaLogConstants.SHOP_INVENTORY_HANDLE, "createWarehouseRecord", msg, frontRecord));
                    CancelRecordDTO cancelRecord = new CancelRecordDTO();
                    cancelRecord.setRecordCode(outWarehouseRecord.getRecordCode());
                    cancelRecord.setRecordType(outWarehouseRecord.getRecordType());
                    cancelRecord.setIsForceCancel(YesOrNoEnum.YES.getType());
                    warehouseRecordCommService.cancelWarehouseRecordToStock(cancelRecord);
                }
            }
        }
    }

    /**
     * 基础单位赋值
     *
     * @param details
     */
    public void unitConvertBasicUnit(List<ShopInventoryDetailE> details) {
        details.forEach(detail -> {
            detail.setBasicSkuQty(detail.getSkuQty());
            detail.setBasicUnit(detail.getUnit());
            detail.setBasicUnitCode(detail.getUnitCode());
        });
    }
}
