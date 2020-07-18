package com.lyf.scm.core.remote.stock.dto;

import com.lyf.scm.core.api.dto.disparity.DisparityDetailDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Doc:差异处理入参
 * @Author: lchy
 * @Date: 2020/7/10
 * @Version 1.0
 */
@Data
public class DisparityParamDTO {


    @ApiModelProperty(value = "出入单据编码,不能为空", required = true)
    private String recordCode;

    @ApiModelProperty(value = "差异单号", required = true)
    private String disparityCode;

    @ApiModelProperty(value = "是否逆向,true表示逆向，false表示正向", required = true)
    private Boolean isReverse ;
    @ApiModelProperty(value = "差异类型 01门店责任 02仓库责任 03 物流责任", required = true)
    private String mode;
    @ApiModelProperty(value = "单据类型,不能为空", required = true)
    private Integer recordType;

    @ApiModelProperty(value = "出入库仓库code，如果出入库为门店则为门店仓的仓库编码[门店或仓库责任时必填]")
    private String outOrInWarehouseCode;

    @ApiModelProperty(value = "出入库工厂code，如果出入库为门店则为门店仓的工厂编码[门店或仓库责任时必填]")
    private String outOrInFactoryCode;

    @ApiModelProperty(value = "门店编码", required = true)
    private String shopCode;

    @ApiModelProperty(value = "大仓仓库编码", required = true)
    private String warehouseCode;

    @ApiModelProperty(value = "大仓所属工厂编码", required = true)
    private String factoryCode;

    @ApiModelProperty(value = "明细，不能为空", required = true)
    private List<DisparityDetailDTO> detailList;

}
