package com.lyf.scm.core.api.dto.stockFront;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 盘点前置单据
 * @author jl
 */
@Data
@EqualsAndHashCode
public class ShopInventoryDTO {

    /**
     * 门店编号
     */
    private String shopCode;

    /**
     * 盘点类型：0-抽盘，1-全盘,9-全盘
     */
    private Integer businessType;
    /**
     * 盘点时间
     */
    private Long outCreateTime;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 盘点明细
     */
    private List<InventoryDetailDTO> frontRecordDetails;
}
