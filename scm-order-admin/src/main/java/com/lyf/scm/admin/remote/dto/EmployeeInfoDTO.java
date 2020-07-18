package com.lyf.scm.admin.remote.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Description 员工信息dto
 * @Author zhangtuo
 * @Date 2019/9/5 10:52
 * @Version 1.0
 */
@Data
@EqualsAndHashCode
public class EmployeeInfoDTO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "账号ID")
    private Long accountId;

    @ApiModelProperty(value = "昵称-员工花名，企业简称等")
    private String username;

    @ApiModelProperty(value = "头像-员工头像，企业log等")
    private String headPortrait;

    @ApiModelProperty(value = "备注：员工签名，企业简述等")
    private String remarks;

    @ApiModelProperty(value = "所属父级")
    private Long parentId;

    @ApiModelProperty(value = "应用用户类型：1：员工   2：供应商")
    private Long userType;

    @ApiModelProperty(value = "等级 L0 超管 L1 一级  L2二级 L3 三级 ")
    private String grade;

    @ApiModelProperty(value = "状态 0正常 1冻结 2注销")
    private Long userStatus;

    @ApiModelProperty(value = "商户ID")
    private Long merchantId;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long modifier;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "应用ID")
    private String appId;

    @ApiModelProperty(value = "业务账号ID")
    private Long userAccountId;

    @ApiModelProperty(value = "账号名称")
    private String accountName;

    @ApiModelProperty(value = "账号密码")
    private String userPwd;

    @ApiModelProperty(value = "密码失效时间")
    private Date pwdExpTime;

    @ApiModelProperty(value = "认证模式，01本地、02第三方")
    private String authmode;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "账号有效时间")
    private Date validtime;

    @ApiModelProperty(value = "加密算法:ASC")
    private String pwdAlg;

    @ApiModelProperty(value = "渠道ID,00 直销，01，POS")
    private String channelId;

    @ApiModelProperty(value = "账号类型集：0：C端账号    1：应用账号(员工、供应商)")
    private Long type;

    @ApiModelProperty(value = "账号状态")
    private Long acountStatus;

    @ApiModelProperty(value = "扩展字段JSON对象")
    private String extention;

    @ApiModelProperty(value = "英文名称")
    private String englishName;

    @ApiModelProperty(value = "姓")
    private String sn;

    @ApiModelProperty(value = "名")
    private String givenName;

    @ApiModelProperty(value = "姓名")
    private String cn;

    @ApiModelProperty(value = "中文拼音")
    private String chinesePinyin;

    @ApiModelProperty(value = "籍贯")
    private String homePlace;

    @ApiModelProperty(value = "出生日期")
    private String birthDay;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "国籍")
    private String nationality;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "护照号")
    private String passport;

    @ApiModelProperty(value = "员工号")
    private String employeeNumber;

    @ApiModelProperty(value = "雇用日期")
    private String hireDay;

    @ApiModelProperty(value = "解雇日期")
    private String termiDay;

    @ApiModelProperty(value = "部门全称")
    private String departmentName;

    @ApiModelProperty(value = "部门代码")
    private String departmentNumber;

    @ApiModelProperty(value = "部门简称")
    private String orgShort;

    @ApiModelProperty(value = "班次ID")
    private String workTime;

    @ApiModelProperty(value = "公司邮箱")
    private String lyfMail;

    @ApiModelProperty(value = "邮编")
    private String postalCode;

    @ApiModelProperty(value = "员工类型")
    private String employeeType;

    @ApiModelProperty(value = "上级领导工号")
    private String managerNumber;

    @ApiModelProperty(value = "人事子范围")
    private String workArea;

    @ApiModelProperty(value = "成本中心")
    private String costCenter;

    @ApiModelProperty(value = "电话号码")
    private String telePhoneNumber;

    @ApiModelProperty(value = "电话短号")
    private String webshort;

    @ApiModelProperty(value = "个人邮箱")
    private String mail;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "公司代码")
    private String companyNumber;

    @ApiModelProperty(value = "岗位名称")
    private String station;

    @ApiModelProperty(value = "职级")
    private String emLevel;

    @ApiModelProperty(value = "岗位代码")
    private String stationCode;

    @ApiModelProperty(value = "人员日期")
    private String aedtm;

    @ApiModelProperty(value = "人员时间")
    private String bTime;

    @ApiModelProperty(value = "传真号")
    private String telexNumber;

    @ApiModelProperty(value = "层级")
    private String jobLevel;

    @ApiModelProperty(value = "网中网")
    private String innet;

    @ApiModelProperty(value = "变更类型 I-新增   U-修改   D-删除")
    private String changeType;

    @ApiModelProperty(value = "人员状态  在职，离职")
    private String erPersonStatus;

    @ApiModelProperty(value = "")
    private String ilaiyfstate;

}
