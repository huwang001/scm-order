package com.lyf.scm.core.mapper.disparity;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.lyf.scm.core.api.dto.disparity.DisparityRecordDetailDTO;
import com.lyf.scm.core.api.dto.disparity.QueryDisparityDTO;
import com.lyf.scm.core.domain.entity.disparity.DisparityRecordE;
import com.lyf.scm.core.domain.model.disparity.DisparityRecordDO;

/**
 * @ClassName: DisparityRecordMapper  
 * @Description: 插入差异订单映射  
 * @author: Lin.Xu  
 * @date: 2020-7-15 11:14:22
 * @version: v1.0
 */
@Mapper
public interface DisparityRecordMapper {

	/**
	 * @Method: isnert  
	 * @Description: 插入差异订单对象
	 * @param disparityRecordDO
	 * @author: Lin.Xu 
	 * @date: 2020-7-15 11:15:08 
	 * @return: int
	 * @throws
	 */
    int isnert(DisparityRecordDO disparityRecordDO);
    
    /**
     * @Method: updateToWait  
     * @Description: 从已定责更新为待过账
     * @param ids
     * @param modifier
     * @author: Lin.Xu 
     * @date: 2020-7-15 11:29:19 
     * @return: void
     * @throws
     */
    void updateToWait(@Param("ids") List<Long> ids, @Param("modifier") Long modifier);
    
    /**
     * @Method: updateToComplete  
     * @Description: 从待过账更新为已处理成功
     * @param ids
     * @param modifier
     * @author: Lin.Xu 
     * @date: 2020-7-15 11:29:05 
     * @return: void
     * @throws
     */
    void updateToComplete(@Param("ids") List<Long> ids, @Param("modifier") Long modifier);

    /**
     * @Method: queryByCondition  
     * @Description: 查询差异订单页面展示信息
     * @param paramDTO
     * @author: Lin.Xu 
     * @date: 2020-7-15 11:16:14 
     * @return: List<DisparityRecordDetailDTO>
     * @throws
     */
    List<DisparityRecordDetailDTO> selectByCondition(QueryDisparityDTO paramDTO);

    /**
     * @Method: queryDisparityRecordById  
     * @Description: 根据id查询差异单
     * @param id
     * @author: Lin.Xu 
     * @date: 2020-7-15 11:28:50 
     * @return: DisparityRecordE
     * @throws
     */
    DisparityRecordE selectDisparityRecordById(Long id);

    /**
     * @Method: selectOutStockNoByPutInNo  
     * @Description: 通过入库单号查询出库单号
     * @param putInNO
     * @author: Lin.Xu 
     * @date: 2020-7-15 9:46:07 
     * @return: String
     * @throws
     */
    String selectOutStockNoByPutInNo(@Param("putInNO")String putInNO);

}
