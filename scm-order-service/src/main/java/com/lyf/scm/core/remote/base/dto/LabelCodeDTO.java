package com.lyf.scm.core.remote.base.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * LabelCodeDTO
 *
 * @author zhaowei
 * @date 2019/05/05
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabelCodeDTO implements Serializable {
    private static final long serialVersionUID = -5624666925027528957L;

    private Long id;

    private String name;

    private String groupCode;

    private Integer type;
}
