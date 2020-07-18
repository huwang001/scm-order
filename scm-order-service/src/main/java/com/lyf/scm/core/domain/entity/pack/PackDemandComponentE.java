package com.lyf.scm.core.domain.entity.pack;

import java.math.BigDecimal;

import com.lyf.scm.core.domain.model.pack.PackDemandComponentDO;
import lombok.Data;

@Data
public class PackDemandComponentE extends PackDemandComponentDO {
	
	/**
	 * 已锁定数量
	 */
	private BigDecimal lockQty;
	
	/**
	 * 获取组合Key
	 * 
	 * @return
	 */
	public String getKey() {
		return this.getParentSkuCode() +"-"+ this.getSkuCode();
	}
	
}