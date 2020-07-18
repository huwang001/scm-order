package com.lyf.scm.core.api.dto.stockFront;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class RecordRealVirtualStockSyncRelationDTO {

	private Long recordId;

	private String recordCode;

	private Long realWarehouseId;

	private String realWarehouseCode;

	private String factoryCode;

	private Long skuId;

	private Long virtualWarehouseId;

	private String virtualWarehouseCode;

	private BigDecimal configSyncRate;

	private Long creator;

	private Long modifier;

	private Integer allotType;

}
