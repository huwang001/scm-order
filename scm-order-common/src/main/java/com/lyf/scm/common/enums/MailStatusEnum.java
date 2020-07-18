package com.lyf.scm.common.enums;

/**
 * @Description: 邮件发送状态Enum <br>
 *
 * @Author chuwenchao 2020/4/9
 */
public enum MailStatusEnum {

    MAIL_SEND_NO(0,"无需发送"),
    MAIL_SEND_WAIT(1,"等待发送"),
    MAIL_SEND_COMPLETE(2,"发送成功");

    private Integer status;

    private String desc;

    MailStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }}
