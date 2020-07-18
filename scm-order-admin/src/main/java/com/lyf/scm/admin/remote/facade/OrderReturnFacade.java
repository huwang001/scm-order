package com.lyf.scm.admin.remote.facade;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.remote.OrderReturnRemoteService;
import com.lyf.scm.admin.remote.dto.OrderReturnDTO;
import com.lyf.scm.admin.remote.dto.OrderReturnDetailDTO;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 退货单 <br>
 *
 * @Author wwh 2020/4/15
 */
@Slf4j
@Component
public class OrderReturnFacade {

    @Resource
    private OrderReturnRemoteService orderReturnRemoteService;
    
	/**
	 * 根据条件查询退货单列表-分页
	 * 
	 * @param orderReturnDTO
	 * @return
	 */
	public PageInfo<OrderReturnDTO> queryOrderReturnPageByCondition(OrderReturnDTO orderReturnDTO) {
		Response<PageInfo<OrderReturnDTO>> response = orderReturnRemoteService.queryOrderReturnPageByCondition(orderReturnDTO);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
	}

	/**
	 * 根据售后单号查询退货单详情列表-分页
	 * 
	 * @param afterSaleCode
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public OrderReturnDTO queryOrderReturnDetailPageByAfterSaleCode(String afterSaleCode, Integer pageNum,
			Integer pageSize) {
		Response<OrderReturnDTO> response = orderReturnRemoteService.queryOrderReturnDetailPageByAfterSaleCode(afterSaleCode, pageNum, pageSize);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
	}

	/**
	 * 根据售后单号查询退货单（包含退货单详情）
	 * 
	 * @param afterSaleCode
	 * @return
	 */
	public OrderReturnDTO queryOrderReturnWithDetailByAfterSaleCode(String afterSaleCode) {
		Response<OrderReturnDTO> response = orderReturnRemoteService.queryOrderReturnWithDetailByAfterSaleCode(afterSaleCode);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
	}

	/**
	 * 根据售后单号查询退货单详情列表
	 * 
	 * @param afterSaleCode
	 * @return
	 */
	public List<OrderReturnDetailDTO> queryOrderReturnDetailByAfterSaleCode(String afterSaleCode) {
		Response<List<OrderReturnDetailDTO>> response = orderReturnRemoteService.queryOrderReturnDetailByAfterSaleCode(afterSaleCode);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
	}

}