package com.lyf.scm.core.service.shopReturn;

import com.lyf.scm.core.api.dto.notify.StockNotifyDTO;
import com.lyf.scm.core.api.dto.shopReturn.ShopReturnDTO;

import java.util.List;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/15
 */
public interface ShopReturnService {

    /**
     * 门店退货 出库单通知结果处理（直营、加盟）
     *
     * @author zhanglong
     * @date 2020/7/15 9:14
     */
    void warehouseOutNotify(StockNotifyDTO stockNotifyDTO);

    /**
     * 门店退货 入库单通知结果处理（直营、加盟）
     *
     * @author zhanglong
     * @date 2020/7/15 9:14
     */
    void warehouseInNotify(StockNotifyDTO stockNotifyDTO);

    /**
     * 门店退货
     *
     * @param * @param shopReturnDTO
     * @return void
     * @author Lucky
     * @date 2020/7/15  11:14
     */
    void addShopReturn(ShopReturnDTO shopReturnDTO);

    /**
     * @description:  门店退货 -取消
     * @param outRecordCode
     * @description: 门店退货取消
     * @author: zys
     * @time: 2020/7/15 9:41
     */
    Integer shopReturnCancel(String outRecordCode);

    /**
     * @description:  门店退货派车回调
     * @param recordCode 后置单编码
     * @author: zys
     * @time: 2020/7/15 17:14
     */
    void dispatchResultShopReturnComplete(String recordCode);

    /**
     * 查询待推送给交易的 门店退货单
     * 
     * @author zhanglong
     * @date 2020/7/15 19:20
     */
    List<String> queryUnPushTradeShopReturnRecord(Integer page, Integer maxResult);

    /**
     * 推送门店退货单 到 交易
     * 
     * @author zhanglong
     * @date 2020/7/15 19:43
     */
    void handlePushTradeShopReturnRecord(String frontRecordCode);
}
