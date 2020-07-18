package com.lyf.scm.core.domain.model.stockFront;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

import com.lyf.scm.core.domain.model.common.BaseDO;

/**
 * 自定义标题
 * 
 * @author WWH
 *
 */
@Data
public class CustomizeTableRowDO extends BaseDO implements Serializable {
	
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 用户id
     */
    private Long userId;
    
    /**
     * 表格code
     */
    private String tableCode;
    
    /**
     * 列code
     */
    private String rowCode;
    
    /**
     * 是否展示
     */
    private Integer isShow;
    
    /**
     * 排序，大值优先
     */
    private Integer orderNum;
    
}