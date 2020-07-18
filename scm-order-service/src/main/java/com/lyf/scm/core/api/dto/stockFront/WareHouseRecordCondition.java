package com.lyf.scm.core.api.dto.stockFront;

import com.lyf.scm.core.api.dto.BaseDTO;
import com.lyf.scm.core.remote.stock.dto.QueryRealWarehouseDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WareHouseRecordCondition extends BaseDTO implements Serializable {

	private List<RealWarehouseParamDTO> realWarehouseParamDTOS;

	/**
	 * 1出库单 2入库单
	 */
	private Integer businessType;

	private List<QueryRealWarehouseDTO> realWhCodes;

}