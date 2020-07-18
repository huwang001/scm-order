package com.lyf.scm.core.domain.convert.common;

import com.lyf.scm.core.api.dto.common.MailSendInfoDTO;
import com.lyf.scm.core.domain.entity.common.MailSendInfoE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description: 服务model转换器 <br>
 *
 * @Author chuwenchao 2020/4/9
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface MailSendInfoConvert {

    MailSendInfoDTO convertE2DTO(MailSendInfoE mailSendInfoE);

    List<MailSendInfoDTO> convertEList2DTOList(List<MailSendInfoE> mailSendInfoEList);
}
