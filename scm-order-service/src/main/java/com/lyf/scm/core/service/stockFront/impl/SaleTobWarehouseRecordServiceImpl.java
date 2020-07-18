package com.lyf.scm.core.service.stockFront.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.enums.WarehouseRecordStatusEnum;
import com.lyf.scm.common.enums.WarehouseRecordTypeEnum;
import com.lyf.scm.core.api.dto.stockFront.SaleTobWarehouseRecordCondition;
import com.lyf.scm.core.api.dto.stockFront.SaleTobWarehouseRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.SaleTobWarehouseRecordDetailDTO;
import com.lyf.scm.core.domain.convert.stockFront.SaleTobWarehouseRecordConvertor;
import com.lyf.scm.core.domain.entity.stockFront.FrontWarehouseRecordRelationE;
import com.lyf.scm.core.domain.entity.stockFront.ReplenishRecordE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.stockFront.FrReplenishRecordMapper;
import com.lyf.scm.core.mapper.stockFront.FrontWarehouseRecordRelationMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordDetailMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.remote.base.dto.ChannelDTO;
import com.lyf.scm.core.remote.base.dto.StoreDTO;
import com.lyf.scm.core.remote.base.facade.BaseFacade;
import com.lyf.scm.core.remote.item.dto.SkuInfoExtDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.stock.dto.QueryRealWarehouseDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.service.stockFront.SaleTobWarehouseRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 门店销售
 */
@Service
@Slf4j
public class SaleTobWarehouseRecordServiceImpl implements SaleTobWarehouseRecordService {

    @Resource
    private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;

    @Resource
    private FrReplenishRecordMapper frReplenishRecordMapper;

    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;
    @Resource
    private WarehouseRecordDetailMapper warehouseRecordDetailMapper;

    @Autowired
    private BaseFacade baseFacade;

    @Autowired
    private ItemFacade itemFacade;

    @Autowired
    private StockRealWarehouseFacade stockRealWarehouseFacade;

    @Autowired
    private SaleTobWarehouseRecordConvertor saleTobWarehouseRecordConvertor;

    /**
     * 查询出库单
     *
     * @param condition
     * @return
     */
    @Override
    public PageInfo<SaleTobWarehouseRecordDTO> queryWarehouseRecordList(SaleTobWarehouseRecordCondition condition) {
        if (StringUtils.isNotBlank(condition.getOutRealWarehouseCode())) {
            RealWarehouse realWarehouseE = stockRealWarehouseFacade.queryRealWarehouseByRWCode(condition.getOutRealWarehouseCode());
            if (realWarehouseE == null) {
                return new PageInfo<>();
            }
            condition.setOutRealWarehouseId(realWarehouseE.getId());
        }
        //如果渠道查询不为空
        if (StringUtils.isNotBlank(condition.getChannelCodes())) {
            String[] channelCodes = condition.getChannelCodes().split(",");
            condition.setChannelCodeList(Arrays.asList(channelCodes));
        }
        //如果发货单号有查询值
        if (StringUtils.isNotBlank(condition.getRecordCode())) {
            condition.setRecordCodes(new LinkedList<>());
            condition.getRecordCodes().add(condition.getRecordCode());
        } else if (StringUtils.isNotBlank(condition.getFrontRecordCode())) {
            //根据前置单号查询发货单号
            List<String> frontRecordCodes = new ArrayList<>();
            frontRecordCodes.add(condition.getFrontRecordCode());
            List<String> recordCodes = frontWarehouseRecordRelationMapper.getRecordCodesByFrontRecordCodes(frontRecordCodes);
            condition.setRecordCodes(recordCodes);
        }
        //如果存在接收门店查询
        if (StringUtils.isNotBlank(condition.getInShopCode()) || StringUtils.isNotBlank(condition.getSapPoNo())
                || StringUtils.isNotBlank(condition.getOutRecordCode())) {
            List<String> sapPoNos = null;
            if(StringUtils.isNotBlank(condition.getSapPoNo())){
                String replaceStr = condition.getSapPoNo().replaceAll("\\n", ",");
                String[] split = replaceStr.split(",");
                sapPoNos = new ArrayList<>(split.length);
                Collections.addAll(sapPoNos,split);
            }
            //查找前置单中存在的前置单号
            List<String> frontRecordCodes = frReplenishRecordMapper.queryFrontRecordCodesByInShopCode(condition.getInShopCode(), sapPoNos, condition.getOutRecordCode());
            //根据前置单号查找关系表中的发货单号
            if(CollectionUtils.isEmpty(frontRecordCodes)) {
                return new PageInfo<>();
            }
            List<String> recordCodes = frontWarehouseRecordRelationMapper.getRecordCodesByFrontRecordCodes(frontRecordCodes);
            if (CollectionUtils.isEmpty(recordCodes)) {
                return new PageInfo<>();
            }
            if (CollectionUtils.isNotEmpty(condition.getRecordCodes())) {
                condition.getRecordCodes().addAll(recordCodes);
                List<String> distinctRecordCodes = condition.getRecordCodes().stream().distinct().collect(Collectors.toList());
                condition.setRecordCodes(distinctRecordCodes);
            } else {
                condition.setRecordCodes(recordCodes);
            }
        }

        //重复判断的原因：如果查询条件设置了发货单号，则只根据发货单号查
        if(StringUtils.isNotBlank(condition.getRecordCode())){
            condition.setRecordCodes(new LinkedList<>());
            condition.getRecordCodes().add(condition.getRecordCode());
        }
        //写入发货单类型
        Set<Integer> types = WarehouseRecordTypeEnum.getReplenishOutWarehouseRecordList().keySet();
        Page page = PageHelper.startPage(condition.getPageIndex(), condition.getPageSize());
        List<SaleTobWarehouseRecordDTO> saleTobWarehouseRecordDTOS = warehouseRecordMapper.queryWarehouseRecordListByCondition(condition, types);
        if (CollectionUtils.isEmpty(saleTobWarehouseRecordDTOS)) {
            return new PageInfo<>();
        }
        //获取渠道信息
        List<ChannelDTO> channelDTOs = baseFacade.queryChannelInfoByChannelCodeList(saleTobWarehouseRecordDTOS.stream().map(SaleTobWarehouseRecordDTO::getChannelCode).distinct().collect(Collectors.toList()));
        Map<String, String> channelInfo = channelDTOs.stream().collect(Collectors.toMap(ChannelDTO::getChannelCode, ChannelDTO::getChannelName));
        //写入单号及其接收门店和发货仓信息
        List<String> recordCodes = saleTobWarehouseRecordDTOS.stream().map(SaleTobWarehouseRecordDTO::getRecordCode).distinct().collect(Collectors.toList());
        List<FrontWarehouseRecordRelationE> frontWarehouseRecordRelationEs = frontWarehouseRecordRelationMapper.getFrontRelationByRecordCodes(recordCodes);
        //发货单----不重复前置单集合
        Map<String, List<String>> recordRelationMap = new HashMap<>();
        frontWarehouseRecordRelationEs.forEach(item -> {
            if (recordRelationMap.containsKey(item.getRecordCode())) {
                if (!recordRelationMap.get(item.getRecordCode()).contains(item.getFrontRecordCode())) {
                    recordRelationMap.get(item.getRecordCode()).add(item.getFrontRecordCode());
                }
            } else {
                List<String> frontRecordCodeList = new LinkedList<>();
                frontRecordCodeList.add(item.getFrontRecordCode());
                recordRelationMap.put(item.getRecordCode(), frontRecordCodeList);
            }
        });

        List<String> frontRecordCodes = frontWarehouseRecordRelationEs.stream().map(FrontWarehouseRecordRelationE::getFrontRecordCode).distinct().collect(Collectors.toList());
        List<ReplenishRecordE> ReplenishRecordEs = frReplenishRecordMapper.queryFrontRecordByRecordCodes(frontRecordCodes);
        //获取前置单中的入仓库信息
        //获取出仓Id
        List<QueryRealWarehouseDTO> queryRealWarehouseDTOList = new ArrayList<QueryRealWarehouseDTO>();
        saleTobWarehouseRecordDTOS.forEach(saleTobWarehouseRecordDTO -> {
            QueryRealWarehouseDTO queryRealWarehouseDTO = new QueryRealWarehouseDTO();
            queryRealWarehouseDTO.setFactoryCode(saleTobWarehouseRecordDTO.getOutFactoryCode());
            queryRealWarehouseDTO.setWarehouseOutCode(saleTobWarehouseRecordDTO.getOutRealWarehouseCode());
            queryRealWarehouseDTOList.add(queryRealWarehouseDTO);
        });

        List<RealWarehouse> realWarehouseEs = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(queryRealWarehouseDTOList);
        //仓库id---仓库名称
        Map<Long, String> realWarehouseInfo = realWarehouseEs.stream().collect(Collectors.toMap(RealWarehouse::getId, RealWarehouse::getRealWarehouseName));
        //前置单编号——前置单信息
        Map<String, ReplenishRecordE> stringReplenishRecordEMap = ReplenishRecordEs.stream().collect(Collectors.toMap(ReplenishRecordE::getRecordCode, item -> item));
        //获取所有门店对应的code

        //Map<Long, String> idCodeMap = inRealWarehouseEs.stream().collect(Collectors.toMap(RealWarehouseE::getId, RealWarehouseE::getRealWarehouseCode));
        List<StoreDTO> StoreDTOs = baseFacade.searchByCodeList(ReplenishRecordEs.stream().map(ReplenishRecordE::getShopCode).distinct().collect(Collectors.toList()));
        Map<String, String> codeFactoryMap = StoreDTOs.stream().collect(Collectors.toMap(StoreDTO::getCode, StoreDTO::getDeliveryFactory));
        //写入订单状态名称和写入单据类型名称
        saleTobWarehouseRecordDTOS.forEach(item -> {
            item.setDisparityHandle(false);
            if (WarehouseRecordTypeEnum.getDescByType(item.getRecordType()) != null) {
                item.setRecordTypeName(WarehouseRecordTypeEnum.getDescByType(item.getRecordType()));
            }
            if (WarehouseRecordStatusEnum.getDescByType(item.getRecordStatus()) != null) {
                item.setRecordStatusName(WarehouseRecordStatusEnum.getDescByType(item.getRecordStatus()));
            }
            //加入渠道名称
            if (channelInfo.containsKey(item.getChannelCode())) {
                item.setChannelName(channelInfo.get(item.getChannelCode()));
            }
            //发货仓名称
            if (realWarehouseInfo.containsKey(item.getOutRealWarehouseId())) {
                item.setOutRealWarehouseName(realWarehouseInfo.get(item.getOutRealWarehouseId()));
            }
            //前置单信息
            if (recordRelationMap.containsKey(item.getRecordCode())) {
                //设置入仓名称
                if (stringReplenishRecordEMap.containsKey(recordRelationMap.get(item.getRecordCode()).get(0))) {
                    ReplenishRecordE masterFront = stringReplenishRecordEMap.get(recordRelationMap.get(item.getRecordCode()).get(0));
                    item.setInRealWarehouseName(masterFront.getShopName());
                    item.setShopCode(masterFront.getShopCode());
                }
                //设置前置单中sap_po_no及out_record_code信息
                List<String> frontRecordCodesItem = recordRelationMap.get(item.getRecordCode());
                final StringBuilder sapPoNoStr = new StringBuilder("");
                final StringBuilder outRecordCodeStr = new StringBuilder("");
                frontRecordCodesItem.forEach(itemFrontRecordCode ->
                        {
                            if (stringReplenishRecordEMap.containsKey(itemFrontRecordCode)) {
                                if (stringReplenishRecordEMap.get(itemFrontRecordCode).getSapPoNo() != null) {
                                    sapPoNoStr.append(stringReplenishRecordEMap.get(itemFrontRecordCode).getSapPoNo()).append("<br/>");
                                }
                                if (stringReplenishRecordEMap.get(itemFrontRecordCode).getOutRecordCode() != null) {
                                    outRecordCodeStr.append(stringReplenishRecordEMap.get(itemFrontRecordCode).getOutRecordCode()).append("<br/>");
                                }
                            }
                        }
                );
                if (!"".equals(sapPoNoStr.toString())) {
                    item.setSapPoNo(sapPoNoStr.substring(0, sapPoNoStr.lastIndexOf("<br/>")).toString());
                }
                if (!"".equals(outRecordCodeStr.toString())) {
                    item.setOutRecordCode(outRecordCodeStr.substring(0, outRecordCodeStr.lastIndexOf("<br/>")).toString());
                }
                final StringBuilder frontRecordCodeStr = new StringBuilder("");
                List<String> frontRecordList = new ArrayList<>();
                recordRelationMap.get(item.getRecordCode()).forEach(frontRecordCode -> {
                    frontRecordCodeStr.append(frontRecordCode).append("<br/>");
                    frontRecordList.add(frontRecordCode);
                });
                item.setFrontRecordCodes(frontRecordCodeStr.substring(0, frontRecordCodeStr.lastIndexOf("<br/>")).toString());
                item.setFrontRecordList(frontRecordList);
            }
            //加入门店deliveryFactory
            for (String frontRecordCode : item.getFrontRecordList()) {
                if (stringReplenishRecordEMap.containsKey(frontRecordCode)) {
                    if (codeFactoryMap.containsKey(stringReplenishRecordEMap.get(frontRecordCode).getShopCode())) {
                        item.setDeliveryFactory(codeFactoryMap.get(stringReplenishRecordEMap.get(frontRecordCode).getShopCode()));
                        break;
                    }
                }
            }

        });
        PageInfo<SaleTobWarehouseRecordDTO> pageInfo = new PageInfo<>(saleTobWarehouseRecordDTOS);
        return pageInfo;
    }

    @Override
    public List<Map<String, String>> getShopInfo() {
        return frReplenishRecordMapper.getShopInfo();
    }

    @Override
    public SaleTobWarehouseRecordDTO getWarehouseSaleTobDetail(Long warehouseRecordId) {
        //根据发货单号查询发货单主表信息
        WarehouseRecordE warehouseRecordE = warehouseRecordMapper.getWarehouseRecordById(warehouseRecordId);
        List<WarehouseRecordDetailE> details = warehouseRecordDetailMapper.queryListByRecordId(warehouseRecordE.getId());
        if (CollectionUtils.isNotEmpty(details)) {
            warehouseRecordE.setWarehouseRecordDetailList(details);
        }
        //创建展示对象并且赋值
        SaleTobWarehouseRecordDTO saleTobWarehouseRecordDTO = new SaleTobWarehouseRecordDTO();
        saleTobWarehouseRecordDTO.setRecordCode(warehouseRecordE.getRecordCode());
        saleTobWarehouseRecordDTO.setOutRealWarehouseId(warehouseRecordE.getRealWarehouseId());
        saleTobWarehouseRecordDTO.setOutRealWarehouseCode(warehouseRecordE.getRealWarehouseCode());
        saleTobWarehouseRecordDTO.setOutFactoryCode(warehouseRecordE.getFactoryCode());
        saleTobWarehouseRecordDTO.setChannelCode(warehouseRecordE.getChannelCode());
        saleTobWarehouseRecordDTO.setRecordType(warehouseRecordE.getRecordType());
        saleTobWarehouseRecordDTO.setRecordStatus(warehouseRecordE.getRecordStatus());
        saleTobWarehouseRecordDTO.setDetails(new LinkedList<>());
        saleTobWarehouseRecordDTO.setSapOrderCode(warehouseRecordE.getSapOrderCode());
        warehouseRecordE.getWarehouseRecordDetailList().forEach(item -> {
            SaleTobWarehouseRecordDetailDTO saleTobWarehouseRecordDetailDTO = saleTobWarehouseRecordConvertor. convertEToSaleTobDetailDto(item);
            saleTobWarehouseRecordDTO.getDetails().add(saleTobWarehouseRecordDetailDTO);
        });
        //写入单号及其接收门店和发货仓信息
        List<String> recordCodes = new LinkedList<>();
        recordCodes.add(saleTobWarehouseRecordDTO.getRecordCode());
        List<FrontWarehouseRecordRelationE> frontWarehouseRecordRelationEs = frontWarehouseRecordRelationMapper.getFrontRelationByRecordCodes(recordCodes);
        //获取前置单并且写入展示类
        final StringBuilder frontRecordCodeStr = new StringBuilder("");
        frontWarehouseRecordRelationEs.forEach(frontRecordCode -> {
            frontRecordCodeStr.append(frontRecordCode.getFrontRecordCode()).append(",");
        });
        if (!"".equals(frontRecordCodeStr.toString())) {
            saleTobWarehouseRecordDTO.setFrontRecordCodes(frontRecordCodeStr.substring(0, frontRecordCodeStr.lastIndexOf(",")).toString());
        }
        //查询前置单中的门店并赋值给展示类
        List<String> frontRecordCodes = frontWarehouseRecordRelationEs.stream().map(FrontWarehouseRecordRelationE::getFrontRecordCode).distinct().collect(Collectors.toList());
        List<ReplenishRecordE> ReplenishRecordEs = frReplenishRecordMapper.queryFrontRecordByRecordCodes(frontRecordCodes);
        if (CollectionUtils.isNotEmpty(ReplenishRecordEs)) {
            saleTobWarehouseRecordDTO.setInRealWarehouseName(ReplenishRecordEs.get(0).getShopName());
        }
        //写入发货仓名称
        List<QueryRealWarehouseDTO> queryRealWarehouseDTOS = new ArrayList<QueryRealWarehouseDTO>();
        QueryRealWarehouseDTO queryRealWarehouseDTO = new QueryRealWarehouseDTO();
        queryRealWarehouseDTO.setFactoryCode(saleTobWarehouseRecordDTO.getOutFactoryCode());
        queryRealWarehouseDTO.setWarehouseOutCode(saleTobWarehouseRecordDTO.getOutRealWarehouseCode());
        queryRealWarehouseDTOS.add(queryRealWarehouseDTO);
        List<RealWarehouse> realWarehouseEs = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(queryRealWarehouseDTOS);
        if (CollectionUtils.isNotEmpty(realWarehouseEs)) {
            saleTobWarehouseRecordDTO.setOutRealWarehouseName(realWarehouseEs.get(0).getRealWarehouseName());
        }
        //写入渠道
        //获取渠道信息
        List<String> channelCodes = new LinkedList<>();
        channelCodes.add(saleTobWarehouseRecordDTO.getChannelCode());
        List<ChannelDTO> channelDTOs = baseFacade.queryChannelInfoByChannelCodeList(channelCodes);
        if (CollectionUtils.isNotEmpty(channelDTOs)) {
            saleTobWarehouseRecordDTO.setChannelName(channelDTOs.get(0).getChannelName());
        }
        //写入单据类型名称和单据状态名称
        if (WarehouseRecordTypeEnum.getDescByType(saleTobWarehouseRecordDTO.getRecordType()) != null) {
            saleTobWarehouseRecordDTO.setRecordTypeName(WarehouseRecordTypeEnum.getDescByType(saleTobWarehouseRecordDTO.getRecordType()));
        }
        if (WarehouseRecordStatusEnum.getDescByType(saleTobWarehouseRecordDTO.getRecordStatus()) != null) {
            saleTobWarehouseRecordDTO.setRecordStatusName(WarehouseRecordStatusEnum.getDescByType(saleTobWarehouseRecordDTO.getRecordStatus()));
        }
        Map<Long, SkuInfoExtDTO> skuInfoExtDTOs = itemFacade.skuBySkuIds(saleTobWarehouseRecordDTO.getDetails().stream().map(SaleTobWarehouseRecordDetailDTO::getSkuId).distinct().collect(Collectors.toList())).stream().collect(Collectors.toMap(SkuInfoExtDTO::getId, item -> item));
        saleTobWarehouseRecordDTO.getDetails().forEach(item ->
                {
                    if (skuInfoExtDTOs.containsKey(item.getSkuId())) {
                        item.setSkuCode(skuInfoExtDTOs.get(item.getSkuId()).getSkuCode());
                        item.setSkuName(skuInfoExtDTOs.get(item.getSkuId()).getName());
                    }
                }
        );
        saleTobWarehouseRecordDTO.getDetails().sort(Comparator.comparing(dto -> dto.getSapPoNo()));
        return saleTobWarehouseRecordDTO;
    }

}
