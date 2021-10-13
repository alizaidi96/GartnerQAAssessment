package restHelpers;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

import com.relevantcodes.extentreports.LogStatus;

import executor.Executor;
import uiHelpers.Reporting;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class APIExecutor {

	public static int responseCount = 0, currentModuleCurrentRowno = 0;
	
	public static String testDataxcelFilePath = "", executionSheetName = "", requestsFilePath = "",
			responsesFilePath = "", Node_Value = "", runTimeValues[] = null, currentModuleName = "",
			reportFilePath = "", RepName = "", RepName1 = "";
	
	public static Sheet executionSheet = null, currentModuleSheet = null;
	public static Response resp = null;
	public static ArrayList<String> nodeArrayValue = null;
	public static FunctionClass fb = null;
	public static RestFunctions restFunctions = null;
	public static XML excelConfigxml = null;
	public static XML configXml = null;

	public static HashMap<String, String> dependency = null;

	public static String executeAPI(Map<String, String> testData_API, Reporting report, boolean reportOn) {

		try {
			
			int responseCount = 0;
			System.out.println(testData_API.get("Test Case No"));
			
			HashMap<String, String> dependency = new HashMap<>();
			FunctionClass fb = new FunctionClass();
			RestFunctions restFunctions = new RestFunctions();

			ArrayList<String> nodeArrayValue = new ArrayList<String>();

			String currentTestBaseUri = "";

			nodeArrayValue.clear();

			boolean newTest = true;
			//String currentModuleName = testData_API.get("Module Name");
			currentTestBaseUri = testData_API.get("Base URI");
			String currentTestNo = testData_API.get("Test Case No");
			String currentTestName = testData_API.get("Test Case Name");
			String currentTestBasePath = testData_API.get("Base Path");
			String currentTestMethod = testData_API.get("Method");
			String currentTestParamNames = testData_API.get("Request Param Names");
			String currentTestParamValues = testData_API.get("Request Param Values");
			String currentTestNodeNames = testData_API.get("Node Names");
			String currentTestResponseCode = testData_API.get("Response Code");
			String currentHeaders = testData_API.get("Headers");
			String currentHeaderValues = testData_API.get("Header Values");


			if (newTest) {
				if (reportOn)
					report.logStepToReport(LogStatus.INFO, testData_API.get("Method").toUpperCase() + " - Making Request", "");
			}

			// Get Run Time Values if present for below

//			currentTestBasePath = runTimeTestData.GetRunTimeVariableNames(currentTestBasePath, report);
//			currentTestNodeNames = runTimeTestData.GetRunTimeVariableNames(currentTestNodeNames, report);
//			currentTestNodeValues = runTimeTestData.GetRunTimeVariableNames(currentTestNodeValues, report);
//			currentTestBody = runTimeTestData.GetRunTimeVariableNames(currentTestBody, report);
//			currentTestParamNames = runTimeTestData.GetRunTimeVariableNames(currentTestParamNames, report);
//			currentTestParamValues = runTimeTestData.GetRunTimeVariableNames(currentTestParamValues, report);
//			currentHeaderValues = runTimeTestData.GetRunTimeVariableNames(currentHeaderValues, report);

			// Set base URI, content type and base path for current request

			restFunctions.setBaseURI(currentTestBaseUri);
			restFunctions.setBasePath(currentTestBasePath);

			RequestSpecification req = null;
			
			if (currentHeaderValues != null) {
				req = fb.setHeader(currentHeaders, currentHeaderValues);
			}

			// add '#' to get last node value name

			if (!currentTestNodeNames.equals("") && currentTestNodeNames != null) {
				currentTestNodeNames = currentTestNodeNames + "#";
			}

			switch (currentTestMethod) {

			case "GET":

				// Log Base URI and Base Path in Report
				if (reportOn)
					report.info("", "URL - <font color=#990099><u><b>" + RestAssured.baseURI + RestAssured.basePath);

				// Log Parameters in report

				if (!(currentTestParamNames.equals("") || currentTestParamNames == null)) {

					String[] parameterNameArray = currentTestParamNames.split(",");
					String[] parameterValueArray = currentTestParamValues.split(",");
					String paramlist = "";

					for (int i = 0; i < parameterNameArray.length; i++) {

						if (parameterNameArray[i].trim().length() > 0) {
							paramlist = paramlist + parameterNameArray[i] + "&nbsp;&nbsp; = " + parameterValueArray[i] + "<br>";
						}
					}
					if (reportOn)
						report.info("Parameters", "<font color=#990099> <b>" + paramlist);
				}

				if (currentTestParamNames != null && !currentTestParamNames.equals("") && currentTestParamValues != null
						&& !currentTestParamValues.equals("")) {
					resp = restFunctions.getQueryResponse(currentTestParamNames, currentTestParamValues, req);
				} else {
					resp = restFunctions.getQueryResponse(req);
				}

				System.out.println("\n * * * * * * * * * Response fetched for Test - '" + currentTestName
						+ "' * * * * * * * * * ");
				System.out.println(
						"\n * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * \n");

				String currentResponseFilePath = report.responseFilePath + currentModuleName.replace(" ", "-") + "-"
						+ currentTestName.replace(" ", "-") + "-" + String.valueOf(responseCount++) + ".json";

				restFunctions.saveResponseToFile(resp, currentResponseFilePath);

				if (!(fb.responseCodeVerify(resp, Integer.parseInt(currentTestResponseCode), report))) {
					dependency.put(currentTestNo, "Fail");
				} else {
					dependency.put(currentTestNo, "Pass");
				}

				String currentResponseFileRelativePath = Executor.browser
						+ File.separator + currentResponseFilePath
						.substring(currentResponseFilePath.indexOf("Response"), currentResponseFilePath.length());

				if (reportOn)
					report.ReportSuccessResponse(currentResponseFileRelativePath);

				break;

			case "PUT":
				break;

			case "POST":
				break;

			case "DELETE":
				break;

			default:
				String message = "Method '" + currentTestMethod
						+ "' is not allowed only GET, PUT, POST & DELETE are allowed";

				System.out.println(message);
				
				if (reportOn)
					report.fail("Invalid Method Name", message);
			}
			return resp.asString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
