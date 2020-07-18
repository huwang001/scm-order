package com.lyf.scm.core.remote.stock.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Doc:.....
 * @Author: lchy
 * @Date: 2020/7/15
 * @Version 1.0
 */
@Data
@EqualsAndHashCode
public class BatchCreateRecordDTO {

    /** 出库单列表 */
    private List<OutWarehouseRecordDTO> outList;

    /** 入库单列表 */
    private List<InWarehouseRecordDTO> inList;

}
