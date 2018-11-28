/***********************************Header Start*********************************************************************************
 * Application/ Module Name                      	   : Decorist
 * Test/ Function Name                                 : All reusable methods
 * Owner                                               : AutomationTeam
 ***********************************************************************
 * Creation/Modification Log: 
 * Date                     By                                Notes                                    
 ---------                ----------                      ---------
 *
 ***********************************Header End*********************************************************************************/
package com.org.core;

import org.testng.Assert;
import org.testng.Reporter;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.decorist.businessrules.BusinessFunctions;
import com.decorist.businessrules.Decorist_Constants;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class Assertions {
	static Logger LOG = Logger.getLogger(BusinessFunctions.class);
	public static String WindowsUserName = System.getProperty("user.name");
	protected static String snapshotFolder = WindowsUserName + "\\Snapshots\\";
	static Calendar c = Calendar.getInstance();
	static int year = c.get(Calendar.YEAR);
	static String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
	public static String releaseName=month+year;
	protected static String passed = Decorist_Constants.PASS;
	protected static String failed = Decorist_Constants.FAIL;
	public static ExtentReports extent = new ExtentReports(
			System.getProperty(Decorist_Constants.USER_DIR) + Decorist_Constants.AUTOMATION_REPORT, true);
	public static ExtentTest test;
	public static void initExtentReport(String name, boolean isRptOverwrite) {
		extent = new ExtentReports(System.getProperty(Decorist_Constants.USER_DIR) + Decorist_Constants.AUTOMATION_REPORT,
				isRptOverwrite);
		test = extent.startTest(Decorist_Constants.Decorist_1 + name + Decorist_Constants.TEST_REPORT, Decorist_Constants.FUN_TEST_CASE);
	}

	/**
	 * test Step Abort
	 * 
	 * @param result
	 * @param message
	 * @author RahulSharma
	 */
	public static void testStepAbort(boolean result, String message) {
		if (result)
			LOG.info(Decorist_Constants.PASS + message);
		else
			LOG.info(Decorist_Constants.FAIL + message);
		Assert.assertTrue(result, message);
	}

	/**
	 * test Step Continue
	 * 
	 * @param result
	 * @param message
	 * @author RahulSharma
	 */
	public static void testStepContinue(boolean result, String message) {
		if (result)
			LOG.info(Decorist_Constants.PASS + message);
		else
			LOG.info(Decorist_Constants.FAIL + message);
	}

	/**
	 * Particularly for test Step
	 * 
	 * @param message
	 * @author RahulSharma
	 */
	public static void testStepMessage(String message) {
		LOG.info(Decorist_Constants.TEST_STEP + message);
		Reporter.log(Decorist_Constants.INFO + message);
	}

	/**
	 * Message from Method
	 * 
	 * @param message
	 * @author RahulSharma
	 */
	public static void methodMessage(String message) {
		LOG.info(Decorist_Constants.METHOD + message);
		Reporter.log(Decorist_Constants.INFO + message);
	}
	
	/**
	 * print message
	 * 
	 * @param message
	 * @author RahulSharma
	 */
	public static void printMessage(String message) {
		LOG.info("----" + message + "----");
	}

	/**
	 * write in extent report
	 * 
	 * @author RahulSharma
	 */
	public static void writeInExtentReport() {
		extent.endTest(test);
		extent.flush();
	}
}