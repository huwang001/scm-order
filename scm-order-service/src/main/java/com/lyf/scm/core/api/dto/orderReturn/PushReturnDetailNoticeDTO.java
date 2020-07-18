package com.lyf.scm.core.api.dto.orderReturn;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @Description: 推送退货入库通知详情给交易中心传输对象
 * <p>
 * @Author: wwh 2020/4/20
 */
@Data
public class PushReturnDetailNoticeDTO implements Serializable {
	
	/**
	 * 商品编码
	 */
    private String skuCode;

	/**
	 * 商品数量
	 */
    private BigDecimal skuQty;

	/**
	 * 单位名称
	 */
    private String salesUnitName;

	/**
	 * 单位编码
	 */
    private String salesUnit;

}