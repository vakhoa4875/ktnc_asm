package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import constant.LoginResult;
import constant.RegisterResult;

public class WriteExcelUtil {
	private static String fileName = "test-case.xlsx";
	private static String testcaseId = "LOG";
	private static int sheetIndex = 0;
	private static int dataCol = 3;
	private static int expectedCol = dataCol + 1;
	private static int actualCol = dataCol + 2;
	private static int statusCol = dataCol + 3;

	public WriteExcelUtil(String fileName, String testcaseId, int sheetIndex, int dataCol) {
		WriteExcelUtil.fileName = fileName;
		WriteExcelUtil.testcaseId = testcaseId;
		WriteExcelUtil.sheetIndex = sheetIndex;
		WriteExcelUtil.dataCol = dataCol;
		WriteExcelUtil.expectedCol = dataCol + 1;
		WriteExcelUtil.actualCol = dataCol + 2;
		WriteExcelUtil.statusCol = dataCol + 3;

	}

	public static void writeActualResult(int rowIndex, String actualResult, boolean status) {
		try {
			FileInputStream fis = new FileInputStream(new File(fileName));
			Workbook workbook = new XSSFWorkbook(fis);
			Sheet sheet = workbook.getSheetAt(sheetIndex);
			Row row = sheet.getRow(rowIndex);
			if (row == null) {
				row = sheet.createRow(rowIndex);
			}
			Cell cell = row.getCell(actualCol);
			if (cell == null) {
				cell = row.createCell(actualCol);
			}
			cell.setCellValue(actualResult);
			row.getCell(statusCol).setCellValue((status) ? "Passed" : "Failed");
			fis.close();

			FileOutputStream fos = new FileOutputStream(new File(fileName));
			workbook.write(fos);
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getOutputResult(String res) {
		return res.substring(res.indexOf("[") + 1, res.indexOf("]"));
	}

	public static void writeTestDefect(String tcID, String defect) throws IOException {
		FileInputStream fis = new FileInputStream(new File(fileName));
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheetAt(sheetIndex + 1);
		System.out.println("abc");

		String[] arr = defect.split(" expected");
		String summary = arr[0];
		String[] result = arr[1].split("found");
		String expected = getOutputResult(result[0]).equals("true") ? LoginResult.SUCCEED.getValue() : LoginResult.FAILED.getValue();
		String actual = getOutputResult(result[1]).equals("true")  ? LoginResult.SUCCEED.getValue() : LoginResult.FAILED.getValue();
		if (testcaseId.equals("REG")) {
			expected = getOutputResult(result[0]).equals("true") ? RegisterResult.SUCCEED.getValue() : RegisterResult.FAILED.getValue();
			actual = getOutputResult(result[1]).equals("true")  ? RegisterResult.SUCCEED.getValue() : RegisterResult.FAILED.getValue();			
		}
		String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        Row emptyRow = null;
        int rowNum = 1;
        for (; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                emptyRow = sheet.createRow(rowNum);
                break;
            }
            Cell defectCell = row.getCell(2);
            if (defectCell != null && defectCell.getStringCellValue().equals(defect)) {
                System.out.println("bruh");
                return;
            }
        }

        if (emptyRow == null)
            emptyRow = sheet.createRow(rowNum);

        emptyRow.createCell(0).setCellValue(tcID);
        emptyRow.createCell(1).setCellValue(summary);
        emptyRow.createCell(2).setCellValue(defect);
        emptyRow.createCell(5).setCellValue("Pending");
        emptyRow.createCell(6).setCellValue(date);
        emptyRow.createCell(11).setCellValue(expected);
        emptyRow.createCell(12).setCellValue(actual);

		fis.close();

		FileOutputStream fos = new FileOutputStream(new File(fileName));
		workbook.write(fos);
		fos.close();
	}
	

}
