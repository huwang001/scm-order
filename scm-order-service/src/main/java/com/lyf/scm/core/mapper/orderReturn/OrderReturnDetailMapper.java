package com.lyf.scm.core.mapper.orderReturn;

import com.lyf.scm.core.domain.entity.orderReturn.OrderReturnDetailE;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description: 退货单详情Mapper <br>
 *
 * @Author wwh 2020/4/8
 */
public interface OrderReturnDetailMapper {

	/**
	 * 保存退货单详情
	 * 
	 * @param orderReturnDetailE
	 * @return
	 */
    int insertOrderReturnDetail(OrderReturnDetailE orderReturnDetailE);
    
    /**
     * 批量保存退货单详情
     * 
     * @param orderReturnDetailEList
     * @return
     */
    int batchInsertOrderReturnDetail(List<OrderReturnDetailE> orderReturnDetailEList);

    /**
     * 修改退货单详情
     * 
     * @param orderReturnDetailE
     * @return
     */
    int updateOrderReturnDetail(OrderReturnDetailE orderReturnDetailE);
    
    /**
     * 根据售后单号查询退货单详情列表
     * 
     * @param afterSaleCode
     * @return
     */
    List<OrderReturnDetailE> queryOrderReturnDetailByAfterSaleCode(@Param("afterSaleCode") String afterSaleCode);

    /**
     * 根据售后单号列表统计退货单详情skuCode退货数量
     * 
     * @param afterSaleCodeList
     * @return
     */
	List<Map<String, String>> countDetailReturnQty(List<String> afterSaleCodeList);

	/**
	 * 根据售后单号列表统计skuCode退货单详情实际收货数量
	 * 
	 * @param afterSaleCodeList
	 * @return
	 */
	List<Map<String, String>> countDetailEntryQty(List<String> afterSaleCodeList);

	/**
	 * 根据售后单号、商品编码修改实际收货数量
	 * 
	 * @param entryQty
	 * @param afterSaleCode
	 * @param skuCode
	 * @return
	 */
	int updateEntryQtyByAfterSaleCodeAndSkuCode(@Param("entryQty") BigDecimal entryQty, @Param("afterSaleCode") String afterSaleCode, @Param("skuCode") String skuCode);

}