package com.lyf.scm.core.service.stockFront.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.lyf.scm.common.enums.*;
import com.lyf.scm.core.domain.convert.stockFront.StockInRecordDTOConvert;
import com.lyf.scm.core.domain.convert.stockFront.StockRecordDTOConvert;
import com.lyf.scm.core.mapper.stockFront.*;
import com.lyf.scm.core.remote.base.dto.StoreDTO;
import com.lyf.scm.core.remote.stock.dto.*;
import com.lyf.scm.core.remote.stock.facade.StockRecordFacade;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.stockFront.WarehouseRecordCommService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.KibanaLogConstants;
import  com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.api.dto.stockFront.ShopAllocationDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopAllocationRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopAllocationRecordPageDTO;
import com.lyf.scm.core.config.ServiceKibanaLog;
import com.lyf.scm.core.domain.convert.stockFront.ShopAllocationConvertor;
import com.lyf.scm.core.domain.entity.stockFront.ShopAllocationDetailE;
import com.lyf.scm.core.domain.entity.stockFront.ShopAllocationE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.remote.base.facade.BaseFacade;
import com.lyf.scm.core.remote.cmp.facade.CmpFacade;
import com.lyf.scm.core.remote.item.ItemInfoTool;
import com.lyf.scm.core.remote.item.SkuQtyUnitTool;
import com.lyf.scm.core.remote.item.dto.ParamExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuInfoExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuUnitExtDTO;
import com.lyf.scm.core.remote.item.dto.StorePurchaseAccessDTO;
import com.lyf.scm.core.remote.item.dto.StorePurchaseDTO;
import com.lyf.scm.core.remote.item.dto.StorePurchaseParamDTO;
import com.lyf.scm.core.remote.item.dto.StorePurchasePowerDTO;
import com.lyf.scm.core.remote.item.dto.StoreSaleParamDTO;
import com.lyf.scm.core.remote.item.dto.StoreSalePowerDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.service.stockFront.ShopAllocationToWareHouseRecordService;
import com.lyf.scm.core.service.stockFront.ShopAllocationDetailService;
import com.lyf.scm.core.service.stockFront.ShopAllocationService;
import com.rome.arch.core.exception.RomeException;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @date 2020/6/15 14:35
 * @Version
 */
@Slf4j
@Service
public class ShopAllocationServiceImpl implements ShopAllocationService {

    @Resource
    private ShopAllocationConvertor shopAllocationConvertor;
    @Resource
    private ShopAllocationDetailService shopAllocationDetailService;
    @Resource
    private ShopAllocationToWareHouseRecordService allocationToWareHouseRecordService;
    @Resource
    private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;
    @Resource
    private ShopAllocationMapper shopAllocationMapper;
    @Resource
    private ShopAllocationDetailMapper shopAllocationDetailMapper;
    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;
    @Resource
    private WarehouseRecordDetailMapper warehouseRecordDetailMapper;
    @Resource
    private SkuQtyUnitTool skuQtyUnitTool;
    @Resource
    private ItemInfoTool itemInfoTool;
    @Resource
    private ItemFacade itemFacade;
    @Resource
    private BaseFacade baseFacade;
    @Resource
    private CmpFacade cmpFacade;
    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;
    @Resource
    private StockRecordFacade stockRecordFacade;
    @Resource
    private StockRecordDTOConvert stockRecordDTOConvert;
    @Resource
    private OrderUtilService orderUtilService;
    @Resource
    private StockInRecordDTOConvert stockInRecordDTOConvert;
    @Resource
    private WarehouseRecordCommService warehouseRecordCommService;

    @Value("${switch.checkStorePower}")
    private boolean switchCheckStorePower;
    @Value("${switch.checkBySap}")
    private boolean switchCheckBySap;

    /**
     * 创建门店调拨单
     * @param frontRecord
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopAllocationRecordDTO addShopAllocationRecord(ShopAllocationRecordDTO frontRecord) {
        //调拨库存可以扣成负数
        //幂等性判断
        int count= shopAllocationMapper.judgeExistByOutRecordCode(frontRecord.getOutRecordCode());
        if(count>0){
            return frontRecord;
        }
        ShopAllocationE frontRecordE = shopAllocationConvertor.convertDTO2E(frontRecord);
        //查询门店对应仓库
        List<String> shopCodes = new ArrayList<>();
        shopCodes.add(frontRecordE.getInShopCode());
        shopCodes.add(frontRecordE.getOutShopCode());
        //  根据门店编号批量查询实仓信息
        List<RealWarehouse> realWarehouseList =stockRealWarehouseFacade.queryRealWarehouseByShopCodes(shopCodes);
        if(realWarehouseList == null || realWarehouseList.size() < 2){
            throw new RomeException(ResCode.ORDER_ERROR_7202,ResCode.ORDER_ERROR_7202_DESC);
        }
        // 获取实仓对象
        RealWarehouse inRealWarehouse=realWarehouseList.stream().filter(realWh ->realWh.getShopCode().equals(frontRecordE.getInShopCode())).findFirst().orElse(null);
        //设置入库仓库
        if(null ==inRealWarehouse.getId() || 0 == inRealWarehouse.getId()){
            throw new RomeException(ResCode.ORDER_ERROR_7202,ResCode.ORDER_ERROR_7202_DESC);
        }else{
            frontRecordE.setInRealWarehouseId(inRealWarehouse.getId());
            // 设置工厂编码和仓库外部编码
            frontRecordE.setInFactoryCode(inRealWarehouse.getFactoryCode());
            frontRecordE.setInRealWarehouseCode(inRealWarehouse.getRealWarehouseOutCode());

        }

        // 获取实仓对象
        RealWarehouse outRealWarehouse=realWarehouseList.stream().filter(realWh ->realWh.getShopCode().equals(frontRecordE.getOutShopCode())).findFirst().orElse(null);
        //设置出库仓库
        if(outRealWarehouse.getId() == null || outRealWarehouse.getId() == 0){
            throw new RomeException(ResCode.ORDER_ERROR_7202,ResCode.ORDER_ERROR_7202_DESC);
        }else{
            frontRecordE.setOutRealWarehouseId(outRealWarehouse.getId());
            //  设置工厂编码和仓库外部编码
            frontRecordE.setOutFactoryCode(outRealWarehouse.getFactoryCode());
            frontRecordE.setOutRealWarehouseCode(outRealWarehouse.getRealWarehouseOutCode());

        }

        //  根据门店code批量获取门店信息
        List<StoreDTO> stores = baseFacade.searchByCodeList(shopCodes);
        StoreDTO outShop = stores.stream().filter(store -> frontRecordE.getOutShopCode().equals(store.getCode())).findFirst().orElse(null);
        StoreDTO inShop = stores.stream().filter(store -> frontRecordE.getInShopCode().equals(store.getCode())).findFirst().orElse(null);
        if(outShop == null || inShop == null) {
            throw new RomeException(ResCode.ORDER_ERROR_7302,ResCode.ORDER_ERROR_7302_DESC);
        }

        //判断门店是直营还是加盟
        if(isDirectlyShop(inShop, outShop)){
            frontRecordE.setBusinessType(1);
        }else if(isJoinShop(inShop, outShop)) {
            frontRecordE.setBusinessType(2);
        }else{
            throw new RomeException(ResCode.ORDER_ERROR_7302,ResCode.ORDER_ERROR_7302_DESC);
        }
        if(frontRecordE.getBusinessType() == 1){
            //直营门店通过公司code判断同组织
            if(!outShop.getCompanyCode().equals(inShop.getCompanyCode())){
                throw new RomeException(ResCode.ORDER_ERROR_7303,ResCode.ORDER_ERROR_7303_DESC);
            }
        }else{
            if(!outShop.getFranchisee().equals(inShop.getFranchisee())){
                throw new RomeException(ResCode.ORDER_ERROR_7303,ResCode.ORDER_ERROR_7303_DESC);
            }
        }

        //设置前置单为已入库
        frontRecordE.setRecordStatus(FrontRecordStatusEnum.IN_ALLOCATION.getStatus());

        //创建门店调拨单 + 明细
        addFrontRecord(frontRecordE);

        if(switchCheckStorePower) {
            //校验门店调拨进货权, 开关判断取sap数据还是取中台的数据
            if(switchCheckBySap){
                this.checkStorePurchasePowerBySAP(frontRecordE);
            }else{
                this.checkStorePurchasePowerByItemCore(frontRecordE);
            }
            //校验门店调拨销售权
            this.checkStoreSalePower(frontRecordE);
        }
       //根据调拨前置单创建后置出库单
        WarehouseRecordE outwarehouseRecordE =allocationToWareHouseRecordService.createOutRecordByFrontRecord(frontRecordE);

        //根据调拨前置单创建后置 入库单
        WarehouseRecordE inwarehouseRecordE=allocationToWareHouseRecordService.createInRecordByFrontRecord(frontRecordE);

        //同步出库单 到 库存中心()
        OutWarehouseRecordDTO outRecordDto = stockRecordDTOConvert.convertE2OutDTO(outwarehouseRecordE);
        for (WarehouseRecordDetailE detail:outwarehouseRecordE.getWarehouseRecordDetailList()) {
            outRecordDto.getDetailList().stream().filter(recordDetail -> recordDetail.getSkuCode().equals(detail.getSkuCode())).forEach(
                    rDetail ->{
                        rDetail.setLineNo(detail.getId() + "");
                        rDetail.setDeliveryLineNo(detail.getId() + "");
                    }
            );
        }
        stockRecordFacade.createOutRecord(outRecordDto);

        boolean flag=false;
        try {
            if(null !=inwarehouseRecordE){
                //同步入库单 到 库存中心
                InWarehouseRecordDTO inRecordDTO = stockInRecordDTOConvert.convertE2InDTO(inwarehouseRecordE);
                for (WarehouseRecordDetailE detail:inwarehouseRecordE.getWarehouseRecordDetailList()) {
                    inRecordDTO.getDetailList().stream().filter(recordDetail -> recordDetail.getSkuCode().equals(detail.getSkuCode())).forEach(
                            rDetail -> {
                                rDetail.setLineNo(detail.getId() + "");
                                rDetail.setDeliveryLineNo(detail.getId() + "");
                            }
                    );
                }
                stockRecordFacade.createInRecord(inRecordDTO);
            }

            flag=true;
            log.info("id:{}，调拨单处理成功",frontRecordE.getId());
            frontRecord.setRecordCode(frontRecordE.getRecordCode());
            return frontRecord;
        } catch (Exception e) {
            log.info("门店调拨RomeException错误，同步入库单异常：", e);
            throw new RomeException(ResCode.ORDER_ERROR_1001,"同步入库单到库存中心失败"+frontRecordE.getOutRecordCode());
        }finally {
            if(!flag){
                //取消出库单
                CancelRecordDTO cancelRecordDTO= new CancelRecordDTO();
                cancelRecordDTO.setRecordCode(outwarehouseRecordE.getRecordCode());
                cancelRecordDTO.setRecordType(outwarehouseRecordE.getRecordType());
                cancelRecordDTO.setIsForceCancel(YesOrNoEnum.YES.getType());
                warehouseRecordCommService.cancelWarehouseRecordToStock(cancelRecordDTO);

            }
        }
    }

    /**
     *查询门店调拨单列表
     * @param frontRecord
     * @return
     */
    @Override
    public PageInfo<ShopAllocationRecordPageDTO> queryShopAllocationList(ShopAllocationRecordPageDTO frontRecord) {
        Page page = PageHelper.startPage(frontRecord.getPageIndex() , frontRecord.getPageSize());
        ShopAllocationE frontRecordE = shopAllocationConvertor.allocationPageDtoToShopAllocationEntity(frontRecord);
        //获取门店调拨单列表
        List<ShopAllocationE> doList = shopAllocationMapper.queryShopAllocationList(frontRecordE);
        List<String> shopCodes = new ArrayList<>();
        shopCodes.addAll(doList.stream().filter(record -> StringUtils.isNotBlank(record.getInShopCode())).map(ShopAllocationE:: getInShopCode).collect(Collectors.toList()));
        shopCodes.addAll(doList.stream().filter(record -> StringUtils.isNotBlank(record.getOutShopCode())).map(ShopAllocationE:: getOutShopCode).collect(Collectors.toList()));

        List<StoreDTO> storeList = baseFacade.searchByCodeList(shopCodes);
        for (StoreDTO store : storeList) {
            doList.stream().filter(record -> record.getInShopCode().equals(store.getCode())).forEach(record -> record.setInShopName(store.getName()));
            doList.stream().filter(record -> record.getOutShopCode().equals(store.getCode())).forEach(record -> record.setOutShopName(store.getName()));
        }
        List<ShopAllocationRecordPageDTO> pageList = shopAllocationConvertor.shopAllocationEntityListToAllocationPageList(doList);
        PageInfo<ShopAllocationRecordPageDTO> personPageInfo = new PageInfo<>(pageList);
        personPageInfo.setTotal(page.getTotal());
        return personPageInfo;

    }

    /**
     * 根据id获取门店调拨单详情列表
     * @param frontRecordId
     * @return
     */
    @Override
    public List<ShopAllocationDetailDTO> queryShopAllocationDetailList(Long frontRecordId) {
        List<ShopAllocationDetailE> doList = shopAllocationDetailMapper.queryShopAllocationDetailList(frontRecordId);
        List<ParamExtDTO> paList= new ArrayList<>();
        for (ShopAllocationDetailE detailE:doList) {
            ParamExtDTO paramExtDTO = new ParamExtDTO();
            paramExtDTO.setSkuId(detailE.getSkuId());
            paramExtDTO.setUnitCode(detailE.getUnitCode());
            paList.add(paramExtDTO);
        }
        //查询商品单位信息
        List<SkuUnitExtDTO> skuList = itemFacade.unitsBySkuIdAndUnitCode(paList, null);
        //查询商品信息
        List<Long> skuIds = doList.stream().map(ShopAllocationDetailE::getSkuId).distinct().collect(Collectors.toList());
        List<SkuInfoExtDTO> skuInfoList = itemFacade.skuBySkuIds(skuIds);
        doList.forEach(detail -> {
            //设置单位信息
            SkuUnitExtDTO skuUnit = skuList.stream().filter(sku -> sku.getSkuId().equals(detail.getSkuId()) && sku.getUnitCode().equals(detail.getUnitCode()))
                    .findFirst().orElse(null);
            if (skuUnit != null) {
                detail.setUnit(skuUnit.getUnitName());
            }
            //设置商品名称
            SkuInfoExtDTO skuInfo = skuInfoList.stream().filter(sku -> sku.getId().equals(detail.getSkuId())).findFirst().orElse(null);
            if (skuInfo != null) {
                detail.setSkuName(skuInfo.getName());
            }
        });
        List<ShopAllocationDetailDTO> list = shopAllocationConvertor.convertDetailE2DetailDTO(doList);
        return list;
    }

    /**
     * 根据出入库单据编号查询门店调拨单
     * @param recordCode
     * @return
     */
    @Override
    public ShopAllocationRecordDTO queryAllocationByRecordCode(String recordCode) {
        //获取前置单号
        String frontRecord = frontWarehouseRecordRelationMapper.getFrontRecordCodeByRecordCode(recordCode);
        if (StringUtils.isBlank(frontRecord)) {
            return null;
        }
        return shopAllocationConvertor.convertE2DTO(shopAllocationMapper.queryFrontRecordByCode(frontRecord));
    }

    /**
     * 推送CMP调拨单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleShopAllocationRecordsPushCmp() {
        Calendar cal = Calendar.getInstance();
        Date endDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        //结束时间
        Date startDate = cal.getTime();
        //查询未推送cmp的调拨出入库单
        List<WarehouseRecordE> wareRecordEList=warehouseRecordMapper.queryAllocationWarehouseCmpList(WarehouseRecordTypeEnum.SHOP_ALLOCATION_IN_WAREHOUSE_RECORD.getType(),startDate,endDate);
        for (WarehouseRecordE recordE : wareRecordEList) {

                //查询调拨明细
                List<WarehouseRecordDetailE> detailEList = warehouseRecordDetailMapper.queryListByRecordId(recordE.getId());
                recordE.setWarehouseRecordDetailList(detailEList);
                //查询调拨单号
                String frontRecordCode = frontWarehouseRecordRelationMapper.getFrontRecordCodeByRecordCode(recordE.getRecordCode());
                recordE.setFrontRecordCode(frontRecordCode);
                ShopAllocationE shopAllocationE = shopAllocationMapper.queryFrontRecordByCode(frontRecordCode);
                recordE.setOutCreateTime(shopAllocationE.getOutCreateTime());
                recordE.setShopCode(shopAllocationE.getInShopCode());
                List<Long> warehouseIds = frontWarehouseRecordRelationMapper.queryWarehouseRecordIdByRecord(frontRecordCode);
                Long outWarehouseId = warehouseIds.stream().filter(id -> !id.equals(recordE.getId())).findFirst().get();
                WarehouseRecordE outWarehouseRecord = warehouseRecordMapper.getWarehouseRecordById(outWarehouseId);
                List<WarehouseRecordDetailE> outDetails = warehouseRecordDetailMapper.queryListByRecordId(outWarehouseRecord.getId());
                outWarehouseRecord.setWarehouseRecordDetailList(outDetails);
                outWarehouseRecord.setFrontRecordCode(frontRecordCode);
                outWarehouseRecord.setOutCreateTime(shopAllocationE.getOutCreateTime());
                outWarehouseRecord.setShopCode(shopAllocationE.getOutShopCode());

                warehouseRecordMapper.updateCmpStatusComplete(recordE.getId());
                //推送cmp
                cmpFacade.shopAllocationRecordsPushCmp(recordE, outWarehouseRecord);

        }
    }

    /**
     * 修改走sap还是走商品中心的开关
     * @param checkStatus
     * @return
     */
    @Override
    public boolean setCheckStatus(boolean checkStatus) {

        switchCheckBySap = checkStatus ;
        return switchCheckBySap;
    }


    /**
     * 判断是否是直营门店 1/2/4
     * @return
     */
    private boolean isDirectlyShop(StoreDTO inShop,StoreDTO outShop){
        if("1".equals(inShop.getStoreProperties()) && "1".equals(outShop.getStoreProperties())){
            return true;
        }
        if("1".equals(inShop.getStoreProperties()) && "2".equals(outShop.getStoreProperties())){
            return true;
        }
        if("1".equals(inShop.getStoreProperties()) && "4".equals(outShop.getStoreProperties())){
            return true;
        }
        if("2".equals(inShop.getStoreProperties()) && "1".equals(outShop.getStoreProperties())){
            return true;
        }
        if("2".equals(inShop.getStoreProperties()) && "2".equals(outShop.getStoreProperties())){
            return true;
        }
        if("2".equals(inShop.getStoreProperties()) && "4".equals(outShop.getStoreProperties())){
            return true;
        }
        if("4".equals(inShop.getStoreProperties()) && "1".equals(outShop.getStoreProperties())){
            return true;
        }
        if("4".equals(inShop.getStoreProperties()) && "2".equals(outShop.getStoreProperties())){
            return true;
        }
        if("4".equals(inShop.getStoreProperties()) && "4".equals(outShop.getStoreProperties())){
            return true;
        }
        return false;
    }

    /**
     * 判断是否是加盟门店 3/5
     * @return
     */
    private boolean isJoinShop(StoreDTO inShop,StoreDTO outShop){
        if("3".equals(inShop.getStoreProperties()) && "3".equals(outShop.getStoreProperties())){
            return true;
        }
        if("3".equals(inShop.getStoreProperties()) && "5".equals(outShop.getStoreProperties())){
            return true;
        }
        if("5".equals(inShop.getStoreProperties()) && "3".equals(outShop.getStoreProperties())){
            return true;
        }
        if("5".equals(inShop.getStoreProperties()) && "5".equals(outShop.getStoreProperties())){
            return true;
        }
        return false;
    }

    /**
     * 创建门店调拨前置单
     * @param frontRecordE
     * @return
     */
    private void  addFrontRecord(ShopAllocationE frontRecordE){
        //设置单据类型
        frontRecordE.setRecordType(FrontRecordTypeEnum.SHOP_ALLOCATION_RECORD.getType());
        //生成单据编号
        String code = orderUtilService.queryOrderCode(FrontRecordTypeEnum.SHOP_ALLOCATION_RECORD.getCode());
        frontRecordE.setRecordCode(code);

        List<ShopAllocationDetailE> frontRecordDetails=frontRecordE.getFrontRecordDetails();

        //设置商品code或id
        itemInfoTool.convertSkuCode(frontRecordDetails);
        //查询基础单位，单位转换
        skuQtyUnitTool.queryBasicUnit(frontRecordDetails);

        if(StringUtils.isBlank(frontRecordE.getRemark())) {
            frontRecordE.setRemark("");
        }
        if(StringUtils.isBlank(frontRecordE.getRecordStatusReason())) {
            frontRecordE.setRecordStatusReason("");
        }

        //保存调拨调整单
        shopAllocationMapper.saveShopAllocationRecord(frontRecordE);

        //保存调拨调整单明细
        shopAllocationDetailService.saveAllocationDetail(frontRecordE);
    }

    /**
     * 校验门店调拨进货权(基于SAP数据)
     * @param frontRecordE
     */
    private void checkStorePurchasePowerBySAP(ShopAllocationE frontRecordE) {
        //调入门店编号
        String inShopCode = frontRecordE.getInShopCode();
        //商品CODE集合
        List<String> skuCodeList = frontRecordE.getFrontRecordDetails().stream().map(ShopAllocationDetailE:: getSkuCode).distinct().collect(Collectors.toList());
        //  根据门店编号和skuCodes查询门店所对应的进货权限（从SAP查询）
        List<StorePurchaseAccessDTO> storePurchasePowerDTOList = itemFacade.getStoreAccessFromSAPBySkuCodesAndStoreCode(inShopCode,skuCodeList);
        if(CollectionUtils.isEmpty(storePurchasePowerDTOList)) {
            StringBuilder  codes = new StringBuilder();
            skuCodeList.forEach(item -> {
                String respMsg = "[" + item + "]";
                codes.append(respMsg);
            });
            String info = "商品编码 " + codes.toString() + "在门店[" + inShopCode + "]无进货权";
            log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.SHOP_ALLOCATION, "checkStorePurchasePowerBySAP", "进货权信息不存在: " +info
              , frontRecordE));
            throw new RomeException(ResCode.ORDER_ERROR_1001,ResCode.ORDER_ERROR_1001_DESC + ": " + info);
        }
        //过滤出 无进货权的门店商品
        for (Iterator<StorePurchaseAccessDTO> iter = storePurchasePowerDTOList.iterator(); iter.hasNext();) {
            StorePurchaseAccessDTO element = iter.next();
            boolean flag = null != element && ("00".equals(element.getIsAccess()));
            if(flag){
                iter.remove();
            }
        }
        List<String> respMsgList = new ArrayList<String>();
        if(CollectionUtils.isNotEmpty(storePurchasePowerDTOList)) {
            storePurchasePowerDTOList.forEach(item -> {
                String respMsg = "[" + item.getSkuCode() + "]";
                respMsgList.add(respMsg);
            });
        }
        if(CollectionUtils.isNotEmpty(respMsgList)) {
            String info = "商品编码" + StringUtils.join(respMsgList, "") + "在门店[" + inShopCode + "]无进货权";
            log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.SHOP_ALLOCATION, "checkStorePurchasePowerBySAP", "门店无进货权: " +
                   info , frontRecordE));
            throw new RomeException(ResCode.ORDER_ERROR_1001,ResCode.ORDER_ERROR_1001_DESC + "：商品编码" + StringUtils.join(respMsgList, "") + "在门店[" + inShopCode + "]无进货权");
        }
    }

    /**
     * 校验门店调拨进货权(基于商品中心数据)
     * @param frontRecordE
     */
    private void checkStorePurchasePowerByItemCore(ShopAllocationE frontRecordE) {
        //调入门店编号
        String inShopCode = frontRecordE.getInShopCode();
        //商品CODE集合
        List<String> skuCodeList = frontRecordE.getFrontRecordDetails().stream().map(ShopAllocationDetailE:: getSkuCode).distinct().collect(Collectors.toList());

        //组装请求参数
        StorePurchaseParamDTO storePurchaseParamDTO =  new StorePurchaseParamDTO();
        storePurchaseParamDTO.setStoreCode(inShopCode);
        storePurchaseParamDTO.setSkuCodes(skuCodeList);
        List<StorePurchaseParamDTO> storePurchaseParamDTOList = new ArrayList<>();
        storePurchaseParamDTOList.add(storePurchaseParamDTO);
        // 根据门店编号和skuCodes查询门店所对应的进货权限（基于商品中心）
        List<StorePurchaseDTO> storePurchaseDTOList = itemFacade.getStorePurchaseAccessBySkuCodesAndStoreCode(storePurchaseParamDTOList);
        if(CollectionUtils.isEmpty(storePurchaseDTOList)) {
            StringBuilder  codes = new StringBuilder();
            skuCodeList.forEach(item -> codes.append("[" + item + "]"));
            String info = "商品编码 " + codes.toString() + "在门店[" + inShopCode + "]无进货权";
            log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.SHOP_ALLOCATION, "checkStorePurchasePowerByItemCore", "门店无进货权: " +
                   info , frontRecordE));
            throw new RomeException(ResCode.ORDER_ERROR_1001,ResCode.ORDER_ERROR_1001_DESC + ": " + info);
        }
        List<StorePurchasePowerDTO> storePurchasePowerDTOList = new ArrayList<>();
        for (StorePurchaseDTO storePurchaseDTO : storePurchaseDTOList) {
            storePurchasePowerDTOList.addAll(storePurchaseDTO.getStoreAccessList());
        }
        if(CollectionUtils.isEmpty(storePurchasePowerDTOList)) {
            StringBuilder  codes = new StringBuilder();
            skuCodeList.forEach(item -> codes.append("[" + item + "]"));
            String info = "商品编码 " + codes.toString() + "在门店[" + inShopCode + "]无进货权";
            log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.SHOP_ALLOCATION, "checkStorePurchasePowerByItemCore", "门店无进货权: " +
                    info  , frontRecordE));
            throw new RomeException(ResCode.ORDER_ERROR_1001,ResCode.ORDER_ERROR_1001_DESC + ": " + info);
        }
        //过滤无进货权的门店
        storePurchasePowerDTOList = storePurchasePowerDTOList.stream().filter(storeAccessExtDTO -> ("0").equals(storeAccessExtDTO.getIsAccess())).collect(Collectors.toList());
        List<String> respMsgList = new ArrayList<String>();
        if(CollectionUtils.isNotEmpty(storePurchasePowerDTOList)) {
            storePurchasePowerDTOList.forEach(item -> {
                String respMsg = "[" + item.getSkuCode() + "]";
                respMsgList.add(respMsg);
            });
        }
        if(CollectionUtils.isNotEmpty(respMsgList)) {
            String info = "商品编码" + StringUtils.join(respMsgList, "") + "在门店[" + inShopCode + "]无进货权";
            log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.SHOP_ALLOCATION, "checkStorePurchasePowerByItemCore", "门店无进货权: " +
                  info, frontRecordE));
            throw new RomeException(ResCode.ORDER_ERROR_1001,ResCode.ORDER_ERROR_1001_DESC + "：商品编码" + StringUtils.join(respMsgList, "") + "在门店[" + inShopCode + "]无进货权");
        }
    }

    /**
     * 校验门店调拨销售权
     * @param frontRecordE
     */
    private void checkStoreSalePower(ShopAllocationE frontRecordE) {
        //调入门店编号
        String inShopCode = frontRecordE.getInShopCode();
        //商品CODE集合
        List<String> skuCodeList = frontRecordE.getFrontRecordDetails().stream().map(ShopAllocationDetailE:: getSkuCode).distinct().collect(Collectors.toList());
        // 根据商品CODE、单位类型查询单位信息，type=2销售单位
        List<SkuUnitExtDTO> skuUnitExtDTOList = itemFacade.querySkuUnitsBySkuCodeAndType(skuCodeList, SkuUnitTypeEnum.SALES_UNIT.getId());;
        if(CollectionUtils.isEmpty(skuUnitExtDTOList)) {
            throw new RomeException(ResCode.ORDER_ERROR_6013,ResCode.ORDER_ERROR_6013_DESC);
        }

        List<StoreSaleParamDTO> storeSaleParamDTOList = new ArrayList<>();
        skuUnitExtDTOList.forEach(item -> {
            //组装请求参数
            StoreSaleParamDTO storeSaleParamDTO = new StoreSaleParamDTO();
            storeSaleParamDTO.setChannelCode(inShopCode + "_100");
            storeSaleParamDTO.setSkuCode(item.getSkuCode());
            storeSaleParamDTO.setSaleUnitCode(item.getUnitCode());
            storeSaleParamDTOList.add(storeSaleParamDTO);
        });
        // 批量通过skuCode、渠道编码、销售单位集合获取Sku/上下架/价格/单位信息
        List<StoreSalePowerDTO> storeSalePowerDTOList = itemFacade.skusByManyParam(storeSaleParamDTOList);
        if(CollectionUtils.isEmpty(storeSalePowerDTOList)) {
            throw new RomeException(ResCode.ORDER_ERROR_1001,ResCode.ORDER_ERROR_1001_DESC + "：商品销售权信息不存在");
        }
        //无销售权的门店
        List<StoreSalePowerDTO> noStoreSalePowerList = new ArrayList<StoreSalePowerDTO>();
        //过滤无销售权的门店
        List<StoreSalePowerDTO> storeSalePowerDTOListN = storeSalePowerDTOList.stream().filter(storeSalePowerDTO -> Integer.valueOf(0).equals(storeSalePowerDTO.getIsObtained())).collect(Collectors.toList());
        //过滤有销售权的门店
        List<StoreSalePowerDTO> storeSalePowerDTOListY = storeSalePowerDTOList.stream().filter(storeSalePowerDTO -> Integer.valueOf(1).equals(storeSalePowerDTO.getIsObtained())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(storeSalePowerDTOListN)) {
            Map<String, StoreSalePowerDTO> storeSalePowerDTOListYMap = storeSalePowerDTOListY.stream().collect(Collectors.toMap(StoreSalePowerDTO :: getSkuCode, Function.identity(), (v1, v2) -> v1));
            storeSalePowerDTOListN.forEach(item -> {
                if(!storeSalePowerDTOListYMap.containsKey(item.getSkuCode())) {
                    noStoreSalePowerList.add(item);
                }
            });
        }

        Set<String> respMsgSet = new HashSet<String>();
        if(CollectionUtils.isNotEmpty(noStoreSalePowerList)) {
            noStoreSalePowerList.forEach(item -> {
                String respMsg = "[" + item.getSkuCode() + "]";
                respMsgSet.add(respMsg);
            });
        }
        if(CollectionUtils.isNotEmpty(respMsgSet)) {
            throw new RomeException(ResCode.ORDER_ERROR_1001,ResCode.ORDER_ERROR_1001_DESC + "：商品编码" + StringUtils.join(respMsgSet, "") + "在门店[" + inShopCode + "]无销售权");
        }
    }


}

