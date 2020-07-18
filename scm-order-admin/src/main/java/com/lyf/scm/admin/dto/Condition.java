package com.lyf.scm.admin.dto;

import com.rome.arch.core.dto.DTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel
public class Condition extends DTO {

    /**
     * 每页大小
     */
    @ApiModelProperty("每页大小")
    public Integer pageSize;

    /**
     * 当前页开始记录数
     */
    @ApiModelProperty("当前页开始记录数")
    public Integer pageNum ;


}
