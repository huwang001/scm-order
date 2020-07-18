package com.lyf.scm.core.mapper.reverse;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lyf.scm.core.domain.entity.reverse.ReverseDetailE;

/**
 * @Description: 冲销单明细Mapper <br>
 *
 * @Author wwh 2020/7/16
 */
public interface ReverseDetailMapper {
	
	/**
	 * 保存冲销单明细
	 * 
	 * @param reverseDetailE
	 * @return
	 */
    int insertReverseDetail(ReverseDetailE reverseDetailE);

    /**
     * 批量保存冲销单明细
     * 
     * @param reverseDetailEList
     * @return
     */
    int batchInsertReverseDetail(List<ReverseDetailE> reverseDetailEList);

    /**
     * 修改冲销单明细
     * 
     * @param reverseDetailE
     * @return
     */
    int updateReverseDetail(ReverseDetailE reverseDetailE);

    /**
     * 根据ID查询冲销单明细
     * 
     * @param id
     * @return
     */
    ReverseDetailE queryById(Long id);
    
    /**
     * 根据单据编号查询冲销单明细列表
     * 
     * @param recordCode
     * @return
     */
    List<ReverseDetailE> queryByRecordCode(@Param("recordCode") String recordCode);

    /**
     * 根据冲销编号code删除成品冲销明细信息
     * @param recordCode
     */
    void deleteReverseDetailEByRequireCode(@Param("recordCode") String recordCode);
    
}