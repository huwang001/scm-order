package com.lyf.scm.job.remote.facade;

import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.job.api.dto.MailSendInfoDTO;
import com.lyf.scm.job.remote.MailSendRemoteService;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 邮件发送 <br>
 *
 * @Author chuwenchao 2020/4/9
 */
@Slf4j
@Component
public class MailSendFacade {

    @Resource
    private MailSendRemoteService mailSendRemoteService;

    /**
     * @Description: 查询待发送邮件列表接口 <br>
     *
     * @Author chuwenchao 2020/4/9
     * @return
     */
    public List<MailSendInfoDTO> queryMailInfoIntervalSeven(){
        Response<List<MailSendInfoDTO>> response = mailSendRemoteService.queryMailInfoIntervalSeven();
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * @Description: 发送邮件 <br>
     *
     * @Author chuwenchao 2020/4/9
     * @param mailSendInfoDTO
     * @return 
     */
    public void sendMailJob(MailSendInfoDTO mailSendInfoDTO){
        mailSendRemoteService.sendMailJob(mailSendInfoDTO);
    }

}
