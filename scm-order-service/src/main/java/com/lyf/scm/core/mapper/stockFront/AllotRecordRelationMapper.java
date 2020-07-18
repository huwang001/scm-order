package com.lyf.scm.core.mapper.stockFront;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lyf.scm.core.domain.entity.stockFront.AllotRecordRelationE;

public interface AllotRecordRelationMapper {
	
	/**
	 * 保存调拨业务单据关系
	 * 
	 * @param allotRecordRelationE
	 * @return
	 */
    int insertAllotRecordRelation(AllotRecordRelationE allotRecordRelationE);

    /**
     * 批量保存调拨业务单据关系
     * 
     * @param allotRecordRelationE
     * @return
     */
    int batchInsertAllotRecordRelation(List<AllotRecordRelationE> allotRecordRelationEList);

    /**
     * 修改调拨业务单据关系
     * 
     * @param allotRecordRelationE
     * @return
     */
    int updateAllotRecordRelation(AllotRecordRelationE allotRecordRelationE);
    
    /**
     * 根据单据编号查询调拨业务单据关系列表
     * 
     * @param recordCode
     * @return
     */
    List<AllotRecordRelationE> queryAllotRecordRelationByRecordCode(@Param("recordCode") String recordCode);
    
    /**
     * 根据调拨单号查询调拨业务单据关系列表
     * 
     * @param allotCode
     * @return
     */
    List<AllotRecordRelationE> queryAllotRecordRelationByAllotCode(@Param("allotCode") String allotCode);
    
    /**
     * 根据类型查询调拨业务单据关系列表
     * 
     * @param type
     * @return
     */
    List<AllotRecordRelationE> queryAllotRecordRelationByType(@Param("type") Integer type);
    
    /**
     * 根据单据编号查询调拨单号列表
     * 
     * @param recordCode
     * @return
     */
    List<String> queryAllotCodeByRecordCode(@Param("recordCode") String recordCode);

    /**
     * 根据行号（调拨单明细关联主键）查询调拨业务单据关系列表
     * 
     * @param lineNoList
     * @return
     */
	List<AllotRecordRelationE> queryAllotRecordRelationByLineNos(List<String> lineNoList);
    
}