package com.lyf.scm.admin.controller;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.common.excel.ExcelUtil;
import com.lyf.scm.admin.remote.dto.OrderReturnDTO;
import com.lyf.scm.admin.remote.dto.OrderReturnDetailDTO;
import com.lyf.scm.admin.remote.facade.OrderReturnFacade;
import com.lyf.scm.admin.remote.template.OrderReturnExportTemplate;
import com.lyf.scm.common.constants.ResCode;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 退货单controller
 * <p>
 * @Author: wwh 2020/4/16
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/orderReturn")
@Api(tags = {"退货单接口管理"})
public class OrderReturnController {

    @Resource
    private OrderReturnFacade orderReturnFacade;

    @ApiOperation(value = "根据条件查询退货单列表-分页", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryOrderReturnPageByCondition", method = RequestMethod.POST)
    public Response<PageInfo<OrderReturnDTO>> queryOrderReturnPageByCondition(@ApiParam(name = "OrderReturnDTO", value = "退货单查询对象") @RequestBody @Valid OrderReturnDTO orderReturnDTO) {
        try {
            PageInfo<OrderReturnDTO> pageInfo = orderReturnFacade.queryOrderReturnPageByCondition(orderReturnDTO);
            return Response.builderSuccess(pageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据售后单号查询退货单详情列表-分页", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryOrderReturnDetailPageByAfterSaleCode", method = RequestMethod.POST)
    public Response<OrderReturnDTO> queryOrderReturnDetailPageByAfterSaleCode(@ApiParam(name = "afterSaleCode", value = "售后单号") @RequestParam(name = "afterSaleCode") String afterSaleCode, @ApiParam(name = "pageNum", value = "页码") @RequestParam(name = "pageNum") Integer pageNum,
                                                                              @ApiParam(name = "pageSize", value = "每页记录数") @RequestParam(name = "pageSize") Integer pageSize) {
        try {
            OrderReturnDTO orderReturnDTO = orderReturnFacade.queryOrderReturnDetailPageByAfterSaleCode(afterSaleCode, pageNum, pageSize);
            return Response.builderSuccess(orderReturnDTO);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据售后单号查询退货单（包含退货单详情）", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryOrderReturnWithDetailByAfterSaleCode/{afterSaleCode}", method = RequestMethod.GET)
    public Response<OrderReturnDTO> queryOrderReturnWithDetailByAfterSaleCode(@ApiParam(name = "afterSaleCode", value = "售后单号") @PathVariable(name = "afterSaleCode") String afterSaleCode) {
        try {
            OrderReturnDTO orderReturnDTO = orderReturnFacade.queryOrderReturnWithDetailByAfterSaleCode(afterSaleCode);
            return Response.builderSuccess(orderReturnDTO);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据售后单号查询退货单详情列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryOrderReturnDetailByAfterSaleCode/{afterSaleCode}", method = RequestMethod.GET)
    public Response<List<OrderReturnDetailDTO>> queryOrderReturnDetailByAfterSaleCode(@ApiParam(name = "afterSaleCode", value = "售后单号") @PathVariable(name = "afterSaleCode") String afterSaleCode) {
        try {
            List<OrderReturnDetailDTO> orderReturnDetailDTOList = orderReturnFacade.queryOrderReturnDetailByAfterSaleCode(afterSaleCode);
            return Response.builderSuccess(orderReturnDetailDTOList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "导出退货单", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/exportOrderReturn", method = RequestMethod.POST)
    public Response exportOrderReturn(@ApiParam(name = "OrderReturnDTO", value = "退货单查询对象") @RequestBody @Valid OrderReturnDTO orderReturnDTO, HttpServletResponse response) {
        try {
            orderReturnDTO.setPageNum(1);
            orderReturnDTO.setPageSize(1001);
            PageInfo<OrderReturnDTO> pageInfo = orderReturnFacade.queryOrderReturnPageByCondition(orderReturnDTO);
            List<OrderReturnDTO> orderReturnDTOList = pageInfo.getList();
            if (CollectionUtils.isEmpty(orderReturnDTOList)) {
                throw new RomeException("999", "无可导出或其他原因");
            }
            if (orderReturnDTOList.size() > 1000) {
                throw new RomeException(ResCode.ORDER_ERROR_1001, "导出数据量不能大于1000条");
            }
            List<OrderReturnExportTemplate> templateList = new ArrayList<OrderReturnExportTemplate>();
            orderReturnDTOList.forEach(e -> {
                OrderReturnExportTemplate orderReturnExportTemplate = new OrderReturnExportTemplate();
                BeanUtils.copyProperties(e, orderReturnExportTemplate);
                templateList.add(orderReturnExportTemplate);
            });
            ExcelUtil.writeExcel(response, templateList, "团购退货单", "sheet1", OrderReturnExportTemplate.class);
            return null;
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1001, ResCode.ORDER_ERROR_1001_DESC);
        }
    }

    @RequestMapping(value = "/importOrderReturn", method = RequestMethod.POST)
    public Response importOrderReturn(@RequestParam("file") MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            List<OrderReturnExportTemplate> orderReturnExportTemplateList = ExcelUtil.readExcel(inputStream, OrderReturnExportTemplate.class);
            return Response.builderSuccess(orderReturnExportTemplateList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1001, ResCode.ORDER_ERROR_1001_DESC);
        }
    }

}