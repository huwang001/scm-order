package com.lyf.scm.job.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode
public class MailSendInfoDTO {

    @ApiModelProperty("唯一标识")
    private Long id;

    @ApiModelProperty("发件人邮箱地址")
    private String sendUserMail;

    @ApiModelProperty("收件人邮箱")
    private String receiveUserMail;

    @ApiModelProperty("邮件标题")
    private String mailTitle;

    @ApiModelProperty("邮件内容")
    private String mailContent;

    @ApiModelProperty("发送状态")
    private Integer sendStatus;

    @ApiModelProperty("发送次数")
    private Integer sendTimes;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
