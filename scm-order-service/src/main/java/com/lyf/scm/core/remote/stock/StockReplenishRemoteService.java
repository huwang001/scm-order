package com.lyf.scm.core.remote.stock;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lyf.scm.core.remote.stock.dto.BatchResultDTO;
import com.rome.arch.core.clientobject.Response;

/**
 * @Description: 调用库存门店补货相关接口
 * <p>
 * @Author: chuwenchao  2020/6/23
 */
@FeignClient(name = "stock-inner-app")
public interface StockReplenishRemoteService {

    @RequestMapping(value = "/stock/v1/shopReplenish/confimJoinReplenish", method = RequestMethod.POST)
    Response confirmJoinReplenish(@RequestParam("recordCode") String recordCode, @RequestParam("sapPoNo") String sapPoNo);
    
}
