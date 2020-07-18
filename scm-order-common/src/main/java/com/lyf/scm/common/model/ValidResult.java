package com.lyf.scm.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidResult {
    /**
     * 错误编码
     */
    private String resCode;
    /**
     * 错误描述
     */
    private String resDesc;
}
