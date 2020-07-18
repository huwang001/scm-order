package com.lyf.scm.admin.dto;

import com.lyf.scm.admin.dto.order.OrderStatusDTO;
import lombok.Data;

import java.util.List;

@Data
public class EnumListDTO {


    /**
     *  预约单状态
     */
    private List<OrderStatusDTO> orderStatusCodeList;

    /**
     * 销售订单类型
     */

}
