package com.lyf.scm.core.domain.model.order;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 订单序列号 <br>
 *
 * @Author chuwenchao 2020/7/2
 */
@Data
public class OrderSequenceDO implements Serializable {
    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 最大值
     */
    private Long sectionNum;
    
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新版本
     */
    private Long version;

}
