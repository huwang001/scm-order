package com.lyf.scm.admin.controller.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.common.excel.ExcelUtil;
import com.lyf.scm.admin.remote.dto.*;
import com.lyf.scm.admin.remote.stockFront.facade.ShopReplenishFacade;
import com.lyf.scm.admin.remote.template.ShopReplenishReportExportTemplate;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.ResponseMsg;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 门店补货
 * @date 2020/6/19
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/shopReplenish")
public class ShopReplenishController {


    @Autowired
    private ShopReplenishFacade shopReplenishFacade;

    @Value("${exportReplenishReport.pageSize}")
    private Integer pageSize;


    @ApiOperation(value = "寻源结果报表", nickname = "queryReplenishReportCondition", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryReplenishReportCondition", method = RequestMethod.POST)
    public Response<PageInfo<ShopReplenishReportDetailDTO>> queryReplenishReportCondition(@ApiParam(name = "condition", value = "查询条件") @RequestBody ShopReplenishReportCondition condition) {
        try {
            PageInfo<ShopReplenishReportDetailDTO> pageInfo = shopReplenishFacade.queryReplenishReportCondition(condition);
            return Response.builderSuccess(pageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }


    @ApiOperation(value = "导出寻源结果报表", nickname = "exportReplenishReport", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/exportReplenishReport", method = RequestMethod.POST)
    public Response exportReplenishReport(@ApiParam(name = "condition", value = "查询条件") @RequestBody ShopReplenishReportCondition condition, HttpServletResponse response) {
        condition.setPageIndex(1);
        //每次查5000条
        condition.setPageSize(pageSize);
        try {
            List<ShopReplenishReportDetailDTO> dtoList = new ArrayList<ShopReplenishReportDetailDTO>();
            PageInfo<ShopReplenishReportDetailDTO> pageInfo = shopReplenishFacade.queryReplenishReportCondition(condition);
            if (pageInfo.getList() == null || pageInfo.getList().isEmpty()) {
                throw new RomeException("999", "无可导出的寻源结果");
            }
            dtoList.addAll(pageInfo.getList());
            while (true) {
                if (pageInfo.getList().size() == condition.getPageSize() && dtoList.size() < pageInfo.getTotal()) {
                    condition.setPageIndex(condition.getPageIndex() + 1);
                    pageInfo = shopReplenishFacade.queryReplenishReportCondition(condition);
                    dtoList.addAll(pageInfo.getList());
                } else {
                    break;
                }
            }

            List<ShopReplenishReportExportTemplate> templateList = new ArrayList<>(dtoList.size());
            for (ShopReplenishReportDetailDTO dto : dtoList) {
                ShopReplenishReportExportTemplate template = new ShopReplenishReportExportTemplate();
                BeanUtils.copyProperties(dto, template);
                templateList.add(template);
            }
            ExcelUtil.writeExcel(response, templateList, "寻源结果报表", "sheet1", ShopReplenishReportExportTemplate.class);
            return null;
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.EXCEPTION.buildMsgWithSelf();
        }
    }

    @ApiOperation(value = "查询寻源日志", nickname = "queryReplenishAllotLogCondition", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryReplenishAllotLogCondition", method = RequestMethod.POST)
    public Response<PageInfo<ReplenishAllotLogDTO>> queryReplenishAllotLogCondition(@ApiParam(name = "condition", value = "查询条件") @RequestBody ReplenishAllotLogCondition condition) {
        try {
            PageInfo<ReplenishAllotLogDTO> pageInfo = shopReplenishFacade.queryReplenishAllotLogCondition(condition);
            return Response.builderSuccess(pageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "交货单汇总", nickname = "statReplenishReport", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/statReplenishReport", method = RequestMethod.POST)
    public Response<PageInfo<ShopReplenishReportStatDTO>> statReplenishReport(@ApiParam(name = "condition", value = "查询条件") @RequestBody ShopReplenishReportCondition condition) {
        try {
            PageInfo<ShopReplenishReportStatDTO> dtoList = shopReplenishFacade.statReplenishReport(condition);
            return Response.builderSuccess(dtoList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }
}
