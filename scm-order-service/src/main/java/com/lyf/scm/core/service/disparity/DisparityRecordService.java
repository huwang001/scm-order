package com.lyf.scm.core.service.disparity;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.api.dto.disparity.BatchRefusedBackDTO;
import com.lyf.scm.core.api.dto.disparity.DisparityDetailDTO;
import com.lyf.scm.core.api.dto.disparity.DisparityRecordDetailDTO;
import com.lyf.scm.core.api.dto.disparity.QueryDisparityDTO;
import com.lyf.scm.core.domain.entity.stockFront.FrontWarehouseRecordRelationE;

/**
 * @ClassName: DisparityRecordService  
 * @Description: 差异订单服务接口 
 * @author: Lin.Xu  
 * @date: 2020-7-10 20:06:23
 * @version: v1.0
 */
public interface DisparityRecordService {
	
	/**
	 * @Method: queryByCondition  
	 * @Description: 分页查询差异订单详细明细
	 * @param: @param paramDTO 查询对象信息
	 * @author: Lin.Xu 
	 * @date: 2020-7-10 20:37:34 
	 * @return: PageInfo<DisparityDetailDTO>
	 * @throws
	 */
	public PageInfo<DisparityRecordDetailDTO> queryByCondition(QueryDisparityDTO paramDTO);
	
	/**
	 * @Method: addDisparityRecord  
	 * @Description: 添加保存差异订单信息
	 * @param recordCode 入库单号
	 * @param wmsOutCode 出库单号
	 * @param frontRecordList 前置单列表
	 * @param frtype 前置单据类型
	 * @author: Lin.Xu 
	 * @date: 2020-7-10 22:26:18 
	 * @return: void
	 * @throws
	 */
	public void addDisparityRecord(String recordCode, List<FrontWarehouseRecordRelationE> frontRecordList);
	
	/**
	 * @Method: overallRejection  
	 * @Description: 批量整单拒收
	 * @param putInNos 入库单号
	 * @param modifier 修改人
	 * @author: Lin.Xu 
	 * @date: 2020-7-11 21:03:35 
	 * @return: List<BatchRefusedBackDTO>
	 * @throws
	 */
	public List<BatchRefusedBackDTO> overallRejection(List<String> putInNos, Long modifier);
	
	/**
	 * @Method: disparityDuty  
	 * @Description: 差异定责
	 * @param: @param details
	 * @author: Lin.Xu 
	 * @date: 2020-7-10 21:10:31 
	 * @return: void
	 * @throws
	 */
	public void disparityDuty(List<DisparityDetailDTO> details);
	
	/**
	 * @Method: confirmPosting  
	 * @Description: 确认过账
	 * @param: @param detailsIds
	 * @param: @param modifier
	 * @author: Lin.Xu 
	 * @date: 2020-7-10 21:10:47 
	 * @return: void
	 * @throws
	 */
	public void confirmPosting(List<Long> detailsIds,  Long modifier);
	
	/**
	 * @Description 门店补货PO推送交易
	 * @Author Lin.Xu
	 * @Date 16:32 2020/7/17
	 * @Param []
	 * @return void
	 **/
	public void pushTransactionStatus();




}
