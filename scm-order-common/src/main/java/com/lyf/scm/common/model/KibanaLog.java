package com.lyf.scm.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: Kibana公共日志封装对象
 * <p>
 * @Author: chuwenchao  2020/3/11
 */
@Data
public class KibanaLog implements Serializable {

    /**
     * 日志索引名
     */
    private String logType;

    /**
     * 方法名
     */
    private String method;

    /**
     * 描述
     */
    private String desc;

    /**
     * 日志参数
     */
    private Object param;

}
