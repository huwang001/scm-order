package com.lyf.scm.core.api.dto.stockFront;

import java.io.Serializable;
import java.util.List;

import com.lyf.scm.common.enums.RealWarehouseTypeEnum;
import com.lyf.scm.core.api.dto.BaseDTO;

import lombok.Data;

@Data
public class RealWarehouseParamDTO extends BaseDTO implements Serializable {

	private List<VWIdSyncRateDTO> vwIdSyncRateDTOList;

	private Long realWarehouseId;

	private String realWarehouseCode;

	private String realWarehouseOutCode;

	private String factoryCode;

	private String realWarehouseName;

	private List<RealWarehouseAreaAddDTO> realWarehouseAreaAddDTOList;

	/**
	 * 按照不包含的类型查询
	 */
	private Integer notInType;

	/**
	 * 根据仓库名和仓库编码模糊查询字段
	 */
	private String nameOrCode;

	private Long userId;

	private Long realWarehouseType;

	/**
	 * 门店仓枚举
	 */
	public static final Integer SHOP_TYPE = RealWarehouseTypeEnum.RW_TYPE_1.getType();

}