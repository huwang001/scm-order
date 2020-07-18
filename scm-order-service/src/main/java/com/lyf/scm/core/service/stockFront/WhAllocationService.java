package com.lyf.scm.core.service.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.api.dto.notify.StockNotifyDTO;
import com.lyf.scm.core.api.dto.notify.TmsNotifyDTO;
import com.lyf.scm.core.api.dto.pack.DemandAllotDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.*;
import com.lyf.scm.core.domain.entity.order.OrderE;
import com.lyf.scm.core.domain.entity.pack.PackDemandE;
import com.lyf.scm.core.domain.entity.stockFront.WhAllocationE;
import com.lyf.scm.core.remote.stock.dto.AllocationCalQtyRes;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.dto.RealWarehouseStockDTO;

import java.util.List;
import java.util.Map;

/**
 * 类WhAllocationService的实现描述：仓库调拨
 *
 * @author sunyj 2019/5/13 11:01
 */
public interface WhAllocationService {

	/**
	 * 保存仓库调拨单
	 * 
	 * @param whAllocationDTO
	 */
	void saveWhAllocation(WhAllocationDTO whAllocationDTO);

	/**
	 * 修改仓库调拨申请
	 * 
	 * @param whAllocationDTO
	 */
	void updateWhAllocation(WhAllocationDTO whAllocationDTO);

	/**
	 * 根据调拨单ID查询（预）下市商品
	 * 
	 * @param id
	 * @return
	 */
	List<String> queryMarketDownSku(Long id);

	/**
	 * 确认调拨申请
	 * 
	 * @param id
	 * @param userId
	 * @return
	 */
	String confimWhAllocation(Long id, Long userId);

	/**
	 * 取消调拨申请
	 * 
	 * @param id
	 * @param userId
	 * @param isForceCancle
	 */
	void cancleWhAllocation(Long id, Long userId, Integer isForceCancle);

	/**
	 * 分页查询仓库调拨单列表
	 * 
	 * @param whAllocationPageDTO
	 * @return
	 */
	PageInfo<WhAllocationPageDTO> queryList(WhAllocationPageDTO whAllocationPageDTO);

	/**
	 * 查询单据sku实仓虚仓分配关系[调拨入库]
	 * 
	 * @param id
	 * @return
	 */
	WhAllocationPageDTO queryAllocConfigInfo(Long id);

	/**
	 * 查询单据sku实仓虚仓分配关系[调拨出库]
	 * 
	 * @param id
	 * @return
	 */
	WhAllocationPageDTO queryOutAllocConfigInfo(Long id);

	/**
	 * 批量查询单据sku实仓虚仓分配关系[调拨入库]
	 * 
	 * @param ids
	 * @return
	 */
	List<WhAllocationPageDTO> queryAllocConfigInfoByRecords(List<Long> ids);

	/**
	 * 批量查询单据sku实仓虚仓分配关系[调拨出库]
	 * 
	 * @param ids
	 * @return
	 */
	List<WhAllocationPageDTO> queryOutAllocConfigInfoByRecords(List<Long> ids);

	/**
	 * 根据仓库ID查询实仓库存列表
	 * 
	 * @param realWarehouseStockDTO
	 * @return
	 */
	PageInfo<RealWarehouseStockDTO> queryStockByWhIdForWhAllot(RealWarehouseStockDTO realWarehouseStockDTO);

	/**
	 * 初始化新增页面
	 * 
	 * @return
	 */
	WhAllocationPageDTO initAddPage();

	/**
	 * 初始化编辑页面
	 * 
	 * @param id
	 * @return
	 */
	WhAllocationPageDTO initEditPage(Long id);

	/**
	 * 获取待同步的订单
	 * 
	 * @param page
	 * @param maxResult
	 * @return
	 */
	List<WhAllocationDTO> getWaitSyncOrder(int page, int maxResult);

	/**
	 * 下发RDC调拨单至SAP
	 * 
	 * @param whAllocationDTO
	 */
	void processWhAllocationOrderToSap(WhAllocationDTO whAllocationDTO);

	/**
	 * 导入excel文件中的数据
	 * 
	 * @param dataList
	 * @param userId
	 */
	void importFileData(List<WhAllocationTemplateDTO> dataList, Long userId);

	/**
	 * 创建差异调拨单
	 * 
	 * @param id
	 * @param userId
	 * @return
	 */
	String createDisparityAllot(Long id, Long userId);

	/**
	 * 导出调拨单
	 * 
	 * @param whAllocationPageDTO
	 * @return
	 */
	List<WhAllocationExportTemplate> exportWhallot(WhAllocationPageDTO whAllocationPageDTO);

	/**
	 * 根据出入库单据编号查询仓库调拨单
	 * 
	 * @param recordCode
	 * @return
	 */
	WhAllocationDTO queryWhAllocationByRecordCode(String recordCode);

	/**
	 * 配置单据级别的sku实仓虚仓分配比例
	 * 
	 * @param config
	 */
	void allotConfig(List<RecordRealVirtualStockSyncRelationDTO> config);

	/**
	 * 根据条件查询实仓信息-运营平台查询接口,不分页
	 * 
	 * @param paramDTO
	 * @return
	 */
	List<RealWarehouse> queryForAdmin(RealWarehouseParamDTO paramDTO);

	/**
	 * 仓库调拨单出库通知
	 * 
	 * @param stockNotifyDTO
	 * @return
	 */
	void outRecordNotify(StockNotifyDTO stockNotifyDTO);

	/**
	 * 仓库调拨单入库通知
	 * 
	 * @param stockNotifyDTO
	 * @return
	 */
	void inRecordNotify(StockNotifyDTO stockNotifyDTO);

	/**
	 * 仓库调拨派车通知
	 * 
	 * @param tmsNotifyDTO
	 * @return
	 */
	void tmsNotify(TmsNotifyDTO tmsNotifyDTO);

	/**
	 * 查询待推送给库存中心的poNo列表
	 * 
	 * @return
	 */
	List<String> queryPoNoToStock();

	/**
	 * 处理待推送给库存中心的poNo
	 * 
	 * @param recordCode
	 */
	void handlePoNoToStock(String recordCode);

	/**
	 * 查询虚仓预计算
	 * 
	 * @param queryPreCalculateDTO
	 * @return
	 */
	List<AllocationCalQtyRes> queryPreCalculate(QueryPreCalculateDTO queryPreCalculateDTO);

	/**
	 * 根据预约单创建调拨单（团购专用）
	 * 
	 * @param orderE
	 * @param whAllocationE
	 */
	void createWhallotByOrder(OrderE orderE, WhAllocationE whAllocationE);
	
	/**
	 * 根据需求单、原料明细创建调拨单
	 * 
	 * @param packDemandE
	 * @param demandAllotDetailList
	 * @return
	 */
	void createDemandAllot(PackDemandE packDemandE, List<DemandAllotDetailDTO> demandAllotDetailList);
	
	/**
	 * 取消调拨单（取消预约单时用）
	 * 
	 * @param recordCode
	 * @param userId
	 */
	void cancleAllot(String recordCode, Long userId);

}