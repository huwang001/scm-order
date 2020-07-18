package com.lyf.scm.core.mapper.reverse;

import com.lyf.scm.core.api.dto.reverse.QueryReverseDTO;
import com.lyf.scm.core.api.dto.reverse.ReverseDTO;
import org.apache.ibatis.annotations.Param;

import com.lyf.scm.core.domain.entity.reverse.ReverseE;

import java.util.List;

/**
 * @Description: 冲销单Mapper <br>
 *
 * @Author wwh 2020/7/16
 */
public interface ReverseMapper {
	
	/**
	 * 保存冲销单
	 * 
	 * @param reverseE
	 * @return
	 */
    int insertReverse(ReverseE reverseE);

    /**
     * 修改冲销单
     * 
     * @param reverseE
     * @return
     */
    int updateReverse(ReverseE reverseE);

    /**
     * 根据ID查询冲销单
     * 
     * @param id
     * @return
     */
    ReverseE queryById(@Param("id") Long id);
    
    /**
     * 根据单据编号查询冲销单
     * 
     * @param recordCode
     * @return
     */
    ReverseE queryByRecordCode(@Param("recordCode") String recordCode);

    /**
      * @Description 分页查询冲销单
      * @author huwang
      * @Date 2020/7/17 9:10
      * @param
      * @return
      **/
    List<ReverseE> queryReversePage(@Param("queryReverseDTO") QueryReverseDTO queryReverseDTO);

    /**
      * @Description 根据单据编号更新单据状态
      * @author huwang
      * @Date 2020/7/17 14:46
      * @param
      * @return
      **/
    int updateRecordStatusByRecordCode(@Param("recordCode") String recordCode, @Param("recordStatus") Integer recordStatus);

    /**
     * 根据收货单据编号和入库冲销类型查询冲销单
     * @author huangyl
     * @Date 2020/7/17
     * @param receiptRecordCode
     * @return
     */
    ReverseE queryByReceiptRecordCode(@Param("receiptRecordCode") String receiptRecordCode);

    /**
     * 根据收货单据编号和状态
     * 修改冲销单为已确认
     * @author huangyl
     * @Date 2020/7/17
     * @param recordCode
     * @return int
     */
    int updateRecordStatusToConfirmedByRecordCode(@Param("recordCode") String recordCode, @Param("userId") Long userId);


    /**
     * 根据原始出/入库单据编号查询冲销单
     * @author huangyl
     * @Date 2020/7/17
     * @param originRecordCode
     * @return
     */
    ReverseE queryByOriginRecordCode(@Param("originRecordCode") String originRecordCode);
}