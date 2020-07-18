package com.lyf.scm.core.service.common;

import com.lyf.scm.core.api.dto.common.MailSendInfoDTO;

import java.util.List;

/**
 * @Description: 邮件服务 <br>
 *
 * @Author chuwenchao 2020/4/8
 */
public interface MailSendInfoService {

    /**
     * @Description: 查询近7天待发送邮件 <br>
     *
     * @Author chuwenchao 2020/4/9
     * @return 
     */
    List<MailSendInfoDTO> queryMailInfoIntervalSeven();

    /**
     * @Description: 【定时器】发送邮件 <br>
     *
     * @Author chuwenchao 2020/4/9
     * @param messageDTO
     * @return 
     */
    void sendMailJob(MailSendInfoDTO messageDTO);

    /**
     * @Description: 异步发送邮件 <br>
     *
     * @Author chuwenchao 2020/4/13
     * @param mailSendInfoDTO
     * @return 
     */
    int asyncSendMail(MailSendInfoDTO mailSendInfoDTO);

}
