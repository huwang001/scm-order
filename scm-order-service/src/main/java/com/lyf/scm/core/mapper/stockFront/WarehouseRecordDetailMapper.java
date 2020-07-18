package com.lyf.scm.core.mapper.stockFront;

import java.math.BigDecimal;
import java.util.List;

import com.lyf.scm.core.domain.model.stockFront.WarehouseRecordDetailDO;
import org.apache.ibatis.annotations.Param;

import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;

public interface WarehouseRecordDetailMapper {

	/**
	 * 批量保存出入库单明细
	 * 
	 * 保存字段：record_code,warehouse_record_id,sku_id,sku_qty,unit
	 * 
	 * @param detailList
	 */
	void insertWarehouseRecordDetails(List<WarehouseRecordDetailE> detailList);

	/**
	 * 批量保存出入库单明细,外采专用，带deleted状态
	 * 
	 * 保存字段：record_code,warehouse_record_id,sku_id,sku_qty,unit
	 * 
	 * @param detailList
	 */
	void insertWarehouseRecordDetailsForPurchase(@Param("detailList") List<WarehouseRecordDetailE> detailList);

	/**
	 * 批量更新行信息
	 * 
	 * @param detailList
	 */
	void updateDetails(@Param("detailList") List<WarehouseRecordDetailE> detailList);

	List<WarehouseRecordDetailE> queryListByRecordId(Long recordId);



	/**
	 * 修改外采单时用
	 * 
	 * @param recordId
	 * @return
	 */
	List<WarehouseRecordDetailE> queryListByRecordIdWithDeleted(Long recordId);

	/**
	 * 增加实际商品数量
	 * 
	 * @param actualQty
	 * @param skuId
	 * @param lineNo
	 * @param warehouseRecordId
	 * @return
	 */
	Integer increaseActualQty(@Param("actualQty") BigDecimal actualQty, @Param("skuId") Long skuId,
			@Param("lineNo") String lineNo, @Param("warehouseRecordId") Long warehouseRecordId);

	/**
	 * 根据单据id查询单据明细
	 * 
	 * @param recordIds
	 * @return
	 */
	List<WarehouseRecordDetailE> queryListByRecordIds(@Param("recordIds") List<Long> recordIds);

	/**
	 * 根据单据ids和实仓id查询sku总出库数
	 * 
	 * @param recordIds
	 * @return
	 */
	List<WarehouseRecordDetailE> queryStatisticInfoListByRecordIds(@Param("recordIds") List<Long> recordIds);

	/**
	 * 通过ID更新实收数量
	 * 
	 * @param basicSkuQty
	 * @param lineNo
	 * @return
	 */
	Integer increaseActualQtyById(@Param("basicSkuQty") BigDecimal basicSkuQty, @Param("lineNo") Long lineNo,
			@Param("recordCode") String recordCode);

	Integer increaseActualQtyById(@Param("basicSkuQty") BigDecimal basicSkuQty, @Param("lineNo") Long lineNo,
			@Param("skuCode") String skuCode, @Param("recordCode") String recordCode);

	/**
	 * 更新交货单修改出库单的数量
	 * 
	 * @param detailList
	 * @return
	 */
	Integer updateDetailByDeliveryOrder(@Param("detailList") List<WarehouseRecordDetailE> detailList);

	/**
	 * @Description: 根据单据编号查询单据明细 <br>
	 *
	 * @Author chuwenchao 2019/9/20
	 * @param recordCode
	 * @return
	 */
	List<WarehouseRecordDetailE> queryListByRecordCode(String recordCode);

	/**
	 * 更新交货单修改出库单的数量
	 * 
	 * @param detailList
	 * @return
	 */
	Integer updateDeliveryLineNo(@Param("detailList") List<WarehouseRecordDetailE> detailList);

	/**
	 * @Description: 通过PO行号更新 <br>
	 *
	 * @Author chuwenchao 2019/11/12
	 * @param basicSkuQty
	 * @return
	 */
	Integer increaseActualQtyByLineNo(@Param("basicSkuQty") BigDecimal basicSkuQty, @Param("lineNo") String lineNo,
			@Param("skuCode") String skuCode, @Param("recordCode") String recordCode);

	/**
	 * @Description: 通过交货单行号更新<br>
	 *
	 * @Author chuwenchao 2019/11/12
	 * @param basicSkuQty
	 * @return
	 */
	Integer increaseActualQtyByDeliveryLineNo(@Param("basicSkuQty") BigDecimal basicSkuQty,
			@Param("deliveryLineNo") String deliveryLineNo, @Param("skuCode") String skuCode,
			@Param("recordCode") String recordCode);

	/**
	 * 批量修改sapPoNo
	 * 
	 * @param detailList
	 * @return
	 */
	Integer updateDetailSapNo(@Param("detailList") List<WarehouseRecordDetailE> detailList);

	/**
	 * 根据单据codes查询明细
	 * 
	 * @param recordCodes
	 * @return
	 */
	List<WarehouseRecordDetailE> selectListByRecordCodes(@Param("recordCodes") List<String> recordCodes);


	/**
	 * 更新出入库单明细实际收货数量
	 * 
	 * @param recordCode
	 * @param deliveryLineNo
	 * @param actualQty
	 * @param actualQty
	 * @return
	 */
	Integer updateActualQty(@Param("recordCode") String recordCode, @Param("deliveryLineNo") String deliveryLineNo,
			@Param("actualQty") BigDecimal actualQty);

}