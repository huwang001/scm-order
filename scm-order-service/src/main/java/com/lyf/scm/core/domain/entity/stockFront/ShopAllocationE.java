package com.lyf.scm.core.domain.entity.stockFront;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lyf.scm.core.domain.model.stockFront.ShopAllocationDO;
import lombok.Data;

import java.util.List;

/**
 * @author zys
 * @Description 门店调拨前置单 （扩展）
 * @date 2020/6/15 15:11
 * @Version
 */
@Data
public class ShopAllocationE extends ShopAllocationDO {



    /**
     * 调出店铺id
     */
    private String outShopName;

    /**
     * 调入门店编号
     */
    private String inShopName;


    @JsonIgnore
    private String recordStatusReason;

    /**
     * 调拨详情
     */
    private List<ShopAllocationDetailE> frontRecordDetails;

}
