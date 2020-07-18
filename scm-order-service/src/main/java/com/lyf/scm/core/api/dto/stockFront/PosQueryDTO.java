/**
 * Filename RealWarehouseStockDo.java
 * Company 上海来伊份科技有限公司。
 * @author xly
 * @version 
 */
package com.lyf.scm.core.api.dto.stockFront;

import java.util.List;

import com.lyf.scm.core.api.dto.BaseDTO;

import lombok.Data;

/**
 * 移动POS查询移动POS盘点物料清单
 */
@Data
public class PosQueryDTO extends BaseDTO {

	/**
	 * 门店编号
	 */
	private String shopCode;

	/**
	 * skuCode
	 */
	private List<String> skuCodes;

}