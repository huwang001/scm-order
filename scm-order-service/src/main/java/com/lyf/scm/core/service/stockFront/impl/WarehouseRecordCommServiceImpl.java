package com.lyf.scm.core.service.stockFront.impl;

import com.alibaba.fastjson.JSON;
import com.lyf.scm.common.constants.DFConstants;
import com.lyf.scm.common.constants.KibanaLogConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.constants.WhAllocationConstants;
import com.lyf.scm.common.enums.FrontRecordTypeEnum;
import com.lyf.scm.common.enums.WarehouseRecordTypeEnum;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.stockFront.CommonFrontRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.CommonFrontRecordDetailDTO;
import com.lyf.scm.core.config.ServiceKibanaLog;
import com.lyf.scm.core.domain.convert.disparity.DisparityConvertor;
import com.lyf.scm.core.domain.convert.disparity.DisparityDetailConvert;
import com.lyf.scm.core.domain.convert.shopReturn.ShopReturnConvert;
import com.lyf.scm.core.domain.convert.stockFront.*;
import com.lyf.scm.core.domain.entity.disparity.DisparityDetailE;
import com.lyf.scm.core.domain.entity.disparity.DisparityRecordE;
import com.lyf.scm.core.domain.entity.shopReturn.ShopReturnDetailE;
import com.lyf.scm.core.domain.entity.shopReturn.ShopReturnE;
import com.lyf.scm.core.domain.entity.stockFront.*;
import com.lyf.scm.core.mapper.disparity.DisparityDetailMapper;
import com.lyf.scm.core.mapper.disparity.DisparityRecordMapper;
import com.lyf.scm.core.mapper.shopReturn.ShopReturnDetailMapper;
import com.lyf.scm.core.mapper.shopReturn.ShopReturnMapper;
import com.lyf.scm.core.mapper.stockFront.*;
import com.lyf.scm.core.message.CancelWarehouseRecordProducer;
import com.lyf.scm.core.remote.stock.dto.CancelRecordDTO;
import com.lyf.scm.core.service.dto.RecordReceiveDTO;
import com.lyf.scm.core.service.stockFront.WarehouseRecordCommService;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhanlong
 */
@Slf4j
@Service("warehouseRecordCommService")
public class WarehouseRecordCommServiceImpl implements WarehouseRecordCommService {

    @Resource
    private CancelWarehouseRecordProducer cancelWarehouseRecordProducer;
    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;
    @Resource
    private ReplenishRecordMapper replenishRecordMapper;
    @Resource
    private WhAllocationMapper whAllocationMapper;
    @Resource
    private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;
    @Resource
    private ReplenishRecordDetailMapper replenishRecordDetailMapper;
    @Resource
    private ReplenishRecordConvert replenishRecordConvert;
    @Resource
    private ReplenishDetailConvert replenishDetailConvert;
    @Resource
    private WhAllocationConvertor whAllocationConvertor;
    @Resource
    private WhAllocationDetailConvertor whAllocationDetailConvertor;
    @Resource
    private WhAllocationDetailMapper whAllocationDetailMapper;
    @Resource
    private DisparityRecordMapper disparityRecordMapper;
    @Resource
    private DisparityDetailMapper disparityDetailMapper;
    @Resource
    private DisparityConvertor disparityConvertor;
    @Resource
    private DisparityDetailConvert disparityDetailConvert;
    @Resource
    private WarehouseRecordDetailMapper warehouseRecordDetailMapper;
    @Resource
    private ShopAllocationMapper shopAllocationMapper;
    @Resource
    private ShopAllocationRecordConvertor shopAllocationRecordConvertor;
    @Resource
    private ShopAllocationDetailMapper shopAllocationDetailMapper;
    @Resource
    private ShopReturnMapper shopReturnMapper;
    @Resource
    private ShopReturnDetailMapper shopReturnDetailMapper;
    @Resource
    private ShopReturnConvert shopReturnConvert;

    @Override
    public void cancelWarehouseRecordToStock(CancelRecordDTO cancelRecord) {
        String recordCode = cancelRecord.getRecordCode();
        boolean result = false;
        try {
            cancelWarehouseRecordProducer.sendAsyncMQ(cancelRecord);
            log.info("取消出入库单-发送消息成功，后置单据号：{}", recordCode);
            result = true;
        } catch (Exception e) {
            log.info("取消出入库单-发送消息失败，后置单据号：{}，异常信息：{}", recordCode, e);
        } finally {
            if (!result) {
                String msg = String.format("取消出入库单-发送消息失败，后置单据号：%s， 请求参数：%s", recordCode, JSON.toJSONString(cancelRecord));
                log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.ROLL_BACK_EXCEPTION, "cancelWarehouseRecord", msg, cancelRecord));
            }
        }
    }

    @Override
    public List<CommonFrontRecordDTO> queryCommonFrontRecordInfoByRecordCode(String doCode) {
        //查询关联关系
        List<FrontWarehouseRecordRelationE> recordRelationEList = frontWarehouseRecordRelationMapper.getFrontRelationByRecordCode(doCode);
        List<CommonFrontRecordDTO> commonFrontRecordDTOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(recordRelationEList)) {
            throw new RomeException(ResCode.ORDER_ERROR_5123, ResCode.ORDER_ERROR_5123_DESC);
        }

        for (FrontWarehouseRecordRelationE frontWarehouseRecordRelationE : recordRelationEList) {
            Integer recordType = frontWarehouseRecordRelationE.getFrontRecordType();
            // sc_fr_replenish_record（内采）
            if (FrontRecordTypeEnum.SHOP_REPLENISHMENT_RECORD.getType().equals(recordType)
                    || FrontRecordTypeEnum.SHOP_COLD_CHAIN_DELIVERY_RECORD.getType().equals(recordType)
                    || FrontRecordTypeEnum.SHOP_SUPPLIER_DIRECT_DELIVERY_RECORD.getType().equals(recordType)
                    || FrontRecordTypeEnum.JOIN_SHOP_REPLENISHMENT_RECORD.getType().equals(recordType)) {
                //获取内采详情
                ReplenishRecordE replenishRecordE = replenishRecordMapper.queryReplenishRecordById(frontWarehouseRecordRelationE.getFrontRecordId());
                AlikAssert.isNotNull(replenishRecordE, ResCode.ORDER_ERROR_5106, ResCode.ORDER_ERROR_5106_DESC);
                CommonFrontRecordDTO commonFrontRecordDTO = replenishRecordConvert.convertE2CommDTO(replenishRecordE);
//                    commonFrontRecordDTO.setOutWarehouseId(replenishRecordE.getOutRealWarehouseId());//手动set出向仓库id
//                    commonFrontRecordDTO.setInWarehouseId(replenishRecordE.getInRealWarehouseId());//手动set入向仓库id
                //获取内采明细
                List<ReplenishDetailE> replenishDetailEList = replenishRecordDetailMapper.queryDetailByRecordId(replenishRecordE.getId());
                List<CommonFrontRecordDetailDTO> commonFrontRecordDetailDTOList = replenishDetailConvert.convertETOCommDTO(replenishDetailEList);
                commonFrontRecordDTO.setFrontRecordDetails(commonFrontRecordDetailDTOList);
                commonFrontRecordDTOList.add(commonFrontRecordDTO);
            } else if (FrontRecordTypeEnum.WAREHOUSE_ALLOCATION_RECORD.getType().equals(recordType)) {
                // sc_fr_wh_allocation 仓库调拨
                WhAllocationE whAllocationE = whAllocationMapper.queryById(frontWarehouseRecordRelationE.getFrontRecordId());
                AlikAssert.isNotNull(whAllocationE, ResCode.ORDER_ERROR_6001, ResCode.ORDER_ERROR_6001_DESC);
                CommonFrontRecordDTO commonFrontRecordDTO = whAllocationConvertor.convertE2CommDTO(whAllocationE);
                //获取调拨单明细
                List<WhAllocationDetailE> whAllocationDetailEList = whAllocationDetailMapper.queryDetailByFrontIdsOrderBySkuQty(whAllocationE.getId());
                List<CommonFrontRecordDetailDTO> commonFrontRecordDetailDTOList = whAllocationDetailConvertor.convertEList2CommDTOList(whAllocationDetailEList);
                commonFrontRecordDTO.setFrontRecordDetails(commonFrontRecordDetailDTOList);
                commonFrontRecordDTOList.add(commonFrontRecordDTO);
            } else if(FrontRecordTypeEnum.DIRECT_SHOP_RETURN_DISPARITY_RECORD.getType().equals(recordType)
                    || FrontRecordTypeEnum.DIRECT_SHOP_REPLENISH_DISPARITY_RECORD.getType().equals(recordType)) {
                // sc_fr_wh_allocation 直营门店补货差异、直营门店退货差异
                DisparityRecordE disparityRecordE = disparityRecordMapper.selectDisparityRecordById(frontWarehouseRecordRelationE.getFrontRecordId());
                AlikAssert.isNotNull(disparityRecordE, ResCode.ORDER_ERROR_1001, "差异单不存在"+doCode);
                CommonFrontRecordDTO commonFrontRecordDTO = disparityConvertor.convertE2CommDTO(disparityRecordE);
                //查询差异明细
                List<DisparityDetailE> disparityDetailES = disparityDetailMapper.selectDisparityDetailByDisparityId(frontWarehouseRecordRelationE.getFrontRecordId());
                //查询后置单明细
                List<WarehouseRecordDetailE> warehouseRecordDetailEList = warehouseRecordDetailMapper.queryListByRecordCode(frontWarehouseRecordRelationE.getRecordCode());
                List<String> deliveryLineNoList = warehouseRecordDetailEList.stream().map(WarehouseRecordDetailE::getDeliveryLineNo).distinct().collect(Collectors.toList());
                List<DisparityDetailE> partDisparityDetailES = disparityDetailES.stream().filter(r -> deliveryLineNoList.contains(r.getId())).collect(Collectors.toList());
                List<CommonFrontRecordDetailDTO> commonFrontRecordDetailDTOList = disparityDetailConvert.convertEList2CommDTOList(partDisparityDetailES);
                commonFrontRecordDTO.setFrontRecordDetails(commonFrontRecordDetailDTOList);
                commonFrontRecordDTOList.add(commonFrontRecordDTO);
            }else if(FrontRecordTypeEnum.SHOP_ALLOCATION_RECORD.getType().equals(recordType)) {
                //sc_fr_shop_allocation 门店调拨
                ShopAllocationE shopAllocationE = shopAllocationMapper.queryFrontRecordByCode(frontWarehouseRecordRelationE.getFrontRecordCode());
                AlikAssert.isNotNull(shopAllocationE, ResCode.ORDER_ERROR_7307, ResCode.ORDER_ERROR_7307_DESC);
                List<ShopAllocationDetailE> detailES = shopAllocationDetailMapper.queryShopAllocationDetailList(shopAllocationE.getId());
                shopAllocationE.setFrontRecordDetails(detailES);
                CommonFrontRecordDTO commonFrontRecordDTO = shopAllocationRecordConvertor.convertE2CommonRecordDTO(shopAllocationE);
                commonFrontRecordDTOList.add(commonFrontRecordDTO);
            }else if(FrontRecordTypeEnum.DIRECT_SHOP_RETURN_GOODS_RECORD.getType().equals(recordType)
                    || FrontRecordTypeEnum.JOIN_SHOP_RETURN_GOODS_RECORD.getType().equals(recordType)) {
                //scm_shop_return 门店退货
                WarehouseRecordE warehouseRecordE = warehouseRecordMapper.queryByRecordCode(doCode);
                AlikAssert.isNotNull(warehouseRecordE, ResCode.ORDER_ERROR_6026, ResCode.ORDER_ERROR_6026_DESC);
                //查询前置单
                ShopReturnE shopReturnE = shopReturnMapper.selectByRecordCode(frontWarehouseRecordRelationE.getFrontRecordCode());
                AlikAssert.isNotNull(shopReturnE, ResCode.ORDER_ERROR_7801, ResCode.ORDER_ERROR_7801_DESC);
                CommonFrontRecordDTO commonFrontRecordDTO = shopReturnConvert.convert2CommonRecord(shopReturnE);
                //门店退货--冷链 入库仓设置
                if(WarehouseRecordTypeEnum.DS_RETURN_COLD_OUT_WAREHOUSE_RECORD.getType().equals(warehouseRecordE.getRecordType())
                        || WarehouseRecordTypeEnum.DS_RETURN_COLD_IN_WAREHOUSE_RECORD.getType().equals(warehouseRecordE.getRecordType())
                        || WarehouseRecordTypeEnum.LS_RETURN_COLD_OUT_WAREHOUSE_RECORD.getType().equals(warehouseRecordE.getRecordType())
                        || WarehouseRecordTypeEnum.LS_RETURN_COLD_IN_WAREHOUSE_RECORD.getType().equals(warehouseRecordE.getRecordType())) {
                    commonFrontRecordDTO.setInFactoryCode(shopReturnE.getInColdFactoryCode());
                    commonFrontRecordDTO.setInRealWarehouseCode(shopReturnE.getInColdRealWarehouseCode());
                }
                //后置单明细
                List<WarehouseRecordDetailE> whRecordDetailList = warehouseRecordDetailMapper.queryListByRecordCode(warehouseRecordE.getRecordCode());
                //前置单明细
                List<ShopReturnDetailE> shopReturnDetailEList = shopReturnDetailMapper.selectByRecordCode(shopReturnE.getRecordCode());
                List<String> deliveryLineNoList = whRecordDetailList.stream().map(WarehouseRecordDetailE::getDeliveryLineNo).distinct().collect(Collectors.toList());
                //过滤掉非当前后置单明细对应的前置单明细
                List<ShopReturnDetailE> currentWhRecordDetailList = shopReturnDetailEList.stream()
                        .filter(r -> deliveryLineNoList.contains(String.valueOf(r.getId()))).collect(Collectors.toList());
                List<CommonFrontRecordDetailDTO> frontRecordDetails = shopReturnConvert.convert2DetailList(currentWhRecordDetailList);
                frontRecordDetails.forEach(detail ->{
                    detail.setFrontRecordId(shopReturnE.getId());
                    detail.setSapPoNo(shopReturnE.getSapPoNo());
                });
                commonFrontRecordDTO.setFrontRecordDetails(frontRecordDetails);
                commonFrontRecordDTOList.add(commonFrontRecordDTO);
            }
        }
        return commonFrontRecordDTOList;
    }

    @Override
    public String queryRecordReceiveCode(String recordCode, String type) {
        if (StringUtils.isEmpty(recordCode)) {
            log.info("单据编号【{}】为空", recordCode);
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        if (StringUtils.isBlank(type)) {
            log.info("wms业务类型【{}】为空", type);
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        //查询后置单（出库单）
        WarehouseRecordE warehouseRecordE = warehouseRecordMapper.queryByRecordCode(recordCode);
        if (null == warehouseRecordE) {
            throw new RomeException(ResCode.ORDER_ERROR_5123, ResCode.ORDER_ERROR_5123_DESC);
        }
        //查询和前置单关系
        List<FrontWarehouseRecordRelationE> frontRelationList = frontWarehouseRecordRelationMapper.getFrontRelationByRecordCode(recordCode);
        AlikAssert.isNotEmpty(frontRelationList, ResCode.ORDER_ERROR_5120, ResCode.ORDER_ERROR_5120_DESC);
        FrontWarehouseRecordRelationE recordRelationE = frontRelationList.get(0);

        //查询发送方信息
        RecordReceiveDTO receiveDTO = queryWarehouseRecordReceiveInfo(recordRelationE);
        String receiveCode = "";
        switch (type) {
            case DFConstants.DF_SHOP:
                // 门店
                receiveCode = "_" + receiveDTO.getShopCode();
                break;
            case DFConstants.DF_REPLENISH:
                // 加盟商
                receiveCode = receiveDTO.getShopCode();
                break;
            case DFConstants.DF_ALLOCATION:
                // 调拨出库
                if (receiveDTO.getAllocationType() != null && receiveDTO.getAllocationType().equals(WhAllocationConstants.RDC_ALLOCATION)) {
                    // RDC调拨出库（40）
                    receiveCode = "000000" + receiveDTO.getInFactoryCode();
                } else {
                    // 内部调拨出库（60）
                    receiveCode = receiveDTO.getInRealWarehouseCode();
                }
                break;
            default:
                log.info("单据【{}】大福类型不在预计中，不作处理", recordCode);
                break;
        }
        if(StringUtils.isEmpty(receiveCode)) {
            throw new RomeException(ResCode.ORDER_ERROR_5124, ResCode.ORDER_ERROR_5124_DESC);
        }
        return receiveCode;
    }

    /**
     * 根据前置单类型 查询送达方信息
     *
     * @param recordRelationE
     * @return
     */
    private RecordReceiveDTO queryWarehouseRecordReceiveInfo(FrontWarehouseRecordRelationE recordRelationE) {
        RecordReceiveDTO receive = new RecordReceiveDTO();
        Integer frontRecordType = recordRelationE.getFrontRecordType();
        // sc_fr_replenish_record（内采）
        if (FrontRecordTypeEnum.SHOP_REPLENISHMENT_RECORD.getType().equals(frontRecordType)
                || FrontRecordTypeEnum.SHOP_COLD_CHAIN_DELIVERY_RECORD.getType().equals(frontRecordType)
                || FrontRecordTypeEnum.SHOP_SUPPLIER_DIRECT_DELIVERY_RECORD.getType().equals(frontRecordType)
                || FrontRecordTypeEnum.JOIN_SHOP_REPLENISHMENT_RECORD.getType().equals(frontRecordType)) {
            ReplenishRecordE recordE = replenishRecordMapper.queryReplenishRecordById(recordRelationE.getFrontRecordId());
            if (recordE != null) {
                receive.setShopCode(recordE.getShopCode());
                receive.setInFactoryCode(recordE.getInFactoryCode());
                receive.setInRealWarehouseCode(recordE.getInRealWarehouseCode());
            }
        } else if (FrontRecordTypeEnum.WAREHOUSE_ALLOCATION_RECORD.getType().equals(frontRecordType)) {
            // sc_fr_wh_allocation 仓库调拨
            WhAllocationE recordE = whAllocationMapper.queryById(recordRelationE.getFrontRecordId());
            if (recordE != null) {
                receive.setAllocationType(recordE.getBusinessType());
                receive.setInFactoryCode(recordE.getInFactoryCode());
                receive.setInRealWarehouseCode(recordE.getInRealWarehouseCode());
            }
        }
        return receive;
    }
}
