package com.lyf.scm.core.mapper.stockFront;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lyf.scm.core.domain.entity.stockFront.WhAllocationE;

/**
 * 
 * @author <sunyj> 门店调拨单mapper
 * @version 2019-05-12 17:32:37
 * 
 */

public interface WhAllocationMapper {

	/**
	 * 根据ID查询仓库调拨单
	 * 
	 * @param id
	 * @return
	 */
	WhAllocationE queryById(@Param("id") Long id);

	/**
	 * 根据ID集合查询仓库调拨单列表
	 * 
	 * @param ids
	 * @return
	 */
	List<WhAllocationE> queryByIds(@Param("ids") List<Long> ids);

	/**
	 * 根据单据编号查询调拨单
	 * 
	 * @param recordCode
	 * @return
	 */
	WhAllocationE queryByRecordCode(@Param("recordCode") String recordCode);

	/**
	 * 保存仓库调拨单
	 * 
	 * @param whAllocationE
	 * @return
	 */
	Integer saveWhAllocation(WhAllocationE whAllocationE);

	/**
	 * 修改仓库调拨单
	 * 
	 * @param whAllocationE
	 * @return
	 */
	Integer updateWhAllocation(WhAllocationE whAllocationE);

	/**
	 * 根据ID修改为派车成功
	 * 
	 * @param id
	 * @return
	 */
	Integer updateDispatchSuccess(@Param("id") Long id);

	/**
	 * 根据ID修改为审核成功
	 * 
	 * @param whAllocationE
	 * @return
	 */
	Integer updateAuditSuccess(WhAllocationE whAllocationE);

	/**
	 * 根据ID修改为审核失败
	 * 
	 * @param id
	 * @param userId
	 * @return
	 */
	Integer updateAuditFail(@Param("id") Long id, @Param("userId") Long userId);

	/**
	 * 根据ID修改为出库完成
	 * 
	 * @param id
	 * @return
	 */
	Integer updateDeliverySuccess(@Param("id") Long id);

	/**
	 * 根据ID修改为入库完成
	 * 
	 * @param id
	 * @return
	 */
	Integer updateToInWh(@Param("id") Long id);

	/**
	 * 根据ID修改差异状态
	 * 
	 * @param id
	 * @param isDisparity
	 * @return
	 */
	Integer updateDisparityStatus(@Param("id") Long id, @Param("isDisparity") Integer isDisparity);

	/**
	 * 根据条件查询仓库调拨单列表
	 * 
	 * @param whAllocationE
	 * @param recordCodes
	 * @return
	 */
	List<WhAllocationE> queryWhAllocationByCondition(@Param("whAllocationE") WhAllocationE whAllocationE,
			@Param("recordCodes") List<String> recordCodes);

	/**
	 * 查询待下发的仓库调拨单
	 * 
	 * @param page
	 * @param maxResult
	 * @return
	 */
	List<WhAllocationE> queryWaitSyncOrder(@Param("page") int page, @Param("maxResult") int maxResult, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

	/**
	 * 修改为下发成功
	 * 
	 * @param recordId
	 * @param sapCode
	 * @return
	 */
	int updateSyncSucc(@Param("id") Long recordId, @Param("sapCode") String sapCode);

	/**
	 * 根据ID集合查询仓库调拨单列表
	 * 
	 * @param idList
	 * @return
	 */
	List<WhAllocationE> queryWhAllocationByIds(@Param("idList") List<Long> idList);

	/**
	 * 根据ID撤销派车
	 * 
	 * @param id
	 * @return
	 */
	int updateCancelDispatch(@Param("id") Long id);

	List<WhAllocationE> queryFrontRecordByIds(@Param("idList") List<Long> idList);
	
	/**
	 * 根据ID修改为出库完成
	 * 
	 * @param id
	 * @return
	 */
	Integer updateToOutWh(@Param("id") Long id);

	/**
	 * 根据单据编号集合查询未出库的仓库调拨单列表
	 * 
	 * @param recordCodeList
	 * @return
	 */
	List<WhAllocationE> queryNotOutByRecordCodes(List<String> recordCodeList);

}