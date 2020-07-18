package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类CreateWhallotDTO的实现描述：团购创建调拨单
 *
 * @author sunyj 2020/4/17 10:53
 */
@Data
@EqualsAndHashCode
public class CreateWhallotDTO {
    /**
     * 外部单号
     */
    private String outRecordCode;

    /**
     * 入库工厂编号
     */
    private String inFactoryCode;

    /**
     * 入库仓库编号
     */
    private String inWarehouseCode;

    /**
     * 用户ID
     */
    private Long  userId;

}
