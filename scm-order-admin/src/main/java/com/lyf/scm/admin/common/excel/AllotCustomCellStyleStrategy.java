package com.lyf.scm.admin.common.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 仓库调拨导出Excel自定义样式
 * <p>
 * @Author: wwh 2020/7/11
 */
@Slf4j
public class AllotCustomCellStyleStrategy extends AbstractCellStyleStrategy {
	
	Workbook workbook;
	
	@Override
	protected void initCellStyle(Workbook workbook) {
		this.workbook = workbook;
	}

	@Override
	protected void setHeadCellStyle(Cell cell, Head head, Integer relativeRowIndex) {
		Font font = workbook.createFont();
		//设置粗体
		font.setBold(false);
		//设置字体大小
		font.setFontHeightInPoints((short) 11);
		//设置字体
		font.setFontName("宋体");
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font);
		//设置换行
		cellStyle.setWrapText(true);
		//设置水平对齐的样式为居中对齐;
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		//cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
		//设置背景色时，fillPattern必须设置，否则设置背景色无效
		//cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cell.setCellStyle(cellStyle);
		//冻结第一行
		cell.getSheet().createFreezePane(0, 1, 0, 1);
	}

	@Override
	protected void setContentCellStyle(Cell cell, Head head, Integer relativeRowIndex) {
		Font font = workbook.createFont();
		//设置粗体
		font.setBold(false);
		//设置字体大小
		font.setFontHeightInPoints((short) 11);
		//设置字体
		font.setFontName("宋体");
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font);
		//设置换行
		cellStyle.setWrapText(true);
		//设置水平对齐的样式为居中对齐;
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cell.setCellStyle(cellStyle);
	}

}