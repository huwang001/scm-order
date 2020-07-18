package com.lyf.scm.core.domain.entity.order;

import java.util.List;

import com.lyf.scm.core.domain.model.OrderDO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;

import lombok.Data;

/**
 * @Description: OrderDO 扩展对象
 * <p>
 * @Author: chuwenchao  2020/4/10
 */
@Data
public class OrderE extends OrderDO {
	
	List<OrderDetailE> orderDetailEList;
	
	RealWarehouse inRealWarehouse;
	
	RealWarehouse outRealWarehouse;

	private Integer recordType;
	
	private String recordCode;
}
