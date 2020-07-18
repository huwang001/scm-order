package com.lyf.scm.core.service.online;

import com.lyf.scm.core.domain.entity.online.AddressE;

public interface AddressService {

    /**
     * 保存地址信息
     *
     * @param addressE
     */
    void addAddressInfo(AddressE addressE);
}
