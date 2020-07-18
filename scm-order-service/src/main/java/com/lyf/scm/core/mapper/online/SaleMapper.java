package com.lyf.scm.core.mapper.online;

import com.lyf.scm.core.domain.entity.online.SaleE;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * APP-电商前置单据
 */
public interface SaleMapper {
    /**
     * 保存
     * 保存字段：record_code,channel_id,channel_type,merchant_id,record_type,record_status,record_status_reason,
     * real_warehouse_id,mobile,out_record_code,out_create_time,user_code,shop_code
     */
    void saveFrSaleRecord(SaleE entity);

    /**
     * 根据ID查找
     *
     * @param id
     * @return
     * @throws Exception
     */
    SaleE selectFrSaleRecordById(Long id);

    /**
     * 根据outRecordCode查找
     *
     * @param outRecordCode
     * @return
     * @throws Exception
     */
    SaleE selectFrSaleRecordByOutRecordCode(String outRecordCode);


    /**
     * 查询出库单编码跟前置单的关系map
     *
     * @param param
     * @return
     */
    List<Map> queryFrSaleFrontAndWarehouseRelation(Map param);

    /**
     * 更新出库销售单完成状态
     *
     * @param id
     */
    void updateCompleteStatus(@Param("id") long id);

    /**
     * 根据前置SO单id状态为已支付
     *
     * @param id 前置SO单主键
     * @return
     */
    int updateToPaid(@Param("id") Long id, @Param("payTime") Date payTime);

    /**
     * 根据前置SO单id状态为已取消
     *
     * @param id 前置SO单主键
     * @return
     */
    int updateToCanceled(@Param("id") Long id);

    /**
     * 根据前置SO单id状态为已出库
     *
     * @param id 前置SO单主键
     * @return
     */
    int updateToOutAllocation(@Param("id") Long id);

    /**
     * 根据id集合查询零售单据
     *
     * @param idList
     * @return
     */
    List<SaleE> queryFrontRecordByIds(@Param("idList") List<Long> idList);

    /**
     * 查询根据单据类型和小于指定的时间,主要用在冷热数据迁移
     *
     * @return
     */
    List<SaleE> selectFrSaleRecordByTypeEndTime(@Param("recordType") Integer recordType, @Param("endTime") Date endTime, @Param("pageSize") Integer pageSize);
}
