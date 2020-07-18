package com.lyf.scm.core.domain.model.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 回调记录日志数据结构原始对象 <br>
 *
 * @Author wwh 2020/2/21
 */
@Data
public class CallRecordLogDO extends BaseDO implements Serializable {

    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 单据编号
     */
    private String recordCode;

    /**
     * 系统名称
     */
    private String systemName;

    /**
     * 请求服务名
     */
    private String requestService;

    /**
     * 请求url
     */
    private String requestUrl;

    /**
     * 请求内容
     */
    private String requestContent;

    /**
     * 响应内容
     */
    private String responseContent;

    /**
     * 交互状态 0失败  1成功
     */
    private Integer status;

    public CallRecordLogDO(String recordCode, String systemName, String requestService, String requestUrl, String requestContent, String responseContent, Integer status) {
        this.recordCode = recordCode;
        this.systemName = systemName;
        this.requestService = requestService;
        this.requestUrl = requestUrl;
        this.requestContent = requestContent;
        this.responseContent = responseContent;
        this.status = status;
    }
    public CallRecordLogDO(){

    }
}