package com.lyf.scm.core.service.stockFront.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.constants.ShopReplenishConstant;
import com.lyf.scm.common.constants.WarehouseRecordConstant;
import com.lyf.scm.common.enums.*;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.notify.StockNotifyDTO;
import com.lyf.scm.core.api.dto.stockFront.*;
import com.lyf.scm.core.config.RedisUtil;
import com.lyf.scm.core.domain.convert.stockFront.ReplenishAllotLogConvertor;
import com.lyf.scm.core.domain.convert.stockFront.ReplenishRecordConvert;
import com.lyf.scm.core.domain.convert.stockFront.StockInRecordDTOConvert;
import com.lyf.scm.core.domain.convert.stockFront.StockRecordDTOConvert;
import com.lyf.scm.core.domain.entity.stockFront.*;
import com.lyf.scm.core.mapper.stockFront.*;
import com.lyf.scm.core.remote.base.dto.StoreDTO;
import com.lyf.scm.core.remote.base.facade.BaseFacade;
import com.lyf.scm.core.remote.item.ItemInfoTool;
import com.lyf.scm.core.remote.item.SkuQtyUnitTool;
import com.lyf.scm.core.remote.item.dto.SkuInfoExtDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.stock.dto.*;
import com.lyf.scm.core.remote.stock.facade.StockQueryFacade;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.remote.stock.facade.StockRecordFacade;
import com.lyf.scm.core.remote.stock.facade.StockReplenishFacade;
import com.lyf.scm.core.service.disparity.DisparityRecordService;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.stockFront.ShopReplenishService;
import com.lyf.scm.core.service.stockFront.WarehouseRecordCommService;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 门店补货ServiceImpl
 * <p>
 * @Author: chuwenchao  2020/6/10
 */
@Slf4j
@Service("shopReplenishService")
public class ShopReplenishServiceImpl implements ShopReplenishService {

    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;
    @Resource
    private ReplenishRecordMapper replenishRecordMapper;
    @Resource
    private ReplenishRecordDetailMapper replenishRecordDetailMapper;
    @Resource
    private ReplenishRecordConvert replenishRecordConvert;
    @Resource
    private OrderUtilService orderUtilService;
    @Resource
    private ItemInfoTool itemInfoTool;
    @Resource
    private SkuQtyUnitTool skuQtyUnitTool;
    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;
    @Resource
    private WarehouseRecordDetailMapper warehouseRecordDetailMapper;
    @Resource
    private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;
    @Resource
    private ReplenishAllotLogMapper replenishAllotLogMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private AllotShopReplenishTask allotShopReplenishTask;
    @Resource
    private ReplenishAllotLogConvertor replenishAllotLogConvertor;
    @Resource
    private ItemFacade itemFacade;
    @Resource
    private BaseFacade baseFacade;
    @Resource
    private StockRecordFacade stockRecordFacade;
    @Resource
    private StockRecordDTOConvert stockRecordDTOConvert;
    @Resource
    private StockInRecordDTOConvert stockInRecordDTOConvert;
    @Resource
    private StockQueryFacade stockQueryFacade;
    @Resource
    private StockReplenishFacade stockReplenishFacade;
    @Resource
    private WarehouseRecordCommService warehouseRecordCommService;
    @Resource
    private DisparityRecordService disparityRecordService;

    /**
     * 分布式锁定
     */
    private final static String ALLOT_REDIS_KEY = "replenish-allot";

    /**
     *	自动释放锁的时间
     *
     */
    private final static Integer REDIS_LAST_TIME = 3600;

    /**
     * 寻源任务的key
     */
    private final static String ALLOT_TASK_KEY = "allot_task";

    @Value("${shop.replenish.report.allot.value:0}")
    private Integer replenishReportAllotValue;

    /**
     * @Description: 创建门店补货需求单 <br>
     *
     * @Author chuwenchao 2020/6/10
     * @param frontRecord
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RealWarehouse createReplenishRecord(ShopReplenishDTO frontRecord) {
        RealWarehouse realWarehouse = null;
        //1.进行参数校验
        this.validAddDataInfo(frontRecord);
        //设置sapPO号到明细
        frontRecord.getFrontRecordDetails().forEach(detail -> detail.setSapPoNo(frontRecord.getSapPoNo()));
        //2.进行幂等校验
        ReplenishRecordE recordE = replenishRecordMapper.queryByOutCode(frontRecord.getOutRecordCode());
        if (recordE != null) {
            if(recordE.getOutRealWarehouseId() != null){
                return stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(recordE.getOutRealWarehouseCode(), recordE.getOutFactoryCode());
            }else {
                return null;
            }
        }

        //3.1 直营叫货
        if (ShopReplenishConstant.DIRECT_REPLENISH.equals(frontRecord.getRecordType())) {
            frontRecord.setRecordType(FrontRecordTypeEnum.SHOP_REPLENISHMENT_RECORD.getType());
            //普通叫货
            if (ShopReplenishConstant.COMMON_REPLENISH.equals(frontRecord.getRequireType())) {
                this.createDirectOrder(frontRecord);
            }
            //指定仓库叫货
            if (ShopReplenishConstant.ASSIGN_WH_REPLENISH.equals(frontRecord.getRequireType())) {
                this.createDirectOrderAssignWh(frontRecord);
            }
        }
        //3.2 加盟
        if (ShopReplenishConstant.JOIN_REPLENISH.equals(frontRecord.getRecordType())) {
            //普通叫货
            if (ShopReplenishConstant.COMMON_REPLENISH.equals(frontRecord.getRequireType())) {
                realWarehouse = this.createJoinOrder(frontRecord, false);
            }
            //指定仓库叫货
            if (ShopReplenishConstant.ASSIGN_WH_REPLENISH.equals(frontRecord.getRequireType())) {
                realWarehouse = this.createJoinOrderAssignWh(frontRecord, false);
            }
        }
        //3.3 加盟主配
        if (ShopReplenishConstant.ALLIANCE_BUSINESS_REPLENISH.equals(frontRecord.getRecordType())) {
            //主配普通叫货
            if (ShopReplenishConstant.COMMON_REPLENISH.equals(frontRecord.getRequireType())) {
                realWarehouse = this.createJoinOrder(frontRecord, true);
            }
            //主配指定仓库叫货
            if (ShopReplenishConstant.ASSIGN_WH_REPLENISH.equals(frontRecord.getRequireType())) {
                realWarehouse = this.createJoinOrderAssignWh(frontRecord, true);
            }
        }
        //3.4 冷链
        if (ShopReplenishConstant.COLD_CHAIN_REPLENISH.equals(frontRecord.getRecordType())) {
            this.addColdChainReplenish(frontRecord);
        }
        //3.5 直送
        if (ShopReplenishConstant.SUPPLIER_DIRECT_REPLENISH.equals(frontRecord.getRecordType())) {
            this.createSupplierReplenish(frontRecord);
        }
        return realWarehouse;
    }

    /**
     * @Description: 创建直送门店补货单 <br>
     *
     * @Author chuwenchao 2020/6/13
     * @param frontRecord
     * @return 
     */
    private void createSupplierReplenish(ShopReplenishDTO frontRecord) {
        //查询门店code对应的仓库
        RealWarehouse rw = stockRealWarehouseFacade.queryRealWarehouseByShopCode(frontRecord.getShopCode());
        AlikAssert.isNotNull(rw, ResCode.ORDER_ERROR_5102, ResCode.ORDER_ERROR_5102_DESC);
        //查询门店发货工厂指定类型仓库
        RealWarehouse outRealWarehouse = stockRealWarehouseFacade.queryRealWarehouseByShopCodeAndType(frontRecord.getShopCode(), RealWarehouseTypeEnum.RW_TYPE_7.getType());
        AlikAssert.isNotNull(outRealWarehouse, ResCode.ORDER_ERROR_5105, ResCode.ORDER_ERROR_5105_DESC);
        ReplenishRecordE replenishRecordE = replenishRecordConvert.convertDTO2E(frontRecord);
        replenishRecordE.setInRealWarehouseId(rw.getId());
        replenishRecordE.setInFactoryCode(rw.getFactoryCode());
        replenishRecordE.setInRealWarehouseCode(rw.getRealWarehouseOutCode());
        replenishRecordE.setOutRealWarehouseId(outRealWarehouse.getId());
        replenishRecordE.setOutFactoryCode(outRealWarehouse.getFactoryCode());
        replenishRecordE.setOutRealWarehouseCode(outRealWarehouse.getRealWarehouseOutCode());
        replenishRecordE.setFactoryCode(outRealWarehouse.getFactoryCode());
        //创建直送门店补货出库前置单
        replenishRecordE.setIsNeedDispatch(ShopReplenishConstant.NOT_NEED_DISPATCH);
        replenishRecordE.setIsNeedAllot(ShopReplenishConstant.NOT_NEED_ALLOT);
        this.addSupplierDirectRecord(replenishRecordE);
        //生成直送仓库出库单
        WarehouseRecordE outRecordE = this.addSupplierDirectOutOrder(replenishRecordE);
        outRecordE.setSyncDispatchStatus(WarehouseRecordConstant.INIT_DISPATCH);
        this.saveWarehouseRecord(outRecordE, replenishRecordE);
        //生成直送门店入库单
        WarehouseRecordE inRecordE = this.addSupplierDirectInOrder(replenishRecordE);
        inRecordE.setSyncDispatchStatus(WarehouseRecordConstant.INIT_DISPATCH);
        inRecordE.setSyncTradeStatus(WarehouseRecordConstant.INIT_SYNC_TRADE);
        inRecordE.setSyncTmsbStatus(WarehouseRecordConstant.INIT_SYNC_TMSB);
        this.saveWarehouseRecord(inRecordE, replenishRecordE);
        //同步库存直送仓库出库单
        OutWarehouseRecordDTO outRecord = stockRecordDTOConvert.convertE2OutDTO(outRecordE);
        stockRecordFacade.createOutRecord(outRecord);
        //同步库存直送门店入库单
        boolean flag = false;
        try {
            InWarehouseRecordDTO inRecord = stockInRecordDTOConvert.convertE2InDTO(inRecordE);
            stockRecordFacade.createInRecord(inRecord);
            flag = true;
        } catch (Exception e) {
            throw new RomeException(ResCode.ORDER_ERROR_1001, "调用库存中心生成门店入库单失败"+frontRecord.getOutRecordCode());
        } finally {
            if(!flag) {
                CancelRecordDTO cancelRecordDTO = new CancelRecordDTO();
                cancelRecordDTO.setRecordCode(outRecord.getRecordCode());
                cancelRecordDTO.setIsForceCancel(YesOrNoEnum.YES.getType());
                cancelRecordDTO.setRecordType(outRecord.getRecordType());
                warehouseRecordCommService.cancelWarehouseRecordToStock(cancelRecordDTO);
            }
        }
    }

    /**
     * @Description: 生成直送门店入库单 <br>
     *
     * @Author chuwenchao 2020/6/23
     * @param replenishRecordE
     * @return 
     */
    private WarehouseRecordE addSupplierDirectInOrder(ReplenishRecordE replenishRecordE) {
        WarehouseRecordE inRecordE = this.initWarehouseRecord(replenishRecordE, WarehouseRecordTypeEnum.SHOP_CHAIN_DIRECT_IN_RECORD);
        List<WarehouseRecordDetailE> warehouseRecordDetailList = this.initWarehouseRecordDetail(replenishRecordE);
        inRecordE.setWarehouseRecordDetailList(warehouseRecordDetailList);
        return inRecordE;
    }

    /**
     * @Description: 生成直送仓库出库单 <br>
     *
     * @Author chuwenchao 2020/6/22
     * @param replenishRecordE
     * @return 
     */
    private WarehouseRecordE addSupplierDirectOutOrder(ReplenishRecordE replenishRecordE) {
        WarehouseRecordE outRecordE = this.initWarehouseRecord(replenishRecordE, WarehouseRecordTypeEnum.WH_CHAIN_DIRECT_OUT_RECORD);
        List<WarehouseRecordDetailE> warehouseRecordDetailList = this.initWarehouseRecordDetail(replenishRecordE);
        outRecordE.setWarehouseRecordDetailList(warehouseRecordDetailList);
        return outRecordE;
    }

    /**
     * @Description: 创建直送补货前置单据 <br>
     *
     * @Author chuwenchao 2020/6/13
     * @param replenishRecordE
     * @return 
     */
    private void addSupplierDirectRecord(ReplenishRecordE replenishRecordE) {
        replenishRecordE.setRecordStatus(FrontRecordStatusEnum.INIT.getStatus());
        replenishRecordE.setRecordType(FrontRecordTypeEnum.SHOP_SUPPLIER_DIRECT_DELIVERY_RECORD.getType());
        this.saveFrontRecord(replenishRecordE, FrontRecordTypeEnum.SHOP_SUPPLIER_DIRECT_DELIVERY_RECORD.getCode());
    }

    /**
     * @Description: 生成门店冷链补货订单 <br>
     *
     * @Author chuwenchao 2020/6/13
     * @param frontRecord
     * @return 
     */
    private void addColdChainReplenish(ShopReplenishDTO frontRecord) {
        //根据门店编码查询门店仓实仓信息
        RealWarehouse inRealWarehouse = stockRealWarehouseFacade.queryRealWarehouseByShopCode(frontRecord.getShopCode());
        AlikAssert.isNotNull(inRealWarehouse, ResCode.ORDER_ERROR_5102, ResCode.ORDER_ERROR_5102_DESC);
        //查询门店对应的冷链仓库
        RealWarehouse outRealWarehouse = stockRealWarehouseFacade.queryRealWarehouseByShopCodeAndType(frontRecord.getShopCode(), RealWarehouseTypeEnum.RW_TYPE_6.getType());
        AlikAssert.isNotNull(outRealWarehouse, ResCode.ORDER_ERROR_5105, ResCode.ORDER_ERROR_5105_DESC);
        ReplenishRecordE replenishRecordE = replenishRecordConvert.convertDTO2E(frontRecord);
        replenishRecordE.setInRealWarehouseId(inRealWarehouse.getId());
        replenishRecordE.setInFactoryCode(inRealWarehouse.getFactoryCode());
        replenishRecordE.setInRealWarehouseCode(inRealWarehouse.getRealWarehouseOutCode());
        replenishRecordE.setOutRealWarehouseId(outRealWarehouse.getId());
        replenishRecordE.setOutFactoryCode(inRealWarehouse.getFactoryCode());
        replenishRecordE.setOutRealWarehouseCode(inRealWarehouse.getRealWarehouseOutCode());
        replenishRecordE.setFactoryCode(outRealWarehouse.getFactoryCode());
        //4.创建门店补货出库前置单
        replenishRecordE.setIsNeedDispatch(ShopReplenishConstant.INIT_DISPATCH);
        replenishRecordE.setIsNeedAllot(ShopReplenishConstant.NOT_NEED_ALLOT);
        this.addColdChainRecord(replenishRecordE);
        //5.生成冷链越库出库单
        WarehouseRecordE outRecordE = this.addColdChainOutOrder(replenishRecordE);
        outRecordE.setSyncDispatchStatus(WarehouseRecordConstant.NEED_DISPATCH);
        outRecordE.setSyncTradeStatus(WarehouseRecordConstant.INIT_SYNC_TRADE);
        outRecordE.setSyncTmsbStatus(WarehouseRecordConstant.NEED_SYNC_TMSB);
        this.saveWarehouseRecord(outRecordE, replenishRecordE);
        //6.同步库存冷链出库单
        OutWarehouseRecordDTO outRecord = stockRecordDTOConvert.convertE2OutDTO(outRecordE);
        stockRecordFacade.createOutRecord(outRecord);
    }

    /**
     * @Description: 冷链直送大仓出库单 <br>
     *
     * @Author chuwenchao 2020/6/13
     * @param replenishRecordE
     * @return 
     */
    private WarehouseRecordE addColdChainOutOrder(ReplenishRecordE replenishRecordE) {
        WarehouseRecordE outRecordE = this.initWarehouseRecord(replenishRecordE, WarehouseRecordTypeEnum.WH_COLD_CHAIN_OUT_RECORD);
        List<WarehouseRecordDetailE> warehouseRecordDetailList = this.initWarehouseRecordDetail(replenishRecordE);
        outRecordE.setWarehouseRecordDetailList(warehouseRecordDetailList);
        return outRecordE;
    }

    /**
     * @Description: 创建冷链补货前置单据 <br>
     *
     * @Author chuwenchao 2020/6/13
     * @param replenishRecordE
     * @return
     */
    private void addColdChainRecord(ReplenishRecordE replenishRecordE) {
        replenishRecordE.setRecordStatus(FrontRecordStatusEnum.INIT.getStatus());
        replenishRecordE.setRecordType(FrontRecordTypeEnum.SHOP_COLD_CHAIN_DELIVERY_RECORD.getType());
        this.saveFrontRecord(replenishRecordE, FrontRecordTypeEnum.SHOP_COLD_CHAIN_DELIVERY_RECORD.getCode());
    }

    /**
     * @Description: 创建加盟指定仓库补货单 <br>
     *
     * @Author chuwenchao 2020/6/13
     * @param frontRecord
     * @param isPrimaryServer 是否主配
     * @return 
     */
    private RealWarehouse createJoinOrderAssignWh(ShopReplenishDTO frontRecord, boolean isPrimaryServer) {
        //1.根据门店编码查询门店仓实仓信息
        RealWarehouse inRealWarehouse = stockRealWarehouseFacade.queryRealWarehouseByShopCode(frontRecord.getShopCode());
        AlikAssert.isNotNull(inRealWarehouse, ResCode.ORDER_ERROR_5102, ResCode.ORDER_ERROR_5102_DESC);
        //2.查询门店的出库仓库
        RealWarehouse outWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(frontRecord.getWarehouseCode(), frontRecord.getFactoryCode());
        AlikAssert.isNotNull(outWarehouse, ResCode.ORDER_ERROR_5103, ResCode.ORDER_ERROR_5103_DESC);
        ReplenishRecordE replenishRecordE = replenishRecordConvert.convertDTO2E(frontRecord);
        replenishRecordE.setInRealWarehouseId(inRealWarehouse.getId());
        replenishRecordE.setInFactoryCode(inRealWarehouse.getFactoryCode());
        replenishRecordE.setInRealWarehouseCode(inRealWarehouse.getRealWarehouseOutCode());
        replenishRecordE.setOutRealWarehouseId(outWarehouse.getId());
        replenishRecordE.setOutFactoryCode(outWarehouse.getFactoryCode());
        replenishRecordE.setOutRealWarehouseCode(outWarehouse.getRealWarehouseOutCode());
        replenishRecordE.setFactoryCode(outWarehouse.getFactoryCode());
        //3.插入前置单
        replenishRecordE.setIsNeedDispatch(ShopReplenishConstant.INIT_DISPATCH);
        replenishRecordE.setIsNeedAllot(ShopReplenishConstant.NOT_NEED_ALLOT);
        this.addJoinRecord(replenishRecordE);
        //4.构造出入库单数据
        WarehouseRecordE outRecordE = this.initRWOutRecord(replenishRecordE);
        outRecordE.setSyncDispatchStatus(WarehouseRecordConstant.NEED_DISPATCH);
        outRecordE.setSyncTradeStatus(WarehouseRecordConstant.INIT_SYNC_TRADE);
        //额度确认后改为待同步派车系统
        outRecordE.setSyncTmsbStatus(WarehouseRecordConstant.INIT_SYNC_TMSB);
        //4.1 2019-9-19 主配明细重新设置
        if(isPrimaryServer){
            this.resetDetailListByRwStock(replenishRecordE, outRecordE);
        }
        //5.保存出入库单据及明细
        this.saveWarehouseRecord(outRecordE, replenishRecordE);
        //6.根据出库单锁定库存
        OutWarehouseRecordDTO outRecord = stockRecordDTOConvert.convertE2OutDTO(outRecordE);
        stockRecordFacade.createOutRecord(outRecord);
        return outWarehouse;
    }

    /**
     * @Description: 保存出入库单及明细 <br>
     *
     * @Author chuwenchao 2020/6/13
     * @param outRecordE
     * @param replenishRecordE
     * @return
     */
    private void saveWarehouseRecord(WarehouseRecordE outRecordE, ReplenishRecordE replenishRecordE) {
        warehouseRecordMapper.insertWarehouseRecord(outRecordE);
        List<WarehouseRecordDetailE> detailList = outRecordE.getWarehouseRecordDetailList();
        detailList.forEach(r -> {
            r.setRecordCode(outRecordE.getRecordCode());
            r.setWarehouseRecordId(outRecordE.getId());
        });
        warehouseRecordDetailMapper.insertWarehouseRecordDetails(detailList);
        FrontWarehouseRecordRelationE relation = new FrontWarehouseRecordRelationE();
        relation.setWarehouseRecordId(outRecordE.getId());
        relation.setFrontRecordId(replenishRecordE.getId());
        relation.setFrontRecordType(replenishRecordE.getRecordType());
        relation.setRecordCode(outRecordE.getRecordCode());
        relation.setFrontRecordCode(replenishRecordE.getRecordCode());
        frontWarehouseRecordRelationMapper.insertFrontWarehouseRecordRelation(relation);
    }

    /**
     * @Description: 加盟普通叫货 <br>
     *
     * @Author chuwenchao 2020/6/12
     * @param frontRecord
     * @return 
     */
    private RealWarehouse createJoinOrder(ShopReplenishDTO frontRecord, boolean isPrimaryServer) {
        //根据门店编码查询门店仓实仓信息
        RealWarehouse inRealWarehouse = stockRealWarehouseFacade.queryRealWarehouseByShopCode(frontRecord.getShopCode());
        AlikAssert.isNotNull(inRealWarehouse, ResCode.ORDER_ERROR_5102, ResCode.ORDER_ERROR_5102_DESC);
        //根据渠道查询虚仓
        List<VirtualWarehouse> vwList = stockRealWarehouseFacade.getVwListByChannelCode(frontRecord.getChannelCode());
        AlikAssert.isNotEmpty(vwList, ResCode.ORDER_ERROR_1002, "未找到单据"+ frontRecord.getOutRecordCode() +"渠道关联虚仓"+frontRecord.getChannelCode());
        //只考虑一个虚仓的场景（做跨工厂共享库存需修改）
        VirtualWarehouse virtualWarehouse = vwList.get(0);
        RealWarehouse realWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(virtualWarehouse.getRealWarehouseOutCode(), virtualWarehouse.getFactoryCode());
        AlikAssert.isNotNull(realWarehouse, ResCode.ORDER_ERROR_5103, ResCode.ORDER_ERROR_5103_DESC);
        //构造门店补货前置单
        ReplenishRecordE replenishRecordE = replenishRecordConvert.convertDTO2E(frontRecord);
        replenishRecordE.setInRealWarehouseId(inRealWarehouse.getId());
        replenishRecordE.setInFactoryCode(inRealWarehouse.getFactoryCode());
        replenishRecordE.setInRealWarehouseCode(inRealWarehouse.getRealWarehouseOutCode());
        replenishRecordE.setOutRealWarehouseId(realWarehouse.getId());
        replenishRecordE.setOutFactoryCode(realWarehouse.getFactoryCode());
        replenishRecordE.setOutRealWarehouseCode(realWarehouse.getRealWarehouseOutCode());
        //插入前置单据
        replenishRecordE.setIsNeedDispatch(ShopReplenishConstant.INIT_DISPATCH);
        replenishRecordE.setIsNeedAllot(ShopReplenishConstant.NOT_NEED_ALLOT);
        this.addJoinRecord(replenishRecordE);
        //构造出入库单据
        WarehouseRecordE outRecordE = this.initRWOutRecord(replenishRecordE);
        outRecordE.setSyncDispatchStatus(WarehouseRecordConstant.NEED_DISPATCH);
        outRecordE.setSyncTradeStatus(WarehouseRecordConstant.INIT_SYNC_TRADE);
        //额度确认后改为待同步派车系统
        outRecordE.setSyncTmsbStatus(WarehouseRecordConstant.INIT_SYNC_TMSB);
        outRecordE.setVirtualWarehouseCode(virtualWarehouse.getVirtualWarehouseCode());
        // 2019-9-19 主配明细重新设置
        if(isPrimaryServer) {
            //根据渠道直接查出仓库
            outRecordE.setVirtualWarehouseCode(virtualWarehouse.getVirtualWarehouseCode());
            this.resetDetailListByVwStock(virtualWarehouse, replenishRecordE, outRecordE);
        }
        //保存出库单及明细
        this.saveWarehouseRecord(outRecordE, replenishRecordE);
        //根据出库单锁定库存
        OutWarehouseRecordDTO outDTO = stockRecordDTOConvert.convertE2OutDTO(outRecordE);
        stockRecordFacade.createOutRecord(outDTO);
        return realWarehouse;
    }

    /**
     * @Description: 明细重新设置, 根据虚仓库存<br>
     *
     * @Author chuwenchao 2019/9/3
     * @param virtualWarehouse
     * @param replenishRecordE
     * @param outRecordE
     * @return
     */
    private void resetDetailListByVwStock(VirtualWarehouse virtualWarehouse, ReplenishRecordE replenishRecordE, WarehouseRecordE outRecordE) {
        //查询明细虚仓库存
        List<SkuInfoForVw> skuInfoForVwList = new ArrayList<>();
        for(WarehouseRecordDetailE detail: outRecordE.getWarehouseRecordDetailList()) {
            SkuInfoForVw skuInfoForVw = new SkuInfoForVw();
            skuInfoForVw.setVirtualWarehouseCode(virtualWarehouse.getVirtualWarehouseCode());
            skuInfoForVw.setSkuCode(detail.getSkuCode());
            skuInfoForVw.setUnitCode(detail.getUnitCode());
            skuInfoForVwList.add(skuInfoForVw);
        }
        List<SkuStockForVw> skuStockForVwList = stockQueryFacade.queryVmListByVmGroupCodes(skuInfoForVwList);
        Map<String, BigDecimal> rwStockMap = skuStockForVwList.stream().collect(Collectors.toMap(SkuStockForVw::getSkuCode, SkuStockForVw::getAvailableQty));
        // 重新设置明细
        List<WarehouseRecordDetailE> newDetail = new ArrayList<>();
        for (WarehouseRecordDetailE detail: outRecordE.getWarehouseRecordDetailList()) {
            // 数量为 0 的过滤
            if(BigDecimal.ZERO.compareTo(detail.getPlanQty()) >= 0){
                log.info("门店普通补货单【{}】skuCode【{}】补货计划数量小于等于0 跳过", replenishRecordE.getOutRecordCode(), detail.getSkuCode());
                continue;
            }
            // 库存为0跳过
            BigDecimal availableQty  = rwStockMap.get(detail.getSkuCode());
            BigDecimal minQty = this.findMinQty(detail.getPlanQty(), availableQty);
            if(BigDecimal.ZERO.compareTo(minQty) >= 0) {
                log.info("门店普通补货单【{}】虚仓【{}】skuCode【{}】最小库存为0 跳过", replenishRecordE.getOutRecordCode(), virtualWarehouse.getVirtualWarehouseCode(), detail.getSkuCode());
                continue;
            }
            if(detail.getScale() == null) {
                log.error("直营补货单【{}】商品单位异常，明细 ==> {}", replenishRecordE.getOutRecordCode(), JSONObject.toJSONString(detail));
                throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + "商品单位异常");
            }
            // 单位取整
            if(BigDecimal.ONE.compareTo(detail.getScale()) != 0) {
                // 取整为0 跳过
                BigDecimal integerNum = minQty.divideToIntegralValue(detail.getScale());
                if(integerNum.compareTo(BigDecimal.ZERO) <= 0) {
                    log.info("门店普通补货单【{}】skuCode【{}】补货计划数量取整后小于等于0 跳过", replenishRecordE.getOutRecordCode(), detail.getSkuCode());
                    continue;
                } else {
                    minQty = integerNum.multiply(detail.getScale());
                }
            }
            WarehouseRecordDetailE temp = new WarehouseRecordDetailE();
            BeanUtils.copyProperties(detail, temp);
            //设置为分配数量
            temp.setPlanQty(minQty);
            temp.setActualQty(BigDecimal.ZERO);
            newDetail.add(temp);
            //更新map中的库存数量
            rwStockMap.put(detail.getSkuCode(), availableQty.subtract(minQty));
        }
        AlikAssert.isNotEmpty(newDetail, ResCode.ORDER_ERROR_5104, ResCode.ORDER_ERROR_5104_DESC + "单号：" + replenishRecordE.getOutRecordCode());
        outRecordE.setWarehouseRecordDetailList(newDetail);
    }

    /**
     * @Description: 预期数量与实际数量取小 <br>
     *
     * @Author chuwenchao 2019/9/3
     * @param planQty
     * @param realQty
     * @return
     */
    private BigDecimal findMinQty(BigDecimal planQty, BigDecimal realQty) {
        realQty = realQty == null ? BigDecimal.ZERO : realQty;
        if(realQty.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        if(planQty.compareTo(realQty) > 0) {
            return realQty;
        }
        return planQty;
    }

    /**
     * @Description: 加盟门店---大仓出库单 <br>
     *
     * @Author chuwenchao 2020/6/12
     * @param replenishRecordE
     * @return 
     */
    private WarehouseRecordE initRWOutRecord(ReplenishRecordE replenishRecordE) {
        WarehouseRecordE outRecordE = this.initWarehouseRecord(replenishRecordE, WarehouseRecordTypeEnum.LS_REPLENISH_OUT_WAREHOUSE_RECORD);
        List<WarehouseRecordDetailE> warehouseRecordDetailList = this.initWarehouseRecordDetail(replenishRecordE);
        outRecordE.setWarehouseRecordDetailList(warehouseRecordDetailList);
        return outRecordE;
    }
    /**
     * @Description: 初始化后置单明细item <br>
     *
     * @Author chuwenchao 2020/6/12
     * @param replenishRecordE
     * @return
     */
    private List<WarehouseRecordDetailE> initWarehouseRecordDetail(ReplenishRecordE replenishRecordE) {
        List<ReplenishDetailE> replenishDetailEList = replenishRecordE.getFrontRecordDetails();
        List<WarehouseRecordDetailE> warehouseRecordDetailList = new ArrayList<>(replenishDetailEList.size());
        for(ReplenishDetailE detailE : replenishDetailEList){
            WarehouseRecordDetailE warehouseRecordDetail = new WarehouseRecordDetailE();
            warehouseRecordDetail.setSkuId(detailE.getSkuId());
            warehouseRecordDetail.setSkuCode(detailE.getSkuCode());
            warehouseRecordDetail.setPlanQty(detailE.getBasicSkuQty());
            warehouseRecordDetail.setActualQty(BigDecimal.ZERO);
            warehouseRecordDetail.setUnit(detailE.getBasicUnit());
            warehouseRecordDetail.setUnitCode(detailE.getBasicUnitCode());
            warehouseRecordDetail.setScale(detailE.getScale());
            warehouseRecordDetail.setSapPoNo(replenishRecordE.getSapPoNo());
            warehouseRecordDetail.setDeliveryLineNo(detailE.getId().toString());
            warehouseRecordDetail.setLineNo(detailE.getLineNo());
            warehouseRecordDetailList.add(warehouseRecordDetail);
        }
        return warehouseRecordDetailList;
    }

    /**
     * @Description: 初始化后置单Header <br>
     *
     * @Author chuwenchao 2020/6/12
     * @param replenishRecordE
     * @param recordTypeEnum
     * @return 
     */
    private WarehouseRecordE initWarehouseRecord(ReplenishRecordE replenishRecordE, WarehouseRecordTypeEnum recordTypeEnum) {
        WarehouseRecordE outRecordE = new WarehouseRecordE();
        String code = orderUtilService.queryOrderCode(recordTypeEnum.getCode());
        outRecordE.setRecordCode(code);
        outRecordE.setSapOrderCode(replenishRecordE.getSapPoNo());
        outRecordE.setRecordType(recordTypeEnum.getType());
        outRecordE.setRealWarehouseId(replenishRecordE.getOutRealWarehouseId());
        outRecordE.setFactoryCode(replenishRecordE.getOutFactoryCode());
        outRecordE.setRealWarehouseCode(replenishRecordE.getOutRealWarehouseCode());
        outRecordE.setBusinessType(WarehouseRecordBusinessTypeEnum.OUT_WAREHOUSE_RECORD.getType());
        outRecordE.setRecordStatus(WarehouseRecordStatusEnum.INIT.getStatus());
        outRecordE.setChannelCode(replenishRecordE.getChannelCode());
        outRecordE.setOutCreateTime(replenishRecordE.getOutCreateTime());
        return outRecordE;
    }

    /**
     * @Description: 创建加盟前置单据 <br>
     *
     * @Author chuwenchao 2020/6/12
     * @param replenishRecordE
     * @return 
     */
    private void addJoinRecord(ReplenishRecordE replenishRecordE) {
        replenishRecordE.setRecordStatus(FrontRecordStatusEnum.INIT.getStatus());
        replenishRecordE.setRecordType(FrontRecordTypeEnum.JOIN_SHOP_REPLENISHMENT_RECORD.getType());
        this.saveFrontRecord(replenishRecordE, FrontRecordTypeEnum.JOIN_SHOP_REPLENISHMENT_RECORD.getCode());
    }

    /**
     * @Description: 保存前置单及明细 <br>
     *
     * @Author chuwenchao 2020/6/12
     * @param replenishRecordE
     * @param frontCode
     * @return 
     */
    private void saveFrontRecord(ReplenishRecordE replenishRecordE, String frontCode) {
        String code = orderUtilService.queryOrderCode(frontCode);
        replenishRecordE.setRecordCode(code);
        List<ReplenishDetailE> replenishDetailEList = replenishRecordE.getFrontRecordDetails();
        //设置商品code或id
        itemInfoTool.convertSkuCode(replenishDetailEList);
        //单位转换
        skuQtyUnitTool.convertRealToBasic(replenishDetailEList);
        //保存前置单及明细
        replenishRecordMapper.insertReplenishRecord(replenishRecordE);
        for (ReplenishDetailE detailE : replenishDetailEList) {
            detailE.setRecordCode(replenishRecordE.getRecordCode());
            detailE.setFrontRecordId(replenishRecordE.getId());
            detailE.setLeftBasicSkuQty(detailE.getBasicSkuQty());
            detailE.setIsDeleted(0);
            detailE.setSkuScale(detailE.getScale());
            replenishRecordDetailMapper.insertDetail(detailE);
        }
    }

    /**
     * @Description: 直营指定仓库叫货 <br>
     *
     * @Author chuwenchao 2020/6/12
     * @param frontRecord
     * @return 
     */
    private void createDirectOrderAssignWh(ShopReplenishDTO frontRecord) {
        //1.查询门店code对应的仓库
        RealWarehouse rw = stockRealWarehouseFacade.queryRealWarehouseByShopCode(frontRecord.getShopCode());
        AlikAssert.isNotNull(rw, ResCode.ORDER_ERROR_5102, ResCode.ORDER_ERROR_5102_DESC);
        //2.查询门店的出库仓库
        RealWarehouse outWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(frontRecord.getWarehouseCode(), frontRecord.getFactoryCode());
        AlikAssert.isNotNull(outWarehouse, ResCode.ORDER_ERROR_5103, ResCode.ORDER_ERROR_5103_DESC);
        //3.构造前置单数据
        ReplenishRecordE replenishRecordE = replenishRecordConvert.convertDTO2E(frontRecord);
        replenishRecordE.setInRealWarehouseId(rw.getId());
        replenishRecordE.setInFactoryCode(rw.getFactoryCode());
        replenishRecordE.setInRealWarehouseCode(rw.getRealWarehouseOutCode());
        replenishRecordE.setOutRealWarehouseId(outWarehouse.getId());
        replenishRecordE.setOutFactoryCode(outWarehouse.getFactoryCode());
        replenishRecordE.setOutRealWarehouseCode(outWarehouse.getRealWarehouseOutCode());
        replenishRecordE.setFactoryCode(outWarehouse.getFactoryCode());
        //4.创建门店补货出库前置单
        replenishRecordE.setIsNeedDispatch(ShopReplenishConstant.INIT_DISPATCH);
        replenishRecordE.setIsNeedAllot(ShopReplenishConstant.NOT_NEED_ALLOT);
        this.addDirectRecord(replenishRecordE);
        //5.指定仓库补货,直接创建大仓出库单
        WarehouseRecordE outRecordE = this.dsOutRecordAssignWh(replenishRecordE);
        outRecordE.setSyncTradeStatus(WarehouseRecordConstant.INIT_SYNC_TRADE);
        outRecordE.setSyncDispatchStatus(WarehouseRecordConstant.NEED_DISPATCH);
        outRecordE.setSyncTmsbStatus(WarehouseRecordConstant.NEED_SYNC_TMSB);
        // 5.1 2019-9-3 明细重新设置
        this.resetDetailListByRwStock(replenishRecordE, outRecordE);
        log.info("直营指定仓叫货根据实际库存数量叫货，单据【{}】明细 ==> {}", frontRecord.getOutRecordCode(), JSONObject.toJSONString(outRecordE.getWarehouseRecordDetailList()));
        // 5.2 保存出入库单据及明细
        this.saveWarehouseRecord(outRecordE, replenishRecordE);
        //6. 锁定库存
        OutWarehouseRecordDTO outRecord = stockRecordDTOConvert.convertE2OutDTO(outRecordE);
        stockRecordFacade.createOutRecord(outRecord);
    }

    /**
     * @Description: 明细重新设置,根据实仓库存 <br>
     *
     * @Author chuwenchao 2019/9/3
     * @param replenishRecordE
     * @param outRecordE
     * @return
     */
    private void resetDetailListByRwStock(ReplenishRecordE replenishRecordE, WarehouseRecordE outRecordE) {
        //查询明细实仓库存
        QueryRealStockDTO realStock = new QueryRealStockDTO();
        realStock.setFactoryCode(outRecordE.getFactoryCode());
        realStock.setWarehouseOutCode(outRecordE.getRealWarehouseCode());
        List<BaseSkuInfoDTO> baseSkuInfoDTOS = new ArrayList<>();
        for(WarehouseRecordDetailE detail: outRecordE.getWarehouseRecordDetailList()) {
            BaseSkuInfoDTO skuInfoDTO = new BaseSkuInfoDTO();
            skuInfoDTO.setSkuCode(detail.getSkuCode());
            skuInfoDTO.setUnitCode(detail.getUnitCode());
            baseSkuInfoDTOS.add(skuInfoDTO);
        }
        realStock.setBaseSkuInfoDTOS(baseSkuInfoDTOS);
        List<SkuStockDTO> skuStockDTOList = stockQueryFacade.queryRealStockBySkuInfo(realStock);
        Map<String, BigDecimal> rwStockMap = skuStockDTOList.stream().collect(Collectors.toMap(SkuStockDTO::getSkuCode, SkuStockDTO::getAvailableQty, (v1, v2) -> v1));
        // 重新设置明细
        List<WarehouseRecordDetailE> newDetail = new ArrayList<>();
        for (WarehouseRecordDetailE detail: outRecordE.getWarehouseRecordDetailList()) {
            // 数量为 0 的过滤
            if(BigDecimal.ZERO.compareTo(detail.getPlanQty()) >= 0){
                log.info("门店补货单【{}】skuCode【{}】补货计划数量小于等于0 跳过", replenishRecordE.getOutRecordCode(), detail.getSkuCode());
                continue;
            }
            // 库存为0跳过
            BigDecimal availableQty  = rwStockMap.get(detail.getSkuCode());
            BigDecimal minQty = this.findMinQty(detail.getPlanQty(), availableQty);
            if(BigDecimal.ZERO.compareTo(minQty) >= 0) {
                log.info("门店补货单【{}】实仓【{}】skuCode【{}】最小库存为0 跳过", replenishRecordE.getOutRecordCode(), replenishRecordE.getOutRealWarehouseId(), detail.getSkuCode());
                continue;
            }
            if(detail.getScale() == null) {
                log.error("门店补货单【{}】商品单位异常，明细 ==> {}", replenishRecordE.getOutRecordCode(), JSONObject.toJSONString(detail));
                throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + "商品单位异常");
            }
            // 单位取整
            if(BigDecimal.ONE.compareTo(detail.getScale()) != 0) {
                // 取整为0 跳过
                BigDecimal integerNum = minQty.divideToIntegralValue(detail.getScale());
                if(integerNum.compareTo(BigDecimal.ZERO) <= 0) {
                    log.info("门店补货单【{}】skuCode【{}】补货计划数量取整后小于等于0 跳过", replenishRecordE.getOutRecordCode(), detail.getSkuCode());
                    continue;
                } else {
                    minQty = integerNum.multiply(detail.getScale());
                }
            }
            WarehouseRecordDetailE temp = new WarehouseRecordDetailE();
            BeanUtils.copyProperties(detail, temp);
            temp.setPlanQty(minQty);
            temp.setActualQty(BigDecimal.ZERO);
            newDetail.add(temp);
            //更新map中的库存数量
            rwStockMap.put(detail.getSkuCode(), availableQty.subtract(minQty));
        }
        AlikAssert.isNotEmpty(newDetail, ResCode.ORDER_ERROR_5104, ResCode.ORDER_ERROR_5104_DESC + "单号：" + replenishRecordE.getOutRecordCode());
        outRecordE.setWarehouseRecordDetailList(newDetail);
    }

    /**
       * @Description: 直营门店指定仓库补货创建大仓出库单 <br>
       *
       * @Author chuwenchao 2020/6/12
       * @param replenishRecordE
       * @return
       */
    public WarehouseRecordE dsOutRecordAssignWh(ReplenishRecordE replenishRecordE){
        WarehouseRecordE outRecordE = null;
        //直营设置为直营大仓出库单
        outRecordE = this.initWarehouseRecord(replenishRecordE, WarehouseRecordTypeEnum.DS_REPLENISH_OUT_WAREHOUSE_RECORD);
        //设置明细
        List<WarehouseRecordDetailE> warehouseRecordDetailList = this.initWarehouseRecordDetail(replenishRecordE);
        outRecordE.setWarehouseRecordDetailList(warehouseRecordDetailList);
        return outRecordE;
    }

    /**
     * @Description: 创建直营门店补货单 <br>
     *
     * @Author chuwenchao 2020/6/12
     * @param frontRecord
     * @return 
     */
    private void createDirectOrder(ShopReplenishDTO frontRecord) {
        //直营普通补货校验工厂
        String factoryCode = frontRecord.getFactoryCode();
        if(StringUtils.isBlank(factoryCode)) {
            //直营普通叫货工厂代码必须传
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC+": 直营普通叫货必须要有工厂代码");
        }
        //渠道查虚仓列表
        List<VirtualWarehouse> vwList = stockRealWarehouseFacade.getVwListByChannelCode(frontRecord.getChannelCode());
        List<String> factoryCodes= vwList.stream().map(VirtualWarehouse::getFactoryCode).collect(Collectors.toList());
        if(!factoryCodes.contains(factoryCode)) {
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC+": 直营普通叫货，当前渠道与工厂代码无法对应");
        }
        //1.查询门店code对应的仓库
        RealWarehouse rw = stockRealWarehouseFacade.queryRealWarehouseByShopCode(frontRecord.getShopCode());
        AlikAssert.isNotNull(rw, ResCode.ORDER_ERROR_5102, ResCode.ORDER_ERROR_5102_DESC);
        //2.直营门店只生成前置单即可
        ReplenishRecordE replenishRecordE = replenishRecordConvert.convertDTO2E(frontRecord);
        replenishRecordE.setInRealWarehouseId(rw.getId());
        replenishRecordE.setInFactoryCode(rw.getFactoryCode());
        replenishRecordE.setInRealWarehouseCode(rw.getRealWarehouseOutCode());
        //3.入库在寻源后修改
        replenishRecordE.setOutRealWarehouseId(null);
        //4.创建门店补货出库前置单
        replenishRecordE.setIsNeedDispatch(ShopReplenishConstant.INIT_DISPATCH);
        replenishRecordE.setIsNeedAllot(ShopReplenishConstant.NEED_ALLOT);
        this.addDirectRecord(replenishRecordE);
    }

    /**
     * @Description: 创建直营补货前置单据 <br>
     *
     * @Author chuwenchao 2020/6/12
     * @param replenishRecordE
     * @return 
     */
    private void addDirectRecord(ReplenishRecordE replenishRecordE) {
        replenishRecordE.setRecordStatus(FrontRecordStatusEnum.INIT.getStatus());
        replenishRecordE.setRecordType(FrontRecordTypeEnum.SHOP_REPLENISHMENT_RECORD.getType());
        //获取单据编号
        this.saveFrontRecord(replenishRecordE, FrontRecordTypeEnum.SHOP_REPLENISHMENT_RECORD.getCode());
    }

    /**
     * @Description: 门店补货参数校验 <br>
     *
     * @Author chuwenchao 2020/6/12
     * @param frontRecord
     * @return 
     */
    private void validAddDataInfo(ShopReplenishDTO frontRecord) {
        //叫货类型必须是指定的5种
        if (!ShopReplenishConstant.ALLOW_RECORD_TYPE.contains(frontRecord.getRecordType())) {
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC+": 叫货类型必须是指定的5种");
        }
        //指定仓库叫货的,必须有仓库
        if (ShopReplenishConstant.ASSIGN_WH_REPLENISH.equals(frontRecord.getRequireType())) {
            if (StringUtils.isBlank(frontRecord.getWarehouseCode())) {
                throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC+": 指定仓库叫货的必须有仓库");
            }
        }
        //普通叫货必须要有渠道
        if (ShopReplenishConstant.COMMON_REPLENISH.equals(frontRecord.getRequireType())) {
            if (!(StringUtils.isNotBlank(frontRecord.getChannelCode()) &&  StringUtils.isNotBlank(frontRecord.getParentChannelCode()))) {
                throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC+": 直营和加盟普通叫货必须要有渠道");
            }
            ChannelSales channelSales = stockRealWarehouseFacade.queryByChannelCode(frontRecord.getChannelCode());
            ChannelSales parentChannelSales = stockRealWarehouseFacade.queryByChannelCode(frontRecord.getParentChannelCode());
            //都为空抛异常
            if(channelSales == null && parentChannelSales == null){
                throw new RomeException(ResCode.ORDER_ERROR_5100, ResCode.ORDER_ERROR_5100_DESC);
            }
            if (channelSales != null) {
                //门店渠道不为空使用门店渠道
                frontRecord.setChannelCode(frontRecord.getChannelCode());
            }else {
                //否则使用公司的渠道
                frontRecord.setChannelCode(frontRecord.getParentChannelCode());
            }
        }
        //冷链和直送的叫货店铺必须是直营
        if (ShopReplenishConstant.COLD_CHAIN_REPLENISH.equals(frontRecord.getRecordType()) ||
                ShopReplenishConstant.SUPPLIER_DIRECT_REPLENISH.equals(frontRecord.getRecordType())) {
            if (!ShopReplenishConstant.CAN_CREATE_COLD_CHAIN.contains(frontRecord.getShopType())) {
                throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC+": 冷链和直送的叫货店铺必须是直营");
            }
        }
        //直营的需要sap单号
        if (ShopReplenishConstant.DIRECT_REPLENISH.equals(frontRecord.getRecordType())) {
            if (StringUtils.isBlank(frontRecord.getSapPoNo())) {
                throw new RomeException(ResCode.ORDER_ERROR_1002, "直营补货需要传入sap单号");
            }
        }
        //校验行行号
        for (ShopReplenishDetailDTO shopReplenishDetailDTO : frontRecord.getFrontRecordDetails()) {
            AlikAssert.isNotNull(shopReplenishDetailDTO.getLineNo(), ResCode.ORDER_ERROR_5101, ResCode.ORDER_ERROR_5101_DESC);
        }
    }

    /**
     * @Description: 修改门店补货需求单 <br>
     *
     * @Author chuwenchao 2020/6/10
     * @param frontRecord
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReplenishRecord(ShopReplenishDTO frontRecord) {
        //根据po单据号查询补货单
        ReplenishRecordE recordE = replenishRecordMapper.queryByOutCode(frontRecord.getOutRecordCode());
        AlikAssert.isNotNull(recordE, ResCode.ORDER_ERROR_5106, ResCode.ORDER_ERROR_5106_DESC);
        //校验删除状态
        int i = 0;
        for(ShopReplenishDetailDTO detailDTO : frontRecord.getFrontRecordDetails()){
            AlikAssert.isNotNull(detailDTO.getIsDeleted(), ResCode.ORDER_ERROR_5107 , ResCode.ORDER_ERROR_5107_DESC);
            if(!(detailDTO.getIsDeleted() == 1)){
                i++;
            }
        }
        //取消的直接返回true
        if(FrontRecordStatusEnum.DISABLED.getStatus().equals(recordE.getRecordStatus())){
            return ;
        }
        //3.进行分发到对应的service
        Integer recordType = recordE.getRecordType();
        //3.1 直营叫货
        if (FrontRecordTypeEnum.SHOP_REPLENISHMENT_RECORD.getType().equals(recordType)) {
            //普通叫货
            if (ShopReplenishConstant.COMMON_REPLENISH.equals(recordE.getRequireType())) {
                List<Long> whIdList = frontWarehouseRecordRelationMapper.queryWarehouseIdByFrontId(recordE.getId(), recordType);
                if(CollectionUtils.isNotEmpty(whIdList)){
                    //寻源分配的无法修改
                    throw new RomeException(ResCode.ORDER_ERROR_5108, ResCode.ORDER_ERROR_5108_DESC);
                }else{
                    if(i > 0){
                        this.cancelAndUpdateOrderInfo(frontRecord, recordE);
                    }else{
                        //如果全部删除就取消单据
                        this.cancelReplenish(frontRecord.getOutRecordCode(), 0, -1L);
                    }
                }
            }
            //指定仓库叫货
            if (ShopReplenishConstant.ASSIGN_WH_REPLENISH.equals(recordE.getRequireType())) {
                throw new RomeException(ResCode.ORDER_ERROR_5109, ResCode.ORDER_ERROR_5109_DESC );
            }
        }
        //3.3 加盟 加盟商和冷链直送及加盟托管暂不支持修改
        if (FrontRecordTypeEnum.JOIN_SHOP_REPLENISHMENT_RECORD.getType().equals(recordType)
                || FrontRecordTypeEnum.SHOP_COLD_CHAIN_DELIVERY_RECORD.getType().equals(recordType)
                || FrontRecordTypeEnum.SHOP_SUPPLIER_DIRECT_DELIVERY_RECORD.getType().equals(recordType)) {
            throw new RomeException(ResCode.ORDER_ERROR_5109, ResCode.ORDER_ERROR_5109_DESC);
        }
    }

    /**
     * @Description: 删除原有明细重新生成 <br>
     *
     * @Author chuwenchao 2020/6/13
     * @param replenishDTO
     * @return 
     */
    private void cancelAndUpdateOrderInfo(ShopReplenishDTO replenishDTO, ReplenishRecordE recordE) {
        //更新前置单明细
        ReplenishRecordE frontRecord = replenishRecordConvert.convertDTO2E(replenishDTO);
        log.info("主内容" + JSON.toJSONString(recordE));
        //先删除已经生成明细
        replenishRecordDetailMapper.deleteDetailByFrontId(recordE.getId());
        List<ReplenishDetailE> detailEList = frontRecord.getFrontRecordDetails();
        //设置商品code或id
        itemInfoTool.convertSkuCode(detailEList);
        //单位转换
        skuQtyUnitTool.convertRealToBasic(detailEList);
        for (ReplenishDetailE detailE : detailEList) {
            detailE.setFrontRecordId(recordE.getId());
            detailE.setRecordCode(recordE.getRecordCode());
            detailE.setLeftBasicSkuQty(detailE.getBasicSkuQty());
            detailE.setSkuScale(detailE.getScale());
            detailE.setSapPoNo(recordE.getSapPoNo());
            replenishRecordDetailMapper.insertDetail(detailE);
        }
    }

    /**
     * @Description: 校验是否可以修改门店补货需求单 <br>
     *
     * @Author chuwenchao 2020/6/10
     * @param poCode
     * @return
     */
    @Override
    public boolean checkUpdateReplenishRecord(String poCode) {
        boolean canUpdate = false;
        //根据po单据号查询补货单,并且没有出库
        ReplenishRecordE recordE = replenishRecordMapper.queryByOutCode(poCode);
        if(recordE != null) {
            //取消的直接返回true
            if(FrontRecordStatusEnum.DISABLED.getStatus().equals(recordE.getRecordStatus())){
                return true;
            }
            //3.进行分发到对应的service
            Integer recordType = recordE.getRecordType();
            //3.1 直营叫货
            if (FrontRecordTypeEnum.SHOP_REPLENISHMENT_RECORD.getType().equals(recordType)) {
                //普通叫货
                if (ShopReplenishConstant.COMMON_REPLENISH.equals(recordE.getRequireType())) {
                    List<Long> whIdList = frontWarehouseRecordRelationMapper.queryWarehouseIdByFrontId(recordE.getId(), recordE.getRecordType());
                    if (CollectionUtils.isNotEmpty(whIdList)) {
                        //寻源分配的无法修改
                        canUpdate = false;
                    } else {
                        canUpdate = true;
                    }
                }
                //指定仓库叫货
                if (ShopReplenishConstant.ASSIGN_WH_REPLENISH.equals(recordE.getRequireType())) {
                    throw new RomeException(ResCode.ORDER_ERROR_5109, ResCode.ORDER_ERROR_5109_DESC);
                }
            }
            //3.3 加盟 加盟商和冷链直送及加盟托管暂不支持修改
            if (FrontRecordTypeEnum.JOIN_SHOP_REPLENISHMENT_RECORD.getType().equals(recordType)
                    || FrontRecordTypeEnum.SHOP_COLD_CHAIN_DELIVERY_RECORD.getType().equals(recordType)
                    || FrontRecordTypeEnum.SHOP_SUPPLIER_DIRECT_DELIVERY_RECORD.getType().equals(recordType)) {
                canUpdate = false;
            }
        }
        return canUpdate;
    }

    /**
     * @Description: 加盟确认扣减额度成功 <br>
     *
     * @Author chuwenchao 2020/6/10
     * @param poCode
     * @param sapPoNo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmJoinReplenish(String poCode, String sapPoNo) {
        //根据po单据号查询补货单
        ReplenishRecordE recordE = replenishRecordMapper.queryByOutCode(poCode);
        AlikAssert.isNotNull(recordE, ResCode.ORDER_ERROR_5106, ResCode.ORDER_ERROR_5106_DESC);
        //判断是加盟的单据,并且店铺类型也是加盟的
        if(!ShopReplenishConstant.JOIN_SHOP_TYPE.equals(recordE.getShopType())){
            throw new RomeException(ResCode.ORDER_ERROR_5110, ResCode.ORDER_ERROR_5110_DESC);
        }
        //只允许加盟和加盟商
        if (!(FrontRecordTypeEnum.JOIN_SHOP_REPLENISHMENT_RECORD.getType().equals(recordE.getRecordType()))) {
            throw new RomeException(ResCode.ORDER_ERROR_5110, ResCode.ORDER_ERROR_5110_DESC);
        }
        //根据po单据号更新sap单号
        int i = replenishRecordMapper.updateSapPoCode(recordE.getId(), sapPoNo);
        AlikAssert.isTrue(i > 0, ResCode.ORDER_ERROR_5111, ResCode.ORDER_ERROR_5111_DESC);
        List<FrontWarehouseRecordRelationE> relationEList = frontWarehouseRecordRelationMapper.queryRecordRelationByFrontIdAndType(recordE.getId(), recordE.getRecordType());
        //加盟此时只会产生一个出入库单
        FrontWarehouseRecordRelationE relationE = relationEList.get(0);
        //修改出入库同步派车系统状态为待同步
        i = warehouseRecordMapper.updateRecordInfoToSyncTmsb(relationE.getWarehouseRecordId(), sapPoNo);
        AlikAssert.isTrue(i > 0, ResCode.ORDER_ERROR_5111, ResCode.ORDER_ERROR_5111_DESC);
        //修改记录sap单号到明细
        List<WarehouseRecordDetailE> detailList = warehouseRecordDetailMapper.queryListByRecordId(relationE.getWarehouseRecordId());
        detailList.forEach(detail -> detail.setSapPoNo(sapPoNo));
        warehouseRecordDetailMapper.updateDetailSapNo(detailList);
        //修改记录sap单号到前置单明细
        i = replenishRecordDetailMapper.updateDetailSapPoCode(recordE.getId(), sapPoNo);
        AlikAssert.isTrue(i > 0, ResCode.ORDER_ERROR_5112, ResCode.ORDER_ERROR_5112_DESC);
        //同步库存中心
        stockReplenishFacade.confirmJoinReplenish(relationE.getRecordCode(), sapPoNo);
    }

    /**
     * @Description: 取消申请 <br>
     *
     * @Author chuwenchao 2020/6/10
     * @param poCode
     * @param isForceCancel
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelReplenish(String poCode, Integer isForceCancel, Long userId) {
        //根据po单据号查询补货单,并且没有出库
        ReplenishRecordE recordE = replenishRecordMapper.queryByOutCode(poCode);
        AlikAssert.isNotNull(recordE, ResCode.ORDER_ERROR_5106, ResCode.ORDER_ERROR_5106_DESC);
        if(FrontRecordStatusEnum.DISABLED.getStatus().equals(recordE.getRecordStatus())){
            return;
        }
        if (!(FrontRecordStatusEnum.INIT.getStatus().equals(recordE.getRecordStatus())
                || FrontRecordStatusEnum.TMS.getStatus().equals(recordE.getRecordStatus()))) {
            AlikAssert.isTrue(false, ResCode.ORDER_ERROR_5113, ResCode.ORDER_ERROR_5113_DESC);
        }
        //取消前置单
        int i = replenishRecordMapper.updateToCancel(Arrays.asList(recordE.getId()));
        AlikAssert.isTrue(i > 0, ResCode.ORDER_ERROR_5113, ResCode.ORDER_ERROR_5113_DESC);
        //查询所有大仓出入库单
        List<Long> whIdList = frontWarehouseRecordRelationMapper.queryWarehouseIdByFrontId(recordE.getId(), recordE.getRecordType());
        if (CollectionUtils.isNotEmpty(whIdList)) {
            //查询后置单据及明细
            List<WarehouseRecordE> recordEList = warehouseRecordMapper.queryWarehouseRecordByIds(whIdList);
            List<WarehouseRecordDetailE> recordDetailList = warehouseRecordDetailMapper.queryListByRecordIds(whIdList);
            Map<Long, List<WarehouseRecordDetailE>> detailMap = recordDetailList.stream().collect(Collectors.groupingBy(WarehouseRecordDetailE::getWarehouseRecordId));
            //如果有一条为下发就直接return
            for (WarehouseRecordE record : recordEList) {
                if (isForceCancel == 0 && WmsSyncStatusEnum.SYNCHRONIZED.getStatus().equals(record.getSyncWmsStatus())) {
                    throw new RomeException(ResCode.ORDER_ERROR_5114, ResCode.ORDER_ERROR_5114_DESC);
                }
                List<FrontWarehouseRecordRelationE> relationList = frontWarehouseRecordRelationMapper.getByWrId(record.getId());
                List<Long> ids = relationList.stream().map(FrontWarehouseRecordRelationE::getFrontRecordId).distinct().collect(Collectors.toList());
                //取消合单的前置单
                if(ids.size() > 1){
                    replenishRecordMapper.updateToCancel(ids);
                }
                //设置明细
                if (detailMap.containsKey(record.getId())) {
                    record.setWarehouseRecordDetailList(detailMap.get(record.getId()));
                }
            }
            //取消出库单并解冻库存
            for(WarehouseRecordE whRecordE : recordEList) {
                //入库单直接return
                if(WarehouseRecordBusinessTypeEnum.IN_WAREHOUSE_RECORD.getType().equals(whRecordE.getBusinessType())){
                    continue;
                }
                //直送的直接return
                if(WarehouseRecordTypeEnum.WH_CHAIN_DIRECT_OUT_RECORD.getType().equals(whRecordE.getRecordType())){
                    continue;
                }
                //取消后置单
                int j = warehouseRecordMapper.updateReplenishToCanceled(whRecordE.getId(), isForceCancel, userId);
                //通知库存取消单据
                CancelRecordDTO cancelRecordDTO = new CancelRecordDTO();
                cancelRecordDTO.setIsForceCancel(isForceCancel);
                cancelRecordDTO.setRecordCode(whRecordE.getRecordCode());
                cancelRecordDTO.setRecordType(whRecordE.getRecordType());
                CancelResultDTO cancelResultDTO = stockRecordFacade.cancelSingleRecord(cancelRecordDTO);
                AlikAssert.isTrue(cancelResultDTO.getStatus(), ResCode.ORDER_ERROR_5115, ResCode.ORDER_ERROR_5115_DESC);
            }
        }
    }

    /**
     * @Description: 直营门店寻源并生成出库单 <br>
     *
     * @Author chuwenchao 2020/6/15
     * @param allotDTO
     * @return
     */
    @Override
    public Integer allotShopReplenish(ShopReplenishAllotDTO allotDTO) {
        //1.先按照门店配送仓库进行分组(多线程)
        Integer times = null;
        boolean isLock = false;
        List<String> poList = allotDTO.getPoList();
        String factoryCode = allotDTO.getFactoryCode();
        String channelCode = allotDTO.getChannelCode();
        if(StringUtils.isNotBlank(channelCode)){
            //苏南寻源也要传入个仓库
            AlikAssert.isNotBlank(factoryCode, ResCode.ORDER_ERROR_5116, ResCode.ORDER_ERROR_5116_DESC);
        }
        try {
            isLock = redisUtil.lock(ALLOT_REDIS_KEY, "1", REDIS_LAST_TIME);
            if (!isLock) {
                throw new RomeException(ResCode.ORDER_ERROR_5117, ResCode.ORDER_ERROR_5117_DESC);
            }
            String value = (String) redisUtil.get(ALLOT_TASK_KEY);
            if(StringUtils.isNotBlank(value)){
                times = Integer.valueOf(value);
            }else{
                times  = 0;
            }
            //2.查询类型为直营状态为初始化的前置单据
            List<ReplenishRecordE> allOrderList = this.getAllotOrderList(allotDTO);
            //3.休眠2秒钟
            Thread.sleep(2000);

            AllotTypeEnum allotType;
            if(StringUtils.isNotBlank(factoryCode)) {
                allotType = AllotTypeEnum.BY_FACTORY_CODE;
            } else if(poList != null && !poList.isEmpty()) {
                allotType = AllotTypeEnum.BY_ORDER;
            } else {
                allotType = AllotTypeEnum.BY_DATE;
            }
            times = times + 1;
            //执行完毕后在释放锁
            allotShopReplenishTask.executeAsyncTask(allOrderList, ALLOT_REDIS_KEY, allotType, times, allotDTO);
            redisUtil.set(ALLOT_TASK_KEY, String.valueOf(times) , this.getRemainSecondsOneDay(new Date()));
        } catch (RomeException e) {
            log.error("执行寻源任务失败", e);
            throw new RomeException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("执行寻源任务异常", e);
            throw new RomeException(ResCode.ORDER_ERROR_5118, ResCode.ORDER_ERROR_5118_DESC);
        }
        return times;
    }

    /**
     * 查询寻源日志
     * @param condition
     * @return
     */
    @Override
    public PageInfo<ReplenishAllotLogDTO> queryReplenishAllotLogDTO(ReplenishAllotLogDTO condition) {
        Page page = PageHelper.startPage(condition.getPageIndex(), condition.getPageSize());
        if(StringUtils.isNotBlank(condition.getAllotArea()) && condition.getAllotArea().contains("-")){
            condition.setChannelCode(condition.getAllotArea().split("-")[0]);
            condition.setFactoryCode(condition.getAllotArea().split("-")[1]);
        }else if(StringUtils.isNotBlank(condition.getAllotArea())){
            condition.setFactoryCode(condition.getAllotArea());
            condition.setChannelCode("");
        }
        List<ReplenishAllotLogE> logDoList = replenishAllotLogMapper.queryAllotLogCondition(condition);
        List<ReplenishAllotLogDTO> dtoList = new ArrayList<>(logDoList.size());
        for (ReplenishAllotLogE repE:logDoList) {
            ReplenishAllotLogDTO reDto=replenishAllotLogConvertor.covertE2DTO(repE);
            dtoList.add(reDto);
        }
        for(ReplenishAllotLogDTO dto: dtoList) {
            AllotTypeEnum allotTypeEnum = AllotTypeEnum.findByType(dto.getAllotType());
            if(allotTypeEnum != null) {
                dto.setAllotTypeName(allotTypeEnum.getDesc());
            }
        }
        PageInfo<ReplenishAllotLogDTO> pageInfo = new PageInfo<>(dtoList);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    /**
     *寻源结果报表
     * @param condition
     * @return
     */
    @Override
    public PageInfo<ShopReplenishReportDetailDTO> queryReplenishReportDTO(ShopReplenishReportDTO condition) {
        Date startTime = condition.getStartTime();
        Date endTime = condition.getEndTime();
        if(startTime == null && endTime == null) {
            Date date = DateUtils.addDays(new Date(), replenishReportAllotValue * -1);
            startTime = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
            condition.setStartTime(startTime);
        }

        //根据仓库编号查询
        String realWarehouseCode = condition.getRealWarehouseCode();
        if(StringUtils.isNotBlank(realWarehouseCode)) {
            //根据仓库编号查询实仓信息
            Long rwId = stockRealWarehouseFacade.queryRealWarehouseByRWCode(realWarehouseCode).getId();
            if(rwId == null) {
                return new PageInfo<>();
            } else {
                condition.setOutRWId(rwId);
            }
        }

        //根据门店编码查询
        String shopCode = condition.getShopCode();
        if(StringUtils.isNotBlank(shopCode)) {
            //根据门店编码查询实仓id
            Long rwId=stockRealWarehouseFacade.queryRealWarehouseByShopCode(shopCode).getId();
            if(rwId == null) {
                return new PageInfo<>();
            } else {
                condition.setInRWId(rwId);
            }
        }

        Page page = PageHelper.startPage(condition.getPageIndex(), condition.getPageSize());
        List<ReplenishRecordE> list = replenishRecordDetailMapper.queryReplenishReportCondition(condition);
        if(CollectionUtils.isEmpty(list)) {
            PageInfo<ShopReplenishReportDetailDTO> pageInfo = new PageInfo<>();
            pageInfo.setTotal(page.getTotal());
            return pageInfo;
        }

        List<QueryRealWarehouseDTO> queryRealWhDTOS= new ArrayList<>();
        for (ReplenishRecordE repDetail: list) {
            QueryRealWarehouseDTO outQueryRealWDTO= new QueryRealWarehouseDTO();

            outQueryRealWDTO.setFactoryCode(repDetail.getOutFactoryCode());
            outQueryRealWDTO.setWarehouseOutCode(repDetail.getOutRealWarehouseCode());
            queryRealWhDTOS.add(outQueryRealWDTO);

            QueryRealWarehouseDTO inQueryRealWDTO= new QueryRealWarehouseDTO();
            inQueryRealWDTO.setFactoryCode(repDetail.getInFactoryCode());
            inQueryRealWDTO.setWarehouseOutCode(repDetail.getInRealWarehouseCode());
            queryRealWhDTOS.add(inQueryRealWDTO);
        }


        if(CollectionUtils.isNotEmpty(queryRealWhDTOS)) {
            //获取实仓信息
            List<RealWarehouse> realWarehouseEList =stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(queryRealWhDTOS);
            Map<Long, RealWarehouse> realWarehouseEMap = realWarehouseEList.stream().collect(Collectors.toMap(RealWarehouse :: getId, Function.identity(), (v1, v2) -> v1));
            list.forEach(item -> {
                if (realWarehouseEMap.containsKey(item.getOutRealWarehouseId())) {
                    item.setOutRealWarehoseCode(realWarehouseEMap.get(item.getOutRealWarehouseId()).getRealWarehouseCode());
                    item.setOutRealWarehouseName(realWarehouseEMap.get(item.getOutRealWarehouseId()).getRealWarehouseName());
                }
            });
        }

        if(CollectionUtils.isNotEmpty(queryRealWhDTOS)) {
            //获取实仓信息
            List<RealWarehouse> realWarehouseEList =stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(queryRealWhDTOS);
            Map<Long, RealWarehouse> realWarehouseEMap = realWarehouseEList.stream().collect(Collectors.toMap(RealWarehouse :: getId, Function.identity(), (v1, v2) -> v1));
            list.forEach(item -> {
                if (realWarehouseEMap.containsKey(item.getInRealWarehouseId())) {
                    item.setInShopCode(realWarehouseEMap.get(item.getInRealWarehouseId()).getShopCode());
                    item.setInRealWarehouseName(realWarehouseEMap.get(item.getInRealWarehouseId()).getRealWarehouseName());
                }
            });
        }

        //前置单CODE集合
        List<String> frontRecordCodes = list.stream().map(ReplenishRecordE :: getRecordCode).distinct().collect(Collectors.toList());
            //根据前置单获取出入库单
        List<WarehouseRecordE> warehouseRecordList = queryWarehouseRecordByFontRecordCode(frontRecordCodes);

        list.forEach(item ->{
            //设置单据类型
            item.setFrontRecordTypeName(FrontRecordTypeEnum.getDescByType(item.getRecordType()));
            warehouseRecordList.forEach(record ->{
                if(item.getRecordCode().equals(record.getFrontRecordCode())) {
                    //出库
                    if(record.getBusinessType() == 1) {
                        if(item.getDeliveryTime() == null) {
                            //设置发货时间
                            item.setDeliveryTime(record.getDeliveryTime());
                        }
                        //设置出库单锁定数量
                        for(WarehouseRecordDetailE warehouseRecordDetail:record.getWarehouseRecordDetailList()){
                            if(item.getSapPoNo().equals(warehouseRecordDetail.getSapPoNo())
                                    && item.getLineNo().equals(warehouseRecordDetail.getLineNo())&& item.getSkuCode().equals(warehouseRecordDetail.getSkuCode())){
                                item.setPlanQty(warehouseRecordDetail.getPlanQty());
                            }
                        }
                    }
                    //入库
                    if(record.getBusinessType() == 2) {
                        if(item.getReceiverTime() == null) {
                            //设置收货时间
                            item.setReceiverTime(record.getDeliveryTime());
                        }
                    }
                }
            });
        });

        List<Long> skuIdList = list.stream().map(ReplenishRecordE::getSkuId).filter((id) -> {
            if(id == null || id == 0) {
                return false;
            } else {
                return true;
            }
        }).distinct().collect(Collectors.toList());
        List<SkuInfoExtDTO> skuList =itemFacade.skuBySkuIds(skuIdList);
        Map<Long, SkuInfoExtDTO> skuMap = new HashMap<>();
        for(SkuInfoExtDTO dto: skuList) {
            skuMap.put(dto.getId(), dto);
        }

        List<String> codeList = list.stream().map(ReplenishRecordE::getFactoryCode).filter((code) -> {
            if(code == null || code.equals("")) {
                return false;
            } else {
                return true;
            }
        }).distinct().collect(Collectors.toList());
        List<StoreDTO> storeList = baseFacade.searchByCodeList(codeList);
        Map<String, StoreDTO> factoryMap = new HashMap<>();
        for(StoreDTO dto: storeList) {
            factoryMap.put(dto.getCode(), dto);
        }

        for(ReplenishRecordE dto: list) {
            Long skuId = dto.getSkuId();
            SkuInfoExtDTO sku = skuMap.get(skuId);
            if(sku != null) {
                dto.setSkuName(sku.getName());
            }

            String factoryCode = dto.getFactoryCode();
            if(StringUtils.isNotBlank(factoryCode)) {
                StoreDTO store = factoryMap.get(factoryCode);
                if(store != null) {
                    dto.setFactoryName(store.getName());
                }
            }
        }

        List<ShopReplenishReportDetailDTO> dtoList= replenishAllotLogConvertor.covertReList2Elist(list);
        PageInfo<ShopReplenishReportDetailDTO> pageInfo = new PageInfo<>(dtoList);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    /**
     * 交货单统计
     * @param condition
     * @return
     */
    @Override
    public PageInfo<ShopReplenishReportStatDTO> statReplenishReport(ShopReplenishReportDTO condition) {
        Date startTime = condition.getStartTime();
        Date endTime = condition.getEndTime();
        if(startTime == null && endTime == null) {
            Date date = DateUtils.addDays(new Date(), replenishReportAllotValue * -1);
            startTime = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
            condition.setStartTime(startTime);
        }
        Page page = PageHelper.startPage(condition.getPageIndex(), condition.getPageSize());
        List<ShopReplenishReportStatE> list = replenishRecordDetailMapper.statReplenishReport(condition);
        List<ShopReplenishReportStatDTO> dtoList=new ArrayList<>();
        for (ShopReplenishReportStatE stateE:list) {
            ShopReplenishReportStatDTO stateDTO=replenishAllotLogConvertor.covertStateE2DTO(stateE);
            dtoList.add(stateDTO);
        }
        if(dtoList.isEmpty()) {
            PageInfo<ShopReplenishReportStatDTO> pageInfo = new PageInfo<>(dtoList);
            pageInfo.setTotal(page.getTotal());
            return pageInfo;
        }

        List<String> codeList = dtoList.stream().map(ShopReplenishReportStatDTO::getFactoryCode).filter((code) -> {
            if(code == null || code.equals("")) {
                return false;
            } else {
                return true;
            }
        }).distinct().collect(Collectors.toList());
        List<StoreDTO> storeList =baseFacade.searchByCodeList(codeList);
        Map<String, StoreDTO> factoryMap = new HashMap<>();
        for(StoreDTO dto: storeList) {
            factoryMap.put(dto.getCode(), dto);
        }

        for(ShopReplenishReportStatDTO dto: dtoList) {
            String factoryCode = dto.getFactoryCode();
            if(factoryCode != null && !factoryCode.equals("")) {
                StoreDTO store = factoryMap.get(factoryCode);
                if(store != null) {
                    dto.setFactoryName(store.getName());
                }
            }

            String desc = FrontRecordTypeEnum.getDescByType(dto.getRecordType());
            dto.setRecordTypeName(desc);

            ShopReplenishRequireTypeEnum requireTypeVo = ShopReplenishRequireTypeEnum.findByType(dto.getRequireType());
            if(requireTypeVo != null) {
                dto.setRequireTypeName(requireTypeVo.getDesc());
            }
        }

        PageInfo<ShopReplenishReportStatDTO> pageInfo = new PageInfo<>(dtoList);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    /**
     * @Description: 门店补货出库通知结果处理 <br>
     *
     * @Author chuwenchao 2020/6/23
     * @param stockNotifyDTO
     * @return
     */
    @Override
    public void warehouseOutNotify(StockNotifyDTO stockNotifyDTO) {
        //查询后置单
        WarehouseRecordE warehouseRecordE = warehouseRecordMapper.queryByRecordCode(stockNotifyDTO.getRecordCode());
        //查询后置单明细
        List<WarehouseRecordDetailE> detailList = warehouseRecordDetailMapper.queryListByRecordCode(stockNotifyDTO.getRecordCode());
        warehouseRecordE.setWarehouseRecordDetailList(detailList);
        //查询关联关系
        List<FrontWarehouseRecordRelationE> relationEList = frontWarehouseRecordRelationMapper.getFrontRelationByRecordCode(stockNotifyDTO.getRecordCode());
        AlikAssert.isNotEmpty(relationEList, ResCode.ORDER_ERROR_5120, ResCode.ORDER_ERROR_5120_DESC);
        //根据ID更新前置单明细实际数量
        List<ReplenishDetailE> updateDetail = new ArrayList<>();
        ReplenishRecordE replenishRecordE = null;
        for(FrontWarehouseRecordRelationE recordRelationE : relationEList) {
            replenishRecordE = replenishRecordMapper.queryReplenishRecordById(recordRelationE.getFrontRecordId());
            AlikAssert.isNotNull(recordRelationE, ResCode.ORDER_ERROR_5106, ResCode.ORDER_ERROR_5106_DESC);
            //Map {前置单ID : 后置单明细}
            Map<String, WarehouseRecordDetailE> map = detailList.stream().collect(Collectors.toMap(WarehouseRecordDetailE::getDeliveryLineNo, Function.identity()));
            List<ReplenishDetailE> replenishDetailEList = replenishRecordDetailMapper.queryDetailByRecordId(replenishRecordE.getId());
            for(ReplenishDetailE replenishDetailE : replenishDetailEList) {
                WarehouseRecordDetailE recordDetailE = map.get(replenishDetailE.getId().toString());
                if(recordDetailE != null) {
                    replenishDetailE.setBasicSkuQty(recordDetailE.getActualQty());
                } else {
                    replenishDetailE.setBasicSkuQty(BigDecimal.ZERO);
                }
            }
            //更新前置单为已出库
            replenishRecordMapper.updateToOutAllocation(replenishRecordE.getId());
            //更新前置单明细仓库出库数量
            skuQtyUnitTool.convertBasicToReal(replenishDetailEList);
            updateDetail.addAll(replenishDetailEList);
        }
        //批量更新明细数量
        replenishRecordDetailMapper.updateRealOutQty(updateDetail);
        //构造门店入库单数据
        WarehouseRecordE inRecordE = this.initShopInRecord(replenishRecordE, warehouseRecordE);
        //同步库存中心入库单据
        InWarehouseRecordDTO inRecordDTO = stockInRecordDTOConvert.convertE2InDTO(inRecordE);
        stockRecordFacade.createInRecord(inRecordDTO);
    }

    @Override
    public void dispatchResultReplenishComplete(String recordCode) {
        List<FrontWarehouseRecordRelationE> relationEList = frontWarehouseRecordRelationMapper.getFrontRelationByRecordCode(recordCode);
        AlikAssert.isNotEmpty(relationEList, ResCode.ORDER_ERROR_5120, ResCode.ORDER_ERROR_5120_DESC);
        List<Long> frontRecordIdList = relationEList.stream().map(FrontWarehouseRecordRelationE::getFrontRecordId).distinct().collect(Collectors.toList());
        replenishRecordMapper.updateIsNeedDispatchComplete(frontRecordIdList);
    }

    /**
     * @Description: 门店补货入库通知结果处理 <br>
     *
     * @Author chuwenchao 2020/6/23
     * @param stockNotifyDTO
     * @return
     */
    @Override
    public void warehouseInNotify(StockNotifyDTO stockNotifyDTO) {
        //查询后置单明细
        List<WarehouseRecordDetailE> detailList = warehouseRecordDetailMapper.queryListByRecordCode(stockNotifyDTO.getRecordCode());
        //查询关联关系
        List<FrontWarehouseRecordRelationE> relationEList = frontWarehouseRecordRelationMapper.getFrontRelationByRecordCode(stockNotifyDTO.getRecordCode());
        AlikAssert.isNotEmpty(relationEList, ResCode.ORDER_ERROR_5120, ResCode.ORDER_ERROR_5120_DESC);
        //根据ID更新前置单明细实际数量
        ReplenishRecordE replenishRecordE = null;
        List<ReplenishDetailE> updateDetail = new ArrayList<>();
        for(FrontWarehouseRecordRelationE recordRelationE : relationEList) {
            replenishRecordE = replenishRecordMapper.queryReplenishRecordById(recordRelationE.getFrontRecordId());
            AlikAssert.isNotNull(recordRelationE, ResCode.ORDER_ERROR_5106, ResCode.ORDER_ERROR_5106_DESC);
            //Map {前置单ID : 后置单明细}
            Map<String, WarehouseRecordDetailE> map = detailList.stream().collect(Collectors.toMap(WarehouseRecordDetailE::getDeliveryLineNo, Function.identity()));
            List<ReplenishDetailE> replenishDetailEList = replenishRecordDetailMapper.queryDetailByRecordId(replenishRecordE.getId());
            for(ReplenishDetailE replenishDetailE : replenishDetailEList) {
                WarehouseRecordDetailE recordDetailE = map.get(replenishDetailE.getId().toString());
                if(recordDetailE != null) {
                    replenishDetailE.setBasicSkuQty(recordDetailE.getActualQty());
                } else {
                    replenishDetailE.setBasicSkuQty(BigDecimal.ZERO);
                }
            }
            //更新前置单为已出库
            replenishRecordMapper.updateToInAllocation(replenishRecordE.getId());
            //更新前置单明细仓库出库数量
            skuQtyUnitTool.convertBasicToReal(replenishDetailEList);
            updateDetail.addAll(replenishDetailEList);
        }
        //批量更新明细数量
        replenishRecordDetailMapper.updateRealInQty(updateDetail);

        //计算差异订单数据-Lin.Xu 2020年7月13日19:34:02
        disparityRecordService.addDisparityRecord(stockNotifyDTO.getRecordCode(), relationEList);
    
    }

    /**
     * @Description: 构造门店入库单数据 <br>
     *
     * @Author chuwenchao 2020/6/27
     * @param replenishRecordE
     * @param warehouseRecordE
     * @return
     */
    private WarehouseRecordE initShopInRecord(ReplenishRecordE replenishRecordE, WarehouseRecordE warehouseRecordE) {
        WarehouseRecordE inRecordE = new WarehouseRecordE();
        if(FrontRecordTypeEnum.SHOP_REPLENISHMENT_RECORD.getType().equals(replenishRecordE.getRecordType())) {
            //直营
            String code = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.DS_REPLENISH_IN_SHOP_RECORD.getCode());
            inRecordE.setRecordCode(code);
            inRecordE.setRecordType(WarehouseRecordTypeEnum.DS_REPLENISH_IN_SHOP_RECORD.getType());
        } else if(FrontRecordTypeEnum.JOIN_SHOP_REPLENISHMENT_RECORD.getType().equals(replenishRecordE.getRecordType())) {
            //加盟
            String code = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.LS_REPLENISH_IN_SHOP_RECORD.getCode());
            inRecordE.setRecordCode(code);
            inRecordE.setRecordType(WarehouseRecordTypeEnum.LS_REPLENISH_IN_SHOP_RECORD.getType());
        } else if(FrontRecordTypeEnum.SHOP_COLD_CHAIN_DELIVERY_RECORD.getType().equals(replenishRecordE.getRecordType())) {
            //冷链
            String code = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.SHOP_COLD_CHAIN_IN_RECORD.getCode());
            inRecordE.setRecordCode(code);
            inRecordE.setRecordType(WarehouseRecordTypeEnum.SHOP_COLD_CHAIN_IN_RECORD.getType());
        }
        inRecordE.setRealWarehouseId(replenishRecordE.getInRealWarehouseId());
        inRecordE.setFactoryCode(replenishRecordE.getInFactoryCode());
        inRecordE.setRealWarehouseCode(replenishRecordE.getInRealWarehouseCode());
        inRecordE.setBusinessType(WarehouseRecordBusinessTypeEnum.IN_WAREHOUSE_RECORD.getType());
        inRecordE.setRecordStatus(WarehouseRecordStatusEnum.INIT.getStatus());
        inRecordE.setChannelCode(replenishRecordE.getChannelCode());
        inRecordE.setOutCreateTime(replenishRecordE.getOutCreateTime());
        warehouseRecordMapper.insertWarehouseRecord(inRecordE);
        List<WarehouseRecordDetailE> detailList = warehouseRecordE.getWarehouseRecordDetailList();
        List<WarehouseRecordDetailE> warehouseRecordDetailList = new ArrayList<>(detailList.size());
        //保存明细
        for(WarehouseRecordDetailE detailE : detailList) {
            WarehouseRecordDetailE warehouseRecordDetail = new WarehouseRecordDetailE();
            BeanUtils.copyProperties(detailE, warehouseRecordDetail);
            warehouseRecordDetail.setRecordCode(inRecordE.getRecordCode());
            warehouseRecordDetail.setWarehouseRecordId(inRecordE.getId());
            warehouseRecordDetail.setPlanQty(detailE.getActualQty());
            warehouseRecordDetail.setActualQty(BigDecimal.ZERO);
            warehouseRecordDetail.setSapPoNo(replenishRecordE.getSapPoNo());
            warehouseRecordDetailList.add(warehouseRecordDetail);
        }
        inRecordE.setWarehouseRecordDetailList(warehouseRecordDetailList);
        warehouseRecordDetailMapper.insertWarehouseRecordDetails(warehouseRecordDetailList);
        //保存关联关系
        FrontWarehouseRecordRelationE relation = new FrontWarehouseRecordRelationE();
        relation.setWarehouseRecordId(inRecordE.getId());
        relation.setFrontRecordId(replenishRecordE.getId());
        relation.setFrontRecordType(replenishRecordE.getRecordType());
        relation.setRecordCode(inRecordE.getRecordCode());
        relation.setFrontRecordCode(replenishRecordE.getRecordCode());
        frontWarehouseRecordRelationMapper.insertFrontWarehouseRecordRelation(relation);
        return inRecordE;
    }

    /**
     * @Description: 获取距离当天凌晨的剩余秒数 <br>
     *
     * @Author chuwenchao 2020/6/15
     * @param currentDate
     * @return 
     */
    private Integer getRemainSecondsOneDay(Date currentDate) {
        LocalDateTime midnight = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault());
        long seconds = ChronoUnit.SECONDS.between(currentDateTime, midnight);
        return (int) seconds;
    }

    /**
     * @Description: 分页查询出所有待分配的订单 <br>
     *
     * @Author chuwenchao 2020/6/15
     * @param allotDTO
     * @return 
     */
    private List<ReplenishRecordE> getAllotOrderList(ShopReplenishAllotDTO allotDTO) {
        List<ReplenishRecordE> allOrderList = new ArrayList<>();
        Integer currentPage = 0;
        Integer rows = 200;
        while (true) {
            currentPage++;
            //分页查询类型为直营 状态为初始化 的前置单据
            Integer startRow = rows * (currentPage - 1);
            List<ReplenishRecordE> records = replenishRecordMapper.getWaitAllotRecord(startRow, rows, allotDTO);
            //查询结果为空则退出循环
            if (records == null || records.isEmpty()) {
                break;
            }
            List<Long> recordIds = records.stream().map(ReplenishRecordE::getId).collect(Collectors.toList());
            //用recordIds批量查询item
            List<ReplenishDetailE> items = replenishRecordDetailMapper.queryDetailByRecordIds(recordIds);
            Map<Long, List<ReplenishDetailE>> itemVOMap = items.stream().collect(Collectors.groupingBy(ReplenishDetailE::getFrontRecordId));
            for (ReplenishRecordE record : records) {
                List<ReplenishDetailE> itemList = itemVOMap.get(record.getId());
                record.setFrontRecordDetails(itemList);
            }
            allOrderList.addAll(records);
            if (records.size() < rows) {
                break;
            }
        }
        return allOrderList;
    }


    /**
     * 根据前置单获取出入库单
     * @param frontRecordCodes
     * @return
     */
    private List<WarehouseRecordE> queryWarehouseRecordByFontRecordCode(List<String> frontRecordCodes){
        List<WarehouseRecordE> recordList = new ArrayList<>();
        //获取关联信息数据
        List<FrontWarehouseRecordRelationE> whRelationList=frontWarehouseRecordRelationMapper.getRecordRelationByFrontRecordCodes(frontRecordCodes);
        List<String> recordCodeList = whRelationList.stream().map(FrontWarehouseRecordRelationE::getRecordCode).distinct().collect(Collectors.toList());

        //根据出库单编码获取所有的出库单类型
        List<WarehouseRecordE> warehouseRecordList = warehouseRecordMapper.getRecordTypeByRecordCodes(recordCodeList);
        for (WarehouseRecordE recordE:warehouseRecordList) {
            List<WarehouseRecordDetailE> detailEList =warehouseRecordDetailMapper.queryListByRecordCode(recordE.getRecordCode());
            recordE.setWarehouseRecordDetailList(detailEList);
        }
        Map<String, WarehouseRecordE> relationMap = warehouseRecordList.stream().collect(Collectors.toMap(WarehouseRecordE::getRecordCode, item -> item));
        for (FrontWarehouseRecordRelationE relationE:whRelationList) {
            WarehouseRecordE warehouseRecord= new WarehouseRecordE();
            warehouseRecord.setFrontRecordCode(relationE.getFrontRecordCode());
            warehouseRecord.setRecordCode(relationE.getRecordCode());
            if(relationMap.containsKey(relationE.getRecordCode())){
                WarehouseRecordE warehouseRecordE  = relationMap.get(relationE.getRecordCode());
                warehouseRecord.setBusinessType(warehouseRecordE.getBusinessType());
                warehouseRecord.setSapOrderCode(warehouseRecordE.getSapOrderCode());
                warehouseRecord.setDeliveryTime(warehouseRecordE.getDeliveryTime());
                warehouseRecord.setWarehouseRecordDetailList(warehouseRecordE.getWarehouseRecordDetailList());
            }
            recordList.add(warehouseRecord);
        }
        return recordList;
    }
}
