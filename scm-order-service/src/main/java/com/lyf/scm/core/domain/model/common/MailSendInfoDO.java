package com.lyf.scm.core.domain.model.common;

import lombok.Data;

/**
 * @Description: 邮件发送表结构原始对象 <br>
 *
 * @Author chuwenchao 2020/4/8
 */
@Data
public class MailSendInfoDO extends BaseDO {
	
	//columns START
	/**
	 * 唯一主键
	 */
	private Long id;
	/**
	 * 发件人邮箱
	 */
	private String sendUserMail;
	/**
	 * 收件人邮箱
	 */
	private String receiveUserMail;
	/**
	 * 邮件标题
	 */
	private String mailTitle;
	/**
	 * 邮件内容
	 */
	private String mailContent;
	/**
	 * 邮件状态:0-无需发送,1-待发送,2-已发送
	 */
	private Integer sendStatus;
	/**
	 * 发送次数
	 */
	private Integer sendTimes;
}

	
