package com.lyf.scm.core.service.stockFront.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.KibanaLogConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.*;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.stockFront.SaleWarehouseRecordDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.SalesReturnRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.SalesReturnRecordParamDTO;
import com.lyf.scm.core.api.dto.stockFront.SalesReturnWarehouseRecordDTO;
import com.lyf.scm.core.config.ServiceKibanaLog;
import com.lyf.scm.core.domain.convert.stockFront.SalesReturnConvertor;
import com.lyf.scm.core.domain.convert.stockFront.SalesReturnDetailConvertor;
import com.lyf.scm.core.domain.convert.stockFront.StockInRecordDTOConvert;
import com.lyf.scm.core.domain.entity.stockFront.FrontWarehouseRecordRelationE;
import com.lyf.scm.core.domain.entity.stockFront.SalesReturnE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.stockFront.FrontWarehouseRecordRelationMapper;
import com.lyf.scm.core.mapper.stockFront.SalesReturnDetailMapper;
import com.lyf.scm.core.mapper.stockFront.SalesReturnMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.remote.base.dto.ChannelDTO;
import com.lyf.scm.core.remote.base.facade.BaseFacade;
import com.lyf.scm.core.remote.item.ItemInfoTool;
import com.lyf.scm.core.remote.item.SkuQtyUnitTool;
import com.lyf.scm.core.remote.item.dto.SkuInfoExtDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.stock.dto.*;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.remote.stock.facade.StockRecordFacade;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.stockFront.SalesReturnService;
import com.lyf.scm.core.service.stockFront.SalesReturnToWareHouseRecordService;
import com.lyf.scm.core.service.stockFront.WarehouseRecordService;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 门店零售退货
 * @author zhanlong
 */
@Slf4j
@Service("salesReturnService")
public class SalesReturnServiceImpl implements SalesReturnService {

    @Resource
    private SalesReturnMapper salesReturnMapper;
    @Resource
    private SalesReturnDetailMapper salesReturnDetailMapper;
    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;
    @Resource
    private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;
    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;
    @Resource
    private StockRecordFacade stockRecordFacade;
    @Resource
    private BaseFacade baseFacade;
    @Resource
    private ItemFacade itemFacade;
    @Resource
    private SalesReturnConvertor salesReturnConvertor;
    @Resource
    private SalesReturnDetailConvertor salesReturnDetailConvertor;
    @Resource
    private SkuQtyUnitTool skuQtyUnitTool;
    @Resource
    private ItemInfoTool itemInfoTool;
    @Resource
    private SalesReturnToWareHouseRecordService salesReturnToWareHouseRecordService;
    @Resource
    private WarehouseRecordService warehouseRecordService;
    @Resource
    private OrderUtilService orderUtilService;
    @Resource
    private StockInRecordDTOConvert stockInRecordDTOConvert;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addShopSaleReturnRecord(SalesReturnRecordDTO frontRecord) {
        //幂等性判断
        SalesReturnE salesReturnE = salesReturnMapper.selectFrSalesReturnRecordByOutRecordCode(frontRecord.getOutRecordCode());
        if (null != salesReturnE) {
            //单据已存在
            return;
        }
        RealWarehouse realWarehouse = stockRealWarehouseFacade.queryRealWarehouseByShopCode(frontRecord.getShopCode());
        AlikAssert.isNotNull(realWarehouse, "999", "当前仓库不存在");

        SalesReturnE frontRecordE = salesReturnConvertor.salesReturnDtoToEntity(frontRecord);
        frontRecordE.setInRealWarehouseId(realWarehouse.getId());
        frontRecordE.setInFactoryCode(realWarehouse.getFactoryCode());
        frontRecordE.setInRealWarehouseCode(realWarehouse.getRealWarehouseOutCode());
        //退货单为完成状态
        frontRecordE.setRecordStatus(FrontRecordStatusEnum.IN_ALLOCATION.getStatus());
        //创建门店退货单 和 明细
        addSalesReturnFrontRecord(frontRecordE);

        //保存后置单据 入库单
        WarehouseRecordE inWarehouseRecord = salesReturnToWareHouseRecordService.createWarehouseRecordBySalesReturn(frontRecordE);

        //输出kibana日志
        log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.SALES_RETURN, "addShopSaleReturnRecord", "门店零售退货单: " +
                frontRecord.getOutRecordCode(), frontRecord));
        try {
            //同步入库单 到 库存中心
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
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            throw new RomeException(ResCode.ORDER_ERROR_7502, ResCode.ORDER_ERROR_7502_DESC);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    /**
     * 取消门店销售退货单
     *
     * @param outRecordCode
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelShopSaleReturnRecord(String outRecordCode) {
        SalesReturnE salesReturnE = salesReturnMapper.selectFrSalesReturnRecordByOutRecordCode(outRecordCode);
        if (salesReturnE == null || FrontRecordStatusEnum.DISABLED.getStatus().equals(salesReturnE.getRecordStatus())) {
            return;
        }
        //更新状态
        int res = salesReturnMapper.updateToCanceled(salesReturnE.getId(), FrontRecordStatusEnum.DISABLED.getStatus());
        if (res < 1) {
            //重复调用了直接返成功
            return;
        }
        List<Long> warehouseIds = frontWarehouseRecordRelationMapper.queryWarehouseIdByFrontId(salesReturnE.getId(), salesReturnE.getRecordType());
        AlikAssert.isTrue(warehouseIds != null && warehouseIds.size() > 0, ResCode.ORDER_ERROR_6026, ResCode.ORDER_ERROR_6026_DESC);
        Long warehouseId = warehouseIds.get(0);
        //修改后置单 状态为取消
        warehouseRecordMapper.updateToCanceled(warehouseId);

        try {
            //调用库存中心 取消入库单
            WarehouseRecordE warehouseRecordE = warehouseRecordService.queryWarehouseRecordEById(warehouseId);
            CancelRecordDTO cancelRecordDTO = new CancelRecordDTO();
            cancelRecordDTO.setRecordCode(warehouseRecordE.getRecordCode());
            cancelRecordDTO.setRecordType(warehouseRecordE.getRecordType());
            cancelRecordDTO.setIsForceCancel(YesOrNoEnum.NO.getType());
            CancelResultDTO result = stockRecordFacade.cancelSingleRecord(cancelRecordDTO);
            if (null == result || !result.getStatus()) {
                throw new RomeException(ResCode.ORDER_ERROR_7503, ResCode.ORDER_ERROR_7503_DESC);
            }
        } catch (RomeException e) {
            log.error("门店零售退货（入库单） 取消失败，后置单ID：{}，异常：{}", warehouseId, e.getMessage());
            throw new RomeException(ResCode.ORDER_ERROR_7404, ResCode.ORDER_ERROR_7404_DESC);
        } catch (Exception e) {
            log.error("门店零售退货（入库单） 取消失败，后置单ID：{}，异常：{}", warehouseId, e.getMessage());
            throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @Override
    public PageInfo<SalesReturnWarehouseRecordDTO> findBySalesReturnRecordCondition(SalesReturnRecordParamDTO paramDTO) {
        //若查询参数中有订单号，则需要先从退货前置单表中通过订单号检索
        if (StringUtils.isNotBlank(paramDTO.getOutRecordCode())) {
            //外部订单号查询 前置单
            SalesReturnE salesReturnE = salesReturnMapper.selectFrSalesReturnRecordByOutRecordCode(paramDTO.getOutRecordCode());
            if (null == salesReturnE) {
                PageInfo<SalesReturnWarehouseRecordDTO> personPageInfo = new PageInfo<>(new ArrayList<>());
                personPageInfo.setTotal(0);
                return personPageInfo;
            }
            List<Long> warehouseRecordIdList = frontWarehouseRecordRelationMapper.queryWarehouseRecordIdByRecord(salesReturnE.getRecordCode());
            if (CollectionUtils.isEmpty(warehouseRecordIdList)) {
                PageInfo<SalesReturnWarehouseRecordDTO> personPageInfo = new PageInfo<>(new ArrayList<>());
                personPageInfo.setTotal(0);
                return personPageInfo;
            }
            paramDTO.setWarehouseRecordIds(warehouseRecordIdList);
        }

        //若查询参数中有仓库编码，截取工厂编码和仓库编码查询
        String realWarehouseCode = paramDTO.getRealWarehouseCode();
        if (StringUtils.isNotBlank(realWarehouseCode)) {
            RealWarehouse warehouse = stockRealWarehouseFacade.queryRealWarehouseByRWCode(realWarehouseCode);
            if (null != warehouse) {
                paramDTO.setRealWarehouseCode(null);
                paramDTO.setRealWarehouseId(warehouse.getId());
            } else {
                //用户输入仓库编码不存在，则直接返回无结果
                PageInfo<SalesReturnWarehouseRecordDTO> personPageInfo = new PageInfo<>(new ArrayList<>());
                personPageInfo.setTotal(0);
                return personPageInfo;
            }
        }

        //分页查询
        paramDTO.setRecordType(WarehouseRecordTypeEnum.SHOP_RETURN_GOODS_WAREHOUSE_RECORD.getType());
        Page page = PageHelper.startPage(paramDTO.getPageIndex(), paramDTO.getPageSize());
        List<WarehouseRecordE> salesReturnRecordList = warehouseRecordMapper.querySalesReturnWarehouseRecordList(paramDTO);
        List<SalesReturnWarehouseRecordDTO> salesReturnWarehouseRecordList = salesReturnConvertor.salesReturnRecordEToDTOList(salesReturnRecordList);

        if (!CollectionUtils.isEmpty(salesReturnWarehouseRecordList)) {
            List<Long> warehouseRecordIdList = salesReturnWarehouseRecordList.stream().map(SalesReturnWarehouseRecordDTO::getId).distinct().collect(Collectors.toList());
            //后置单Id -查询前置单code集合
            List<FrontWarehouseRecordRelationE> recordRelationEList = frontWarehouseRecordRelationMapper.queryFrontRecordListByIdList(warehouseRecordIdList);
            List<Long> frontRecordIdList = recordRelationEList.stream().map(FrontWarehouseRecordRelationE::getFrontRecordId).distinct().collect(Collectors.toList());
            //批量查询前置单列表
            List<SalesReturnE> saleReturnList = salesReturnMapper.queryFrontRecordByIds(frontRecordIdList);
            //前置单编码--->外部编码（外部订单号）
            Map<String, String> frontRecordCode2OueRecordCode = saleReturnList.stream().collect(Collectors.toMap(SalesReturnE::getRecordCode, SalesReturnE::getOutRecordCode, (key1, key2)->key1));
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

            //查询仓库信息
            List<QueryRealWarehouseDTO> qrwList = new ArrayList<>();
            salesReturnWarehouseRecordList.forEach(record -> {
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
            Map<Long, String> realWarehouseServiceMap = realWarehouseList.stream().collect(Collectors.toMap(RealWarehouse::getId, RealWarehouse::getRealWarehouseName, (key1, key2)->key1));

            //批量查询渠道信息
            List<ChannelDTO> channelInfoList = new ArrayList<>();
            try {
                List<String> channelCodeList = salesReturnRecordList.stream().map(WarehouseRecordE::getChannelCode).distinct().collect(Collectors.toList());
                if (null != channelCodeList && channelCodeList.size() > 0) {
                    channelInfoList = baseFacade.queryChannelInfoByChannelCodeList(channelCodeList);
                }
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }
            Map<String, ChannelDTO> channelInfoMap = channelInfoList.stream().collect(Collectors.toMap(ChannelDTO::getChannelCode, Function.identity(), (key1, key2)->key1));

            //拼装返回结果
            for (SalesReturnWarehouseRecordDTO dto : salesReturnWarehouseRecordList) {
                dto.setOutRecordCodeList(whId2OutRecordMap.get(dto.getId()));
                dto.setRecordStatusName(WarehouseRecordStatusEnum.getDescByType(dto.getRecordStatus()));
                dto.setRealWarehouseName(realWarehouseServiceMap.get(dto.getRealWarehouseId()));
                if (channelInfoMap.containsKey(dto.getChannelCode())) {
                    dto.setChannelCodeName(channelInfoMap.get(dto.getChannelCode()).getChannelName());
                }
            }
        }
        PageInfo<SalesReturnWarehouseRecordDTO> pageInfo = new PageInfo<>(salesReturnWarehouseRecordList);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    /**
     * 门店退货详情查询
     *
     * @param recordId
     * @return
     */
    @Override
    public SalesReturnWarehouseRecordDTO querySaleReturnWarehouseRecordInfoById(Long recordId) {
        if (null == recordId) {
            return new SalesReturnWarehouseRecordDTO();
        }
        WarehouseRecordE record = warehouseRecordService.queryWarehouseRecordEById(recordId);
        if (null == record) {
            return new SalesReturnWarehouseRecordDTO();
        }
        List<WarehouseRecordDetailE> warehouseRecordDetails = record.getWarehouseRecordDetailList();
        SalesReturnWarehouseRecordDTO result = salesReturnConvertor.salesReturnRecordEToDTO(record);
        List<SaleWarehouseRecordDetailDTO> details = salesReturnDetailConvertor.recordEToRecordDTO(warehouseRecordDetails);

        //查询仓库信息
        RealWarehouse realWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(record.getRealWarehouseCode(), record.getFactoryCode());
        if (null != realWarehouse) {
            result.setRealWarehouseName(realWarehouse.getRealWarehouseName());
            result.setRealWarehouseAddress(realWarehouse.getRealWarehouseAddress());
        }
        result.setRecordTypeName(WarehouseRecordTypeEnum.getDescByType(record.getRecordType()));
        result.setRecordStatusName(WarehouseRecordStatusEnum.getDescByType(record.getRecordStatus()));

        //查询商品信息
        List<SkuInfoExtDTO> skuInfoList = new ArrayList<>();
        try {
            List<Long> skuIdList = details.stream().map(SaleWarehouseRecordDetailDTO::getSkuId).distinct().collect(Collectors.toList());
            skuInfoList = itemFacade.skuBySkuIds(skuIdList);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        Map<Long, SkuInfoExtDTO> skuInfoMap = skuInfoList.stream().collect(Collectors.toMap(SkuInfoExtDTO::getId, Function.identity(), (key1, key2)->key1));

        //拼装返回信息
        for (SaleWarehouseRecordDetailDTO detail : details) {
            Long skuId = detail.getSkuId();
            if (skuInfoMap.containsKey(skuId)) {
                SkuInfoExtDTO sku = skuInfoMap.get(skuId);
                detail.setSkuName(sku.getName());
                detail.setCategoryCode(sku.getCategoryCode());
                detail.setCategoryName(sku.getCategoryName());
            }
        }
        result.setDetails(details);
        return result;
    }

    /**
     * 保存门店零售退货 前置单
     *
     * @param frontRecordE
     */
    private void addSalesReturnFrontRecord(SalesReturnE frontRecordE) {
        //设置单据类型
        frontRecordE.setRecordType(FrontRecordTypeEnum.SHOP_RETURN_GOODS_RECORD.getType());
        //获取单据编号
        String code = orderUtilService.queryOrderCode(FrontRecordTypeEnum.SHOP_RETURN_GOODS_RECORD.getCode());
        frontRecordE.setRecordCode(code);
        //设置商品code或id
        itemInfoTool.convertSkuCode(frontRecordE.getFrontRecordDetails());
        //单位转换
        skuQtyUnitTool.convertRealToBasic(frontRecordE.getFrontRecordDetails());
        if (StringUtils.isBlank(frontRecordE.getRecordStatusReason())) {
            frontRecordE.setRecordStatusReason("");
        }
        if (StringUtils.isBlank(frontRecordE.getReason())) {
            frontRecordE.setReason("");
        }
        if (StringUtils.isBlank(frontRecordE.getMobile())) {
            frontRecordE.setMobile("");
        }
        if (StringUtils.isBlank(frontRecordE.getUserCode())) {
            frontRecordE.setUserCode("");
        }
        salesReturnMapper.saveFrSalesReturnRecord(frontRecordE);

        //保存退货单 明细
        frontRecordE.getFrontRecordDetails().forEach(record -> {
            record.setFrontRecordId(frontRecordE.getId());
            record.setRecordCode(frontRecordE.getRecordCode());
        });
        salesReturnDetailMapper.saveFrSalesReturnRecordDetail(frontRecordE.getFrontRecordDetails());
    }
}
