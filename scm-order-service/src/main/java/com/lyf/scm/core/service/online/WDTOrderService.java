package com.lyf.scm.core.service.online;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.api.dto.online.OnlineOrderDTO;
import com.lyf.scm.core.api.dto.online.QueryWDTOrderDTO;
import com.lyf.scm.core.api.dto.online.WDTOrderPageInfoDTO;
import com.lyf.scm.core.api.dto.online.WDTStockLogisticInfoParamDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;

import java.util.List;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/2
 */
public interface WDTOrderService {

    /**
     * 旺店通下单锁库存
     *
     * @param * @param onlineOrderDTO
     * @return com.lyf.scm.core.remote.stock.dto.RealWarehouse
     * @author Lucky
     * @date 2020/7/2  16:13
     */
    RealWarehouse lockStockByRecord(OnlineOrderDTO onlineOrderDTO);

    /**
     * 功能描述 保存物流公司编码,SO维度
     *
     * @param * @param paramDTO
     * @return void
     * @author Lucky
     * @date 2020/7/2  16:13
     */
    void saveLogisticInfo(List<WDTStockLogisticInfoParamDTO> paramDTO);

    PageInfo<WDTOrderPageInfoDTO> queryForSplitPage(QueryWDTOrderDTO queryWDTOrderDTO);

    /**
     * 拆单页面明细
     *
     * @param * @param wdtOrderPageInfoDTO
     * @return com.lyf.scm.core.api.dto.online.WDTOrderPageInfoDTO
     * @author Lucky
     * @date 2020/7/2  16:12
     */
    WDTOrderPageInfoDTO querySplitDetail(WDTOrderPageInfoDTO wdtOrderPageInfoDTO);

    /**
     * 拆单核心方法
     *
     * @param * @param wdtOrderPageInfoDTO
     * @return boolean
     * @author Lucky
     * @date 2020/7/2  16:13
     */
    boolean splitOrder(WDTOrderPageInfoDTO wdtOrderPageInfoDTO);

    /**
     * 重新计算仓库
     *
     * @param * @param wdtPageInfoDTO
     * @return boolean
     * @author Lucky
     * @date 2020/7/2  16:14
     */
    boolean recalculateHouse(WDTOrderPageInfoDTO wdtOrderPageInfoDTO);


    /**
     * 根据子do单号取消do单
     * @param doCode
     */
    void cancelChildDo(String doCode);

}
