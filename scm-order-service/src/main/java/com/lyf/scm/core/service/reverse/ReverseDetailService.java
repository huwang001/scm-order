package com.lyf.scm.core.service.reverse;

import com.lyf.scm.core.api.dto.reverse.ReverseDetailDTO;

import java.util.List;

/**
 * @Desc:冲销单明细接口对象
 * @author:Huangyl
 * @date: 2020/7/17
 */
public interface ReverseDetailService {


    void batchCreateReverseDetail(List<ReverseDetailDTO> reverseDetailDTOList,Long userId);

}
