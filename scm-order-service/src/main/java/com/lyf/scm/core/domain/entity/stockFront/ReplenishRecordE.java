package com.lyf.scm.core.domain.entity.stockFront;

import com.lyf.scm.core.domain.model.stockFront.ReplenishRecordDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: ReplenishRecordE
 * <p>
 * @Author: chuwenchao  2020/6/10
 */
@Data
public class ReplenishRecordE extends ReplenishRecordDO {

    /**
     * 商品ID
      */
    private Long skuId;

    /**
     * 商品名称
      */
    private String skuName;

    /**
     * 工厂名称
     */
    private String factoryName;

    /**
     * 虚拟仓库组id   所属虚拟仓库组(寻源用)
     */
    private String virtualWarehouseGroupCode;

    /**
     * 前置单明细
     */
    List<ReplenishDetailE> frontRecordDetails;

    /**
     * 仓库编号
     */
    private String outRealWarehoseCode;

    /**
     * 仓库名称
     */
    private String outRealWarehouseName;

    /**
     * 门店编号
     */
    private String inShopCode;

    /**
     * 门店名称
     */
    private String inRealWarehouseName;

    /**
     * 前置类型名称
     */
    private String frontRecordTypeName;

    /**
     * 发货时间
     */
    private Date deliveryTime;

    /**
     * 采购单行号
     */
    private String lineNo;

    /**
     * 商品编号
     */
    private String skuCode;

    /**
     * 出库单锁定数量
     */
    private BigDecimal planQty;

    /**
     * 收货时间
     */
    private Date receiverTime;

}
