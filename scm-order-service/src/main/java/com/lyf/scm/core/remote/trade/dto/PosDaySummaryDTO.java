package com.lyf.scm.core.remote.trade.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 门店日结
 *
 * @author lei.jin
 */
@Data
@EqualsAndHashCode
public class PosDaySummaryDTO {

    private String appId;

    private String channelCode;

    private Date createTime;

    private String creator;

    private String extData;

    private Integer isDeleted;

    private Integer merchantId;

    private String modifier;

    private String shopCode;

    private Date summaryDate;

    private Integer summaryStatus;

    private String tenantId;

    private Date updateTime;

    private Integer versionNo;
}
