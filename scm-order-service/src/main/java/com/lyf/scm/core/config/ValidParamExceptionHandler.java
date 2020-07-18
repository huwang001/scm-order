package com.lyf.scm.core.config;

import com.alibaba.fastjson.JSONObject;
import com.lyf.scm.common.constants.ResCode;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: spring Validated 校验处理统一异常处理器 <br>
 *
 * @Author chuwenchao 2020/7/13
 * @return
 */
@Slf4j
@ControllerAdvice
public class ValidParamExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        // 根据自定义注解值决定响应对象
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder errorMessage = new StringBuilder();
        for(FieldError fieldError:bindingResult.getFieldErrors()){
            errorMessage.append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage()).append(",");
        }
        log.error("请求参数==> {} 异常 ==> {}", JSONObject.toJSONString(bindingResult.getTarget()), errorMessage.toString());
        return Response.builderFail(ResCode.ORDER_ERROR_1002_DESC, errorMessage.toString());
    }
}
