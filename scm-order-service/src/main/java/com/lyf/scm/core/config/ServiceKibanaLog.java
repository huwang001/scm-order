package com.lyf.scm.core.config;

import com.alibaba.fastjson.JSONObject;
import com.lyf.scm.common.model.KibanaLog;

/**
 * @Description: Kibana日志封装 <br>
 *
 * @Author chuwenchao 2020/3/11
 */
public class ServiceKibanaLog extends KibanaLog {

    private static final String PREF = "ScmOrderService-";

    /**
     * @Description: 设置kibana日志<br>
     *
     * @Author chuwenchao 2020/3/11
     * @param logType
     * @return 
     */
    public static String getServiceLog(String logType, String method, String desc, Object param) {
        ServiceKibanaLog logObj = new ServiceKibanaLog();
        logObj.setLogType(PREF + logType);
        logObj.setMethod(method);
        logObj.setDesc(desc);
        logObj.setParam(param);
        return JSONObject.toJSONString(logObj);
    }
}
