package com.lyf.scm.core.config;

import com.alibaba.fastjson.JSONObject;
import com.lyf.scm.common.constants.KibanaLogConstants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 内部接口调用超时输出Kibana日志AOP <br>
 *
 * @Author chuwenchao 2020/3/12
 */
@Slf4j
@Aspect
@Component
public class ScmInnerTimeAspect {
    
    //切点为有api.controller中所有方法
    @Pointcut("execution(* com.lyf.scm.core.api.controller..*.*(..))")
    public void timePointCut() {
    }

    @Around(value = "timePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long time = System.currentTimeMillis();
        try {
            result = point.proceed();
            return result;
        } catch (Exception e) {
            result = e.getMessage();
            throw e;
        } finally {
            time = System.currentTimeMillis() - time;
            // 获取方法签名
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            // 判断是否是指定注解
            boolean flag = false;
            Annotation[] annotations = method.getDeclaredAnnotations();
            for(Annotation annotation : annotations) {
                String name = annotation.annotationType().getName();
                if(name.endsWith("RequestMapping") || name.endsWith("GetMapping") || name.endsWith("PostMapping")) {
                    flag = true;
                    break;
                }
            }
            if(flag && time >= 2000) {
                try {
                    Object[] args = point.getArgs();
                    String[] paramNames = ((CodeSignature) point
                            .getSignature()).getParameterNames();
                    Map<String, Object> param = new HashMap<>();
                    for(int i= 0; i < args.length; i++) {
                        param.put(paramNames[i], args[i]);
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("time", time);
                    jsonObject.put("param", param);
                    jsonObject.put("result", result);
                    log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.INNER_SERVICE_TIME_OUT, method.getName(), "scm-order方法【"+ method.getName() +"】执行耗时【"+ time +"】", jsonObject));
                } catch (Exception e) {
                    log.info("内部controller调用耗时日志异常。。。", e);
                }
            }
        }
    }

}
