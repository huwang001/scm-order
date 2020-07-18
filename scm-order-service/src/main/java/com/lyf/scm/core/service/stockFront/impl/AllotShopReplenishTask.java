package com.lyf.scm.core.service.stockFront.impl;

import com.alibaba.fastjson.JSONObject;
import com.lyf.scm.common.constants.AllocationConstant;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.constants.StockCoreConstant;
import com.lyf.scm.common.enums.AllotTypeEnum;
import com.lyf.scm.common.enums.YesOrNoEnum;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.stockFront.ShopReplenishAllotDTO;
import com.lyf.scm.core.config.RedisUtil;
import com.lyf.scm.core.domain.convert.stockFront.AllocationCalQtyConvert;
import com.lyf.scm.core.domain.entity.stockFront.ReplenishAllotLogE;
import com.lyf.scm.core.domain.entity.stockFront.ReplenishDetailE;
import com.lyf.scm.core.domain.entity.stockFront.ReplenishRecordE;
import com.lyf.scm.core.mapper.stockFront.ReplenishAllotLogMapper;
import com.lyf.scm.core.remote.base.dto.StoreDTO;
import com.lyf.scm.core.remote.base.facade.BaseFacade;
import com.lyf.scm.core.remote.item.dto.SkuUnitExtDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.stock.dto.*;
import com.lyf.scm.core.remote.stock.facade.StockQueryFacade;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.service.stockFront.AllianceBusinessReplenishService;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ROUND_DOWN;

/**
 * 类AllotShopReplenishTask的实现描述：门店订单寻源
 *
 * @author sunyj 2019/5/7 15:05
 */
@Service
@Slf4j
public class AllotShopReplenishTask {

    @Resource
    private ReplenishAllotLogMapper replenishAllotLogMapper;
    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private StockQueryFacade stockQueryFacade;
    @Resource
    private AllianceBusinessReplenishService allianceBusinessReplenishService;
    @Resource
    private BaseFacade baseFacade;
    @Resource
    private AllocationCalQtyConvert allocationCalQtyConvert;
    @Resource
    private ItemFacade itemFacade;

    /**
     * @Description: 异步寻源分配 <br>
     *
     * @Author chuwenchao 2020/6/15
     * @param orderList 门店补货单
     * @param key       分布式锁定
     * @param allotType 寻源类型
     * @param shopReplenishAllotDTO 寻源参数
     * @return
     */
    @Async
    public void executeAsyncTask(List<ReplenishRecordE> orderList, String key, AllotTypeEnum allotType, int times, ShopReplenishAllotDTO shopReplenishAllotDTO){
        int successRecords = 0;
        Long logId = 0L ;
        Map<String, List<ReplenishRecordE>> recordMap = new HashMap<>(30);
        try {
            //写入寻源日志
            logId = this.saveReplenishAllotLog(orderList.size(), allotType, times, shopReplenishAllotDTO);
            //设置虚仓组ID
            if(CollectionUtils.isNotEmpty(orderList)) {
                List<String> channelCodeList = orderList.stream().map(ReplenishRecordE::getChannelCode).distinct().collect(Collectors.toList());
                List<ChannelSales> channelSalesList = stockRealWarehouseFacade.queryByChannelCodes(channelCodeList);
                Map<String, ChannelSales> channelSalesMap = channelSalesList.stream().collect(Collectors.toMap(ChannelSales::getChannelCode, Function.identity(), (v1, v2) -> v1));
                for (ReplenishRecordE recordE : orderList) {
                    ChannelSales channelSales = channelSalesMap.get(recordE.getChannelCode());
                    if(channelSales != null){
                        recordE.setVirtualWarehouseGroupCode(channelSales.getVirtualWarehouseGroupCode());
                    }
                }
                recordMap = orderList.stream().collect(Collectors.groupingBy(ReplenishRecordE::getVirtualWarehouseGroupCode));
                for(Map.Entry<String, List<ReplenishRecordE>> entry : recordMap.entrySet()) {
                    List<ReplenishRecordE> recordList = entry.getValue();
                    //开启分配
                    int successCount=this.allotByChannelCode(recordList, entry.getKey());
                    successRecords += successCount;
                }
            } else {
                log.info("待寻源单据为空，寻源结束");
            }
        }catch (Exception e){
            log.error("执行寻源任务失败", e);
            throw new RomeException(ResCode.ORDER_ERROR_5118, ResCode.ORDER_ERROR_5118_DESC);
        } finally {
            replenishAllotLogMapper.updateSuccessRecords(logId, successRecords, new Date());
            boolean rs = redisUtil.unLock(key, "1");
            log.info("寻源任务结束,是否锁结果==>{}", rs);
            //释放一下
            orderList.clear();
            recordMap.clear();
        }
    }

    /**
     * @Description: 按照渠道寻源生成出入库单 <br>
     *
     * @Author chuwenchao 2020/6/16
     * @param orderList
     * @param virtualWarehouseGroupCode
     * @return 
     */
    private int allotByChannelCode(List<ReplenishRecordE> orderList, String virtualWarehouseGroupCode) {
        //查询门店信息
        List<String> shopCodes = orderList.stream().map(ReplenishRecordE::getShopCode).distinct().collect(Collectors.toList());
        List<StoreDTO> storeList = baseFacade.searchByCodeList(shopCodes);
        Map<String, StoreDTO> storeMap = storeList.stream().collect(Collectors.toMap(StoreDTO::getCode, Function.identity(), (v1, v2) -> v1));
        // 查询渠道是否存在
        List<String> vwGroupCodes = new ArrayList<>();
        vwGroupCodes.add(virtualWarehouseGroupCode);
        List<VirtualWarehouse> vwList = stockRealWarehouseFacade.queryVmListByVmGroupCodes(vwGroupCodes);
        if(CollectionUtils.isEmpty(vwList)) {
            log.info("部分单据寻源异常跳过，策略组【】不存在，单据信息 ==> {}", virtualWarehouseGroupCode, JSONObject.toJSON(orderList.stream().map(ReplenishRecordE::getRecordCode).collect(Collectors.toList())));
            return 0;
        }
        //只考虑一个虚仓的场景
        String vmCode = vwList.get(0).getVirtualWarehouseCode();
        RealWarehouse realWarehouse = stockRealWarehouseFacade.queryRealWarehouseByVmCode(vmCode);
        //1.根据明细算出所有sku并设置门店库存
        Map<String, ReplenishDetailE> skuCodes = new HashMap<>();
        List<ReplenishDetailE> allDetail = new ArrayList<>();
        for (ReplenishRecordE order : orderList){
            List<ReplenishDetailE> replenishDetailEList = order.getFrontRecordDetails();
            if(CollectionUtils.isEmpty(replenishDetailEList)) {
                log.info("直营门店补货单【{}】寻源无明细，跳过", order.getRecordCode());
                continue;
            }
            //查询门店库存
            Map<String, SkuStockDTO> skuStockMap = this.queryStockByOrder(order, replenishDetailEList);
            //构造单据明细
            for(ReplenishDetailE detailE : replenishDetailEList) {
                detailE.setShopCode(order.getShopCode());
                detailE.setRecordType(order.getRecordType());
                detailE.setChannelCode(order.getChannelCode());
                detailE.setSapPoNo(order.getSapPoNo());
                if(detailE.getAllotQty().compareTo(detailE.getSkuQty()) == 0){
                    continue;
                }
                skuCodes.put(detailE.getSkuCode(), detailE);
                SkuStockDTO stock = skuStockMap.get(detailE.getSkuCode());
                // 与勇军确认门店无锁定库存，源代码使用的真实库存，此处使用可用库存（=真实库存）
                detailE.setShopStock(stock == null ? BigDecimal.ZERO : stock.getAvailableQty());
                if(storeMap.containsKey(order.getShopCode())){
                    StoreDTO store = storeMap.get(order.getShopCode());
                    //设置门店等级
                    detailE.setShopLevel(store.getStoreLevel());
                }
                detailE.setReplenishTime(order.getReplenishTime());
                allDetail.add(detailE);
            }
        }
        //2.算出sku的总库存
        if(skuCodes == null || skuCodes.isEmpty()) {
            return 0;
        }
        Map<String, SkuStockForVw> totalStockMap = this.getTotalStock(skuCodes, vmCode);
        if(totalStockMap == null){
            return 0;
        }
        //3.对明细并进行排序
        allDetail = this.customSortDetail(allDetail);
        //4.分配订单明细的库存
        List<ReplenishDetailE> allotDetail = new ArrayList<>();
        for (ReplenishDetailE detail : allDetail){
            SkuStockForVw skuStock = totalStockMap.get(detail.getSkuCode());
            if(skuStock != null && skuStock.getAvailableQty().compareTo(BigDecimal.ZERO) > 0){
                BigDecimal stock = skuStock.getAvailableQty().subtract(detail.getLeftBasicSkuQty());
                if(stock.compareTo(BigDecimal.ZERO) >= 0){
                    //查询商品单位
                    List<SkuUnitExtDTO> skuUnits = this.querySkuUnitExtDTOList(detail);
                    //获取商品单位与基础单位转换比例
                    SkuUnitExtDTO skuUnit = this.getBaseSkuUnitExtDTO(detail, skuUnits);
                    //获取商品发货单位与基础单位转换比例
                    SkuUnitExtDTO tranSkuUnit = this.getDeliverySkuUnitExtDTO(detail, skuUnits);
                    //计算各种单位取整后的数量
                    AllocationCalQtyParam calQtyParam = this.buildAllocationCalQtyParam(detail, skuUnit, tranSkuUnit);
                    //过滤出转换后的基础数量大于0的列表
                    if(calQtyParam.getSaleBasicQty().compareTo(BigDecimal.ZERO) > 0) {
                        detail.setAllotQty(calQtyParam.getSaleQty());
                        allotDetail.add(detail);
                        skuStock.setAvailableQty(skuStock.getAvailableQty().subtract(calQtyParam.getSaleBasicQty()));
                    }
                } else {
                    //查询商品单位
                    List<SkuUnitExtDTO> skuUnits = this.querySkuUnitExtDTOList(detail);
                    //获取商品单位与基础单位转换比例
                    SkuUnitExtDTO skuUnit = this.getBaseSkuUnitExtDTO(detail, skuUnits);
                    //获取商品发货单位与基础单位转换比例
                    SkuUnitExtDTO tranSkuUnit = this.getDeliverySkuUnitExtDTO(detail, skuUnits);
                    //根据基础单位构造临时明细对象
                    ReplenishDetailE detailETemp = new ReplenishDetailE();
                    BeanUtils.copyProperties(detail, detailETemp);
                    detailETemp.setUnitCode(skuUnit.getBasicUnitCode());
                    detailETemp.setUnit(skuUnit.getBasicUnitName());
                    detailETemp.setSkuQty(skuStock.getAvailableQty());
                    //计算各种单位取整后的数量
                    AllocationCalQtyParam calQtyParam = this.buildAllocationCalQtyParam(detail, skuUnit, tranSkuUnit);
                    //过滤出转换后的基础数量大于0的列表
                    if(calQtyParam.getSaleBasicQty().compareTo(BigDecimal.ZERO) > 0) {
                        detail.setAllotQty(calQtyParam.getSaleQty());
                        allotDetail.add(detail);
                        skuStock.setAvailableQty(skuStock.getAvailableQty().subtract(calQtyParam.getSaleBasicQty()));
                    }
                }
            }
        }
        //5.按照门店聚合
        Map<String, List<ReplenishDetailE>> detailMap = allotDetail.stream().collect(Collectors.groupingBy(ReplenishDetailE::getShopCode));
        List<String> frontRecordCodes = allotDetail.stream().map(ReplenishDetailE::getRecordCode).distinct().collect(Collectors.toList());
        //6.分配完毕后
        for(Map.Entry<String, List<ReplenishDetailE>> entry : detailMap.entrySet()){
            List<ReplenishDetailE> detail = entry.getValue();
            try {
                //生成大仓出库单
                allianceBusinessReplenishService.saveAllotWarehouseRecord(detail, realWarehouse, vmCode);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return frontRecordCodes.size();
    }

    /**
     * @Description: 计算寻源后商品分配数量 <br>
     *
     * @Author chuwenchao 2020/6/18
     * @param detail
     * @return 
     */
    private AllocationCalQtyParam buildAllocationCalQtyParam(ReplenishDetailE detail, SkuUnitExtDTO skuUnit, SkuUnitExtDTO tranSkuUnit) {
        AllocationCalQtyParam allocationCalQtyParam = allocationCalQtyConvert.detailToAllocation(detail);
        //防止后期多次寻源（多次寻源，可用库存小于剩余分配数量时，此处有问题需修改）
        allocationCalQtyParam.setPlanQty(detail.getSkuQty().subtract(detail.getAllotQty()));
        //根据基础单位转换比例计算基础单位数量
        BigDecimal basicQty = allocationCalQtyParam.getPlanQty().multiply(skuUnit.getScale()).setScale(StockCoreConstant.DECIMAL_POINT_NUM, ROUND_DOWN);
        //发货单位取整
        BigDecimal skuQty = basicQty.divide(tranSkuUnit.getScale(), 0, ROUND_DOWN);
        //转换后的发货单位数量
        BigDecimal realBasicQty = skuQty.multiply(tranSkuUnit.getScale()).setScale(StockCoreConstant.DECIMAL_POINT_NUM, ROUND_DOWN);
        //转换成销售单位取整
        BigDecimal saleQty = realBasicQty.divide(allocationCalQtyParam.getScale(), StockCoreConstant.DECIMAL_POINT_NUM, ROUND_DOWN);
        //转换成实际销售数量
        BigDecimal saleBasicQty = saleQty.multiply(allocationCalQtyParam.getScale()).setScale(StockCoreConstant.DECIMAL_POINT_NUM, ROUND_DOWN);
        allocationCalQtyParam.setSaleQty(saleQty);
        allocationCalQtyParam.setSaleBasicQty(saleBasicQty);
        return allocationCalQtyParam;
    }

    /**
     * @Description: 获取商品发货单位与基础单位转换比例 <br>
     *
     * @Author chuwenchao 2020/6/18
     * @param detail
     * @return
     */
    private SkuUnitExtDTO getDeliverySkuUnitExtDTO(ReplenishDetailE detail, List<SkuUnitExtDTO> skuUnits) {
        //获取商品发货单位
        Optional<SkuUnitExtDTO> deliverySkuUnitOp  = skuUnits.stream().filter(r -> AllocationConstant.TRANS_TYPE.intValue() == r.getType().intValue()).findFirst();
        AlikAssert.isTrue(deliverySkuUnitOp.isPresent(), ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + "商品发货单位未查到 " + detail.getSkuCode());
        return deliverySkuUnitOp.get();
    }

    /**
     * @Description: 获取商品单位与基础单位转换比例 <br>
     *
     * @Author chuwenchao 2020/6/18
     * @param detail
     * @return 
     */
    private SkuUnitExtDTO getBaseSkuUnitExtDTO(ReplenishDetailE detail, List<SkuUnitExtDTO> skuUnits) {
        Optional<SkuUnitExtDTO> skuUnitOp  = skuUnits.stream().filter(r -> r.getUnitCode().equals(detail.getUnitCode())).findFirst();
        AlikAssert.isTrue(skuUnitOp.isPresent(), ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + "商品单位未查到 " + detail.getSkuCode());
        return skuUnitOp.get();
    }

    /**
     * @Description: 查询商品单位 <br>
     *
     * @Author chuwenchao 2020/6/18
     * @param detail
     * @return 
     */
    private List<SkuUnitExtDTO> querySkuUnitExtDTOList(ReplenishDetailE detail) {
        List<String> skuCodeList = new ArrayList<>(1);
        skuCodeList.add(detail.getSkuCode());
        List<SkuUnitExtDTO> skuUnits = itemFacade.querySkuUnits(skuCodeList);
        AlikAssert.isNotEmpty(skuUnits, ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + "未查询到商品单位信息");
        return skuUnits;
    }

    /**
     * 自定义排序所有的明细(0库存>等级>时间)
     */
    private List<ReplenishDetailE> customSortDetail(List<ReplenishDetailE> allDetail) {
        Collections.sort(allDetail,new Comparator<ReplenishDetailE>() {
            @Override
            public int compare(ReplenishDetailE o1, ReplenishDetailE o2) {
                return recursion(o1, o2);
            }

            private int recursion(ReplenishDetailE o1, ReplenishDetailE o2) {
                if(0 == o1.getShopStock().compareTo(BigDecimal.ZERO) && 0 == o2.getShopStock().compareTo(BigDecimal.ZERO)){
                    return 0;
                } else if(0 == o1.getShopStock().compareTo(BigDecimal.ZERO) && 0 != o2.getShopStock().compareTo(BigDecimal.ZERO)){
                    return -1;
                } else if(0 == o2.getShopStock().compareTo(BigDecimal.ZERO) && 0 != o1.getShopStock().compareTo(BigDecimal.ZERO)){
                    return 1;
                }else {
                    if(!o2.getShopLevel().equals(o1.getShopLevel())){
                        return o1.getShopLevel().compareTo(o2.getShopLevel());
                    }else {
                        return o2.getReplenishTime().compareTo(o1.getReplenishTime());
                    }
                }
            }
        });
        return allDetail;
    }

    /**
     * @Description: 根据虚仓查询Sku总库存 <br>
     *
     * @Author chuwenchao 2020/6/17
     * @param skuCodes
     * @return 
     */
    private Map<String, SkuStockForVw> getTotalStock(Map<String, ReplenishDetailE> skuCodes, String vmCode) {
        List<SkuInfoForVw> skuInfoList = new ArrayList<>();
        for(Map.Entry<String, ReplenishDetailE> entry : skuCodes.entrySet()) {
            SkuInfoForVw skuInfoForVw = new SkuInfoForVw();
            skuInfoForVw.setSkuCode(entry.getKey());
            skuInfoForVw.setUnitCode(entry.getValue().getUnitCode());
            skuInfoForVw.setVirtualWarehouseCode(vmCode);
            skuInfoList.add(skuInfoForVw);
        }
        List<SkuStockForVw> skuStockForVwList = stockQueryFacade.queryVmListByVmGroupCodes(skuInfoList);
        return skuStockForVwList.stream().collect(Collectors.toMap(SkuStockForVw::getSkuCode, Function.identity(), (v1, v2)->v1));
    }

    /**
     * @Description: 查询单据门店库存 <br>
     *
     * @Author chuwenchao 2020/6/17
     * @param order
     * @param replenishDetailEList
     * @return 
     */
    private Map<String, SkuStockDTO> queryStockByOrder(ReplenishRecordE order, List<ReplenishDetailE> replenishDetailEList) {
        QueryRealStockDTO queryRealStockDTO = new QueryRealStockDTO();
        queryRealStockDTO.setFactoryCode(order.getInFactoryCode());
        queryRealStockDTO.setWarehouseOutCode(order.getInRealWarehouseCode());
        List<BaseSkuInfoDTO> baseSkuInfoDTOS = new ArrayList<>();
        for(ReplenishDetailE replenishDetailE : replenishDetailEList) {
            BaseSkuInfoDTO skuInfoDTO = new BaseSkuInfoDTO();
            skuInfoDTO.setSkuCode(replenishDetailE.getSkuCode());
            baseSkuInfoDTOS.add(skuInfoDTO);
        }
        queryRealStockDTO.setBaseSkuInfoDTOS(baseSkuInfoDTOS);
        List<SkuStockDTO> skuStockDTOList = stockQueryFacade.queryRealStockBySkuInfo(queryRealStockDTO);
        return skuStockDTOList.stream().collect(Collectors.toMap(SkuStockDTO::getSkuCode, Function.identity(), (v1, v2) -> v1));
    }

    /**
     * @Description: 保存寻源日志 <br>
     *
     * @Author chuwenchao 2020/6/15
     * @param orderSize
     * @return
     */
    private Long saveReplenishAllotLog(int orderSize, AllotTypeEnum allotType, int times, ShopReplenishAllotDTO shopReplenishAllotDTO) {
        ReplenishAllotLogE logE = new ReplenishAllotLogE();
        logE.setTimes(times);
        logE.setTotalRecords(orderSize);
        logE.setAllotType(allotType.getType());
        Date now = new Date();
        logE.setStartTime(now);
        logE.setIsAvailable(YesOrNoEnum.YES.getType());
        logE.setIsDeleted(YesOrNoEnum.NO.getType());
        logE.setCreateTime(now);
        logE.setUpdateTime(now);
        logE.setCreator(shopReplenishAllotDTO.getOperator());
        logE.setModifier(shopReplenishAllotDTO.getOperator());
        logE.setChannelCode(shopReplenishAllotDTO.getChannelCode());
        logE.setFactoryCode(shopReplenishAllotDTO.getFactoryCode());
        replenishAllotLogMapper.saveReplenishAllotLog(logE);
        return logE.getId();
    }

}
