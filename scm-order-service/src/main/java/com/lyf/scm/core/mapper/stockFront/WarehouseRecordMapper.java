package com.lyf.scm.core.mapper.stockFront;

import com.lyf.scm.core.api.dto.stockFront.*;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.domain.model.stockFront.WarehouseRecordDO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * WarehouseRecordMapper类的实现描述：出入库单mapper
 *
 * @author sunyj 2019/6/26 17:17
 */
public interface WarehouseRecordMapper {

	/**
	 * 保存出入库单
	 * 
	 * @param warehouseRecordE
	 * @return
	 */
	void insertWarehouseRecord(WarehouseRecordE warehouseRecordE);


	/**
	 * 更新仓库单完成状态
	 * 
	 * @param id
	 */
	void updateCompleteStatus(Long id);

	/**
	 * 根据页面查询条件查询出库单
	 * 
	 * @param condition
	 * @return
	 */
	List<WarehouseRecordE> queryWarehouseRecordList(SaleWarehouseRecordCondition condition);

	/**
	 * 查询退货单
	 * 
	 * @param salesReturnRecordParamDTO
	 * @return
	 */
	List<WarehouseRecordE> querySalesReturnWarehouseRecordList(SalesReturnRecordParamDTO salesReturnRecordParamDTO);

	List<WarehouseRecordE> queryInWarehouseRecordList(@Param("warehouseRecord") WarehouseRecordE warehouseRecordE);

	/**
	 * 查询入库单页面
	 * 
	 * @param warehouseRecordE
	 * @param types
	 * @param ids
	 * @param realWarehouseIds
	 * @return
	 */
	List<WarehouseRecordE> queryInWarehouseRecordPage(@Param("warehouseRecord") WarehouseRecordE warehouseRecordE,
			@Param("types") List<Integer> types, @Param("ids") List<Long> ids,
			@Param("realWarehouseIds") List<Long> realWarehouseIds);

	/**
	 * 查询出库单页面
	 * 
	 * @param warehouseRecordE
	 * @param types
	 * @param ids
	 * @param realWarehouseIds
	 * @return
	 */
	List<WarehouseRecordE> queryOutWarehouseRecordPage(@Param("warehouseRecord") WarehouseRecordE warehouseRecordE,
			@Param("types") List<Integer> types, @Param("ids") List<Long> ids,
			@Param("realWarehouseIds") List<Long> realWarehouseIds);

	/**
	 * 查询出库单
	 * 
	 * @param warehouseRecordE
	 * @return
	 */
	List<WarehouseRecordE> queryOutWarehouseRecordList(WarehouseRecordE warehouseRecordE);

	/**
	 * 更新仓库单完成状态
	 * 
	 * @param id
	 * @param realWarehouseId
	 * @param virtualWarehouseId
	 */
	void updateCompleteStatus(@Param("id") Long id, @Param("realWarehouseId") Long realWarehouseId,
			@Param("realWarehouseCode") String realWarehouseCode, @Param("factoryCode") String factoryCode,
			@Param("virtualWarehouseId") Long virtualWarehouseId,
			@Param("virtualWarehouseCode") String virtualWarehouseCode);


	List<WarehouseRecordE> queryWareHouseRecordList(WareHouseRecordCondition wareHouseRecordCondition);

	/**
	 * 根据id批量查询出入库单数据
	 * 
	 * @param ids
	 * @return
	 */
	List<WarehouseRecordE> queryWarehouseRecordByIds(@Param("ids") List<Long> ids);

	/**
	 * 更新出库单状态为已取消，wms同步状态为无需同步
	 * 
	 * @param id
	 * @return
	 */
	int updateToCanceled(@Param("id") Long id);

	/**
	 * 更新出库单状态为已取消，wms同步状态为无需同步
	 * 
	 * @param id
	 * @return
	 */
	int updateToCanceledFromComplete(@Param("id") Long id);

	/**
	 * 更新叫货的出库单数据为已取消(有初始化-已取消并且没有下发到仓库的单据)
	 * 
	 * @param id
	 * @param isForceCancle
	 * @param userId
	 * @return
	 */
	int updateReplenishToCanceled(@Param("id") Long id, @Param("isForceCancle") Integer isForceCancle,
			@Param("userId") Long userId);

	/**
	 * 修改后置单同步交易状态
	 * 
	 * @param id
	 * @return
	 */
	int updateSyncTradeStatus(@Param("id") Long id);

	/**
	 * 查询未推送cmp出入库单
	 * 
	 * @param type
	 * @return
	 */
	List<WarehouseRecordE> queryAllocationWarehouseCmpList(@Param("type") Integer type,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	/**
	 * 更新cmp完成状态
	 * 
	 * @param id
	 */
	void updateCmpStatusComplete(Long id);

	/**
	 * 查询待推送到交易中心订单
	 * 
	 * @param page
	 * @param maxResult
	 * @return
	 */
	List<WarehouseRecordE> queryBySyncTradeStatus(@Param("page") int page, @Param("maxResult") int maxResult);

	/**
	 * 查询待推送到交易中心订单,不限定时间
	 * 
	 * @param page
	 * @param maxResult
	 * @return
	 */
	List<WarehouseRecordE> queryAllBySyncTradeStatus(@Param("page") int page, @Param("maxResult") int maxResult);

	/**
	 * 根据查询条件查找列表
	 *
	 * @param condition
	 * @param types
	 * @return
	 */
	List<SaleTobWarehouseRecordDTO> queryWarehouseRecordListByCondition(
			@Param("condition") SaleTobWarehouseRecordCondition condition, @Param("types") Set<Integer> types);

	/**
	 * 根据单据编号查询出入库单据列表
	 * 
	 * @param recordCodes
	 * @return
	 */
	List<WarehouseRecordE> queryWarehouseRecordByRecordCode(@Param("recordCodes") List<String> recordCodes);

	/**
	 * 查询未处理门店批次出入库单
	 * 
	 * @param startDate
	 * @param endDate
	 * @param rows
	 * @return
	 */
	List<Long> queryWarehouseBatchList(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("rows") Integer rows);

	List<WarehouseRecordE> queryWarehouseRecordByTmsRecordCode(@Param("tmsRecordCode") String tmsRecordCode);

	/**
	 * 更新未同步派车单号
	 * 
	 * @param unSyncCodes
	 * @param tmsCode
	 * @param userId
	 * @return
	 */
	Integer updateUnSyncWmsRecord(@Param("unSyncCodes") List<String> unSyncCodes, @Param("tmsCode") String tmsCode,
			@Param("userId") Long userId);

	/**
	 * 更新同步成功派车单号
	 * 
	 * @param syncCodes
	 * @param tmsCode
	 * @param userId
	 * @return
	 */
	Integer updateSyncWmsRecord(@Param("syncCodes") List<String> syncCodes, @Param("tmsCode") String tmsCode,
			@Param("userId") Long userId);

	/**
	 * 根据单据编号查询出入库单
	 * 
	 * @param recordCode
	 * @return
	 */
	WarehouseRecordE queryByRecordCode(@Param("recordCode") String recordCode);

	/**
	 * 根据出入库单据编号修改TMS派车单号、 派车状态（已派车）
	 * 
	 * @param recordCode
	 * @param tmsRecordCode
	 * @return
	 */
	int updateTmsRecordCodeAndDispatchStatus(@Param("recordCode") String recordCode,
			@Param("tmsRecordCode") String tmsRecordCode,@Param("isDispatch") Integer isDispatch);

	/**
	 * 根据出入库单据编号修改TMS派车单号
	 *
	 * @param recordCode
	 * @param tmsRecordCode
	 * @return
	 */
	int updateNotifyTmsRecordCode(@Param("recordCode") String recordCode, @Param("tmsRecordCode") String tmsRecordCode);
	/**
	 * 根据单据编号取消出库单
	 * 
	 * @param recordCode
	 * @param userId
	 * @return
	 */
	int updateCancelOutRecord(@Param("recordCode") String recordCode, @Param("userId") Long userId);

	/**
	 * 修改同步派车系统状态、同步库存状态
	 * 
	 * @param recordCode
	 * @param syncTmsbStatus
	 * @param syncStockStatus
	 * @return
	 */
	int updateSyncTmsbAndStockStatus(@Param("recordCode") String recordCode,
			@Param("syncTmsbStatus") Integer syncTmsbStatus,
			@Param("syncStockStatus") Integer syncStockStatus);

	/**
	 * 查询待推送给库存中心的poNo列表
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<String> queryPoNoToStock(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

	/**
	 * 根据出入库单据编号修改派车状态
	 * 
	 * @param recordCode
	 * @return
	 */
	int updateDispatchStatus(@Param("recordCode") String recordCode, @Param("status") Integer status);

	/**
	 * 根据出入库单据编号修改TMS派车单号
	 * 
	 * @param recordCode
	 * @param tmsRecordCode
	 * @return
	 */
	int updateTmsRecordCode(@Param("recordCode") String recordCode, @Param("tmsRecordCode") String tmsRecordCode);

	/**
	 * @Description: 更新出库单状态已出库和出入库时间 <br>
	 *
	 * @Author chuwenchao 2020/6/19
	 * @param recordCode
	 * @return 
	 */
	int updateRecordInfoToOut(@Param("recordCode") String recordCode, @Param("operateTime") String operateTime);

	/**
	 * @Description: 通过ID查询出入库单 <br>
	 *
	 * @Author chuwenchao 2020/6/19
	 * @param id
	 * @return
	 */
    WarehouseRecordE getWarehouseRecordById(Long id);

	/**
	 * @Description: 更新入库单状态已入库和出入库时间 <br>
	 *
	 * @Author chuwenchao 2020/6/19
	 * @param recordCode
	 * @return
	 */
	int updateRecordInfoToIn(@Param("recordCode") String recordCode, @Param("operateTime") String operateTime);

    /**
     * 查询 待同步到派车系统的 出库单
     *
     * @param startPage
     * @param endPage
     * @return
     */
    List<Long> queryNeedSynTmsBWarehouseRecordByPage(@Param("startPage") Integer startPage, @Param("endPage") Integer endPage);

	/**
	 * 修改同步派车系统 状态为 已同步
	 * @param id
	 * @return
	 */
	int updateWarehouseRecordSynTmsBStatusComplete(@Param("id") Long id);

	/**
	 * 根据出库单编码获取所有的出库单类型
	 *
	 * @param recordCodes
	 * @return
	 */
	List<WarehouseRecordE> getRecordTypeByRecordCodes(@Param("recordCodes") List<String> recordCodes);

	/**
	 * @Description: 通过后置单ID更新sapPoNo + 派车状态 <br>
	 *
	 * @Author chuwenchao 2020/6/23
	 * @param id
	 * @param sapPoNo
	 * @return 
	 */
	int updateRecordInfoToSyncTmsb(@Param("id") Long id, @Param("sapPoNo") String sapPoNo);

	/**
	 * 修改sapPoNo
	 * 
	 * @param recordCode
	 * @param sapPoNo
	 * @return
	 */
	int updateSapPoNo(@Param("recordCode")String recordCode, @Param("sapPoNo") String sapPoNo);

	/**
	 * 派车下发失败时 修改 失败时间
	 */
	void updateSyncTmsBFailTime(Long id);


	/**
	 * 保存出入库单(包含派车状态)
	 * 保存字段：record_code,business_type,record_status,record_type,real_warehouse_id,channel_type,channel_code,merchant_id,out_create_time,
	 * sync_dispatch_status
	 *
	 * @param warehouseRecordDO
	 * @return
	 */
	void insertWrWithDispatchInfo(WarehouseRecordDO warehouseRecordDO);

	/**
	 * 批量修改出库单状态为取消订单和同步派车系统状态为无需同步
	 * @param warehouseRecordES
	 */
	void updateRecordAndSyncTmsbStatus(@Param("whRecordS") List<WarehouseRecordE> warehouseRecordES);

	/**
	 * 根据单据编号和单据类型查询出入库单
	 * * @Author huangyl 2020/07/17
	 * @param recordCode
	 * @param businessType
	 * @return
	 */
	WarehouseRecordE queryByRecordCodeAndBusinessType(@Param("recordCode") String recordCode, @Param("businessType") Integer businessType);
}