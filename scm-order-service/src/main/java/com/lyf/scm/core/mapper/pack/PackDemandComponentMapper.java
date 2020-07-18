package com.lyf.scm.core.mapper.pack;

import com.lyf.scm.core.domain.entity.pack.PackDemandComponentE;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PackDemandComponentMapper {

    /**
     * 通过需求单编号 + 成品编号 组件信息（组装、反拆）
     *
     * @param recordCode
     * @param skuCode
     * @return
     */
    List<PackDemandComponentE> queryDemandComponentByPackedType(@Param("recordCode") String recordCode, @Param("skuCode") String skuCode);

    /**
     * 根据需求编码查询需求单明细原料列表
     * 
     * @param recordCode
     * @return
     */
    List<PackDemandComponentE> queryDemandComponentByRecordCode(@Param("recordCode") String recordCode);

    /**
     * 批量新增原料
     * @param packDemandComponentEList
     * @return
     */
    int batchInsertPackDemandComponent(List<PackDemandComponentE> packDemandComponentEList);

    /**
     * 修改实际移库数量
     * 
     * @param orderLineNo
     * @param skuCode
     * @param actualQty
     * @return
     */
	int updateActualMoveQty(@Param("orderLineNo") String orderLineNo, @Param("skuCode") String skuCode, @Param("actualQty")BigDecimal actualQty);

	/**
	 * 根据ID查询需求单明细原料
	 * 
	 * @param id
	 * @return
	 */
	PackDemandComponentE queryById(@Param("id") Long id);

    /**
     * 根据包装需求单编号删除组件明细(物理删除)
     * @param recordCode   包装需求单编号
     * @return
     */
	int deleteByRecordCode(@Param("recordCode") String recordCode);
    
}