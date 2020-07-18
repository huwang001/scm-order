package com.lyf.scm.common.util.validate;

import com.lyf.scm.common.constants.CommonConstants;
import com.lyf.scm.common.constants.ResCode;
import com.rome.arch.core.clientobject.Response;

import java.io.Serializable;

/**
 * @Description: 响应结果处理工具类
 * <p>
 * @Author: chuwenchao  2019/11/15
 */
public class ResponseValidateUtils implements Serializable {

    /**
     * @Description: 响应结果校验，失败抛异常 <br>
     *
     * @Author chuwenchao 2019/11/15
     * @param response
     * @return 
     */
    public static void validResponse(Response response) {
        if (null == response) {
            AlikAssert.isTrue(false, ResCode.ORDER_ERROR_1001, ResCode.ORDER_ERROR_1001_DESC);
        } else if(!CommonConstants.CODE_SUCCESS.equals(response.getCode())) {
            AlikAssert.isTrue(false, response.getCode(), String.valueOf(response.getMsg()));
        }
    }

    /**
     * @Description: SAP响应结果校验，失败抛异常 <br>
     *
     * @Author chuwenchao 2020/7/11
     * @param response
     * @return
     */
    public static void validSAPResponse(Response response) {
        if (null == response) {
            AlikAssert.isTrue(false, ResCode.ORDER_ERROR_1001, ResCode.ORDER_ERROR_1001_DESC);
        } else if(!CommonConstants.SAP_SUCCESS.equals(response.getCode())) {
            AlikAssert.isTrue(false, response.getCode(), String.valueOf(response.getMsg()));
        }
    }

}
