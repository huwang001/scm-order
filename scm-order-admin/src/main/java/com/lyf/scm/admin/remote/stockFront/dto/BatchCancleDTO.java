package com.lyf.scm.admin.remote.stockFront.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类BatchCancleDTO的实现描述：批量取消
 *
 * @author sunyj 2020/2/14 16:59
 */
@Data
@EqualsAndHashCode
public class BatchCancleDTO {

    /**
     * 取消列表
     */
    private String list;

    /**
     * 用户ID
     */
    private Long userId;

    private Long  realWarehouseId;

}
