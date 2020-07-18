package com.lyf.scm.core.api.dto.stockFront;

import com.lyf.scm.common.model.Pagination;
import com.lyf.scm.core.domain.entity.stockFront.FrontRecordE;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 类ShopAssembleSpitPage的实现描述：门店盘点
 *
 * @author sunyj 2019/4/23 20:29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WarehouseRecordPageDTO extends Pagination {

    /**
     * 唯一主键
     */
    private Long id;
    /**
     * 单据编号
     */
    private String recordCode;

    /**
     * sap单号
     */
    private String sapOrderCode;

    /**
     * 单据类型
     */
    private Integer recordType;

    /**
     * 出入库类型
     */
    private Integer businessType;

    /**
     * 单据状态：已入库
     */
    private Integer recordStatus;

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long creator;

    private Long realWarehouseId;

    private String realWarehouseCode;

    /**
     * 实仓仓库名称
     */
    private String realWarehouseName;

    /**
     * 工厂代码
     */
    private String factoryCode;

    /**
     * 仓库外部编号-wms
     */
    private String realWarehouseOutCode;

    /**
     * 出库时间
     */
    private Date deliveryTime;

    /**
     * 入库时间
     */
    private Date receiverTime;

    private List<WarehouseRecordDetailDTO> warehouseRecordDetails;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * tms派车单号
     */
    private String tmsRecordCode;

    /**
     * 入向工厂
     */
    private String inFactory;

    /**
     * 入向仓库编码
     */
    private String inWarehouse;

    /**
     * 出向仓库信息
     */
    private RealWarehouse outWarehouseInfo;

    /**
     * 入向仓库信息
     */
    private RealWarehouse inWarehouseInfo;

    /**
     * 查询用前置单
     */
    private FrontRecordE frontRecord;

}
