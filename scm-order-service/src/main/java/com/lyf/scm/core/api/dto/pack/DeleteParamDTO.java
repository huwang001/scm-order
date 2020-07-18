package com.lyf.scm.core.api.dto.pack;

import lombok.Data;

/**
 * @Remarks
 * @date 2020/7/7
 */
@Data
public class DeleteParamDTO {

    /**
     * 包装类型 1:组装2:反拆3:自定义组合4:自定义反拆5:拆箱
     */
    private Integer packType;

    /**
     * 需求编码
     */
    private String recordCode;

    /**
     * 自定义组合码
     */
    private String customGroupCode;

    /**
     * 商品编码
     */
    private String skuCode;

    /**
     * 成品表主键
     */
    private Long id;

}
