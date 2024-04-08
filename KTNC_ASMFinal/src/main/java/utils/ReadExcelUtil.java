package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcelUtil {
	private static String fileName = "test-case.xlsx";
	private static String testcaseId = "LOG";
	private static int sheetIndex = 0;
	private static int dataCol = 3;
	private static int expectedCol = dataCol + 1;
	private static int actualCol = dataCol + 2;
	private static int statusCol = dataCol + 3;

	public ReadExcelUtil(String fileName, String testcaseId, int sheetIndex, int dataCol) {
		ReadExcelUtil.fileName = fileName;
		ReadExcelUtil.testcaseId = testcaseId;
		ReadExcelUtil.dataCol = dataCol;
		ReadExcelUtil.expectedCol = dataCol + 1;
		ReadExcelUtil.actualCol = dataCol + 2;
		ReadExcelUtil.statusCol = dataCol + 3;

	}

	public static Object[][] getTestData(String... params) {
		try {
			FileInputStream inputStream = new FileInputStream(fileName);// create input stream with the file of
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(sheetIndex);

			List<Object[]> listData = new ArrayList<Object[]>();// tạo list chứa data sau khi xử lý
			for (int i = 0; i < 100; i++) {
				Row currentRow = sheet.getRow(i);
				String firstCellVal = currentRow.getCell(0).getStringCellValue();// lấy ô đầu tiên của hàng

				if (firstCellVal.isBlank())
					break;// nếu blank thì ngưng vòng lặp
				if (firstCellVal.contains(testcaseId)) {// nếu chứa testcaseId thì chạy
					String testCaseData = currentRow.getCell(dataCol).getStringCellValue();
					String testCaseExpectedResult = currentRow.getCell(expectedCol).getStringCellValue();
					//
					String dataCases[] = testCaseData.split("Case ");
					String expectedCases[] = testCaseExpectedResult.split("\n");
					//
					StringBuilder sb = new StringBuilder();
					int count = -1;
					for (String dataCase : dataCases) {
						count++;
						if (dataCase.isBlank())
							continue;
						sb.append(String.valueOf(i)).append(">>>khoadeptraivai");
						sb.append(firstCellVal).append(">>>khoadeptraivai");
						String lines[] = dataCase.split("\n");
						sb.append(lines[0].substring(0, lines[0].indexOf(":")).trim()).append(">>>khoadeptraivai");
						for (int j = 0; j < params.length; j++) {
							String val = lines[j + 1].substring(lines[j + 1].indexOf("=") + 1);// lấy giá trị sau dấu
																								// bằng
							sb.append(val).append(">>>khoadeptraivai");
						}
						sb.append(expectedCases[count - 1]);
						String[] aCase = sb.toString().split(">>>khoadeptraivai");
						listData.add(aCase);
						sb = new StringBuilder();
					}
				}
			}
			return convertListToObjectArray(listData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static Object[][] convertListToObjectArray(List<Object[]> list) {
		Object[][] newArray = new Object[list.size()][];
		for (int i = 0; i < list.size(); i++) {
			newArray[i] = list.get(i);
		}
		return newArray;
	}

}
