package com.lyf.scm.core.service.stockFront.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.CommonConstants;
import com.lyf.scm.common.constants.KibanaLogConstants;
import com.lyf.scm.common.enums.FrontRecordStatusEnum;
import com.lyf.scm.common.enums.FrontRecordTypeEnum;
import com.lyf.scm.common.enums.WarehouseRecordTypeEnum;
import com.lyf.scm.common.util.date.DateUtil;
import com.lyf.scm.core.api.dto.stockFront.InventoryRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopInventoryDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopInventoryPageDTO;
import com.lyf.scm.core.config.ServiceKibanaLog;
import com.lyf.scm.core.domain.convert.stockFront.ShopInventoryConvertor;
import com.lyf.scm.core.domain.entity.stockFront.ShopInventoryDetailE;
import com.lyf.scm.core.domain.entity.stockFront.ShopInventoryE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.stockFront.ShopInventoryDetailMapper;
import com.lyf.scm.core.mapper.stockFront.ShopInventoryMapper;
import com.lyf.scm.core.remote.base.dto.StoreDTO;
import com.lyf.scm.core.remote.base.facade.BaseFacade;
import com.lyf.scm.core.remote.item.ItemInfoTool;
import com.lyf.scm.core.remote.item.SkuQtyUnitTool;
import com.lyf.scm.core.remote.item.dto.ParamExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuInfoExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuUnitExtDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.sap.facade.SapFacade;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.remote.trade.dto.PosDaySummaryDTO;
import com.lyf.scm.core.remote.trade.facade.TradeFacade;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.stockFront.ShopInventoryDetailService;
import com.lyf.scm.core.service.stockFront.ShopInventoryHandleService;
import com.lyf.scm.core.service.stockFront.ShopInventoryService;
import com.lyf.scm.core.service.stockFront.ShopInventoryToWareHouseRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 门店盘点
 * @author zhanglong
 */
@Slf4j
@Service("shopInventoryService")
public class ShopInventoryServiceImpl implements ShopInventoryService {

    @Resource
    private ShopInventoryDetailService shopInventoryDetailService;
    @Resource
    private ShopInventoryToWareHouseRecordService shopInventoryToWareHouseRecordService;
    @Resource
    private ShopInventoryMapper shopInventoryMapper;
    @Resource
    private ShopInventoryDetailMapper shopInventoryDetailMapper;
    @Resource
    private ShopInventoryConvertor shopInventoryConvertor;
    @Resource
    private BaseFacade baseFacade;
    @Resource
    private ItemFacade itemFacade;
    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;
    @Resource
    private SkuQtyUnitTool skuQtyUnitTool;
    @Resource
    private ItemInfoTool itemInfoTool;
    @Resource
    private SapFacade sapFacade;
    @Resource
    private TradeFacade tradeFacade;
    @Resource
    private ShopInventoryHandleService shopInventoryHandleService;
    @Resource
    private OrderUtilService orderUtilService;

    @Override
    public List<Long> queryInitShopInventoryRecords(Integer startPage, Integer endPage) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date date = cal.getTime();
        List<Long> ids = shopInventoryMapper.queryInitShopInventoryRecordPage(date, startPage, endPage, FrontRecordStatusEnum.INIT.getStatus());
        return ids;
    }

    @Override
    public void handleShopInventoryRecords(Long id) {
        //查询 当前盘点前置单（初始）
        ShopInventoryE frontRecord = shopInventoryMapper.queryFrontRecordById(id, FrontRecordStatusEnum.INIT.getStatus());
        if (frontRecord == null) {
            log.info("id：{}，状态为：{}的前置单不存在，不处理", id, FrontRecordStatusEnum.INIT.getDesc());
            return;
        }
        log.info("id:{}，盘点单处理开始", id);
        //查询盘点前置单 明细
        List<ShopInventoryDetailE> detailDOS = shopInventoryDetailMapper.queryShopInventoryDetailListById(id);
        frontRecord.setFrontRecordDetails(detailDOS);
        try {
            PosDaySummaryDTO posDaySummaryDTO = tradeFacade.posDaySummary(frontRecord.getShopCode(), frontRecord.getOutCreateTime());
            if (posDaySummaryDTO == null) {
                log.info("门店：{}，盘点时间：{}交易无日结信息，不处理", frontRecord.getShopCode(), DateUtil.formatDateTime(frontRecord.getOutCreateTime()));
                return;
            }
            if (frontRecord.getBusinessType() == 1) {
                //全盘
                shopInventoryHandleService.allInventory(frontRecord);
            } else if (frontRecord.getBusinessType() == 9) {
                //账面库存
                shopInventoryHandleService.accountInventory(frontRecord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addShopInventoryRecords(List<InventoryRecordDTO> frontRecords) {
        List<String> outRecordCodes = frontRecords.stream().map(InventoryRecordDTO::getOutRecordCode).collect(Collectors.toList());
        //查询盘点单据
        List<String> finalOutRecordCodes = shopInventoryMapper.judgeExistByOutRecordCodes(outRecordCodes);
        //判断判断单是否已存入中台
        List<InventoryRecordDTO> newFrontRecords = frontRecords.stream().
                filter(frontRecord -> !finalOutRecordCodes.contains(frontRecord.getOutRecordCode() + "_" + frontRecord.getShopCode())).collect(Collectors.toList());
        //幂等判断已有单据，返回成功
        if (newFrontRecords.size() == 0) {
            return;
        }
        List<ShopInventoryE> frontRecordList = shopInventoryConvertor.shopInventoryDtoListToShopInventoryEntityList(newFrontRecords);
        //根据门店编号查询门店仓
        List<String> shopList = frontRecordList.stream().map(ShopInventoryE::getShopCode).distinct().collect(Collectors.toList());
        List<RealWarehouse> realWarehouseList = stockRealWarehouseFacade.queryRealWarehouseByShopCodes(shopList);
        for (ShopInventoryE frontRecordE : frontRecordList) {
            //根据门店查询门店仓库id
            RealWarehouse warehouse = realWarehouseList.stream().filter(realWarehouse ->
                    frontRecordE.getShopCode().equals(realWarehouse.getShopCode())).findFirst().orElse(null);
            //没有实仓，设置仓库为0，并状态为异常
            if (null == warehouse || 0 == warehouse.getId() || null == warehouse.getId()) {
                frontRecordE.setRealWarehouseId(0L);
                //状态为异常状态
                frontRecordE.setRecordStatus(FrontRecordStatusEnum.EXCEPTION_RECORD.getStatus());
            } else {
                frontRecordE.setRealWarehouseId(warehouse.getId());
                frontRecordE.setFactoryCode(warehouse.getFactoryCode());
                frontRecordE.setRealWarehouseCode(warehouse.getRealWarehouseOutCode());
                if (frontRecordE.getBusinessType() == 1 || frontRecordE.getBusinessType() == 9) {
                    //状态为 初始
                    frontRecordE.setRecordStatus(FrontRecordStatusEnum.INIT.getStatus());
                } else {
                    //状态为完成状态
                    frontRecordE.setRecordStatus(FrontRecordStatusEnum.COMPLETE.getStatus());
                }
            }
            //生成盘点前置单
            addFrontRecord(frontRecordE);
            //输出kibana日志
            log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.SHOP_INVENTORY, "addShopInventoryRecords", "门店盘点单: " +
                    frontRecordE.getOutRecordCode(), frontRecordE));
        }
        List<ShopInventoryE> sapFrontRecordList = shopInventoryConvertor.shopInventoryDtoListToShopInventoryEntityList(newFrontRecords);
        List<ShopInventoryE> sapFilterFrontRecordList = sapFrontRecordList.stream().filter(record ->
                record.getBusinessType() == 1 || record.getBusinessType() == 9).collect(Collectors.toList());
        if (sapFilterFrontRecordList != null && sapFilterFrontRecordList.size() > 0) {
            //盘点推送SAP
            sapFacade.transferInventorys(sapFrontRecordList);
        }
    }

    @Override
    public PageInfo<ShopInventoryPageDTO> queryShopInventoryList(ShopInventoryPageDTO frontRecord) {
        ShopInventoryE shopInventoryRecordE = shopInventoryConvertor.shopInventoryPageDtoToShopInventoryEntity(frontRecord);
        Page page = PageHelper.startPage(frontRecord.getPageIndex(), frontRecord.getPageSize());
        List<ShopInventoryE> list = shopInventoryMapper.selectShopInventoryList(shopInventoryRecordE);

        List<String> shopCodes = list.stream().filter(record ->
                StringUtils.isNotBlank(record.getShopCode())).map(ShopInventoryE::getShopCode).collect(Collectors.toList());
        List<StoreDTO> storeList = baseFacade.searchByCodeList(shopCodes);
        for (StoreDTO store : storeList) {
            list.stream().filter(record -> record.getShopCode().equals(store.getCode())).forEach(record -> record.setShopName(store.getName()));
        }
        List<ShopInventoryPageDTO> pageList = shopInventoryConvertor.shopInventoryListEntityToShopInventoryPageList(list);
        PageInfo<ShopInventoryPageDTO> personPageInfo = new PageInfo<>(pageList);
        personPageInfo.setTotal(page.getTotal());
        return personPageInfo;
    }

    @Override
    public List<ShopInventoryDetailDTO> queryShopInventoryDetailList(Long frontRecordId) {
        List<ShopInventoryDetailE> details = shopInventoryDetailMapper.queryShopInventoryDetailList(frontRecordId);
        List<ParamExtDTO> paramList = new ArrayList<>();
        details.forEach(detail -> {
            ParamExtDTO param = new ParamExtDTO();
            param.setSkuId(detail.getSkuId());
            param.setUnitCode(detail.getUnitCode());
            paramList.add(param);
        });
        //查询商品单位信息
        List<SkuUnitExtDTO> skuList = itemFacade.unitsBySkuIdAndUnitCodeAndMerchantId(paramList);

        //查询商品信息
        List<Long> skuIds = details.stream().map(ShopInventoryDetailE::getSkuId).distinct().collect(Collectors.toList());
        List<SkuInfoExtDTO> skuInfoList = itemFacade.skuBySkuIds(skuIds);

        //查询出入库单信息
        List<WarehouseRecordE> warehouseRecords = shopInventoryToWareHouseRecordService.queryInventoryWarehouseById(frontRecordId);
        List<WarehouseRecordDetailE> outDetailList = warehouseRecords.stream().filter(record ->
                WarehouseRecordTypeEnum.SHOP_INVENTORY_OUT_WAREHOUSE_RECORD.getType().equals(record.getRecordType()))
                .map(WarehouseRecordE::getWarehouseRecordDetailList).findAny().orElse(null);
        List<WarehouseRecordDetailE> intDetailList = warehouseRecords.stream().filter(record ->
                WarehouseRecordTypeEnum.SHOP_INVENTORY_IN_WAREHOUSE_RECORD.getType().equals(record.getRecordType()))
                .map(WarehouseRecordE::getWarehouseRecordDetailList).findAny().orElse(null);

        details.forEach(detail -> {
            //设置单位信息
            SkuUnitExtDTO skuUnit = skuList.stream().filter(sku -> sku.getSkuId().equals(detail.getSkuId()) && sku.getUnitCode().equals(detail.getUnitCode()))
                    .findFirst().orElse(null);
            if (skuUnit != null) {
                detail.setUnit(skuUnit.getUnitName());
                detail.setScale(skuUnit.getScale());
            }
            //设置商品名称
            SkuInfoExtDTO skuInfo = skuInfoList.stream().filter(sku -> sku.getId().equals(detail.getSkuId())).findFirst().orElse(null);
            if (skuInfo != null) {
                detail.setSkuName(skuInfo.getName());
            }
            detail.setStockQty(detail.getSkuQty());
            detail.setDiffStockQty(new BigDecimal(0));
            if (outDetailList != null && detail.getScale() != null) {
                outDetailList.stream().filter(outDetail -> detail.getSkuId().equals(outDetail.getSkuId())).forEach(outDetail -> {
                    detail.setDiffStockQty(detail.getDiffStockQty().subtract(outDetail.getActualQty().
                            divide(detail.getScale(), CommonConstants.DECIMAL_POINT_NUM, BigDecimal.ROUND_DOWN)));
                    detail.setStockQty(detail.getStockQty().add(outDetail.getActualQty().
                            divide(detail.getScale(), CommonConstants.DECIMAL_POINT_NUM, BigDecimal.ROUND_DOWN)));
                });
            }
            if (intDetailList != null && detail.getScale() != null) {
                intDetailList.stream().filter(inDetail -> detail.getSkuId().equals(inDetail.getSkuId())).forEach(inDetail -> {
                    detail.setDiffStockQty(detail.getDiffStockQty().add(inDetail.getActualQty().
                            divide(detail.getScale(), CommonConstants.DECIMAL_POINT_NUM, BigDecimal.ROUND_DOWN)));
                    detail.setStockQty(detail.getStockQty().subtract(inDetail.getActualQty().
                            divide(detail.getScale(), CommonConstants.DECIMAL_POINT_NUM, BigDecimal.ROUND_DOWN)));
                });
            }
        });
        List<ShopInventoryDetailDTO> list = shopInventoryConvertor.shopInventoryDetailListDtoToShopInventoryDetailListEntity(details);
        return list;
    }

    /**
     * 保存前置单据 门店盘点单
     *
     * @param frontRecordE
     * @return
     */
    private boolean addFrontRecord(ShopInventoryE frontRecordE) {
        //设置单据类型
        frontRecordE.setRecordType(FrontRecordTypeEnum.SHOP_INVENTORY_RECORD.getType());
        //生成单据编号
        String code = orderUtilService.queryOrderCode(FrontRecordTypeEnum.SHOP_INVENTORY_RECORD.getCode());
        frontRecordE.setRecordCode(code);
        List<ShopInventoryDetailE> shopInventoryDetailEList = frontRecordE.getFrontRecordDetails();
        //设置商品code或id
        itemInfoTool.convertSkuCodeFilter(shopInventoryDetailEList);
        //查询基础单位
        skuQtyUnitTool.queryBasicUnitNoChecked(shopInventoryDetailEList);
        if (StringUtils.isBlank(frontRecordE.getRemark())) {
            frontRecordE.setRemark("");
        }
        if (StringUtils.isBlank(frontRecordE.getRecordStatusReason())) {
            frontRecordE.setRecordStatusReason("");
        }
        if (StringUtils.isBlank(frontRecordE.getShopName())) {
            frontRecordE.setRecordStatusReason("");
        }
        //插入盘点单据
        shopInventoryMapper.insertShopInventoryRecord(frontRecordE);

        //插入盘点明细
        shopInventoryDetailService.saveShopInventoryDetail(frontRecordE);
        return Boolean.TRUE;
    }
}
