package com.lyf.scm.core.config;

import java.lang.annotation.*;

/**
 * @Description: Scm调用外部接口日志注解 <br>
 *
 * @Author chuwenchao 2020/3/11
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ScmCallLog {

    /**
     * 系统名称
     */
    String systemName();

    /**
     * 记录编号
     */
    String recordCode();

    /**
     * 是否存数据库
     */
    boolean needDB() default true;
}
