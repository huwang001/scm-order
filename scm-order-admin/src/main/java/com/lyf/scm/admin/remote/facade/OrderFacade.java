package com.lyf.scm.admin.remote.facade;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.lyf.scm.admin.dto.order.OrderCreatePackDemandDTO;
import com.lyf.scm.admin.dto.order.QueryOrderDTO;
import com.lyf.scm.admin.remote.OrderRemoteService;
import com.lyf.scm.admin.remote.dto.OrderRespDTO;
import com.lyf.scm.admin.remote.dto.RealWarehouse;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * com.lyf.scm.admin.remote.facade
 *
 * @author zhangxu
 * @date 2020/4/13
 */
@Slf4j
@Component
public class OrderFacade {

    @Resource
    private OrderRemoteService orderRemoteService;

    /**
     * 获取预约单所有单据状态
     *
     * @return
     */
	public Map<Integer, String> getAllOrderStatusList() {
		Response<Map<Integer, String>> response = orderRemoteService.getAllOrderStatusList();
        ResponseValidateUtils.validResponse(response);
        return response.getData();
	}



    /**
     * 导出预约单
     * @param response
     * @param queryOrderDTO
     */
    public void exportOrder(HttpServletResponse response, QueryOrderDTO queryOrderDTO) {

        // 查询导出数据
        Response<List<OrderRespDTO>> result = orderRemoteService.queryOrder(queryOrderDTO);

        if (null == result||!"0".equals(result.getCode())) {
            log.warn("未查询到预约单：{}", JSON.toJSONString(queryOrderDTO));
            throw new RomeException("未查询到预约单");
        } else {
            log.error(result.getCode() + ":" + result.getMsg());
        }
        List<OrderRespDTO> searchList = result.getData();


        // 组装导出格式
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
            String fileName = "预约单列表-" + sdf.format(new Date()) + ".xls";
            response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("退款流水列表");
            HSSFRow firstRow = sheet.createRow(0);
            firstRow.createCell(0).setCellValue("预约单号");
            firstRow.createCell(1).setCellValue("销售单号");
            firstRow.createCell(2).setCellValue("仓库调拨单号");
            firstRow.createCell(3).setCellValue("团购发货单号");
            firstRow.createCell(4).setCellValue("所属客户");
            firstRow.createCell(5).setCellValue("销售订单类型");
            firstRow.createCell(6).setCellValue("预约单状态");
            firstRow.createCell(7).setCellValue("发货仓");
            firstRow.createCell(8).setCellValue("交货日期");
            firstRow.createCell(9).setCellValue("创建时间");
            HSSFRow row = null;
            OrderRespDTO item = null;
            if (searchList != null && searchList.size() > 0) {
                for (int i = 0; i < searchList.size(); i++) {
                    row = sheet.createRow(i + 1);
                    item = searchList.get(i);
//                    String refundSts = StrUtil.isEmpty(dicMap.get(item.getRefundSts()))?item.getRefundSts():dicMap.get(item.getRefundSts());
//                    String refundChannel = StrUtil.isEmpty(channelMap.get(item.getRefundChannel()))?item.getRefundChannel():channelMap.get(item.getRefundChannel());


                    row.createCell(0).setCellValue(item.getOrderCode());
                    row.createCell(1).setCellValue(item.getSaleCode());
                    row.createCell(2).setCellValue(item.getAllotCode());
                    row.createCell(3).setCellValue(item.getDoCode());
                    row.createCell(4).setCellValue(item.getCustomName());
                    row.createCell(5).setCellValue(item.getOrderType());
                    row.createCell(6).setCellValue(item.getOrderStatus());
                    row.createCell(7).setCellValue(item.getRealWarehouseName());
                    if (null != item.getExpectDate()){
                        row.createCell(8).setCellValue(DateUtil.format(item.getExpectDate(), DatePattern.NORM_DATETIME_PATTERN) );
                    }
                    if (null != item.getCreateTime()){
                        row.createCell(9).setCellValue(DateUtil.format(item.getCreateTime(),DatePattern.NORM_DATETIME_PATTERN));
                    }
                }
            }
            OutputStream output = response.getOutputStream();
            response.flushBuffer();
            wb.write(output);
            output.close();
        } catch (Exception e) {
            log.error("导出退款流水信息异常", e);
            throw new RomeException("导出退款流水信息异常");
        }
    }

    /**
     * 强制生成DO
     * @param orderCode
     * @param userId
     * @return
     */
    public Response<Boolean> forceCreateDo(String orderCode, Long userId) {
        return orderRemoteService.forceCreateDo(orderCode, userId);
    }

    public void createPackDemand(OrderCreatePackDemandDTO orderCreatePackDemandDTO) {
        Response response = orderRemoteService.createPackDemand(orderCreatePackDemandDTO);
        ResponseValidateUtils.validResponse(response);
    }

}

