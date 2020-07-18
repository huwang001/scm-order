package com.lyf.scm.core.mapper.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.FrontWarehouseRecordRelationE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FrontWarehouseRecordRelationMapper {

	/**
	 * 保存前置单与仓库单关系
	 * 保存字段：front_record_id,warehouse_record_id,front_record_type,front_record_code,record_code
	 * 
	 * @param relation
	 */
	void insertFrontWarehouseRecordRelation(FrontWarehouseRecordRelationE relation);

	/**
	 * 查询出入库id
	 * 
	 * @param frontRecordId
	 * @param recordType
	 * @return
	 */
	List<Long> queryWarehouseIdByFrontId(@Param("frontRecordId") Long frontRecordId,
			@Param("recordType") Integer recordType);

	/**
	 * 根据出入库单id集合查询前置单号id
	 * 
	 * @param idList
	 * @return
	 */
	List<FrontWarehouseRecordRelationE> queryFrontRecordListByIdList(@Param("idList") List<Long> idList);

	/**
	 * 根据id和查询关联关系
	 * 
	 * @param warehouseRecordId
	 * @return
	 */
	List<FrontWarehouseRecordRelationE> getByWrId(@Param("warehouseRecordId") Long warehouseRecordId);

	/**
	 * 根据前置单号查询出入库单号
	 * 
	 * @param frontRecordCode
	 * @return
	 */
	List<String> getRecordCodeByFrontRecordCode(String frontRecordCode);

	/**
	 * 根据出入库单号查询前置单号
	 * 
	 * @param recordCode
	 * @return
	 */
	String getFrontRecordCodeByRecordCode(String recordCode);

	/**
	 * 根据前置单查出入库单id
	 * 
	 * @param frontRecordCode
	 * @return
	 */
	List<Long> queryWarehouseRecordIdByRecord(String frontRecordCode);

	List<String> getRecordCodesByFrontRecordCodes(@Param("frontRecordCodes") List<String> frontRecordCodes);

	List<FrontWarehouseRecordRelationE> getFrontRelationByRecordCode(@Param("recordCode") String recordCode);

	List<FrontWarehouseRecordRelationE> getFrontRelationByRecordCodes(@Param("recordCodes") List<String> recordCodes);

	List<FrontWarehouseRecordRelationE> getRecordRelationByFrontRecordCodes(@Param("frontRecordCodes") List<String> frontRecordCodes);

	/**
	 * 根据后置单id删除关联关系
	 * 
	 * @param warehouseRecordId
	 * @return
	 */
	int deleteRelationByWrId(@Param("warehouseRecordId") Long warehouseRecordId);

	/**
	 * @Description: 通过前置单ID+前置单类型查询关联关系 <br>
	 *
	 * @Author chuwenchao 2020/6/23
	 * @param frontId
	 * @return 
	 */
	List<FrontWarehouseRecordRelationE> queryRecordRelationByFrontIdAndType(@Param("frontId") Long frontId, @Param("recordType") Integer recordType);

	/**
	 * 根据出入库单号查询前置单ID
	 *
	 * @param recordCode
	 * @return
	 */
	Long getFrontRecordIdByRecordCode(String recordCode);

	/**
	 * 通过Id更新出入库单关联单号
	 * 
	 * @author zhanglong
	 * @date 2020/7/15 15:48
	 */
	int updateDependRecordCodeById(@Param("dependRecordCode") String dependRecordCode, @Param("id") Long id);

	/**
	 * 根据出入库单号查询前置单号
	 * @param recordCode
	 * @return
	 */
	List<String> getFrontRecordCodeListByRecordCode(@Param("recordCode") String recordCode);
}