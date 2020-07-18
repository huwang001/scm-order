package com.lyf.scm.core.service.order.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.KibanaLogConstants;
import com.lyf.scm.common.constants.OrderConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.FrontRecordTypeEnum;
import com.lyf.scm.common.enums.OrderStatusEnum;
import com.lyf.scm.common.enums.RealWarehouseTypeEnum;
import com.lyf.scm.common.enums.SkuUnitTypeEnum;
import com.lyf.scm.common.util.Snowflake;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.notify.StockNotifyDTO;
import com.lyf.scm.core.api.dto.order.*;
import com.lyf.scm.core.api.dto.pack.DemandFromSoDTO;
import com.lyf.scm.core.config.ServiceKibanaLog;
import com.lyf.scm.core.domain.convert.order.OrderConvert;
import com.lyf.scm.core.domain.convert.order.OrderDetailConvert;
import com.lyf.scm.core.domain.convert.stockFront.StockRecordDTOConvert;
import com.lyf.scm.core.domain.entity.common.RecordStatusLogE;
import com.lyf.scm.core.domain.entity.order.OrderDetailE;
import com.lyf.scm.core.domain.entity.order.OrderE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.domain.entity.stockFront.WhAllocationE;
import com.lyf.scm.core.mapper.common.RecordStatusLogMapper;
import com.lyf.scm.core.mapper.order.OrderDetailMapper;
import com.lyf.scm.core.mapper.order.OrderMapper;
import com.lyf.scm.core.mapper.stockFront.FrontWarehouseRecordRelationMapper;
import com.lyf.scm.core.remote.item.ItemInfoTool;
import com.lyf.scm.core.remote.item.SkuQtyUnitTool;
import com.lyf.scm.core.remote.item.dto.ParamExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuInfoExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuUnitExtDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.stock.dto.*;
import com.lyf.scm.core.remote.stock.facade.StockFacade;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.remote.stock.facade.StockRecordFacade;
import com.lyf.scm.core.remote.trade.dto.DoDTO;
import com.lyf.scm.core.remote.trade.facade.TradeFacade;
import com.lyf.scm.core.service.order.OrderService;
import com.lyf.scm.core.service.order.OrderToWareHouseRecordService;
import com.lyf.scm.core.service.pack.PackDemandService;
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
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * com.lyf.scm.core.service
 *
 * @author zhangxu
 * @date 2020/4/9
 */
@Slf4j
@Service("orderService")
public class OrderServiceImpl implements OrderService {

	@Resource
    private  OrderMapper orderMapper;
	
	@Resource
    private OrderDetailMapper orderDetailMapper;
	
	@Resource
    private RecordStatusLogMapper recordStatusLogMapper;
	
	@Resource
    private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;
	
	@Resource
    private OrderToWareHouseRecordService orderToWareHouseRecordService;
	
	@Resource
    private OrderConvert orderConvert;
	
	@Resource
    private OrderDetailConvert orderDetailConvert;
	
	@Resource
    private StockRecordDTOConvert stockRecordDTOConvert;
	
	@Resource
    private ItemFacade itemFacade;
	
	@Resource
    private StockFacade stockFacade;
	
	@Resource
    private TradeFacade tradeFacade;
	
	@Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;
	
	@Resource
    private StockRecordFacade stockRecordFacade;
	
	@Resource
    private SkuQtyUnitTool skuQtyUnitTool;
	
	@Resource
    private ItemInfoTool itemInfoTool;

    @Resource
    private PackDemandService packDemandService;

	@Value("${app.merchantId}")
    private Long merchantId;
	
	@Value("${app.deliveryWarehouseType}")
    private Integer deliveryWarehouseType;
	
	

    /**
     * 接收预约单
     *
     * @param tradeOrderDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receiveTradeOrder(TradeOrderDTO tradeOrderDTO) {
        // 销售单号幂等校验
        OrderE exist = orderMapper.findBySaleCode(tradeOrderDTO.getSaleCode());
        if (exist != null) {
            // 销售单据存在
            throw new RomeException(ResCode.ORDER_ERROR_1015, ResCode.ORDER_ERROR_1015_DESC);
        }

        List<TradeOrderDTO.TradeOrderDetailDTO> tradeOrderDetailDTOS = tradeOrderDTO.getOrderDetails();
        if (CollectionUtils.isEmpty(tradeOrderDetailDTOS)) {
            // 校验预约单明细
            throw new RomeException(ResCode.ORDER_ERROR_1007, ResCode.ORDER_ERROR_1007_DESC);
        }

        // 汇集商品code
        List<String> skuCodeList = tradeOrderDetailDTOS.stream().map(TradeOrderDTO.TradeOrderDetailDTO::getSkuCode).collect(Collectors.toList());
        if (skuCodeList.size() != tradeOrderDetailDTOS.size()) {
            // 校验预约单明细重复行
            throw new RomeException(ResCode.ORDER_ERROR_1029, ResCode.ORDER_ERROR_1029_DESC);
        }
        // 商品信息校验
        List<SkuInfoExtDTO> skuInfoExts = itemFacade.skuListBySkuCodes(skuCodeList);
        if (skuInfoExts.size() != skuCodeList.size()) {
            // 存在有的商品不存在
            throw new RomeException(ResCode.ORDER_ERROR_1018, ResCode.ORDER_ERROR_1018_DESC);
        }

        // 调用商品中心接口查询sku的单位信息
        List<ParamExtDTO> paramExtDTO = tradeOrderDetailDTOS.stream().map(d -> {
            ParamExtDTO paramExt = new ParamExtDTO();
            paramExt.setMerchantId(merchantId);
            paramExt.setSkuCode(d.getSkuCode());
            return paramExt;
        }).collect(Collectors.toList());
        List<SkuUnitExtDTO> skuUnitExts = itemFacade.unitsBySkuCodeAndMerchantId(paramExtDTO);

        // 校验商品的单位是否为基础单位
        Map<String, String> skuBasisUnitExtsMap = skuUnitExts.stream()
                .filter(u -> u.getType().equals(SkuUnitTypeEnum.BASIS_UNIT.getId()))
                .collect(Collectors.toMap(SkuUnitExtDTO::getSkuCode, SkuUnitExtDTO::getUnitCode));
        if (skuCodeList.size() != skuBasisUnitExtsMap.size()) {
            throw new RomeException(ResCode.ORDER_ERROR_1019, ResCode.ORDER_ERROR_1019_DESC);
        }
        for (TradeOrderDTO.TradeOrderDetailDTO tradeOrderDetailDTO : tradeOrderDetailDTOS) {
            // 校验所有商品的单位是否是基本单位
            String unitCode = skuBasisUnitExtsMap.get(tradeOrderDetailDTO.getSkuCode());
            if (!unitCode.equals(tradeOrderDetailDTO.getUnitCode())) {
                throw new RomeException(ResCode.ORDER_ERROR_1019, ResCode.ORDER_ERROR_1019_DESC);
            }
        }

        // 查询商品发货单位及转换比例
        Map<String, SkuUnitExtDTO> skuTransportUnitExtsMap = skuUnitExts.stream()
                .filter(u -> u.getType().equals(SkuUnitTypeEnum.TRANSPORT_UNIT.getId()))
                .collect(Collectors.toMap(SkuUnitExtDTO::getSkuCode, v -> v));

        // 组装预约单及预约单明细并且保存数据库
        OrderE orderE = new OrderE();
        // 生成预约单号
        orderE.setOrderCode("YYD" + Snowflake.getInstanceSnowflake().nextId());
        orderE.setOrderStatus(OrderConstants.INIT_STATUS);
        BeanUtils.copyProperties(tradeOrderDTO, orderE);
        List<OrderDetailE> orderDetailEs = tradeOrderDetailDTOS.stream().map(detail -> {
            OrderDetailE orderDetailE = new OrderDetailE();
            BeanUtils.copyProperties(detail, orderDetailE);
            SkuUnitExtDTO skuUnitExtDTO = skuTransportUnitExtsMap.get(detail.getSkuCode());
            orderDetailE.setDeliveryUnitCode(skuUnitExtDTO.getUnitCode());
            orderDetailE.setOrderCode(orderE.getOrderCode());
            orderDetailE.setScale(skuUnitExtDTO.getScale());
            // 需求锁定数量，根据下单数据按照发货单位向上取整
            BigDecimal requireQty = NumberUtil.div(orderDetailE.getOrderQty(), orderDetailE.getScale(), 0, RoundingMode.UP);
            // 需求锁定数量，再转成基本单位数量
            orderDetailE.setRequireQty(NumberUtil.mul(orderDetailE.getScale(), requireQty));
            orderDetailE.setHasLockQty(BigDecimal.ZERO);
            return orderDetailE;
        }).collect(Collectors.toList());

        // 保存预约单及明细数据
        int row = orderMapper.insertOrder(orderE);
        if (row != 1) {
            throw new RomeException(ResCode.ORDER_ERROR_1030, ResCode.ORDER_ERROR_1030_DESC);
        }
        row = orderDetailMapper.batchInsertOrderDetail(orderDetailEs);
        if (row != orderDetailEs.size()) {
            throw new RomeException(ResCode.ORDER_ERROR_1031, ResCode.ORDER_ERROR_1031_DESC);
        }
    }

    /**
     * 接收交易中心可发货的通知
     *
     * @param saleCode
     */
    @Override
    public void receiveTradeDeliveryNotice(String saleCode) {
        OrderE orderE = orderMapper.findBySaleCode(saleCode);
        if (orderE == null) {
            throw new RomeException(ResCode.ORDER_ERROR_1004, ResCode.ORDER_ERROR_1004_DESC);
        }

        // 更新预约单交易审核状态为审核通过
        int row = orderMapper.updateTradeAuditStatusPassedByOrderCode(orderE.getOrderCode());
        if (row != 1) {
            throw new RomeException(ResCode.ORDER_ERROR_1021, ResCode.ORDER_ERROR_1021_DESC);
        }
    }

    @Override
    public int notifyTradeAfterLocked(OrderDTO orderDTO) {
        // 调用交易中心通知完全锁定
        tradeFacade.stockLockedNotify(orderDTO.getSaleCode());
        // 修改同步交易状态为2
        return orderMapper.updateSyncTradeStatusLockDoneByOrderCode(orderDTO.getOrderCode());
    }

    /**
     * 查询预约单
     *
     * @param orderCode
     */
    @Override
    public OrderE queryOrderByCode(String orderCode) {
        OrderE orderE = orderMapper.findByOrderCode(orderCode);
        return orderE;
    }

    /**
     * 查询预约单明细
     *
     * @param orderCode
     */
    @Override
    public List<OrderDetailE> queryOrderDetailsByCode(String orderCode) {
        List<OrderDetailE> detailEList = orderDetailMapper.findByOrderCode(orderCode);
        return detailEList;
    }

    @Override
    public void completeProcess(String orderCode, Long userId) {
        OrderE orderE = orderMapper.findByOrderCode(orderCode);
        if (orderE == null) {
            throw new RomeException(ResCode.ORDER_ERROR_1004, ResCode.ORDER_ERROR_1004_DESC);
        }
        if (orderE.getNeedPackage().equals(OrderConstants.NO_NEED_PACKAGE)) {
            throw new RomeException(ResCode.ORDER_ERROR_1016, ResCode.ORDER_ERROR_1016_DESC);
        }
        if (!orderE.getOrderStatus().equals(OrderConstants.ALLOT_STATUS_IN)) {
            throw new RomeException(ResCode.ORDER_ERROR_1017, ResCode.ORDER_ERROR_1017_DESC);
        }

        // 更新预约单状态为已加工
        int row = orderMapper.updateOrderStatusProcessCompletedByOrderCode(orderCode, userId);
        if (row != 1) {
            throw new RomeException(ResCode.ORDER_ERROR_1014, ResCode.ORDER_ERROR_1014_DESC);
        }

        // 保存单据流转状态
        RecordStatusLogE recordStatusLogE = new RecordStatusLogE();
        recordStatusLogE.setOrderCode(orderCode);
        recordStatusLogE.setRecordStatus(OrderConstants.PROCESS_STATUS_DONE);
        recordStatusLogMapper.insertRecordStatusLog(recordStatusLogE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lockSeondary(String orderCode) {
    	OrderE orderE = orderMapper.findByOrderCode(orderCode);
        if (orderE == null) {
            throw new RomeException(ResCode.ORDER_ERROR_1004, ResCode.ORDER_ERROR_1004_DESC);
        }
        if (StringUtils.isBlank(orderE.getFactoryCode()) || StringUtils.isBlank(orderE.getRealWarehouseCode())
                && StringUtils.isBlank(orderE.getVmWarehouseCode())) {
            throw new RomeException(ResCode.ORDER_ERROR_1037, ResCode.ORDER_ERROR_1037_DESC);
        }
        boolean isSucc = false;
        try {
	        // 调库存中心接口根据预约单号锁库存
	        ReservationDTO reservationOutDTO = stockFacade.lockReservationByRecordCode(orderE.getOrderCode());
        
			List<ReservationDTO.ReservationDetailDTO> reservationDetailOutDTOS = reservationOutDTO.getReservationDetails();
			Map<String, ReservationDTO.ReservationDetailDTO> reservationDetailOutDTOMap = reservationDetailOutDTOS.stream().collect(Collectors.toMap(ReservationDTO.ReservationDetailDTO::getSkuCode, v -> v));

			List<OrderDetailE> orderDetailEList = orderDetailMapper.findByOrderCode(orderE.getOrderCode());

			// 设置预约单明细已锁定数量及锁定状态
			boolean allLockFlag = true;
			for (OrderDetailE orderDetailE : orderDetailEList) {
			    ReservationDTO.ReservationDetailDTO reservationDetailOutDTO = reservationDetailOutDTOMap.get(orderDetailE.getSkuCode());
			    orderDetailE.setHasLockQty(reservationDetailOutDTO.getAssignedQty());
			    if (reservationDetailOutDTO.getAssignedQty().compareTo(orderDetailE.getRequireQty()) < 0) {
			        // 已锁定数量小于需求锁定数量
			    	orderDetailE.setLockStatus(OrderConstants.LOCK_STATUS_PART);
			        allLockFlag = false;
			    } else {
			    	orderDetailE.setLockStatus(OrderConstants.LOCK_STATUS_ALL);
			    }
			}

			// 预约单及预约单明细更新数据库
			// 更新预约单及明细数据
			int row = orderMapper.updateLockStatus(orderE.getId(), allLockFlag ? OrderConstants.LOCK_STATUS_ALL : OrderConstants.LOCK_STATUS_PART, orderE.getVersionNo());
			if (row < 1) {
			    throw new RomeException(ResCode.ORDER_ERROR_1032, ResCode.ORDER_ERROR_1032_DESC);
			}
			row = orderDetailMapper.batchUpdateOrderDetail(orderDetailEList);
			if (row != orderDetailEList.size()) {
			    throw new RomeException(ResCode.ORDER_ERROR_1033, ResCode.ORDER_ERROR_1033_DESC);
			}

			// 如果全部锁定，修改同步交易状态为1
			if (allLockFlag) {
			    row = orderMapper.updateSyncTradeStatusLockWaitByOrderCode(orderE.getOrderCode());
			    if (row != 1) {
			        throw new RomeException(ResCode.ORDER_ERROR_1027, ResCode.ORDER_ERROR_1027_DESC);
			    }
			}

			// 保存单据流转状态
			RecordStatusLogE recordStatusLogE = new RecordStatusLogE();
			recordStatusLogE.setOrderCode(orderE.getOrderCode());
			recordStatusLogE.setRecordStatus(allLockFlag ? OrderConstants.LOCK_STATUS_ALL : OrderConstants.LOCK_STATUS_PART);
			recordStatusLogMapper.insertRecordStatusLog(recordStatusLogE);
			isSucc = true;
		} catch (Exception e) {
			throw e;
		} finally {
			if(!isSucc) {
				try {
					//调用库存中心取消预约单，释放锁定库存
					stockFacade.cancelOrder(orderE.getOrderCode());
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.CANCEL_ORDER, "cancelOrder", "取消预约单：" + orderE.getOrderCode(), orderE.getOrderCode()));

				}
			}
		}
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lockInventory(String recordCode) {
    	OrderE orderE = orderMapper.queryByOrderCode(recordCode);
    	if(orderE == null) {
    		throw new RomeException(ResCode.ORDER_ERROR_1004, ResCode.ORDER_ERROR_1004_DESC);
    	}
        // 是否有虚仓编码
        if (StringUtils.isNotBlank(orderE.getFactoryCode()) && StringUtils.isNotBlank(orderE.getRealWarehouseCode())
                && StringUtils.isNotBlank(orderE.getVmWarehouseCode())) {
            this.lockSeondary(orderE.getOrderCode());
        } else {
            ReservationDTO reservationInDTO = new ReservationDTO();
            reservationInDTO.setAddress(orderE.getCustomAddress());
            reservationInDTO.setBusinessType(orderE.getOrderType());
            reservationInDTO.setChannelCode(orderE.getChannelCode());
            reservationInDTO.setCity(orderE.getCityName());
            reservationInDTO.setCityCode(orderE.getCityCode());
            reservationInDTO.setCounty(orderE.getCountyName());
            reservationInDTO.setCountyCode(orderE.getCountyCode());
            reservationInDTO.setMobile(orderE.getCustomMobile());
            reservationInDTO.setName(orderE.getCustomName());
            reservationInDTO.setUserCode(orderE.getCustomName());
            reservationInDTO.setOutRecordCode(orderE.getOrderCode());
            reservationInDTO.setProvince(orderE.getProvinceName());
            reservationInDTO.setProvinceCode(orderE.getProvinceCode());
            reservationInDTO.setOutCreateTime(DateUtil.format(orderE.getCreateTime(), DatePattern.NORM_DATETIME_PATTERN));
            reservationInDTO.setExpectReceiveDateStart(orderE.getExpectDate());

            List<OrderDetailE> orderDetailEList = orderDetailMapper.findByOrderCode(orderE.getOrderCode());
            List<ReservationDTO.ReservationDetailDTO> reservationDetailInDTOS = orderDetailEList.stream().map(detail -> {
                ReservationDTO.ReservationDetailDTO reservationDetailInDTO = new ReservationDTO.ReservationDetailDTO();
                // 锁库给需求锁定数量和基本单位
                reservationDetailInDTO.setSkuCode(detail.getSkuCode());
                reservationDetailInDTO.setSkuQty(detail.getRequireQty());
                reservationDetailInDTO.setUnitCode(detail.getUnitCode());
                return reservationDetailInDTO;
            }).collect(Collectors.toList());
            reservationInDTO.setReservationDetails(reservationDetailInDTOS);
            boolean isSucc = false;
            try {
	            // 调库存中心创建预约单锁库存
	            ReservationDTO reservationOutDTO = stockFacade.createReservation(reservationInDTO);
            
				List<ReservationDTO.ReservationDetailDTO> reservationDetailOutDTOS = reservationOutDTO.getReservationDetails();
				Map<String, ReservationDTO.ReservationDetailDTO> reservationDetailOutDTOMap = reservationDetailOutDTOS.stream().collect(Collectors.toMap(ReservationDTO.ReservationDetailDTO::getSkuCode, v -> v));

				// 设置预约单明细已锁定数量及锁定状态
				boolean allLockFlag = true;
				for (OrderDetailE orderDetailE : orderDetailEList) {
				    ReservationDTO.ReservationDetailDTO reservationDetailOutDTO = reservationDetailOutDTOMap.get(orderDetailE.getSkuCode());
				    orderDetailE.setHasLockQty(reservationDetailOutDTO.getAssignedQty());
				    if (reservationDetailOutDTO.getAssignedQty().compareTo(orderDetailE.getRequireQty()) < 0) {
				        // 已锁定数量小于需求锁定数量
				    	orderDetailE.setLockStatus(OrderConstants.LOCK_STATUS_PART);
				        allLockFlag = false;
				    } else {
				    	orderDetailE.setLockStatus(OrderConstants.LOCK_STATUS_ALL);
				    }
				}

				OrderE newOrderE = new OrderE();
				newOrderE.setOrderCode(orderE.getOrderCode());
				// 设置预约单工厂code和仓库code
				newOrderE.setFactoryCode(reservationOutDTO.getRealFactoryCode());
				newOrderE.setRealWarehouseCode(reservationOutDTO.getRealWarehouseCode());
				newOrderE.setVmWarehouseCode(reservationOutDTO.getVirtualWarehouseCode());
				// 预约单及预约单明细更新数据库
				// 更新预约单及明细数据
				int row1 = orderMapper.updateOrder(newOrderE);
				if (row1 < 1) {
				    throw new RomeException(ResCode.ORDER_ERROR_1032, ResCode.ORDER_ERROR_1032_DESC);
				}
				int row2 = orderMapper.updateLockStatus(orderE.getId(), allLockFlag ? OrderConstants.LOCK_STATUS_ALL : OrderConstants.LOCK_STATUS_PART, orderE.getVersionNo());
				if (row2 < 1) {
				    throw new RomeException(ResCode.ORDER_ERROR_1032, ResCode.ORDER_ERROR_1032_DESC);
				}
				int row3 = orderDetailMapper.batchUpdateOrderDetail(orderDetailEList);
				if (row3 != orderDetailEList.size()) {
				    throw new RomeException(ResCode.ORDER_ERROR_1033, ResCode.ORDER_ERROR_1033_DESC);
				}

				// 如果全部锁定，修改同步交易状态为1
				if (allLockFlag) {
				    int row4 = orderMapper.updateSyncTradeStatusLockWaitByOrderCode(orderE.getOrderCode());
				    if (row4 < 1) {
				        throw new RomeException(ResCode.ORDER_ERROR_1027, ResCode.ORDER_ERROR_1027_DESC);
				    }
				}

				// 保存单据流转状态
				RecordStatusLogE recordStatusLogE = new RecordStatusLogE();
				recordStatusLogE.setOrderCode(orderE.getOrderCode());
				recordStatusLogE.setRecordStatus(allLockFlag ? OrderConstants.LOCK_STATUS_ALL : OrderConstants.LOCK_STATUS_PART);
				recordStatusLogMapper.insertRecordStatusLog(recordStatusLogE);
				isSucc = true;
			} catch (Exception e) {
				throw e;
			} finally {
				if(!isSucc) {
					try {
						//调用库存中心取消预约单，释放锁定库存
						stockFacade.cancelOrder(orderE.getOrderCode());
					} catch (Exception e) {
						log.error(e.getMessage(), e);
						log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.CANCEL_ORDER, "cancelOrder", "取消预约单：" + orderE.getOrderCode(), orderE.getOrderCode()));

					}
				}
			}
        }
    }

    /**
     * 通知交易中心发货状态
     */
    @Override
    public int notifyTradeDeliveryStatus(OrderDTO orderDTO) {
        List<DoDTO> doDTOList = new ArrayList<>();
        DoDTO doDTO = new DoDTO();
        doDTO.setDoNo(orderDTO.getSaleCode());

        List<OrderDetailE> orderDetailEList = orderDetailMapper.findByOrderCode(orderDTO.getOrderCode());
        List<DoDTO.DoItemDTO> doItemDTOS = orderDetailEList.stream().map(detail -> {
            DoDTO.DoItemDTO doItemDTO = new DoDTO.DoItemDTO();
            doItemDTO.setSkuCode(detail.getSkuCode());
            doItemDTO.setSkuQty(detail.getOrderQty());
            return doItemDTO;
        }).collect(Collectors.toList());
        doDTO.setDoItemList(doItemDTOS);
        doDTOList.add(doDTO);
        // 调用交易中心发货状态接口
        tradeFacade.deliverNotify(doDTOList);

        // 修改同步状态为11
        return orderMapper.updateSyncTradeStatusDoDoneByOrderCode(orderDTO.getOrderCode());
    }

    @Override
    public List<OrderDetailLockStatusDTO> queryOrderDetailLockStatus(String saleCode) {
        OrderE orderE = orderMapper.findBySaleCode(saleCode);
        if (orderE == null) {
            throw new RomeException(ResCode.ORDER_ERROR_1004, ResCode.ORDER_ERROR_1004_DESC);
        }
        List<OrderDetailE> orderDetailEList = orderDetailMapper.findByOrderCode(orderE.getOrderCode());

        List<OrderDetailLockStatusDTO> orderDetailLockStatusDTOS = orderDetailEList.stream().map(detail -> {
            OrderDetailLockStatusDTO orderDetailLockStatusDTO = new OrderDetailLockStatusDTO();
            orderDetailLockStatusDTO.setSkuCode(detail.getSkuCode());
            orderDetailLockStatusDTO.setOrderQty(detail.getOrderQty());
            orderDetailLockStatusDTO.setRequireQty(detail.getRequireQty());
            orderDetailLockStatusDTO.setHasLockQty(detail.getHasLockQty());
            if (detail.getHasLockQty().compareTo(BigDecimal.ZERO) == 0) {
                orderDetailLockStatusDTO.setLockStatus(0);
            } else {
                orderDetailLockStatusDTO.setLockStatus(detail.getLockStatus());
            }
            return orderDetailLockStatusDTO;
        }).collect(Collectors.toList());

        return orderDetailLockStatusDTOS;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDo(String orderCode, Long userId) {
        OrderE orderE = orderMapper.findByOrderCode(orderCode);
        if (orderE == null) {
            throw new RomeException(ResCode.ORDER_ERROR_1004, ResCode.ORDER_ERROR_1004_DESC);
        }
        // 查询预约单工厂对应的团购发货仓列表
        WarehouseQueryDTO warehouseQueryDTO = new WarehouseQueryDTO();
        warehouseQueryDTO.setFactoryCode(orderE.getFactoryCode());
        warehouseQueryDTO.setType(deliveryWarehouseType);
        List<RealWarehouse> realWarehouses = stockRealWarehouseFacade.queryRealWarehouseByFactoryCodeAndType(warehouseQueryDTO);
        if (CollectionUtils.isEmpty(realWarehouses)) {
            throw new RomeException(ResCode.ORDER_ERROR_1036, ResCode.ORDER_ERROR_1036_DESC);
        }
        // 需要包装时状态必须为加工完成20
        if (orderE.getNeedPackage().equals(OrderConstants.NEED_PACKAGE) && !orderE.getOrderStatus().equals(OrderConstants.PROCESS_STATUS_DONE)) {
            throw new RomeException(ResCode.ORDER_ERROR_1034, ResCode.ORDER_ERROR_1034_DESC);
        }
        // 不需要包装时状态必须为调拨入库12
        if (orderE.getNeedPackage().equals(OrderConstants.NO_NEED_PACKAGE) && !orderE.getOrderStatus().equals(OrderConstants.ALLOT_STATUS_IN)) {
            throw new RomeException(ResCode.ORDER_ERROR_1035, ResCode.ORDER_ERROR_1035_DESC);
        }
        // 创建DO
        this.createDoCommon(orderE, userId);
    }

    /**
     * 强制生成DO
     *
     * @param orderCode
     * @param userId
     */
    @Override
    public void forceCreateDo(String orderCode, Long userId) {
        OrderE orderE = orderMapper.findByOrderCode(orderCode);
        if (orderE == null) {
            throw new RomeException(ResCode.ORDER_ERROR_1004, ResCode.ORDER_ERROR_1004_DESC);
        }
        // 根据渠道查询虚仓列表，再根据虚仓找到实仓
        // (正常流程：锁轮询锁库存时会设置factorCode和realWarehouseCode；强制创建DO时绕过了轮询锁库存操作，所以需要进行设置)
        List<VirtualWarehouse> virtualWarehouseList = stockRealWarehouseFacade.getVwListByChannelCode(orderE.getChannelCode());
        AlikAssert.notEmpty(virtualWarehouseList,ResCode.ORDER_ERROR_1001, "渠道对应的虚仓不存在");
        VirtualWarehouse virtualWarehouse = virtualWarehouseList.stream().findFirst().orElse(null);
        //根据预约单工厂编号查询团购退货仓列表
        List<RealWarehouse> warehouseList =  stockRealWarehouseFacade.queryRealWarehouseByFactoryCodeAndRealWarehouseType(virtualWarehouse.getFactoryCode(), deliveryWarehouseType);
        AlikAssert.notEmpty(warehouseList, ResCode.ORDER_ERROR_1036, ResCode.ORDER_ERROR_1036_DESC);
        RealWarehouse realWarehouse = warehouseList.stream().findFirst().orElse(null);
        orderE.setFactoryCode(realWarehouse.getFactoryCode());
        orderE.setRealWarehouseCode(realWarehouse.getRealWarehouseOutCode());
        if(!orderE.getNeedPackage().equals(OrderConstants.NO_NEED_PACKAGE)
                || !orderE.getOrderStatus().equals(OrderStatusEnum.INIT_STATUS.getStatus())
                || !orderE.getHasTradeAudit().equals(OrderConstants.TRADE_AUDIT_STATUS_PASSED)){
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        // 创建DO
        this.createDoCommon(orderE, userId);
    }

    /**
     * 公共代码。创建DO
     * @param orderE
     * @param userId
     */
    public void createDoCommon(OrderE orderE, Long userId){
        List<OrderDetailE> orderDetailEList = orderDetailMapper.findByOrderCode(orderE.getOrderCode());
        orderDetailEList.forEach(e -> {
            e.setSkuQty(e.getOrderQty());
        });

        orderE.setOrderDetailEList(orderDetailEList);

        //创建预约前置单 + 明细
        this.addFrontRecord(orderE);
        //根据预约单创建后置出库单
        WarehouseRecordE outwarehouseRecordE = orderToWareHouseRecordService.createOutRecordByFrontRecord(orderE);

        // 创建出库单后更新预约单的相关数据
        this.updateOrderAfterCreateDo(orderE, outwarehouseRecordE, userId);

        //同步出库单 到 库存中心()
        OutWarehouseRecordDTO outRecordDto = stockRecordDTOConvert.convertE2OutDTO(outwarehouseRecordE);
        outRecordDto.setOutRecordCode(orderE.getOrderCode());
        for (WarehouseRecordDetailE detail : outwarehouseRecordE.getWarehouseRecordDetailList()) {
            outRecordDto.getDetailList().stream().filter(recordDetail -> recordDetail.getSkuCode().equals(detail.getSkuCode())).forEach(
                    rDetail ->{
                        rDetail.setLineNo(detail.getId() + "");
                        rDetail.setDeliveryLineNo(detail.getId() + "");
                    }
            );
        }
        stockRecordFacade.createOutRecord(outRecordDto);
    }

    /**
     * 创建预约前置单 + 明细
     * @param orderE
     * @return
     */
    private void addFrontRecord(OrderE orderE){
        //设置单据类型
        orderE.setRecordType(FrontRecordTypeEnum.RESERVATION_DO_RECORD.getType());

        List<OrderDetailE> orderDetailEList = orderE.getOrderDetailEList();

        itemInfoTool.convertSkuCode(orderDetailEList);
        //单位转换
        skuQtyUnitTool.queryBasicUnit(orderDetailEList);

        if(StringUtils.isBlank(orderE.getRemark())) {
            orderE.setRemark("");
        }
    }

    /**
     * 创建出库单后更新预约单的相关数据
     * @param orderE
     * @param outwarehouseRecordE
     * @param userId
     */
    public void updateOrderAfterCreateDo(OrderE orderE, WarehouseRecordE outwarehouseRecordE, Long userId){
    	orderE.setDoCode(outwarehouseRecordE.getRecordCode());
    	orderE.setDoFactoryCode(outwarehouseRecordE.getFactoryCode());
    	orderE.setDoRealWarehouseCode(outwarehouseRecordE.getRealWarehouseCode());
    	orderE.setHasDo(OrderConstants.HAS_DO);
    	orderE.setModifier(userId);
        int row = orderMapper.updateOrder(orderE);
        if (row != 1) {
            throw new RomeException(ResCode.ORDER_ERROR_1032, ResCode.ORDER_ERROR_1032_DESC);
        }
        row = 0;
        if (orderE.getNeedPackage().equals(OrderConstants.NEED_PACKAGE)) {
            row = orderMapper.updateOrderStatusDeliveryWaitWhenNeedPackageByOrderCode(orderE.getOrderCode());
        } else if (orderE.getNeedPackage().equals(OrderConstants.NO_NEED_PACKAGE) && orderE.getOrderStatus().equals(OrderConstants.ALLOT_STATUS_IN)) {
            row = orderMapper.updateOrderStatusDeliveryWaitWhenNonNeedPackageByOrderCode(orderE.getOrderCode());
        } else if (orderE.getNeedPackage().equals(OrderConstants.NO_NEED_PACKAGE)
                && orderE.getOrderStatus().equals(OrderStatusEnum.INIT_STATUS.getStatus())
                && orderE.getHasTradeAudit().equals(OrderConstants.TRADE_AUDIT_STATUS_PASSED)){
            // 强制创建DO，orderStatus从0改为 30（待发货），hasTradeAudit从0改为1待同步(锁定)
            row = orderMapper.updateOrderStatusAndSyncTradeStatus(orderE.getId(), OrderStatusEnum.DELIVERY_STATUS_WAIT.getStatus(), orderE.getOrderStatus(), orderE.getVersionNo());
        }
        if (row != 1) {
            throw new RomeException(ResCode.ORDER_ERROR_1020, ResCode.ORDER_ERROR_1020_DESC);
        }

        // 保存单据流转状态
        RecordStatusLogE recordStatusLogE = new RecordStatusLogE();
        recordStatusLogE.setOrderCode(orderE.getOrderCode());
        recordStatusLogE.setRecordStatus(OrderConstants.DELIVERY_STATUS_WAIT);
        recordStatusLogMapper.insertRecordStatusLog(recordStatusLogE);
    }

    @Override
    public PageInfo<OrderRespDTO> pageOrder(QueryOrderDTO queryOrderDTO, Integer pageNum, Integer pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<OrderE> orderEList = orderMapper.findPageByCondition(queryOrderDTO);
        List<OrderRespDTO> list = orderConvert.convertEList2DTOList(orderEList);
        PageInfo<OrderRespDTO> pageInfo = new PageInfo<>(list);
        pageInfo.setTotal(page.getTotal());

        // 收集查询实仓名称的条件（工厂编码和实仓编码）
        List<QueryRealWarehouseDTO> queryRealWarehouseDTOS = pageInfo.getList().stream()
                //过滤掉工厂编码和实仓编码为空的
                .filter(order -> StringUtils.isNotBlank(order.getFactoryCode()) && StringUtils.isNotBlank(order.getRealWarehouseCode()))
                .map(order -> {
                    QueryRealWarehouseDTO queryRealWarehouseDTO = new QueryRealWarehouseDTO();
                    queryRealWarehouseDTO.setFactoryCode(order.getFactoryCode());
                    queryRealWarehouseDTO.setWarehouseOutCode(order.getRealWarehouseCode());
                    return queryRealWarehouseDTO;
                }).collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                        // 去除重复的条件
                        new TreeSet<>(Comparator.comparing(o -> o.getFactoryCode() + "-" + o.getWarehouseOutCode()))), ArrayList::new));
        List<RealWarehouse> realWarehouses = null;
        try {
            if (CollectionUtils.isNotEmpty(queryRealWarehouseDTOS)) {
                realWarehouses = stockRealWarehouseFacade.queryByOutCodeAndFactoryCodeList(queryRealWarehouseDTOS);
            }
        } catch (Exception e) {
            log.error("调用库存中心接口失败: {}", e);
        }
        if (CollectionUtils.isNotEmpty(realWarehouses)) {
            Map<String, String> realWarehouseMap = realWarehouses.stream().collect(Collectors.toMap(RealWarehouse::getRealWarehouseCode, RealWarehouse::getRealWarehouseName, (v1, v2) -> v1));
            pageInfo.getList().forEach(order -> {
                String realWarehouseName = realWarehouseMap.get(order.getFactoryCode() + "-" + order.getRealWarehouseCode());
                order.setRealWarehouseName(realWarehouseName);
            });
        }

        return pageInfo;
    }

    @Override
    public List<OrderRespDTO> findOrder(QueryOrderDTO queryOrderDTO) {
    	List<OrderE> orderEList = orderMapper.findPageByCondition(queryOrderDTO);
        List<OrderRespDTO> list = orderConvert.convertEList2DTOList(orderEList);

        // 收集查询实仓名称的条件（工厂编码和实仓编码）
        List<QueryRealWarehouseDTO> queryRealWarehouseDTOS = list.stream()
                //过滤掉工厂编码和实仓编码为空的
                .filter(order -> StringUtils.isNotBlank(order.getFactoryCode()) && StringUtils.isNotBlank(order.getRealWarehouseCode()))
                .map(order -> {
                    QueryRealWarehouseDTO queryRealWarehouseDTO = new QueryRealWarehouseDTO();
                    queryRealWarehouseDTO.setFactoryCode(order.getFactoryCode());
                    queryRealWarehouseDTO.setWarehouseOutCode(order.getRealWarehouseCode());
                    return queryRealWarehouseDTO;
                }).collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                        // 去除重复的条件
                        new TreeSet<>(Comparator.comparing(o -> o.getFactoryCode() + "-" + o.getWarehouseOutCode()))), ArrayList::new));

        List<QueryRealWarehouseDTO> queryDoRealWarehouseDTOS = list.stream()
                //过滤掉工厂编码和实仓编码为空的
                .filter(order -> StringUtils.isNotBlank(order.getDoFactoryCode()) && StringUtils.isNotBlank(order.getDoRealWarehouseCode()))
                .map(order -> {
                    QueryRealWarehouseDTO queryRealWarehouseDTO = new QueryRealWarehouseDTO();
                    queryRealWarehouseDTO.setFactoryCode(order.getDoFactoryCode());
                    queryRealWarehouseDTO.setWarehouseOutCode(order.getDoRealWarehouseCode());
                    return queryRealWarehouseDTO;
                }).collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                // 去除重复的条件
                new TreeSet<>(Comparator.comparing(o -> o.getFactoryCode() + "-" + o.getWarehouseOutCode()))), ArrayList::new));


        List<RealWarehouse> realWarehouses = null;
        try {
            queryRealWarehouseDTOS.addAll(queryDoRealWarehouseDTOS);
            if (CollectionUtils.isNotEmpty(queryRealWarehouseDTOS)) {
                realWarehouses = stockRealWarehouseFacade.queryByOutCodeAndFactoryCodeList(queryRealWarehouseDTOS);
            }
        } catch (Exception e) {
            log.error("调用库存中心接口失败: {}", e);
        }
        if (CollectionUtils.isNotEmpty(realWarehouses)) {
            Map<String, String> realWarehouseMap = realWarehouses.stream().collect(Collectors.toMap(RealWarehouse::getRealWarehouseCode, RealWarehouse::getRealWarehouseName, (v1, v2) -> v1));
            list.forEach(order -> {
                String realWarehouseName = realWarehouseMap.get(order.getFactoryCode() + "-" + order.getRealWarehouseCode());
                order.setRealWarehouseName(realWarehouseName);
                String doRealWarehouseName = realWarehouseMap.get(order.getDoFactoryCode() + "-" + order.getDoRealWarehouseCode());
                order.setDoRealWarehouseName(doRealWarehouseName);
            });
        }
        return list;
    }

    @Override
    public OrderRespDTO findOrder(String orderCode) {
        OrderE orderE = orderMapper.findByOrderCode(orderCode);
        OrderRespDTO orderRespDTO = orderConvert.convertE2DTO(orderE);

        QueryRealWarehouseDTO queryRealWarehouseDTO = new QueryRealWarehouseDTO();
        queryRealWarehouseDTO.setFactoryCode(orderRespDTO.getFactoryCode());
        queryRealWarehouseDTO.setWarehouseOutCode(orderRespDTO.getRealWarehouseCode());
        List<QueryRealWarehouseDTO> queryRealWarehouseDTOS = new ArrayList<>();
        queryRealWarehouseDTOS.add(queryRealWarehouseDTO);
        List<RealWarehouse> realWarehouses = null;
        try {
            realWarehouses = stockRealWarehouseFacade.queryByOutCodeAndFactoryCodeList(queryRealWarehouseDTOS);
        } catch (Exception e) {
            log.error("调用库存中心接口失败: {}", e);
        }
        if (CollectionUtils.isNotEmpty(realWarehouses)) {
            RealWarehouse realWarehouse = realWarehouses.get(0);
            orderRespDTO.setRealWarehouseName(realWarehouse.getRealWarehouseName());
        }
        return orderRespDTO;
    }

    @Override
    public PageInfo<OrderDetailRespDTO> pageOrderDetail(String orderCode, Integer pageNum, Integer pageSize) {
    	Page page = PageHelper.startPage(pageNum, pageSize);
        List<OrderDetailE> orderDetailEList = orderDetailMapper.findPageByOrderCode(orderCode);
        List<OrderDetailRespDTO> list = orderDetailConvert.convertEList2DTOList(orderDetailEList);
        PageInfo<OrderDetailRespDTO> pageInfo = new PageInfo<>(list);
        pageInfo.setTotal(page.getTotal());
        //收集skucode
        List<String> skuCodeList = pageInfo.getList().stream().map(OrderDetailRespDTO::getSkuCode).collect(Collectors.toList());
        // 调商品中心接口查询商品名称
        List<SkuInfoExtDTO> skuInfoExtDTOS = null;
        try {
            if (CollectionUtils.isNotEmpty(skuCodeList)) {
                skuInfoExtDTOS = itemFacade.skuListBySkuCodes(skuCodeList);
            }
        } catch (Exception e) {
            log.error("调用商品中心接口失败: {}", e);
        }
        if (CollectionUtils.isNotEmpty(skuCodeList)) {
            Map<String, String> skuInfoExtDTOMap = skuInfoExtDTOS.stream().collect(Collectors.toMap(SkuInfoExtDTO::getSkuCode, SkuInfoExtDTO::getName, (v1, v2) -> v1));
            pageInfo.getList().forEach(detail -> {
                String skuName = skuInfoExtDTOMap.get(detail.getSkuCode());
                detail.setSkuName(skuName);
            });
        }
        return pageInfo;
    }

    @Override
    public List<OrderDetailRespDTO> queryOrderDetailByOrderCode(String orderCode) {
        List<OrderDetailE> list = orderDetailMapper.findByOrderCode(orderCode);
        List<String> skuCodes = list.stream().map(OrderDetailE::getSkuCode).collect(Collectors.toList());

        List<SkuInfoExtDTO> skuInfoExtDTOList = itemFacade.skuListBySkuCodes(skuCodes);
        Map<String, SkuInfoExtDTO> SkuInfoExtDTOMap = skuInfoExtDTOList.stream().collect(Collectors.toMap(SkuInfoExtDTO::getSkuCode, Function.identity(), (v1, v2) -> v1));
        List<OrderDetailRespDTO> orderDetailRespDTOS = list.stream().map(orderDetailDO -> {
            OrderDetailRespDTO orderDetailRespDTO = new OrderDetailRespDTO();
            BeanUtils.copyProperties(orderDetailDO, orderDetailRespDTO);
            if (SkuInfoExtDTOMap.containsKey(orderDetailDO.getSkuCode())) {
                orderDetailRespDTO.setSkuName(SkuInfoExtDTOMap.get(orderDetailDO.getSkuCode()).getName());
            }
            return orderDetailRespDTO;
        }).collect(Collectors.toList());
        return orderDetailRespDTOS;
    }

    /**
     * 根据预约单号查询预约单（包含明细）
     */
	@Override
	public OrderE queryOrderWithDetail(String orderCode) {
		OrderE orderE = orderMapper.queryByOrderCode(orderCode);
		if(orderE == null) {
			return null;
		}
		List<OrderDetailE> orderDetailEList = orderDetailMapper.queryByOrderCode(orderCode);
		orderE.setOrderDetailEList(orderDetailEList);
		return orderE;
	}

    /**
     * 预约单出库回调
     * @param stockNotifyDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderOutNotify(StockNotifyDTO stockNotifyDTO) {
        //1、查询预约单
        OrderE orderE = this.queryWithDetailByRecordCode(stockNotifyDTO.getRecordCode());
        AlikAssert.isNotNull(orderE, ResCode.ORDER_ERROR_1001, "预约单不存在");
        List<OrderDetailE> orderDetailEList = orderE.getOrderDetailEList();
        if(CollectionUtils.isEmpty(orderDetailEList)) {
            throw new RomeException(ResCode.ORDER_ERROR_1001, "预约单明细不存在");
        }
        // 修改预约单状态(改为31已发货)&校验预约单明细的发货数量
        this.updateOrderWithDetail(orderE.getOrderCode(), stockNotifyDTO, orderDetailEList);
        // 更新预约单同步交易状态为10
        int row = orderMapper.updateSyncTradeStatusDoWaitByOrderCode(orderE.getOrderCode());
        if (row != 1) {
            throw new RomeException(ResCode.ORDER_ERROR_1023, ResCode.ORDER_ERROR_1023_DESC);
        }
        // 通知交易中心的工作由job完成
    }

    /**
     * 根据出库单据编号查询预约单（包含明细）
     */
    private OrderE queryWithDetailByRecordCode(String recordCode) {
        Long orderId = frontWarehouseRecordRelationMapper.getFrontRecordIdByRecordCode(recordCode);
        if (orderId == null) {
            return null;
        }
        OrderE orderE = orderMapper.queryOrderById(orderId);
        if (orderE == null) {
            return null;
        }
        List<OrderDetailE> orderReturnDetailES = orderDetailMapper.queryByOrderCode(orderE.getOrderCode());
        if (CollectionUtils.isEmpty(orderReturnDetailES)) {
            return null;
        }
        orderE.setOrderDetailEList(orderReturnDetailES);
        return orderE;
    }

    /**
     * 修改预约单状态(改为31已发货)&校验预约单明细的发货数量
     * @param stockNotifyDTO
     */
    private void updateOrderWithDetail(String orderCode, StockNotifyDTO stockNotifyDTO, List<OrderDetailE> orderDetailEList) {
        //根据预约单号预约单状态=31（已发货）
        int row = orderMapper.updateOrderStatusByOrderCode(orderCode);
        if(row < 1) {
            throw new RomeException(ResCode.ORDER_ERROR_1001, "预约单状态已发货更新失败");
        }
        Map<String, OrderDetailE> orderDetailEMap = orderDetailEList.stream().collect(Collectors.toMap(OrderDetailE :: getSkuCode, Function.identity(), (v1, v2) -> v1));
        stockNotifyDTO.getDetailDTOList().forEach(e -> {
            if(orderDetailEMap.containsKey(e.getSkuCode())) {
                //下单数量
                BigDecimal orderQty = orderDetailEMap.get(e.getSkuCode()).getOrderQty();
                if(orderQty.compareTo(e.getActualQty()) == -1) {
                    throw new RomeException(ResCode.ORDER_ERROR_1001, "发货数量不等于下单数量");
                }
            } else {
                throw new RomeException(ResCode.ORDER_ERROR_1001, "商品[" +e.getSkuCode()+ "]未出库");
            }
        });
    }

    /**
     * 调拨出库通知
     */
	@Override
	public void allotOutNotify(WhAllocationE whAllocationE) {
		//根据调拨单号查询预约单
		OrderE orderE = orderMapper.queryOrderByAllotCode(whAllocationE.getRecordCode());
		AlikAssert.isNotNull(orderE, ResCode.ORDER_ERROR_1004, ResCode.ORDER_ERROR_1004_DESC);
		//预约单状态orderStatus是否等于调拨出库,直接返回
		if(orderE.getOrderStatus() == 11 || orderE.getOrderStatus() == 12){
		   return;
		}
		//预约单状态orderStatus是否等于10（调拨审核通过）
		if(orderE.getOrderStatus() != 10){
		    throw new RomeException(ResCode.ORDER_ERROR_1005, "预约单状态不是调拨审核通过");
		}
		//设置预约单状态orderStatus=11（调拨出库），根据预约单号orderCode修改预约单，前置条件预约单状态orderStatus=10（调拨审核通过）
		int j = orderMapper.updateOrderAllocationOutStatusByRecordCode(orderE.getOrderCode());
		if(j < 1){
		    throw new RomeException(ResCode.ORDER_ERROR_1012, ResCode.ORDER_ERROR_1012_DESC);
		}
		//添加调用日志
		recordStatusLogMapper.insertRecordStatusLog(new RecordStatusLogE(orderE.getOrderCode(),11));
	}

	/**
	 * 调拨入库通知
	 */
	@Override
	public void allotInNotify(WhAllocationE whAllocationE) {
		//根据调拨单号allotCode查询预约单
        OrderE orderE = orderMapper.queryOrderByAllotCode(whAllocationE.getRecordCode());
        AlikAssert.isNotNull(orderE, ResCode.ORDER_ERROR_1004, ResCode.ORDER_ERROR_1004_DESC);
        //预约单状态orderStatus是否等于调拨入库/待加工，直接返回
        if(orderE.getOrderStatus() == 12){
            return;
        }
        //预约单状态orderStatus是否等于调拨出库
        if(orderE.getOrderStatus() != 11){
            throw new RomeException(ResCode.ORDER_ERROR_1005, "预约单状态不是调拨出库");
        }
        //设置预约单状态orderStatus=12（调拨入库），根据预约单号orderCode修改预约单，前置条件预约单状态orderStatus=11（调拨出库）
        int j = orderMapper.updateOrderAllocationInStatusByRecordCode(orderE.getOrderCode());
        if(j < 1){
            throw new RomeException(ResCode.ORDER_ERROR_1012, ResCode.ORDER_ERROR_1012_DESC);
        }
        //添加调用日志
        recordStatusLogMapper.insertRecordStatusLog(new RecordStatusLogE(orderE.getOrderCode(),12));
	}

	/**
	 * 查询待锁定的预约单列表
	 */
	@Override
	public List<String> queryWaitLockOrder() {
		Date endTime = new Date();
        Date startTime = DateUtils.addDays(endTime, -30);
		return orderMapper.queryWaitLockOrder(startTime, endTime);
}

	/**
	 * 根据销售单号查询预约单及明细
	 */
	@Override
	public List<OrderDetailRespDTO> findByOrderCode(String orderCode) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 根据同步交易状态查询预约单
	 */
	@Override
	public List<OrderDTO> findBySyncTradeStatus(Integer syncTradeStatus, Date monthAgo) {
		List<OrderE> orderEList = orderMapper.findBySyncTradeStatus(syncTradeStatus, monthAgo);
		return orderDetailConvert.convertEList2DTOList2(orderEList);
	}

	/**
	 */
	@Override
	public OrderRespDTO findBySaleCode(String saleCode) {
		OrderE orderE = orderMapper.findBySaleCode(saleCode);
		return orderConvert.convertE2DTO(orderE);
	}

	/**
	 * 根据销售单号查询预约单及明细
	 */
	@Override
	public OrderRespDTO queryOrderAndOrderDetailBySaleCode(String saleCode) {
		OrderE orderE = orderMapper.findBySaleCode(saleCode);
		if(orderE == null) {
			return null;
		}
		OrderRespDTO orderRespDTO = orderConvert.convertE2DTO(orderE);
		List<OrderDetailE> list = orderDetailMapper.findByOrderCode(orderE.getOrderCode());
		List<OrderDetailRespDTO> orderDetailRespDTOList = orderDetailConvert.convertEList2DTOList(list);
		orderRespDTO.setOrderDetailRespDTOS(orderDetailRespDTOList);
		return orderRespDTO;
	}

	/**
	 * 根据预约单号修改单据状态、同步交易状态
	 */
	@Override
	public void updateOrderStatusAndSyncTradeStatus(String recordCode) {
		//根据预约单号查询预约单
		OrderE orderE = orderMapper.queryByOrderCode(recordCode);
		if(orderE == null) {
			 throw new RomeException(ResCode.ORDER_ERROR_1004, ResCode.ORDER_ERROR_1004_DESC);
		}
		Integer orderStatus = orderE.getOrderStatus();
		if(!OrderStatusEnum.INIT_STATUS.getStatus().equals(orderStatus) && !OrderStatusEnum.LOCK_STATUS_PART.getStatus().equals(orderStatus)) {
			throw new RomeException(ResCode.ORDER_ERROR_1001, ResCode.ORDER_ERROR_1001_DESC + "：预约单状态不是初始或部分锁定");
		}
		Integer needPackage = orderE.getNeedPackage();
		Integer recordStatus = null;
		if(OrderStatusEnum.INIT_STATUS.getStatus().equals(orderStatus)) {
			//需要包装
			if(Integer.valueOf(OrderConstants.NEED_PACKAGE).equals(needPackage)) {
				//创建DO，orderStatus从0改为12（待加工），hasTradeAudit从0改为1待同步(锁定)
				recordStatus = OrderStatusEnum.ALLOT_STATUS_IN.getStatus();
				int row = orderMapper.updateOrderStatusAndSyncTradeStatus(orderE.getId(), recordStatus, OrderStatusEnum.INIT_STATUS.getStatus(), orderE.getVersionNo());
				if(row < 1) {
					throw new RomeException(ResCode.ORDER_ERROR_1027, ResCode.ORDER_ERROR_1027_DESC);
				}
			} else {
				//创建DO，orderStatus从0改为 30（待发货），hasTradeAudit从0改为1待同步(锁定)
				recordStatus = OrderStatusEnum.DELIVERY_STATUS_WAIT.getStatus();
				int row = orderMapper.updateOrderStatusAndSyncTradeStatus(orderE.getId(), recordStatus, OrderStatusEnum.INIT_STATUS.getStatus(), orderE.getVersionNo());
				if(row < 1) {
					throw new RomeException(ResCode.ORDER_ERROR_1027, ResCode.ORDER_ERROR_1027_DESC);
				}
			}
		}
		if(OrderStatusEnum.LOCK_STATUS_PART.getStatus().equals(orderStatus)) {
			//创建调拨，orderStatus从1改为10（调拨审核通过）hasTradeAudit从0改为1待同步(锁定)
			recordStatus = OrderStatusEnum.ALLOT_AUDIT_STATUS_PASSED.getStatus();
			int row = orderMapper.updateOrderStatusAndSyncTradeStatus(orderE.getId(), recordStatus, OrderStatusEnum.LOCK_STATUS_PART.getStatus(), orderE.getVersionNo());
			if(row < 1) {
				throw new RomeException(ResCode.ORDER_ERROR_1027, ResCode.ORDER_ERROR_1027_DESC);
			}
		}
		RecordStatusLogE recordStatusLogE = new RecordStatusLogE();
		recordStatusLogE.setOrderCode(recordCode);
		recordStatusLogE.setRecordStatus(recordStatus);
		int row = recordStatusLogMapper.insertRecordStatusLog(recordStatusLogE);
		if(row < 1) {
			throw new RomeException(ResCode.ORDER_ERROR_6042, ResCode.ORDER_ERROR_6042_DESC);
		}
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPackDemand(OrderCreatePackDemandDTO orderCreatePackDemandDTO) {
        OrderE orderE = orderMapper.findByOrderCode(orderCreatePackDemandDTO.getOrderCode());
        AlikAssert.isNotNull(orderE, ResCode.ORDER_ERROR_7610, ResCode.ORDER_ERROR_7610_DESC);
        //校验预约单是否是初始状态
        Integer orderStatus = orderE.getOrderStatus();
        AlikAssert.isTrue(orderStatus.equals(OrderConstants.INIT_STATUS), ResCode.ORDER_ERROR_1040, ResCode.ORDER_ERROR_1040_DESC);
        //更新预约单的仓库信息、交易状态和单据状态
        Integer needPackage = orderE.getNeedPackage();
        if(Integer.valueOf(OrderConstants.NEED_PACKAGE).equals(needPackage)) {
            //更新单据状态、同步交易状态和仓库信息
            this.updateOrderByCondition(orderE, orderCreatePackDemandDTO);
            DemandFromSoDTO demandFromSoDTO = new DemandFromSoDTO();
            demandFromSoDTO.setOrderCode(orderCreatePackDemandDTO.getOrderCode());
            demandFromSoDTO.setUserId(orderCreatePackDemandDTO.getUserId());
            packDemandService.createPackDemandBySo(demandFromSoDTO);
        }
    }

    /**
     * @Description 更新单据状态、同步交易状态和仓库信息
     * @Date 2020/7/16 11:57
     * @param orderE,virtualWarehouse,realWarehouse
     * @return
     **/
    private void updateOrderByCondition(OrderE orderE, OrderCreatePackDemandDTO orderCreatePackDemandDTO) {
        UpdateOrderDTO updateOrderDTO = new UpdateOrderDTO();
        updateOrderDTO.setId(orderE.getId());
        updateOrderDTO.setOrderStatus(OrderStatusEnum.ALLOT_STATUS_IN.getStatus());
        updateOrderDTO.setOldOrderStatus(OrderStatusEnum.INIT_STATUS.getStatus());
        updateOrderDTO.setVersionNo(orderE.getVersionNo());
        updateOrderDTO.setFactoryCode(orderCreatePackDemandDTO.getOutFactoryCode());
        updateOrderDTO.setRealWarehouseOutCode(orderCreatePackDemandDTO.getOutRealWarehouseCode());
        updateOrderDTO.setAllotFactoryCode(orderCreatePackDemandDTO.getInFactoryCode());
        updateOrderDTO.setAllotRealWarehouseCode(orderCreatePackDemandDTO.getInRealWarehouseCode());
        int row = orderMapper.updateOrderByCondition(updateOrderDTO);
        if(row < 1) {
            throw new RomeException(ResCode.ORDER_ERROR_1027, ResCode.ORDER_ERROR_1027_DESC);
        }
    }

}
