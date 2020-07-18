package com.lyf.scm.admin.remote.stockFront;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lyf.scm.admin.remote.stockFront.dto.CustomizeTableRowDTO;
import com.rome.arch.core.clientobject.Response;

@FeignClient(value="scm-order-service")
public interface CustomizeTableRowRemoteService {

    /**
     * 根据table_code和用户获取自定义数据
     * @param tableCode
     * @param userId
     * @return
     */
    @RequestMapping(value = "/order/v1/customize_table_row/getDetailByTableCodeAndUserId", method = RequestMethod.GET)
    Response<List<CustomizeTableRowDTO>> getDetailByTableCodeAndUserId(@RequestParam("tableCode")String tableCode, @RequestParam("userId")Long userId);

    /**
     * 根据CustomizeTableRowDTO对象集合更新某表的标题信息
     * @param customizeTableRowDTOs
     * @return
     */
    @RequestMapping(value = "/order/v1/customize_table_row/updateDetailByDates", method = RequestMethod.POST)
    Response updateDetailByDates(@RequestBody List<CustomizeTableRowDTO> customizeTableRowDTOs);
}
