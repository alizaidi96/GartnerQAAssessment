package executor;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.LogStatus;

import uiHelpers.Drivers;
import uiHelpers.ExcelReader;
import uiHelpers.Reporting;

public class Controller {
	
	public WebDriver driver;
	public Reporting report;
	String BrowserIP;
	String browserName;
	String statusValue = "";
	String reportDuration;
	
	LogStatus testStatus;
	Map<String,Map<String,String>> testList;
	ArrayList<String> testcases = new ArrayList<>();
	public String printstatus = "";
	public boolean overallstatus = false;
	public String errorMessage = "";
	public long executionStartTime;
	public long executionEndTime;
	public String executionduration = "";

	public WebDriver getDriver() throws InterruptedException {
		Drivers utilObj = new Drivers();
		driver = utilObj.OpenBrowser();
		return driver;
	}
	
	public void controllerMethod() throws Exception {
		
		ExcelReader testDataReader = new ExcelReader();

		report = new Reporting(Executor.browser);
		executionStartTime = System.currentTimeMillis();
		
		// Reading test data from TestData.xls file
		testList = testDataReader.readAllTestDataExtracter();
		Map<String, Map<String, String>> testCaseDataList_UI;

		Map<String, String> testCaseData = null;
		for (String testCaseName : testDataReader.getTCList()) {
			testCaseDataList_UI = testDataReader.getUIData(testCaseName);
			for(String testcaseNameWithENV:testCaseDataList_UI.keySet()) 
			{
				testCaseData = testCaseDataList_UI.get(testcaseNameWithENV);
				System.out.println(testcaseNameWithENV);
				executeTestCode(testCaseData, testDataReader.getAPIDataAccordingToEnv(testcaseNameWithENV), report);
			}
		}
		
		//Executor.SummaryReport.endTest(report.SummarytestWrapper);
        //Executor.SummaryReport.flush();
        executionEndTime = System.currentTimeMillis();
        
        reportDuration = getExecutionDuration(executionEndTime - executionStartTime);
        System.out.println("Total Execution Time: " + reportDuration);
        
        printstatus = overAllStatus();
        System.out.println(printstatus);
	}

	/**
	 * RETURNS DATA OF ONLY THOSE API's WHOSE NAME IS MENTIONED IN APIName Column of
	 * TestcaseIn UI sheet
	 * 
	 * @param string
	 * @param testData_API
	 * @return
	 */
	private void executeTestCode(Map<String, String> testCase, Map<String, Map<String, String>> testData_API, Reporting report) {

		Tests FactMethodObj = new Tests();
		String testDescription = testCase.get("TestCaseName");
		
		System.out.println("Test case execution start on "
				+ Executor.browser + " with test case description " + testDescription);

		try {

			driver = getDriver();
			driver.manage().deleteAllCookies();
			report.assignDriverToReport(driver);
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

			// Identify Module and Test Cases

			TestInterface TestCaseSelectionObj = FactMethodObj.testModulesSelection(testCase.get("TestCaseNo"));

			report.startTest(testDescription, Executor.browser, testCase.get("Environment"));
		
			TestCaseSelectionObj.testCasesSelection(testCase, report, this.driver, testData_API);
			
			testStatus = report.endTestCase();
			
			if(testStatus == LogStatus.FAIL)
				testcases.add(testCase.get("TestCaseName"));
			
			System.out.println("Test Run Status - " + testStatus.toString().toUpperCase());

            //report.SummarytestWrapper.appendChild(report.test);
            //Executor.SummaryReport.endTest(report.test);
            //Executor.SummaryReport.flush();

			driver.quit();

		} catch (Exception e) {
			System.err.println("Exception in test execution - " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public String overAllStatus() throws ParseException {
		
		String finalStatus = "*********************************************************************\n";
		
		int pass = testList.size() - testcases.size();
		
		finalStatus = finalStatus.concat("BROWSER NAME = " + getBrowserName() + "\n");
		finalStatus = finalStatus.concat("TOTAL NUMBER OF TESTS EXECUTED = " + testList.size() + "\n");
		
		if (testcases.size() == 0) {
			statusValue = "PASS";
		} else {
			statusValue = "FAIL";
		}
		
		finalStatus = finalStatus.concat("PASSED = " + pass + "\n");
		finalStatus = finalStatus.concat("FAILED = " + testcases.size() + "\n");
		
		return finalStatus;
		
	}
	
	private String getBrowserName() {
		browserName = Executor.browser;
		return browserName;	
	}
	
	private String getExecutionDuration(long duration) {
		
		String timeTaken = String.format("%02d:%02d:%02d", 
				TimeUnit.MILLISECONDS.toHours(duration),
				TimeUnit.MILLISECONDS.toMinutes(duration) -  
				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
				TimeUnit.MILLISECONDS.toSeconds(duration) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
		
		return timeTaken;
	}
}
