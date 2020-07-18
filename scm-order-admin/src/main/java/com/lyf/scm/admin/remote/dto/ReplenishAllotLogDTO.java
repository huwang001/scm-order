package com.lyf.scm.admin.remote.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode
public class ReplenishAllotLogDTO {
    /**
     * 唯一主键
     */
    private Long id;
    /**
     * 寻源类型  1-指定单据寻源, 2-当日寻源
     */
    private Integer allotType;
    /**
     * 寻源类型  1-指定单据寻源, 2-当日寻源
     */
    private String allotTypeName;
    /**
     * 寻源开始时间
     */
    private Date startTime;
    /**
     * 寻源结束时间
     */
    private Date endTime;
    /**
     * 当日寻源次数
     */
    private Integer times;
    /**
     * 寻源订单数
     */
    private Integer totalRecords;
    /**
     * 寻源成功订单数
     */
    private Integer successRecords;
    /**
     * 是否可用：0-否，1-是
     */
    private Integer isAvailable;
    /**
     * 是否逻辑删除：0-否，1-是
     */
    private Integer isDeleted;
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
     * 工厂编号
     */
    private String factoryCode;
    /**
     * 渠道编号
     */
    private String channelCode;
}
