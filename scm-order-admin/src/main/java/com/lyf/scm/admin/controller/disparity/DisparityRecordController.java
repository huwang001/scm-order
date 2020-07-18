package com.lyf.scm.admin.controller.disparity;


import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.common.constant.DisparityStatusVO;
import com.lyf.scm.admin.common.excel.ExcelUtil;
import com.lyf.scm.admin.dto.OrderExcelDTO;
import com.lyf.scm.admin.dto.disparity.DisparityRecordDetailDTO;
import com.lyf.scm.admin.dto.disparity.DisparityRecord;
import com.lyf.scm.admin.dto.disparity.DisparityRecordDetail;
import com.lyf.scm.admin.dto.disparity.DisparityRecordParamDTO;
import com.lyf.scm.admin.remote.dto.EmployeeInfoDTO;
import com.lyf.scm.admin.remote.dto.OrderRespDTO;
import com.lyf.scm.admin.remote.facade.DisparityRecordFacade;
import com.lyf.scm.admin.remote.facade.UserCoreFacade;
import com.lyf.scm.admin.remote.template.DisparityRecordExportTemplate;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.OrderStatusEnum;
import com.lyf.scm.common.enums.OrderTypeEnum;
import com.lyf.scm.common.enums.ResponseMsg;
import com.lyf.scm.common.util.validate.ParamValidator;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RomeController
@RequestMapping("/order/v1/disparity/record")
@Api(tags={"差异单管理接口"}, description="DisparityRecordController")
public class DisparityRecordController {

    private ParamValidator validator = ParamValidator.INSTANCE;

    @Autowired
    private DisparityRecordFacade disparityRecordFacade;
    @Resource
    private UserCoreFacade userCoreFacade;


    @ApiOperation(value = "根据条件查询差异单信息,分页", nickname = "query_by_condition", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = DisparityRecord.class)
    @RequestMapping(value = "/query_by_condition", method = RequestMethod.POST)
    public Response findByConditionPage(@RequestBody DisparityRecordParamDTO paramDTO) {
        if(! validator.validParam(paramDTO)) {
            return Response.builderFail(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        try {
            if(null != paramDTO.getEmpNum() && !"".equals(paramDTO.getEmpNum())){
                //若查询条件empNum不为空,转换成用户id
                EmployeeInfoDTO employeeNum = userCoreFacade.getUserIdByEmployeeNum(paramDTO.getEmpNum());
                if (null != employeeNum) {
                    paramDTO.setModifier(employeeNum.getId());
                } else {
                    return ResponseMsg.SUCCESS.buildMsg(new PageInfo<>());
                }
            }
            PageInfo<DisparityRecordDetailDTO> pageInfo = disparityRecordFacade.queryByCondition(paramDTO);
            return ResponseMsg.SUCCESS.buildMsg(pageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.FAIL.buildMsg(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @RequestMapping(value = "/exportDisparityRecords", method = RequestMethod.POST)
    @ApiOperation(value = "导出差异单据", nickname = "exportDisparityRecords", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response exportDisparityRecords(@RequestBody DisparityRecordParamDTO paramDTO, HttpServletResponse response) {
        if(! validator.validParam(paramDTO)) {
            return ResponseMsg.PARAM_ERROR.buildMsg();
        }
        try {
            PageInfo<DisparityRecordDetailDTO> pageInfo ;
            //在后台再做一次时间校验
            if (paramDTO.getCreateStartDate() == null || paramDTO.getCreateEndDate() == null) {
                throw new RomeException(ResCode.STOCK_ERROR_1002, ResCode.STOCK_ERROR_1002_DESC + ":时间必填");
            }
            long max = 3600 * 24 * 30 * 1000L;
            if (paramDTO.getCreateEndDate().getTime() - paramDTO.getCreateStartDate().getTime() > max) {
                throw new RomeException(ResCode.STOCK_ERROR_1002, ResCode.STOCK_ERROR_1002_DESC + ":时间跨度不能大于30天");
            }
            //设置查询全部
            paramDTO.setPageIndex(1);
            paramDTO.setPageSize(Integer.MAX_VALUE);
            boolean needQuery = true;
            if (null != paramDTO.getEmpNum() && !"".equals(paramDTO.getEmpNum())) {
                //若查询条件empNum不为空,转换成用户id
                EmployeeInfoDTO employeeNum = userCoreFacade.getUserIdByEmployeeNum(paramDTO.getEmpNum());
                if (null != employeeNum) {
                    paramDTO.setModifier(employeeNum.getId());
                } else {
                    needQuery = false;
                }
            }
            if (needQuery) {
                pageInfo = disparityRecordFacade.queryByCondition(paramDTO);
            } else {
                pageInfo = new PageInfo<>();
            }
            Map<String, Object> map = new HashMap<>();
            String excelName = "补货差异单明细" ;
            if(paramDTO.getRecordType() == 51){
                excelName = "退货差异单明细" ;
            }

            List<DisparityRecordDetailDTO> list = pageInfo.getList();
            List<DisparityRecordExportTemplate> disparityRecordExportTemplateList = list.stream().map(o -> {
                DisparityRecordExportTemplate disparityRecordExportTemplate = new DisparityRecordExportTemplate();
                BeanUtils.copyProperties(o, disparityRecordExportTemplate);
                return disparityRecordExportTemplate;
            }).collect(Collectors.toList());
            ExcelUtil.writeExcel(response, disparityRecordExportTemplateList, excelName, "sheet1", DisparityRecordExportTemplate.class);
            return null;
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.EXCEPTION.buildMsgWithSelf();
        }

    }

    @ApiOperation(value = "差异定责", nickname = "disparityDuty", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = DisparityRecord.class)
    @RequestMapping(value = "/disparityDuty", method = RequestMethod.POST)
    public Response disparityDuty(@RequestBody List<DisparityRecordDetail> details) {
        if(details == null || details.size() == 0) {
            return ResponseMsg.PARAM_ERROR.buildMsg();
        }
        try {
            disparityRecordFacade.disparityDuty(details);
            return ResponseMsg.SUCCESS.buildMsg();
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.FAIL.buildMsg(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.EXCEPTION.buildMsg();
        }
    }

    @ApiOperation(value = "差异处理", nickname = "handlerDisparity", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = DisparityRecord.class)
    @RequestMapping(value = "/handlerDisparity", method = RequestMethod.POST)
    public Response handlerDisparity(@RequestBody List<Long>  ids) {
        try {
            disparityRecordFacade.handlerDisparity(ids);
            return ResponseMsg.SUCCESS.buildMsg();
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.FAIL.buildMsg(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.EXCEPTION.buildMsg();
        }
    }

    @ApiOperation(value = "整单拒收落差异单批处理", nickname = "handlerDisparity", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = DisparityRecord.class)
    @RequestMapping(value = "/rejectConfirmDisparityBySapCode", method = RequestMethod.POST)
    public Response rejectConfirmDisparityBySapCode(@RequestBody DisparityRecordParamDTO  paramDTO) {
        try {
            String tip = disparityRecordFacade.rejectConfirmDisparityBySapCode(paramDTO.getSapDeliveryCode());
            return ResponseMsg.SUCCESS.buildMsg(tip);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.FAIL.buildMsg(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.EXCEPTION.buildMsg();
        }
    }

    private List<DisparityRecordExportTemplate> convertDataForExcel(List<DisparityRecordDetail> list) {
        if (list == null || list.size() < 1) {
            return new ArrayList<>();
        }
        List<DisparityRecordExportTemplate> res = new ArrayList<>();
        for (DisparityRecordDetail detail : list) {
            DisparityRecordExportTemplate template = new DisparityRecordExportTemplate();
            template.setRecordCode(detail.getRecordCode());
            template.setSapPoNo(detail.getSapPoNo());
            template.setSapDeliveryCode(detail.getSapDeliveryCode());
            template.setOutWarehouseRecordCode(detail.getOutWarehouseRecordCode());
            template.setInWarehouseRecordCode(detail.getInWarehouseRecordCode());
            template.setOutRealWarehouseCode(detail.getOutRealWarehouseCode());
            template.setOutRealWarehouseName(detail.getOutRealWarehouseName());
            template.setInRealWarehouseCode(detail.getInRealWarehouseCode());
            template.setInRealWarehouseName(detail.getInRealWarehouseName());
            template.setHandlerInFactoryCode(detail.getHandlerInFactoryCode());
            template.setHandlerInRealWarehouseCode(detail.getHandlerInRealWarehouseCode());
            template.setHandlerInRealWarehouseName(detail.getHandlerInRealWarehouseName());
            template.setRecordStatus(DisparityStatusVO.getDescByType(detail.getRecordStatus()));
            template.setSkuCode(detail.getSkuCode());
            template.setSkuName(detail.getSkuName());
            template.setOutSkuQty(detail.getOutSkuQty());
            template.setInSkuQty(detail.getInSkuQty());
            template.setSkuQty(detail.getSkuQty());
            template.setUnitCode(detail.getUnitCode());
            template.setUnit(detail.getUnit());
            if (null != detail.getResponsibleType()) {
                if (1 == detail.getResponsibleType()) {
                    template.setResponsibleType("门店责任");
                } else if (2 == detail.getResponsibleType()) {
                    template.setResponsibleType("仓库责任");
                } else if (3 == detail.getResponsibleType()) {
                    template.setResponsibleType("物流责任");
                }
            }
            //1-实物在收货仓 2-实物在发货仓 3-实物丢失 4-实物质量问题 5-仓库漏发
            if (null != detail.getReasons()) {
                if (1 == detail.getReasons()) {
                    template.setReasons("实物在收货仓");
                } else if (2 == detail.getReasons()) {
                    template.setReasons("实物在发货仓");
                } else if (3 == detail.getReasons()) {
                    template.setReasons("实物丢失");
                } else if (4 == detail.getReasons()) {
                    template.setReasons("实物质量问题");
                } else if (5 == detail.getReasons()) {
                    template.setReasons("仓库漏发");
                }
            }
            template.setRemark(detail.getRemark());
            template.setCostCenter(detail.getCostCenter());
            template.setCreateTime(detail.getCreateTime());
            template.setUpdateTime(detail.getUpdateTime());
            template.setEmployeeNumber(detail.getEmployeeNumber());

            res.add(template);
        }
        return res;
    }
}
