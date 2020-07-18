package com.lyf.scm.core.api.dto.stockFront;

import java.io.Serializable;

import lombok.Data;

@Data
public class VWIdSyncRateDTO implements Serializable {

	private Long id;

	private Integer syncRate;

}