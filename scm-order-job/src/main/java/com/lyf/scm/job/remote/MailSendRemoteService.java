package com.lyf.scm.job.remote;

import com.lyf.scm.job.api.dto.MailSendInfoDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Description: 邮件发送远程接口 <br>
 *
 * @Author chuwenchao 2020/4/9
 */
@FeignClient(value = "scm-order-service")
public interface MailSendRemoteService {

	@RequestMapping(value = "/order/v1/mailSend/queryMailInfoIntervalSeven", method = RequestMethod.GET)
    Response<List<MailSendInfoDTO>> queryMailInfoIntervalSeven();

	@RequestMapping(value = "/order/v1/mailSend/sendMailJob", method = RequestMethod.POST)
    Response sendMailJob(@RequestBody MailSendInfoDTO messageDTO);
}
