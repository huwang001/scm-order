package com.lyf.scm.core.api.dto.orderReturn;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @Description: 推送退货单给库存中心传输对象
 * <p>
 * @Author: wwh 2020/4/20
 */
@Data
public class PushReturnDTO implements Serializable {
	
    /**
     * 工厂编码
     */
    private String factoryCode;

    /**
     * 仓库外部编码
     */
    private String realWarehouseOutCode;
    
    /**
     * 预约单号
     */
    private String reservationNo;
    
    /**
     * 销售单号
     */
    private String saleCode;

    /**
     * 售后单号
     */
    private String outRecordCode;


    /**
     * 客户名称
     */
    private String customName;

    /**
     * 客户手机号
     */
    private String customMobile;

    /**
     * 退货原因
     */
    private String reason;

    /**
     * 快递单号
     */
    private String expressNo;


    /**
     * 省
     */
    private String province;


    /**
     * 省编码
     */
    private String provinceCode;

    /**
     * 市
     */
    private String city;

    /**
     * 市编码
     */
    private String cityCode;

    /**
     * 区县
     */
    private String county;

    /**
     * 区县编码
     */
    private String countyCode;

    /**
     * 详细地址
     */
    private String address;

    /**
     *收货人姓名
     */
    private String name;
    
    /**
     * 明细集合
     */
    private List<PushReturnDetailDTO> returnDetails;
    
}