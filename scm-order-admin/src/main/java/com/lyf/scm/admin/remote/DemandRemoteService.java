package com.lyf.scm.admin.remote;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.pack.*;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/7
 */
@FeignClient(value = "scm-order-service")
public interface DemandRemoteService {

    @PostMapping("/order/v1/pack/demand/createDemand")
    Response<String> createDemand(@RequestBody DemandDTO demandDTO);

    @RequestMapping(value = "/order/v1/pack/demand/batchCreateDemand", method = RequestMethod.POST)
    Response batchCreateDemand(@RequestBody List<DemandBatchDTO> demandBatchDTOList, @RequestParam("userId") Long userId);

    @GetMapping("/order/v1/pack/demand/queryDemandDetailAndComponent")
    Response<DemandDetailAndComponentDTO> queryDemandDetailAndComponent(@RequestParam("recordCode") String recordCode);

    @RequestMapping(value = "/order/v1/pack/demand/queryPackDemandPage", method = RequestMethod.POST)
    Response<PageInfo<PackDemandResponseDTO>> queryPackDemandPage(@RequestBody QueryPackDemandDTO condition);

    @RequestMapping(value = "/order/v1/pack/demand/queryPackDemandDetail", method = RequestMethod.POST)
    Response<PackDemandResponseDTO> queryPackDemandDetail(@RequestParam("recordCode") String recordCode);

    @RequestMapping(value = "/order/v1/pack/demand/updatePackDemandStatus", method = RequestMethod.POST)
    Response updatePackDemandStatus(@RequestBody List<String> recordCodes);

    @RequestMapping(value = "/order/v1/pack/demand/batchCancelDemand", method = RequestMethod.POST)
    Response<List<DemandCancelResponseDTO>> batchCancelDemand(@RequestBody List<String> requireCodeList);

    @RequestMapping(value = "/order/v1/pack/demandDetail/pageSkuInfo", method = RequestMethod.POST)
    Response<PageInfo<SkuAttributeInfo>> pageSkuInfo(@RequestBody SkuParamDTO skuParamDTO);

    @RequestMapping(value = "/order/v1/pack/demand/demandRecordStatusConfirmed", method = RequestMethod.POST)
    Response<List<DemandConfirmedInfoDTO>> demandRecordStatusConfirmed(@RequestBody DemandConfirmFromPageDTO demandConfirmFromPageDTO);

    @RequestMapping(value = "/order/v1/pack/demandDetail/queryCombinesBySkuCodeAndUnitCode", method = RequestMethod.POST)
    Response<SkuAndCombineDTO> queryCombinesBySkuCodeAndUnitCode(@RequestBody List<ParamExtDTO> param, @RequestParam("packType") Integer packType);

    @RequestMapping(value = "/order/v1/pack/demand/queryPackDemandExport", method = RequestMethod.POST)
    Response<List<PackDemandResponseDTO>> queryPackDemandExport(@RequestBody QueryPackDemandDTO condition);

    @RequestMapping(value = "/order/v1/pack/demand/queryPackDemandList", method = RequestMethod.POST)
    Response<PageInfo<PackDemandResponseDTO>> queryPackDemandList(@RequestBody QueryPackDemandDTO condition);

    @RequestMapping(value = "/order/v1/pack/demandDetail/skuType")
    Response<List<SkuTypeDTO>> skuType();

}
