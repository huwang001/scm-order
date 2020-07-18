package com.lyf.scm.core.remote.trade;

import com.lyf.scm.core.remote.trade.dto.PosDaySummaryCondition;
import com.lyf.scm.core.remote.trade.dto.PosDaySummaryDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "trade-core-order-server")
public interface TradeCoreRemoteService {

    @PostMapping("/v1/trade/posDaySumary")
    Response<PosDaySummaryDTO> posDaySumary(@RequestBody PosDaySummaryCondition posDaySummaryCondition);
}
