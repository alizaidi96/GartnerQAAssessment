# Gartner QA Assessment
QA Engineer Assignment - Automate test scenario for movie search functionality <br /><br />

THIS IS AN **MS EXCEL-DRIVEN**, **MAVEN-JAVA FRAMEWORK** THAT COMBINES **API AND UI TESTING**. <br />
The contents of the projects and steps for execution are defined below.

This framework uses **Extent Reporting** library for generating **HTML reports** after the execution is completed. <br /><br />

### 1. PROJECT CONTENTS EXPLAINED
The project folder looks like this:
  * **GridFiles:** contains the Selenium standalone jars and the selenium browser drivers for different browsers
  * **Reports:** contains the execution reports that are generated after every execution. Each report folder after every execution has a unique timestamp which differentiates it from the rest.
  * **src:** contains all the packages and classes (all the code) for the project.
  * **TestData:** as the name suggests, contains the test data excel sheet that we need for running our executions smoothly.
  * **Excel-config:** configuration file (XML) containing details about the Assessment-TestData.xls file (headers, rows, columns, etc.)
  * **Extent-config:** configuration file containing details about the Extent reports (HTML reports) that are generated after the execution is complete.
  * **pom.xml:** contains details about the various dependencies and also downloads them as part of the Maven build.
  
### 2. TEST DATA EXCEL FILE
The test data sheet **contains all the required test data** that we need for executing our test cases. It is also **used to control which test cases we need to execute** and the ones we do not need to execute.

The Assessment-TestData.xlsx file contains three sheets.

  * **Execution_Admin:** to control which test cases to run and on which environment (in our case - only Test environment).
  * **UI_TestData:** contains all the UI related test data for UI-based test cases.
  * **UIRelatedAPITestData:** contains the data for UI-based test cases which all need to run APIs.
  
### 3. STEPS FOR EXECUTION
Please follow the below steps for execution on your local machine.

  * Open the Assessment-TestData.xls file under the TestData folder.
  * In the **Execution_Admin** worksheet, select the test cases that are to be executed and save the file. In our case (for the assessment purposes) only one test case is defined - **IMDBTest001**.
  * Import the Maven project in an IDE of your choice, preferably Eclipse or IntelliJ Idea.
  * Open the **Executor.class** under **src/test/java/executor/** package. This class is also defined as the **mainClass** in the **pom.xml** file of the Maven configuration build. This means that we can also execute it using the: mvn clean install -s settings.xml command if we have a proper settings.xml file.
  * **\*FOR ECLIPSE**\* Right click on the code. Go to **Run As > Java Application**.
  * The execution will start and the test cases will be executed one-by-one. Since in our case there is only one scenario to be automated so there is only one single test case.
  * Once the execution is complete, the execution will stop running and an **HTML report** will be generated. The report generated can be found under **Reports/** folder.
  
### 4. EXECUTION REPORT
An HTML report is generated after the execution is completed. This report is using the Extent Reporting library by ReleventCodes.  <br /><br />
The report can be found in the **Reports/** folder.

  * Once the execution is completed, a new folder will be created under the **Reports/** folder with a **unique timestamp** corresponding to when the execution was started.
  * Open the newly created folder with the timestamp of your execution.
  * **UIReport:** folder containing individual browser report folders, screenshots, responses and other UI elements and files needed for testing purposes.
  * Open the UIReport folder.
  * The report generated will be by the name **GoogleChrome_Report.html** and will contain details about every step of the test execution. The report will also contain screenshots and response files if any.
  
  
  
