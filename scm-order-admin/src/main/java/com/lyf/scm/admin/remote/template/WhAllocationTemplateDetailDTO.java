package com.lyf.scm.admin.remote.template;

import lombok.Data;

/**
 * 类WhAllocationTemplateDetailDTO的实现描述：调拨明细导入模板
 *
 * @author sunyj 2019/7/31 11:47
 */
@Data
public class WhAllocationTemplateDetailDTO {

    /**
     * 商品编号
     */
    private String skuCode;

    /**
     * 数量
     */
    private String allotQty;

    /**
     * 单位编号
     */
    private String unitCode;

    /**
     * 退货原因(退货时必填)
     */
    private String returnReason;
    
}