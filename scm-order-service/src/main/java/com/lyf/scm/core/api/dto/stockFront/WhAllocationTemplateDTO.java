package com.lyf.scm.core.api.dto.stockFront;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 类WhAllocationTemplateDTO的实现描述：导入模板DTO
 *
 * @author sunyj 2019/7/19 14:23
 */
@Data
public class WhAllocationTemplateDTO {

    /**
     * 调拨类型
     */
    private String businessTypeStr;

    /**
     * 是否退货
     */
    private String isReturnAllotcate;

    /**
     * 是否质量调拨
     */
    private String isQualityAllotcate;

    /**
     * 调拨日期
     */
    private Date allotTime;

    /**
     * 出库工厂编号
     */
    private String outFactoryCode;

    /**
     * 出库仓库
     */
    private String outRealWareCode;

    /**
     * 调出联系人
     */
    private String outWarehouseName;

    /**
     * 调出联系人电话
     */
    private String outWarehouseMobile;

    /**
     * 入库工厂编号
     */
    private String inFactoryCode;

    /**
     * 入库仓库
     */
    private String inRealWareCode;

    /**
     * 调入联系人
     */
    private String inWarehouseName;

    /**
     * 调入联系人电话
     */
    private String inWarehouseMobile;

    /**
     * 预计到货日期
     */
    private Date expeAogTimeStr;

    /**
     * 调拨商品信息
     */
    private List<WhAllocationTemplateDetailDTO> frontRecordDetails;
    
}