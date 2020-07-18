package com.lyf.scm.core.service.order.impl;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.MailStatusEnum;
import com.lyf.scm.common.enums.OrderStatusEnum;
import com.lyf.scm.common.enums.YesOrNoEnum;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.common.MailSendInfoDTO;
import com.lyf.scm.core.domain.entity.common.RecordStatusLogE;
import com.lyf.scm.core.domain.entity.order.OrderE;
import com.lyf.scm.core.domain.entity.pack.PackDemandE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.common.MailSendInfoMapper;
import com.lyf.scm.core.mapper.common.RecordStatusLogMapper;
import com.lyf.scm.core.mapper.order.OrderMapper;
import com.lyf.scm.core.mapper.pack.PackDemandMapper;
import com.lyf.scm.core.mapper.stockFront.FrontWarehouseRecordRelationMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.remote.stock.dto.CancelRecordDTO;
import com.lyf.scm.core.remote.stock.dto.CancelResultDTO;
import com.lyf.scm.core.remote.stock.facade.StockFacade;
import com.lyf.scm.core.remote.stock.facade.StockRecordFacade;
import com.lyf.scm.core.service.order.CancelOrderService;
import com.lyf.scm.core.service.pack.PackDemandService;
import com.lyf.scm.core.service.stockFront.WhAllocationService;
import com.rome.arch.core.exception.RomeException;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 取消预约单接口实现对象
 * <p>
 * @Author: wwh 2020/4/10
 */
@Slf4j
@Service("cancelOrderService")
public class CancelOrderServiceImpl implements CancelOrderService {
	
	@Resource
	private OrderMapper orderMapper;
	
	@Resource
	private MailSendInfoMapper mailSendInfoMapper;
	
	@Resource
	private RecordStatusLogMapper recordStatusLogMapper;

	@Resource
	private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;

	@Resource
	private WarehouseRecordMapper warehouseRecordMapper;

	@Resource
	private StockFacade stockFacade;

	@Resource
	private StockRecordFacade stockRecordFacade;
	
	@Resource
    private FreeMarkerConfigurer freeMarkerConfigurer;

	@Resource
	private PackDemandMapper packDemandMapper;

	@Resource
	private PackDemandService packDemandService;

	@Resource
	private WhAllocationService whAllocationService;
	
	@Value("${spring.mail.username}")
    private String sendUserMail;
    @Value("${receiveUser.Mail}")
    private String receiveUserList;
	
	/**
	 * 根据销售单号取消预约单
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void cancelOrder(String saleCode) {

		//1、根据销售单号查询预约单
		OrderE orderE = orderMapper.queryOrderBySaleCode(saleCode);

		if (orderE == null ||  OrderStatusEnum.CANCEL_STATUS.getStatus().equals(orderE.getOrderStatus())){

		    return;
        }
		//2、获取预约单单据状态 -- 已发货抛异常
        Integer orderStatus = orderE.getOrderStatus();
		if(OrderStatusEnum.DELIVERY_STATUS_DONE.getStatus().equals(orderStatus)) {

			throw new RomeException(ResCode.ORDER_ERROR_4013, ResCode.ORDER_ERROR_4013_DESC);
		}
		//3、根据预约单号修改预约单单据状态orderStatus=2（已取消）
		int row = orderMapper.updateOrderStatusCancelByOrderCode(orderE.getOrderCode());
		if (row < 1) {
            throw new RomeException(ResCode.ORDER_ERROR_4014, ResCode.ORDER_ERROR_4014_DESC);
        }

		//保存单据流转状态
        RecordStatusLogE recordStatusLogE = new RecordStatusLogE();
        recordStatusLogE.setOrderCode(orderE.getOrderCode());
        recordStatusLogE.setRecordStatus(OrderStatusEnum.CANCEL_STATUS.getStatus());
        recordStatusLogMapper.insertRecordStatusLog(recordStatusLogE);

		//更新后置单状态为取消 -- 是否创建DO 0:未创建 1:已创建
		if (YesOrNoEnum.YES.getType().equals(orderE.getHasDo())) {
			List<String> recordCodes = frontWarehouseRecordRelationMapper.getRecordCodeByFrontRecordCode(orderE.getOrderCode());
			if(CollectionUtils.isEmpty(recordCodes)) {
				throw new RomeException(ResCode.ORDER_ERROR_4008, ResCode.ORDER_ERROR_4008_DESC);
			}
			List<WarehouseRecordE> warehouseRecordEList  = warehouseRecordMapper.queryWarehouseRecordByRecordCode(recordCodes);

			warehouseRecordEList.forEach(warehouseRecordE -> {
				int i = warehouseRecordMapper.updateToCanceledFromComplete(warehouseRecordE.getId());
				AlikAssert.isTrue(i > 0, ResCode.ORDER_ERROR_4020, ResCode.ORDER_ERROR_4020_DESC);
			});

			//调用库存中心取消发货单，释放锁定库存,
            List<CancelRecordDTO> cancelRecordList = new ArrayList<>();
                    warehouseRecordEList.forEach(value -> {
                        CancelRecordDTO cancelRecordDTO = new CancelRecordDTO();
                        cancelRecordDTO.setIsForceCancel(YesOrNoEnum.YES.getType());
                        cancelRecordDTO.setRecordCode(value.getRecordCode());
                        cancelRecordDTO.setRecordType(value.getRecordType());
                        cancelRecordList.add(cancelRecordDTO);
                    });
			List<CancelResultDTO> cancelResultDTOList = stockRecordFacade.cancelRecord(cancelRecordList);
			List<Boolean> statusList = cancelResultDTOList.stream().filter(cancelResultDTO -> cancelResultDTO.getStatus() == false).map(CancelResultDTO::getStatus).distinct().collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(statusList)) {
				throw new RomeException(ResCode.ORDER_ERROR_6032, ResCode.ORDER_ERROR_6032_DESC);
			}
		}

		//判断预约单状态是否部分锁定/全部锁定/调库存中心下发预约单存在调用失败的情况，取消时查询库存中心，会导致预约单不存在异常
		if (StringUtils.isNotBlank(orderE.getFactoryCode())){

			if (YesOrNoEnum.NO.getType().equals(orderE.getHasDo())) {
				//调用库存中心取消预约单，释放锁定库存
				 this.stockCancelOrder(orderE.getOrderCode());
			}
		}
		// 判断预约单是否创建调拨，hasAllot=0 否 1是
		if(YesOrNoEnum.YES.getType().equals(orderE.getHasAllot())){
			try {
				//取消调拨单（已出库则不取消调拨）
				whAllocationService.cancleAllot(orderE.getAllotCode(),0L);

			}catch (Exception e){
				log.error("预约单取消调拨单是失败：{}，{} ",saleCode,e.getLocalizedMessage(),e);
			}
		}
		//判断是否需要包装 1 需要 0 不需要 取消包装需求单
		if (YesOrNoEnum.YES.getType().equals(orderE.getNeedPackage())){
			PackDemandE packDemand = packDemandMapper.queryBySaleCode(saleCode);
			if (packDemand != null){
				try {
					packDemandService.cancelPackDemand(packDemand.getRecordCode());
				} catch (Exception e) {
					log.error("预约单取消包装需求单失败 ：{}，{}，{}",saleCode,packDemand.getRecordCode(),e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 没有生成调拨
	 * 取消释放库存
	 * @param orderCode
	 */
	private void  stockCancelOrder(String orderCode){
		try {
			stockFacade.cancelOrder(orderCode);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			throw new RomeException(ResCode.ORDER_ERROR_4014, ResCode.ORDER_ERROR_4014_DESC);
		} catch (Exception e) {
			throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}


    /**
     * 保存邮件发送信息
     *
     * @param orderE
     */
    private void saveSendMail(OrderE orderE) {
        try {
            String orderStatusDesc = "【调拨入库】";
            if(Integer.valueOf(1).equals(orderE.getNeedPackage())) {
                orderStatusDesc = "【调拨入库待加工】";
            }
            Map<String, Object> model = new HashMap<String, Object>(2);
            model.put("order",orderE);
            model.put("orderStatusDesc", orderStatusDesc);
            Template temp = freeMarkerConfigurer.getConfiguration().getTemplate("CancelAllotTemplate.ftl");
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(temp, model);
            String mailTitle = "销售中心—调拨单操作取消提醒 " + DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");
            //组装邮件信息
            MailSendInfoDTO mailSendInfoDTO = new MailSendInfoDTO();
            mailSendInfoDTO.setSendUserMail(sendUserMail);
            mailSendInfoDTO.setReceiveUserMail(receiveUserList);
            mailSendInfoDTO.setMailTitle(mailTitle);
            mailSendInfoDTO.setMailContent(text);
            //邮件状态sendStatus=0无需发送1待发送2已发送
            mailSendInfoDTO.setSendStatus(MailStatusEnum.MAIL_SEND_WAIT.getStatus());
            mailSendInfoMapper.saveSendMail(mailSendInfoDTO);
        } catch (Exception e) {
            log.error("保存邮件发送信息失败",e);
            throw new RomeException(ResCode.ORDER_ERROR_4017, ResCode.ORDER_ERROR_4017_DESC);
        }
    }

}