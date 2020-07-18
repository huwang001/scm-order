package com.lyf.scm.core.service.common.impl;

import com.alibaba.fastjson.JSONObject;
import com.lyf.scm.core.api.dto.common.MailSendInfoDTO;
import com.lyf.scm.core.domain.convert.common.MailSendInfoConvert;
import com.lyf.scm.core.domain.entity.common.MailSendInfoE;
import com.lyf.scm.core.mapper.common.MailSendInfoMapper;
import com.lyf.scm.core.service.common.MailSendInfoService;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;

/**
 * @Description: 邮件服务实现
 * <p>
 * @Author: chuwenchao  2020/4/8
 */
@Slf4j
@Service("mailSendInfoService")
public class MailSendInfoServiceImpl implements MailSendInfoService {

    @Value("${spring.mail.username}")
    private String sendUserMail;

    @Resource
    private MailSendInfoMapper mailSendInfoMapper;
    @Resource
    private MailSendInfoConvert mailSendInfoConvert;
    @Resource
    private JavaMailSender javaMailSender;

    /**
     * @Description: 查询近7天待发送邮件 <br>
     *
     * @Author chuwenchao 2020/4/9
     * @return
     */
    @Override
    public List<MailSendInfoDTO> queryMailInfoIntervalSeven() {
        Date endTime = new Date();
        Date startTime = DateUtils.addDays(endTime,-7);
        List<MailSendInfoE> mailMessageES = mailSendInfoMapper.queryMailByStatus(startTime, endTime);
        return mailSendInfoConvert.convertEList2DTOList(mailMessageES);
    }

    /**
     * @Description: 发送邮件 <br>
     *
     * @Author chuwenchao 2020/4/9
     * @param mailSendInfoDTO
     * @return
     */
    @Override
    public void sendMailJob(MailSendInfoDTO mailSendInfoDTO) {
        this.sendMimeMailMessageForHtml(mailSendInfoDTO);
        mailSendInfoMapper.updateStatusToComplete(mailSendInfoDTO.getId());
    }

    /**
     * @Description: 异步发送邮件 <br>
     *
     * @Author chuwenchao 2020/4/13
     * @param mailSendInfoDTO
     * @return
     */
    @Override
    public int asyncSendMail(MailSendInfoDTO mailSendInfoDTO) {
        return mailSendInfoMapper.saveSendMail(mailSendInfoDTO);
    }

    /**
     * 发送html格式的邮件
     * @param mailMessageDTO
     * @throws Exception
     */
    private void sendMimeMailMessageForHtml(MailSendInfoDTO mailMessageDTO){
        try {
            if(null != mailMessageDTO.getReceiveUserMail()){
                String receiveUserMail = mailMessageDTO.getReceiveUserMail();
                String[] split = receiveUserMail.split(",");
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom(mailMessageDTO.getSendUserMail());
                helper.setTo(split);
                helper.setSubject(mailMessageDTO.getMailTitle());
                helper.setText(mailMessageDTO.getMailContent(), true);
                javaMailSender.send(mimeMessage);
            }
        }catch (Exception e){
            log.error("邮件发送失败,入参 ==> {}", JSONObject.toJSONString(mailMessageDTO), e);
            throw new RomeException(mailMessageDTO.getSendUserMail() + "发送给" + mailMessageDTO.getReceiveUserMail() + "的邮件发送失败!");
        }
    }

}
