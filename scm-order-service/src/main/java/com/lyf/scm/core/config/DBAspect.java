/**
 * Filename DBAspect.java
 * Company 上海来伊份科技有限公司。
 * @author xly
 * @version 
 */
package com.lyf.scm.core.config;

import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

/**
 * 目的：自动对数据公用字段的设置 
 * @author xly
 * @since 2019年4月24日 上午11:45:34
 */
@Aspect
@Component
@Slf4j
public class DBAspect {

	 private final int DEFAULT_VERSION_NO = 0;
	    private final int DEFAULT_NOTDELETED = 0;
	    private final int DEFAULT_AVAILABLE = 1;
	    
	    @Before("execution(* com.lyf.scm.core.mapper..*.insert*(..))")
	    public void mabatisInsert(JoinPoint jp) throws Exception {
	       insert(jp);
	    }
	    
	    @Before("execution(* com.lyf.scm.core.mapper..*.save*(..))")
	    public void mabatisSave(JoinPoint jp) throws Exception {
	       insert(jp);
	    }
	    
	    @Before("execution(* com.lyf.scm.core.mapper..*.batchInsert*(..))")
	    public void mabatisBatchInsert(JoinPoint jp) throws Exception {
	       insert(jp);
	    }

	    public void insert(JoinPoint jp) throws Exception {
	        if (jp.getArgs() == null) {
	            return;
	        }
	        for (Object bean : jp.getArgs()) {
	            if (bean instanceof String) {
	                continue;
	            } else if(bean instanceof Byte) {
					continue;
				} else if(bean instanceof Integer) {
					continue;
				} else if(bean instanceof Long) {
					continue;
				} else if(bean instanceof Double) {
					continue;
				} else if(bean instanceof BigDecimal) {
					continue;
				}

	            try {
	                if(bean instanceof Collection){
	                    Collection<?> col = (Collection<?>) bean;
	                    for (Object o : col) {
	                        setupDefaultValue(o);
	                    }
	                }else{
	                    setupDefaultValue(bean);
	                }


	            }catch(RomeException be){
	                throw be;
	            }catch (Exception e) {
	                log.error(e.getMessage(), e);
					throw e;
	            }
	        }
	    }

	    private void setupDefaultValue(Object bean) throws Exception {
	        String isDeleted = "isDeleted";
	        if (PropertyUtils.getProperty(bean, isDeleted) == null) {
	            PropertyUtils.setProperty(bean, isDeleted, DEFAULT_NOTDELETED);
	        }
	        String creator = "creator";
	        if (PropertyUtils.isReadable(bean, creator) && PropertyUtils.getProperty(bean, creator) == null) {
	            PropertyUtils.setProperty(bean, creator, 0L);
	        }
	        String modifier = "modifier";
	        if (PropertyUtils.isReadable(bean, modifier) && PropertyUtils.getProperty(bean, modifier) == null) {
	            PropertyUtils.setProperty(bean, modifier, 0L);
	        }
	        String isAvailable = "isAvailable";
	        if (PropertyUtils.isReadable(bean, isAvailable) && PropertyUtils.getProperty(bean, isAvailable) == null) {
	            PropertyUtils.setProperty(bean, isAvailable, DEFAULT_AVAILABLE);
	        }
	        String versionNo = "versionNo";
	        if (PropertyUtils.isReadable(bean, versionNo) &&PropertyUtils.getProperty(bean, versionNo) == null) {
	            PropertyUtils.setProperty(bean, versionNo, DEFAULT_VERSION_NO);
	        }
	        String createTime = "createTime";
	        if (PropertyUtils.isReadable(bean, createTime) &&PropertyUtils.getProperty(bean, createTime) == null) {
	            PropertyUtils.setProperty(bean, createTime, new Date());
	        }
	        String updateTime = "updateTime";
	        if (PropertyUtils.isReadable(bean, updateTime) &&PropertyUtils.getProperty(bean, updateTime) == null) {
	            PropertyUtils.setProperty(bean, updateTime, new Date());
	        }
	        // 租户ID
	        String tenantId = "tenantId";
	        if (PropertyUtils.isReadable(bean, tenantId) &&PropertyUtils.getProperty(bean, tenantId) == null) {
	            PropertyUtils.setProperty(bean, tenantId, 0L);
	        }
	        // 业务应用ID
	        String appId = "appId";
	        if (PropertyUtils.isReadable(bean, appId) &&PropertyUtils.getProperty(bean, appId) == null) {
	            PropertyUtils.setProperty(bean, appId, "0");
	        }
	    }
}
