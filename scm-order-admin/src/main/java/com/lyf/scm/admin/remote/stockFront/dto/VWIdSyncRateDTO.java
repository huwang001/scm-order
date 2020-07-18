package com.lyf.scm.admin.remote.stockFront.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class VWIdSyncRateDTO implements Serializable {

	private Long id;

	private Integer syncRate;

}