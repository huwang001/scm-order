package com.lyf.scm.admin.controller.pack;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.common.excel.ExcelUtil;
import com.lyf.scm.admin.dto.pack.*;
import com.lyf.scm.admin.remote.facade.DemandFacade;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.pack.DemandPickStatusEnum;
import com.lyf.scm.common.enums.pack.DemandRecordStatusEnum;
import com.lyf.scm.common.enums.pack.PackCreateTypeEnum;
import com.lyf.scm.common.enums.pack.PackTypeEnum;
import com.lyf.scm.common.util.date.DateUtil;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import com.rome.user.common.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 需求单
 *
 * @Author: liuyao
 * @Date: 2020/7/7
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/demand")
@Api(tags = {"需求单"})
public class DemandController {


    @Resource
    private DemandFacade demandFacade;

    @ApiOperation(value = "新增需求单", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @PostMapping("/createDemand")
    public Response<String> createDemand(@ApiParam(name = "TruckingOrderDTO", value = "派车单新增对象")
                                         @RequestBody @Valid DemandDTO demandDTO, HttpServletRequest request) {
        try {
            demandDTO.setUserId(UserContext.getUserId());
            String result = demandFacade.createDemand(demandDTO);
            return Response.builderSuccess(result);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据历史需求号返回成品明细和组件明细", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryDemandDetailAndComponent", method = RequestMethod.GET)
    public Response<DemandDetailAndComponentDTO> queryDemandDetailAndComponent(@ApiParam(name = "recordCode", value = "包装需求单号") @RequestParam(name = "recordCode", required = false) String recordCode) {
        try {
            //todo
            DemandDetailAndComponentDTO result = demandFacade.queryDemandDetailAndComponent(recordCode);
            return Response.builderSuccess(result);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }


    @ApiOperation(value = "需求单列表查询-分页", nickname = "queryPackDemandPage", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryPackDemandPage", method = RequestMethod.POST)
    public Response<PageInfo<PackDemandResponseDTO>> queryPackDemandPage(@ApiParam(name = "condition", value = "查询条件") @RequestBody @Validated QueryPackDemandDTO condition) {
        try {
            PageInfo<PackDemandResponseDTO> pageInfo = demandFacade.queryPackDemandPage(condition);
            return Response.builderSuccess(pageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "需求单导出", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/exportPackDemand", method = RequestMethod.POST)
    public void exportPackDemand(HttpServletResponse response, @RequestBody @Validated QueryPackDemandDTO condition) throws Exception {
        // 定义数据格式
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        List<PackDemandResponseDTO> dataList = demandFacade.queryPackDemandExport(condition);
        List<PackDemandExcelDTO> demandExcelList = dataList.stream().map(o -> {
            PackDemandExcelDTO demand = new PackDemandExcelDTO();
            BeanUtils.copyProperties(o, demand);
            if (null != PackCreateTypeEnum.getDescByType(demand.getCreateType())) {
                demand.setCreateTypeDesc(PackCreateTypeEnum.getDescByType(demand.getCreateType()).getDesc());
            }
            if (null != PackTypeEnum.getDescByType(demand.getPackType())) {
                demand.setPackTypeDesc(PackTypeEnum.getDescByType(demand.getPackType()));
            }
            if (null != DemandRecordStatusEnum.getDescByStatus(demand.getRecordStatus())) {
                demand.setRecordStatusDesc(DemandRecordStatusEnum.getDescByStatus(demand.getRecordStatus()));
            }
            if (null != DemandPickStatusEnum.getDescByStatus(demand.getPickStatus())) {
                demand.setPickStatusDesc(DemandPickStatusEnum.getDescByStatus(demand.getPickStatus()));
            }
            return demand;
        }).collect(Collectors.toList());
        String excelName = "需求单-" + DateUtil.format(new Date(), "yyyyMMddHHmmss");
        ExcelUtil.writeExcel(response, demandExcelList, excelName, "需求单列表", PackDemandExcelDTO.class);
    }

    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    @ApiResponse(code = 200, message = "success")
    @ApiOperation(value = "导入需求单", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response importFile(@RequestBody MultipartFile file, HttpServletResponse response,
                               HttpServletRequest request) throws Exception {
        try {
            if (file == null) {
                throw new RomeException("999", "请选择文件");
            }
            Long userId = UserContext.getUserId();
            InputStream inputStream = file.getInputStream();
            List<DemandBatchDTO> result = ExcelUtil.readExcel(inputStream, DemandBatchDTO.class);
            if (result == null || result.size() == 0) {
                throw new RomeException("999", "数据不能为空");
            }
            if (result.size() > 1000) {
                throw new RomeException("999", "导入数据不能超过1000条");
            }
            demandFacade.batchCreateDemand(result, userId);
            return Response.builderSuccess(null);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            String[] msg = {e.getMessage()};
            return Response.builderFail(e.getCode(), msg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }

    }

    @ApiOperation(value = "查询需求单详情", nickname = "queryPackDemandDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryPackDemandDetail", method = RequestMethod.POST)
    public Response<PackDemandResponseDTO> queryPackDemandDetail(@RequestParam("recordCode") String recordCode) {
        try {
            return Response.builderSuccess(demandFacade.queryPackDemandDetail(recordCode));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "需求单确认", nickname = "updatePackDemandStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/updatePackDemandStatus", method = RequestMethod.POST)
    public Response updatePackDemandStatus(@RequestBody List<String> recordCodes) {
        DemandConfirmFromPageDTO demandConfirmFromPageDTO = new DemandConfirmFromPageDTO();
        demandConfirmFromPageDTO.setUserId(UserContext.getUserId());
        demandConfirmFromPageDTO.setRecordCodeList(recordCodes);
        try {
            return Response.builderSuccess(demandFacade.updatePackDemandStatus(demandConfirmFromPageDTO));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "批量取消需求单", nickname = "batchCancelDemand", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/batchCancelDemand", method = RequestMethod.POST)
    public Response<List<DemandCancelResponseDTO>> batchCancelDemand(@RequestBody List<String> recordCodes) {
        try {
            List<DemandCancelResponseDTO> responseDTOList = demandFacade.batchCancelDemand(recordCodes);
            return Response.builderSuccess(responseDTOList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "商品信息展示", nickname = "querySkuInfoListByChannel", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/querySkuInfoListByChannel", method = RequestMethod.POST)
    public Response<PageInfo<SkuAttributeInfo>> querySkuInfoListByChannel(@RequestBody SkuParamDTO skuParamDTO) {
        try {
            PageInfo<SkuAttributeInfo> skuList = demandFacade.pageSkuInfo(skuParamDTO);
            return Response.builderSuccess(skuList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询成品信息及组合品子品信息（如果是组装商品）", nickname = "queryCombinesBySkuCodeAndUnitCode", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryCombinesBySkuCodeAndUnitCode", method = RequestMethod.POST)
    public Response<SkuAndCombineDTO> queryCombinesBySkuCodeAndUnitCode(@RequestBody List<ParamExtDTO> param,@RequestParam("packType") Integer packType) {
        try {
            SkuAndCombineDTO skuAndCombineInfo = demandFacade.queryCombinesBySkuCodeAndUnitCode(param,packType);
            return Response.builderSuccess(skuAndCombineInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "需求单列表查询", nickname = "queryPackDemandList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryPackDemandList", method = RequestMethod.POST)
    public Response<PageInfo<PackDemandResponseDTO>> queryPackDemandList(@ApiParam(name = "condition", value = "查询条件") @RequestBody @Validated QueryPackDemandDTO condition) {
        try {
            PageInfo<PackDemandResponseDTO> pageInfo = demandFacade.queryPackDemandList(condition);
            return Response.builderSuccess(pageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "商品类型查询", nickname = "skuType", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/skuType", method = RequestMethod.GET)
    public Response<List<SkuTypeDTO>> skuType(){
        try {
            List<SkuTypeDTO> skuTypeDTOS = demandFacade.skuType();
            return Response.builderSuccess(skuTypeDTOS);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }

    }

}
