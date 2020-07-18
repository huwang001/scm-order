package com.lyf.scm.common.util.validate;

import com.lyf.scm.common.constants.ResCode;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description: 对象校验
 * <p>
 * @Author: chuwenchao  2019/8/11
 */
public class BeanValidateUtils {


    /**
     * @Description: 对象参数校验 <br>
     *
     * @Author chuwenchao 2019/8/11
     * @param obj
     * @return
     */
    public static <T> void validate(T obj) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        if(!constraintViolations.isEmpty()){
            StringBuffer sb = new StringBuffer("");
            for (Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator(); iterator.hasNext();) {
                ConstraintViolation<T> constraintViolation = (ConstraintViolation<T>) iterator.next();
                sb.append(constraintViolation.getPropertyPath() + ":" + constraintViolation.getMessage() + ",");
            }
            AlikAssert.isTrue(false, ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + ":" + sb.toString());
        }
    }

}
