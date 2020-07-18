package com.lyf.scm.admin.remote.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode
public class VirtualWarehouse {

	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty(value = "虚拟仓库编码")
	private String virtualWarehouseCode;

	@ApiModelProperty(value = "虚拟仓库名称")
	private String virtualWarehouseName;

	@ApiModelProperty(value = "实体仓库id")
	private Long realWarehouseId;

	@ApiModelProperty(value = "修改者id")
	private Long modifier;

	@ApiModelProperty(value = "创建者id")
	private Long creator;

	@ApiModelProperty(value = "关联的虚仓组id")
	private Long virtualWarehouseGroupId;

	@ApiModelProperty(value = "同步比率（百分比）")
	private Integer syncRate;


	private BigDecimal configSyncRate;
	@ApiModelProperty(value = "工厂编码")
	private String factoryCode;
	@ApiModelProperty(value = "实体仓库ids")
	private List<Long> realWarehouseIds;

	@ApiModelProperty(value = "仓库状态：0-初始，1-启用，2-停用")
	private Long virtualWarehouseStatus;
	@ApiModelProperty(value = "实体仓库code")
	private String realWarehouseCode;
	@ApiModelProperty(value = "实体仓库name")
	private String realWarehouseName;
	@ApiModelProperty(value = "工厂name")
	private String factoryName;
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	@ApiModelProperty(value = "修改时间")
	private Date updateTime;
	
	@ApiModelProperty(value = "分配类型1比例分配 2绝对值分配")
    private Integer allotType;
	
}
