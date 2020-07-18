package com.lyf.scm.core.domain.convert.pack;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.FrontRecordTypeEnum;
import com.lyf.scm.common.enums.RealWarehouseTypeEnum;
import com.lyf.scm.common.enums.YesOrNoEnum;
import com.lyf.scm.common.enums.pack.*;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.domain.entity.order.OrderDetailE;
import com.lyf.scm.core.domain.entity.order.OrderE;
import com.lyf.scm.core.domain.entity.pack.PackDemandComponentE;
import com.lyf.scm.core.domain.entity.pack.PackDemandDetailE;
import com.lyf.scm.core.domain.entity.pack.PackDemandE;
import com.lyf.scm.core.mapper.order.OrderMapper;
import com.lyf.scm.core.remote.base.dto.ChannelDTO;
import com.lyf.scm.core.remote.base.facade.BaseFacade;
import com.lyf.scm.core.remote.pack.dto.ObtainDemandGoodsDTO;
import com.lyf.scm.core.remote.pack.dto.ObtainOrderDTO;
import com.lyf.scm.core.remote.pack.dto.ObtainSkuDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.service.order.OrderService;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.rome.arch.core.exception.RomeException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Desc:需求单，预约单的转换
 * 下发包装系统的数据封装
 * @author:Huangyl
 * @date: 2020/7/7
 */
@Component
public class PackDemandPortalConvertor {

    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderService orderService;

    @Resource
    private OrderUtilService orderUtilService;

    @Resource
    private BaseFacade baseFacade;

    //需求单优先级
    private static final   Integer  priority = 1;
    /**
     * 根据预约单构建需求单对象
     * @param orderE
     * @return
     */
    public PackDemandE convertToPackDemandE(OrderE orderE){
        PackDemandE  packDemandE = new PackDemandE();
        //生成需求单号
        String recordCode = orderUtilService.queryOrderCode(FrontRecordTypeEnum.PACKAGE_NEED_RECORD.getCode());
        packDemandE.setRecordCode(recordCode);
        //自定义组合
        packDemandE.setPackType(PackTypeEnum.SELF_COMPOSE.getType());
        packDemandE.setSaleCode(orderE.getSaleCode());
        packDemandE.setIntroducer(orderE.getCustomName());
        packDemandE.setChannelCode(orderE.getChannelCode());
        packDemandE.setDemandDate(new Date());
        packDemandE.setInFactoryCode(orderE.getAllotFactoryCode());
        packDemandE.setInRealWarehouseCode(orderE.getAllotRealWarehouseCode());
        //包装仓ID = (入向实仓编码+ 入向工厂编码)
        RealWarehouse realWarehouseIn = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(packDemandE.getInRealWarehouseCode(),packDemandE.getInFactoryCode());
        AlikAssert.isNotNull(realWarehouseIn, ResCode.ORDER_ERROR_7613, ResCode.ORDER_ERROR_7613_DESC + packDemandE.getInRealWarehouseCode()+","+packDemandE.getInFactoryCode());
        packDemandE.setInRealWarehouseId(realWarehouseIn.getId());
        // 出虚仓编码 - 虚仓Code
        packDemandE.setOutVirtualWarehouseCode(orderE.getVmWarehouseCode());
        packDemandE.setOutFactoryCode(orderE.getFactoryCode());
        packDemandE.setOutRealWarehouseCode(orderE.getRealWarehouseCode());
        //领料仓ID = (出向工厂编码+ 出向实仓编码)
        RealWarehouse realWarehouseOut = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(packDemandE.getOutRealWarehouseCode(),packDemandE.getOutFactoryCode());
        AlikAssert.isNotNull(realWarehouseOut, ResCode.ORDER_ERROR_7614, ResCode.ORDER_ERROR_7614_DESC + packDemandE.getOutRealWarehouseCode()+","+packDemandE.getOutFactoryCode());
        packDemandE.setOutRealWarehouseId(realWarehouseOut.getId());
        packDemandE.setOutVirtualWarehouseCode(orderE.getVmWarehouseCode());
        packDemandE.setRecordStatus(DemandRecordStatusEnum.CONFIRMED.getStatus());
        packDemandE.setPickStatus( DemandPickStatusEnum.NOT_PICK.getStatus());
        packDemandE.setRemark(orderE.getRemark());
        packDemandE.setCreateTime(new Date());
        packDemandE.setUpdateTime(new Date());
        packDemandE.setPriority(priority);
        packDemandE.setCreateType(PackCreateTypeEnum.ORDER_CREATE.getType());
        packDemandE.setIsOut(YesOrNoEnum.NO.getType());
        //构建需求单明细对象
        packDemandE.setFinishProductDetail(this.convertToPackDemandDetailEList(orderE,packDemandE));
        //构建需求的那组件对象
        packDemandE.setComponentEList(this.convertToPackDemandComponentEList(packDemandE.getFinishProductDetail()));
        return  packDemandE;
    }

    /**
     * 根据预约单、需求单
     * 构建需求单明细对象
     * @param orderE
     * @param packDemandE
     * @return
     */
    public List<PackDemandDetailE> convertToPackDemandDetailEList(OrderE orderE, PackDemandE packDemandE){

         List<OrderDetailE> orderDetailES = orderService.queryOrderDetailsByCode(orderE.getOrderCode());
         if(CollectionUtils.isEmpty(orderDetailES)) {
             throw new RomeException(ResCode.ORDER_ERROR_7617, ResCode.ORDER_ERROR_7617_DESC);
         }
        return orderDetailES.stream().map((orderDetailE)->{
            PackDemandDetailE packDemandDetailE = new PackDemandDetailE();
            packDemandDetailE.setRecordCode(packDemandE.getRecordCode());
            // 自定义组合码-销售号
            packDemandDetailE.setCustomGroupCode(orderE.getSaleCode());
            packDemandDetailE.setSkuCode(orderDetailE.getSkuCode());
            // 成品名称-暂时无定义
            //packDemandDetailE.setSkuName();
            // 需求数量
            packDemandDetailE.setRequireQty(orderDetailE.getOrderQty());
            //组合份数 - 包装份数
            packDemandDetailE.setCompositeQty(new BigDecimal(orderE.getPackageNum()));
            packDemandDetailE.setUnit(orderDetailE.getUnit());
            packDemandDetailE.setUnitCode(orderDetailE.getUnitCode());
            packDemandDetailE.setCreateTime(new Date());
            packDemandDetailE.setUpdateTime(new Date());
            return packDemandDetailE;
        }).collect(Collectors.toList());
    }




    /**
     * 根据需求单明细
     * 构建需求单明细关联组件对象
     * @param packDemandDetailEList
     * @return
     */
    public List<PackDemandComponentE> convertToPackDemandComponentEList(List<PackDemandDetailE> packDemandDetailEList){
        return packDemandDetailEList.stream().map(packDemandDetailE -> {
            PackDemandComponentE packDemandComponentE = new PackDemandComponentE();
            packDemandComponentE.setRecordCode(packDemandDetailE.getRecordCode());
            packDemandComponentE.setParentSkuCode(packDemandDetailE.getCustomGroupCode());
            packDemandComponentE.setSkuCode(packDemandDetailE.getSkuCode());
            packDemandComponentE.setRequireQty(packDemandDetailE.getRequireQty());
            // BOM数量=需求数量/组成份数数量 (不保留小数，向上取整)
            packDemandComponentE.setBomQty(packDemandDetailE.getCompositeQty()== null || BigDecimal.ZERO.compareTo(packDemandDetailE.getCompositeQty()) >= 0 ?  BigDecimal.ZERO : packDemandDetailE.getRequireQty().divide(packDemandDetailE.getCompositeQty(),3,BigDecimal.ROUND_UP));
            //箱单位换算比率boxUnitRate（查商品主数据）
            packDemandComponentE.setUnit(packDemandDetailE.getUnit());
            packDemandComponentE.setUnitCode(packDemandDetailE.getUnitCode());
            packDemandComponentE.setMoveType(MoveTypeEnum.IN_STOCK.getType());
            // 是否领料isPick 默认是true
            packDemandComponentE.setIsPick(true);
            return  packDemandComponentE;
        }).collect(Collectors.toList());
    }

    /**
     * 构建包装系统-包装需求单
     * @param packDemandE
     * @return
     */
    public ObtainOrderDTO  convertObtainOrderDTO(PackDemandE packDemandE){

        ObtainOrderDTO obtainOrderDTO = new ObtainOrderDTO();
        obtainOrderDTO.setChannelCode(packDemandE.getChannelCode());
        //渠道名称
        List<ChannelDTO> channelDTOs = baseFacade.queryChannelInfoByChannelCodeList(Arrays.asList(packDemandE.getChannelCode()));
        AlikAssert.isNotEmpty(channelDTOs, ResCode.ORDER_ERROR_7611, ResCode.ORDER_ERROR_7611_DESC + packDemandE.getChannelCode());
        obtainOrderDTO.setChannelName(channelDTOs.get(0).getChannelName());
        obtainOrderDTO.setCreator(packDemandE.getCreator());
        // 客户 - 需求提出人
        obtainOrderDTO.setCustomerName(packDemandE.getIntroducer());
        obtainOrderDTO.setDemandNo(packDemandE.getRecordCode());
        // 需求完成日期 - 需求日期
        obtainOrderDTO.setFinishDate(packDemandE.getDemandDate());
        //领料状态 -
        obtainOrderDTO.setGetStatus(packDemandE.getPickStatus().toString());
        if (!StringUtils.isEmpty(packDemandE.getSaleCode())) {
            OrderE orderE = orderMapper.queryOrderBySaleCode(packDemandE.getSaleCode());
          //  AlikAssert.isNotNull(orderE, ResCode.ORDER_ERROR_7612, ResCode.ORDER_ERROR_7612_DESC+packDemandE.getSaleCode());
            if (orderE != null){
                obtainOrderDTO.setOrderCode(orderE.getOrderCode());
            }
        }
        //销售单号
        obtainOrderDTO.setSaleCode(packDemandE.getSaleCode());
        //订购日期-需求单创建时间
        obtainOrderDTO.setOrderDate(packDemandE.getCreateTime());
        obtainOrderDTO.setPackType(packDemandE.getPackType().toString());
        //包装仓 = (入向实仓编码+ 入向工厂编码)
        RealWarehouse realWarehouseIn = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(packDemandE.getInRealWarehouseCode(),packDemandE.getInFactoryCode());
        AlikAssert.isNotNull(realWarehouseIn, ResCode.ORDER_ERROR_7615, ResCode.ORDER_ERROR_7615_DESC + packDemandE.getInRealWarehouseCode()+","+packDemandE.getInFactoryCode());
        //判断是不是包装仓库
        if (!RealWarehouseTypeEnum.RW_TYPE_2.getType().equals(realWarehouseIn.getRealWarehouseType())){
            throw  new RomeException(ResCode.ORDER_ERROR_7637, ResCode.ORDER_ERROR_7637_DESC+ packDemandE.getRecordCode());
        }
        //包装车间编码
        obtainOrderDTO.setPackhouseCode(realWarehouseIn.getRealWarehouseCode());
        obtainOrderDTO.setPackhouseName(realWarehouseIn.getRealWarehouseName());
        obtainOrderDTO.setRemark(packDemandE.getRemark());
        //仓库编码–组合、反拆时需要 -- 领料
        //领料仓ID = (出向工厂编码+ 出向实仓编码)
        RealWarehouse realWarehouseOut = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(packDemandE.getOutRealWarehouseCode(),packDemandE.getOutFactoryCode());
        AlikAssert.isNotNull(realWarehouseOut,  ResCode.ORDER_ERROR_7616, ResCode.ORDER_ERROR_7616_DESC  +packDemandE.getOutRealWarehouseCode()+","+packDemandE.getOutFactoryCode());
        //仓库编码–组合、反拆时需要
        obtainOrderDTO.setWarehouseCode(realWarehouseOut.getRealWarehouseCode());
        obtainOrderDTO.setWarehouseName(realWarehouseOut.getRealWarehouseName());
        obtainOrderDTO.setCreator(packDemandE.getModifier());
        // 构建包装系统成品列表信息
        obtainOrderDTO.setGoodsList(this.convertObtainDemandGoodsDTOList(packDemandE));
        return obtainOrderDTO;
    }

    /**
     * 构建包装系统-构建组装，拆箱时的成品列表信息
     * @param packDemandE
     * @return
     */
    private List<ObtainDemandGoodsDTO> convertObtainDemandGoodsDTOList(PackDemandE packDemandE){

        // 需求明细
        List<PackDemandDetailE> detailEList = packDemandE.getFinishProductDetail();
        // 需求组件明细
        List<PackDemandComponentE> packDemandComponentEs = packDemandE.getComponentEList();

        //自定义组合- 自定义反拆
        if (PackTypeEnum.SELF_COMPOSE.getType().equals(packDemandE.getPackType()) || PackTypeEnum.UN_SELF_COMPOSE.getType().equals(packDemandE.getPackType()) ) {
            List<ObtainDemandGoodsDTO> list = new ArrayList<>();
            //针对自定义组合码去重
            Map<String, List<PackDemandDetailE>> collect = detailEList.stream().collect(Collectors.groupingBy(PackDemandDetailE::getCustomGroupCode));
            // 循环自定义组合键
            collect.forEach((customGroupCode, packDemandDetailEList) -> {
                PackDemandDetailE packDemandDetailE = packDemandDetailEList.get(0);
                ObtainDemandGoodsDTO obtainDemandGoodsDTO = convert2ODG(packDemandDetailE);
                obtainDemandGoodsDTO.setGoodsCode(packDemandDetailE.getCustomGroupCode());
                // 需求数量 - 组合数量
                obtainDemandGoodsDTO.setNeedAmount(packDemandDetailE.getCompositeQty());
                obtainDemandGoodsDTO.setPriority(packDemandE.getPriority());
                // 构建组件原料明细
                obtainDemandGoodsDTO.setSkuList(this.convertObtainSkuDTOList(packDemandComponentEs,customGroupCode));
                list.add(obtainDemandGoodsDTO);
            });
            return list;
        }
        // 组装，反拆，拆箱
        return  detailEList.stream().map((packDemandDetailE -> {
            ObtainDemandGoodsDTO obtainDemandGoodsDTO = convert2ODG(packDemandDetailE);
                obtainDemandGoodsDTO.setGoodsCode(packDemandDetailE.getSkuCode());
                // 需求数量 - 需求数量
                obtainDemandGoodsDTO.setNeedAmount(packDemandDetailE.getRequireQty());
                obtainDemandGoodsDTO.setPriority(packDemandE.getPriority());
                // 构建组件原料明细
                obtainDemandGoodsDTO.setSkuList(this.convertObtainSkuDTOList(packDemandComponentEs,packDemandDetailE.getSkuCode()));
           return obtainDemandGoodsDTO;
        })).collect(Collectors.toList());


    }

    /**
     * 封装obtainDemandGoodsDTO公共部分
     * @param packDemandDetailE
     * @return
     */
    private ObtainDemandGoodsDTO convert2ODG(PackDemandDetailE packDemandDetailE){
        ObtainDemandGoodsDTO obtainDemandGoodsDTO = new ObtainDemandGoodsDTO();
        obtainDemandGoodsDTO.setUnitCode(packDemandDetailE.getUnitCode());
        obtainDemandGoodsDTO.setGoodsName(packDemandDetailE.getSkuName());

        return obtainDemandGoodsDTO;

    }

    /**
     * 构建包装系统-组合、反拆时的sku列表信息
     * @param code
     * @return
     */
    private List<ObtainSkuDTO> convertObtainSkuDTOList(List<PackDemandComponentE> packDemandComponentEs,String code){

        return packDemandComponentEs.stream()
                .filter(packDemandComponentE -> code.equals(packDemandComponentE.getParentSkuCode()))
                .map( demandComponentE -> {
                    ObtainSkuDTO obtainSkuDTO = new ObtainSkuDTO();
                    obtainSkuDTO.setBomAmount(demandComponentE.getBomQty());
                    obtainSkuDTO.setBomSkuCode(demandComponentE.getSkuCode());
                    obtainSkuDTO.setUnit(demandComponentE.getUnit());
                    //需求数量
                    obtainSkuDTO.setNeedAmount(demandComponentE.getRequireQty());
                    return obtainSkuDTO;
                }
        ).collect(Collectors.toList());
    }


//    public static void main(String[] args) {
//
//        List<String> list = Arrays.asList("a","b","c");
//        PackDemandComponentE p = new PackDemandComponentE();
//        p.set
//
//
//
//    }
}
