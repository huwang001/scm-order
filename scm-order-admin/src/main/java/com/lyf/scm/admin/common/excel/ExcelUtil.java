package com.lyf.scm.admin.common.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    /**
     * 模型解析监听器 -- 每解析一行会回调invoke()方法，整个excel解析结束会执行doAfterAllAnalysed()方法
     */
    private static class ModelExcelListener<E> extends AnalysisEventListener<E> {

        /**
         * 自定义用于暂时存储data
         * 可以通过实例获取该值
         */
        private List<E> dataList = new ArrayList<E>();

        /**
         * 每解析一行都会回调invoke()方法
         *
         * @param object
         * @param context
         */
        @Override
        public void invoke(E object, AnalysisContext context) {
            dataList.add(object);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            //解析结束销毁不用的资源
            //注意不要调用datas.clear(),否则getDatas为null
        }

        public List<E> getDataList() {
            return dataList;
        }

        @SuppressWarnings("unused")
        public void setDataList(List<E> dataList) {
            this.dataList = dataList;
        }
    }

    /**
     * 使用 模型 来读取Excel
     *
     * @param inputStream Excel的输入流
     * @param clazz       模型的类
     * @return 返回 模型 的列表
     */
    public static <E> List<E> readExcel(InputStream inputStream, Class<E> clazz) {
        // 解析每行结果在listener中处理
        ModelExcelListener<E> listener = new ModelExcelListener<E>();
        //这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(inputStream, clazz, listener).sheet().doRead();
        return listener.getDataList();
    }

    /**
     * 导出 Excel
     *
     * @param response  HttpServletResponse
     * @param list      数据 list
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param clazz     模型的类
     */
    public static void writeExcel(HttpServletResponse response, List<?> list, String fileName, String sheetName, Class<?> clazz) throws Exception {
    	EasyExcel.write(getOutputStream(fileName, response), clazz)
                .sheet(sheetName)
                .doWrite(list);
    }
    
    /**
     * 导出 自定义Excel
     * 
     * @param response  HttpServletResponse
     * @param list      数据 list
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param clazz     模型的类
     * @param abstractCellStyleStrategyArr	
     */
    public static void customCellWriteExcel(HttpServletResponse response, List<?> list, String fileName, String sheetName, Class<?> clazz, AbstractCellStyleStrategy... abstractCellStyleStrategyArr) throws Exception {
    	ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(getOutputStream(fileName, response), clazz);
    	for (AbstractCellStyleStrategy acs : abstractCellStyleStrategyArr) {
    		excelWriterBuilder.registerWriteHandler(acs);
		}
    	excelWriterBuilder.sheet(sheetName).doWrite(list);
    }

    /**
     * 导出文件时为Writer生成OutputStream
     *
     * @param fileName
     * @param response
     * @return
     */
    private static OutputStream getOutputStream(String fileName, HttpServletResponse response) throws Exception {
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "no-store");
            response.addHeader("Cache-Control", "max-age=0");
            return response.getOutputStream();
        } catch (IOException e) {
            throw new Exception("导出excel表格失败!", e);
        }
    }

}