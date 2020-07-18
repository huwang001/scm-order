package com.lyf.scm.core.domain.entity.stockFront;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FrReplenishRecordResultE implements Serializable {

    private String recordCode;
    private String outRecordCode;
    private String shopCode;
    private Integer recordType;
    private Integer recordStatus;
    private Integer inRealWarehouseId;
    private Integer outRealWarehouseId;
    private String realWarehouseCode;
    private String inRwName;
    private String outRwName;
    private Date createTime;

}
