package com.lyf.scm.core.domain.entity.order;

import com.lyf.scm.core.domain.model.OrderDetailDO;
import lombok.Data;

/**
 * @Description: OrderDetailDO扩展对象
 * <p>
 * @Author: chuwenchao  2020/4/10
 */
@Data
public class OrderDetailE extends OrderDetailDO {

    /**
     * 商品编码
     */
    private String skuName;

}
