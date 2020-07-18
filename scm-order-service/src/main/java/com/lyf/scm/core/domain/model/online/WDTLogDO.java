package com.lyf.scm.core.domain.model.online;

import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class WDTLogDO extends BaseDO {

    /**
     * 唯一主键
     */
    private Long id;
    /**
     * 所属单据编码
     */
    private String recordCode;
    private String originOrderCode;
    private String outRecordCode;
    /**
     * 类型 1：拆单  2:修改仓库 3、修改物流 4、仓库物流一起修改
     */
    private Integer type;

    private String beforeValue;
    private String afterValue;


}

