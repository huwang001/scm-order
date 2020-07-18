package com.lyf.scm.admin.remote.dto;

import java.io.Serializable;
import java.util.List;


import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 退货单传输对象
 * <p>
 * @Author: wwh 2020/4/8
 */
@Data
public class OrderReturnDTO extends BaseDTO implements Serializable {
	
	@ApiModelProperty(value = "唯一主键")
    private Long id;

	@ApiModelProperty(value = "预约单号")
    private String orderCode;

	@ApiModelProperty(value = "销售单号")
    private String saleCode;

	@ApiModelProperty(value = "售后单号")
    private String afterSaleCode;

	@ApiModelProperty(value = "团购入库单号")
    private String returnEntryCode;

	@ApiModelProperty(value = "单据状态 1待入库 2已入库")
    private Integer orderStatus;
	
	@ApiModelProperty(value = "单据状态描述")
	private String orderStatusDesc;
	
	@ApiModelProperty(value = "客户名称")
    private String customName;

	@ApiModelProperty(value = "客户手机号")
    private String customMobile;

    @ApiModelProperty(value = "省Code")
    private String provinceCode;

    @ApiModelProperty(value = "省名称")
    private String provinceName;

    @ApiModelProperty(value = "市code")
    private String cityCode;

    @ApiModelProperty(value = "市名称")
    private String cityName;

    @ApiModelProperty(value = "区code")
    private String countyCode;

    @ApiModelProperty(value = "区名称")
    private String countyName;

    @ApiModelProperty(value = "客户详细地址")
    private String customAddress;

    @ApiModelProperty(value = "工厂编码")
    private String factoryCode;

    @ApiModelProperty(value = "退货实仓code")
    private String realWarehouseCode;
    
    @ApiModelProperty(value = "退货实仓名称")
    private String realWarehouseName;

    @ApiModelProperty(value = "退货仓库地址")
    private String warehouseAddress;

    @ApiModelProperty(value = "退货原因")
    private String reason;

    @ApiModelProperty(value = "通知交易中心状态 0初始 1待通知 2已通知")
    private Integer syncTradeStatus;

    @ApiModelProperty(value = "通知库存中心状态 0初始 1待通知 2已通知")
    private Integer syncStockStatus;

    @ApiModelProperty(value = "快递单号")
    private String expressNo;
    
    @ApiModelProperty(value = "退货单详情")
    private List<OrderReturnDetailDTO> orderReturnDetailList;
    
    @ApiModelProperty(value = "退货单详情-分页")
    PageInfo<OrderReturnDetailDTO> orderReturnDetailPageInfo;

}