package com.maoshen.component.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;


public class ExcelImportTest {
	public static void main(String[] args) {
		File file = new File("src/test/java/com/maoshen/component/excel/bb.xls");
		readExcel(file);
	}

	// 去读Excel的方法readExcel，该方法的入口参数为一个File对象
	public static void readExcel(File file) {
		try {
			// 创建输入流，读取Excel
			InputStream is = new FileInputStream(file.getAbsolutePath());
			// jxl提供的Workbook类
			Workbook wb = Workbook.getWorkbook(is);
			// Excel的页签数量
			int sheet_size = wb.getNumberOfSheets();
			for (int index = 0; index < sheet_size; index++) {
				// 每个页签创建一个Sheet对象
				Sheet sheet = wb.getSheet(index);
				// sheet.getRows()返回该页的总行数
				for (int i = 0; i < sheet.getRows(); i++) {
					// sheet.getColumns()返回该页的总列数
					for (int j = 0; j < sheet.getColumns(); j++) {
						String cellinfo = sheet.getCell(j, i).getContents();
						System.out.println(cellinfo);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
