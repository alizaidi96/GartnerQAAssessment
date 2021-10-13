package uiHelpers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import executor.Executor;
import restHelpers.XML;

/**
 * ExcelReader reads data in a given excel file
 */

public class ExcelReader {
	
	// path of the excel file
	private String excelFile;

	// HOLDING LIST OF TESTCASES FROM BOTH API AND UI

	List<String> testCases = new ArrayList<String>();
	Map<String, Map<String, String>> APITestData;
	Map<String, Map<String, String>> UITestData;
	Map<String, String> testCaseTo;
		
	/**
	 * @return a map of map containing all test data
	 * @throws IOException
	 */
	public ExcelReader() throws IOException {
		this.excelFile = Executor.PROJECT_PATH + File.separator + "TestData" + File.separator + "Assessment-TestData.xlsx";
		System.out.println(excelFile);
	}
	
	
	public Map<String, Map<String, String>> readAllTestDataExtracter() throws IOException {

		System.out.println("READING DATA FROM TEST DATA SHEET");
		XSSFWorkbook workbook = null;
		List<String> requiredCalls = new ArrayList<String>();
		String environment = "";

		UITestData = new HashMap<String, Map<String, String>>();
		APITestData = new HashMap<String, Map<String, String>>();

		System.out.println("Excel Configuration File Path - " + new File("").getAbsolutePath() + File.separator + "Excel-config.xml");
		XML excelConfigxml = new XML(new File("").getAbsolutePath() + File.separator + "Excel-config.xml");

		try {
			workbook = new XSSFWorkbook(excelFile);

			String testCaseNumber;
			String APIname;

			XSSFSheet sheet = workbook.getSheet("Execution_Admin");
			System.out.println("Sheet Name - Execution_Admin");
			int rowCount = sheet.getPhysicalNumberOfRows();
			System.out.println("Number of rows - " + rowCount);
			
			Map<String, String> modulesList = new HashMap<String, String>();
			
			for (int adminSheetRow = 1; adminSheetRow < rowCount; adminSheetRow++) {
				XSSFRow row = sheet.getRow(adminSheetRow);
				String module = row.getCell(getColHeaderNo(sheet, excelConfigxml.readTagVal("ModuleName")))
						.getStringCellValue();
				String execute = row.getCell(getColHeaderNo(sheet, excelConfigxml.readTagVal("ModuleExecute")))
						.getStringCellValue();
				String envValue = row.getCell(getColHeaderNo(sheet, excelConfigxml.readTagVal("Environment")))
						.getStringCellValue();
				boolean toBeExecuted = execute.equalsIgnoreCase("yes");

				if (toBeExecuted && !modulesList.containsKey(module)) {
					environment = envValue;
					modulesList.put(module, envValue);
				}
			}

			// GETTING DATA FROM UI TESTDATA
			sheet = workbook.getSheet("UI_TestData");
			System.out.println("Sheet Name - UI_TestData");
			rowCount = sheet.getPhysicalNumberOfRows();
			System.out.println("Number of rows - " + rowCount);
			for (int rowIndex = 1; rowIndex < rowCount; rowIndex++) {
				XSSFRow row = sheet.getRow(rowIndex);
				String enviornment = row.getCell(getColHeaderNo(sheet, excelConfigxml.readTagVal("UIEnvironment")))
						.getStringCellValue();
				String module = row.getCell(getColHeaderNo(sheet, excelConfigxml.readTagVal("UIModuleName")))
						.getStringCellValue();

				if (modulesList.containsKey(module)) {
					if (!enviornment.equalsIgnoreCase(modulesList.get(module)))
						continue;
				} else
					continue;

				testCaseNumber = row.getCell(getColHeaderNo(sheet, excelConfigxml.readTagVal("UITestCaseNo")))
						.getStringCellValue();

				if (row.getCell(getColHeaderNo(sheet, excelConfigxml.readTagVal("UIExecute"))).getStringCellValue()
						.equalsIgnoreCase("yes")) {
					if (!testCases.contains(testCaseNumber))
						testCases.add(testCaseNumber);

					if (enviornment.equalsIgnoreCase("test")) {
						UITestData.put(testCaseNumber, getAllTheDataForTheTestCase(row, sheet.getRow(0)));
						
						APIname = UITestData.get(testCaseNumber).get("APIName");
						if (APIname != null)
							if (APIname.contains(",")) {
								for (String api : APIname.split(",")) {

									if (!requiredCalls.contains(api.trim() + "_" + enviornment))
										requiredCalls.add(api.trim() + "_" + enviornment);
								}
							} else {
								if (!requiredCalls.contains(APIname + "_" + enviornment))
									requiredCalls.add(APIname.trim() + "_" + enviornment);
							}
					}
				}
			}

			sheet = workbook.getSheet("UIRelatedAPITestData");
			System.out.println("Sheet Name - UIRelatedAPITestData");
			rowCount = sheet.getPhysicalNumberOfRows();
			System.out.println("Number of rows - " + rowCount);
			for (int rowIndex = 1; rowIndex < rowCount; rowIndex++) {

				XSSFRow row = sheet.getRow(rowIndex);
				String enviornment = row.getCell(getColHeaderNo(sheet, excelConfigxml.readTagVal("TestEnvironment")))
						.getStringCellValue();

				testCaseNumber = row.getCell(getColHeaderNo(sheet, excelConfigxml.readTagVal("TestCaseNo")))
						.getStringCellValue();

				if (requiredCalls.contains(testCaseNumber + "_" + enviornment)) {
					if (enviornment.equalsIgnoreCase("test"))
						APITestData.put(testCaseNumber, getAllTheDataForTheTestCase(row, sheet.getRow(0)));
				}
			}

		} catch (Exception e) {
			System.out.println("Error for reading Excel + " + e.getLocalizedMessage());
			e.printStackTrace();
		}

		if(environment.equalsIgnoreCase("test"))
			return combineData(APITestData, UITestData);
		else {
			System.err.println("ERROR IN READING DATA");
			return null;
		}
			
	}
		
	/**
	 * @return a map containing all test data in one Excel row
	 * @throws IOException
	 */
	private Map<String, String> getAllTheDataForTheTestCase(XSSFRow moduleRow, XSSFRow headerRow) {

		Map<String, String> moduleToExecute = new HashMap<>();
		DataFormatter formatter = new DataFormatter(Locale.US);

		int moduleColumns = moduleRow.getLastCellNum();

		// Add the values of all columns
		for (int moduleColIndex = 0; moduleColIndex < moduleColumns; moduleColIndex++) {
			try {
				XSSFCell keyCell = headerRow.getCell(moduleColIndex);
				XSSFCell valueCell = moduleRow.getCell(moduleColIndex);

				if (keyCell != null && valueCell != null) {
					moduleToExecute.put(formatter.formatCellValue(keyCell), formatter.formatCellValue(valueCell));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return moduleToExecute;
	}
		
		
	/**
	 * @return a map of combined API and UI test data
	 * @throws IOException
	 */
	private Map<String, Map<String, String>> combineData(Map<String, Map<String, String>> APITestData,
			Map<String, Map<String, String>> UITestData) {
		Map<String, Map<String, String>> mainDetails = new HashMap<String, Map<String, String>>();
		Map<String, String> data = null;

		for (String tcName : testCases) {
			data = new HashMap<String, String>();
			if (APITestData.containsKey(tcName)) {
				for (String key : APITestData.get(tcName).keySet()) {
					data.putIfAbsent(key, APITestData.get(tcName).get(key));
				}
			}

			if (UITestData.containsKey(tcName)) {
				for (String key : UITestData.get(tcName).keySet()) {
					data.putIfAbsent(key, UITestData.get(tcName).get(key));
				}
			}
			mainDetails.put(tcName, data);
		}
		return mainDetails;
	}
		

	public int getColHeaderNo(Sheet sheet, String colName) {
		int colNo = -1;
		try {
			int totCol = getColCount(sheet, 0);
			totCol = 25;
			int currentColNo;
			
			for (currentColNo = 0; currentColNo < totCol; currentColNo++) {
				if (getCellData(sheet, 0, currentColNo).equalsIgnoreCase(colName)) {
					return currentColNo;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(
					"Unable to get column number of '" + colName + "' column in sheet '" + sheet.getSheetName() + "'");
		}
		return colNo;
	}

	public int getColCount(Sheet sheet, int rowNo) {
		return sheet.getRow(rowNo).getLastCellNum();
	}

	/**
	 * Function - Get Cell data based on row no and column no
	 * 
	 * @param sheet - Excel sheet
	 * @param rowNo - Row number of excel sheet
	 * @param colNo - Column number of excel sheet
	 */

	public String getCellData(Sheet sheet, int rowNo, int colNo) {
		
		String cellVal = "";

		Cell col = sheet.getRow(rowNo).getCell(colNo);
		cellVal = col.getStringCellValue();

		return cellVal;
	}

	public List<String> getTCList() {
		return testCases;
	}
		
	public Map<String, Map<String, String>> getUIData(String testCaseName) {

		Map<String, Map<String, String>> tempMap = new HashMap<String, Map<String, String>>();

		if (UITestData.containsKey(testCaseName))
			tempMap.put(testCaseName + "_" + "Test", UITestData.get(testCaseName));

		return tempMap;
	}		


	public Map<String, Map<String, String>> getAPIDataAccordingToEnv(String testCaseName) {

		if (testCaseName.toLowerCase().contains("_test"))
			return APITestData;
		else
			return null;
	}
}
