package com.lyf.scm.core.api.dto.stockFront;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode
public class SaleWarehouseRecordDTO {

    private Long id;
    /**
     * 所属单据编码
     */
    private String recordCode;
    /**
     * 业务类型：
     */
    private Integer businessType;
    /**
     * do单状态，0 未同步  1 已同步 10 拣货 11 打包 12 装车 13 发运 21 接单 22 配送 23 完成
     */
    private Integer recordStatus;

    private String recordStatusName;
    /**
     * 单据类型：1-销售出库订单，2-采购单
     */
    private Integer recordType;

    /**
     * 单据类型对应的单据名称
     */
    private String recordTypeName;
    /**
     * 用户code
     */
    private String userCode;
    /**
     * 虚拟仓库ID
     */
    private Long virtualWarehouseId;
    /**
     * 实仓ID
     */
    private Long realWarehouseId;

    /**
     * 实仓名称
     */
    private String realWarehouseName;

    /**
     * 实仓【门店】地址
     */
    private String realWarehouseAddress;
    /**
     * 渠道类型
     */
    private Long channelType;

    /**
     * 渠道类型
     */
    private String channelTypeName;
    /**
     * 渠道id
     */
    private String channelCode;

    /**
     * 渠道id名称
     */
    private String channelCodeName;
    /**
     * 商家id
     */
    private Long merchantId;
    /**
     * 订单备注(用户)
     */
    private String orderRemarkUser;
    /**
     * 外部系统数据创建时间:下单时间
     */
    private Date outCreateTime;
    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 退货流程用：退货原因
     */
    private String reason;
    /**
     * 期望收货日期_开始
     */
    private Date expectReceiveDateStart;
    /**
     * 期望收货日期_截止
     */
    private Date expectReceiveDateEnd;
    /**
     * 撤回原因
     */
    private String reasons;
    /**
     * 异常原因
     */
    private String errorMessage;
    /**
     * 撤回时间
     */
    private Date relinquishTime;
    /**
     * 发货时间
     */
    private Date deliveryTime;
    /**
     * 收货时间
     */
    private Date receiverTime;


    /**
     * 订单编码
     */
    private List<String> outRecordCode;

    private Date createTime;

    /**
     * 商品详情
     */
    private List<SaleWarehouseRecordDetailDTO> details;

    /**
     * 同步捋单系统状态 0-无需同步 1-待同步交货信息 2-已同步
     */
    private Integer syncFulfillmentStatus;

    /**
     * 出入库单同步WMS状态：0-无需同步 1-未同步 2-已同步
     */
    private Integer syncWmsStatus;

    /**
     * 实仓编号
     */
    private String realWarehouseCode;

    /**
     * 实仓类型
     */
    private Integer realWarehouseType;
    /**
     * 创建人
     */
    private Long creator;

    private String factoryCode;
}
