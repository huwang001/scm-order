package com.lyf.scm.job.config;

import com.alibaba.fastjson.JSONObject;
import com.lyf.scm.common.model.KibanaLog;

/**
 * @Description: Kibana日志封装 <br>
 *
 * @Author chuwenchao 2020/3/11
 */
public class JobKibanaLog extends KibanaLog {

    private static final String PREF = "ScmOrderJob-";

    /**
     * @Description: 设置kibana日志<br>
     *
     * @Author chuwenchao 2020/3/11
     * @param logType
     * @return 
     */
    public static String getJobLog(String logType, String method, String desc, Object param) {
        JobKibanaLog logObj = new JobKibanaLog();
        logObj.setLogType(PREF + logType);
        logObj.setMethod(method);
        logObj.setDesc(desc);
        logObj.setParam(param);
        return JSONObject.toJSONString(logObj);
    }
}
