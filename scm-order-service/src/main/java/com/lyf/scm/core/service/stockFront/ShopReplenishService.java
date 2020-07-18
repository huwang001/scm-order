package com.lyf.scm.core.service.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.api.dto.notify.StockNotifyDTO;
import com.lyf.scm.core.api.dto.stockFront.*;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;

/**
 * @Description: 门店补货
 * <p>
 * @Author: chuwenchao  2020/6/10
 */
public interface ShopReplenishService {

    /**
     * @Description: 创建门店补货需求单 <br>
     *
     * @Author chuwenchao 2020/6/10
     * @param frontRecord
     * @return 
     */
    RealWarehouse createReplenishRecord(ShopReplenishDTO frontRecord);

    /**
     * @Description: 修改门店补货需求单 <br>
     *
     * @Author chuwenchao 2020/6/10
     * @param frontRecord
     * @return 
     */
    void updateReplenishRecord(ShopReplenishDTO frontRecord);

    /**
     * @Description: 校验是否可以修改门店补货需求单 <br>
     *
     * @Author chuwenchao 2020/6/10
     * @param poCode
     * @return
     */
    boolean checkUpdateReplenishRecord(String poCode);

    /**
     * @Description: 加盟确认扣减额度成功 <br>
     *
     * @Author chuwenchao 2020/6/10
     * @param poCode
     * @param sapPoNo
     * @return
     */
    void confirmJoinReplenish(String poCode, String sapPoNo);

    /**
     * @Description: 取消申请 <br>
     *
     * @Author chuwenchao 2020/6/10
     * @param poCode
     * @param isForceCancel
     * @return
     */
    void cancelReplenish(String poCode, Integer isForceCancel, Long userId);

    /**
     * @Description: 直营门店寻源并生成出库单 <br>
     *
     * @Author chuwenchao 2020/6/15
     * @param allotDTO
     * @return
     */
    Integer allotShopReplenish(ShopReplenishAllotDTO allotDTO);


    /**
     * 查询寻源日志
     */
     PageInfo<ReplenishAllotLogDTO> queryReplenishAllotLogDTO(ReplenishAllotLogDTO condition);

    /**
     * 寻源结果报表
     */
    PageInfo<ShopReplenishReportDetailDTO> queryReplenishReportDTO(ShopReplenishReportDTO condition);

    /**
     * 交货单统计
     */
    PageInfo<ShopReplenishReportStatDTO> statReplenishReport(ShopReplenishReportDTO condition);

    /**
     * @Description: 门店补货出库通知结果处理 <br>
     *
     * @Author chuwenchao 2020/6/23
     * @param stockNotifyDTO
     * @return 
     */
    void warehouseOutNotify(StockNotifyDTO stockNotifyDTO);

    /**
     * tms派车回调 修改派车状态
     * @param recordCode 后置单编码
     */
    void dispatchResultReplenishComplete(String recordCode);

    /**
     * @Description: 门店补货入库通知结果处理（直营、加盟、冷链、直送） <br>
     *
     * @Author chuwenchao 2020/6/23
     * @param stockNotifyDTO
     * @return
     */
    void warehouseInNotify(StockNotifyDTO stockNotifyDTO);
}
