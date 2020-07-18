package com.lyf.scm.core.mapper.stockFront;


import com.lyf.scm.core.domain.entity.stockFront.ShopInventoryE;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author <lyf>
 * @version 2019-04-22 15:52:43
 */

public interface ShopInventoryMapper {
    /**
     * 保存盘点单
     * 保存字段：record_code,merchant_id,shop_code,shop_name,business_type,record_type,record_status,remark,
     * record_status_reason,real_warehouse_id,out_record_code,out_create_time
     *
     * @param recordDo
     */
    void insertShopInventoryRecord(ShopInventoryE recordDo);

    /**
     * 更新盘点单完成状态
     */
    void updateCompleteStatus(@Param("id") Long id, @Param("initRecordStatus") Integer initRecordStatus, @Param("recordStatus") Integer recordStatus);

    /**
     * 查询盘点单列表
     * 查询字段：record_code,out_record_code,shop_code,shop_name,business_type,remark,out_create_time,record_status,create_time,creator
     * 查询条件：is_available,recordCode,shopCode,recordStatus,startDate,endDate
     *
     * @return
     */
    List<ShopInventoryE> selectShopInventoryList(ShopInventoryE recordDo);

    /**
     * 根据id集合查询前置单据
     *
     * @param idList
     * @return
     */
    List<ShopInventoryE> queryFrontRecordByIds(@Param("idList") List<Long> idList);

    /**
     * 查询外部单号是否存在
     *
     * @param outRecordCodes
     * @return
     */
    List<String> judgeExistByOutRecordCodes(@Param("outRecordCodes") List<String> outRecordCodes);

    /**
     * 根据id查询盘点单
     *
     * @param id
     * @return
     */
    ShopInventoryE queryFrontRecordById(@Param("id") Long id, @Param("recordStatus") Integer recordStatus);

    /**
     * 分页查询未处理盘点单
     *
     * @param date
     * @param startPage
     * @param endPage
     * @return
     */
    List<Long> queryInitShopInventoryRecordPage(@Param("date") Date date, @Param("startPage") Integer startPage,
                                                @Param("endPage") Integer endPage, @Param("recordStatus") Integer recordStatus);
}
