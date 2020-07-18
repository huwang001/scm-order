package com.lyf.scm.core.domain.entity.stockFront;

import com.lyf.scm.core.domain.model.stockFront.FrontRecordDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 前置单
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FrontRecordE extends FrontRecordDO {
    /**
     * 备注
     */
    private String remark;
    /**
     * 门店编号
     */
    private String shopCode;
    /**
     * 门店名称
     */
    private String shopName;
}
