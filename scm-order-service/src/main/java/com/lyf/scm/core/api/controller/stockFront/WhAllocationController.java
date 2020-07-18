package com.lyf.scm.core.api.controller.stockFront;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.lyf.scm.common.enums.RealWarehouseTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.KibanaLogConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.api.dto.orderReturn.OrderReturnDTO;
import com.lyf.scm.core.api.dto.stockFront.PosQueryDTO;
import com.lyf.scm.core.api.dto.stockFront.PosResultDTO;
import com.lyf.scm.core.api.dto.stockFront.QueryPreCalculateDTO;
import com.lyf.scm.core.api.dto.stockFront.RealWarehouseParamDTO;
import com.lyf.scm.core.api.dto.stockFront.RecordRealVirtualStockSyncRelationDTO;
import com.lyf.scm.core.api.dto.stockFront.WhAllocationDTO;
import com.lyf.scm.core.api.dto.stockFront.WhAllocationExportTemplate;
import com.lyf.scm.core.api.dto.stockFront.WhAllocationPageDTO;
import com.lyf.scm.core.api.dto.stockFront.WhAllocationTemplateDTO;
import com.lyf.scm.core.config.ServiceKibanaLog;
import com.lyf.scm.core.remote.stock.dto.AllocationCalQtyRes;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.dto.RealWarehouseStockDTO;
import com.lyf.scm.core.remote.user.dto.EmployeeInfoDTO;
import com.lyf.scm.core.remote.user.facade.UserFacade;
import com.lyf.scm.core.service.stockFront.WhAllocationService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 类WhAllocationController的实现描述:仓库调拨
 *
 * @author sunyj 2019/5/13 10:20
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/wh_allocation")
@Api(tags = {"仓库调拨"})
public class WhAllocationController {

    @Autowired
    private WhAllocationService whAllocationService;

    @Resource
    private UserFacade userFacade;

    @ApiOperation(value = "仓库调拨申请保存", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/addWhAllocationApply", method = RequestMethod.POST)
    public Response addWhAllocationApply(
            @ApiParam(name = "whAllocationDTO", value = "仓库调拨申请单") @RequestBody @Validated WhAllocationDTO whAllocationDTO) {
        try {
            whAllocationService.saveWhAllocation(whAllocationDTO);
            return Response.builderSuccess(null);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "修改仓库调拨申请", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/updateWhAllocationApply", method = RequestMethod.POST)
    public Response updateWhAllocationApply(
            @ApiParam(name = "whAllocationDTO", value = "仓库调拨申请单") @RequestBody @Validated WhAllocationDTO whAllocationDTO) {
        try {
            whAllocationService.updateWhAllocation(whAllocationDTO);
            return Response.builderSuccess(null);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据调拨单ID查询（预）下市商品", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryMarketDownSku", method = RequestMethod.POST)
    public Response<List<String>> queryMarketDownSku(
            @ApiParam(name = "id", value = "仓库调拨单ID") @RequestParam("id") Long id) {
        try {
            List<String> skuCodes = whAllocationService.queryMarketDownSku(id);
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
    public Response<String> confimWhAllocation(@ApiParam(name = "id", value = "仓库调拨单ID") @RequestParam("id") Long id,
                                               @ApiParam(name = "userId", value = "操作人ID") @RequestParam("userId") Long userId) {
        try {
            String res = whAllocationService.confimWhAllocation(id, userId);
            return Response.builderSuccess(res);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "取消调拨申请", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/cancleWhAllocation", method = RequestMethod.POST)
    public Response cancleWhAllocation(@ApiParam(name = "id", value = "单据Id") @RequestParam Long id,
                                       @ApiParam(name = "userId", value = "用户ID") @RequestParam Long userId,
                                       @ApiParam(name = "isForceCancle", value = "是否是强制取消") @RequestParam Integer isForceCancle) {
        try {
            whAllocationService.cancleWhAllocation(id, userId, isForceCancle);
            return Response.builderSuccess(null);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        } finally {
            try {
                List<EmployeeInfoDTO> empList = userFacade.getEmployeeInfoByUserIds(Arrays.asList(userId));
                if (CollectionUtils.isNotEmpty(empList)) {
                    EmployeeInfoDTO dto = empList.get(0);
                    log.error(dto.getEmployeeNumber() + "提交了取消调拨订单,主键为:" + id);
                }
            } catch (Exception e) {
                log.error("输出取消人失败", e);
            }
        }
    }

    @ApiOperation(value = "分页查询仓库调拨单列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/listPage", method = RequestMethod.POST)
    public Response<PageInfo<WhAllocationPageDTO>> listPage(
            @ApiParam(name = "whAllocationPageDTO", value = "查询条件") @RequestBody WhAllocationPageDTO whAllocationPageDTO) {
        try {
            PageInfo<WhAllocationPageDTO> pageInfo = whAllocationService.queryList(whAllocationPageDTO);
            return Response.builderSuccess(pageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询单据sku实仓虚仓分配关系[调拨入库]", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryAllocConfigInfo/{recordId}", method = RequestMethod.GET)
    public Response<WhAllocationPageDTO> queryAllocConfigInfo(
            @ApiParam(name = "recordId", value = "仓库调拨单ID") @PathVariable Long recordId) {
        try {
            return Response.builderSuccess(whAllocationService.queryAllocConfigInfo(recordId));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询单据sku实仓虚仓分配关系[调拨出库]", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryOutAllocConfigInfo/{recordId}", method = RequestMethod.GET)
    public Response<WhAllocationPageDTO> queryOutAllocConfigInfo(
            @ApiParam(name = "recordId", value = "仓库调拨单ID") @PathVariable Long recordId) {
        try {
            return Response.builderSuccess(whAllocationService.queryOutAllocConfigInfo(recordId));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "批量查询单据sku实仓虚仓分配关系[调拨入库]", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryAllocConfigInfoByRecords", method = RequestMethod.POST)
    public Response<List<WhAllocationPageDTO>> queryAllocConfigInfoByRecords(
            @ApiParam(name = "recordIds", value = "仓库调拨单ID集合") @RequestBody List<Long> recordIds) {
        try {
            return Response.builderSuccess(whAllocationService.queryAllocConfigInfoByRecords(recordIds));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "批量查询单据sku实仓虚仓分配关系[调拨出库]", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryOutAllocConfigInfoByRecords", method = RequestMethod.POST)
    public Response<List<WhAllocationPageDTO>> queryOutAllocConfigInfoByRecords(
            @ApiParam(name = "recordIds", value = "仓库调拨单ID集合") @RequestBody List<Long> recordIds) {
        try {
            return Response.builderSuccess(whAllocationService.queryOutAllocConfigInfoByRecords(recordIds));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据仓库ID查询实仓库存列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryStockByWhIdForWhAllot", method = RequestMethod.POST)
    public Response<PageInfo<RealWarehouseStockDTO>> queryStockByWhIdForWhAllot(
            @RequestBody RealWarehouseStockDTO dto) {
        try {
            PageInfo<RealWarehouseStockDTO> stockList = whAllocationService.queryStockByWhIdForWhAllot(dto);
            return Response.builderSuccess(stockList);
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
            whAllocationService.allotConfig(config);
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
            return Response.builderSuccess(whAllocationService.initAddPage());
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "初始化编辑页面", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/initEditPage", method = RequestMethod.GET)
    public Response<WhAllocationPageDTO> initEditPage(@RequestParam("id") Long id) {
        try {
            return Response.builderSuccess(whAllocationService.initEditPage(id));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "获取待同步的订单", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/getWaitSyncOrder", method = RequestMethod.POST)
    public Response<List<WhAllocationDTO>> getWaitSyncOrder(@RequestParam("page") int page,
                                                            @RequestParam("maxResult") int maxResult) {
        try {
            List<WhAllocationDTO> list = whAllocationService.getWaitSyncOrder(page, maxResult);
            return Response.builderSuccess(list);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "下发同公司PO单给SAP", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/processWhAllocationOrderToSap", method = RequestMethod.POST)
    public Response processWhAllocationOrderToSap(@RequestBody WhAllocationDTO whAllocationDTO) {
        try {
            whAllocationService.processWhAllocationOrderToSap(whAllocationDTO);
            return Response.builderSuccess(null);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @RequestMapping(value = "/importWhAllocation", method = RequestMethod.POST)
    @ApiOperation(value = "导入excel文件中的数据", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response importWhAllocation(@RequestBody List<WhAllocationTemplateDTO> dataList,
                                       @RequestParam("userId") Long userId) {
        try {
            whAllocationService.importFileData(dataList, userId);
            return Response.builderSuccess(null);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "创建差异调拨单", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/createDisparityAllot", method = RequestMethod.POST)
    public Response<String> createDisparityAllot(@RequestParam("id") Long id, @RequestParam("userId") Long userId) {
        try {
            String tipLogs = whAllocationService.createDisparityAllot(id, userId);
            return Response.builderSuccess(tipLogs);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "导出调拨单", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/exportWhallot", method = RequestMethod.POST)
    public Response<List<WhAllocationExportTemplate>> exportWhallot(@RequestBody WhAllocationPageDTO whAllocationPageDTO) {
        try {
            List<WhAllocationExportTemplate> exportWhallot = whAllocationService.exportWhallot(whAllocationPageDTO);
            return Response.builderSuccess(exportWhallot);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据出入库单据编号查询仓库调拨单", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryWhAllocationByRecordCode", method = RequestMethod.GET)
    public Response<WhAllocationDTO> queryWhAllocationByRecordCode(
            @ApiParam(name = "recordCode", value = "出入库单据编号") @RequestParam("recordCode") String recordCode) {
        try {
            log.info("根据出入库单据编号查询仓库调拨单 <<< {}", recordCode);
            WhAllocationDTO whAllocationDTO = whAllocationService.queryWhAllocationByRecordCode(recordCode);
            return Response.builderSuccess(whAllocationDTO);
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
	public Response<List<RealWarehouse>> queryForAdmin(@RequestBody RealWarehouseParamDTO dto) {
        try {
			List<RealWarehouse> realWarehouseList = whAllocationService.queryForAdmin(dto);
            return Response.builderSuccess(realWarehouseList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询待推送给库存中心的poNo列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryPoNoToStock", method = RequestMethod.GET)
    public Response<List<OrderReturnDTO>> queryOrderReturnToStock() {
        try {
            List<String> recordCodeList = whAllocationService.queryPoNoToStock();
            return Response.builderSuccess(recordCodeList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "处理待推送给库存中心的poNo", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/handlePoNoToStock", method = RequestMethod.POST)
    public Response handleOrderReturnToStock(
            @ApiParam(name = "recordCode", value = "单据编号") @RequestParam(name = "recordCode") String recordCode) {
        try {
            log.info("处理待推送给库存中心的poNo <<< {}", recordCode);
            whAllocationService.handlePoNoToStock(recordCode);
            return Response.builderSuccess(null);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询虚仓预计算（库存中心入库回调时查询用）", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryPreCalculate", method = RequestMethod.POST)
    public Response queryPreCalculate(
            @ApiParam(name = "queryPreCalculateDTO", value = "查询虚仓预计算") @RequestBody @Validated QueryPreCalculateDTO queryPreCalculateDTO) {
        try {
            log.info("查询虚仓预计算 <<< {}", JSON.toJSONString(queryPreCalculateDTO));
            List<AllocationCalQtyRes> preResult = whAllocationService.queryPreCalculate(queryPreCalculateDTO);
            return Response.builderSuccess(preResult);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

}