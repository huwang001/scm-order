package com.lyf.scm.core.mapper.common;

import com.lyf.scm.core.api.dto.common.MailSendInfoDTO;
import com.lyf.scm.core.domain.entity.common.MailSendInfoE;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MailSendInfoMapper {

    /**
     * @Description: 查询近7天待发送邮件 <br>
     *
     * @Author chuwenchao 2020/4/9
     * @return
     */
    List<MailSendInfoE> queryMailByStatus(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * @Description: 更新发送邮件状态及次数 <br>
     *
     * @Author chuwenchao 2020/4/9
     * @return
     */
    int updateStatusToComplete(@Param("id") Long id);

    /**
     * @Description: 保存邮件发送信息 <br>
     *
     * @Author chuwenchao 2020/4/13
     * @param mailSendInfoDTO
     * @return 
     */
    int saveSendMail(MailSendInfoDTO mailSendInfoDTO);
    
}
