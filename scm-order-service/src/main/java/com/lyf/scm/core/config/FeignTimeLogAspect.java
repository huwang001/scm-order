/**
 * Filename RemoteTimeLogAspect.java
 * Company 上海来伊份科技有限公司。
 * @author xly
 * @version 
 */
package com.lyf.scm.core.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lyf.scm.common.constants.KibanaLogConstants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: Feign远程调用耗时日志 <br>
 *
 * @Author chuwenchao 2020/7/11
 */
@Slf4j
@Aspect
@Component
public class FeignTimeLogAspect {

    @Around(value = "(execution(* com.lyf.scm.core.remote..*.*(..))) && @annotation(mLog)")
    public Object around(ProceedingJoinPoint point, RequestMapping mLog) throws Throwable {
    	Object result = null;
        long time = System.currentTimeMillis();
        try {
            result = point.proceed();
            return result;
        } catch (Throwable e) {
            result = e.getMessage();
            throw e;
        } finally {
            // 异步记录日志
            time = System.currentTimeMillis() - time;
            if(time >= 2000) {
            	try {
            		@SuppressWarnings("unchecked")
					FeignClient f = (FeignClient) point.getSignature().getDeclaringType().getAnnotation(FeignClient.class);
            		if(f != null && !(f instanceof FeignClient)) {
            			f = null;
            		}
                    Object[] args = point.getArgs();
                    String[] paramNames = ((CodeSignature) point.getSignature()).getParameterNames();
                    MethodSignature signature = (MethodSignature) point.getSignature();
                    Method method = signature.getMethod();
                    if (null == paramNames) {
                        Parameter[] parameters = method.getParameters();
                        paramNames = new String[parameters.length];
                        for (int i = 0; i < parameters.length; i++) {
                            paramNames[i] = parameters[i].getName();
                        }
                    }
                    Map<String, Object> param = new HashMap<>();
                    for(int i= 0; i < args.length; i++) {
                        param.put(paramNames[i], args[i]);
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("timeout", time);
                    jsonObject.put("serverpool", f == null ? "" : f.value());
                    jsonObject.put("param", JSON.toJSONString(param));
                    jsonObject.put("result", result);
                    log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.OUTER_SERVICE_TIME_OUT, JSONObject.toJSONString(mLog.value()), "Feign远程调用耗时记录", jsonObject));
				} catch (Throwable e2) {
            	    log.info("feign调用耗时日志异常。。。", e2);
				}
            }
        }
    }
}
