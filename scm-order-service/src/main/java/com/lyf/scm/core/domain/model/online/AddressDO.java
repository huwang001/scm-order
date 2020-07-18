package com.lyf.scm.core.domain.model.online;


import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 类BaseDo的实现描述：地址DO
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class AddressDO extends BaseDO implements Serializable {

    /**
     * 主键
     */
    private Long id;
    /**
     * 所属单据编码
     */
    private String recordCode;
    /**
     * 用户类型：0-用户，1-门店 ，2-仓库
     */
    private Byte userType;
    /**
     * 地址类型：0 收货地址，1 发货地址
     */
    private Byte addressType;
    /**
     * 地址邮编
     */
    private String postcode;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 证件类型(1-身份证、2-军官证、3-护照、4-其他)
     */
    private Byte idType;
    /**
     * 证件号码
     */
    private String idNumber;
    /**
     * 国家
     */
    private String country;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 区/县城市
     */
    private String county;
    /**
     * 四级区域
     */
    private String area;
    /**
     * 国家code
     */
    private String countryCode;
    /**
     * 省份code
     */
    private String provinceCode;
    /**
     * 城市code
     */
    private String cityCode;
    /**
     * 区/县城市code
     */
    private String countyCode;
    /**
     * 四级区域code
     */
    private String areaCode;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 姓名
     */
    private String name;
    /**
     * 备注
     */
    private String remark;
}



	
	

