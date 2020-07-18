package com.lyf.scm.core.service.online.impl;

import com.lyf.scm.core.domain.entity.online.AddressE;
import com.lyf.scm.core.mapper.online.AddressMapper;
import com.lyf.scm.core.service.online.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service("addressService")
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressMapper addressMapper;

    @Override
    public void addAddressInfo(AddressE addressE) {
        addressMapper.saveAddress(addressE);
    }
}
