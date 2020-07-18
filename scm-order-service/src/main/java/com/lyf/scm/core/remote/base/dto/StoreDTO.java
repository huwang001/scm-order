package com.lyf.scm.core.remote.base.dto;

import com.lyf.scm.common.model.Pagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 门店信息
 * @author sunyongjun
 * @date 2019/5/23
 */
@ApiModel(description="门店信息")
@Data
@EqualsAndHashCode
public class StoreDTO extends Pagination {

    @ApiModelProperty("门店code")
    private String code;

    @ApiModelProperty("门店名称")
    private String name;

    @ApiModelProperty("门店编号或名称")
    private String codeOrName;

    @ApiModelProperty("门店类型")
    private String type;

    @ApiModelProperty("区域")
    private String area;

    @ApiModelProperty("省份")
    private String province;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("区县")
    private String county;

    @ApiModelProperty("商圈名称")
    private String tradeAreaName;

    @ApiModelProperty("描述")
    private String desc;

    @ApiModelProperty("销售面积")
    private String saleArea;

    @ApiModelProperty("营业面积")
    private String businessArea;

    @ApiModelProperty("当地税率")
    private String localTaxRate;

    @ApiModelProperty("格斗数量")
    private Integer cabinetQuantity;

    @ApiModelProperty("电话号码: 拨区号 + 号码")
    private String tel;

    @ApiModelProperty("区域负责人")
    private String areaOwner;

    @ApiModelProperty("大区负责人")
    private String regionOwner;

    @ApiModelProperty("公司代码")
    private String companyCode;

    @ApiModelProperty("实际开店日期")
    private Date actualOpenDate;

    @ApiModelProperty("住宅号及街道")
    private String address;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("营业时间")
    private String saleTime;

    @ApiModelProperty("柜台布置类型")
    private String counterLayoutType;

    @ApiModelProperty("税务登记证号")
    private String taxRegistNum;

    @ApiModelProperty("税务登记证开始日期")
    private Date taxRegistStartDate;

    @ApiModelProperty("组织机构代码")
    private String orgCode;

    @ApiModelProperty("组织机构代码开始日期")
    private Date orgCodeStartDate;

    @ApiModelProperty("组织机构代码结束日期")
    private Date orgCodeEndDate;

    @ApiModelProperty("统计证号")
    private String statisticsCertificateNum;

    @ApiModelProperty("统计证开始日期")
    private Date scNumStartDate;

    @ApiModelProperty("统计证结束日期")
    private Date scNumEndDate;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;

    @ApiModelProperty("门店代购")
    private String storeAgent;

    @ApiModelProperty("门店自提")
    private String storePickup;

    @ApiModelProperty("闭店日期")
    private Date closeDate;

    @ApiModelProperty("停车服务")
    private String parkingService;

    @ApiModelProperty("wifi")
    private String wifi;

    @ApiModelProperty("监控")
    private String monitor;

    @ApiModelProperty("要货周期")
    private Integer requireGoodsCycle;

    @ApiModelProperty("门店登记")
    private String storeLevel;

    @ApiModelProperty("风幕柜")
    private String airCurtainCabinet;

    @ApiModelProperty("商圈性质")
    private String tradeAreaQuality;

    @ApiModelProperty("交货工厂")
    private String deliveryFactory;

    @ApiModelProperty("翻新实际竣工时间")
    private Date renovationFinishDate;

    @ApiModelProperty("人员号")
    private Integer personCode;

    @ApiModelProperty("门店装修风格")
    private String renovationStyle;

    @ApiModelProperty("带教中心门店")
    private String teachingCenterStore;

    @ApiModelProperty("HR组织代码")
    private String hrOrgCode;

    @ApiModelProperty("门店性质：1 直营,2 非分支,3 加盟,4 联营5，加盟托管")
    private String storeProperties;

    @ApiModelProperty(value = "冗余")
    private Date expectOpenDate;

    @ApiModelProperty(value = "门店类型")
    private String storeType;

    @ApiModelProperty(value = "交货模式")
    private String requireGoodsPattern;

    @ApiModelProperty(value = "所属加盟商")
    private String franchisee;
}
