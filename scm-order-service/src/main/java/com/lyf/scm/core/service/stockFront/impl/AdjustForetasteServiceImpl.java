package com.lyf.scm.core.service.stockFront.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.KibanaLogConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.FrontRecordStatusEnum;
import com.lyf.scm.common.enums.FrontRecordTypeEnum;
import com.lyf.scm.common.enums.ShopAdjustRecordBusinessReasonEnum;
import com.lyf.scm.common.enums.WarehouseRecordStatusEnum;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.stockFront.ShopAdjustDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopAdjustRecordDTO;
import com.lyf.scm.core.config.ServiceKibanaLog;
import com.lyf.scm.core.domain.convert.stockFront.AdjustForetasteConvertor;
import com.lyf.scm.core.domain.convert.stockFront.StockRecordDTOConvert;
import com.lyf.scm.core.domain.entity.stockFront.AdjustForetasteDetailE;
import com.lyf.scm.core.domain.entity.stockFront.AdjustForetasteE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.stockFront.AdjustForetasteMapper;
import com.lyf.scm.core.remote.base.dto.SaleOrgDTO;
import com.lyf.scm.core.remote.base.dto.StoreDTO;
import com.lyf.scm.core.remote.base.facade.BaseFacade;
import com.lyf.scm.core.remote.item.ItemInfoTool;
import com.lyf.scm.core.remote.item.SkuQtyUnitTool;
import com.lyf.scm.core.remote.item.dto.SkuInfoExtDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.stock.dto.OutWarehouseRecordDTO;
import com.lyf.scm.core.remote.stock.dto.QueryRealWarehouseDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.remote.stock.facade.StockRecordFacade;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.stockFront.AdjustForetasteDetailService;
import com.lyf.scm.core.service.stockFront.AdjustForetasteService;
import com.lyf.scm.core.service.stockFront.AdjustForetasteToWareHouseRecordService;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhanlong
 */
@Slf4j
@Service("adjustForetasteService")
public class AdjustForetasteServiceImpl implements AdjustForetasteService {

    @Resource
    private AdjustForetasteDetailService adjustForetasteDetailService;
    @Resource
    private AdjustForetasteToWareHouseRecordService adjustForetasteToWareHouseRecordService;
    @Resource
    private AdjustForetasteMapper adjustForetasteMapper;
    @Resource
    private AdjustForetasteConvertor adjustForetasteConvertor;
    @Resource
    private BaseFacade baseFacade;
    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;
    @Resource
    private ItemFacade itemFacade;
    @Resource
    private SkuQtyUnitTool skuQtyUnitTool;
    @Resource
    private ItemInfoTool itemInfoTool;
    @Resource
    private StockRecordFacade stockRecordFacade;
    @Resource
    private OrderUtilService orderUtilService;
    @Resource
    private StockRecordDTOConvert stockRecordDTOConvert;

    @Override
    public PageInfo<ShopAdjustRecordDTO> findShopForetasteCondition(ShopAdjustRecordDTO param, int pageNum, int pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<AdjustForetasteE> shopEList = adjustForetasteMapper.selectShopAdjustForetasteList(param);
        PageInfo<ShopAdjustRecordDTO> shopDtoList = new PageInfo<>(adjustForetasteConvertor.convertE2DTOList(shopEList));

        //根据仓库id查询仓库名称
        Set<QueryRealWarehouseDTO> warehouseDTOSet = new HashSet();
        shopEList.stream().filter(adjust -> StringUtils.isNotEmpty(adjust.getFactoryCode()) && StringUtils.isNotEmpty(adjust.getRealWarehouseCode()))
                .forEach(adjustForetaste -> {
                    QueryRealWarehouseDTO warehouseDTO = new QueryRealWarehouseDTO();
                    warehouseDTO.setFactoryCode(adjustForetaste.getFactoryCode());
                    warehouseDTO.setWarehouseOutCode(adjustForetaste.getRealWarehouseCode());
                    warehouseDTOSet.add(warehouseDTO);
                });
        List<RealWarehouse> realWarehouseList = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(new ArrayList<>(warehouseDTOSet));
        Map<Long, RealWarehouse> warehouseEMap = realWarehouseList.stream().collect(Collectors.toMap(RealWarehouse::getId, Function.identity(), (key1, key2)->key1));

        //根据门店code查询组织归属
        List<String> shopCodeList = shopEList.stream().map(AdjustForetasteE::getShopCode).distinct().collect(Collectors.toList());
        List<StoreDTO> storeDTOS = baseFacade.searchByCodeList(shopCodeList);
        Map<String, StoreDTO> codeMap = storeDTOS.stream().collect(Collectors.toMap(StoreDTO::getCode, Function.identity(), (key1, key2)->key1));
        Map<String, SaleOrgDTO> orgMap = new HashMap<>();

        shopDtoList.getList().forEach(shopAdjustRecordDTO -> {
            if (warehouseEMap.containsKey(shopAdjustRecordDTO.getRealWarehouseId())) {
                RealWarehouse warehouseE = warehouseEMap.get(shopAdjustRecordDTO.getRealWarehouseId());
                if (null != warehouseE) {
                    shopAdjustRecordDTO.setRealWarehouseName(warehouseE.getRealWarehouseName());
                }
            }
            if (codeMap.containsKey(shopAdjustRecordDTO.getShopCode())) {
                StoreDTO storeDTO = codeMap.get(shopAdjustRecordDTO.getShopCode());
                if (!orgMap.containsKey(storeDTO.getCompanyCode())) {
                    SaleOrgDTO orgCode = baseFacade.getOrgByOrgCode(storeDTO.getCompanyCode());
                    if (null != orgCode) {
                        shopAdjustRecordDTO.setOrganizationName(orgCode.getOrgName());
                        orgMap.put(orgCode.getOrgCode(), orgCode);
                    }
                } else {
                    shopAdjustRecordDTO.setOrganizationName(orgMap.get(storeDTO.getCompanyCode()).getOrgName());
                }
            }
            shopAdjustRecordDTO.setRecordStatusDesc(WarehouseRecordStatusEnum.getDescByType(shopAdjustRecordDTO.getRecordStatus()));
            shopAdjustRecordDTO.setReasonDesc(ShopAdjustRecordBusinessReasonEnum.getDescByType(shopAdjustRecordDTO.getReason()));
            shopAdjustRecordDTO.setRecordTypeDesc(FrontRecordTypeEnum.getDescByType(shopAdjustRecordDTO.getRecordType()));
        });
        shopDtoList.setTotal(page.getTotal());
        shopDtoList.setPageNum(page.getPageNum());
        return shopDtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addShopForetasteRecord(ShopAdjustRecordDTO frontRecord) {
        log.info("=====门店试吃接收到试吃信息，开始处理======单号:" + frontRecord.getOutRecordCode() + "==门店号:" + frontRecord.getShopCode());
        Integer count = adjustForetasteMapper.selectCountByOutRecordCode(frontRecord.getOutRecordCode(), frontRecord.getShopCode());
        if (null != count && count > 0) {
            log.info("单号已经存在");
            return;
        }
        if (null == frontRecord.getFrontRecordDetails() || 0 == frontRecord.getFrontRecordDetails().size()) {
            throw new RomeException(ResCode.ORDER_ERROR_7001, ResCode.ORDER_ERROR_7001_DESC);
        }

        List<ShopAdjustDetailDTO> frontRecordDetails = frontRecord.getFrontRecordDetails();
        //提取skuCode
        List<String> skuCodes = frontRecordDetails.stream().map(ShopAdjustDetailDTO::getSkuCode).distinct().collect(Collectors.toList());
        //批量根据skuCode查询skuId
        List<SkuInfoExtDTO> skuInfoExtDTOS = itemFacade.skuBySkuCodes(skuCodes);
        Map<String, SkuInfoExtDTO> skuIdInfos = skuInfoExtDTOS.stream().collect(Collectors.toMap(SkuInfoExtDTO::getSkuCode, Function.identity(), (key1, key2)->key1));
        //接口传输的单位为单位名称，根据单位名称查询单位code
        for (ShopAdjustDetailDTO detailDTO : frontRecord.getFrontRecordDetails()) {
            if (skuIdInfos.containsKey(detailDTO.getSkuCode())) {
                SkuInfoExtDTO infoExtDTO = skuIdInfos.get(detailDTO.getSkuCode());
                detailDTO.setSkuId(infoExtDTO.getId());
                detailDTO.setUnitCode(infoExtDTO.getSpuUnitCode());
                detailDTO.setUnit(infoExtDTO.getSpuUnitName());
            }
        }
        frontRecord.setRecordType(FrontRecordTypeEnum.SHOP_FORETASTE_RECORD.getType());
        frontRecord.setReason(ShopAdjustRecordBusinessReasonEnum.PROMOTIONS_FORETASTE.getReason());
        AdjustForetasteE frontRecordE = adjustForetasteConvertor.dtoToEntity(frontRecord);

        //根据shopCode查询实仓ID
        RealWarehouse realWarehouse = stockRealWarehouseFacade.queryRealWarehouseByShopCode(frontRecord.getShopCode());
        AlikAssert.isNotNull(realWarehouse, "999", "当前仓库不存在");
        frontRecordE.setRealWarehouseId(realWarehouse.getId());
        frontRecordE.setFactoryCode(realWarehouse.getFactoryCode());
        frontRecordE.setRealWarehouseCode(realWarehouse.getRealWarehouseOutCode());
        frontRecordE.setRecordStatus(FrontRecordStatusEnum.OUT_ALLOCATION.getStatus());
        //创建门店试吃单 + 明细
        addAdjustForetasteRecord(frontRecordE);

        //创建后置 出库单
        WarehouseRecordE warehouseRecord = adjustForetasteToWareHouseRecordService.createWarehouseRecordByAdjustForetaste(frontRecordE);
        //输出kibana日志
        log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.SHOP_ADJUST_FORETASTE, "addShopForetasteRecord",
                "门店试吃调整单: " + frontRecord.getOutRecordCode(), frontRecord));
        try {
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
            log.error(e.getMessage(), e);
            throw new RomeException(ResCode.ORDER_ERROR_7002, ResCode.ORDER_ERROR_7002_DESC);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    /**
     * 保存 试吃 调整单 和 明细
     *
     * @param frontRecord
     */
    private void addAdjustForetasteRecord(AdjustForetasteE frontRecord) {
        List<AdjustForetasteDetailE> frontRecordDetails = frontRecord.getFrontRecordDetails();

        //获取单据编号
        String code = orderUtilService.queryOrderCode(FrontRecordTypeEnum.SHOP_FORETASTE_RECORD.getCode());
        frontRecord.setRecordCode(code);
        //设置商品code或id
        itemInfoTool.convertSkuCode(frontRecordDetails);
        //单位转换
        skuQtyUnitTool.convertRealToBasic(frontRecordDetails);

        //保存试吃调整单
        adjustForetasteMapper.insert(frontRecord);

        //保存试吃调整单明细
        adjustForetasteDetailService.saveAdjustForetasteDetail(frontRecord);
    }
}
