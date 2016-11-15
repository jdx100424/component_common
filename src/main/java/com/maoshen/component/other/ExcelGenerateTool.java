package com.maoshen.component.other;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * excel文件生成工具
 * 
 * @author tim
 *
 */
public class ExcelGenerateTool {

	/**
	 * excel工作文件
	 */
	private Workbook wb;

	/**
	 * 日期样式
	 */
	private CellStyle dateStyle;
	private CellStyle intStyle;
	private CellStyle floatStyle;
	private CellStyle textStyle;

	/**
	 * 当前sheet
	 */
	@Deprecated
	private Sheet currentSheet;

	@Deprecated
	private AtomicInteger rowIndex;

	private ExcelGenerateTool() {
		wb = new HSSFWorkbook();

		Font f = wb.createFont();
		f.setFontHeightInPoints((short) 12);
		f.setFontName("仿宋");

		// 文本样式
		textStyle = wb.createCellStyle();
		textStyle.setFont(f);
		textStyle.setShrinkToFit(true);
		textStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		textStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
		textStyle.setAlignment(CellStyle.ALIGN_LEFT);

		// 日期样式
		dateStyle = wb.createCellStyle();
		dateStyle.setFont(f);
		dateStyle.setShrinkToFit(true);
		dateStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		dateStyle.setDataFormat(wb.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss"));
		textStyle.setAlignment(CellStyle.ALIGN_LEFT);
		// 整数样式
		intStyle = wb.createCellStyle();
		intStyle.setFont(f);
		intStyle.setShrinkToFit(true);
		intStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		intStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
		intStyle.setAlignment(CellStyle.ALIGN_LEFT);

		// 小数样式
		floatStyle = wb.createCellStyle();
		floatStyle.setFont(f);
		floatStyle.setShrinkToFit(true);
		floatStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		floatStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
		floatStyle.setAlignment(CellStyle.ALIGN_LEFT);
	}

	public static ExcelGenerateTool createExcel() {
		return new ExcelGenerateTool();
	}

	public ExcelSheet createSheet(String sheetName) {
		if (wb == null) {
			throw new RuntimeException("should invoke createExcel() first when build sheet!");
		}
		return new ExcelSheet(wb, sheetName);
	}
	@Deprecated
	public ExcelGenerateTool addSheet(String sheetName) {
		if (wb == null) {
			throw new RuntimeException("should invoke createExcel() first when build sheet!");
		}
		currentSheet = wb.createSheet(sheetName);
		rowIndex = new AtomicInteger(0);
		return this;
	}

	@Deprecated
	public ExcelGenerateTool addRow(Object... data) {
		if (currentSheet == null) {
			throw new RuntimeException("should invoke addSheet() first when build sheet!");
		}
		Row row = currentSheet.createRow(rowIndex.getAndIncrement());
		for (int i = 0; i < data.length; i++) {
			Object d = data[i];

			if (d == null) {
				row.createCell(i, Cell.CELL_TYPE_BLANK);
				continue;
			}
			Class<? extends Object> clazz = d.getClass();
			if (clazz.isAssignableFrom(Integer.class) || clazz.isAssignableFrom(Long.class)) {
				Cell c = row.createCell(i, Cell.CELL_TYPE_NUMERIC);
				c.setCellValue(NumberUtils.toDouble(d.toString()));
				c.setCellStyle(intStyle);
			} else if (clazz.isAssignableFrom(Float.class) || clazz.isAssignableFrom(Double.class)) {
				Cell c = row.createCell(i, Cell.CELL_TYPE_NUMERIC);
				c.setCellValue(NumberUtils.toDouble(d.toString()));
				c.setCellStyle(floatStyle);
			} else if (clazz.isAssignableFrom(Date.class)) {
				Cell c = row.createCell(i, Cell.CELL_TYPE_STRING);
				c.setCellValue((Date) d);
				c.setCellStyle(dateStyle);
			} else {
				Cell c = row.createCell(i, Cell.CELL_TYPE_STRING);
				c.setCellValue(String.valueOf(d));
				c.setCellStyle(textStyle);
			}
		}

		return this;
	}

	public void write(String filePath) throws FileNotFoundException, IOException {
		write(new File(filePath));
	}

	public void write(File file) throws FileNotFoundException, IOException {
		wb.write(new FileOutputStream(file));
	}

	public void write(OutputStream os) throws IOException {
		wb.write(os);
		os.close();
	}

	public class ExcelSheet {

		private Sheet sheet;

		private AtomicInteger rowIndex;

		private ExcelSheet() {
		}

		private ExcelSheet(Workbook wb, String sheetName) {
			this.sheet = wb.createSheet(sheetName);
			this.rowIndex = new AtomicInteger(0);
		}

		public ExcelSheet addRow(Object... data) {
			if (sheet == null) {
				throw new RuntimeException("should invoke addSheet() first when build sheet!");
			}
			Row row = sheet.createRow(rowIndex.getAndIncrement());
			for (int i = 0; i < data.length; i++) {
				Object d = data[i];

				if (d == null) {
					row.createCell(i, Cell.CELL_TYPE_BLANK);
					continue;
				}
				Class<? extends Object> clazz = d.getClass();
				if (clazz.isAssignableFrom(Integer.class) || clazz.isAssignableFrom(Long.class)) {
					Cell c = row.createCell(i, Cell.CELL_TYPE_NUMERIC);
					c.setCellValue(NumberUtils.toDouble(d.toString()));
					c.setCellStyle(intStyle);
				} else if (clazz.isAssignableFrom(Float.class) || clazz.isAssignableFrom(Double.class)) {
					Cell c = row.createCell(i, Cell.CELL_TYPE_NUMERIC);
					c.setCellValue(NumberUtils.toDouble(d.toString()));
					c.setCellStyle(floatStyle);
				} else if (clazz.isAssignableFrom(Date.class)) {
					Cell c = row.createCell(i, Cell.CELL_TYPE_STRING);
					c.setCellValue((Date) d);
					c.setCellStyle(dateStyle);
				} else {
					Cell c = row.createCell(i, Cell.CELL_TYPE_STRING);
					c.setCellValue(String.valueOf(d));
					c.setCellStyle(textStyle);
				}
			}

			return this;
		}

		public ExcelSheet mergeCell(int firstRow, int firstCol, int lastRow, int lastCol) {
			this.sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
			return this;
		}

		public int getCurrentRow() {
			return rowIndex.get();
		}

		public ExcelSheet setCurrentRow(int row) {
			rowIndex.set(row);
			return this;
		}

	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		ExcelGenerateTool createExcel = createExcel();
		createExcel.createSheet("abc").mergeCell(0, 0, 1, 4).addRow("导出数据文件名").setCurrentRow(2)
				.addRow("姓名", "年龄", "生日", "身高").addRow("Tim", 32, new Date(), 172.3);
		createExcel.createSheet("bcd").addRow("姓名", "年龄", "生日", "身高").addRow("Tim", 32, new Date(), 172.3);
		createExcel.write("/tmp/ac.xls");

	}

}
