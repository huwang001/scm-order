package com.lyf.scm.core.domain.model.pack;

import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PackDemandDO extends BaseDO implements Serializable {

    /**
     * 主键唯一id
     */
    private Long id;

    /**
     * 需求编码
     */
    private String recordCode;

    /**
     * 销售单号
     */
    private String saleCode;

    /**
     * 包装类型 1:组装2:反拆3:自定义组合4:自定义反拆5:拆箱
     */
    private Integer packType;

    /**
     * 渠道编码
     */
    private String channelCode;

    /**
     * 创建类型1：接口创建2：页面创建3：导入创建
     */
    private Integer createType;

    /**
     * 需求日期
     */
    private Date demandDate;

    /**
     * 需求部门
     */
    private String department;

    /**
     * 是否外采 0:否 1:是
     */
    private Integer isOut;

    /**
     * 外部单号
     */
    private String outCode;

    /**
     * 包装仓ID
     */
    private Long inRealWarehouseId;

    /**
     * 入向工厂编码
     */
    private String inFactoryCode;

    /**
     * 入向实仓编码
     */
    private String inRealWarehouseCode;

    /**
     * 入虚仓编码
     */
    private String inVirtualWarehouseCode;

    /**
     * 领料仓ID
     */
    private Long outRealWarehouseId;

    /**
     * 出向工厂编码
     */
    private String outFactoryCode;

    /**
     * 出向实仓编码
     */
    private String outRealWarehouseCode;

    /**
     * 出虚仓编码
     */
    private String outVirtualWarehouseCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 单据状态 0:初始1:已确认2:已取消3:部分包装4:已包装完成
     */
    private Integer recordStatus;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 需求提出人
     */
    private String introducer;

    /**
     * 领料状态 0:未领料1:领料中2:已部分领料3:已全部领料
     */
    private Integer pickStatus;
}