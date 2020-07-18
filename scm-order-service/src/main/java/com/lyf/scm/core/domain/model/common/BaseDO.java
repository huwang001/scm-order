package com.lyf.scm.core.domain.model.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Description: Model 公共字段抽取 <br>
 * @Author chuwenchao 2020/2/21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseDO {

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
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
     * 是否可用：0-否，1-是
     */
    private Integer isAvailable;
    /**
     * 是否逻辑删除：0-否，1-是
     */
    private Integer isDeleted;
    /**
     * 版本号:默认0,每次更新+1
     */
    private Integer versionNo;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 业务应用ID
     */
    private String appId;

}
