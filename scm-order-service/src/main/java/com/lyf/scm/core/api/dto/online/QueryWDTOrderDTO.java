package com.lyf.scm.core.api.dto.online;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/2
 */
@Data
@EqualsAndHashCode
public class QueryWDTOrderDTO implements Serializable {
    private String originOrderCode;
    private String outRecordCode;
    private List<String> originOrderCodeList;
    private List<String> outRecordCodeList;
    private String channelCode;
    private List<String> channelCodeList;
    private Long realWarehouseId;
    private Integer recordStatus;
    private Date startTime;
    private Date endTime;
    private Integer splitType;
    private List<Integer> splitTypeList;
    private Integer allotStatus;
    private String logisticsCode;
    private String provinceCode;
    private List<String> provinceCodes;

    private String skuCodeStr;
    private List<String> skuCodes;

    private List<Long> frontRecordIds;
    private String warehouseCodeStr;


}
