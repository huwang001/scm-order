package com.lyf.scm.core.mapper.stockFront;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lyf.scm.core.domain.entity.stockFront.WhAllocationDetailE;

/**
 * 
 * @author <sunyj> 调拨单明细
 * @version 2019-05-12 17:32:49
 * 
 */

public interface WhAllocationDetailMapper {

	/**
	 * 批量保存仓库调拨单明细
	 * 
	 * @param whAllocationDetailEList
	 * @return
	 */
	Integer saveWhAllocationDetail(@Param("details") List<WhAllocationDetailE> whAllocationDetailEList);

	/**
	 * 根据仓库调拨单ID集合查询仓库调拨单明细列表
	 * 
	 * @param frontRecordIds
	 * @return
	 */
	List<WhAllocationDetailE> queryDetailByFrontIds(@Param("frontRecordIds") List<Long> frontRecordIds);

	/**
	 * 根据仓库调拨单ID查询仓库调拨单明细列表（按数量排序）
	 * 
	 * @param frontRecordId
	 * @return
	 */
	List<WhAllocationDetailE> queryDetailByFrontIdsOrderBySkuQty(@Param("frontRecordId") Long frontRecordId);

	/**
	 * 根据仓库调拨单ID删除仓库调拨单明细
	 * 
	 * @param frontRecordId
	 * @return
	 */
	Integer deleteDetailByFrontId(@Param("frontRecordId") Long frontRecordId);

	/**
	 * 批量修改实出数量
	 * 
	 * @param whAllocationDetailEList
	 */
	void updateDetailOutQty(@Param("details") List<WhAllocationDetailE> whAllocationDetailEList);

	/**
	 * 批量修改实入数量
	 * 
	 * @param whAllocationDetailEList
	 */
	void updateDetailInQty(@Param("details") List<WhAllocationDetailE> whAllocationDetailEList);

	/**
	 * 批量修改调拨数量
	 * 
	 * @param whAllocationDetailEList
	 */
	void updateDetailAllotQty(@Param("details") List<WhAllocationDetailE> whAllocationDetailEList);

	/**
	 * 批量修改原始数量、调拨数量
	 * 
	 * @param whAllocationDetailEList
	 */
	void updateDetailOriginAndAllotQty(@Param("details") List<WhAllocationDetailE> whAllocationDetailEList);

	/**
	 * 批量修改实入数量
	 * 
	 * @param whAllocationDetailEList
	 */
	void updateDetailLionNo(@Param("details") List<WhAllocationDetailE> whAllocationDetailEList);

	/**
	 * 根据商品编号查询单据编号列表
	 * 
	 * @param skuCode
	 * @return
	 */
	List<String> queryRecordCodeBySkuCode(@Param("skuCode") String skuCode);

	/**
	 * 根据单据编号查询仓库调拨单明细列表
	 * 
	 * @param recordCode
	 * @return
	 */
	List<WhAllocationDetailE> queryDetailByRecordCode(@Param("recordCode") String recordCode);

	/**
	 * 根据ID集合查询仓库调拨单明细列表
	 * 
	 * @param ids
	 * @return
	 */
	List<WhAllocationDetailE> queryDetailByIds(List<String> ids);

	/**
	 * 根据单据编号集合查询仓库调拨单明细列表
	 * 
	 * @param recordList
	 * @return
	 */
	List<WhAllocationDetailE> queryDetailByRecordCodes(List<String> recordList);

}