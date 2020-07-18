package com.lyf.scm.core.service.order.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.OrderStatusEnum;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.order.OrderVwMoveDTO;
import com.lyf.scm.core.api.dto.order.OrderVwMoveDetailDTO;
import com.lyf.scm.core.domain.convert.order.OrderVwMoveDetailConvert;
import com.lyf.scm.core.domain.entity.order.OrderDetailE;
import com.lyf.scm.core.domain.entity.order.OrderE;
import com.lyf.scm.core.domain.entity.order.OrderVwMoveDetailE;
import com.lyf.scm.core.domain.entity.order.OrderVwMoveE;
import com.lyf.scm.core.domain.model.OrderDetailDO;
import com.lyf.scm.core.mapper.order.OrderVwMoveDetailMapper;
import com.lyf.scm.core.mapper.order.OrderVwMoveMapper;
import com.lyf.scm.core.remote.item.dto.SkuInfoExtDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.stock.dto.QueryVirtualWarehouseDTO;
import com.lyf.scm.core.remote.stock.dto.SkuInfoForVw;
import com.lyf.scm.core.remote.stock.dto.SkuStockForVw;
import com.lyf.scm.core.remote.stock.dto.VirtualWarehouse;
import com.lyf.scm.core.remote.stock.dto.VwDetailDTO;
import com.lyf.scm.core.remote.stock.dto.VwMoveRecordDTO;
import com.lyf.scm.core.remote.stock.facade.StockFacade;
import com.lyf.scm.core.remote.stock.facade.StockQueryFacade;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.service.order.OrderService;
import com.lyf.scm.core.service.order.OrderVwMoveService;
import com.rome.arch.core.exception.RomeException;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: OrderVwMove
 * <p>
 * @Author: chuwenchao  2020/4/8
 */
@Slf4j
@Service("orderVwMoveService")
public class OrderVwMoveServiceImpl implements OrderVwMoveService {

    @Resource
    private OrderVwMoveMapper orderVwMoveMapper;
    @Resource
    private OrderVwMoveDetailMapper orderVwMoveDetailMapper;
    @Resource
    private StockFacade stockFacade;
    @Resource
    private StockQueryFacade stockQueryFacade;
    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;
    @Resource
    private ItemFacade itemFacade;
    @Resource
    private OrderService orderService;
    @Resource
    private OrderVwMoveDetailConvert orderVwMoveDetailConvert;

    /**
     * @Description: 通过工厂编码和仓库编码查询虚仓列表 <br>
     *
     * @Author chuwenchao 2020/4/8
     * @param factoryCode
     * @param realWarehouseOutCode
     * @return
     */
    @Override
    public List<VirtualWarehouse> queryVmListByCodes(String factoryCode, String realWarehouseOutCode) {
    	QueryVirtualWarehouseDTO queryVirtualWarehouseDTO = new QueryVirtualWarehouseDTO();
    	queryVirtualWarehouseDTO.setRealWarehouseOutCode(realWarehouseOutCode);
    	queryVirtualWarehouseDTO.setFactoryCode(factoryCode);
        return stockRealWarehouseFacade.queryVwByRealWarehouseCode(queryVirtualWarehouseDTO);
    }

    /**
     * @Description: 查询预约单待虚仓调拨Sku明细 <br>
     *
     * @Author chuwenchao 2020/4/9
     * @param code
     * @param orderCode
     * @return
     */
    @Override
    public List<OrderVwMoveDetailDTO> queryNeedOrderVmMoveInfo(String orderCode, String vwWarehouseCode) {
        // 校验预约单及明细是否存在
        OrderE orderE = orderService.queryOrderByCode(orderCode);
        AlikAssert.isNotNull(orderE, ResCode.ORDER_ERROR_1004, ResCode.ORDER_ERROR_1004_DESC + orderCode);
        AlikAssert.isNotBlank(orderE.getVmWarehouseCode(), ResCode.ORDER_ERROR_2001,ResCode.ORDER_ERROR_2001_DESC + orderCode);
        List<OrderDetailE> orderDetailES = orderService.queryOrderDetailsByCode(orderCode);
        AlikAssert.isNotEmpty(orderDetailES, ResCode.ORDER_ERROR_1007, ResCode.ORDER_ERROR_1007 + orderCode);
        List<String> skuCodes = orderDetailES.stream().map(OrderDetailDO::getSkuCode).collect(Collectors.toList());
        // 查询商品信息
        List<SkuInfoExtDTO> skuInfoExtDTOS = itemFacade.skuListBySkuCodes(skuCodes);
        Map<String, String> skuInfoExtDTOMap = skuInfoExtDTOS.stream().collect(Collectors.toMap(SkuInfoExtDTO::getSkuCode, SkuInfoExtDTO::getName));
        // 查询虚仓库存
        List<SkuInfoForVw> skuInfoList = new ArrayList<>();
        for(OrderDetailE orderDetailE : orderDetailES) {
            orderDetailE.setSkuName(skuInfoExtDTOMap.get(orderDetailE.getSkuCode()));
            SkuInfoForVw skuInfoForVw = new SkuInfoForVw();
            skuInfoForVw.setVirtualWarehouseCode(vwWarehouseCode);
            skuInfoForVw.setSkuCode(orderDetailE.getSkuCode());
            skuInfoForVw.setUnitCode(orderDetailE.getUnitCode());
            skuInfoList.add(skuInfoForVw);
        }
        List<SkuStockForVw> skuStockForVwS = stockQueryFacade.queryVmListByVmGroupCodes(skuInfoList);
        Map<String, BigDecimal> skuStockForVwMap = skuStockForVwS.stream().collect(Collectors.toMap(SkuStockForVw::getSkuCode, SkuStockForVw::getAvailableQty));
        // 构造返回对象
        List<OrderVwMoveDetailDTO> resultDTO = new ArrayList<>();
        this.buildResultDTO(resultDTO, orderDetailES, skuStockForVwMap);
        return resultDTO;
    }

    /**
     * @Description: 构造虚仓调拨明细返回对象 <br>
     *
     * @Author chuwenchao 2020/4/10
     * @param resultDTO
     * @return 
     */
    private void buildResultDTO(List<OrderVwMoveDetailDTO> resultDTO, List<OrderDetailE> orderDetailES, Map<String, BigDecimal> skuStockForVwMap) {
        for(OrderDetailE orderDetailE : orderDetailES) {
            OrderVwMoveDetailDTO temp = new OrderVwMoveDetailDTO();
            temp.setId(orderDetailE.getId());
            temp.setSkuCode(orderDetailE.getSkuCode());
            temp.setSkuName(orderDetailE.getSkuName());
            temp.setOrderQty(orderDetailE.getOrderQty());
            temp.setRequireQty(orderDetailE.getRequireQty());
            temp.setHasLockQty(orderDetailE.getHasLockQty());
            temp.setVmStockQty(skuStockForVwMap.get(orderDetailE.getSkuCode()));
            temp.setNeedMoveQty(orderDetailE.getRequireQty().subtract(orderDetailE.getHasLockQty()));
            temp.setMoveQty(this.findMin(temp.getNeedMoveQty(), skuStockForVwMap.get(orderDetailE.getSkuCode())));
            temp.setUnit(orderDetailE.getUnit());
            temp.setUnitCode(orderDetailE.getUnitCode());
            resultDTO.add(temp);
        }
    }

    /**
     * @Description: 两个数相比取小 <br>
     *
     * @Author chuwenchao 2020/4/10
     * @param requireQty
     * @return 
     */
    private BigDecimal findMin(BigDecimal requireQty, BigDecimal stockQty) {
        if(requireQty == null) {
            requireQty = BigDecimal.ZERO;
        }
        if(stockQty == null) {
            stockQty = BigDecimal.ZERO;
        }
        if(requireQty.compareTo(stockQty) > 0) {
            return stockQty;
        }
        return requireQty;
    }

    /**
     * @Description: 保存预约单虚仓调拨信息 <br>
     *
     * @Author chuwenchao 2020/4/9
     * @param orderVwMoveDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNeedOrderVmMoveInfo(OrderVwMoveDTO orderVwMoveDTO) {
        // 虚仓调拨明细
        List<OrderVwMoveDetailDTO> detailDTOList = orderVwMoveDTO.getDetailDTOList();
        List<OrderVwMoveDetailDTO> newDetailDTOList = detailDTOList.stream().filter(r -> r.getMoveQty().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        AlikAssert.isNotEmpty(newDetailDTOList, ResCode.ORDER_ERROR_2003, ResCode.ORDER_ERROR_2003_DESC + orderVwMoveDTO.getOrderCode());
        Map<String, OrderVwMoveDetailDTO> newDetailDTOMap = newDetailDTOList.stream().collect(Collectors.toMap(OrderVwMoveDetailDTO::getSkuCode, Function.identity()));
        // 查预约单及明细
        OrderE orderE = orderService.queryOrderByCode(orderVwMoveDTO.getOrderCode());
        AlikAssert.isNotNull(orderE, ResCode.ORDER_ERROR_1004, ResCode.ORDER_ERROR_1004_DESC + orderVwMoveDTO.getOrderCode());
        AlikAssert.isTrue(OrderStatusEnum.LOCK_STATUS_PART.getStatus().equals(orderE.getOrderStatus()), ResCode.ORDER_ERROR_1013, ResCode.ORDER_ERROR_1013_DESC + orderVwMoveDTO.getOrderCode());
        List<OrderDetailE> orderDetailES = orderService.queryOrderDetailsByCode(orderVwMoveDTO.getOrderCode());
        AlikAssert.isNotEmpty(orderDetailES, ResCode.ORDER_ERROR_1007, ResCode.ORDER_ERROR_1007 + orderVwMoveDTO.getOrderCode());
        // 查虚仓
        List<VirtualWarehouse> virtualWarehouseList = this.queryVmListByCodes(orderE.getFactoryCode(), orderE.getRealWarehouseCode());
        Map<String, VirtualWarehouse> virtualWarehouseMap = virtualWarehouseList.stream().collect(Collectors.toMap(VirtualWarehouse::getVirtualWarehouseCode, Function.identity(), (v1, v2) -> v1));
        VirtualWarehouse outVirtualWarehouse = virtualWarehouseMap.get(orderVwMoveDTO.getOutVwWarehouseCode());
        AlikAssert.isNotNull(outVirtualWarehouse, ResCode.ORDER_ERROR_2002, ResCode.ORDER_ERROR_2002_DESC + orderVwMoveDTO.getOutVwWarehouseCode());
        // 构造调用库存接口对象
        VwMoveRecordDTO vwMoveRecordDTO = new VwMoveRecordDTO();
        vwMoveRecordDTO.setInVirtualWarehouseCode(orderE.getVmWarehouseCode());
        vwMoveRecordDTO.setOutVirtualWarehouseCode(orderVwMoveDTO.getOutVwWarehouseCode());
        List<VwDetailDTO> virtualWarehouseSku = new ArrayList<>();
        vwMoveRecordDTO.setVirtualWarehouseSkus(virtualWarehouseSku);
        vwMoveRecordDTO.setOrderCode(orderE.getOrderCode());
        // 校验虚仓转移数量
        List<OrderVwMoveDetailE> detailEList = new ArrayList<>();
        for(OrderDetailE orderDetailE : orderDetailES) {
            OrderVwMoveDetailDTO vwMoveDetailDTO = newDetailDTOMap.get(orderDetailE.getSkuCode());
            VwDetailDTO vwDetailDTO = new VwDetailDTO();
            vwDetailDTO.setSkuCode(orderDetailE.getSkuCode());
            BigDecimal needMoveQty = orderDetailE.getRequireQty().subtract(orderDetailE.getHasLockQty());
            if(vwMoveDetailDTO != null) {
                if(vwMoveDetailDTO.getMoveQty().compareTo(needMoveQty) > 0) {
                    log.info("预约单【{}】移库失败，商品【{}】实际转移数量【{}】不能大于需要转移数量【{}】", orderVwMoveDTO.getOrderCode(), vwMoveDetailDTO.getSkuCode(), vwMoveDetailDTO.getMoveQty(), needMoveQty);
                    throw new RomeException(ResCode.ORDER_ERROR_2004, ResCode.ORDER_ERROR_2004_DESC);
                }
                vwDetailDTO.setSkuQty(vwMoveDetailDTO.getMoveQty());
                vwDetailDTO.setSkuUnitCode(vwMoveDetailDTO.getUnitCode());
            } else if(needMoveQty.compareTo(BigDecimal.ZERO) > 0)  {
                vwDetailDTO.setSkuQty(needMoveQty);
            } else {
                continue;
            }
            virtualWarehouseSku.add(vwDetailDTO);
            OrderVwMoveDetailE vwMoveDetailE = new OrderVwMoveDetailE();
            vwMoveDetailE.setSkuCode(orderDetailE.getSkuCode());
            vwMoveDetailE.setMoveQty(vwDetailDTO.getSkuQty());
            vwMoveDetailE.setUnit(orderDetailE.getUnit());
            vwMoveDetailE.setUnitCode(orderDetailE.getUnitCode());
            detailEList.add(vwMoveDetailE);
        }
        // 调用库存接口生成虚仓调拨单
        String vmMoveCode = stockFacade.saveVirtualWarehouseMoveRecord(vwMoveRecordDTO);
        // 保存虚仓调拨信息
        this.saveOrderVmMove(orderVwMoveDTO, detailEList, orderE, vmMoveCode);
    }

    /**
     * @Description: 保存虚仓转移及明细 <br>
     *
     * @Author chuwenchao 2020/4/13
     * @param orderVwMoveDTO
     * @param vwMoveDetailEList
     * @return
     */
    private void saveOrderVmMove(OrderVwMoveDTO orderVwMoveDTO, List<OrderVwMoveDetailE> vwMoveDetailEList, OrderE orderE, String vmMoveCode) {
        // 保存虚仓转移
        OrderVwMoveE orderVwMoveE = new OrderVwMoveE();
        orderVwMoveE.setVwMoveCode(vmMoveCode);
        orderVwMoveE.setOrderCode(orderE.getOrderCode());
        orderVwMoveE.setFactoryCode(orderE.getFactoryCode());
        orderVwMoveE.setRealWarehouseCode(orderE.getRealWarehouseCode());
        orderVwMoveE.setInVwWarehouseCode(orderE.getVmWarehouseCode());
        orderVwMoveE.setOutVwWarehouseCode(orderVwMoveDTO.getOutVwWarehouseCode());
        orderVwMoveE.setCreator(orderVwMoveDTO.getCreator());
        orderVwMoveMapper.saveOrderVwMove(orderVwMoveE);
        //构造转移明细对象
        for(OrderVwMoveDetailE vwMoveDetailE: vwMoveDetailEList) {
            vwMoveDetailE.setVwMoveCode(vmMoveCode);
        }
        orderVwMoveDetailMapper.batchSaveOrderVwMoveDetail(vwMoveDetailEList);
    }

}
