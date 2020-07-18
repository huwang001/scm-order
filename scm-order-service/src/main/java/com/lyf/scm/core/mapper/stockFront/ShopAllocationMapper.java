package com.lyf.scm.core.mapper.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.ShopAllocationE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zys
 * @Description 调拨单
 * @date 2020/6/15 15:03
 * @Version
 */
public interface ShopAllocationMapper {
    /**
     * 判断外部单据是否重复
     * @param outRecordCode
     * @return
     */
    int judgeExistByOutRecordCode(@Param("outRecordCode") String outRecordCode);

    /**
     * 保存门店调拨单
     * @param frontRecordE
     */
    void saveShopAllocationRecord(ShopAllocationE frontRecordE);

    /**
     * 查询门店调拨单列表
     * 查询字段：
     * @param frontRecordE
     * @return
     */
    List<ShopAllocationE> queryShopAllocationList(ShopAllocationE frontRecordE);


    /**
     * 根据调拨单号查询调拨单
     * @param recordCode
     * @return
     */
    ShopAllocationE queryFrontRecordByCode(@Param("recordCode")String recordCode);

    /**
     * 根据id集合查询前置单     * @param idList
     * @return
     */
    List<ShopAllocationE> queryFrontRecordByIds(@Param("idList") List<Long> idList);

}
