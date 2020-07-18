package com.lyf.scm.core.remote.trade;

import com.lyf.scm.core.api.dto.orderReturn.PushReturnNoticeDTO;
import com.lyf.scm.core.remote.trade.dto.DoDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "trade-core-order-fulfillment")
public interface TradeRemoteService {

    @PostMapping("/v1/trade/order-fulfillment/stockLockedNotify")
    Response stockLockedNotify(@RequestParam("orderNo") String orderNo);

    @PostMapping("/v1/trade/order-fulfillment/deliverNotify")
    Response deliverNotify(@RequestBody List<DoDTO> doDTOList);

    @PostMapping("/v1/trade/reverse/inBound/notify")
    Response reverseInBoundNotify(@RequestBody PushReturnNoticeDTO pushReturnNoticeDTO);
}
