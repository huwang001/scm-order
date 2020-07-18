package com.lyf.scm.core.mapper.online;

import com.lyf.scm.core.domain.entity.online.AddressE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类AddressMapper的实现描述：地址mapper
 */
public interface AddressMapper {

    /**
     * 根据订单号更新地址
     *
     * @param addressE
     * @return
     */
    int updateAddressByRecordCode(AddressE addressE);


    /**
     * 查询收货和发货地址
     */
    AddressE getDeliveryAddressByEntity(AddressE addressE);

    boolean saveAddress(AddressE addressE);

    AddressE queryByRecordCode(@Param("recordCode") String recordCode);

    List<AddressE> queryByRecordCodes(@Param("recordCodes") List<String> recordCodes);
}
