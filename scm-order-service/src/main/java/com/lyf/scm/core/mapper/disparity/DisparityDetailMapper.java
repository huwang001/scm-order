package com.lyf.scm.core.mapper.disparity;

import java.util.List;

import com.lyf.scm.core.domain.model.disparity.DisparityDetailDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.lyf.scm.core.domain.entity.disparity.DisparityDetailE;

/**
 * @ClassName: DisparityDetailMapper  
 * @Description: 差异明细记录信息映射  
 * @author: Lin.Xu  
 * @date: 2020-7-15 11:03:49
 * @version: v1.0
 */
@Mapper
public interface DisparityDetailMapper {

	/**
	 * @Method: insertBatch  
	 * @Description: 批量插入差异订单明细信息
	 * @param detailDoList
	 * @author: Lin.Xu 
	 * @date: 2020-7-15 11:08:40 
	 * @return: void
	 * @throws
	 */
	void insertBatch(@Param("list") List<DisparityDetailDO> detailDoList);

	/**
	 * @Method: updateReasonsById  
	 * @Description: 批量保存定责责任原因
	 * @param detailEList
	 * @author: Lin.Xu 
	 * @date: 2020-7-15 11:29:43 
	 * @return: int
	 * @throws
	 */
	int updateReasonsById(@Param("list") List<DisparityDetailE> detailEList);

	/**
	 * @Method: selectHasNotDutyDetails  
	 * @Description: 查询还未定责的明细
	 * @param disparityId
	 * @author: Lin.Xu 
	 * @date: 2020-7-15 11:29:59 
	 * @return: List<Long>
	 * @throws
	 */
	List<Long> selectHasNotDutyDetails(@Param("disparityId") Long disparityId);

	/**
	 * @Method: selectDisparityDetailByDisparityId  
	 * @Description: 根据差异单id查询差异明细[只查询差异为正值的]
	 * @param disparityId
	 * @author: Lin.Xu 
	 * @date: 2020-7-15 11:30:50 
	 * @return: List<DisparityDetailE>
	 * @throws
	 */
	List<DisparityDetailE> selectDisparityDetailByDisparityId(@Param("disparityId") Long disparityId);

	/**
	 * @Method: selectDisparityDetailByIds  
	 * @Description: 根据差异明细id查询明细[只查询差异为正值的]
	 * @param ids
	 * @author: Lin.Xu 
	 * @date: 2020-7-15 11:31:06 
	 * @return: List<DisparityDetailE>
	 * @throws
	 */
	List<DisparityDetailE> selectDisparityDetailByIds(@Param("ids") List<Long> ids);
	
}
