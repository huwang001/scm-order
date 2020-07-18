package com.lyf.scm.core.service.online.impl;

import com.lyf.scm.core.domain.entity.online.RecordPoolE;
import com.lyf.scm.core.domain.entity.online.SaleE;
import com.lyf.scm.core.mapper.online.RecordPoolDetailMapper;
import com.lyf.scm.core.mapper.online.RecordPoolMapper;
import com.lyf.scm.core.remote.stock.dto.CoreVirtualStockOpDO;
import com.lyf.scm.core.service.online.RecordPoolService;
import com.lyf.scm.core.service.order.OrderUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service("recordPoolService")
public class RecordPoolServiceImpl implements RecordPoolService {

    @Resource
    private OrderUtilService orderUtilService;
    @Resource
    private RecordPoolMapper recordPoolMapper;
    @Resource
    private RecordPoolDetailMapper recordPoolDetailMapper;

    @Override
    public RecordPoolE saveRecordPool(SaleE onlineRetailE, int needCombine, Long rwId, Long vwId, List<CoreVirtualStockOpDO> cvsList) {
        RecordPoolE poolE = new RecordPoolE();
        //生成do单号和recordType
//        poolE.setRecordType(WarehouseRecordTypeEnum.POOL_DO_RECORD.getType());
//        poolE.setDoCode(orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.POOL_DO_RECORD.getCode()));
//        poolE.setFrontRecordId(onlineRetailE.getId());
//        poolE.setFrontRecordCode(onlineRetailE.getRecordCode());
//        poolE.setChannelCode(onlineRetailE.getChannelCode());
//        poolE.setRealWarehouseId(rwId);
//        poolE.setVirtualWarehouseId(vwId);
//        poolE.setMerchantId(onlineRetailE.getMerchantId());
//        //初始
//        poolE.setRecordStatus(RwRecordPoolStatusEnum.INIT.getStatus());
//        poolE.setNeedCombine(needCombine);
//        if (poolE.getSyncStatus() == null) {
//            poolE.setSyncStatus(0);
//        }
        //保存子do单
        recordPoolMapper.insertRecordPool(poolE);
        //保存明细
        saveRecordPoolDetail(cvsList, onlineRetailE, poolE);
        return poolE;
    }

    /**
     * 保存子do单 和 明细 信息
     *
     * @param cvsList
     * @param onlineRetailE
     * @param poolE
     */
    private void saveRecordPoolDetail(List<CoreVirtualStockOpDO> cvsList, SaleE onlineRetailE, RecordPoolE poolE) {
//        //同一个skuId的基础单位相同，可以选取一个放入缓存中给后面使用
//        Map<Long, SaleDetailE> skuIdUnitCodeMap = new HashMap<>();
//        for (SaleDetailE detailE : onlineRetailE.getFrontRecordDetails()) {
//            skuIdUnitCodeMap.putIfAbsent(detailE.getSkuId(), detailE);
//        }
//        List<RecordPoolDetailE> recordPoolDetailEList = new ArrayList<>();
//        CoreVirtualStockOpDO master = cvsList.get(0);
//        RecordPoolDetailE poolDetailE;
//        for (CoreVirtualStockOpDO cvs : cvsList) {
//            SaleDetailE frontDetailE = skuIdUnitCodeMap.get(cvs.getSkuId());
//            AlikAssert.isNotNull(frontDetailE, ResCode.STOCK_ERROR_5024, ResCode.STOCK_ERROR_5024_DESC);
//            poolDetailE = new RecordPoolDetailE();
//            poolDetailE.setSkuId(cvs.getSkuId());
//            poolDetailE.setSkuCode(cvs.getSkuCode());
//            //基本单位转为销售单位
//            poolDetailE.setSkuQty(cvs.getLockQty().divide(frontDetailE.getScale(), CommonConstants.DECIMAL_POINT_NUM, ROUND_DOWN));
//            poolDetailE.setUnit(frontDetailE.getUnit());
//            poolDetailE.setUnitCode(frontDetailE.getUnitCode());
//            poolDetailE.setBasicSkuQty(cvs.getLockQty());
//            poolDetailE.setBasicUnit(frontDetailE.getBasicUnit());
//            poolDetailE.setBasicUnitCode(frontDetailE.getBasicUnitCode());
//            poolDetailE.setRealWarehouseId(master.getRealWarehouseId());
//            poolDetailE.setVirtualWarehouseId(master.getVirtualWarehouseId());
//            poolDetailE.setRecordPoolId(poolE.getId());
//            poolDetailE.setDoCode(poolE.getDoCode());
//            recordPoolDetailEList.add(poolDetailE);
//        }
//        //批量插入
//        recordPoolDetailMapper.insertAllRwRecordPoolDetail(recordPoolDetailEList);
    }
}
