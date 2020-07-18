package com.lyf.scm.core.mapper.online;

import com.lyf.scm.core.domain.entity.online.WDTLogE;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/2
 */


public interface WDTLogMapper {
    /**
     * 保存
     */
    void saveLog(WDTLogE entity);

    // List<FrWDTLogDO> queryOperateLog(WDTLogPageParamDTO logDtO);

}
