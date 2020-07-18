package com.lyf.scm.core.api.dto.stockFront;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode
public class AddressDTO {

	/**
	 * 所属单据编码
	 */
	@ApiModelProperty(value = "所属单据编码")
	@NotBlank(message="单据号不能为空")
	private String recordCode;
	/**
	 * 地址类型：0 收货地址，1 发货地址
	 */
	@ApiModelProperty(value = "地址类型：0 收货地址，1 发货地址")
	@NotNull(message="地址类型")
	private Integer addressType;
	/**
	 * 地址邮编 
	 */
	@ApiModelProperty(value = "地址邮编")
	private String postcode;
	/**
	 * 手机 
	 */
	@ApiModelProperty(value = "手机")
	private String mobile;
	/**
	 * 电话 
	 */
	@ApiModelProperty(value = "电话")
	private String phone;
	/**
	 * 邮箱
	 */
	@ApiModelProperty(value = "邮箱")
	private String email;
	/**
	 * 详细地址
	 */
	@ApiModelProperty(value = "详细地址")
	@NotBlank(message="详细地址不能为空")
	private String address;

	@ApiModelProperty(value = "省份")
	private String province;

	@ApiModelProperty(value = "城市")
	private String city;

	@ApiModelProperty(value = "区/县城市")
	private String county;

	@ApiModelProperty(value = "四级区域")
	private String area;

	/**
	 * 姓名
	 */
	@ApiModelProperty(value = "姓名")
	@NotBlank(message="姓名不能为空")
	private String name;
}

	
