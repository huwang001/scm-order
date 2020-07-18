package com.lyf.scm.core.domain.entity.stockFront;

import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class RealWarehouseE extends BaseDO implements Serializable {
    /**
     * 唯一主键
     */
    private Long id;
    /**
     * 商家id
     */
    private Long merchantId;

    //实仓实仓主键
    private Long realWarehouseId;
    //实仓仓库编码-来源可能是sap或其他
    private String realWarehouseCode;
    //实仓仓库外部编号-wms
    private String realWarehouseOutCode;
    //工厂编码
    private String factoryCode;
    //工厂名称他
    private String factoryName;
    //实仓仓库名称
    private String realWarehouseName;
    //实仓仓库类型：1-电商仓，2-门店仓，3-采购仓，4-退货仓，5-虚拟产品仓，6-生鲜仓，7-行政仓
    private Integer realWarehouseType;
    //实仓如果为门店仓，店铺编码不能为空
    private String shopCode;
    //实仓仓库状态：0-初始，1-启用，2-停用
    private Integer realWarehouseStatus;
    //实仓虚拟仓库库存操作优先级 1为最高
    private Integer realWarehousePriority;
    //实仓邮编
    private String realWarehousePostcode;
    //实仓联系手机
    private String realWarehouseMobile;
    //实仓联系电话
    private String realWarehousePhone;
    //实仓联系邮箱
    private String realWarehouseEmail;
    //实仓国家
    private String realWarehouseCountry;
    //实仓省份
    private String realWarehouseProvince;
    //实仓城市
    private String realWarehouseCity;
    //实仓区、县城市  暂时只维护到省市级别
    private String realWarehouseCounty;
    //实仓四级区域
    private String realWarehouseArea;
    //实仓国家code
    private String realWarehouseCountryCode;
    //实仓省份code
    private String realWarehouseProvinceCode;
    //实仓城市code
    private String realWarehouseCityCode;
    //实仓区、县城市code 暂时只维护到省市级别
    private String realWarehouseCountyCode;
    //实仓四级区域code
    private String realWarehouseAreaCode;
    //实仓详细地址
    private String realWarehouseAddress;
    //实仓联系人姓名
    private String realWarehouseContactName;
    //实仓备注信息
    private String realWarehouseRemark;

}
