package com.lyf.scm.core.service.stockFront.impl;

import com.lyf.scm.common.constants.WarehouseRecordConstant;
import com.lyf.scm.common.enums.*;
import com.lyf.scm.core.domain.entity.stockFront.ShopSaleDetailE;
import com.lyf.scm.core.domain.entity.stockFront.ShopSaleE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordDetailMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.remote.item.ItemInfoTool;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.stockFront.FrontWarehouseRecordRelationService;
import com.lyf.scm.core.service.stockFront.ShopSaleToWareHouseRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanlong
 */
@Slf4j
@Service("shopSaleToWareHouseRecordService")
public class ShopSaleToWareHouseRecordServiceImpl implements ShopSaleToWareHouseRecordService {

    @Resource
    private ItemInfoTool itemInfoTool;
    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;
    @Resource
    private WarehouseRecordDetailMapper warehouseRecordDetailMapper;
    @Resource
    private FrontWarehouseRecordRelationService frontWarehouseRecordRelationService;
    @Resource
    private OrderUtilService orderUtilService;

    @Override
    public WarehouseRecordE createWarehouseRecordByShopSale(ShopSaleE frontRecordE) {
        WarehouseRecordE warehouseRecordE = new WarehouseRecordE();
        String code = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.SHOP_RETAIL_WAREHOUSE_RECORD.getCode());
        warehouseRecordE.setRecordCode(code);
        warehouseRecordE.setRealWarehouseId(frontRecordE.getRealWarehouseId());
        warehouseRecordE.setFactoryCode(frontRecordE.getFactoryCode());
        warehouseRecordE.setRealWarehouseCode(frontRecordE.getRealWarehouseCode());
        warehouseRecordE.setBusinessType(WarehouseRecordBusinessTypeEnum.OUT_WAREHOUSE_RECORD.getType());
        warehouseRecordE.setRecordType(WarehouseRecordTypeEnum.SHOP_RETAIL_WAREHOUSE_RECORD.getType());
        warehouseRecordE.setChannelCode(frontRecordE.getChannelCode());
        warehouseRecordE.setMerchantId(frontRecordE.getMerchantId());
        warehouseRecordE.setOutCreateTime(frontRecordE.getOutCreateTime());
        warehouseRecordE.setUserCode(frontRecordE.getUserCode() == null ? "" : frontRecordE.getUserCode());
        warehouseRecordE.setMobile(frontRecordE.getMobile() == null ? "" : frontRecordE.getMobile());

        List<ShopSaleDetailE> frontRecordDetails = frontRecordE.getFrontRecordDetails();
        List<WarehouseRecordDetailE> warehouseRecordDetailList = new ArrayList<>();
        if (frontRecordDetails != null) {
            for (ShopSaleDetailE detailE : frontRecordDetails) {
                WarehouseRecordDetailE warehouseRecordDetail = createRecordDetailByFrontRecord(detailE);
                //后置单明细交货单行号 = 前置单明细ID
                warehouseRecordDetail.setDeliveryLineNo(String.valueOf(detailE.getId()));
                warehouseRecordDetailList.add(warehouseRecordDetail);
            }
            //设置skuCode和skuID
            itemInfoTool.convertSkuCode(warehouseRecordDetailList);
            warehouseRecordE.setWarehouseRecordDetailList(warehouseRecordDetailList);
        }
        warehouseRecordE.setRecordStatus(WarehouseRecordStatusEnum.OUT_ALLOCATION.getStatus());
        //需要处理批次库存
        warehouseRecordE.setBatchStatus(WarehouseRecordBatchStatusEnum.INIT.getStatus());
        //创建门店销售出库单
        if (StringUtils.isBlank(warehouseRecordE.getMobile())) {
            warehouseRecordE.setMobile("");
        }
        if (StringUtils.isBlank(warehouseRecordE.getUserCode())) {
            warehouseRecordE.setUserCode("");
        }
        //创建后置单
        if (warehouseRecordE.getSyncTransferStatus() == null) {
            warehouseRecordE.setSyncTransferStatus(WarehouseRecordConstant.INIT_TRANSFER);
        }
        warehouseRecordMapper.insertWarehouseRecord(warehouseRecordE);

        //创建后置单明细
        warehouseRecordE.getWarehouseRecordDetailList().forEach(record -> {
            record.setWarehouseRecordId(warehouseRecordE.getId());
            record.setRecordCode(warehouseRecordE.getRecordCode());
        });
        warehouseRecordDetailMapper.insertWarehouseRecordDetails(warehouseRecordE.getWarehouseRecordDetailList());

        //保存前置单 + 后置单关系
        frontWarehouseRecordRelationService.saveAddFrontRecordAndWarehouseRelation(warehouseRecordE, frontRecordE);
        return warehouseRecordE;
    }

    /**
     * 根据前置单生成单据明细
     */
    private WarehouseRecordDetailE createRecordDetailByFrontRecord(ShopSaleDetailE shopSaleDetailE) {
        WarehouseRecordDetailE warehouseRecordDetailE = new WarehouseRecordDetailE();
        warehouseRecordDetailE.setSkuId(shopSaleDetailE.getSkuId());
        warehouseRecordDetailE.setSkuCode(shopSaleDetailE.getSkuCode());
        //此处设置计划数量和实际数量一致，wms回调再更新实际数量，无需wms回调的业务，直接设置成一致即可
        warehouseRecordDetailE.setPlanQty(shopSaleDetailE.getBasicSkuQty());
        warehouseRecordDetailE.setUnit(shopSaleDetailE.getBasicUnit());
        warehouseRecordDetailE.setUnitCode(shopSaleDetailE.getBasicUnitCode());
        warehouseRecordDetailE.setActualQty(shopSaleDetailE.getBasicSkuQty());
        return warehouseRecordDetailE;
    }
}
