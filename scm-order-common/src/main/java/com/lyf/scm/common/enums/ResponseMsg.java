package com.lyf.scm.common.enums;

import com.rome.arch.core.clientobject.Response;

public enum ResponseMsg {

    SUCCESS("0", "操作成功"),
    FAIL("1", "操作失败"),
    PARAM_ERROR("2", "参数有误"),
    EXCEPTION("3", "系统异常");

    private String code;
    private String msg;

    ResponseMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }
    public String getMsg() {
        return this.msg;
    }

    public Response<Object> buildMsg() {
        return buildMsg(null);
    }

    public Response buildMsg(String code, String msg) {
        return Response.builderFail(code, msg);
    }

    public Response buildMsg(Object data) {
        if (this == SUCCESS) {
            return Response.builderSuccess(data);
        }
        return Response.builderFail(code, msg);
    }

    public Response buildMsgWithSelf() {
        return Response.builderFail(this.code, this.msg);
    }
}
