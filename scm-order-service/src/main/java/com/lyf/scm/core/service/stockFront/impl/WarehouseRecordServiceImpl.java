package com.lyf.scm.core.service.stockFront.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.lyf.scm.common.constants.CommonConstants;
import com.lyf.scm.common.constants.KibanaLogConstants;
import com.lyf.scm.common.constants.ReceiverTypeConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.*;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.notify.RecordSkuUnitDTO;
import com.lyf.scm.core.api.dto.stockFront.WarehouseRecordDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.WarehouseRecordPageDTO;
import com.lyf.scm.core.common.RomeCollectionUtil;
import com.lyf.scm.core.config.ServiceKibanaLog;
import com.lyf.scm.core.domain.convert.order.OrderConvert;
import com.lyf.scm.core.domain.convert.orderReturn.OrderReturnConvert;
import com.lyf.scm.core.domain.convert.shopReturn.ShopReturnConvert;
import com.lyf.scm.core.domain.convert.stockFront.*;
import com.lyf.scm.core.domain.entity.order.OrderE;
import com.lyf.scm.core.domain.entity.orderReturn.OrderReturnE;
import com.lyf.scm.core.domain.entity.shopReturn.ShopReturnDetailE;
import com.lyf.scm.core.domain.entity.shopReturn.ShopReturnE;
import com.lyf.scm.core.domain.entity.stockFront.*;
import com.lyf.scm.core.mapper.order.OrderMapper;
import com.lyf.scm.core.mapper.orderReturn.OrderReturnMapper;
import com.lyf.scm.core.mapper.shopReturn.ShopReturnDetailMapper;
import com.lyf.scm.core.mapper.shopReturn.ShopReturnMapper;
import com.lyf.scm.core.mapper.stockFront.*;
import com.lyf.scm.core.remote.base.dto.StoreDTO;
import com.lyf.scm.core.remote.base.facade.BaseFacade;
import com.lyf.scm.core.remote.item.dto.ParamExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuInfoExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuUnitExtDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.stock.dto.QueryRealWarehouseDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.remote.trade.dto.UpdatePoDTO;
import com.lyf.scm.core.remote.trade.dto.UpdatePoLineDTO;
import com.lyf.scm.core.remote.trade.facade.TransactionFacade;
import com.lyf.scm.core.remote.transport.dto.DoDetailDTO;
import com.lyf.scm.core.remote.transport.dto.DoMainDTO;
import com.lyf.scm.core.remote.transport.facade.TransportFacade;
import com.lyf.scm.core.service.stockFront.WarehouseRecordService;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ROUND_DOWN;

@Slf4j
@Service("warehouseRecordService")
public class WarehouseRecordServiceImpl implements WarehouseRecordService {

    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;
    @Resource
    private WarehouseRecordDetailMapper warehouseRecordDetailMapper;
    @Resource
    private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;
    @Resource
    private ShopInventoryMapper shopInventoryMapper;
    @Resource
    private WhAllocationMapper whAllocationMapper;
    @Resource
    private ReplenishRecordMapper replenishRecordMapper;
    @Resource
    private ReplenishRecordDetailMapper replenishRecordDetailMapper;
    @Resource
    private ShopAllocationMapper shopAllocationMapper;
    @Resource
    private SalesReturnMapper salesReturnMapper;
    @Autowired
    private ItemFacade itemFacade;
    @Autowired
    private StockRealWarehouseFacade stockRealWarehouseFacade;
    @Autowired
    private SalesReturnConvertor salesReturnConvertor;
    @Autowired
    private ShopAllocationConvertor shopAllocationConvertor;
    @Resource
    private ShopReturnConvert shopReturnConvert;
    @Resource
    private ShopReturnMapper shopReturnMapper;
    @Autowired
    private WarehouseRecordConvertor warehouseRecordConvertor;
    @Autowired
    private ShopInventoryConvertor shopInventoryConvertor;
    @Autowired
    private FrontRecordConvertor frontRecordConvertor;
    @Autowired
    private ReplenishRecordConvert replenishRecordConvert;
    @Resource
    private OrderReturnConvert orderReturnConvert;
    @Resource
    private OrderConvert orderConvert;
    @Resource
    private TransactionFacade transactionFacade;
    @Resource
    private TransportFacade transportFacade;
    @Resource
    private BaseFacade baseFacade;
    @Resource
    private WhAllocationDetailMapper whAllocationDetailMapper;
    @Resource
    private OrderReturnMapper orderReturnMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private ShopReturnDetailMapper shopReturnDetailMapper;

    @Override
    public WarehouseRecordE queryWarehouseRecordEById(Long warehouseRecordId) {
        // 查询单据
        WarehouseRecordE warehouseRecordE = warehouseRecordMapper.getWarehouseRecordById(warehouseRecordId);
        if (warehouseRecordE == null) {
            return null;
        }
        // 设置明细
        List<WarehouseRecordDetailE> details = warehouseRecordDetailMapper.queryListByRecordId(warehouseRecordId);
        if (CollectionUtils.isEmpty(details)) {
            return null;
        }
        warehouseRecordE.setWarehouseRecordDetailList(details);
        return warehouseRecordE;
    }

    @Override
    public PageInfo<WarehouseRecordPageDTO> queryInWarehouseRecordList(WarehouseRecordPageDTO warehouseRecord) {
        WarehouseRecordE warehouseRecordE = warehouseRecordConvertor.convertDtoToEntity(warehouseRecord);
        List<Long> warehouseIds = new ArrayList<>();
        FrontRecordE frontRecord = warehouseRecordE.getFrontRecord();
        if (null != frontRecord && StringUtils.isNotBlank(frontRecord.getRecordCode())) {
            warehouseIds = frontWarehouseRecordRelationMapper.queryWarehouseRecordIdByRecord(warehouseRecordE.getFrontRecord().getRecordCode());
            if (CollectionUtils.isEmpty(warehouseIds)) {
                return new PageInfo<>();
            }
        }
        List<Long> realWarehouseIds = new ArrayList<>();
        if (StringUtils.isNotBlank(warehouseRecordE.getRealWarehouseCode())) {
            List<RealWarehouse> realWarehouseList = new ArrayList<>();
            RealWarehouse realWarehouse = stockRealWarehouseFacade.queryRealWarehouseByRWCode(warehouseRecordE.getRealWarehouseCode());
            if (realWarehouse != null) {
                realWarehouseList.add(realWarehouse);
            }
            realWarehouseIds = realWarehouseList.stream().map(RealWarehouse::getId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(realWarehouseIds)) {
                return new PageInfo<>();
            }
        }
        //查询入库单
        List<Integer> types = new ArrayList<>();
        if (warehouseRecord.getRecordType() == null) {
            Map<Integer, String> map = WarehouseRecordTypeEnum.getInWarehouseRecordPageTypeList();
            map.forEach((k, v) -> types.add(k));
        } else {
            types.add(warehouseRecord.getRecordType());
        }
        Page page = PageHelper.startPage(warehouseRecord.getPageIndex(), warehouseRecord.getPageSize());
        List<WarehouseRecordE> entityList = warehouseRecordMapper.queryInWarehouseRecordPage(warehouseRecordE, types, warehouseIds, realWarehouseIds);
        if (CollectionUtils.isEmpty(entityList)) {
            return new PageInfo<>();
        }
        //查询仓库信息
        List<QueryRealWarehouseDTO> queryRealWarehouseList = new ArrayList<QueryRealWarehouseDTO>();
        entityList.forEach(WarehouseRecordE -> {
            QueryRealWarehouseDTO queryRealWarehouseDTO = new QueryRealWarehouseDTO();
            queryRealWarehouseDTO.setWarehouseOutCode(WarehouseRecordE.getRealWarehouseCode());
            queryRealWarehouseDTO.setFactoryCode(WarehouseRecordE.getFactoryCode());
            queryRealWarehouseList.add(queryRealWarehouseDTO);
        });
        List<RealWarehouse> realWarehouseList = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(queryRealWarehouseList);
        for (RealWarehouse realWarehouse : realWarehouseList) {
            entityList.stream().filter(warehouse -> realWarehouse.getId().equals(warehouse.getRealWarehouseId())).forEach(warehouse -> {
                warehouse.setRealWarehouseCode(realWarehouse.getRealWarehouseCode() == null ? "" : realWarehouse.getRealWarehouseCode());
                warehouse.setRealWarehouseName(realWarehouse.getRealWarehouseName() == null ? "" : realWarehouse.getRealWarehouseName());
                warehouse.setFactoryCode(realWarehouse.getFactoryCode() == null ? "" : realWarehouse.getFactoryCode());
            });
        }
        //根据出入库单查询前置单
        List<Long> idList = entityList.stream().map(WarehouseRecordE::getId).collect(Collectors.toList());
        List<FrontWarehouseRecordRelationE> recordRelations = frontWarehouseRecordRelationMapper.queryFrontRecordListByIdList(idList);
        //查询前置单类型集合
        Map<Integer, List<FrontWarehouseRecordRelationE>> recordCollect = recordRelations.stream().collect(Collectors.groupingBy(relation -> relation.getFrontRecordType()));
        recordCollect.forEach((type, relationEntityList) -> queryInFrontRecord(type, relationEntityList, entityList));
        List<WarehouseRecordPageDTO> pageList = warehouseRecordConvertor.convertEToDto(entityList);
        PageInfo<WarehouseRecordPageDTO> personPageInfo = new PageInfo<>(pageList);
        personPageInfo.setTotal(page.getTotal());
        return personPageInfo;
    }

    @Override
    public PageInfo<WarehouseRecordPageDTO> queryOutWarehouseRecordList(WarehouseRecordPageDTO warehouseRecord) {
        WarehouseRecordE warehouseRecordE = warehouseRecordConvertor.convertDtoToEntity(warehouseRecord);
        List<Long> warehouseIds = new ArrayList<>();
        FrontRecordE frontRecord = warehouseRecordE.getFrontRecord();
        if (null != frontRecord && StringUtils.isNotBlank(frontRecord.getRecordCode())) {
            warehouseIds = frontWarehouseRecordRelationMapper.queryWarehouseRecordIdByRecord(warehouseRecordE.getFrontRecord().getRecordCode());
            if (CollectionUtils.isEmpty(warehouseIds)) {
                return new PageInfo<>();
            }
        }
        List<Long> realWarehouseIds = new ArrayList<>();
        if (StringUtils.isNotBlank(warehouseRecordE.getRealWarehouseCode())) {
            List<RealWarehouse> realWarehouseList = new ArrayList<>();
            RealWarehouse realWarehouse = stockRealWarehouseFacade.queryRealWarehouseByRWCode(warehouseRecordE.getRealWarehouseCode());
            if (realWarehouse != null) {
                realWarehouseList.add(realWarehouse);
            }
            realWarehouseIds = realWarehouseList.stream().map(RealWarehouse::getId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(realWarehouseIds)) {
                return new PageInfo<>();
            }
        }
        //查询出库单
        List<Integer> types = new ArrayList<>();
        if (warehouseRecord.getRecordType() == null) {
            Map<Integer, String> map = WarehouseRecordTypeEnum.getOutWarehouseRecordPageTypeList();
            map.forEach((k, v) -> types.add(k));
        } else {
            types.add(warehouseRecord.getRecordType());
        }
        Page page = PageHelper.startPage(warehouseRecord.getPageIndex(), warehouseRecord.getPageSize());
        List<WarehouseRecordE> entityList = warehouseRecordMapper.queryOutWarehouseRecordPage(warehouseRecordE, types, warehouseIds, realWarehouseIds);
        if (CollectionUtils.isEmpty(entityList)) {
            return new PageInfo<>();
        }
        //查询仓库信息
        List<QueryRealWarehouseDTO> queryRealWarehouseList = new ArrayList<QueryRealWarehouseDTO>();
        entityList.forEach(WarehouseRecordE -> {
            QueryRealWarehouseDTO queryRealWarehouseDTO = new QueryRealWarehouseDTO();
            queryRealWarehouseDTO.setWarehouseOutCode(WarehouseRecordE.getRealWarehouseCode());
            queryRealWarehouseDTO.setFactoryCode(WarehouseRecordE.getFactoryCode());
            queryRealWarehouseList.add(queryRealWarehouseDTO);
        });
        List<RealWarehouse> realWarehouseList = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(queryRealWarehouseList);
        for (RealWarehouse realWarehouse : realWarehouseList) {
            entityList.stream().filter(warehouse -> realWarehouse.getId().equals(warehouse.getRealWarehouseId())).forEach(warehouse -> {
                warehouse.setRealWarehouseCode(realWarehouse.getRealWarehouseCode() == null ? "" : realWarehouse.getRealWarehouseCode());
                warehouse.setRealWarehouseName(realWarehouse.getRealWarehouseName() == null ? "" : realWarehouse.getRealWarehouseName());
                warehouse.setFactoryCode(realWarehouse.getFactoryCode() == null ? "" : realWarehouse.getFactoryCode());
            });
        }
        //根据出入库单查询前置单
        List<Long> idList = entityList.stream().map(WarehouseRecordE::getId).collect(Collectors.toList());
        List<FrontWarehouseRecordRelationE> recordRelations = frontWarehouseRecordRelationMapper.queryFrontRecordListByIdList(idList);
        //查询前置单类型集合
        Map<Integer, List<FrontWarehouseRecordRelationE>> recordCollect = recordRelations.stream().collect(Collectors.groupingBy(relation -> relation.getFrontRecordType()));
        recordCollect.forEach((type, relationEntityList) -> queryOutFrontRecord(type, relationEntityList, entityList));
        List<WarehouseRecordPageDTO> pageList = warehouseRecordConvertor.convertEToDto(entityList);
        PageInfo<WarehouseRecordPageDTO> personPageInfo = new PageInfo<>(pageList);
        personPageInfo.setTotal(page.getTotal());
        return personPageInfo;
    }

    @Override
    public List<WarehouseRecordDetailDTO> queryWarehouseRecordDetailList(Long warehouseRecordId) {
        List<WarehouseRecordDetailE> details = warehouseRecordDetailMapper.queryListByRecordId(warehouseRecordId);
        //查询商品单位信息
//		List<SkuUnitExtDTO> skuList = skuFacade.unitsByWarehouseRecordDetail(details);
        //查询商品信息
        List<Long> skuIds = details.stream().map(WarehouseRecordDetailE::getSkuId).distinct().collect(Collectors.toList());
        List<SkuInfoExtDTO> skuInfoList = itemFacade.skuBySkuIds(skuIds);
        Map<Long, SkuInfoExtDTO> skuInfoExtDTOMap = skuInfoList.stream().distinct().collect(Collectors.toMap(SkuInfoExtDTO::getId, item -> item));
        for (WarehouseRecordDetailE item : details) {
            if (skuInfoExtDTOMap.containsKey(item.getSkuId())) {
                item.setSkuName(skuInfoExtDTOMap.get(item.getSkuId()).getName());
                item.setSkuCode(skuInfoExtDTOMap.get(item.getSkuId()).getSkuCode());
                item.setUnit(skuInfoExtDTOMap.get(item.getSkuId()).getSpuUnitName());
            }
        }
        List<WarehouseRecordDetailDTO> list = warehouseRecordConvertor.entityToDtoDetails(details);
        return list;
    }

    /**
     * @param page
     * @param maxResult
     * @return
     * @Description: 【定时器】分页查询待同步交易单据 <br>
     * @Author chuwenchao 2020/6/19
     */
    @Override
    public List<WarehouseRecordPageDTO> queryNeedSyncTradeRecordByPage(Integer page, Integer maxResult) {
        List<WarehouseRecordE> warehouseRecordEList = warehouseRecordMapper.queryBySyncTradeStatus(page, maxResult);
        return warehouseRecordConvertor.entityToWarehouseDto(warehouseRecordEList);
    }

    /**
     * 【定时器】分页查询待同步派车系统 出库单
     *
     * @param startPage
     * @param endPage
     * @return
     */
    @Override
    public List<Long> queryNeedSyncTmsBWarehouseRecords(Integer startPage, Integer endPage) {
        return warehouseRecordMapper.queryNeedSynTmsBWarehouseRecordByPage(startPage, endPage);
    }

    /**
     * 【定时器】 同步出库单 到 派车系统
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleDispatchCarWarehouseRecord(Long id) {
        WarehouseRecordE warehouseRecord = queryWarehouseRecordEById(id);
        if (null == warehouseRecord || !SyncTmsBStatusEnum.UNSYNCHRONIZED.getStatus().equals(warehouseRecord.getSyncTmsbStatus())) {
            log.info("后置单不存在或者状态不为待同步，无需同步");
            return;
        }
        log.info("出库单同步派车系统，后置单编码：{}", warehouseRecord.getRecordCode());

        //查询前置单
        List<FrontWarehouseRecordRelationE> frontRelationList = frontWarehouseRecordRelationMapper.getFrontRelationByRecordCode(warehouseRecord.getRecordCode());
        AlikAssert.isNotEmpty(frontRelationList, ResCode.ORDER_ERROR_5120, ResCode.ORDER_ERROR_5120_DESC);
        FrontWarehouseRecordRelationE recordRelationE = frontRelationList.get(0);

        List<WarehouseRecordDetailE> warehouseRecordDetailEList = warehouseRecord.getWarehouseRecordDetailList();
        //查询 单位转化 信息
        List<String> skuCodeList = warehouseRecordDetailEList.stream().map(WarehouseRecordDetailE::getSkuCode).distinct().collect(Collectors.toList());
        List<SkuUnitExtDTO> skuUnitExtList = itemFacade.querySkuUnits(skuCodeList);
        Map<String, List<SkuUnitExtDTO>> skuCode2UnitList = skuUnitExtList.stream().collect(Collectors.groupingBy(skuUnit -> skuUnit.getSkuCode()));

        //skuCode 运输单位 信息
        Map<String, String> skuCode2transPortUnitMap = new HashMap<>();
        for (Map.Entry<String, List<SkuUnitExtDTO>> entry : skuCode2UnitList.entrySet()) {
            SkuUnitExtDTO transPortUnit = entry.getValue().stream().filter(unit ->
                    SkuUnitTypeEnum.TRANSPORT_UNIT.getId().equals(unit.getType())).findFirst().orElse(null);
            if (null == transPortUnit) {
                continue;
            }
            skuCode2transPortUnitMap.put(entry.getKey(), transPortUnit.getUnitCode());
        }

        //查询 物料 运输单位 的 毛重 和 体积
        List<ParamExtDTO> skuCodeAndUnitCodeList = new ArrayList<>();
        warehouseRecordDetailEList.stream().forEach(recordDetail -> {
            if (skuCode2transPortUnitMap.containsKey(recordDetail.getSkuCode())) {
                ParamExtDTO param = new ParamExtDTO();
                param.setSkuCode(recordDetail.getSkuCode());
                param.setUnitCode(skuCode2transPortUnitMap.get(recordDetail.getSkuCode()));
                skuCodeAndUnitCodeList.add(param);
            }
        });
        List<SkuUnitExtDTO> skuUnitExtDTOList = itemFacade.unitsBySkuCodeAndUnitCodeAndMerchantId(skuCodeAndUnitCodeList, null);
        Map<String, SkuUnitExtDTO> skuCodeAndUnitCode2InfoMap = skuUnitExtDTOList.stream()
                .filter(extDto -> SkuUnitTypeEnum.TRANSPORT_UNIT.getId() == extDto.getType())
                .collect(Collectors.toMap(SkuUnitExtDTO::getSkuCode, Function.identity(), (key1, key2) -> key1));

        //组装请求参数报文
        DoMainDTO doMainDTO = new DoMainDTO();
        Date deliveryDate = warehouseRecord.getDeliveryTime();
        if (null == deliveryDate) {
            deliveryDate = new Date();
        }
        doMainDTO.setDeliveryDate(deliveryDate);
        doMainDTO.setDeliveryWarehouseCode(warehouseRecord.getRealWarehouseCode());
        doMainDTO.setDoNo(warehouseRecord.getRecordCode());
        doMainDTO.setDoType(warehouseRecord.getRecordType());
        doMainDTO.setFactoryCode(warehouseRecord.getFactoryCode());
        RealWarehouse realWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(warehouseRecord.getRealWarehouseCode(),
                warehouseRecord.getFactoryCode());
        if (null != realWarehouse) {
            doMainDTO.setDeliveryWarehouseName(realWarehouse.getRealWarehouseName());
        }
        //处理送达方信息 查询 前置单物料信息
        Map<String, String> frontDetailId2UnitCode = queryReceiverInfoAndFrontDetailSku(doMainDTO, warehouseRecord, recordRelationE);

        //构造明细
        List<DoDetailDTO> doDetailDTOList = new ArrayList<>();
        BigDecimal totalRoughWeight = BigDecimal.ZERO;
        BigDecimal totalVolume = BigDecimal.ZERO;
        SkuUnitExtDTO skuUnitInfo = null;
        List<RecordSkuUnitDTO> skuUnitList = null;
        for (WarehouseRecordDetailE detail : warehouseRecordDetailEList) {
            DoDetailDTO doDetail = new DoDetailDTO();
            doDetail.setDoNo(warehouseRecord.getRecordCode());
            doDetail.setSkuCode(detail.getSkuCode());
            doDetail.setSkuQty(detail.getPlanQty());
            doDetail.setUnit(detail.getUnit());
            doDetail.setUnitCode(detail.getUnitCode());

            BigDecimal detailRoughWeight = BigDecimal.ZERO;
            BigDecimal detailVolume = BigDecimal.ZERO;
            //skuCode 物料下 运输单位 信息
            skuUnitInfo = skuCodeAndUnitCode2InfoMap.get(detail.getSkuCode());
            if (null != skuUnitInfo && null != skuUnitInfo.getScale() && skuUnitInfo.getScale().compareTo(BigDecimal.ZERO) == 1) {
                detailRoughWeight = null == skuUnitInfo.getGrossWeight() ? BigDecimal.ZERO :
                        skuUnitInfo.getGrossWeight().multiply(detail.getPlanQty()).divide(skuUnitInfo.getScale(), CommonConstants.DECIMAL_POINT_NUM, BigDecimal.ROUND_DOWN);
                detailVolume = null == skuUnitInfo.getVolume() ? BigDecimal.ZERO :
                        skuUnitInfo.getVolume().multiply(detail.getPlanQty()).divide(skuUnitInfo.getScale(), CommonConstants.DECIMAL_POINT_NUM, BigDecimal.ROUND_DOWN);
            }
            doDetail.setRoughWeight(detailRoughWeight);
            doDetail.setVolume(detailVolume);
            totalRoughWeight = totalRoughWeight.add(detailRoughWeight);
            totalVolume = totalVolume.add(detailVolume);

            //获取当前sku的三种单位信息
            skuUnitList = createRecordSkuUnitList(skuCode2UnitList.get(detail.getSkuCode()), detail.getUnitCode(),
                    frontDetailId2UnitCode.get(detail.getDeliveryLineNo()), skuCode2transPortUnitMap.get(detail.getSkuCode()));
            doDetail.setSkuUnitDTOList(skuUnitList);
            doDetailDTOList.add(doDetail);
        }
        doMainDTO.setRoughWeight(totalRoughWeight);
        doMainDTO.setVolume(totalVolume);
        doMainDTO.setDoDetailDTOList(doDetailDTOList);
        //修改状态 为已同步
        warehouseRecordMapper.updateWarehouseRecordSynTmsBStatusComplete(id);
        //同步派车系统
        Boolean result = transportFacade.receiveOutBound(doMainDTO);
        if (!result) {
            //更新同步失败时间
            warehouseRecordMapper.updateSyncTmsBFailTime(id);
            log.error(ServiceKibanaLog.getServiceLog(KibanaLogConstants.DISPATCH_CAR, "handleDispatchCarWarehouseRecord",
                    "出库单编号: " + doMainDTO.getDoNo(), doMainDTO));
            throw new RomeException(ResCode.ORDER_ERROR_6036, ResCode.ORDER_ERROR_6036_DESC);
        }
        log.info("出库单同步派车系统成功，后置单编码：{}", doMainDTO.getDoNo());
    }

    /**
     * 查询和设置送达方 并查询和返回前置单明细
     *
     * @param doMain
     * @param warehouseRecord
     * @param relation
     * @return
     */
    private Map<String, String> queryReceiverInfoAndFrontDetailSku(DoMainDTO doMain, WarehouseRecordE warehouseRecord, FrontWarehouseRecordRelationE relation) {
        Map<String, String> frontRecordDetailId2UnitCode = new HashMap<>();
        Integer frontRecordType = relation.getFrontRecordType();
        if (FrontRecordTypeEnum.WAREHOUSE_ALLOCATION_RECORD.getType().equals(frontRecordType)) {
            //仓库调拨
            WhAllocationE whAllocationE = whAllocationMapper.queryById(relation.getFrontRecordId());
            AlikAssert.isNotNull(whAllocationE, ResCode.ORDER_ERROR_5122, ResCode.ORDER_ERROR_5122_DESC);
            doMain.setReceiverType(ReceiverTypeConstants.FACTORY);
            doMain.setReceiverCode(whAllocationE.getInFactoryCode());
            List<String> shopCodeList = Lists.newArrayList(warehouseRecord.getFactoryCode());
            if (!shopCodeList.contains(whAllocationE.getInFactoryCode())) {
                shopCodeList.add(whAllocationE.getInFactoryCode());
            }
            List<StoreDTO> storeDTOList = baseFacade.searchByCodeList(shopCodeList);
            Map<String, String> storeCode2NameMap = storeDTOList.stream().collect(Collectors.toMap(StoreDTO::getCode, StoreDTO::getName, (key1, key2) -> key1));
            doMain.setReceiverName(storeCode2NameMap.get(whAllocationE.getInFactoryCode()));
            doMain.setFactoryName(storeCode2NameMap.get(warehouseRecord.getFactoryCode()));

            //查询前置单明细
            List<WhAllocationDetailE> whAllocationDetailEList = whAllocationDetailMapper.queryDetailByFrontIdsOrderBySkuQty(relation.getFrontRecordId());
            frontRecordDetailId2UnitCode = whAllocationDetailEList.stream()
                    .collect(Collectors.toMap(k -> String.valueOf(k.getId()), WhAllocationDetailE::getUnitCode, (key1, key2) -> key1));
        } else if (FrontRecordTypeEnum.SHOP_REPLENISHMENT_RECORD.getType().equals(frontRecordType)
                || FrontRecordTypeEnum.JOIN_SHOP_REPLENISHMENT_RECORD.getType().equals(frontRecordType)
                || FrontRecordTypeEnum.SHOP_SUPPLIER_DIRECT_DELIVERY_RECORD.getType().equals(frontRecordType)
                || FrontRecordTypeEnum.SHOP_COLD_CHAIN_DELIVERY_RECORD.getType().equals(frontRecordType)) {
            //直营门店补货单 + 加盟门店补货单 + 门店供应商直送交货单 + 门店冷链交货单
            List<ReplenishRecordE> replenishRecordList = replenishRecordMapper.queryReplenishRecordByIds(Lists.newArrayList(relation.getFrontRecordId()));
            AlikAssert.isNotEmpty(replenishRecordList, ResCode.ORDER_ERROR_5122, ResCode.ORDER_ERROR_5122_DESC);
            ReplenishRecordE replenishRecordE = replenishRecordList.get(0);
            doMain.setReceiverType(ReceiverTypeConstants.SHOP);
            doMain.setReceiverCode(null == replenishRecordE ? "" : replenishRecordE.getShopCode());
            doMain.setReceiverName(null == replenishRecordE ? "" : replenishRecordE.getShopName());
            StoreDTO storeDTO = baseFacade.searchByCode(warehouseRecord.getFactoryCode());
            doMain.setFactoryName(null == storeDTO ? "" : storeDTO.getName());

            //查询前置单明细
            List<ReplenishDetailE> replenishDetailEList = replenishRecordDetailMapper.queryDetailByRecordId(relation.getFrontRecordId());
            frontRecordDetailId2UnitCode = replenishDetailEList.stream()
                    .collect(Collectors.toMap(k -> String.valueOf(k.getId()), ReplenishDetailE::getUnitCode, (key1, key2) -> key1));
        } else if (FrontRecordTypeEnum.DIRECT_SHOP_RETURN_GOODS_RECORD.getType().equals(frontRecordType)
                || FrontRecordTypeEnum.JOIN_SHOP_RETURN_GOODS_RECORD.getType().equals(frontRecordType)) {
            //加盟门店退货 直营门店退货
            ShopReturnE shopReturnE = shopReturnMapper.queryShopReturnById(relation.getFrontRecordId());
            AlikAssert.isNotNull(shopReturnE, ResCode.ORDER_ERROR_7801, ResCode.ORDER_ERROR_7801_DESC);
            doMain.setReceiverType(ReceiverTypeConstants.FACTORY);
            List<String> shopCodeList = Lists.newArrayList(warehouseRecord.getFactoryCode());
            if (StringUtils.isNotBlank(shopReturnE.getInColdFactoryCode())) {
                shopCodeList.add(shopReturnE.getInColdFactoryCode());
            }
            shopCodeList.add(shopReturnE.getInFactoryCode());
            List<StoreDTO> storeDTOList = baseFacade.searchByCodeList(shopCodeList);
            Map<String, String> storeCode2NameMap = storeDTOList.stream().collect(Collectors.toMap(StoreDTO::getCode, StoreDTO::getName, (key1, key2) -> key1));
            if (warehouseRecord.getRecordType().equals(WarehouseRecordTypeEnum.DS_RETURN_OUT_WAREHOUSE_RECORD.getType()) || warehouseRecord.getRecordType().equals(WarehouseRecordTypeEnum.LS_RETURN_OUT_WAREHOUSE_RECORD.getType())) {
                doMain.setReceiverCode(shopReturnE.getInFactoryCode());
                doMain.setReceiverName(storeCode2NameMap.get(shopReturnE.getInFactoryCode()));
            } else {
                doMain.setReceiverCode(shopReturnE.getInColdFactoryCode());
                doMain.setReceiverName(storeCode2NameMap.get(shopReturnE.getInColdFactoryCode()));
            }
            doMain.setFactoryName(storeCode2NameMap.get(warehouseRecord.getFactoryCode()));
            //查询前置单明细
            List<ShopReturnDetailE> shopReturnDetailEList = shopReturnDetailMapper.selectByRecordCode(shopReturnE.getRecordCode());
            //当前后置单明细
            List<String> deliveryLineNoList = warehouseRecord.getWarehouseRecordDetailList().stream()
                    .map(WarehouseRecordDetailE::getDeliveryLineNo).distinct().collect(Collectors.toList());
            //过滤掉非当前后置单明细对应的前置单明细
            List<ShopReturnDetailE> currentWhRecordDetailList = shopReturnDetailEList.stream()
                    .filter(r -> deliveryLineNoList.contains(String.valueOf(r.getId()))).collect(Collectors.toList());
            frontRecordDetailId2UnitCode = currentWhRecordDetailList.stream()
                    .collect(Collectors.toMap(k -> String.valueOf(k.getId()), ShopReturnDetailE::getUnitCode, (key1, key2) -> key1));
        } else {
            log.error("前置单类型错误，不为【仓库调拨,直营门店补货单,加盟门店补货单,门店供应商直送交货单,门店冷链交货单】，无需同步，后置单编码：{}，前置单编码：{}，前置单类型：{}",
                    warehouseRecord.getRecordCode(), relation.getFrontRecordCode(), frontRecordType);
            throw new RomeException(ResCode.ORDER_ERROR_6037, ResCode.ORDER_ERROR_6037_DESC);
        }
        return frontRecordDetailId2UnitCode;
    }

    /**
     * 构造单位转换列表
     */
    private List<RecordSkuUnitDTO> createRecordSkuUnitList(List<SkuUnitExtDTO> skuUnitList, String recordUnitCode, String frontUnitCode, String transportUnitCode) {
        List<RecordSkuUnitDTO> recordSkuUnitList = new ArrayList<>();
        Map<String, SkuUnitExtDTO> unitCode2InfoMap = skuUnitList.stream().collect(Collectors.toMap(SkuUnitExtDTO::getUnitCode, Function.identity(), (key1, key2) -> key1));
        //后置单 单位
        SkuUnitExtDTO recordSkuUnit = unitCode2InfoMap.get(recordUnitCode);
        RecordSkuUnitDTO recordSkuUnitDTO = new RecordSkuUnitDTO();
        if (null != recordSkuUnit) {
            recordSkuUnitDTO.setUnitCode(recordSkuUnit.getUnitCode());
            recordSkuUnitDTO.setUnitName(recordSkuUnit.getUnitName());
            recordSkuUnitDTO.setScale(recordSkuUnit.getScale());
            recordSkuUnitDTO.setUnitType(SkuUnitTypeEnum.BASIS_UNIT.getId().intValue());
        }
        recordSkuUnitList.add(recordSkuUnitDTO);
        //前置单 单位
        SkuUnitExtDTO frontSkuUnit = unitCode2InfoMap.get(frontUnitCode);
        RecordSkuUnitDTO frontSkuUnitDTO = new RecordSkuUnitDTO();
        if (null != frontSkuUnit) {
            frontSkuUnitDTO.setUnitCode(frontSkuUnit.getUnitCode());
            frontSkuUnitDTO.setUnitName(frontSkuUnit.getUnitName());
            frontSkuUnitDTO.setScale(frontSkuUnit.getScale());
            frontSkuUnitDTO.setUnitType(SkuUnitTypeEnum.SALES_UNIT.getId().intValue());
        }
        recordSkuUnitList.add(frontSkuUnitDTO);
        //箱 单位
        SkuUnitExtDTO boxSkuUnit = unitCode2InfoMap.get(transportUnitCode);
        RecordSkuUnitDTO boxSkuUnitDTO = new RecordSkuUnitDTO();
        if (null != boxSkuUnit) {
            boxSkuUnitDTO.setUnitCode(boxSkuUnit.getUnitCode());
            boxSkuUnitDTO.setUnitName(boxSkuUnit.getUnitName());
            boxSkuUnitDTO.setScale(boxSkuUnit.getScale());
            boxSkuUnitDTO.setUnitType(SkuUnitTypeEnum.TRANSPORT_UNIT.getId().intValue());
        }
        recordSkuUnitList.add(boxSkuUnitDTO);
        return recordSkuUnitList;
    }

    /**
     * @param warehouseRecord
     * @return
     * @Description: 【定时器】 处理待同步交易的单子 <br>
     * @Author chuwenchao 2020/6/19
     */
    @Override
    public void processSyncTradeStatus(WarehouseRecordPageDTO warehouseRecord) {
        //查询后置单对应前置单
        List<FrontWarehouseRecordRelationE> relationEList = frontWarehouseRecordRelationMapper.getFrontRelationByRecordCode(warehouseRecord.getRecordCode());
        AlikAssert.isNotEmpty(relationEList, ResCode.ORDER_ERROR_5120, ResCode.ORDER_ERROR_5120_DESC);
        //查询门店补货单
        List<Long> frontIds = relationEList.stream().map(FrontWarehouseRecordRelationE::getFrontRecordId).distinct().collect(Collectors.toList());
        List<ReplenishRecordE> replenishRecordEList = replenishRecordMapper.queryReplenishRecordByIds(frontIds);
        AlikAssert.isNotEmpty(replenishRecordEList, ResCode.ORDER_ERROR_5106, ResCode.ORDER_ERROR_5106_DESC);
        Map<String, ReplenishRecordE> frontSapNoMap = replenishRecordEList.stream().collect(Collectors.toMap(ReplenishRecordE::getSapPoNo, Function.identity(), (v1, v2) -> v1));
        Map<Long, ReplenishRecordE> frontIdMap = replenishRecordEList.stream().collect(Collectors.toMap(ReplenishRecordE::getId, Function.identity(), (v1, v2) -> v1));
        //查询前置单明细
        Map<String, Map<String, ReplenishDetailE>> skuMap = new HashMap<>();
        for (Long frontId : frontIds) {
            ReplenishRecordE frontRecord = frontIdMap.get(frontId);
            List<ReplenishDetailE> replenishDetailEList = replenishRecordDetailMapper.queryDetailByRecordId(frontId);
            Map<String, ReplenishDetailE> skuIdDetailsMap = replenishDetailEList.stream().collect(Collectors.toMap(ReplenishDetailE::getLineNo, Function.identity(), (v1, v2) -> v1));
            skuMap.put(frontRecord.getSapPoNo(), skuIdDetailsMap);
        }
        //后置单明细
        List<WarehouseRecordDetailE> detailList = warehouseRecordDetailMapper.queryListByRecordId(warehouseRecord.getId());
        UpdatePoDTO poUpdateQuantityDTO = new UpdatePoDTO();
        poUpdateQuantityDTO.setDoNo(warehouseRecord.getRecordCode());
        List<UpdatePoLineDTO> lines = new ArrayList<>();
        poUpdateQuantityDTO.setPoLineQuantityDTOs(lines);

        for (WarehouseRecordDetailE detailE : detailList) {
            UpdatePoLineDTO poLineQuantityDTO = new UpdatePoLineDTO();
            poLineQuantityDTO.setLineNo(detailE.getLineNo());
            ReplenishRecordE tempFront = frontSapNoMap.get(detailE.getSapPoNo());
            ReplenishDetailE tempFrontDetail = skuMap.get(detailE.getSapPoNo()).get(detailE.getLineNo());
            AlikAssert.isNotNull(tempFrontDetail, ResCode.ORDER_ERROR_5121, ResCode.ORDER_ERROR_5121_DESC);
            poLineQuantityDTO.setSkuId(detailE.getSkuId());
            poLineQuantityDTO.setPoNo(tempFront.getOutRecordCode());
            poLineQuantityDTO.setSkuCode(detailE.getSkuCode());
            poLineQuantityDTO.setSaleUnitCode(tempFrontDetail.getUnitCode());
            poLineQuantityDTO.setActualDeliveryQuantity(detailE.getPlanQty().divide(tempFrontDetail.getSkuScale(), StockCoreConsts.DECIMAL_POINT_NUM, ROUND_DOWN));
            poLineQuantityDTO.setActualReceiveQuantity(detailE.getActualQty().divide(tempFrontDetail.getSkuScale(), StockCoreConsts.DECIMAL_POINT_NUM, ROUND_DOWN));
            lines.add(poLineQuantityDTO);
        }
        AlikAssert.notEmpty(lines, ResCode.ORDER_ERROR_5121, ResCode.ORDER_ERROR_5121_DESC);
        //调用交易接口
        boolean response = transactionFacade.pushTransactionStatus(poUpdateQuantityDTO);
        if (!response) {
            throw new RomeException(ResCode.ORDER_ERROR_5121, ResCode.ORDER_ERROR_5121_DESC + ":" + warehouseRecord.getRecordCode());
        }
        //修改后置单同步交易状态
        warehouseRecordMapper.updateSyncTradeStatus(warehouseRecord.getId());
    }

    @Override
    public List<String> queryOutWhRecordByInWhRecord(String recordCode) {
        if (StringUtils.isEmpty(recordCode)) {
            log.info("入库单编码为空");
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        WarehouseRecordE whRecord = warehouseRecordMapper.queryByRecordCode(recordCode);
        if (null == whRecord || !WarehouseRecordBusinessTypeEnum.IN_WAREHOUSE_RECORD.getType().equals(whRecord.getBusinessType())) {
            log.info("单号为：{}的入库单不存在", recordCode);
            throw new RomeException(ResCode.ORDER_ERROR_6040, "单号为：" + recordCode + "的" + ResCode.ORDER_ERROR_6040_DESC);
        }
        String frontRecordCode = frontWarehouseRecordRelationMapper.getFrontRecordCodeByRecordCode(recordCode);
        if (StringUtils.isEmpty(frontRecordCode)) {
            log.info("单号为：{}的入库单关系不存在", recordCode);
            throw new RomeException(ResCode.ORDER_ERROR_5120, ResCode.ORDER_ERROR_5120_DESC);
        }
        List<Long> whRecodeIdList = frontWarehouseRecordRelationMapper.queryWarehouseRecordIdByRecord(frontRecordCode);
        if (CollectionUtils.isNotEmpty(whRecodeIdList)) {
            List<WarehouseRecordE> whRecordList = warehouseRecordMapper.queryWarehouseRecordByIds(whRecodeIdList);
            List<String> outRecordCodes = whRecordList.stream().filter(record -> WarehouseRecordBusinessTypeEnum.OUT_WAREHOUSE_RECORD.getType() == record.getBusinessType())
                    .map(p -> p.getRecordCode()).distinct().collect(Collectors.toList());
            return outRecordCodes;
        }
        return new ArrayList<>();
    }


    /**
     * 根据出库单查询前置单信息
     *
     * @param type
     * @param relationEntityList
     * @param entityList
     * @return
     */
    public List<WarehouseRecordE> queryOutFrontRecord(Integer type, List<FrontWarehouseRecordRelationE> relationEntityList, List<WarehouseRecordE> entityList) {
        List<Long> idList = relationEntityList.stream().map(FrontWarehouseRecordRelationE::getFrontRecordId).collect(Collectors.toList());
        List<FrontRecordE> frontRecords = null;
        //门店盘点单
        if (type.equals(FrontRecordTypeEnum.SHOP_INVENTORY_RECORD.getType())) {
            frontRecords = shopInventoryConvertor.convertETOEntity(shopInventoryMapper.queryFrontRecordByIds(idList));
        }
        //门店调拨
        if (type.equals(FrontRecordTypeEnum.SHOP_ALLOCATION_RECORD.getType())) {
            frontRecords = shopInventoryConvertor.convertETOEntity(shopInventoryMapper.queryFrontRecordByIds(idList));
        }
        //门店试吃
        if (type.equals(FrontRecordTypeEnum.SHOP_FORETASTE_RECORD.getType())) {
            frontRecords = shopInventoryConvertor.convertETOEntity(shopInventoryMapper.queryFrontRecordByIds(idList));
        }
        //门店零售
        if (type.equals(FrontRecordTypeEnum.SHOP_RETAIL_RECORD.getType())) {
            frontRecords = shopInventoryConvertor.convertETOEntity(shopInventoryMapper.queryFrontRecordByIds(idList));
        }
        //仓库加工
        if (type.equals(FrontRecordTypeEnum.WAREHOUSE_ASSEMBLE_TASK_RECORD.getType())) {
            frontRecords = shopInventoryConvertor.convertETOEntity(shopInventoryMapper.queryFrontRecordByIds(idList));
        }
        //仓库反拆
        if (type.equals(FrontRecordTypeEnum.WAREHOUSE_REVERSE_DISASSEMBLY_TASK_RECORD.getType())) {
            frontRecords = shopInventoryConvertor.convertETOEntity(shopInventoryMapper.queryFrontRecordByIds(idList));
        }
        //仓库盘点
        if (type.equals(FrontRecordTypeEnum.WAREHOUSE_INVENTORY_RECORD.getType())) {
            frontRecords = shopInventoryConvertor.convertETOEntity(shopInventoryMapper.queryFrontRecordByIds(idList));
        }
        //仓库报废出库单
        if (type.equals(FrontRecordTypeEnum.WAREHOUSE_CONSUME_ADJUST_RECORD.getType())) {
            //frontRecords = frAdjustConsumeRepository.queryFrontRecordByIds(idList);
        }
        //仓库调拨
        if (type.equals(FrontRecordTypeEnum.WAREHOUSE_ALLOCATION_RECORD.getType())) {
            frontRecords = frontRecordConvertor.convertETOFrontRecordE(whAllocationMapper.queryFrontRecordByIds(idList));
        }
        //大仓采购退货
        if (type.equals(FrontRecordTypeEnum.WAREHOUSE_RETURN_GOODS_RECORD.getType())) {
            //frontRecords = frPurchaseOrderRepository.queryFrontRecordByIds(idList);
        }
        //直营门店补货出库单
        if (type.equals(FrontRecordTypeEnum.SHOP_REPLENISHMENT_RECORD.getType())) {
            frontRecords = replenishRecordConvert.convertETOE(replenishRecordMapper.queryReplenishRecordByIds(idList));
        }
        //直营门店退货出库单
        if (type.equals(FrontRecordTypeEnum.DIRECT_SHOP_RETURN_GOODS_RECORD.getType())) {
            frontRecords = shopReturnConvert.convertEtoE(shopReturnMapper.queryShopReturnByIdS(idList));
        }
        //加盟门店补货单
        if (type.equals(FrontRecordTypeEnum.JOIN_SHOP_REPLENISHMENT_RECORD.getType())) {
            frontRecords = replenishRecordConvert.convertETOE(replenishRecordMapper.queryReplenishRecordByIds(idList));
        }
        //加盟门店退货出库单
        if (type.equals(FrontRecordTypeEnum.JOIN_SHOP_RETURN_GOODS_RECORD.getType())) {
            frontRecords = shopReturnConvert.convertEtoE(shopReturnMapper.queryShopReturnByIdS(idList));
        }
        //门店冷链交货单
        if (type.equals(FrontRecordTypeEnum.SHOP_COLD_CHAIN_DELIVERY_RECORD.getType())) {
            frontRecords = replenishRecordConvert.convertETOE(replenishRecordMapper.queryReplenishRecordByIds(idList));
        }
        //门店供应商直送交货单
        if (type.equals(FrontRecordTypeEnum.SHOP_SUPPLIER_DIRECT_DELIVERY_RECORD.getType())) {
            frontRecords = replenishRecordConvert.convertETOE(replenishRecordMapper.queryReplenishRecordByIds(idList));
        }
        //电商零售
        if (type.equals(FrontRecordTypeEnum.ONLINE_SALE_RECORD.getType())) {
            //frontRecords = frSaleRepository.queryFrontRecordByIds(idList);
        }
        //仓库领用调整单
        if (type.equals(FrontRecordTypeEnum.WAREHOUSE_USE_ADJUST_RECORD.getType())) {
            //frontRecords = frAdjustConsumeRepository.queryFrontRecordByIds(idList);
        }
        //退货单
        if (type.equals(FrontRecordTypeEnum.GROUP_RETURN_RECORD.getType())) {
            List<OrderReturnE> orderReturnEList = orderReturnMapper.queryOrderReturnByIds(idList);
            if (CollectionUtils.isNotEmpty(orderReturnEList)) {
                List<FrontRecordE> frontRecordES = new ArrayList<>();
                orderReturnEList.forEach(orderReturnE -> {
                    FrontRecordE frontRecordE = new FrontRecordE();
                    frontRecordE.setRecordType(FrontRecordTypeEnum.GROUP_RETURN_RECORD.getType());
                    frontRecordE.setRecordCode(orderReturnE.getRecordCode());
                    frontRecordE.setId(orderReturnE.getId());
                    frontRecordES.add(frontRecordE);
                });
                frontRecords = frontRecordES;
            }
        }
        //销售中心预约do单
        if (type.equals(FrontRecordTypeEnum.RESERVATION_DO_RECORD.getType())) {
            frontRecords = null;
            List<OrderE> orderEList = orderMapper.queryOrderByIds(idList);
            if (CollectionUtils.isNotEmpty(orderEList)) {
                List<FrontRecordE> frontRecordES = new ArrayList<>();
                orderEList.forEach(orderE -> {
                    FrontRecordE frontRecordE = new FrontRecordE();
                    frontRecordE.setId(orderE.getId());
                    frontRecordES.add(frontRecordE);
                });
                for (FrontRecordE frontRecordE : frontRecordES) {
                    relationEntityList.forEach(relation -> {
                        if (frontRecordE.getId().equals(relation.getFrontRecordId())) {
                            frontRecordE.setRecordType(relation.getFrontRecordType());
                            frontRecordE.setRecordType(relation.getFrontRecordType());
                        }
                    });
                }
            }
        }
        if (CollectionUtils.isNotEmpty(frontRecords)) {
            for (FrontRecordE frontRecordE : frontRecords) {
                List<Long> warehouseIds = relationEntityList.stream().filter(relation -> type.equals(frontRecordE.getRecordType()) && frontRecordE.getId().equals(relation.getFrontRecordId()))
                        .map(FrontWarehouseRecordRelationE::getWarehouseRecordId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(warehouseIds)) {
                    entityList.stream().filter(warehouseRecord -> warehouseIds.contains(warehouseRecord.getId())).forEach(warehouse -> warehouse.setFrontRecord(frontRecordE));
                }
            }
        }
        return entityList;
    }

    /**
     * 根据入库单查询前置单信息
     *
     * @param type
     * @param relationEntityList
     * @param entityList
     * @return
     */
    public List<WarehouseRecordE> queryInFrontRecord(Integer type, List<FrontWarehouseRecordRelationE> relationEntityList, List<WarehouseRecordE> entityList) {
        List<Long> idList = relationEntityList.stream().map(FrontWarehouseRecordRelationE::getFrontRecordId).collect(Collectors.toList());
        List<FrontRecordE> frontRecords = null;
        //门店盘点单
        if (type.equals(FrontRecordTypeEnum.SHOP_INVENTORY_RECORD.getType())) {
            frontRecords = shopInventoryConvertor.convertETOEntity(shopInventoryMapper.queryFrontRecordByIds(idList));
        }
        //门店调拨
        if (type.equals(FrontRecordTypeEnum.SHOP_ALLOCATION_RECORD.getType())) {
            frontRecords = shopAllocationConvertor.convertE2E(shopAllocationMapper.queryFrontRecordByIds(idList));
        }
        //门店零售退货
        if (type.equals(FrontRecordTypeEnum.SHOP_RETURN_GOODS_RECORD.getType())) {
            frontRecords = salesReturnConvertor.convertETOE(salesReturnMapper.queryFrontRecordByIds(idList));
        }
        //仓库加工
        if (type.equals(FrontRecordTypeEnum.WAREHOUSE_ASSEMBLE_TASK_RECORD.getType())) {
            //frontRecords = frWarehouseReverseRepository.queryFrontRecordByIds(idList);
        }
        //仓库反拆
        if (type.equals(FrontRecordTypeEnum.WAREHOUSE_REVERSE_DISASSEMBLY_TASK_RECORD.getType())) {
            //frontRecords = frWarehouseReverseRepository.queryFrontRecordByIds(idList);
        }
        //仓库调拨
        if (type.equals(FrontRecordTypeEnum.WAREHOUSE_ALLOCATION_RECORD.getType())) {
            frontRecords = frontRecordConvertor.convertETOFrontRecordE(whAllocationMapper.queryFrontRecordByIds(idList));
        }
        //直营门店补货单
        if (type.equals(FrontRecordTypeEnum.SHOP_REPLENISHMENT_RECORD.getType())) {
            frontRecords = replenishRecordConvert.convertETOE(replenishRecordMapper.queryReplenishRecordByIds(idList));
        }
        //加盟门店退货入库单
        if (type.equals(FrontRecordTypeEnum.JOIN_SHOP_RETURN_GOODS_RECORD.getType())) {
            frontRecords = shopReturnConvert.convertEtoE(shopReturnMapper.queryShopReturnByIdS(idList));
        }
        //加盟门店补货单
        if (type.equals(FrontRecordTypeEnum.JOIN_SHOP_REPLENISHMENT_RECORD.getType())) {
            frontRecords = replenishRecordConvert.convertETOE(replenishRecordMapper.queryReplenishRecordByIds(idList));
        }
        //大仓采购入库
        if (type.equals(FrontRecordTypeEnum.WAREHOUSE_PURCHASE_RECORD.getType())) {
            //frontRecords = frPurchaseOrderRepository.queryFrontRecordByIds(idList);
        }
        //仓库盘点单
        if (type.equals(FrontRecordTypeEnum.WAREHOUSE_INVENTORY_RECORD.getType())) {
            //frontRecords = frWarehouseInventoryRepository.queryFrontRecordByIds(idList);
        }
        //直营门店退货入库单
        if (type.equals(FrontRecordTypeEnum.DIRECT_SHOP_RETURN_GOODS_RECORD.getType())) {
            frontRecords = shopReturnConvert.convertEtoE(shopReturnMapper.queryShopReturnByIdS(idList));
        }
        //冷链采购单
        if (type.equals(FrontRecordTypeEnum.COLD_CHAIN_SURPASS_WAREHOUSE_RECORD.getType())) {
            //frontRecords = frPurchaseOrderRepository.queryFrontRecordByIds(idList);
        }
        //门店冷链交货单
        if (type.equals(FrontRecordTypeEnum.SHOP_COLD_CHAIN_DELIVERY_RECORD.getType())) {
            frontRecords = replenishRecordConvert.convertETOE(replenishRecordMapper.queryReplenishRecordByIds(idList));
        }
        //供应商直送采购
        if (type.equals(FrontRecordTypeEnum.SUPPLIER_DIRECT_DELIVERY_RECORD.getType())) {
            frontRecords = replenishRecordConvert.convertETOE(replenishRecordMapper.queryReplenishRecordByIds(idList));
        }
        //门店供应商直送交货单
        if (type.equals(FrontRecordTypeEnum.SHOP_SUPPLIER_DIRECT_DELIVERY_RECORD.getType())) {
            frontRecords = replenishRecordConvert.convertETOE(replenishRecordMapper.queryReplenishRecordByIds(idList));
        }
        //退货预入库
        if (type.equals(FrontRecordTypeEnum.PREDICT_RETURN_RECORD.getType())) {
            //frontRecords = frPredictReturnRepository.queryFrontRecordByIds(idList);
        }
        if (CollectionUtils.isNotEmpty(frontRecords)) {
            for (FrontRecordE frontRecordE : frontRecords) {
                List<Long> warehouseIds = relationEntityList.stream().filter(relation -> frontRecordE.getId().equals(relation.getFrontRecordId()))
                        .map(FrontWarehouseRecordRelationE::getWarehouseRecordId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(warehouseIds)) {
                    entityList.stream().filter(warehouseRecord -> warehouseIds.contains(warehouseRecord.getId())).forEach(warehouse -> warehouse.setFrontRecord(frontRecordE));
                }
            }
        }
        return entityList;
    }

    /**
     * @return java.util.List<com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE>
     * @Description 通过订单号查询后置单信息  包含：对应的明细记录
     * @Author Lin.Xu
     * @Date 9:55 2020/7/17
     * @Param [orderNos]
     **/
    @Override
    public List<WarehouseRecordE> queryWarehouseRecordByOrderNos(List<String> orderNos) {
        //通过单号集合查询后置单记录和后置单明细记录
        List<WarehouseRecordE> warehouseRecordES = warehouseRecordMapper.getRecordTypeByRecordCodes(orderNos);
        List<WarehouseRecordDetailE> warehouseRecordDetailES = warehouseRecordDetailMapper.selectListByRecordCodes(orderNos);
        if (CollectionUtils.isEmpty(warehouseRecordES) || CollectionUtils.isEmpty(warehouseRecordDetailES)) {
            return null;
        }
        //明细按照recordCode进行分类
        Map<String, List<WarehouseRecordDetailE>> warehouseRecordDetailEMap = RomeCollectionUtil.listforListMap(warehouseRecordDetailES, "recordCode");
        for (WarehouseRecordE record : warehouseRecordES) {
            String recordNo = record.getRecordCode();
            List<WarehouseRecordDetailE> detailES = warehouseRecordDetailEMap.get(recordNo);
            record.setWarehouseRecordDetailList(detailES);
        }
        return warehouseRecordES;
    }


}
