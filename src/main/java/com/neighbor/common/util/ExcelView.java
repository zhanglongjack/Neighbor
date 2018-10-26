package com.neighbor.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Excel视图
 *
 * 支持多个Sheet, Sheet名称、标题和表头不是必须的 AbstractPdfView和AbstractXlsxView原理大致相同
 * 
 * @author mengday zhang
 */
public class ExcelView extends AbstractXlsxView {
	private static final Logger logger = LoggerFactory.getLogger(ExcelView.class);

	@Override
	protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ExcelMappingsAbstract mapping = (ExcelMappingsAbstract) map.get("ExcelMappings");
		logger.info("buildExcelDocument request:" + mapping.getSheetName());
		responseContextBuild(response, mapping);

		Sheet sheet = workbook.createSheet(mapping.getSheetName());
		CellStyle headerStyle = buildHeaderStyle(workbook, sheet);
		CellStyle rowStyle = buildRowStyle(workbook, sheet);
		
		buildHeaderColumns(mapping, sheet.createRow(0), headerStyle);

		buildRowColumns(mapping, sheet,rowStyle);
	}

	private CellStyle buildRowStyle(Workbook workbook, Sheet sheet) {
		CellStyle rowStyle = workbook.createCellStyle();
		DataFormat format = workbook.createDataFormat();
		rowStyle.setDataFormat(format.getFormat("@"));
		return rowStyle;
	}

	private void responseContextBuild(HttpServletResponse response, ExcelMappingsAbstract mapping)
			throws UnsupportedEncodingException {
		response.setHeader("content-disposition",
				"attachment;filename=" + URLEncoder.encode(mapping.getSheetName() + ".xlsx", "utf-8"));
		response.setContentType("application/ms-excel; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
	}

	private CellStyle buildHeaderStyle(Workbook workbook, Sheet sheet) {
		logger.info("build sheet:" + sheet);
		sheet.setDefaultColumnWidth(30);
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("黑体");
		style.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		font.setBold(true);
		font.setColor(HSSFColor.BLACK.index);
		style.setFont(font);
		return style;
	}

	
	private void buildRowColumns(ExcelMappingsAbstract mapping, Sheet sheet, CellStyle style) {
		List<String> columnsMapping = mapping.getColumnsMappings();
		JSONArray jsonObj = mapping.getData();
		logger.info("build row:" + jsonObj);
		   
		   
		for (int i = 0; i < jsonObj.size(); i++) {
			Row userRow = sheet.createRow(i + 1);
			JSONObject info = (JSONObject) jsonObj.get(i);
			for (int j = 0; j < columnsMapping.size(); j++) {
				String value = mapping.getColumnsMappingValue(columnsMapping.get(j),info.get(columnsMapping.get(j))+"");
				Cell cell = userRow.createCell(j);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(style);
				cell.setCellValue(value);
			}
		}
	}

	private void buildHeaderColumns(ExcelMappingsAbstract mapping, Row header, CellStyle style) {
		List<String> headerList = mapping.getHeaderList();
		logger.info("build header:" + headerList);
		for (int i = 0; i < headerList.size(); i++) {
			header.createCell(i).setCellValue(headerList.get(i));
			header.getCell(i).setCellStyle(style);
		}
	}

}