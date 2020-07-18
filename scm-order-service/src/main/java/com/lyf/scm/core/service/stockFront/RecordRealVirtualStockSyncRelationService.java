package com.lyf.scm.core.service.stockFront;

import java.util.List;
import java.util.Map;

import com.lyf.scm.core.api.dto.stockFront.RecordRealVirtualStockSyncRelationDTO;

public interface RecordRealVirtualStockSyncRelationService {

	/**
	 * 根据单据code查询单据的sku级别的实仓虚仓的配比关系，key为skuID+"_"+虚仓id，value为Do对象，这种只适合一个流程只涉及一个实仓的，比如外采单
	 * 
	 * @param recordCode
	 * @return
	 */
	Map<String, RecordRealVirtualStockSyncRelationDTO> queryRelationMapByRecordCode(String recordCode);

	/**
	 * 查询指定实仓的比例,比如 仓库调拨 可以查询入库或出库比例
	 * 
	 * @param recordCode
	 * @param rwId
	 * @return
	 */
	Map<String, RecordRealVirtualStockSyncRelationDTO> queryRelationMapByRecordCode(String recordCode, Long rwId);

	List<RecordRealVirtualStockSyncRelationDTO> queryRelationListByRecordCode(String recordCode);

	List<RecordRealVirtualStockSyncRelationDTO> queryRelationListByRecordCode(String recordCode, Long rwId);

	void insertRecordRealVirtualStockRelation(List<RecordRealVirtualStockSyncRelationDTO> configList);

}
