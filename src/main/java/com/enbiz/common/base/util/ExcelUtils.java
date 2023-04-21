package com.enbiz.common.base.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.enbiz.common.base.entity.BaseCommonEntity;
import com.enbiz.common.base.exception.CommonException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelUtils {
	private static int rowNum = 0;

	private ExcelUtils() {
		throw new UnsupportedOperationException();
	}

	public static List<? extends BaseCommonEntity> extractCSVList(MultipartFile csvFile, String[] columns,
			Class<? extends BaseCommonEntity> type) {
		String line = null;
		List<BaseCommonEntity> result = new ArrayList<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream(), "KSC5601"));

			int cnt = 0;
			while ((line = reader.readLine()) != null) {
				if (cnt++ == 0) {
					continue;
				}
				Map<String, String> csvDataMap = new HashMap<>();
				String[] values = line.split(",");
				int colCount = values.length;
				for (int col = 0; col < colCount; col++) {
					csvDataMap.put(columns[col].trim(), values[col]);
				}

				if (!csvDataMap.isEmpty()) {
					BaseCommonEntity instance = createAndPopulateEntity(type, csvDataMap);
					result.add(instance);
				}
			}
		} catch (Exception e) {
			throw new CommonException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new CommonException(e);
				}
			}
		}
		return result;
	}

	private static BaseCommonEntity createAndPopulateEntity(Class<? extends BaseCommonEntity> type,
			Map<String, String> csvDataMap) {
		try {
			BaseCommonEntity instance = type.getDeclaredConstructor().newInstance();
			BeanUtils.populate(instance, csvDataMap);
			instance.setState("created");
			return instance;
		} catch (Exception e) {
			throw new CommonException(e);
		}
	}

	public static List<? extends BaseCommonEntity> extractExcelList(MultipartFile file, String[] columns,
			Class<? extends BaseCommonEntity> type) {
		List<BaseCommonEntity> result = new ArrayList<>();

		Workbook workbook = null;
		InputStream inputStream = null;
		try {
			inputStream = file.getInputStream();
			workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();

			for (int rowIndex = 1; rowIndex < rows; rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				Map<String, String> excelDataMap = getExcelDataMap(columns, row);

				if (!excelDataMap.isEmpty()) {
					BaseCommonEntity instance = createAndPopulateEntity(type, excelDataMap);
					result.add(instance);
				}
			}

			return result;
		} catch (Exception e) {
			throw new CommonException(e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				log.trace(e.getMessage(), e);
			}
		}
	}

	private static Map<String, String> getExcelDataMap(String[] columns, Row row) {
		Map<String, String> excelDataMap = new HashMap<>();
		for (int col = 0; col < columns.length; col++) {
			Cell columnValue = row.getCell(col);
			if (columnValue != null && !columnValue.toString().isEmpty()) {
				excelDataMap.put(columns[col].trim(), getCellValue(row.getCell(col)));
			}
		}
		return excelDataMap;
	}

	private static String getCellValue(Cell cell) {
		int cellType = cell.getCellType();
		String value;
		switch (cellType) {
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				value = new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
			} else {
				value = String.valueOf((long) cell.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_STRING:
		case Cell.CELL_TYPE_BLANK:
		case Cell.CELL_TYPE_BOOLEAN:
		case Cell.CELL_TYPE_ERROR:
		case Cell.CELL_TYPE_FORMULA:
		default:
			value = cell.toString();
			break;
		}

		return value;
	}

	// File로 만들 경우
	public static void createExcelToFile(List<Map<String, Object>> datas, String filepath)
			throws FileNotFoundException, IOException {
		// workbook = new HSSFWorkbook(); // 엑셀 97 ~ 2003
		// workbook = new XSSFWorkbook(); // 엑셀 2007 버전 이상

		Workbook workbook = new SXSSFWorkbook(); // 성능 개선 버전
		Sheet sheet = workbook.createSheet("데이터");

		rowNum = 0;

		createExcel(sheet, datas);

		try (FileOutputStream fos = new FileOutputStream(new File(filepath))) {
			workbook.write(fos);
			// workbook.close();
		}

	}

	// File로 만들 경우
	@SuppressWarnings("rawtypes")
	public static void createExcelToFile(List<Map<String, Object>> datas, List titles, String filepath)
			throws FileNotFoundException, IOException {
		// workbook = new HSSFWorkbook(); // 엑셀 97 ~ 2003
		// workbook = new XSSFWorkbook(); // 엑셀 2007 버전 이상

		Workbook workbook = new SXSSFWorkbook(); // 성능 개선 버전
		Sheet sheet = workbook.createSheet("데이터");

		rowNum = 0;

		createExcel(sheet, datas);

		try (FileOutputStream fos = new FileOutputStream(new File(filepath))) {
			workbook.write(fos);
			// workbook.close();
		}
	}

	// HttpServletResponse 경우 ()
	public static void createExcelToResponse(List<Map<String, Object>> datas, List<Map<String, Object>> titles,
			String filename, String sheetName, HttpServletResponse response) throws IOException {

		Workbook workbook = new SXSSFWorkbook(); // 성능 개선 버전
		Sheet sheet = workbook.createSheet(sheetName);

		createExcel(sheet, titles, datas);

		// 컨텐츠 타입과 파일명 지정
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", String.format("attachment;filename=%s.xlsx", filename));

		workbook.write(response.getOutputStream());
		// workbook.close();

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ByteArrayInputStream createExcelToByteArrayInputStream(List<Map<String, Object>> datas, List titles,
			String filename, String sheetName) throws IOException {

		Workbook workbook = new SXSSFWorkbook(); // 성능 개선 버전
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		Sheet sheet = workbook.createSheet(sheetName);

		rowNum = 0;

		createExcel(sheet, titles, datas);

		workbook.write(out);

		return new ByteArrayInputStream(out.toByteArray());
	}

	// 엑셀 생성
	private static void createExcel(Sheet sheet, List<Map<String, Object>> datas) {

		// 데이터를 한개씩 조회해서 한개의 행으로 만든다.
		for (Map<String, Object> data : datas) {
			// row 생성
			Row row = sheet.createRow(rowNum++);
			int cellNum = 0;

			// map에 있는 데이터를 한개씩 조회해서 열을 생성한다.
			for (String key : data.keySet()) {
				// cell 생성
				Cell cell = row.createCell(cellNum++);

				// cell에 데이터 삽입
				cell.setCellValue(data.get(key).toString());
			}
		}
	}

	// 엑셀 생성
	public static void createExcel(Sheet sheet, List<Map<String, Object>> titles, List<Map<String, Object>> datas) {
		int rowNum = 0;

		// title 생성
		Row row = sheet.createRow(rowNum++);
		int cellNum = 0;

		for (Map<String, Object> title : titles) {
			// cell 생성
			Cell cell = row.createCell(cellNum++);

			// cell에 데이터 삽입
			cell.setCellValue(title.get("headerText").toString());
		}

		// 데이터를 한개씩 조회해서 한개의 행으로 만든다.
		for (Map<String, Object> data : datas) {
			// row 생성
			row = sheet.createRow(rowNum++);

			cellNum = 0;

			// map에 있는 데이터를 한개씩 조회해서 열을 생성한다.
			for (String key : data.keySet()) {
				boolean columnCheck = false;
				// key값이 title에 있는 경우만 컬럼 저장
				for (Map<String, Object> title : titles) {
					if (title.get("columns").toString().equals(key)) { // 해당 컬럼이 타이틀에 존재하는 경우
						columnCheck = true;
						break;
					}
				}

				if (columnCheck) {
					// cell 생성
					Cell cell = row.createCell(cellNum++);

					// cell에 데이터 삽입
					String val = "";
					if (data.get(key) != null) {
						val = data.get(key).toString();
					}
					cell.setCellValue(val);
				}

			}
		}

	}

}
