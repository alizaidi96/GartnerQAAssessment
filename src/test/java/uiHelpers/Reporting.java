package uiHelpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import executor.Executor;
import restHelpers.FunctionClass;

public class Reporting {
	
	public String folderName, screenshotsFolder, folder, folderpath, responseFilePath, parentPath, uiReportPath;
	public WebDriver driver;
	public ExtentReports extentReports;
	public ExtentTest test;
	public ExtentTest SummarytestWrapper;
	public int stepCounter;

	Date date = new Date();

	public String imagePath = "";

	DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy__hh_mm_ss_SSaa");
	public String runtimeValuefilePath = "";

	public Reporting() {

	}

	public Reporting(String browserCode) throws Exception, InterruptedException {
		createFolder(browserCode, Executor.uniqueID);
		
		parentPath = "UIReport" + File.separator;
	}

	public void startTest(String testName, String browserName, String environment) {
		try {
			
			String pathToCopyImage, pathToIMDBLogo, pathToScreenshotImage, extentReportPath;
			
			extentReportPath = Executor.PROJECT_PATH + File.separator + "Reports" + File.separator
					+ Executor.uniqueID + File.separator + "UIReport";
			extentReportPath = extentReportPath + File.separator
					+ Executor.browser + "_Report.html";
			uiReportPath = extentReportPath;
			
			pathToScreenshotImage = Executor.PROJECT_PATH + File.separator + "src" + File.separator
					+ "main" + File.separator + "resources" + File.separator + "camera.png";
			pathToCopyImage = Executor.PROJECT_PATH + File.separator + "Reports" + File.separator
					+ Executor.uniqueID + File.separator + "UIReport";			
			
			pathToIMDBLogo = Executor.PROJECT_PATH + File.separator + "src" + File.separator
					+ "main" + File.separator + "resources" + File.separator + "IMDB_Logo_2016.svg";
			try {
				FileUtils.copyFile(new File(pathToScreenshotImage), new File(pathToCopyImage + File.separator + "camera.png"));
				FileUtils.copyFile(new File(pathToIMDBLogo), new File(pathToCopyImage + File.separator + "IMDBLogo.svg"));
			} catch (Exception e) {
				e.printStackTrace();
			}

			extentReports = new ExtentReports(extentReportPath, false);
			extentReports.loadConfig(new File("extent-config2.xml"));
			
			String stepName = "<font face='Verdana' color=#805500> <bold>" + testName + "</bold> </font>";	
			test = extentReports.startTest(stepName);
			stepCounter = 0;

			try {
				File file = new File(runtimeValuefilePath);
				if (file.exists())
					file.delete();
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void assignDriverToReport(WebDriver driver) {
		this.driver = driver;
	}

	/** Create Reporting and Screenshot folders */

	public String createFolder(String browserName, String Uniqueid) {
		folderName = "";

		try {

			// Check if Reports folder is created; if not create it
			File file = new File(Executor.PROJECT_PATH + File.separator + "Reports");

			if (!file.exists()) {
				if (file.mkdir()) {
				}
			}

			folderName = Executor.PROJECT_PATH + File.separator + "Reports" + File.separator + Uniqueid + File.separator
					+ "UIReport";
			
			file = new File(folderName);

			if (!file.exists()) {
				if (file.mkdir()) {
				}
			}

			// Create browser folder to hold browser-specific data if running for multiple browsers
			folderName = folderName + File.separator + browserName;

			file = new File(folderName);

			if (!file.exists()) {
				if (file.mkdir()) {
				}
			}
			
			// Creating a folder to store screenshots
			screenshotsFolder = folderName;

		} catch (Exception e) {
			e.printStackTrace();
		}

		setFolderpath(folderName);

		responseFilePath = folderpath + File.separator + "Responses" + File.separator;
		File responseFolder = new File(responseFilePath);
		if (!responseFolder.exists()) {
			if (responseFolder.mkdir()) {
			}
		}
		runtimeValuefilePath = folderName + File.separator + "ValueLogs.txt";
		try {
			new File(runtimeValuefilePath).createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setfolder(Uniqueid);
		return folderName;
	}

	/** Set Reporting Folder Path */

	public void setFolderpath(String folderpath) {
		this.folderpath = folderpath;
		imagePath = folderpath + File.separator + "ScreenShots" + File.separator;
		File imgFolder = new File(imagePath);
		if (!imgFolder.exists()) {
			if (imgFolder.mkdir()) {
			}
		}

	}

	/** Set Reporting Folder */

	public void setfolder(String foldername) {
		this.folder = foldername;
	}

	/** Generate Current Date Number which can be used to create Reporting Folder */

	public static String generateCurrentDateNumber() {
		Date dNow = new Date();
		SimpleDateFormat sFrmt = new SimpleDateFormat("ddMMMyyyy_hhmmss_SSaa");
		String folderName = sFrmt.format(dNow);

		return folderName;
	}

	/** Get screenshot from Screenshots folder */

	public String getscreenshot(String fileName) {
		DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy__hh_mm_ss_SSaa");
		Date date = new Date();
		String screenshotfile = dateFormat.format(date) + ".png";

		try {
			FileUtils.copyFile(((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE),
					new File(fileName + screenshotfile));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return Executor.browser + "/ScreenShots/"
				+ screenshotfile;
	}

	/** Log step into the extent report */

	public void logStepToReport(LogStatus status, String Details, String filenamePath) {
		if (!filenamePath.equalsIgnoreCase("")) {

			try {
				test.log(status, "<font color=000000> Step " + ++stepCounter,
						"<div align='left' style='float:left;height:100%; width:100%'>" + Details + "<a "
								+ NewWindowPopUpHTMLCode() + " target='_blank' href=" + getscreenshot(imagePath)
								+ " style='float:right'>" + "<img src='camera.png' alt='Screenshot' height='30' width='30'></a></div>");

			} catch (Exception e) {
				test.log(status, "<font color=000000> Step " + ++stepCounter,
						Details + "<div align='right' style='float:right'>Unable to take screenshot</div>");
			}

		} else {
			test.log(status, "Step "+ ++stepCounter, Details);
		}

		extentReports.flush();
	}

	/** Assert Expected condition with the Actual Condition */

	public void assertThat(boolean status, String passMessage, String failMessage) {

		if (!status) {

			String line2 = " Expected - " + passMessage + "<br> <b> <font color='red'> Actual - " + failMessage;

			test.log(LogStatus.FAIL, "<font color=800080> <b> Verification Point",
					line2 + "<div align='right' style='float:right'><a " + NewWindowPopUpHTMLCode()
							+ " target='_blank' href=" + getscreenshot(imagePath) + " style='float:right'>" 
							+ "<img src='UIReport/camera.png' alt='Screenshot' height='30' width='30'></a></div>");

		} else {

			String line2 = " Expected - " + passMessage + "<br> <b> <font color='green'> Actual - " + passMessage;

			test.log(LogStatus.PASS, "<font color=800080> <b> Verification Point",
					line2 + "<div align='right' style='float:right'><a " + NewWindowPopUpHTMLCode()
							+ " target='_blank' href=" + getscreenshot(imagePath) + " style='float:right'>" 
							+ "<img src='UIReport/camera.png' alt='Screenshot' height='30' width='30'></a></div>");

		}

		extentReports.flush();

	}

	/** End Test Cases */
	
	public LogStatus endTestCase() {
		extentReports.flush();
		extentReports.endTest(test);
		extentReports.flush();
		return test.getTest().getStatus();
	}
	
	public String getDuration() {
		
		
		long duration = test.getEndedTime().getTime() - test.getStartedTime().getTime();
		
		String result = String.format("%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(duration),
				TimeUnit.MILLISECONDS.toMinutes(duration) -
				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
				TimeUnit.MILLISECONDS.toSeconds(duration) -
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
		
		return result;
	}

	/** Copy File */

	public static void copyFile(String fromLocation, String toLocation) throws Exception {

		FileInputStream instream = null;
		FileOutputStream outstream = null;

		try {

			File infile = new File(fromLocation);
			File outfile = new File(toLocation);
			instream = new FileInputStream(infile);
			outstream = new FileOutputStream(outfile);
			byte[] buffer = new byte[1024];

			int length;

			// Copy contents from input stream to output stream using read and write methods

			while ((length = instream.read(buffer)) > 0) {
				outstream.write(buffer, 0, length);
			}

			// Close input / output file streams

			instream.close();
			outstream.close();

		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
	}

	/** Generate attribute tag to open mentioned file path in new pop up window */

	public String NewWindowPopUpHTMLCode() {
		return "onclick = \"window.open(this.href,'newwindow', 'width="
				+ Executor.propReader.getProperty("ReportPopUpWindowWidth") + ",height="
				+ Executor.propReader.getProperty("ReportPopUpWindowHeight") + "');return false;\"";
	}

	public void logGalenStepToReport(LogStatus status, String stepname, String Details, String filenamePath) {
		if (!filenamePath.equalsIgnoreCase("")) {
			try {
				test.log(status, "<font color=#800080> <b> " + stepname,
						Details + "<div align='right' style='float:right'><a " + NewWindowPopUpHTMLCode()
								+ " target='_blank' href=" + filenamePath + ">Galen Report</a></div>");
				extentReports.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			test.log(status, stepname, Details);
		}

		extentReports.flush();
	}

	public void assignCategory(String category) {
		try {
			test.assignCategory(category);
			extentReports.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startTest1(String testName) {
		try {

			ExtentTest newTest = extentReports
					.startTest("<font face='Verdana' color=#805500> <bold>" + testName + "</bold> </font>");
			test = newTest;
			extentReports.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void logVisualStepToReport(LogStatus status, String stepname, String Details) {

		test.log(status, stepname, Details);
		extentReports.flush();
	}

	public ExtentTest getCurrentExtentTest() {
		return this.test;
	}

	// ----------------FOR
	// API---------------------------------------------------------------------
	public void info(String StepName, String Description) {

		StepName = "<font color='#000000'>" + StepName;
		test.log(LogStatus.INFO, "Step "+ ++stepCounter, StepName+"<br />"+Description);
		extentReports.flush();
	}

	public void ReportSuccessResponse(String currentResponseFileRelativePath) {

		try {
			pass("Response File Path",
					"Response is stored successfully" + "<div align='right' style='float:right'><a "
							+ NewWindowPopUpHTMLCode() + " target='_blank' href=" + currentResponseFileRelativePath
							+ ">Response Json</a></div>");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void pass(String StepName, String Description) {

		StepName = "<font color='#000000'>" + StepName;

		test.log(LogStatus.PASS, "Step "+ ++stepCounter, "<b>"+StepName+"<br /><font color='Green'>"+Description);

		extentReports.flush();
	}

	public void ReportFailedResponse(String currentResponseFileRelativePath) {

		try {
			fail("Response File Path",
					"Response failed in verification" + "<div align='right' style='float:right'><a "
							+ NewWindowPopUpHTMLCode() + " target='_blank' href=" + currentResponseFileRelativePath
							+ ">Response Json</a></div>");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void fail(String StepName, String Description) {

		StepName = "<font color='#000000'>" + StepName;
		test.log(LogStatus.FAIL, "Step "+ ++stepCounter, "<b>"+ StepName+"<br /><font color='red'>"+Description);
		extentReports.flush();
	}

	/**
	 * FUNCTION - Verify & log step when expected & actual are in string format
	 * 
	 * @param Description - Description of the step to be displayed
	 * @param exp         - Expected Value in String format
	 * @param act         - Actual Value to be compared with Expected value in
	 *                    String format
	 */

	public void verify(String Description, String exp, String act) {

		Description = "<font color='#000000'>" + Description;

		if (exp.equalsIgnoreCase(act)) {
			test.log(LogStatus.PASS, "Step "+ ++stepCounter,
					"<b>"+Description+"</b><br />Expected : " + exp + "<br /> Actual : <b><font color='green'>" + act);
		} else {
			test.log(LogStatus.FAIL, "Step "+ ++stepCounter, "<b>"+Description+"</b><br />Expected : " + exp + "<br /> Actual : <b><font color='red'>" + act);
		}

		extentReports.flush();

	}
	
	public void verify(String Description, String exp, String act, String filenamePath) {
		
		Description = "<font color='#000000'>" + Description;
		
		if(!filenamePath.equalsIgnoreCase("")) {
			try {
				if (exp.equalsIgnoreCase(act)) {
					
					test.log(LogStatus.PASS, "<font color=000000> Step " + ++stepCounter,
							"<div align='left' style='float:left;height:100%; width:100%'><b>" + Description + "</b><br />"
									+ "Expected : " + exp + "<br /> Actual : <b><font color='green'>" + act);
					
				} else {
					test.log(LogStatus.FAIL, "<font color=000000> Step " + ++stepCounter,
							"<div align='left' style='float:left;height:100%; width:100%'><b>" + Description + "</b><br />"
									+ "Expected : " + exp + "<br /> Actual : <b><font color='red'>" + act + "<a "
									+ NewWindowPopUpHTMLCode() + " target='_blank' href=" + getscreenshot(imagePath)
									+ " style='float:right'>" + "<img src='camera.png' alt='Screenshot' height='30' width='30'></a></div>");
				}

			} catch (Exception e) {
				test.log(LogStatus.WARNING, "<font color=000000> Step " + ++stepCounter,
						Description + "<div align='right' style='float:right'>Unable to take screenshot</div>");
			}
		}

		
		else {
			if (exp.equalsIgnoreCase(act)) {
				test.log(LogStatus.PASS, "Step "+ ++stepCounter,
						"<b>"+Description+"</b><br />Expected : " + exp + "<br /> Actual : <b><font color='green'>" + act);
			} else {
				test.log(LogStatus.FAIL, "Step "+ ++stepCounter, "<b>"+Description+"</b><br />Expected : " + exp + "<br /> Actual : <b><font color='red'>" + act);
			}
		}

		extentReports.flush();

	}

	/**
	 * FUNCTION - Verify & log step when expected & actual are in string format
	 * 
	 * @param Description - Description of the step to be displayed
	 * @param exp         - Expected Value in 'int' format
	 * @param act         - Actual Value to be compared with Expected value in 'int'
	 *                    format
	 */

	public void verify(String Description, int exp, int act) {

		Description = "<font color='#000000'>" + Description;

		if (exp == act) {
			test.log(LogStatus.PASS, "Step " + ++stepCounter, "<b>"+Description+"</b><br />Expected : " + String.valueOf(exp)
					+ "<br /> Actual : <b><font color=\"green\">" + String.valueOf(act));
		} else {
			test.log(LogStatus.FAIL, "Step " + ++stepCounter, "<b>"+Description+"</b><br /> Expected: <b>" + String.valueOf(exp)
					+ "<br /> Actual : <b><font color=\"red\">" + String.valueOf(act));
		}

		extentReports.flush();

	}

	/**
	 * FUNCTION - Verify & log step when expected & actual are in string format
	 * 
	 * @param Description - Description of the step to be displayed
	 * @param exp         - Expected Value in 'boolean' format
	 * @param act         - Actual Value to be compared with Expected value in
	 *                    'boolean' format
	 */

	public void verify(String Description, boolean exp, boolean act) {

		Description = "<font color='#000000'>" + Description;

		if (exp == act) {
			test.log(LogStatus.PASS, "Step "+ ++stepCounter, "<b>"+Description+"</b><br />Expected : " + String.valueOf(exp)
					+ "<br /> Actual: <b><font color=\"green\">" + String.valueOf(act));
		} else {
			test.log(LogStatus.FAIL, "Step "+ ++stepCounter, "<b>"+Description+"</b><br /> Expected: <b>" + String.valueOf(exp)
					+ "<br /> Actual: <b><font color=\"red\">" + String.valueOf(act));
		}

		extentReports.flush();

	}

	public void verifyRegex(String Description, String exp, String act) {

		boolean bool = new FunctionClass().regexMatcher(exp, act);
		Description = "<font color='#000000'>" + Description;

		if (bool) {
			test.log(LogStatus.PASS, "Step "+ ++stepCounter,
					"<b>"+Description+"</b><br />Expected : " + exp + "<br /> Actual : <b><font color='green'>" + act);
		} else {
			test.log(LogStatus.FAIL, "Step "+ ++stepCounter, "<b>"+Description+"</b><br />Expected : " + exp + "<br /> Actual : <b><font color='red'>" + act);
		}
		extentReports.flush();
	}

	public void warn(String StepName, String Description) {
		StepName = "<font color='#C35817'>" + StepName;

		test.log(LogStatus.WARNING, StepName, "<b><font color='#C35817'>" + Description);

		extentReports.flush();
	}
	
	public void sectionInfo(String Description) {

		String stepName = "<font style='color: #1220E6;background-color: #C0C0C0'><b>SECTION INFO</b></font>";

		String markup = "<div style='color: black;background-color: #C0C0C0;text-shadow: 2px 2px 5px white; text-align: left'><b>REPLACE</b></div>";
		Description = markup.replace("REPLACE", Description);
		test.log(LogStatus.INFO, stepName, Description);
		extentReports.flush();
	}
	
	public void subSectionInfo(String description) {
		
		String stepName = "<font style='color: #1220E6;background-color: lightblue'>INFO</font>";

		String markup = "<div style='color: black;background-color: lightblue;text-shadow: 2px 2px 5px white; text-align: left'><b>REPLACE</b></div>";
		description = markup.replace("REPLACE", description);
		test.log(LogStatus.INFO, stepName, description);
		extentReports.flush();
	}
}
