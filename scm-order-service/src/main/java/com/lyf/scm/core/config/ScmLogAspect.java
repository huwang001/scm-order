package com.lyf.scm.core.config;

import com.alibaba.fastjson.JSONObject;
import com.lyf.scm.common.constants.CommonConstants;
import com.lyf.scm.core.domain.entity.common.CallRecordLogE;
import com.lyf.scm.core.remote.log.CallLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: SCM外部调用日志处理
 * <p>
 * @Author: chuwenchao  2020/3/11
 */
@Slf4j
@Aspect
@Component
public class ScmLogAspect {

    @Resource
    private ApplicationEventPublisher publisher;

    //解析spel表达式
    ExpressionParser parser = new SpelExpressionParser();
    //将方法参数纳入Spring管理
    LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    // 切点为有@ScmCallLog注解的方法
    @Pointcut("@annotation(com.lyf.scm.core.config.ScmCallLog)")
    public void scmLogPointCut() {
    }

    @Around(value = "scmLogPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        CallRecordLogE callRecordLogE = new CallRecordLogE();
        // 获取方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        ScmCallLog scmCallLog = method.getAnnotation(ScmCallLog.class);
        try {
            String recordCode = this.getRecordCode(point);
            callRecordLogE.setStatus(0);
            callRecordLogE.setRecordCode(recordCode);
            callRecordLogE.setRequestService(method.getName());
            // 获取请求url
            String url = "";
            RequestMapping classAnnotation = AnnotationUtils.findAnnotation(signature.getMethod().getDeclaringClass(), RequestMapping.class);
            if (null != classAnnotation) {
                url += classAnnotation.value()[0];
            }
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                url += requestMapping.value()[0];
            } else if (method.isAnnotationPresent(PostMapping.class)) {
                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                url += postMapping.value()[0];
            } else if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                url += getMapping.value()[0];
            }
            callRecordLogE.setRequestUrl(url);
            String systemName = scmCallLog.systemName();
            callRecordLogE.setSystemName(systemName);
            // 获取请求参数
            Object[] args = point.getArgs();
            String[] paramNames = ((CodeSignature) point.getSignature()).getParameterNames();
            if (null == paramNames) {
                Parameter[] parameters = method.getParameters();
                paramNames = new String[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    paramNames[i] = parameters[i].getName();
                }
            }
            Map<String, Object> param = new HashMap<>();
            for (int i = 0; i < args.length; i++) {
                param.put(paramNames[i], args[i]);
            }
            callRecordLogE.setRequestContent(JSONObject.toJSONString(param));
        } catch (Exception e) {
            log.warn("接口日志处理异常：", e);
        }
        Object result = null;
        try {
            result = point.proceed();
            return result;
        } catch (Exception e) {
            result = e.getMessage() != null ? e.getMessage() : e.toString();
            throw e;
        } finally {
            // 响应结果
            if (result != null) {
                String responseContent = JSONObject.toJSONString(result);
                if (responseContent.contains(CommonConstants.SUCCESS)) {
                    callRecordLogE.setStatus(1);
                }
                callRecordLogE.setResponseContent(responseContent);
            }else {
                callRecordLogE.setResponseContent("");
            }
            if (scmCallLog.needDB()) {
                try {
                    publisher.publishEvent(new CallLogEvent(callRecordLogE));
                } catch (Exception e) {
                    log.error("保存调用记录异常，参数==> {}", JSONObject.toJSONString(callRecordLogE), e);
                }
            }
        }
    }

    private String getRecordCode(ProceedingJoinPoint point) {
        String res = "";
        try {
            //获取参数对象数组
            Object[] args = point.getArgs();
            //获取方法
            Method method = ((MethodSignature) point.getSignature()).getMethod();
            ScmCallLog scmCallLog = method.getAnnotation(ScmCallLog.class);
            String spel = scmCallLog.recordCode();
            //获取方法参数名
            String[] params = discoverer.getParameterNames(method);
            if (null == params) {
                Parameter[] parameters = method.getParameters();
                params = new String[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    params[i] = parameters[i].getName();
                }
            }
            //将参数纳入Spring管理
            EvaluationContext context = new StandardEvaluationContext();
            for (int len = 0; len < params.length; len++) {
                context.setVariable(params[len], args[len]);
            }
            Expression expression = parser.parseExpression(spel);
            res = expression.getValue(context, String.class);
        } catch (Exception e) {
            log.warn("接口日志获取单据号异常：", e);
            res = "";
        }
        return res;
    }
}
