package com.lyf.scm.admin.config;

import com.alibaba.fastjson.JSONObject;
import com.lyf.scm.common.model.KibanaLog;

/**
 * @Description: Kibana日志封装 <br>
 *
 * @Author chuwenchao 2020/3/11
 */
public class AdminKibanaLog extends KibanaLog {

    private static final String PREF = "ScmOrderAdmin-";

    /**
     * @Description: 设置kibana日志<br>
     *
     * @Author chuwenchao 2020/3/11
     * @param logType
     * @return
     */
    public static String getAdminLog(String logType, String method, String desc, Object param) {
        AdminKibanaLog logObj = new AdminKibanaLog();
        logObj.setLogType(PREF + logType);
        logObj.setMethod(method);
        logObj.setDesc(desc);
        logObj.setParam(param);
        return JSONObject.toJSONString(logObj);
    }
}
