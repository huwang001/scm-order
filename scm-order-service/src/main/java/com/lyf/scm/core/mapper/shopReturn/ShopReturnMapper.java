package com.lyf.scm.core.mapper.shopReturn;

import com.lyf.scm.core.domain.entity.shopReturn.ShopReturnE;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ShopReturnMapper {

    /**
     * 通过编码查询门店退货单详情
     *
     * @param * @param recordCode
     * @return com.lyf.scm.core.domain.entity.shopReturn.ShopReturnE
     * @author Lucky
     * @date 2020/7/15  19:27
     */
    ShopReturnE selectByRecordCode(String recordCode);

    /**
     * 通过外部编码查询门店退货单详情
     *
     * @param * @param outRecordCode
     * @return com.lyf.scm.core.domain.entity.shopReturn.ShopReturnE
     * @author Lucky
     * @date 2020/7/15  19:28
     */
    ShopReturnE selectByOutRecordCode(String outRecordCode);

    /**
      *新增门店退货单
      * @author Lucky
      * @date 2020/7/15  19:28
      * @param  * @param shopReturnE
      * @return int
     */
    int insert(ShopReturnE shopReturnE);

    ShopReturnE queryShopReturnById(Long id);

    /**
     * 更新前置单 已出库
     *
     * @author zhanglong
     * @date 2020/7/15 12:00
     */
    void updateToOutAllocation(Long id);

    /**
     * @param recordCode
     * @description: 更新门店退货单章状态 已取消
     * @author: zys
     * @time: 2020/7/15 13:09
     */
    void updateRecordStatus(String recordCode);

    /**
     * 更新前置单 已入库
     *
     * @author zhanglong
     * @date 2020/7/15 12:00
     */
    void updateToInAllocation(Long id);

    /**
     * 更新前置单 推送交易状态：0-未推送  -->  1-待推送
     *
     * @author zhanglong
     * @date 2020/7/15 14:15
     */
    void updateRecordTransStatusToUnPush(@Param("id") Long id);

    /**
     * 根据id查询前置单
     *
     * @param idList
     * @return
     */
    List<ShopReturnE> queryShopReturnByIdS(@Param("idList") List<Long> idList);

    /**
     * 批量修改派车状态完成 0:待指定 -> 1:派车 订单状态 0初始 -> 4已派车
     *
     * @param ids
     * @return
     */
    int updateIsNeedDispatchComplete(@Param("ids") List<Long> ids);

    /**
     * 分页查询 待同步交易的 门店退货单
     * 
     * @author zhanglong
     * @date 2020/7/15 19:35
     */
    List<String> queryUnPushTradeShopReturnRecord(@Param("page") Integer page, @Param("maxResult") Integer maxResult);

    /**
     * 更新门店退货单-推交易完成 推送交易中心状态:1-待推送 --> 2-推送完成
     * 
     * @author zhanglong
     * @date 2020/7/15 20:13
     */
    int updateShopReturnTransStatusPushed(Long id);
}