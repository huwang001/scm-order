package com.lyf.scm.core.api.dto.stockFront;

import java.io.Serializable;

import lombok.Data;

@Data
public class RealWarehouseAreaAddDTO implements Serializable {

	private Long realWarehouseId;

	private String countryCode;

	private String provinceCode;

	private String cityCode;

	private String countyCode;

	private String areaCode;

	private Long creator;
	
}