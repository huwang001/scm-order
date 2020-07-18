package com.lyf.scm.admin.controller.stockFront;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.common.excel.AllotCustomCellStyleStrategy;
import com.lyf.scm.admin.common.excel.ExcelUtil;
import com.lyf.scm.admin.remote.dto.RealWarehouse;
import com.lyf.scm.admin.remote.stockFront.dto.RealWarehouseParamDTO;
import com.lyf.scm.admin.remote.stockFront.dto.RealWarehouseStockDTO;
import com.lyf.scm.admin.remote.stockFront.dto.RecordRealVirtualStockSyncRelationDTO;
import com.lyf.scm.admin.remote.stockFront.dto.WhAllocationDTO;
import com.lyf.scm.admin.remote.stockFront.dto.WhAllocationPageDTO;
import com.lyf.scm.admin.remote.stockFront.facade.WhAllocationFacade;
import com.lyf.scm.admin.remote.template.WhAllocationExportTemplate;
import com.lyf.scm.admin.remote.template.WhAllocationTemplateDTO;
import com.lyf.scm.admin.remote.template.WhAllocationTemplateDetailDTO;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.constants.WhAllocationConstants;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.user.common.utils.UserContext;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author sunyongjun
 * @Date 2019/5/13 15:49
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/order/v1/whAllocation")
public class WhAllocationController {

    @Resource
    private WhAllocationFacade whAllocationFacade;

    @ApiOperation(value = "运营平台查询列表功能", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryList", method = RequestMethod.POST)
    public Response<PageInfo<WhAllocationPageDTO>> queryList(@RequestBody WhAllocationPageDTO whAllocationPageDTO) {
        try {
            PageInfo<WhAllocationPageDTO> reasonDTO = whAllocationFacade.queryList(whAllocationPageDTO);
            return Response.builderSuccess(reasonDTO);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "保存仓库调拨前置单", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/saveWhAllocation", method = RequestMethod.POST)
    public Response saveWhAllocation(@RequestBody WhAllocationDTO dto) {
        long startDate = System.currentTimeMillis();
        try {
            Long userId = UserContext.getUserId();
            dto.setCreator(userId);
            dto.setModifier(userId);
            whAllocationFacade.saveWhAllocation(dto);
            return Response.builderSuccess(null);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        } finally {
            long endDate = System.currentTimeMillis();
            log.error("保存仓库调拨前置单: 执行时间: " + (endDate - startDate) + "毫秒");
        }

    }

    @ApiOperation(value = "修改仓库调拨前置单", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/updateWhAllocation", method = RequestMethod.POST)
    public Response updateWhAllocation(@RequestBody WhAllocationDTO dto) {
        long startDate = System.currentTimeMillis();
        try {
            Long userId = UserContext.getUserId();
            dto.setModifier(userId);
            whAllocationFacade.updateWhAllocation(dto);
            return Response.builderSuccess(null);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        } finally {
            long endDate = System.currentTimeMillis();
            log.error("修改仓库调拨前置单: 执行时间: " + (endDate - startDate) + "毫秒");
        }
    }

    @ApiOperation(value = "根据调拨单id查询（预）下市商品", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryMarketDownSku", method = RequestMethod.POST)
    public Response<List<String>> queryMarketDownSku(
            @ApiParam(name = "id", value = "申请单") @RequestParam("id") Long id) {
        try {
            List<String> skuCodes = whAllocationFacade.queryMarketDownSku(id);
            return Response.builderSuccess(skuCodes);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "确认调拨申请", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/confimWhAllocation", method = RequestMethod.POST)
    public Response<String> confimWhAllocation(Long id) {
        long startDate = System.currentTimeMillis();
        try {
            Long userId = UserContext.getUserId();
            String res = whAllocationFacade.confimWhAllocation(id, userId);
            return Response.builderSuccess(res);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        } finally {
            long endDate = System.currentTimeMillis();
            log.error("确认调拨申请: 执行时间: " + (endDate - startDate) + "毫秒");
        }
    }

    @ApiOperation(value = "取消调拨申请", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/cancleWhAllocation", method = RequestMethod.POST)
    public Response cancleWhAllocation(Long id, Integer isForceCancle) {
        long startDate = System.currentTimeMillis();
        try {
            Long userId = UserContext.getUserId();
            whAllocationFacade.cancleWhAllocation(id, userId, isForceCancle);
            return Response.builderSuccess(null);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        } finally {
            long endDate = System.currentTimeMillis();
            log.error("取消调拨申请: 执行时间: " + (endDate - startDate) + "毫秒");
        }
    }

    @ApiOperation(value = "根据仓库查询库存", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryStockByWhId", method = RequestMethod.POST)
    public Response<PageInfo<RealWarehouseStockDTO>> queryStockByWhId(@RequestBody RealWarehouseStockDTO dto) {
        try {
            PageInfo<RealWarehouseStockDTO> reasonDTO = whAllocationFacade.queryStockByWhIdForWhAllot(dto);
            return Response.builderSuccess(reasonDTO);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "模糊搜索不为门店的仓库", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryForAdmin", method = RequestMethod.POST)
    public Response<List<RealWarehouse>> queryForAdmin(String name) {
        try {
            RealWarehouseParamDTO paramDTO = new RealWarehouseParamDTO();
            paramDTO.setNotInType(RealWarehouseParamDTO.SHOP_TYPE);
            paramDTO.setNameOrCode(name);
            List<RealWarehouse> realWarehouseList = whAllocationFacade.queryForAdmin(paramDTO);
            return Response.builderSuccess(realWarehouseList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询采购单sku实仓虚仓分配关系[入库]", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryAllocConfigInfo/{recordId}", method = RequestMethod.GET)
    public Response<WhAllocationPageDTO> queryAllocConfigInfo(
            @ApiParam(name = "recordId", value = "recordId") @PathVariable Long recordId) {
        try {
            return Response.builderSuccess(whAllocationFacade.queryAllocConfigInfo(recordId));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询采购单sku实仓虚仓分配关系[出库]", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryOutAllocConfigInfo/{recordId}", method = RequestMethod.GET)
    public Response<WhAllocationPageDTO> queryOutAllocConfigInfo(
            @ApiParam(name = "recordId", value = "recordId") @PathVariable Long recordId) {
        try {
            return Response.builderSuccess(whAllocationFacade.queryOutAllocConfigInfo(recordId));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "批量查询采购单sku实仓虚仓分配关系[入库]", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryAllocConfigInfoByRecords", method = RequestMethod.POST)
    public Response<List<WhAllocationPageDTO>> queryAllocConfigInfo(
            @ApiParam(name = "recordIds", value = "recordIds") @RequestBody List<Long> recordIds) {
        try {
            return Response.builderSuccess(whAllocationFacade.queryAllocConfigInfoByRecords(recordIds));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "批量查询采购单sku实仓虚仓分配关系[出库]", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryOutAllocConfigInfoByRecords", method = RequestMethod.POST)
    public Response<List<WhAllocationPageDTO>> queryOutAllocConfigInfoByRecords(
            @ApiParam(name = "recordIds", value = "recordIds") @RequestBody List<Long> recordIds) {
        try {
            return Response.builderSuccess(whAllocationFacade.queryOutAllocConfigInfoByRecords(recordIds));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "配置单据级别的sku实仓虚仓分配比例", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/allotConfig", method = RequestMethod.POST)
    public Response allotConfig(
            @ApiParam(name = "config", value = "config") @RequestBody List<RecordRealVirtualStockSyncRelationDTO> config) {
        try {
            whAllocationFacade.allotConfig(config);
            return Response.builderSuccess(null);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "初始化新增页面", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/initAddPage", method = RequestMethod.GET)
    public Response<WhAllocationPageDTO> initAddPage() {
        try {
            return Response.builderSuccess(whAllocationFacade.initAddPage());
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "编辑页面", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/initEditPage", method = RequestMethod.GET)
    public Response<WhAllocationPageDTO> initEditPage(@RequestParam("id") Long id) {
        try {
            return Response.builderSuccess(whAllocationFacade.initEditPage(id));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    @ApiOperation(value = "导入商品", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response importFile(@ApiParam(value = "文件上传", required = true) MultipartFile file) {
        List<WhAllocationTemplateDTO> result = new ArrayList<WhAllocationTemplateDTO>();
        try {
            if (file == null) {
                throw new RomeException("999", "请选择文件");
            }
            InputStream inputStream = file.getInputStream();
            result = ExcelUtil.readExcel(inputStream, WhAllocationTemplateDTO.class);
            if (CollectionUtils.isEmpty(result)) {
                throw new RomeException("999", "数据不能为空");
            }
            //校验参数
            this.validParam(result);
            List<WhAllocationTemplateDetailDTO> detailList = new ArrayList<>();
            result.forEach(e -> {
                WhAllocationTemplateDetailDTO detail = new WhAllocationTemplateDetailDTO();
                detail.setSkuCode(e.getSkuCode());
                detail.setUnitCode(e.getUnitCode());
                detail.setReturnReason(e.getReturnReason());
                detail.setAllotQty(e.getAllotQty());
                detailList.add(detail);
            });
            result.forEach(e -> {
                e.setFrontRecordDetails(detailList);
            });
            whAllocationFacade.importFile(result, UserContext.getUserId());
            return Response.builderSuccess(null);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    /**
     * 校验参数
     *
     * @param result
     */
    private void validParam(List<WhAllocationTemplateDTO> result) {
        List<StringBuffer> errMag = new ArrayList<StringBuffer>();
        for (int i = 0; i < result.size(); i++) {
            boolean flag = false;
            WhAllocationTemplateDTO temp = result.get(i);
            StringBuffer sb = new StringBuffer();
            sb.append("第" + (i + 2) + "行：");
            if (StringUtils.isBlank(temp.getBusinessTypeStr())) {
                sb.append("[调拨类型]");
                flag = true;
            }
            if (StringUtils.isBlank(temp.getIsReturnAllotcate())) {
                sb.append("[是否退货]");
                flag = true;
            }
            if (StringUtils.isBlank(temp.getIsQualityAllotcate())) {
                sb.append("[是否质量调拨]");
                flag = true;
            }
            if (StringUtils.isBlank(temp.getOutFactoryCode())) {
                sb.append("[出库工厂编号]");
                flag = true;
            }
            if (StringUtils.isBlank(temp.getOutRealWareCode())) {
                sb.append("[出库仓库]");
                flag = true;
            }
            if (StringUtils.isBlank(temp.getInFactoryCode())) {
                sb.append("[入库工厂编号]");
                flag = true;
            }
            if (StringUtils.isBlank(temp.getInRealWareCode())) {
                sb.append("[入库仓库]");
                flag = true;
            }
            if (StringUtils.isBlank(temp.getSkuCode())) {
                sb.append("[商品编号");
                flag = true;
            }
            if (StringUtils.isBlank(temp.getAllotQty())) {
                sb.append("[数量]");
                flag = true;
            }
            if (StringUtils.isBlank(temp.getUnitCode())) {
                sb.append("[单位编号]");
                flag = true;
            }
            if (StringUtils.isNotBlank(temp.getIsReturnAllotcate())
                    && WhAllocationConstants.RETURN_ALLOCATE_STR.equals(temp.getIsReturnAllotcate())
                    && StringUtils.isBlank(temp.getReturnReason())) {
                sb.append("[退货原因]");
                flag = true;
            }
            sb.append("不能为空");
            if (flag) {
                errMag.add(sb);
            }
        }
        if (CollectionUtils.isNotEmpty(errMag)) {
            throw new RomeException(ResCode.ORDER_ERROR_1001, "导入失败：" + StringUtils.join(errMag, "；"));
        }
    }

    @ApiOperation(value = "差异创建", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/createDisparityAllot", method = RequestMethod.POST)
    public Response<String> createDisparityAllot(Long id) {
        long startDate = System.currentTimeMillis();
        try {
            Long userId = UserContext.getUserId();
            String res = whAllocationFacade.createDisparityAllot(id, userId);
            return Response.builderSuccess(res);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        } finally {
            long endDate = System.currentTimeMillis();
            log.error("差异创建: 执行时间: " + (endDate - startDate) + "毫秒");
        }
    }

    @RequestMapping(value = "/exportWhallot", method = RequestMethod.POST)
    @ApiOperation(value = "导出调拨单", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response exportWhallot(@RequestBody WhAllocationPageDTO paramDto, HttpServletResponse response) {
        try {
            List<WhAllocationExportTemplate> list = whAllocationFacade.exportWhallot(paramDto);
            if (CollectionUtils.isEmpty(list)) {
                throw new RomeException("999", "无可导出或其他原因");
            }
            AllotCustomCellStyleStrategy allotCustomCellStyleStrategy = new AllotCustomCellStyleStrategy();
            ExcelUtil.customCellWriteExcel(response, list, "仓库调拨单", "sheet1", WhAllocationExportTemplate.class, allotCustomCellStyleStrategy);
            return null;
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

}