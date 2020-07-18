package com.lyf.scm.core.service.stockFront.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.KibanaLogConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.*;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.stockFront.*;
import com.lyf.scm.core.config.ServiceKibanaLog;
import com.lyf.scm.core.domain.convert.stockFront.SaleWarehouseRecordConvertor;
import com.lyf.scm.core.domain.convert.stockFront.ShopSaleConvertor;
import com.lyf.scm.core.domain.convert.stockFront.StockRecordDTOConvert;
import com.lyf.scm.core.domain.entity.stockFront.FrontWarehouseRecordRelationE;
import com.lyf.scm.core.domain.entity.stockFront.ShopSaleE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.stockFront.FrontWarehouseRecordRelationMapper;
import com.lyf.scm.core.mapper.stockFront.ShopSaleDetailMapper;
import com.lyf.scm.core.mapper.stockFront.ShopSaleMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.remote.base.dto.ChannelDTO;
import com.lyf.scm.core.remote.base.facade.BaseFacade;
import com.lyf.scm.core.remote.item.ItemInfoTool;
import com.lyf.scm.core.remote.item.SkuQtyUnitTool;
import com.lyf.scm.core.remote.item.dto.SkuInfoExtDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.stock.dto.*;
import com.lyf.scm.core.remote.stock.facade.StockQueryFacade;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.remote.stock.facade.StockRecordFacade;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.stockFront.ShopSaleService;
import com.lyf.scm.core.service.stockFront.ShopSaleToWareHouseRecordService;
import com.lyf.scm.core.service.stockFront.WarehouseRecordService;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 门店销售
 * @author zhanglong
 */
@Service
@Slf4j
public class ShopSaleServiceImpl implements ShopSaleService {

    @Resource
    private ShopSaleMapper shopSaleMapper;
    @Resource
    private ShopSaleDetailMapper shopSaleDetailMapper;
    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;
    @Resource
    private ShopSaleConvertor shopSaleConvertor;
    @Resource
    private SaleWarehouseRecordConvertor saleWarehouseRecordConvertor;
    @Resource
    private SkuQtyUnitTool skuQtyUnitTool;
    @Resource
    private ItemInfoTool itemInfoTool;
    @Resource
    private ShopSaleToWareHouseRecordService shopSaleToWareHouseRecordService;
    @Resource
    private StockRecordFacade stockRecordFacade;
    @Resource
    private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;
    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;
    @Resource
    private BaseFacade baseFacade;
    @Resource
    private WarehouseRecordService warehouseRecordService;
    @Resource
    private ItemFacade itemFacade;
    @Resource
    private OrderUtilService orderUtilService;
    @Resource
    private StockQueryFacade stockQueryFacade;
    @Resource
    private StockRecordDTOConvert stockRecordDTOConvert;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addShopSaleRecord(ShopSaleRecordDTO frontRecord) {
        ShopSaleE shopSaleE = shopSaleMapper.selectFrSaleRecordByOutRecordCode(frontRecord.getOutRecordCode());
        if (null != shopSaleE) {
            //单据已存在，直接返回
            return;
        }
        RealWarehouse realWarehouse = stockRealWarehouseFacade.queryRealWarehouseByShopCode(frontRecord.getShopCode());
        AlikAssert.isNotNull(realWarehouse, "999", "当前仓库不存在");
        ShopSaleE frontRecordE = shopSaleConvertor.shopSaleDto2Entity(frontRecord);
        frontRecordE.setRealWarehouseId(realWarehouse.getId());
        frontRecordE.setFactoryCode(realWarehouse.getFactoryCode());
        frontRecordE.setRealWarehouseCode(realWarehouse.getRealWarehouseOutCode());
        frontRecordE.setRecordStatus(FrontRecordStatusEnum.OUT_ALLOCATION.getStatus());
        frontRecordE.setRecordType(FrontRecordTypeEnum.SHOP_RETAIL_RECORD.getType());
        //创建门店销售单（前置单）
        createFrontShopSaleRecord(frontRecordE);

        //根据前置单生成出库单数据
        WarehouseRecordE warehouseRecord = shopSaleToWareHouseRecordService.createWarehouseRecordByShopSale(frontRecordE);

        //输出kibana日志
        log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.SHOP_SALE, "addShopSaleRecord", "门店零售: " +
                frontRecord.getOutRecordCode(), frontRecord));
        try {
            //同步出库单 到 库存中心
            OutWarehouseRecordDTO outRecordDto = stockRecordDTOConvert.convertE2OutDTO(warehouseRecord);
            for (WarehouseRecordDetailE detail : warehouseRecord.getWarehouseRecordDetailList()) {
                outRecordDto.getDetailList().stream().filter(recordDetail -> recordDetail.getSkuCode().equals(detail.getSkuCode())).forEach(
                        rDetail -> {
                            rDetail.setLineNo(detail.getId() + "");
                            if (StringUtils.isEmpty(rDetail.getDeliveryLineNo())) {
                                rDetail.setDeliveryLineNo(detail.getId() + "");
                            }
                        }
                );
            }
            stockRecordFacade.createOutRecord(outRecordDto);
        } catch (RomeException e) {
            log.error("创建出库单异常：{}", e);
            throw new RomeException(ResCode.ORDER_ERROR_7402, ResCode.ORDER_ERROR_7402_DESC);
        } catch (Exception e) {
            log.error("创建出库单异常：{}", e);
            throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    /**
     * 取消门店零售单
     *
     * @param outRecordCode
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelShopSaleRecord(String outRecordCode) {
        ShopSaleE shopSaleE = shopSaleMapper.selectFrSaleRecordByOutRecordCode(outRecordCode);
        if (shopSaleE == null || FrontRecordStatusEnum.DISABLED.getStatus().equals(shopSaleE.getRecordStatus())) {
            return;
        }
        //修改前置单 状态
        int res = shopSaleMapper.updateToCanceled(shopSaleE.getId(), FrontRecordStatusEnum.DISABLED.getStatus());
        if (res < 1) {
            //重复调用了直接返成功
            return;
        }
        List<Long> warehouseRecordIds = frontWarehouseRecordRelationMapper.queryWarehouseIdByFrontId(shopSaleE.getId(), shopSaleE.getRecordType());
        AlikAssert.isTrue(warehouseRecordIds != null && warehouseRecordIds.size() > 0, ResCode.ORDER_ERROR_6026, ResCode.ORDER_ERROR_6026_DESC);
        Long warehouseRecordId = warehouseRecordIds.get(0);
        //修改后置单 状态
        warehouseRecordMapper.updateToCanceledFromComplete(warehouseRecordId);

        try {
            //调用库存中心 取消出库单
            WarehouseRecordE warehouseRecordE = warehouseRecordService.queryWarehouseRecordEById(warehouseRecordId);
            CancelRecordDTO cancelRecordDTO = new CancelRecordDTO();
            cancelRecordDTO.setRecordCode(warehouseRecordE.getRecordCode());
            cancelRecordDTO.setRecordType(warehouseRecordE.getRecordType());
            cancelRecordDTO.setIsForceCancel(YesOrNoEnum.NO.getType());
            CancelResultDTO result = stockRecordFacade.cancelSingleRecord(cancelRecordDTO);
            if (null == result || !result.getStatus()) {
                throw new RomeException(ResCode.ORDER_ERROR_7403, ResCode.ORDER_ERROR_7403_DESC);
            }
        } catch (RomeException e) {
            log.error("门店零售单（出库单） 取消失败，后置单ID：{}，异常：{}", warehouseRecordId, e);
            throw new RomeException(ResCode.ORDER_ERROR_7404, ResCode.ORDER_ERROR_7404_DESC);
        } catch (Exception e) {
            log.error("门店零售单（出库单） 取消失败，后置单ID：{}，异常：{}", warehouseRecordId, e);
            throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    /**
     * 列表查询
     *
     * @param condition
     * @return
     */
    @Override
    public PageInfo<SaleWarehouseRecordDTO> queryWarehouseRecordList(SaleWarehouseRecordCondition condition) {
        //1、如果查询条件里面有订单编码，则先根据订单编码查询出符合条件的出库单ids
        if (StringUtils.isNotBlank(condition.getOrderCode())) {
            List<String> orderCodes = Arrays.asList(condition.getOrderCode().split("\n|\r"));
            List<ShopSaleE> shopSaleEList = shopSaleMapper.selectFrSaleRecordByOutRecordCodeList(orderCodes);
            if (CollectionUtils.isEmpty(shopSaleEList)) {
                PageInfo<SaleWarehouseRecordDTO> personPageInfo = new PageInfo<>(new ArrayList<>());
                personPageInfo.setTotal(0);
                return personPageInfo;
            }
            List<String> frontRecordCodeList = shopSaleEList.stream().map(ShopSaleE::getRecordCode).distinct().collect(Collectors.toList());
            List<FrontWarehouseRecordRelationE> recordRelationEs = frontWarehouseRecordRelationMapper.getRecordRelationByFrontRecordCodes(frontRecordCodeList);
            if (CollectionUtils.isEmpty(recordRelationEs)) {
                PageInfo<SaleWarehouseRecordDTO> personPageInfo = new PageInfo<>(new ArrayList<>());
                personPageInfo.setTotal(0);
                return personPageInfo;
            }
            List<Long> whrIdList = recordRelationEs.stream().map(FrontWarehouseRecordRelationE::getWarehouseRecordId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(whrIdList)) {
                //用户输入的订单 编号不存在，则直接返回无结果
                PageInfo<SaleWarehouseRecordDTO> personPageInfo = new PageInfo<>(new ArrayList<>());
                personPageInfo.setTotal(0);
                return personPageInfo;
            } else {
                condition.setWarehouseRecordIds(whrIdList);
            }
        }

        //2.设置仓库信息
        String realWarehouseCode = condition.getRealWarehouseCode();
        if (StringUtils.isNotBlank(realWarehouseCode)) {
            RealWarehouse warehouse = stockRealWarehouseFacade.queryRealWarehouseByRWCode(realWarehouseCode);
            if (null != warehouse) {
                condition.setRealWarehouseCode(null);
                condition.setRealWarehouseId(warehouse.getId());
            } else {
                //用户输入仓库编码不存在，则直接返回无结果
                PageInfo<SaleWarehouseRecordDTO> personPageInfo = new PageInfo<>(new ArrayList<>());
                personPageInfo.setTotal(0);
                return personPageInfo;
            }
        }

        //3、分页查询
        Page page = PageHelper.startPage(condition.getPageIndex(), condition.getPageSize());
        List<WarehouseRecordE> warehouseRecordEList = warehouseRecordMapper.queryWarehouseRecordList(condition);
        List<SaleWarehouseRecordDTO> result = saleWarehouseRecordConvertor.warehouseConvertE2DTOList(warehouseRecordEList);
        PageInfo<SaleWarehouseRecordDTO> recordDTOPageInfo = new PageInfo<>(result);
        recordDTOPageInfo.setTotal(page.getTotal());

        //4、将出库单关联的订单编号赋值给页面dto,实仓名称赋值给页面DTO
        if (!CollectionUtils.isEmpty(recordDTOPageInfo.getList())) {
            //查询仓库信息
            List<QueryRealWarehouseDTO> qrwList = new ArrayList<>();
            recordDTOPageInfo.getList().forEach(record -> {
                if (StringUtils.isNotEmpty(record.getFactoryCode()) && StringUtils.isNotEmpty(record.getRealWarehouseCode())) {
                    QueryRealWarehouseDTO warehouseDTO = new QueryRealWarehouseDTO();
                    warehouseDTO.setFactoryCode(record.getFactoryCode());
                    warehouseDTO.setWarehouseOutCode(record.getRealWarehouseCode());
                    if (!qrwList.contains(warehouseDTO)) {
                        qrwList.add(warehouseDTO);
                    }
                }
            });
            List<RealWarehouse> realWarehouseList = new ArrayList<>();
            if (null != qrwList && qrwList.size() > 0) {
                realWarehouseList = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(qrwList);
            }
            Map<Long, RealWarehouse> realWarehouseMap = realWarehouseList.stream().collect(Collectors.toMap(RealWarehouse::getId, Function.identity(), (v1, v2) -> v1));

            //查询后置单Id---外部单号集合 映射
            List<Long> warehouseRecordIds = recordDTOPageInfo.getList().stream().map(SaleWarehouseRecordDTO::getId).distinct().collect(Collectors.toList());
            List<FrontWarehouseRecordRelationE> recordRelationEList = frontWarehouseRecordRelationMapper.queryFrontRecordListByIdList(warehouseRecordIds);
            List<Long> frontRecordIdList = recordRelationEList.stream().map(FrontWarehouseRecordRelationE::getFrontRecordId).distinct().collect(Collectors.toList());
            //批量查询前置单列表
            List<ShopSaleE> shopSaleList = shopSaleMapper.batchQueryShopSalesByIds(frontRecordIdList);
            //前置单编码--->外部编码（外部订单号）
            Map<String, String> frontRecordCode2OueRecordCode = shopSaleList.stream().collect(Collectors.toMap(ShopSaleE::getRecordCode, ShopSaleE::getOutRecordCode));
            Map<Long, List<String>> whId2OutRecordMap = recordRelationEList.stream()
                    .collect(Collectors.toMap(FrontWarehouseRecordRelationE::getWarehouseRecordId, p -> {
                                List<String> outRecordCodes = new ArrayList<>();
                                outRecordCodes.add(frontRecordCode2OueRecordCode.get(p.getFrontRecordCode()));
                                return outRecordCodes;
                            },
                            (List<String> outRecordCodes1, List<String> outRecordCodes2) -> {
                                outRecordCodes1.addAll(outRecordCodes2);
                                return outRecordCodes1;
                            }));

            //批量查询渠道信息
            List<ChannelDTO> channelInfoList = new ArrayList<>();
            try {
                List<String> channelCodeList = recordDTOPageInfo.getList().stream().map(SaleWarehouseRecordDTO::getChannelCode).distinct().collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(channelCodeList)) {
                    channelInfoList = baseFacade.queryChannelInfoByChannelCodeList(channelCodeList);
                }
            } catch (Exception e) {
                log.info("查询渠道信息异常：{}", e);
            }
            Map<String, ChannelDTO> channelInfoMap = channelInfoList.stream().collect(Collectors.toMap(ChannelDTO::getChannelCode, Function.identity(), (v1, v2) -> v1));

            //设置显示属性
            for (SaleWarehouseRecordDTO dto : recordDTOPageInfo.getList()) {
                RealWarehouse outRealWarehouse = realWarehouseMap.get(dto.getRealWarehouseId());
                dto.setOutRecordCode(whId2OutRecordMap.get(dto.getId()));
                dto.setRecordStatusName(WarehouseRecordStatusEnum.getDescByType(dto.getRecordStatus()));
                dto.setRecordTypeName(WarehouseRecordTypeEnum.getDescByType(dto.getRecordType()));
                if (null != outRealWarehouse) {
                    dto.setRealWarehouseName(outRealWarehouse.getRealWarehouseName());
                    dto.setRealWarehouseCode(outRealWarehouse.getRealWarehouseCode());
                    dto.setRealWarehouseType(outRealWarehouse.getRealWarehouseType());
                }
                if (channelInfoMap.containsKey(dto.getChannelCode())) {
                    dto.setChannelCodeName(channelInfoMap.get(dto.getChannelCode()).getChannelName());
                }
            }
        }
        return recordDTOPageInfo;
    }

    /**
     * 根据门店销售出库单查询详情
     *
     * @param warehouseRecordId
     * @return
     */
    @Override
    public SaleWarehouseRecordDTO querySaleWarehouseRecordInfoById(Long warehouseRecordId) {
        //查询后置单+明细信息
        WarehouseRecordE record = warehouseRecordService.queryWarehouseRecordEById(warehouseRecordId);
        List<WarehouseRecordDetailE> warehouseRecordDetails = record.getWarehouseRecordDetailList();
        SaleWarehouseRecordDTO result = saleWarehouseRecordConvertor.warehouseRecordE2SaleDTO(record);
        List<SaleWarehouseRecordDetailDTO> details = saleWarehouseRecordConvertor.convertEList2SaleWarehouseDTOList(warehouseRecordDetails);

        //查询实仓信息
        RealWarehouse realWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(record.getRealWarehouseCode(), record.getFactoryCode());
        if (null != realWarehouse) {
            result.setRealWarehouseName(realWarehouse.getRealWarehouseName());
            result.setRealWarehouseAddress(realWarehouse.getRealWarehouseAddress());
            result.setRealWarehouseCode(realWarehouse.getRealWarehouseCode());
        }
        result.setRecordTypeName(WarehouseRecordTypeEnum.getDescByType(record.getRecordType()));
        result.setRecordStatusName(WarehouseRecordStatusEnum.getDescByType(record.getRecordStatus()));

        //查询渠道信息
        if (StringUtils.isNotBlank(result.getChannelCode())) {
            try {
                ChannelDTO channelDTO = baseFacade.queryChannelInfoByCode(result.getChannelCode());
                if (null != channelDTO) {
                    result.setChannelCodeName(channelDTO.getChannelName());
                }
            } catch (Exception e) {
                log.info("查询渠道信息异常：{}", e);
            }
        }

        //查询批次信息
        List<BatchStockChangeFlowDTO> batchInfoList = stockQueryFacade.queryBatchInfoByRecordCode(record.getRecordCode());
        Map<Long, List<BatchStockChangeFlowDTO>> batchInfoMap = batchInfoList.stream().collect(Collectors.groupingBy(stock -> stock.getSkuId()));

        //查询商品名称信息
        List<SkuInfoExtDTO> skuInfoList = new ArrayList<>();
        try {
            List<String> skuCodeList = details.stream().map(SaleWarehouseRecordDetailDTO::getSkuCode).distinct().collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(skuCodeList)) {
                skuInfoList = itemFacade.skuListBySkuCodes(skuCodeList);
            }
        } catch (Exception e) {
            log.info("查询商品信息异常：{}", e);
        }
        Map<Long, SkuInfoExtDTO> skuInfoMap = skuInfoList.stream().collect(Collectors.toMap(SkuInfoExtDTO::getId, Function.identity(), (v1, v2) -> v1));

        //拼装详情信息
        for (SaleWarehouseRecordDetailDTO detail : details) {
            Long skuId = detail.getSkuId();
            detail.setRealWarehouseName(realWarehouse.getRealWarehouseName());
            if (skuInfoMap.containsKey(skuId)) {
                detail.setSkuName(skuInfoMap.get(skuId).getName());
            }
            if (batchInfoMap.containsKey(skuId)) {
                detail.setBatchStockChangeFlowList(batchInfoMap.get(skuId));
            }
        }
        result.setDetails(details);
        return result;
    }

    /**
     * 创建前置单 明细
     *
     * @param frontRecordE
     */
    private void createFrontShopSaleRecord(ShopSaleE frontRecordE) {
        //获取单据编号
        String code = orderUtilService.queryOrderCode(FrontRecordTypeEnum.SHOP_RETAIL_RECORD.getCode());
        frontRecordE.setRecordCode(code);
        //设置商品code或id
        itemInfoTool.convertSkuCode(frontRecordE.getFrontRecordDetails());
        //单位转换
        skuQtyUnitTool.convertRealToBasic(frontRecordE.getFrontRecordDetails());
        if (StringUtils.isBlank(frontRecordE.getUserCode())) {
            frontRecordE.setUserCode("");
        }
        if (StringUtils.isBlank(frontRecordE.getMobile())) {
            frontRecordE.setMobile("");
        }
        if (StringUtils.isBlank(frontRecordE.getRecordStatusReason())) {
            frontRecordE.setRecordStatusReason("");
        }
        //门店零售默认交易类型为6-POS门店
        frontRecordE.setTransType(OnlineStockTransTypeEnum.TRANS_TYPE_6.getTransType());
        //保存门店销售单
        shopSaleMapper.saveFrSaleRecord(frontRecordE);
        //门店销售单详情关联数据
        frontRecordE.getFrontRecordDetails().forEach(record -> {
            record.setFrontRecordId(frontRecordE.getId());
            record.setRecordCode(frontRecordE.getRecordCode());
        });
        //保存门店销售单详情
        shopSaleDetailMapper.saveFrSaleRecordDetails(frontRecordE.getFrontRecordDetails());
    }
}
