package com.lyf.scm.core.service.pack.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.*;
import com.lyf.scm.common.enums.pack.*;
import com.lyf.scm.common.util.CollectorsUtil;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.pack.*;
import com.lyf.scm.core.domain.convert.pack.PackDemandComponentConvertor;
import com.lyf.scm.core.domain.convert.pack.PackDemandConvertor;
import com.lyf.scm.core.domain.convert.pack.PackDemandDetailConvertor;
import com.lyf.scm.core.domain.convert.pack.PackDemandPortalConvertor;
import com.lyf.scm.core.domain.entity.order.OrderE;
import com.lyf.scm.core.domain.entity.pack.PackDemandE;
import com.lyf.scm.core.mapper.order.OrderMapper;
import com.lyf.scm.core.mapper.pack.PackDemandComponentMapper;
import com.lyf.scm.core.mapper.pack.PackDemandDetailMapper;
import com.lyf.scm.core.mapper.pack.PackDemandMapper;
import com.lyf.scm.core.remote.base.dto.ChannelDTO;
import com.lyf.scm.core.remote.base.facade.BaseFacade;
import com.lyf.scm.core.remote.item.SkuQtyUnitTool;
import com.lyf.scm.core.remote.item.dto.*;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.pack.dto.CancelRequireDTO;
import com.lyf.scm.core.remote.pack.dto.ObtainOrderDTO;
import com.lyf.scm.core.remote.pack.facade.PackFacade;
import com.lyf.scm.core.remote.stock.dto.QueryRealWarehouseDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.service.order.OrderService;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.pack.PackDemandComponentService;
import com.lyf.scm.core.service.pack.PackDemandDetailService;
import com.lyf.scm.core.service.pack.PackDemandService;
import com.lyf.scm.core.service.stockFront.RealWarehouseService;
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
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Desc:
 * @author:Huangyl
 * @date: 2020/7/6
 */
@Slf4j
@Service("packDemandService")
public class PackDemandServiceImpl implements PackDemandService {

    @Resource
    private PackDemandMapper packDemandMapper;

    @Resource
    private OrderService orderService;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderUtilService orderUtilService;

    @Resource
    private PackDemandPortalConvertor packDemandPortalConvertor;

    @Resource
    private PackDemandDetailService packDemandDetailService;

    @Resource
    private PackDemandComponentService packDemandComponentService;

    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;

    @Resource
    private BaseFacade baseFacade;

    @Resource
    private PackDemandConvertor packDemandConvertor;

    @Resource
    private PackDemandDetailMapper packDemandDetailMapper;

    @Resource
    private PackDemandComponentMapper packDemandComponentMapper;

    @Resource
    private PackDemandComponentConvertor packDemandComponentConvertor;

    @Resource
    private PackDemandDetailConvertor packDemandDetailConvertor;

    @Resource
    private ItemFacade itemFacade;

    @Resource
    private PackFacade packFacade;

    @Resource
    private SkuQtyUnitTool skuQtyUnitTool;

    @Resource
    private RealWarehouseService realWarehouseService;


    /**
     * 根据需求单code
     * 同步需求单包装完成状态
     *
     * @param recordCode
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePackComplete(String recordCode) {
        //根据需求单编号查询是否存在
        PackDemandE packDemandE = packDemandMapper.queryByRecordCode(recordCode);
        AlikAssert.isNotNull(packDemandE, ResCode.ORDER_ERROR_7608, ResCode.ORDER_ERROR_7608_DESC + recordCode);

        //判断单据的状态 已确认/部分包装
        if (DemandRecordStatusEnum.CONFIRMED.getStatus().equals(packDemandE.getRecordStatus()) ||
                DemandRecordStatusEnum.PART_PACK.getStatus().equals(packDemandE.getRecordStatus())) {
            //更新需求单单据状态为已完成包装
            int result = packDemandMapper.updateRecordStatusToCompletePackByRecordCode(recordCode);
            if (result == 0) {
                throw new RomeException(ResCode.ORDER_ERROR_7605, ResCode.ORDER_ERROR_7605_DESC);
            }
            //判断销售号sale_code是否存在
            if (StringUtils.isNotEmpty(packDemandE.getSaleCode())) {
                OrderE orderE = orderMapper.queryOrderBySaleCode(packDemandE.getSaleCode());
                if (orderE != null) {
                    //已经加工 直接返回
                    if(OrderStatusEnum.PROCESS_STATUS_DONE.getStatus().equals(orderE.getOrderStatus())){
                        return;
                    }
                    //根据销售单号同步SO状态 -失败>抛异常
                    int resultOder = orderMapper.updateOrderStatusBySaleCode(orderE.getOrderCode());
                    if (resultOder == 0) {
                        throw new RomeException(ResCode.ORDER_ERROR_7604, ResCode.ORDER_ERROR_7604_DESC);
                    }
                }
            }

        } else if (!DemandRecordStatusEnum.COMPLETE_PACK.getStatus().equals(packDemandE.getRecordStatus())) {
            //-其他状态>抛出异常
            throw new RomeException(ResCode.ORDER_ERROR_7636, ResCode.ORDER_ERROR_7636_DESC + recordCode);
        }
    }

    /**
     * 根据预约单SO
     * 创建单个需求单
     *
     * @param demandFromSoDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createPackDemandBySo(DemandFromSoDTO demandFromSoDTO) {
        log.info("根据预约单SO-开始");
        //根据预约单号orderCode查询预约单（包含明细）
        OrderE orderE = orderService.queryOrderByCode(demandFromSoDTO.getOrderCode());
        AlikAssert.isNotNull(orderE, ResCode.ORDER_ERROR_7610, "预约单号不存在：" + demandFromSoDTO.getOrderCode());

        //防止重复插入
        PackDemandE packDemand = packDemandMapper.queryBySaleCode(orderE.getSaleCode());
        if (null != packDemand) {

            return packDemand.getRecordCode();
        }
        // 构建需求单对象
        PackDemandE packDemandE = packDemandPortalConvertor.convertToPackDemandE(orderE);
        packDemandE.setCreator(demandFromSoDTO.getUserId());
        packDemandE.setModifier(demandFromSoDTO.getUserId());
        // 保存需求单
        packDemandMapper.insert(packDemandE);
        //批量插入成品商品数据
        packDemandDetailService.batchSavePackDemandComponent(packDemandDetailConvertor.convertEList2DTOList(packDemandE.getFinishProductDetail()), demandFromSoDTO.getUserId());
        //批量插入组件数据
        packDemandComponentService.batchSavePackDemandComponent(packDemandComponentConvertor.convertEList2DTOList(packDemandE.getComponentEList()), demandFromSoDTO.getUserId());
        // 下发包装系统
        this.releasePackSystem(packDemandE);

        log.info("根据预约单SO-结束");
        return packDemandE.getRecordCode();
    }


    @Override
    @Transactional
    public String createPackDemand(DemandDTO demandDTO) {
        //判断销售单号是否已关联 暂时不做校验
        /*if (StringUtils.isNotBlank(demandDTO.getSaleCode())) {
            if (null != packDemandMapper.queryBySaleCode(demandDTO.getSaleCode())) {
                throw new RomeException(ResCode.ORDER_ERROR_7609, ResCode.ORDER_ERROR_7609_DESC);
            }
        }*/
        //生成需求单号
        String recordCode;
        if (StringUtils.isNotBlank(demandDTO.getRecordCode())) {
            recordCode = demandDTO.getRecordCode();
        } else {
            recordCode = orderUtilService.queryOrderCode(FrontRecordTypeEnum.PACKAGE_NEED_RECORD.getCode());
        }
        //成品明细赋值recordCode
        List<PackDemandDetailDTO> packDemandDetailDTOList = demandDTO.getPackDemandDetailDTOList();
        packDemandDetailDTOList.forEach((detail) -> detail.setRecordCode(recordCode));
        //组件明细赋值recordCode
        List<PackDemandComponentDTO> packDemandComponentDTOList = demandDTO.getPackDemandComponentDTOList();
        //获取组合商品详情列表
        packDemandComponentDTOList.forEach((component) -> component.setRecordCode(recordCode));
        packDemandDetailService.batchSavePackDemandComponent(packDemandDetailDTOList, demandDTO.getUserId());
        packDemandComponentService.batchSavePackDemandComponent(packDemandComponentDTOList, demandDTO.getUserId());
        PackDemandE packDemandE = packDemandConvertor.convertorDTO2E(demandDTO);
        if (null == demandDTO.getRecordCode()) {
            packDemandE.setRecordCode(recordCode);
            packDemandE.setRecordStatus(DemandRecordStatusEnum.INIT.getStatus());
            packDemandE.setPickStatus(DemandPickStatusEnum.NOT_PICK.getStatus());
            packDemandE.setIsOut(0);
            packDemandE.setCreator(demandDTO.getUserId());
            packDemandE.setModifier(demandDTO.getUserId());
            packDemandE.setCreateType(PackCreateTypeEnum.PAGE.getType());
            if (packDemandMapper.insert(packDemandE) < 1) {
                throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
            }
        } else {
            packDemandE.setModifier(demandDTO.getUserId());
            packDemandMapper.updateById(packDemandE);
        }
        return recordCode;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCreatePackDemand(List<DemandBatchDTO> demandBatchDTOList, Long userId) {
        List<String> errorLogs = new ArrayList<>();
        Integer row = 2;
        //校验数据正确性
        List<String> channelCodeList = new ArrayList<>();
        for (DemandBatchDTO demandBatchDTO : demandBatchDTOList) {
            //必填校验
            if (StringUtils.isBlank(demandBatchDTO.getChannelCode())) {
                errorLogs.add("第" + row + "行" + ResCode.ORDER_ERROR_7627_DESC);
            } else {
                channelCodeList.add(demandBatchDTO.getChannelCode());
            }
            if (StringUtils.isBlank(demandBatchDTO.getPackType())) {
                errorLogs.add("第" + row + "行" + ResCode.ORDER_ERROR_7628_DESC);
            }
            if (StringUtils.isBlank(demandBatchDTO.getPackWarehouse())) {
                errorLogs.add("第" + row + "行" + ResCode.ORDER_ERROR_7629_DESC);
            }
            if (StringUtils.isBlank(demandBatchDTO.getPickWarehouse())) {
                errorLogs.add("第" + row + "行" + ResCode.ORDER_ERROR_7630_DESC);
            }
            if (null == demandBatchDTO.getDemandDate()) {
                errorLogs.add("第" + row + "行" + ResCode.ORDER_ERROR_7631_DESC);
            }
            if (StringUtils.isBlank(demandBatchDTO.getSkuCode())) {
                errorLogs.add("第" + row + "行" + ResCode.ORDER_ERROR_7632_DESC);
            }
            if (null == demandBatchDTO.getRequireQty()) {
                errorLogs.add("第" + row + "行" + ResCode.ORDER_ERROR_7633_DESC);
            }
            //包装类型校验
            if (null == PackTypeEnum.getPackTypeEnumByDesc(demandBatchDTO.getPackType())) {
                errorLogs.add("第" + row + "行" + ResCode.ORDER_ERROR_7622_DESC);
            } else {
                //反拆不能导入
                if (demandBatchDTO.getPackType().equals(PackTypeEnum.UN_SELF_COMPOSE.getDesc())) {
                    errorLogs.add("第" + row + "行" + ResCode.ORDER_ERROR_7623_DESC);
                }
            }
            demandBatchDTO.setRow(row);
            row++;
        }
        if (errorLogs.size() > 0) {
            throw new RomeException(ResCode.ORDER_ERROR_1001, errorLogs);
        }
        //获取领料仓库
        List<RealWarehouse> pickRealWarehouseList = realWarehouseService.queryNotShopWarehouse();
        //获取包装仓库
        List<RealWarehouse> packRealWarehouseList = realWarehouseService.queryNotShopWarehouseByType(RealWarehouseTypeEnum.RW_TYPE_2.getType());
        //获取所有的渠道
        List<ChannelDTO> channelDTOList = baseFacade.queryChannelInfoByChannelCodeList(channelCodeList);
        //获取到所有的skuCode
        List<String> skuCodeList = demandBatchDTOList.stream().map(DemandBatchDTO::getSkuCode).distinct()
                .collect(Collectors.toList());
        skuCodeList.removeIf(s -> StringUtils.isBlank(s));
        //获取到所有的sku信息
        List<SkuInfoExtDTO> skuList = itemFacade.skuBySkuCodes(skuCodeList);
        for (SkuInfoExtDTO skuInfo : skuList) {
            demandBatchDTOList.stream().filter(sku -> skuInfo.getSkuCode().equals(sku.getSkuCode()))
                    .forEach(sku -> {
                        sku.setSkuName(skuInfo.getName());
                        sku.setSkuId(skuInfo.getId());
                    });
        }
        for (SkuInfoExtDTO skuInfo : skuList) {
            demandBatchDTOList.stream().filter(sku -> skuInfo.getSkuCode().equals(sku.getSkuCode()))
                    .forEach(sku -> {
                        sku.setSkuName(skuInfo.getName());
                        sku.setSkuId(skuInfo.getId());
                    });
        }
        Map<String, SkuInfoExtDTO> skuMap = skuList.stream()
                .collect(Collectors.toMap(SkuInfoExtDTO::getSkuCode, Function.identity(), (v1, v2) -> v1));
        List<ParamExtDTO> paramExtList = packDemandConvertor.convertBatchDTOLitsToParamDTOList(demandBatchDTOList);
        paramExtList.removeIf(s -> s.getSkuId() == null || StringUtils.isBlank(s.getUnitCode()));
        //获取到所有的编码
        List<SkuUnitExtDTO> skuUnitList = itemFacade.unitsBySkuIdAndUnitCode(paramExtList, null);
        Map<String, SkuUnitExtDTO> skuUnitMap = skuUnitList.stream()
                .collect(Collectors.toMap((skuUnit) -> unitGroupKey(skuUnit), Function.identity(), (v1, v2) -> v1));
        Map<String, List<DemandBatchDTO>> map = demandBatchDTOList.stream()
                .collect(Collectors.groupingBy(e -> fetchGroupKey(e)));
        List<DemandDTO> demandDTOList = new ArrayList<>();
        for (Map.Entry<String, List<DemandBatchDTO>> entry : map.entrySet()) {
            List<DemandBatchDTO> gruopList = entry.getValue();
            DemandBatchDTO baseDemandBatchDTO = gruopList.get(0);
            //需求单明细
            List<PackDemandDetailDTO> demandDetailDTOList = new ArrayList<>();
            Integer packType = PackTypeEnum.getPackTypeEnumByDesc(baseDemandBatchDTO.getPackType()).getType();
            baseDemandBatchDTO.setPackType(null);
            DemandDTO demandDTO = packDemandConvertor.convertBatchDTOToDTO(baseDemandBatchDTO);
            //转换包装类型
            demandDTO.setPackType(packType);
            baseDemandBatchDTO.setPackType(PackTypeEnum.getDescByType(packType));
            boolean channel = true;
            for (ChannelDTO channelDTO : channelDTOList) {
                if (channelDTO.getChannelCode().equals(baseDemandBatchDTO.getChannelCode())) {
                    channel = false;
                }
            }
            if (channel) {
                errorLogs.add("第" + baseDemandBatchDTO.getRow() + "渠道编码不存在");
            }
            //确认包装仓库
            packRealWarehouseList.stream().filter(warehouse -> warehouse.getRealWarehouseCode().equals(baseDemandBatchDTO.getPackWarehouse()))
                    .forEach(warehouse -> {
                        demandDTO.setInFactoryCode(warehouse.getFactoryCode());
                        demandDTO.setInRealWarehouseCode(warehouse.getRealWarehouseOutCode());
                        demandDTO.setInRealWarehouseId(warehouse.getId());
                    });
            //确认领料仓库
            pickRealWarehouseList.stream().filter(warehouse -> warehouse.getRealWarehouseCode().equals(baseDemandBatchDTO.getPickWarehouse()))
                    .forEach(warehouse -> {
                        demandDTO.setOutFactoryCode(warehouse.getFactoryCode());
                        demandDTO.setOutRealWarehouseCode(warehouse.getRealWarehouseOutCode());
                        demandDTO.setOutRealWarehouseId(warehouse.getId());
                    });
            if (StringUtils.isBlank(demandDTO.getInFactoryCode())) {
                errorLogs.add("第" + baseDemandBatchDTO.getRow() + "行包装仓库不存在");
            }
            if (StringUtils.isBlank(demandDTO.getOutFactoryCode())) {
                errorLogs.add("第" + baseDemandBatchDTO.getRow() + "行领料仓库不存在");
            }
            demandDTO.setChannelCode(baseDemandBatchDTO.getChannelCode());
            demandDTO.setDemandDate(baseDemandBatchDTO.getDemandDate());
            demandDTO.setDepartment(baseDemandBatchDTO.getDepartment());
            //成品商品的信息组合
            for (DemandBatchDTO demandBatchDTO : gruopList) {
                PackDemandDetailDTO demandDetailDTO = new PackDemandDetailDTO();
                // 设置skuCode和skuName
                if (StringUtils.isNotBlank(demandBatchDTO.getSkuCode())
                        && skuMap.containsKey(demandBatchDTO.getSkuCode())) {
                    SkuInfoExtDTO skuInfoExtDTO = skuMap.get(demandBatchDTO.getSkuCode());
                    if (demandDTO.getPackType().equals(PackTypeEnum.PACKAGING.getType()) || demandDTO.getPackType().equals(PackTypeEnum.UN_PACKAGING.getType())) {
                        if (skuInfoExtDTO.getCombineType().equals(CombineTypeEnum.RAW_MATERIAL.getType())) {
                            errorLogs.add("第" + demandBatchDTO.getRow() + "行组装和反拆的包装类型需求商品不能是原料");
                        }
                    }
                    demandDetailDTO.setSkuCode(demandBatchDTO.getSkuCode());
                    demandDetailDTO.setSkuName(demandBatchDTO.getSkuName());
                } else {
                    errorLogs.add("第" + demandBatchDTO.getRow() + "行商品编号" + demandBatchDTO.getSkuCode() + "不存在");
                }
                // 设置单位
                SkuUnitExtDTO extDTO;
                if (StringUtils.isNotBlank(demandBatchDTO.getUnitCode())
                        && skuUnitMap.containsKey(demandBatchDTO.getSkuCode() + demandBatchDTO.getUnitCode())) {
                    extDTO = skuUnitMap.get(demandBatchDTO.getSkuCode() + demandBatchDTO.getUnitCode());
                    demandDetailDTO.setUnitCode(extDTO.getUnitCode());
                    demandDetailDTO.setUnit(extDTO.getUnitName());
                    demandDetailDTO.setBasicScale(extDTO.getScale());
                    demandDetailDTO.setBasicUnitCode(extDTO.getBasicUnitCode());
                    demandDetailDTO.setBasicUnitName(extDTO.getUnitName());
                    // 设置数量
                    if (null == demandBatchDTO.getRequireQty()) {
                        errorLogs.add("第" + demandBatchDTO.getRow() + "行商品编号为" + demandBatchDTO.getSkuCode() + "需求数量" + "不存在");
                    } else {
                        if (demandBatchDTO.getRequireQty().compareTo(new BigDecimal("0.00")) == 1) {
                            demandDetailDTO.setRequireQty(demandBatchDTO.getRequireQty());
                        } else {
                            errorLogs.add("第" + demandBatchDTO.getRow() + "行商品编号为" + demandBatchDTO.getSkuCode() + "需求数量必须大于0");
                        }
                    }
                } else {
                    errorLogs.add("第" + demandBatchDTO.getRow() + "行商品编号为" + demandBatchDTO.getSkuCode() + "的单位"
                            + demandBatchDTO.getUnitCode() + "不存在");
                }
                //判断是否为自定义组合类型
                if (demandBatchDTO.getPackType().equals(PackTypeEnum.SELF_COMPOSE.getDesc())) {
                    if (StringUtils.isNotBlank(demandBatchDTO.getCustomGroupCode())) {
                        demandDetailDTO.setCustomGroupCode(demandBatchDTO.getCustomGroupCode());
                    } else {
                        errorLogs.add("第" + demandBatchDTO.getRow() + "行" + ResCode.ORDER_ERROR_7634_DESC);
                    }
                    if (null == demandBatchDTO.getCompositeQty()) {
                        errorLogs.add("第" + demandBatchDTO.getRow() + "行" + ResCode.ORDER_ERROR_7635_DESC);
                    } else {
                        if (demandBatchDTO.getRequireQty().compareTo(new BigDecimal("0.00")) == 1) {
                            demandDetailDTO.setCompositeQty(demandBatchDTO.getCompositeQty());
                        } else {
                            errorLogs.add("第" + demandBatchDTO.getRow() + "行商品编号为" + demandBatchDTO.getSkuCode() + "组合份数必须大于0");
                        }
                    }
                }
                demandDetailDTO.setRemark(demandBatchDTO.getDetailRemark());
                demandDetailDTOList.add(demandDetailDTO);
            }
            demandDTO.setPackDemandDetailDTOList(demandDetailDTOList);
            demandDTOList.add(demandDTO);
        }
        if (errorLogs.size() > 0) {
            throw new RomeException(ResCode.ORDER_ERROR_1001, errorLogs);
        }
        //处理校验完成的数据入库
        for (DemandDTO demandDTO : demandDTOList) {
            //生成需求单号
            String recordCode = orderUtilService.queryOrderCode(FrontRecordTypeEnum.PACKAGE_NEED_RECORD.getCode());
            //成品明细赋值recordCode
            List<ParamExtDTO> paramExtDTOList = new ArrayList<ParamExtDTO>();
            List<PackDemandDetailDTO> frontPackDemandDetailDTOList = demandDTO.getPackDemandDetailDTOList();
            //对skuCode和unitCode相同的成品进行合并处理
            List<PackDemandDetailDTO> packDemandDetailDTOList = new ArrayList<PackDemandDetailDTO>();
            Map<String, List<PackDemandDetailDTO>> demandDetaiMap = frontPackDemandDetailDTOList.stream()
                    .collect(Collectors.groupingBy(e -> e.getUnitCode() + e.getSkuCode()));
            for (List<PackDemandDetailDTO> value : demandDetaiMap.values()) {
                PackDemandDetailDTO packDemandDetailDTO = value.get(0);
                packDemandDetailDTO.setCompositeQty(value.stream().collect(CollectorsUtil.summingBigDecimal(PackDemandDetailDTO::getCompositeQty)));
                if (packDemandDetailDTO.getCompositeQty().compareTo(new BigDecimal("0.00")) == 0) {
                    packDemandDetailDTO.setCompositeQty(null);
                }
                packDemandDetailDTO.setRequireQty(value.stream().collect(CollectorsUtil.summingBigDecimal(PackDemandDetailDTO::getRequireQty)));
                packDemandDetailDTOList.add(packDemandDetailDTO);
            }
            packDemandDetailDTOList.stream().forEach((detail) -> {
                ParamExtDTO paramExtDTO = new ParamExtDTO();
                detail.setRecordCode(recordCode);
                detail.setCerator(userId);
                detail.setModifier(userId);
                paramExtDTO.setSkuCode(detail.getSkuCode());
                paramExtDTO.setUnitCode(detail.getUnitCode());
                paramExtDTOList.add(paramExtDTO);
            });
            //获取组合商品详情列表
            List<SkuUnitAndCombineExtDTO> combineExtDTOList = itemFacade.queryCombinesBySkuCodeAndUnitCode(paramExtList, null);
            //组件明细赋值recordCode
            //获取所有组合品的skuCodeh和unitCode
            for (SkuUnitAndCombineExtDTO skuUnitAndCombineExtDTO : combineExtDTOList) {
                if (!skuUnitAndCombineExtDTO.getCombineType().equals(CombineTypeEnum.RAW_MATERIAL.getType())) {
                    for (SkuCombineSimpleDTO skuCombineSimpleDTO : skuUnitAndCombineExtDTO.getCombineInfo()) {
                        ParamExtDTO paramExtDTO = new ParamExtDTO();
                        paramExtDTO.setUnitCode(skuCombineSimpleDTO.getCombineSkuUnitCode());
                        paramExtDTO.setSkuCode(skuCombineSimpleDTO.getCombineSkuCode());
                        paramExtDTOList.add(paramExtDTO);
                    }
                }
            }
            //获取到所有组合品的编码
            List<SkuUnitExtDTO> combineSkuUnitList = itemFacade.unitsBySkuCodeAndUnitCodeAndMerchantId(paramExtDTOList, null);
            Map<String, SkuUnitExtDTO> combineSkuUnitMap = combineSkuUnitList.stream()
                    .collect(Collectors.toMap((skuUnit) -> unitGroupKey(skuUnit), Function.identity(), (v1, v2) -> v1));
            List<PackDemandComponentDTO> packDemandComponentDTOList = new ArrayList<>();
            for (PackDemandDetailDTO demandDetailDTO : packDemandDetailDTOList) {
                combineExtDTOList.stream().filter(combineExt -> combineExt.getSkuCode().equals(demandDetailDTO.getSkuCode())).forEach((combineExt) -> {
                    //自定义组合和拆箱，明细和组件一样
                    if (demandDTO.getPackType().equals(PackTypeEnum.SELF_COMPOSE.getType()) || demandDTO.getPackType().equals(PackTypeEnum.DEVAN.getType())) {
                        PackDemandComponentDTO demandComponentDTO = new PackDemandComponentDTO();
                        demandComponentDTO.setRecordCode(recordCode);
                        demandComponentDTO.setIsPick(true);
                        demandComponentDTO.setSkuCode(demandDetailDTO.getSkuCode());
                        demandComponentDTO.setUnitCode(demandDetailDTO.getBasicUnitCode());
                        demandComponentDTO.setUnit(demandDetailDTO.getBasicUnitName());
                        demandComponentDTO.setSkuName(demandDetailDTO.getSkuName());
                        //判断包装类型填出入库类型
                        if (demandDTO.getPackType().equals(PackTypeEnum.SELF_COMPOSE.getType())) {
                            demandComponentDTO.setMoveType(MoveTypeEnum.IN_STOCK.getType());
                            demandComponentDTO.setParentSkuCode(demandDetailDTO.getCustomGroupCode());
                            demandComponentDTO.setRequireQty(demandDetailDTO.getRequireQty().multiply(demandDetailDTO.getBasicScale()));
                            demandComponentDTO.setBomQty(demandComponentDTO.getRequireQty().divide(demandDetailDTO.getCompositeQty(), 3));
                        } else {
                            demandComponentDTO.setMoveType(MoveTypeEnum.OUT_STOCK.getType());
                            demandComponentDTO.setParentSkuCode(demandDetailDTO.getSkuCode());
                            demandComponentDTO.setRequireQty(demandDetailDTO.getRequireQty().multiply(demandDetailDTO.getBasicScale()));
                            demandComponentDTO.setBomQty(demandComponentDTO.getRequireQty().divide(demandDetailDTO.getRequireQty(), 3));
                        }
                        packDemandComponentDTOList.add(demandComponentDTO);
                    } else //处理组装和反拆
                    {
                        List<SkuCombineSimpleDTO> simpleDTOList = combineExt.getCombineInfo();
                        for (SkuCombineSimpleDTO skuCombineSimpleDTO : simpleDTOList) {
                            PackDemandComponentDTO demandComponentDTO = new PackDemandComponentDTO();
                            demandComponentDTO.setRecordCode(recordCode);
                            demandComponentDTO.setIsPick(true);
                            demandComponentDTO.setSkuCode(skuCombineSimpleDTO.getCombineSkuCode());
                            demandComponentDTO.setParentSkuCode(skuCombineSimpleDTO.getSkuCode());
                            //判断包装类型填出入库类型
                            if (demandDTO.getPackType().equals(PackTypeEnum.PACKAGING.getType())) {
                                demandComponentDTO.setMoveType(MoveTypeEnum.IN_STOCK.getType());
                            } else {
                                demandComponentDTO.setMoveType(MoveTypeEnum.OUT_STOCK.getType());
                            } //通过unitCode+skuCode匹配取到与基础单位的转换比例
                            if (StringUtils.isNotBlank(skuCombineSimpleDTO.getCombineSkuCode())
                                    && combineSkuUnitMap.containsKey(skuCombineSimpleDTO.getCombineSkuCode() + skuCombineSimpleDTO.getCombineSkuUnitCode())) {
                                SkuUnitExtDTO extDTO = combineSkuUnitMap.get(skuCombineSimpleDTO.getCombineSkuCode() + skuCombineSimpleDTO.getCombineSkuUnitCode());
                                demandComponentDTO.setUnit(extDTO.getBasicUnitName());
                            }
                            demandComponentDTO.setUnitCode(skuCombineSimpleDTO.getCombineSkuCode());
                            demandComponentDTO.setRequireQty(demandDetailDTO.getRequireQty().multiply(demandDetailDTO.getBasicScale().multiply(skuCombineSimpleDTO.getNum())));
                            demandComponentDTO.setBomQty(skuCombineSimpleDTO.getNum());
                            packDemandComponentDTOList.add(demandComponentDTO);
                        }
                    }
                });
            }
            packDemandDetailService.batchSavePackDemandComponent(packDemandDetailDTOList, userId);
            packDemandComponentService.batchSavePackDemandComponent(packDemandComponentDTOList, userId);
            PackDemandE packDemandE = packDemandConvertor.convertorDTO2E(demandDTO);
            packDemandE.setRecordCode(recordCode);
            packDemandE.setRecordStatus(DemandRecordStatusEnum.INIT.getStatus());
            packDemandE.setPickStatus(DemandPickStatusEnum.NOT_PICK.getStatus());
            packDemandE.setIsOut(0);
            packDemandE.setCreator(userId);
            packDemandE.setModifier(userId);
            packDemandE.setCreateType(PackCreateTypeEnum.IMPORT.getType());
            if (packDemandMapper.insert(packDemandE) < 1) {
                throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
            }
        }
    }


    /**
     * @param demandBatchDTO
     * @return
     * @description 定义组合分组的key-批量创建需求单：合并按渠道、包装类型、包装仓库、领料仓库合并
     */
    private static String fetchGroupKey(DemandBatchDTO demandBatchDTO) {
        return demandBatchDTO.getChannelCode() + "#" + demandBatchDTO.getPackType() + "#" + demandBatchDTO.getPackWarehouse() + "#" + demandBatchDTO.getPickWarehouse();
    }

    /**
     * @param skuUnitExtDTO
     * @return
     * @description SkuCode+UnitCode唯一
     */
    private static String unitGroupKey(SkuUnitExtDTO skuUnitExtDTO) {
        return skuUnitExtDTO.getSkuCode() + skuUnitExtDTO.getUnitCode();
    }

    @Override
    public PackDemandResponseDTO queryDemandDetailAndComponent(String recordCode) {
        PackDemandResponseDTO packDemandResponseDTO = new PackDemandResponseDTO();
        List<PackDemandDetailDTO> packDemandDetailDTOList = packDemandDetailService.queryFinishProductSkuDetail(recordCode);
        if (CollectionUtils.isNotEmpty(packDemandDetailDTOList)) {
            for (PackDemandDetailDTO packDemandDetailDTO : packDemandDetailDTOList) {
                packDemandDetailDTO.setId(null);
                packDemandDetailDTO.setRecordCode(null);
            }
        }
        packDemandResponseDTO.setPackDemandDetailDTOList(packDemandDetailDTOList);
        List<PackDemandComponentDTO> packDemandComponentDTOList = packDemandComponentService.queryDemandComponentByRecordCode(recordCode);
        if (CollectionUtils.isNotEmpty(packDemandComponentDTOList)) {
            for (PackDemandComponentDTO packDemandComponentDTO : packDemandComponentDTOList) {
                packDemandComponentDTO.setId(null);
                packDemandComponentDTO.setRecordCode(null);
                packDemandComponentDTO.setRemark(null);
                packDemandComponentDTO.setMoveType(MoveTypeEnum.IN_STOCK.getType());
            }
        }
        packDemandResponseDTO.setPackDemandComponentDTOList(packDemandComponentDTOList);
        return packDemandResponseDTO;
    }


    /**
     * 根据需求单号
     * 单个修改单据状态为已确认，并下发包装系统
     *
     * @param recordCode
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDemandRecordStatusConfirmed(String recordCode, Long userId) {
        //判断需求编号是否存在
        PackDemandE packDemandE = packDemandMapper.queryByRecordCode(recordCode);
        AlikAssert.isNotNull(packDemandE, ResCode.ORDER_ERROR_7618, ResCode.ORDER_ERROR_7618_DESC + recordCode);

        //判断单据的状态-初始
        if (DemandRecordStatusEnum.INIT.getStatus().equals(packDemandE.getRecordStatus())) {
            //更新需求单单据状态为已确认
            int result = packDemandMapper.updateRecordStatusToConfirmedByRecordCode(recordCode);
            if (result == 0) {
                throw new RomeException(ResCode.ORDER_ERROR_7606, ResCode.ORDER_ERROR_7606_DESC);
            }
            packDemandE.setModifier(userId);
            packDemandE.setFinishProductDetail(packDemandDetailMapper.queryDemandDetailByRequireCode(packDemandE.getRecordCode()));
            packDemandE.setComponentEList(packDemandComponentMapper.queryDemandComponentByRecordCode(packDemandE.getRecordCode()));
            // 下发包装系统
            this.releasePackSystem(packDemandE);

        } else if (DemandRecordStatusEnum.CONFIRMED.getStatus().equals(packDemandE.getRecordStatus())) {

            throw new RomeException(ResCode.ORDER_ERROR_7640, ResCode.ORDER_ERROR_7640_DESC);

        } else {

            throw new RomeException(ResCode.ORDER_ERROR_7607, ResCode.ORDER_ERROR_7607_DESC);
        }

    }

    /**
     * 封装数据
     * 并下发包装系统
     *
     * @param packDemandE
     */
    @Override
    public void releasePackSystem(PackDemandE packDemandE) {

        //检查需求单明细
        if (CollectionUtils.isEmpty(packDemandE.getFinishProductDetail())) {
            throw new RomeException(ResCode.ORDER_ERROR_7702, ResCode.ORDER_ERROR_7702_DESC);
        }
        //检查需求单组件
        if (CollectionUtils.isEmpty(packDemandE.getComponentEList())) {
            throw new RomeException(ResCode.ORDER_ERROR_7704, ResCode.ORDER_ERROR_7704_DESC);
        }
        // 封装对象数据
        ObtainOrderDTO obtainOrderDTO = packDemandPortalConvertor.convertObtainOrderDTO(packDemandE);
        // 调用包装系统
        packFacade.releasePackSystem(obtainOrderDTO);

    }

    @Override
    public PageInfo<PackDemandResponseDTO> queryPackDemandPage(QueryPackDemandDTO condition) {
        List<PackDemandE> packDemandEList = new ArrayList<>();
        Page page = PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
        if (CollectionUtils.isEmpty(condition.getSkuCodes())) {
            packDemandEList = packDemandMapper.queryPackDemandListByCondition(condition);
        } else {
            packDemandEList = packDemandMapper.queryPackDemandPage(condition);
        }
        if (CollectionUtils.isEmpty(packDemandEList)) {
            return new PageInfo<>();
        }
        List<PackDemandResponseDTO> packDemandDTOS = queryHandlePackDemandData(packDemandEList);
        PageInfo<PackDemandResponseDTO> pageInfo = new PageInfo<>(packDemandDTOS);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    @Override
    public List<PackDemandResponseDTO> queryPackDemandExport(QueryPackDemandDTO condition) {
        List<PackDemandE> packDemandEList = packDemandMapper.queryPackDemandPage(condition);
        if (CollectionUtils.isEmpty(packDemandEList)) {
            return new ArrayList<>();
        }
        return queryHandlePackDemandData(packDemandEList);
    }

    //查询出结果后 处理
    private List<PackDemandResponseDTO> queryHandlePackDemandData(List<PackDemandE> packDemandList) {
        //获取渠道信息
        List<String> channelCodeList = packDemandList.stream().map(PackDemandE::getChannelCode).distinct().collect(Collectors.toList());
        List<ChannelDTO> channelDTOs = baseFacade.queryChannelInfoByChannelCodeList(channelCodeList);
        Map<String, String> channelInfo = channelDTOs.stream().collect(Collectors.toMap(ChannelDTO::getChannelCode, ChannelDTO::getChannelName, (v1, v2) -> v2));

        //转换返回对象
        List<PackDemandResponseDTO> packDemandDTOS = packDemandConvertor.convertEListToDtoList(packDemandList);

        //查询出入仓库信息
        List<QueryRealWarehouseDTO> queryRealWarehouseDTOS = new ArrayList<>();
        packDemandDTOS.forEach(item -> {
            QueryRealWarehouseDTO queryRealWarehouseDTO = new QueryRealWarehouseDTO();
            queryRealWarehouseDTO.setFactoryCode(item.getOutFactoryCode());
            queryRealWarehouseDTO.setWarehouseOutCode(item.getOutRealWarehouseCode());
            queryRealWarehouseDTOS.add(queryRealWarehouseDTO);

            QueryRealWarehouseDTO queryRealWarehouse = new QueryRealWarehouseDTO();
            queryRealWarehouse.setFactoryCode(item.getInFactoryCode());
            queryRealWarehouse.setWarehouseOutCode(item.getInRealWarehouseCode());
            queryRealWarehouseDTOS.add(queryRealWarehouse);
        });
        List<RealWarehouse> realWarehouseList = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(queryRealWarehouseDTOS);
        Map<String, String> realWarehouseInfo = realWarehouseList.stream().collect(Collectors.toMap(RealWarehouse::getKey, RealWarehouse::getRealWarehouseCode, (oldValue, newValue) -> oldValue));

        //拼装返回结果
        packDemandDTOS.forEach(dto -> {
            //加入渠道名称
            if (channelInfo.containsKey(dto.getChannelCode())) {
                dto.setChannelName(channelInfo.get(dto.getChannelCode()));
            }
            //领料仓库
            if (realWarehouseInfo.containsKey(dto.getOutFactoryCode() + "-" + dto.getOutRealWarehouseCode())) {
                dto.setPickWorkshopCode(realWarehouseInfo.get(dto.getOutFactoryCode() + "-" + dto.getOutRealWarehouseCode()));
            }
            //包装车间
            if (realWarehouseInfo.containsKey(dto.getInFactoryCode() + "-" + dto.getInRealWarehouseCode())) {
                dto.setPackWorkshopCode(realWarehouseInfo.get(dto.getInFactoryCode() + "-" + dto.getInRealWarehouseCode()));
            }
        });
        return packDemandDTOS;
    }

    @Override
    public PackDemandResponseDTO queryPackDemandDetail(String recordCode) {
        PackDemandE packDemandE = packDemandMapper.queryByRecordCode(recordCode);
        if (packDemandE == null) {
            return new PackDemandResponseDTO();
        }
        PackDemandResponseDTO packDemandResponseDTO = packDemandConvertor.convertEToDto(packDemandE);
        //设置渠道名称和仓库名称
        ChannelDTO channelDTO = baseFacade.queryChannelInfoByCode(packDemandE.getChannelCode());
        packDemandResponseDTO.setChannelName(channelDTO.getChannelName());
        RealWarehouse realWarehouseE = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(packDemandE.getOutRealWarehouseCode(), packDemandE.getOutFactoryCode());
        if (realWarehouseE != null) {
            packDemandResponseDTO.setOutRealWarehouseName(realWarehouseE.getRealWarehouseName());
        }
        RealWarehouse realWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(packDemandE.getInRealWarehouseCode(), packDemandE.getInFactoryCode());
        if (realWarehouse != null) {
            packDemandResponseDTO.setInRealWarehouseName(realWarehouse.getRealWarehouseName());
        }
        //设置商品名称
        List<PackDemandDetailDTO> packDemandDetailDTOList = packDemandDetailService.queryFinishProductSkuDetail(recordCode);
        List<SkuInfoExtDTO> skuList = itemFacade.skuListBySkuCodes(packDemandDetailDTOList.stream().map(PackDemandDetailDTO::getSkuCode).distinct().collect(Collectors.toList()));
        Map<String, SkuInfoExtDTO> skuMap = skuList.stream().collect(Collectors.toMap(SkuInfoExtDTO::getSkuCode, Function.identity(), (v1, v2) -> v1));
        packDemandDetailDTOList.forEach(packDemandDetailDTO -> {
            if (StringUtils.isNotBlank(packDemandDetailDTO.getSkuCode())
                    && skuMap.containsKey(packDemandDetailDTO.getSkuCode())) {
                packDemandDetailDTO.setSkuName(skuMap.get(packDemandDetailDTO.getSkuCode()).getName());
            }
        });
        packDemandResponseDTO.setPackDemandDetailDTOList(packDemandDetailDTOList);

        List<PackDemandComponentDTO> packDemandComponentDTOList = packDemandComponentService.queryDemandComponentByRecordCode(recordCode);
        List<SkuInfoExtDTO> skuInfoExtDTOList = itemFacade.skuListBySkuCodes(packDemandComponentDTOList.stream().map(PackDemandComponentDTO::getSkuCode).distinct().collect(Collectors.toList()));
        Map<String, SkuInfoExtDTO> componentMap = skuInfoExtDTOList.stream().collect(Collectors.toMap(SkuInfoExtDTO::getSkuCode, Function.identity(), (v1, v2) -> v1));
        packDemandComponentDTOList.forEach(packDemandComponentDTO -> {
            if (StringUtils.isNotBlank(packDemandComponentDTO.getSkuCode())
                    && componentMap.containsKey(packDemandComponentDTO.getSkuCode())) {
                packDemandComponentDTO.setSkuName(componentMap.get(packDemandComponentDTO.getSkuCode()).getName());
            }
        });
        packDemandResponseDTO.setPackDemandComponentDTOList(packDemandComponentDTOList);

        return packDemandResponseDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelPackDemand(String requireCode) {
        if (StringUtils.isEmpty(requireCode)) {
            log.info("需求单取消-异常，需求单编码为空");
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        log.info("需求单取消-开始，需求单编码：{}", requireCode);
        PackDemandE packDemand = packDemandMapper.queryByRecordCode(requireCode);
        if (null == packDemand) {
            log.info("需求单取消-异常，需求单为空，编码：{}", requireCode);
            throw new RomeException(ResCode.ORDER_ERROR_7618, ResCode.ORDER_ERROR_7618_DESC);
        }
        Integer recordStatus = packDemand.getRecordStatus();
        if (!DemandRecordStatusEnum.INIT.getStatus().equals(recordStatus) &&
                !DemandRecordStatusEnum.CONFIRMED.getStatus().equals(recordStatus)) {
            throw new RomeException(ResCode.ORDER_ERROR_7619, ResCode.ORDER_ERROR_7619_DESC);
        }
        //修改需求单状态--已取消
        packDemandMapper.updatePackDemandCancel(packDemand.getId(), recordStatus);

        //检查包装系统是否允许取消
        if (DemandRecordStatusEnum.CONFIRMED.getStatus().equals(recordStatus)) {
            List<CancelRequireDTO> packSystemResp = packFacade.batchCancelRequire(Lists.newArrayList(requireCode));
            if (CollectionUtils.isEmpty(packSystemResp)) {
                throw new RomeException(ResCode.ORDER_ERROR_7621, ResCode.ORDER_ERROR_7621_DESC);
            }
            CancelRequireDTO cancel = packSystemResp.get(0);
            if (!cancel.getStatus().equals(YesOrNoEnum.NO.getType())) {
                throw new RomeException(ResCode.ORDER_ERROR_7620, ResCode.ORDER_ERROR_7620_DESC + "-" + cancel.getMessage());
            }
            log.info("需求单取消-包装系统取消成功，需求单编码：{}", requireCode);
        }
        log.info("需求单取消-成功，需求单编码：{}", requireCode);
    }

    @Override
    public PageInfo<PackDemandResponseDTO> queryPackDemandList(QueryPackDemandDTO condition) {
        Page page = PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
        List<PackDemandE> packDemandEList = packDemandMapper.queryPackDemandList(condition);
        if (CollectionUtils.isEmpty(packDemandEList)) {
            return new PageInfo<>();
        }
        List<PackDemandResponseDTO> packDemandDTOS = queryHandlePackDemandData(packDemandEList);
        PageInfo<PackDemandResponseDTO> pageInfo = new PageInfo<>(packDemandDTOS);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }
}
