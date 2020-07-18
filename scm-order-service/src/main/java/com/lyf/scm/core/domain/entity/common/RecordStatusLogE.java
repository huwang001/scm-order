package com.lyf.scm.core.domain.entity.common;

import com.lyf.scm.core.domain.model.common.RecordStatusLogDO;
import lombok.Data;

/**
 * @Description: RecordStatusLogDO
 * <p>
 * @Author: chuwenchao  2020/2/21
 */
@Data
public class RecordStatusLogE extends RecordStatusLogDO {

    public RecordStatusLogE(String orderCode, Integer recordStatus) {
        super(orderCode, recordStatus);
    }

    public RecordStatusLogE() {
    }
}
