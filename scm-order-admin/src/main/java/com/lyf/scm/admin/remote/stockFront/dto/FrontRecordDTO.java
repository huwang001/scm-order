package com.lyf.scm.admin.remote.stockFront.dto;

import com.lyf.scm.common.model.Pagination;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class FrontRecordDTO extends Pagination {

    /**
     * 渠道编号
     */
    private String channelCode;

    /**
     * 店铺编码
     */
    private String shopCode;


    /**
     * 单据编码
     */
    private String recordCode;

    /**
     * 单据类型
     */
    private String recordType;

    /**
     * 外部单据
     */
    private  String outRecordCode;

    /**
     * 备注
     */
    private String remark;
    /**
     * 门店名称
     */
    private String shopName;

    /**
     * 单据明细
     */
    private List<FrontRecordDetailDTO> frontRecordDetails;

}
