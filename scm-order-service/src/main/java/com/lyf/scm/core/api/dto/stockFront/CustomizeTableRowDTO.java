package com.lyf.scm.core.api.dto.stockFront;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

import com.lyf.scm.core.api.dto.BaseDTO;
/**
 * 自定义标题
 * 
 * @author WWH
 *
 */
@Data
public class CustomizeTableRowDTO extends BaseDTO implements Serializable {
	
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
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 创建人
     */
    private Long creator;
    
    /**
     * 更新人
     */
    private Long modifier;
    
    /**
     * 是否可用
     */
    private Integer isAvailable;
    
    /**
     * 是否删除
     */
    private Integer isDeleted;
    
}