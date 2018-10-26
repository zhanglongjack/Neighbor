package com.neighbor.common.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public abstract class ExcelImport<T> {
	public List<T> batchImport(String fileName, MultipartFile file) throws Exception {
		List<T> infoList = new ArrayList<T>();
		Workbook wb = createWorkBookFactory(fileName, file.getInputStream());
		Sheet sheet = wb.getSheetAt(0);
		
		for (int r = 1; r <= sheet.getLastRowNum(); r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				continue;
			}
			infoList.add(buildObjectByRow(row));
		}
		file.getInputStream().close();
		return infoList;
	}

	private Workbook createWorkBookFactory(String fileName, InputStream is) throws Exception {
		boolean isExcel2003 = fileName.matches("^.+\\.(?i)(xls)$");
		boolean isExcelGraterThen2007 = fileName.matches("^.+\\.(?i)(xlsx)$");
		if (!isExcel2003 && !isExcelGraterThen2007) {
			throw new Exception("上传文件格式不正确");
		}
		return isExcel2003 ? new HSSFWorkbook(is) : new XSSFWorkbook(is);
	}

	protected abstract T buildObjectByRow(Row row);
	
	protected String conversCell(Cell cell){
		if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
			return cell.getNumericCellValue()+"";
		}
		return cell.getStringCellValue();
	}
}
