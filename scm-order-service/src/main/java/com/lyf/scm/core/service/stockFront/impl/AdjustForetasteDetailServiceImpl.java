package com.lyf.scm.core.service.stockFront.impl;

import com.lyf.scm.common.constants.CommonConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.stockFront.ShopAdjustRecordDTO;
import com.lyf.scm.core.domain.convert.stockFront.AdjustForetasteConvertor;
import com.lyf.scm.core.domain.entity.stockFront.AdjustForetasteDetailE;
import com.lyf.scm.core.domain.entity.stockFront.AdjustForetasteE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.mapper.stockFront.AdjustForetasteDetailMapper;
import com.lyf.scm.core.mapper.stockFront.AdjustForetasteMapper;
import com.lyf.scm.core.mapper.stockFront.FrontWarehouseRecordRelationMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordDetailMapper;
import com.lyf.scm.core.remote.item.SkuQtyUnitTool;
import com.lyf.scm.core.remote.item.dto.SkuInfoExtDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.service.stockFront.AdjustForetasteDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhanlong
 */
@Slf4j
@Service("adjustForetasteDetailService")
public class AdjustForetasteDetailServiceImpl implements AdjustForetasteDetailService {

    @Resource
    private AdjustForetasteMapper adjustForetasteMapper;
    @Resource
    private AdjustForetasteDetailMapper adjustForetasteDetailMapper;
    @Resource
    private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;
    @Resource
    private WarehouseRecordDetailMapper warehouseRecordDetailMapper;
    @Resource
    private AdjustForetasteConvertor adjustForetasteConvertor;
    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;
    @Resource
    private SkuQtyUnitTool skuQtyUnitTool;
    @Resource
    private ItemFacade itemFacade;

    @Override
    public ShopAdjustRecordDTO getAdjustForetasteByRecordId(Long recordId) {
        AdjustForetasteE foretasteRecordE = adjustForetasteMapper.selectByPrimaryKey(recordId);
        List<AdjustForetasteDetailE> detailES = adjustForetasteDetailMapper.queryAdjustForetasteDetailByRecordId(foretasteRecordE.getId());

        //根据前置单id查询出库单信息
        List<Long> warehouseIds = frontWarehouseRecordRelationMapper.queryWarehouseIdByFrontId(foretasteRecordE.getId(), foretasteRecordE.getRecordType());
        AlikAssert.isTrue(warehouseIds != null && warehouseIds.size() > 0, ResCode.ORDER_ERROR_1017, ResCode.ORDER_ERROR_1017_DESC);
        Long warehouseId = warehouseIds.get(0);
        //根据出库单id查询出库单明细
        List<WarehouseRecordDetailE> warehouseRecordDetails = warehouseRecordDetailMapper.queryListByRecordId(warehouseId);

        try {
            skuQtyUnitTool.convertRealToBasic(detailES);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        //根据商品id获取商品信息
        List<Long> skuIds = detailES.stream().map(AdjustForetasteDetailE::getSkuId).distinct().collect(Collectors.toList());
        List<SkuInfoExtDTO> skuInfoList = new ArrayList<>();
        try {
            skuInfoList = itemFacade.skuBySkuIds(skuIds);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        Map<Long, SkuInfoExtDTO> skuInfoMap = skuInfoList.stream().collect(Collectors.toMap(SkuInfoExtDTO::getId, Function.identity(), (key1, key2)->key1));
        Map<Long, WarehouseRecordDetailE> warehouseRecordDetailMap = warehouseRecordDetails.stream().
                collect(Collectors.toMap(WarehouseRecordDetailE::getSkuId, Function.identity(), (key1, key2)->key1));

        for (AdjustForetasteDetailE detailE : detailES) {
            if (skuInfoMap.containsKey(detailE.getSkuId())) {
                detailE.setSkuCode(skuInfoMap.get(detailE.getSkuId()).getSkuCode());
                detailE.setSkuName(skuInfoMap.get(detailE.getSkuId()).getName());
                if (warehouseRecordDetailMap.containsKey(detailE.getSkuId()) && null != detailE.getScale()) {
                    //计算实际出库数量
                    WarehouseRecordDetailE warehouseRecordDetail = warehouseRecordDetailMap.get(detailE.getSkuId());
                    detailE.setActualQty(warehouseRecordDetail.getActualQty().divide(detailE.getScale(), CommonConstants.DECIMAL_POINT_NUM, BigDecimal.ROUND_DOWN));
                }
            } else {
                detailE.setSkuName("");
            }
        }
        foretasteRecordE.setFrontRecordDetails(detailES);
        ShopAdjustRecordDTO shopAdjustRecordDTO = adjustForetasteConvertor.convertE2DTO(foretasteRecordE);
        RealWarehouse realWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(foretasteRecordE.getRealWarehouseCode(), foretasteRecordE.getFactoryCode());
        if (null != realWarehouse) {
            shopAdjustRecordDTO.setRealWarehouseName(realWarehouse.getRealWarehouseName());
            shopAdjustRecordDTO.setRealWarehouseOutCode(realWarehouse.getRealWarehouseOutCode());
        }
        return shopAdjustRecordDTO;
    }

    @Override
    public boolean saveAdjustForetasteDetail(AdjustForetasteE frontRecord) {
        List<AdjustForetasteDetailE> list = frontRecord.getFrontRecordDetails();
        list.forEach(detail -> {
            detail.setFrontRecordId(frontRecord.getId());
            detail.setRecordCode(frontRecord.getRecordCode());
        });
        Integer flag = adjustForetasteDetailMapper.saveFrAdjustForetasteDetailList(list);
        return null != flag && flag > 0;
    }
}
