package com.lyf.scm.core.domain.entity.orderReturn;

import com.lyf.scm.core.domain.model.orderReturn.OrderReturnDO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import lombok.Data;

import java.util.List;

/**
 * @Description: 退货单扩展对象
 * <p>
 * @Author: wwh 2020/4/8
 */
@Data
public class OrderReturnE extends OrderReturnDO {

    private List<OrderReturnDetailE> orderReturnDetailEList;

    private RealWarehouse realWarehouse;

}