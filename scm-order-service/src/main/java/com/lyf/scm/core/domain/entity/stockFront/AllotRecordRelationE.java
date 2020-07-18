package com.lyf.scm.core.domain.entity.stockFront;

import java.math.BigDecimal;

import com.lyf.scm.core.domain.model.stockFront.AllotRecordRelationDO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 调拨业务单据关系扩展对象 <br>
 *
 * @Author wwh 2020/7/8
 */
@Data
public class AllotRecordRelationE extends AllotRecordRelationDO {
	
	/**
	 * 实际出/入库数量
	 */
    private BigDecimal actualQty;
    
    /**
     * 商品编码
     */
    private String skuCode;

}