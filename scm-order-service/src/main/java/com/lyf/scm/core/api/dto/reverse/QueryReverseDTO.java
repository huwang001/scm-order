package com.lyf.scm.core.api.dto.reverse;

import com.lyf.scm.core.api.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName QueryReverseDTO
 * @Description 分页查询冲销单对象
 * @Author huwang
 * @Date 2020/7/179:38
 **/

@Data
public class QueryReverseDTO extends BaseDTO {

    @ApiModelProperty(value = "单据编号")
    private List<String> recordCodes;

    @ApiModelProperty(value = "单据类型 1出库单冲销 2入库单冲销")
    private Integer recordType;

    @ApiModelProperty(value = "单据状态 1已新建 2已取消 3已确认 11已出库 12已入库 13已过账")
    private Integer recordStatus;

    @ApiModelProperty(value = "原始出/入库单据编号")
    private List<String> originRecordCodes;

    @ApiModelProperty(value = "外部单据编号（出入库单对应的前置单号）")
    private List<String> outRecordCodes;

    @ApiModelProperty(value = "工厂编码/实仓编码")
    private List<String> warehouseCodes;

    @ApiModelProperty(value = "创建人")
    private Long creator;
}
