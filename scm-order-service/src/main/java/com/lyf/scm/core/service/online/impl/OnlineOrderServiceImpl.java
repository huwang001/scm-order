package com.lyf.scm.core.service.online.impl;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.OnlineStockTransTypeEnum;
import com.lyf.scm.common.model.ValidResult;
import com.lyf.scm.common.util.validate.ParamValidator;
import com.lyf.scm.core.api.dto.online.OnlineOrderDTO;
import com.lyf.scm.core.domain.convert.online.AddressConvertor;
import com.lyf.scm.core.domain.entity.online.AddressE;
import com.lyf.scm.core.domain.entity.online.RecordPoolE;
import com.lyf.scm.core.domain.entity.online.SaleDetailE;
import com.lyf.scm.core.domain.entity.online.SaleE;
import com.lyf.scm.core.remote.stock.dto.CoreChannelOrderDTO;
import com.lyf.scm.core.remote.stock.dto.CoreOrderDetailDO;
import com.lyf.scm.core.remote.stock.dto.CoreVirtualStockOpDO;
import com.lyf.scm.core.service.online.AddressService;
import com.lyf.scm.core.service.online.OnlineOrderService;
import com.lyf.scm.core.service.online.RecordPoolService;
import com.lyf.scm.core.service.online.SaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("onlineOrderService")
public class OnlineOrderServiceImpl implements OnlineOrderService {

    @Value("${app.merchantId}")
    private Long merchantId;
    @Resource
    private SaleService saleService;
    @Resource
    private AddressService addressService;
    @Resource
    private RecordPoolService recordPoolService;
    @Resource
    private AddressConvertor addressConvertor;

    ParamValidator validator = ParamValidator.INSTANCE;

    @Override
    public void lockStockByRecord(OnlineOrderDTO stockOrderDTO) {
//        // 必要参数校验
//        ValidResult validResult = validOnlineAddOrderParam(stockOrderDTO);
//        if (null != validResult) {
//            throw new RomeException(validResult.getResCode(), validResult.getResDesc());
//        }
//        if (saleService.judgeExistByOutRecordCode(stockOrderDTO.getOrderCode())) {
//            //单据已存在，直接返回
//            return;
//        }
//        boolean isVirtual = OnlineStockTransTypeEnum.TRANS_TYPE_4.getTransType().equals(stockOrderDTO.getTransType()) ||
//                OnlineStockTransTypeEnum.TRANS_TYPE_12.getTransType().equals(stockOrderDTO.getTransType());
//        // 入电商销售前置单表 + 明细表
//        SaleE saleE = saleService.addSaleFrontRecord(stockOrderDTO);
//
//        // 入sc_address收货地址信息表
//        AddressE addressE = null;
//        if (!isVirtual) {
//            //非虚拟订单
//            addressE = addressConvertor.orderDtoToAddressEntity(stockOrderDTO);
//            addressE.setUserType((byte) 0);
//            addressE.setAddressType((byte) 0);
//            addressE.setRecordCode(saleE.getRecordCode());
//            addressService.addAddressInfo(addressE);
//        }
//        // 寻源 + 锁库存
//        CoreChannelOrderDTO cco = null;
//        boolean isSuccess = false;
//        try {
//            cco = warpRouteAndLockStockDO(saleE, addressE);
//            List<CoreVirtualStockOpDO> cvsList = null;
//            if (stockOrderDTO.getRealWarehouseId() != null) {
//                //指定仓库下单
//                saleE.setRealWarehouseId(stockOrderDTO.getRealWarehouseId());
//                //包装指定虚仓和实仓锁库存的入参
//                cvsList = warpAssignHouseRoute(saleE);
//                cco.setVirtualStockOpDetailDOs(cvsList);
//
////                coreChannelSalesRepository.lockStock(cco);
//            } else {
////                cvsList = StockOnlineOrderFacade.routeAndLockStock(cco);
//            }
//            AlikAssert.notEmpty(cvsList, ResCode.STOCK_ERROR_5024, ResCode.STOCK_ERROR_5024_DESC);
//            // 根据寻源结果拆单
//            splitOnlineOrder(cvsList, stockOrderDTO, saleE);
//            isSuccess = true;
//        } catch (RomeException e) {
//            log.error(e.getMessage(), e);
//            throw e;
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            throw new RomeException(ResCode.STOCK_ERROR_5024, ResCode.STOCK_ERROR_5024_DESC);
//        } finally {
//            if (!isSuccess && null != cco) {
////                RedisRollBackFacade.redisRollBack(cco);
//            }
//        }
    }

    /**
     * 根据寻源结果 拆分 子-do单
     *
     * @param cvsList
     * @param stockOrderDTO
     * @param saleE
     */
    private void splitOnlineOrder(List<CoreVirtualStockOpDO> cvsList, OnlineOrderDTO stockOrderDTO, SaleE saleE) {
        Map<Long, List<CoreVirtualStockOpDO>> rwIdMap = cvsList.stream().collect(Collectors.groupingBy(cvs -> cvs.getRealWarehouseId()));
        OnlineStockTransTypeEnum transTypeVO = OnlineStockTransTypeEnum.getByTransType(stockOrderDTO.getTransType());
        int needCombine = merchantId.equals(stockOrderDTO.getMerchantId()) ? 1 : 0;
        boolean isVirtual = false;
        if (OnlineStockTransTypeEnum.TRANS_TYPE_7.equals(transTypeVO)) {
            //自营外卖，无需合单,目前外卖的不会走到这里来
            needCombine = 0;
        } else {
//            List<RealWarehouseWmsConfigDO> configDOS = realWarehouseWmsConfigMapper.findRealWarehouseWmsConfigByIds(new ArrayList<>(rwIdMap.keySet()));
//            if (configDOS == null || configDOS.size() < rwIdMap.keySet().size()) {
//                throw new RomeException(ResCode.STOCK_ERROR_1002, "寻源仓库不在仓库配置表");
//            }
//            int wmsCode = configDOS.get(0).getWmsCode();
//            if (WmsConfigConstants.WMS_VIRTUAL == wmsCode) {
//                //虚拟订单无需合单
//                needCombine = 0;
//                isVirtual = true;
//            } else if (WmsConfigConstants.WMS_MERCHANT == wmsCode) {
//                needCombine = 0;
//            }
        }
        for (List<CoreVirtualStockOpDO> mapValue : rwIdMap.values()) {
            CoreVirtualStockOpDO master = mapValue.get(0);
            //保存子do单和明细
            RecordPoolE poolE = recordPoolService.saveRecordPool(saleE, needCombine, master.getRealWarehouseId(), master.getVirtualWarehouseId(), mapValue);
            if (!isVirtual) {
                //地址信息在do上各存一份，后面有可能修改do的地址信息
                AddressE addr = addressConvertor.orderDtoToAddressEntity(stockOrderDTO);
                addr.setUserType((byte) 0);
                addr.setAddressType((byte) 0);
                addr.setRecordCode(poolE.getDoCode());
                addressService.addAddressInfo(addr);
            }
        }
    }

    /**
     * 包装寻源+锁库存接口参数
     */
    private CoreChannelOrderDTO warpRouteAndLockStockDO(SaleE frontRecord, AddressE addressE) {
        CoreChannelOrderDTO coreChannelOrderDO = new CoreChannelOrderDTO();
        //库存交易类型
        coreChannelOrderDO.setRecordCode(frontRecord.getRecordCode());
        coreChannelOrderDO.setTransType(frontRecord.getRecordType());
        coreChannelOrderDO.setMerchantId(frontRecord.getMerchantId());
        coreChannelOrderDO.setChannelCode(frontRecord.getChannelCode());
        if (addressE != null) {
            coreChannelOrderDO.setCityCode(addressE.getCityCode());
        }
        List<CoreOrderDetailDO> details = new ArrayList<>();
        coreChannelOrderDO.setOrderDetailDOs(details);
        CoreOrderDetailDO detailDO;
        for (SaleDetailE detailE : frontRecord.getFrontRecordDetails()) {
            detailDO = new CoreOrderDetailDO();
            detailDO.setLockQty(detailE.getBasicSkuQty());
            detailDO.setSkuId(detailE.getSkuId());
            detailDO.setSkuCode(detailE.getSkuCode());
            details.add(detailDO);
        }
        return coreChannelOrderDO;
    }

    private List<CoreVirtualStockOpDO> warpAssignHouseRoute(SaleE onlineRetailE) {
        List<CoreVirtualStockOpDO> cvsList = new ArrayList<>();
        //根据指定的实仓跟渠道code查询虚仓信息
        Long vmId = null;
//        ChannelSalesE channelSalesE = channelSalesRepository.queryByChannelCode(onlineRetailE.getChannelCode());
//        AlikAssert.isNotNull(channelSalesE,ResCode.STOCK_ERROR_5028,ResCode.STOCK_ERROR_5028_DESC);
//        List<VirtualWarehouseE> vwList = virtualWarehouseRepository.queryByRealWarehouseId(onlineRetailE.getRealWarehouseId());
//        for(VirtualWarehouseE virtualWarehouseE : vwList){
//            if(channelSalesE.getVirtualWarehouseGroupId().equals(virtualWarehouseE.getVirtualWarehouseGroupId())){
//                vmId = virtualWarehouseE.getId();
//                break;
//            }
//        }
//        AlikAssert.isNotNull(vmId, ResCode.STOCK_ERROR_1041, ResCode.STOCK_ERROR_1041_DESC +
//                "rwId=" + onlineRetailE.getRealWarehouseId() + ",groupId=" + channelSalesE.getVirtualWarehouseGroupId());
        for (SaleDetailE detailE : onlineRetailE.getFrontRecordDetails()) {
            CoreVirtualStockOpDO coreStockDO = new CoreVirtualStockOpDO();
            coreStockDO.setLockQty(detailE.getBasicSkuQty());
            coreStockDO.setVirtualWarehouseId(vmId);
            coreStockDO.setRealWarehouseId(onlineRetailE.getRealWarehouseId());
            coreStockDO.setRecordCode(onlineRetailE.getRecordCode());
            coreStockDO.setTransType(onlineRetailE.getRecordType());
            coreStockDO.setChannelCode(onlineRetailE.getChannelCode());
            coreStockDO.setMerchantId(onlineRetailE.getMerchantId());
            coreStockDO.setSkuId(detailE.getSkuId());
            coreStockDO.setSkuCode(detailE.getSkuCode());
            cvsList.add(coreStockDO);
        }
        return cvsList;
    }

    /**
     * 校验电商下单锁库存接口参数
     *
     * @return 校验结果
     */
    private ValidResult validOnlineAddOrderParam(OnlineOrderDTO stockOrderDTO) {
        if (null == stockOrderDTO) {
            return new ValidResult(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
//        if (!(OnlineStockTransTypeEnum.TRANS_TYPE_4.getTransType().equals(stockOrderDTO.getTransType()) ||
//                OnlineStockTransTypeEnum.TRANS_TYPE_12.getTransType().equals(stockOrderDTO.getTransType()))) {
//            //虚拟商品不校验地址相关参数
//            if (StringUtils.isBlank(stockOrderDTO.getAddress())) {
//                return new ValidResult(ResCode.STOCK_ERROR_5012, ResCode.STOCK_ERROR_5012_DESC);
//            }
//            if (StringUtils.isBlank(stockOrderDTO.getMobile())) {
//                return new ValidResult(ResCode.STOCK_ERROR_5005, ResCode.STOCK_ERROR_5005_DESC);
//            }
//            if (StringUtils.isBlank(stockOrderDTO.getProvince())) {
//                return new ValidResult(ResCode.STOCK_ERROR_5006, ResCode.STOCK_ERROR_5006_DESC);
//            }
//            if (StringUtils.isBlank(stockOrderDTO.getProvinceCode())) {
//                return new ValidResult(ResCode.STOCK_ERROR_5007, ResCode.STOCK_ERROR_5007_DESC);
//            }
//            if (StringUtils.isBlank(stockOrderDTO.getCity())) {
//                return new ValidResult(ResCode.STOCK_ERROR_5008, ResCode.STOCK_ERROR_5008_DESC);
//            }
//            if (StringUtils.isBlank(stockOrderDTO.getCityCode())) {
//                return new ValidResult(ResCode.STOCK_ERROR_5009, ResCode.STOCK_ERROR_5009_DESC);
//            }
//            if (StringUtils.isBlank(stockOrderDTO.getName())) {
//                return new ValidResult(ResCode.STOCK_ERROR_5013, ResCode.STOCK_ERROR_5013_DESC);
//            }
//        }
//        if (OnlineStockTransTypeEnum.TRANS_TYPE_7.getTransType().equals(stockOrderDTO.getTransType())) {
//            if (stockOrderDTO.getExpectReceiveDateStart() == null) {
//                return new ValidResult(ResCode.STOCK_ERROR_5030, ResCode.STOCK_ERROR_5030_DESC + "：自营外卖期望发货时间必传");
//            }
//        }
//        if (StringUtils.isBlank(stockOrderDTO.getChannelCode())) {
//            return new ValidResult(ResCode.STOCK_ERROR_5001, ResCode.STOCK_ERROR_5001_DESC);
//        }
//        if (StringUtils.isBlank(stockOrderDTO.getOrderCode())) {
//            return new ValidResult(ResCode.STOCK_ERROR_5003, ResCode.STOCK_ERROR_5003_DESC);
//        }
//        if (null == stockOrderDTO.getMerchantId()) {
//            return new ValidResult(ResCode.STOCK_ERROR_5004, ResCode.STOCK_ERROR_5004_DESC);
//        }
//        if (null == stockOrderDTO.getFrontRecordDetails() || stockOrderDTO.getFrontRecordDetails().isEmpty()) {
//            return new ValidResult(ResCode.STOCK_ERROR_5014, ResCode.STOCK_ERROR_5014_DESC);
//        }
//        for (OnlineOrderDetailDTO orderDetailDTO : stockOrderDTO.getFrontRecordDetails()) {
//            if (!validator.validPositiveLong(orderDetailDTO.getSkuId()) ||
//                    !validator.validStr(orderDetailDTO.getSkuCode()) ||
//                    !validator.validPositiveBigDecimal(orderDetailDTO.getSkuQty()) ||
//                    !validator.validStr(orderDetailDTO.getUnitCode())) {
//                return new ValidResult(ResCode.STOCK_ERROR_5027, ResCode.STOCK_ERROR_5027_DESC);
//            }
//        }
        return null;
    }
}
