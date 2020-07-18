
package com.lyf.scm.core.mapper.stockFront;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lyf.scm.core.domain.entity.stockFront.RecordRealVirtualStockSyncRelationE;

public interface RecordRealVirtualStockSyncRelationMapper {

	/**
	 * 根据单据编码查询单据级别的sku实仓虚仓同步比例
	 *
	 * @param recordCode
	 * @return
	 */
	List<RecordRealVirtualStockSyncRelationE> queryByRecordCode(@Param("recordCode") String recordCode);

	/**
	 * 批量保存单据级别的sku实仓虚仓分配关系
	 *
	 * @param relationList
	 */
	void insertRecordRealVirtualStockRelation(
			@Param("relationList") List<RecordRealVirtualStockSyncRelationE> relationList);

	/**
	 * 批量修改单据级别的sku实仓虚仓分配关系
	 * 
	 * @param relationList
	 */
	void updateRecordRealVirtualStockRelation(
			@Param("relationList") List<RecordRealVirtualStockSyncRelationE> relationList);

	void deleteRelationByRecordCodeAndRid(@Param("recordCode") String recordCode,
			@Param("realWarehouseId") Long realWarehouseId);

}