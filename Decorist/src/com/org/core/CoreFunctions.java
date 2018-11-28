/***********************************Header Start*********************************************************************************
 * Application/ Module Name                      	   : eFollett/FMS
 * Owner                                               : AutomationTeam
 ****************************************************************************************************************************************************
 * Creation /Modification Log: 
 * Date                     By                                Notes                                    
 ---------                ----------                          ---------
 * 
 ****************************************************************************************************************************************************
 * Review/Feedback Log: 
 * Date                     By                                Notes                                    
 ---------                 --------                   	 ----------
 * [Date]                   [Reviewer]                 [Brief description of the review/feedback comments]
 ***********************************************************************
 * Functional Test Coverage Description   			   : It defined all the Selenium dependent methods. 														   
 ***********************************Header End*********************************************************************************/
package com.org.core;

import java.awt.Robot;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.decorist.businessrules.Decorist_Constants;
import com.relevantcodes.extentreports.LogStatus;

public class CoreFunctions {
	static Logger LOG = Logger.getLogger(CoreFunctions.class);
	/**
	 * * All global variable deceleration
	 */
	private static ThreadLocal<WebDriver> localThread = new ThreadLocal<WebDriver>();
	private String parentWindowHandle;
	private int elementWaitTime = 40;
	protected static String browserCounterIe = "";
	protected static String browserCounterFF = "";
	protected static String browserCounterChrome = "";
	protected static String browserCounterMSEdge = "";
	private static String gridMachine = System.getProperty("enableGrid", "true");
	Robot robot = null;

	/**
	 * Returns Global Driver Object
	 * 
	 * @author RahulSharma
	 */
	public static WebDriver getDriver() {
		return (WebDriver) localThread.get();
	}

	/**
	 * Taking URL property from config file
	 * 
	 * @return String
	 * @author RahulSharma
	 */

	public String loadurl() {
		FileInputStream file;
		String testUrl = null;
		try {
			file = new FileInputStream(
					System.getProperty(Decorist_Constants.USER_DIR) + Decorist_Constants.PATH_CONFIG_PROPERTIES);
			Properties prop = new Properties();
			prop.load(file);
			testUrl = prop.getProperty(Decorist_Constants.URL);
		} catch (FileNotFoundException e) {
			LOG.info(Decorist_Constants.ERROR, e);

		} catch (IOException e) {
			LOG.info(Decorist_Constants.ERROR, e);
		}
		return testUrl;
	}

	/**
	 * Customize function for taking URL property from config file
	 * 
	 * @return String
	 * @param url
	 * @author RahulSharma
	 */

	public String loadCustomUrl(String url) {
		FileInputStream fileInput;
		String testUrl = null;
		try {
			fileInput = new FileInputStream(
					System.getProperty(Decorist_Constants.USER_DIR) + Decorist_Constants.PATH_CONFIG_PROPERTIES);
			Properties prop = new Properties();
			prop.load(fileInput);
			testUrl = prop.getProperty(url);
		} catch (FileNotFoundException e) {
			LOG.info(Decorist_Constants.ERROR, e);
		} catch (IOException e) {
			LOG.info(Decorist_Constants.ERROR, e);
		}
		return testUrl;
	}

	/**
	 * Taking Browser property from config file
	 * 
	 * @return String
	 * @author RahulSharma
	 */
	public String loadBrowser() {
		FileInputStream fileInput;
		String browser = null;
		try {
			fileInput = new FileInputStream(
					System.getProperty(Decorist_Constants.USER_DIR) + Decorist_Constants.PATH_CONFIG_PROPERTIES);
			Properties prop = new Properties();
			prop.load(fileInput);
			browser = prop.getProperty(Decorist_Constants.BROWSER);
		} catch (FileNotFoundException e) {
			LOG.info(Decorist_Constants.ERROR, e);
		} catch (IOException e) {
			LOG.info(Decorist_Constants.ERROR, e);
		}
		return browser;
	}

	/**
	 * *Taking URL for auto provision property from config file
	 * 
	 * @param url
	 * @author RahulSharma
	 */
	public void autoProvUrl(String url) {
		String expecUrl = loadCustomUrl(url);
		setUrl(expecUrl);
	}

	/**
	 * Getting environment info
	 * 
	 * @author RahulSharma
	 */
	public void envinfo() {
		String browserDetails = "";
		if (!browserCounterChrome.equals("")) {
			browserDetails = browserCounterChrome;
		} else if (!browserCounterFF.equals("")) {
			browserDetails = browserCounterFF;
		} else if (!browserCounterIe.equals("")) {
			browserDetails = browserCounterIe;
		}
		Assertions.methodMessage(Decorist_Constants.START_BROWSER + browserDetails);
		Capabilities cap = ((RemoteWebDriver) getDriver()).getCapabilities();
		//String details = cap.getPlatform().toString();
		//Platform OS = cap.getPlatform().WIN10;
		String details = System.getProperty("os.name");
		Assertions.methodMessage(Decorist_Constants.PLATFORM + details);
		String version = cap.getVersion().toString();
		Assertions.methodMessage(Decorist_Constants.VERSION + browserDetails + Decorist_Constants.BROWSER_IS + version);
		Assertions.methodMessage(Decorist_Constants.BROWSER + browserDetails + Decorist_Constants.SUCCESS_INVOKE);
	}

	/**
	 * Tear down method
	 * 
	 * @author RahulSharma
	 */

	@AfterMethod
	public void tearDown() {
		Assertions.writeInExtentReport();
		closeBrowser();
	}

	/**
	 * navigates to Url passed in String Argument
	 * 
	 * @param url
	 * @author RahulSharma
	 */
	public void setUrl(String url) {
		try {
			if (getDriver() != null) {
				Assertions.methodMessage(Decorist_Constants.NAVIGAT_URL + url);
				getDriver().navigate().to(url);
				waitForBrowsertoload();
			} else
				Assertions.methodMessage(Decorist_Constants.NO_BRWSR_NAVIGAT);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
		}
	}

	/**
	 * Closes all current Driver Browsers
	 * 
	 * @author RahulSharma
	 */
	public void closeBrowser() {
		try {
			LOG.info(Decorist_Constants.CLOSE);
			if (getDriver() != null) {
				Assertions.methodMessage(Decorist_Constants.CLOSING_BRWSR);
				getDriver().quit();
			} else
				Assertions.methodMessage(Decorist_Constants.NO_BRWSR_CLOSE);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.UNABLE_CLOSE_BRWSR, true);
		}
	}

	/**
	 * Takes screenshot of current page appearing in Browser Extent Report
	 * implementation
	 * 
	 * @param result
	 * @param message
	 * @param snapshot
	 * @author RahulSharma
	 */
	public void logReport(boolean result, String message, boolean snapshot) {
		String snapshotPath = "";
		String extentSnapshotPath = "";
		if (snapshot) {
			snapshotPath = "SS_" + takeSnapshot();
			extentSnapshotPath = takeSnapshot();
		}
		if (result) {
			Reporter.log(Assertions.passed + message + snapshotPath);
			Assertions.test.log(LogStatus.PASS, message);
			if (!extentSnapshotPath.equals("")) {
				Assertions.test.log(LogStatus.PASS,
						Decorist_Constants.PASSED_SNAP + Assertions.test.addScreenCapture(extentSnapshotPath));
			}
		} else {
			Reporter.log(Assertions.failed + message + snapshotPath);
			Assertions.test.log(LogStatus.FAIL, message);
			if (!extentSnapshotPath.equals("")) {
				Assertions.test.log(LogStatus.FAIL,
						Decorist_Constants.FAILED_SNAP + Assertions.test.addScreenCapture(extentSnapshotPath));
			}
			Assert.fail(Assertions.failed + message);
		}
	}

	/**
	 * Taking snapshot
	 * 
	 * @return string
	 * @author RahulSharma
	 */
	public String takeSnapshot() {

		File scrFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		String imgFileName = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss-SS").format(new GregorianCalendar().getTime());
		imgFileName = Assertions.releaseName + "_" + Assertions.WindowsUserName + "_" + super.getClass().getSimpleName()
				+ "_" + imgFileName + "_" + getUniqueID();
		imgFileName = Assertions.snapshotFolder + imgFileName + Decorist_Constants.PNG;
		try {
			FileUtils.copyFile(scrFile, new File(imgFileName));
		} catch (IOException e) {
			LOG.info(Decorist_Constants.ERROR, e);
		}
		return imgFileName;
	}

	/**
	 * Highlights By Locator passed in arguments
	 * 
	 * @param element
	 * @Author RahulSharma
	 */
	public void highlightObject(WebElement element) {
		try {
			if (getDriver() != null) {
				JavascriptExecutor javascript = (JavascriptExecutor) getDriver();
				javascript.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
						"border: 3px solid yellow;");
			} else
				logReport(false, Decorist_Constants.NO_BRWSR_HIGLT_ELE, true);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.UNABLE_HIGLT_LOCTR, true);
		}
	}

	/**
	 * Print Exception
	 * 
	 * @author RahulSharma
	 */
	public void excepLog(Exception e) {
		LOG.info(e.getMessage());
	}

	/**
	 * Check Browser Ready state
	 * 
	 * @return boolean
	 * @author RahulSharma
	 */
	private boolean checkBrowserReadyState() {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		if (js.executeScript("return document.readyState").toString().equals("complete")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @purpose To wait for browser to load
	 * 
	 * @author RahulSharma
	 */
	public void waitForBrowsertoload() {
		try {
			boolean isReady = checkBrowserReadyState();
			if (!isReady) {
				waitForBrowsertoload();
			}
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
		}
	}

	/**
	 * To Click on Element
	 * 
	 * @param element
	 * @param name
	 * @Author RahulSharma
	 */
	public void click(WebElement element, String name) {
		try {
			highlightObject(element);
			Assertions.methodMessage(Decorist_Constants.VRFY_THAT + name + Decorist_Constants.IS_CLCKD);
			element.click();
			waitForBrowsertoload();
			logReport(true, Decorist_Constants.VERFID + name + Decorist_Constants.IS_CLCKD + Decorist_Constants.SUCCESSFLY, true);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
		}
	}

	/**
	 * To Click on list of an Elements
	 * 
	 * @param elements
	 * @param index
	 * @param name
	 * @Author RahulSharma
	 */
	public void click(List<WebElement> elements, int index, String name) {
		try {
			Assertions.methodMessage(Decorist_Constants.VRFY_THAT + name + Decorist_Constants.ELEMENT + Decorist_Constants.IS_CLCKD);
			elements.get(index).click();
			logReport(true, Decorist_Constants.VERFID + name + Decorist_Constants.ELEMENT + Decorist_Constants.IS_CLCKD
					+ Decorist_Constants.SUCCESSFLY + Decorist_Constants.AT_INDEX + Integer.toString(index), true);
		} catch (Throwable t) {
			LOG.info(Decorist_Constants.ERROR, t);
			logReport(false, Decorist_Constants.FAILD_CLCK_ON + Decorist_Constants.ELEMENT + name + Decorist_Constants.AT_INDEX
					+ Integer.toString(index), true);
		}
	}

	/**
	 * To enter input value
	 * 
	 * @param text
	 * @param element
	 * @author RahulSharma
	 */
	public void input(String text, WebElement element) {
		try {
			Assertions.methodMessage(Decorist_Constants.ENTER_TXT + text);
			element.sendKeys(text);
			logReport(true, Decorist_Constants.TXT_ENT_SCUCCESSFLY + Decorist_Constants.INPUT_VAL + text, true);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.FAIL_TO + Decorist_Constants.ENTER_TXT + text, true);
		}
	}

	/**
	 * To clear and entering text in any field
	 * 
	 * @param element
	 * @author RahulSharma
	 */
	public void clearTextBoxValue(WebElement element) {
		try {
			Assertions.printMessage(Decorist_Constants.VRFY_CLR_TXTBOX_VAL);
			element.clear();
			Assertions.printMessage(Decorist_Constants.VRFYD_TXTBOX_CLR_VAL);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.FAILED_CLR_TXTBOX_VAL, true);
		}
	}

	/**
	 * To focus on pop up window
	 * 
	 * @author RahulSharma
	 */
	public void focusPopupWindow() {
		try {
			Assertions.methodMessage(Decorist_Constants.VRFY_FOCUS_POPUP);
			this.parentWindowHandle = getDriver().getWindowHandle();
			for (String winHandle : getDriver().getWindowHandles()) {
				getDriver().switchTo().window(winHandle);
			}
			logReport(true, Decorist_Constants.VRFYD_FOCUS_POPUP, true);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.EXEP_WHEN_CHNG_FOCUS_POPUP, true);
		}
	}

	/**
	 * To focus on main pop up window
	 * 
	 * @Author RahulSharma
	 */
	public void focusMainWindow() {
		try {
			Assertions.methodMessage(Decorist_Constants.VRFY_FOCUS_MAIN);
			getDriver().switchTo().window(this.parentWindowHandle);
			logReport(true, Decorist_Constants.VRFYD_FOCUS_MAIN, true);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.EXEP_WHEN_CHNG_FOCUS_MAIN, true);
		}
	}

	/**
	 * To get current url
	 * 
	 * @Author RahulSharma
	 */
	public String getCurrentURL() {
		try {
			Assertions.methodMessage(Decorist_Constants.GET_CURR_URL);
			String url = getDriver().getCurrentUrl();
			logReport(true, Decorist_Constants.CURR_URL_RETRIVED + url, true);
			return url;
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.FAILED_GET_CURR_URL, true);
		}
		return null;
	}

	/**
	 * To set url in address bar
	 * 
	 * @param url
	 * @Author RahulSharma
	 */
	public void setCurrentURL(String url) {
		try {
			Assertions.methodMessage(Decorist_Constants.SET_CURR_URL);
			getDriver().get(url);
			logReport(true, Decorist_Constants.CURR_URL_SET + url, true);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.FAILED_SET_CURR_URL + url, true);
		}
	}

	/**
	 * To double click on any element
	 * 
	 * @param element
	 * @Author RahulSharma
	 */
	public void doubleClick(WebElement element) {
		try {
			Assertions.methodMessage(Decorist_Constants.VRFY_DOUBLE_CLCK_ELE);
			Actions action = new Actions(getDriver());
			action.doubleClick(element);
			action.perform();
			waitForBrowsertoload();
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.FAILED_DOUBLE_CLCK_ELE + e.getMessage(), true);
		}
	}

	/**
	 * To select option from drop down
	 * 
	 * @param element
	 * @param optionToSelect
	 * @Author RahulSharma
	 */
	public void selectOptionFromDropdown(WebElement element, String optionToSelect) {
		universalWait(element);
		try {
			Assertions.methodMessage(MessageFormat.format(Decorist_Constants.VRFY_SEL_OPT_DROPDOWN, optionToSelect,
					element.getAttribute("name")));
			new Select(element).selectByVisibleText(optionToSelect);
			logReport(true,
					MessageFormat.format(Decorist_Constants.VRFYD_TXT_SEL, optionToSelect, element.getAttribute("name")),
					true);
		} catch (NoSuchElementException err) {
			LOG.info(Decorist_Constants.ERROR, err);
		}
	}

	/**
	 * To select option from drop down
	 * 
	 * @param element
	 * @param optionToSelect
	 * @Author RahulSharma
	 */
	public void selectOptionFromDropdownValue(WebElement element, String optionToSelect) {
		universalWait(element);
		try {
			Assertions.methodMessage(MessageFormat.format(Decorist_Constants.VRFY_SEL_OPT_DROPDOWN, optionToSelect,
					element.getAttribute("name")));
			new Select(element).selectByValue(optionToSelect);
			logReport(true,
					MessageFormat.format(Decorist_Constants.VRFYD_VAL_SEL, optionToSelect, element.getAttribute("name")),
					true);
		} catch (NoSuchElementException err) {
			LOG.info(Decorist_Constants.ERROR, err);
			logReport(false, MessageFormat.format(Decorist_Constants.FAILED_SEL_OPT_DROPDOWN, optionToSelect,
					element.getAttribute("name")), true);
		}
	}

	/**
	 * To select option from drop down
	 * 
	 * @param element
	 * @param index
	 * @Author RahulSharma
	 */
	public void selectOptionFromDropdown(WebElement element, int index) {
		universalWait(element);
		WebElement selectedElement = null;
		String selectedItemText = null;
		try {
			Assertions.methodMessage(
					MessageFormat.format(Decorist_Constants.VRFY_IDX_OPT_DROPDOWN, index, element.getAttribute("name")));
			Select selector = new Select(element);
			selector.selectByIndex(index);
			selectedElement = selector.getFirstSelectedOption();
			selectedItemText = selectedElement.getText();
			logReport(true, Decorist_Constants.VRFYD_INDEX_SEL + index + ", (" + selectedItemText + ") "
					+ Decorist_Constants.FRM_DROPDOWN_SEL_LST, true);
		} catch (NoSuchElementException err) {
			LOG.info(Decorist_Constants.ERROR, err);
			logReport(false, MessageFormat.format(Decorist_Constants.FAIL_IDX_OPT_DROPDOWN, index, selectedItemText,
					element.getAttribute("name")), true);
		}
	}

	/**
	 * To Waiter for clicking any element
	 * 
	 * @param element
	 * @param name
	 * @Author RahulSharma
	 */
	public void waitElementClickable(WebElement element, String name) {
		try {
			Assertions.methodMessage(Decorist_Constants.VRFY_WAIT + name + Decorist_Constants.ELE_CLICKABLE);
			long start = System.currentTimeMillis();
			universalWait(element);
			logReport(true,
					Decorist_Constants.VERIFID + name + Decorist_Constants.ELE_CLICKABLE_WAIT
							+ Long.toString(System.currentTimeMillis() - start) + Decorist_Constants.MILI_SCE_CLICKABLE,
					true);
		} catch (TimeoutException e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.FAILED_TIMEOUT + Decorist_Constants.TIMEOUT_THRSHOLD
					+ Integer.toString(this.elementWaitTime), true);
		}
	}

	/**
	 * To Waiter for element present
	 * 
	 * @Param element
	 * @param name
	 * @Author RahulSharma
	 */
	public void waitForElementPresent(WebElement element, String name) {
		long startTime = System.currentTimeMillis();
		long totalWaitTime = 100L;
		try {
			Assertions.methodMessage(Decorist_Constants.VRFY_WAIT + name + Decorist_Constants.ELE_PRESENT);
			startTime = System.currentTimeMillis();
			universalWait(element);
			totalWaitTime = startTime - System.currentTimeMillis();
			logReport(true, Decorist_Constants.VERIFID + name + Decorist_Constants.ELE_PRESENT_WAIT + Long.toString(totalWaitTime)
					+ Decorist_Constants.MILI_SCE_PRESENT, true);
		} catch (TimeoutException e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.FAILED_TIMEOUT + name + Decorist_Constants.TIMEOUT_THRSHOLD
					+ Integer.toString(this.elementWaitTime), false);
		}
	}

	/**
	 * To get the text of any locator
	 * 
	 * @param element
	 * @Author RahulSharma
	 */
	public String getTextOnLocator(WebElement element) {
		String value = null;
		try {
			value = element.getText();
			return value.trim();
		} catch (NoSuchElementException e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.EXEP_FAILED_RETRIVE_TXT + e.getMessage(), true);
		}
		return null;
	}

	/**
	 * To get the text of any locator
	 * 
	 * @param elements
	 * @param index
	 * @Author RahulSharma
	 */
	public String getTextOnLocator(List<WebElement> elements, int index) {
		try {
			return elements.get(index).getText().trim();
		} catch (NoSuchElementException err) {
			logReport(false, Decorist_Constants.FAILD_LOCT_INDEX_VAL, true);
			return null;
		} catch (IndexOutOfBoundsException err) {
			LOG.info(Decorist_Constants.ERROR, err);
			logReport(false, Decorist_Constants.FAILD_IOOB_EXEP + Integer.toString(index), true);
			logReport(false, Decorist_Constants.ELE_NOTFOUND, true);
		}
		return null;
	}

	/**
	 * To clear and sendkeys data in the field
	 * 
	 * @param fieldName
	 * @param data
	 * @Author RahulSharma
	 */
	public void clearAndSendKey(WebElement fieldName, String data) {
		try {
			Assertions.methodMessage(
					MessageFormat.format(Decorist_Constants.VRFY_TXT_CLR_VAL, fieldName.getAttribute("name")));
			clearTextBoxValue(fieldName);
			fieldName.sendKeys(data);
			logReport(true,
					MessageFormat.format(Decorist_Constants.VRFYD_TXT_CLR_VAL + data, fieldName.getAttribute("name")), true);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, MessageFormat.format(Decorist_Constants.FAILED_TXT_CLR_VAL, fieldName.getAttribute("name")),
					true);
		}
	}

	/**
	 * To clear and sendkeys data in the field
	 * 
	 * @param fieldName
	 * @param data
	 * @param hideValue
	 * @Author RahulSharma
	 */
	public void clearAndSendKey(WebElement fieldName, String data, String hideValue) {
		try {
			Assertions.methodMessage(
					MessageFormat.format(Decorist_Constants.VRFY_TXT_CLR_VAL, fieldName.getAttribute("name")));
			clearTextBoxValue(fieldName);
			fieldName.sendKeys(data);
			logReport(true,
					MessageFormat.format(Decorist_Constants.VRFYD_TXT_CLR_VAL + hideValue, fieldName.getAttribute("name")),
					true);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, MessageFormat.format(Decorist_Constants.FAILED_TXT_CLR_VAL, fieldName.getAttribute("name")),
					true);
		}
	}

	/**
	 * To check element visible
	 * 
	 * @param element
	 * @return boolean
	 * @Author RahulSharma
	 */
	public boolean isElementPresent(WebElement element) {
		try {
			Assertions.methodMessage("Verify element is present and displaying");
			if (element.isDisplayed()) {
				logReport(true, "Verified element is displayed -> element text: " + element.getText(), true);
				return true;
			} else {
				return false;
			}
		} catch (NoSuchElementException e) {
			LOG.info(Decorist_Constants.ERROR, e);
			return false;

		}
	}

	/**
	 * To get element count
	 * 
	 * @param elements
	 * @return int
	 * @author RahulSharma
	 */
	public int getElementCount(List<WebElement> elements) {
		int count1 = 0;
		try {
			count1 = elements.size();
			logReport(true, Decorist_Constants.VRFYD_ELE_COUNT + count1, true);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.FAILED_COUNT, true);
		}
		return count1;
	}

	/**
	 * To get data from xml
	 * 
	 * @param data
	 * @return String
	 * @Author RahulSharma
	 */
	public String getUserfromXml(String data) {
		String element = null;
		File xmlFile = new File(System.getProperty(Decorist_Constants.USER_DIR) + Decorist_Constants.PATH_TEST_DATA);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			if (browserCounterChrome.equals("chrome")) {
				DocumentBuilder dbuilder = dbFactory.newDocumentBuilder();
				Document doc = dbuilder.parse(xmlFile);
				element = doc.getElementsByTagName(data).item(0).getTextContent();
			} else if (browserCounterIe.equals("ie")) {
				DocumentBuilder dbuilder = dbFactory.newDocumentBuilder();
				Document doc = dbuilder.parse(xmlFile);
				element = doc.getElementsByTagName(data).item(1).getTextContent();
			} else if (browserCounterFF.equals("firefox")) {
				DocumentBuilder dbuilder = dbFactory.newDocumentBuilder();
				Document doc = dbuilder.parse(xmlFile);
				element = doc.getElementsByTagName(data).item(2).getTextContent();
			}
			else if (browserCounterMSEdge.equals("edge")) {
				DocumentBuilder dbuilder = dbFactory.newDocumentBuilder();
				Document doc = dbuilder.parse(xmlFile);
				element = doc.getElementsByTagName(data).item(3).getTextContent();
			}
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, e + Decorist_Constants.NODATA_FOUND, false);
		}
		return element;
	}

	/**
	 * To get data from xml
	 * 
	 * @param data
	 * @return String
	 * @Author RahulSharma
	 */
	public String getdatafromXml(String data) {
		String element = null;
		File xmlFile = new File(System.getProperty(Decorist_Constants.USER_DIR) + Decorist_Constants.PATH_TEST_DATA);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			element = doc.getElementsByTagName(data).item(0).getTextContent();
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, e + Decorist_Constants.NODATA_FOUND, false);
		}
		return element;
	}

	/**
	 * To add order number in txt file
	 * 
	 * @param Ordernumber
	 * @Author RahulSharma
	 */
	public void addOrderNumber(String orderNumber, String fileName) {
		String testFile = System.getProperty(Decorist_Constants.USER_DIR) + Decorist_Constants.PATH_ORDRNO_TXT + fileName;
		File file = new File(testFile);
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(file, true));
			pw.println(orderNumber);
			pw.flush();
			pw.close();
		} catch (IOException ex) {
			LOG.info(Decorist_Constants.ERROR, ex);
			logReport(false, Decorist_Constants.NOTABLE_ADD_ORDRNO, false);
		}
	}

	/**
	 * Wait handle functionality
	 * 
	 * @param element
	 * @return boolean
	 * @Author RahulSharma
	 */
	public boolean universalWait(WebElement element) {
		for (int index = 0; index < 30; index++) {
			try {
				element.isDisplayed();
				return true;
			} catch (Exception e1) {
				LOG.info(Decorist_Constants.ERROR, e1);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					LOG.info(Decorist_Constants.ERROR, e);
				}
			}
		}
		return false;
	}

	/**
	 * Wait handler functionality
	 * 
	 * @param secs
	 * @Author RahulSharma
	 */
	public void waitHandler(int secs) {
		try {
			Thread.sleep(secs * 1000);
		} catch (InterruptedException e) {
			LOG.info(Decorist_Constants.ERROR, e);
		}
	}

	/**
	 * To make enter to any object
	 * 
	 * @param element
	 * @author RahulSharma
	 */
	public void sendKeyToObject(WebElement element) {
		try {
			element.sendKeys(Keys.ENTER);
			Assertions.testStepContinue(true, Decorist_Constants.PASS + Decorist_Constants.SNDKEY_TO_OBJ + "-> ");
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			Assertions.testStepAbort(false, Decorist_Constants.FAIL + Decorist_Constants.SNDKEY_TO_OBJ + "-> ");
			logReport(false, Decorist_Constants.FAILED_SNDKEY_TO_OBJ, false);
		}
	}

	/**
	 * To return Method Name
	 * 
	 * @return String
	 * @author RahulSharma
	 */
	public String getCallerMtdName() {
		String name = "";
		try {
			name = Thread.currentThread().getStackTrace()[2].getMethodName();
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
		}
		return " " + name + "- ";
	}

	/**
	 * To get data from nodes
	 * 
	 * @param nodeTagName
	 * @param index
	 * @return String
	 * @Author RahulSharma
	 */
	public String getDataOfNode(String nodeTagName, int index) {
		String levelList = "";
		File xmlfile = new File(System.getProperty(Decorist_Constants.USER_DIR) + Decorist_Constants.PATH_TEST_DATA);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dbuilder = dbFactory.newDocumentBuilder();
			Document doc = dbuilder.parse(xmlfile);
			NodeList nodeTagList = doc.getElementsByTagName(nodeTagName);
			Element elementNode = (Element) nodeTagList.item(index);
			levelList = elementNode.getTextContent();
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
		}
		return levelList;
	}

	/**
	 * verify Element Present On Page
	 * 
	 * @param element
	 * @Author RahulSharma
	 */
	public void verifyElementPresentOnPage(WebElement element) {
		Assertions.methodMessage(MessageFormat.format(Decorist_Constants.VRFY_ELE_PAGE, element.getAttribute("name")));
		try {
			if (((WebElement) element).isDisplayed())
				Assertions.methodMessage(
						MessageFormat.format(Decorist_Constants.VRFIED_ELE_PAGE, element.getAttribute("name")));
		} catch (NoSuchElementException ignored) {
			LOG.info(Decorist_Constants.ERROR, ignored);
			Assertions
					.methodMessage(MessageFormat.format(Decorist_Constants.FAIL_ELE_NOTFOUND, element.getAttribute("name")));
			Assert.assertFalse(false);
		}
	}

	/**
	 * verify Element Absent On Page
	 * 
	 * @param element
	 * @Author RahulSharma
	 */
	public void verifyElementAbsentOnPage(WebElement element) {
		try {
			Assertions
					.methodMessage(MessageFormat.format(Decorist_Constants.VRFY_ELE_NOT_PAGE, element.getAttribute("name")));
			boolean check = element.isDisplayed();
			if (check == true) {
				Assertions.methodMessage(
						MessageFormat.format(Decorist_Constants.FAIL_ELEMENT_FOUND, element.getAttribute("name")));
				Assert.assertFalse(true);
			}
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			Assertions
					.methodMessage(MessageFormat.format(Decorist_Constants.VRFY_ELE_NOT_PAGE, element.getAttribute("name")));
		}
	}

	/**
	 * verify Text
	 * 
	 * @param actual
	 * @param expected
	 * @Author RahulSharma
	 */
	public void verifyText(String actual, String expected) {
		Assertions.methodMessage(Decorist_Constants.VRFY_TXT);
		if (actual.trim().equalsIgnoreCase(expected.trim())) {
			Assertions.methodMessage(Decorist_Constants.VRFYD_TXT_EXPECTED + expected);
		} else
			Assertions
					.methodMessage(Decorist_Constants.FAILED_TXT_NOT_EXPECTED + actual + Decorist_Constants.EXPECTED + expected);
	}

	/**
	 * verify Text Exist
	 * 
	 * @param parentString
	 * @param subString
	 * @Author RahulSharma
	 */
	public void verifyTextExist(String parentString, String subString) {
		Assertions.methodMessage(Decorist_Constants.VRFY_TXT);
		if (parentString.contains(subString))
			Assertions.methodMessage(Decorist_Constants.VRFYD_SUBSTR);
		else
			Assertions.methodMessage(Decorist_Constants.FAILED_SUBSTR + parentString + Decorist_Constants.SUB_STR + subString);
	}

	/**
	 * verify Element Present On Page
	 * 
	 * @param element
	 * @param type
	 * @Author RahulSharma
	 */
	public void verifyElementPresentOnPage(WebElement element, String type) {
		Assertions.methodMessage(MessageFormat.format(Decorist_Constants.VRFY_ELE_PAGE, element.getText()));
		try {
			if (element.isDisplayed()) {
				Assertions.methodMessage(MessageFormat.format(Decorist_Constants.VRFIED_ELE_PAGE, element.getText()));
			}
		} catch (NoSuchElementException ignored) {
			LOG.info(Decorist_Constants.ERROR, ignored);
			Assertions.methodMessage(MessageFormat.format(Decorist_Constants.FAIL_ELE_NOTFOUND, element.getText()));
			Assert.assertFalse(false);
		}
	}

	/**
	 * verify Double Equal
	 * 
	 * @param valueA
	 * @param valueB
	 * @param verificationDescription
	 * @Author RahulSharma
	 */
	public void verifyDoubleEqual(String valueA, String valueB, String verificationDescription) {
		Assertions.methodMessage(Decorist_Constants.VRFY_DOUBLE_VAL_EQL);
		double parsedA = Double.parseDouble(valueA.trim().replaceAll(Decorist_Constants.NUM, ""));
		double parsedB = Double.parseDouble(valueB.trim().replaceAll(Decorist_Constants.NUM, ""));
		if (parsedA == parsedB)
			Assertions.methodMessage(Decorist_Constants.VRFYD_DOUBLE_VAL_EQL + verificationDescription + ": "
					+ valueA.trim().replaceAll(Decorist_Constants.NUM, "") + Decorist_Constants.WAS_PASSED_STR + valueA
					+ Decorist_Constants.VRFYD_EQLVAL + valueB.trim().replaceAll(Decorist_Constants.NUM, "")
					+ Decorist_Constants.THAT_WAS_PASSED_STR + valueB);
		else
			Assertions.methodMessage(Decorist_Constants.FAILED_DOUBLE_VALNOTEQL + verificationDescription + ": "
					+ valueA.trim().replaceAll(Decorist_Constants.NUM, "") + Decorist_Constants.WAS_PASSED_STR + valueB
					+ Decorist_Constants.NOTEQL_VAL + valueB.trim().replaceAll(Decorist_Constants.NUM, "")
					+ Decorist_Constants.THAT_WAS_PASSED_STR + valueB);
	}

	/**
	 * verify double greater or equal
	 * 
	 * @param expectedGreaterOrEqual
	 * @param expectedLessOrEqual
	 * @param verificationDescription
	 * @Author RahulSharma
	 */
	public void verifyDoubleGreaterOrEqual(String expectedGreaterOrEqual, String expectedLessOrEqual,
			String verificationDescription) {
		Assertions.methodMessage(Decorist_Constants.VRFY_VAL_GRTR_EQL);
		double parsedA = Double.parseDouble(expectedGreaterOrEqual.trim().replaceAll(Decorist_Constants.NUM, ""));
		double parsedB = Double.parseDouble(expectedLessOrEqual.trim().replaceAll(Decorist_Constants.NUM, ""));
		if (parsedA >= parsedB)
			Assertions.methodMessage(Decorist_Constants.VRFYD_VAL_GRTR_EQL + verificationDescription + ": "
					+ expectedGreaterOrEqual.trim().replaceAll(Decorist_Constants.NUM, "") + " was parsed from String: "
					+ expectedGreaterOrEqual + ", and is verified equal or greater than value: "
					+ expectedLessOrEqual.trim().replaceAll("[^.0-9]", "") + " that was parsed from String: "
					+ expectedLessOrEqual);
		else
			Assertions.methodMessage(Decorist_Constants.FAILD_VAL_GRTR_EQL + verificationDescription + ": "
					+ expectedGreaterOrEqual.trim().replaceAll(Decorist_Constants.NUM, "") + " was parsed from String: "
					+ expectedLessOrEqual + ", and is NOT equal or greater than value: "
					+ expectedLessOrEqual.trim().replaceAll("[^.0-9]", "") + " that was parsed from String: "
					+ expectedLessOrEqual);
	}

	/**
	 * verify Checkbox is Checked
	 * 
	 * @param element
	 * @param name
	 * @Author RahulSharma
	 */
	public void verifyCheckboxIsChecked(WebElement element, String name) {
		try {
			Assertions.methodMessage(Decorist_Constants.VRFYD_CHECKBOX);
			if (element.isSelected())
				Assertions.methodMessage(Decorist_Constants.VRFY + name + Decorist_Constants.CHCKBOX_CHCK);
			else
				Assertions.methodMessage(Decorist_Constants.FAILD + name + Decorist_Constants.CHCKBOX_NOTCHCK);
		} catch (NoSuchElementException err) {
			LOG.info(Decorist_Constants.ERROR, err);
			Assertions.methodMessage(Decorist_Constants.FAILED_ELE_NOTFOUND);
			Assert.fail();
		}
	}

	/**
	 * verify element not on page
	 * 
	 * @param element
	 * @param elementName
	 * @Author RahulSharma
	 */
	public void verifyElementNotOnPage(WebElement element, String elementName) {
		try {
			boolean check = element.isDisplayed();
			if (check == true) {
				Assertions.methodMessage(MessageFormat.format(Decorist_Constants.FAIL_ELEMENT_FOUND, element.getText()));
				logReport(false, Decorist_Constants.FAILED + "Element found on page-" + elementName, true);
			} else {
				Assertions.methodMessage(MessageFormat.format(Decorist_Constants.VRFED_ELE_NOT_PAGE, element.getText()));
				logReport(true, Decorist_Constants.PASSED + "Element Not found on page-" + elementName, true);
			}
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.FAIL_ELE_NOTFOUND + elementName, true);
		}
	}

	/* ================================= */
	/**
	 * Set element wait time
	 *
	 * @param waitTime
	 * @author RahulSharma
	 */
	public void setElementWaitTime(int waitTime) {
		this.elementWaitTime = waitTime;
	}

	/**
	 * Element wait time
	 * 
	 * @author RahulSharma
	 */
	public int getElementWaitTime() {
		return this.elementWaitTime;
	}

	/**
	 * Opens browser and home url Check whether browser is already opened or
	 * not.
	 * 
	 * @author RahulSharma
	 */
	@Parameters({ "browser" })
	@BeforeMethod
	public WebDriver openBrowser(@Optional("IamOptional") String browser) {
		String testUrl = loadurl();
		String browser1 = loadBrowser();
		createReportsDirectory();
		try {
			if (getDriver() == null || getDriver().toString().contains("null")) {
				Assertions.methodMessage(Decorist_Constants.START_BROWSER + browser1);
				createInstance(browser1, gridMachine, browser);
				Capabilities cap = ((RemoteWebDriver) getDriver()).getCapabilities();
				String os = cap.getPlatform().toString();
				Assertions.methodMessage(Decorist_Constants.PLATFORM + os);
				String version = cap.getVersion().toString();
				Assertions.methodMessage(Decorist_Constants.VERSION + browser1 + Decorist_Constants.BROWSER_IS + version);
				clearCookies();
				Assertions.methodMessage(Decorist_Constants.BROWSER + browser1 + Decorist_Constants.SUCCESS_INVOKE);
				setUrl(testUrl);
			}
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.UNABLE_LAUNCH_BRWSR, true);
		}
		return (WebDriver) localThread.get();
	}

	/**
	 * Update Proxy Setting
	 * 
	 * @author RahulSharma
	 */
	public void runProxy() {
		try {
			String[] command = { "cmd.exe", "/C", "Start",
					System.getProperty(Decorist_Constants.USER_DIR) + Decorist_Constants.PATH_PROXY_FILE };
			@SuppressWarnings("unused")
			Process prs = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			LOG.info(Decorist_Constants.ERROR, e);
		}
	}

	/**
	 * Creates report directory
	 * 
	 * @author RahulSharma
	 */
	public void createReportsDirectory() {

		String destLocation = "R:\\QA\\QA-01 Structure\\Automation\\Intersoft\\Automation Team";
		File theDir1 = new File(destLocation);
		if (!theDir1.exists()) {
			LOG.info("creating directory: ");
			boolean result = theDir1.mkdir();
			if (result) {
				LOG.info("Execution_Reports DIR created");
			}
		}
		File theDir = new File(Assertions.snapshotFolder);
		if (!theDir.exists()) {
			LOG.info("creating directory: ");
			boolean result = theDir.mkdir();
			if (result) {
				LOG.info("DIR created");
			}
		}
	}

	/**
	 * Clears Browsers all cookies
	 * 
	 * @author RahulSharma
	 */
	public void clearCookies() {
		try {
			if (getDriver() != null) {
				getDriver().manage().deleteAllCookies();
				Assertions.methodMessage(Decorist_Constants.BRWSR_COOKIE_SUCCCFLY_CLERD);
			} else
				logReport(false, Decorist_Constants.NO_BRWSR_DEL_COOKIE, false);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.UNABLE_CLER_COOKIE, false);
		}
	}

	/**
	 * delete Cookie Name
	 * 
	 * @param cookieName
	 * @author RahulSharma
	 */

	public void deleteCookieNamed(String cookieName) {
		try {
			getDriver().manage().deleteCookieNamed(cookieName);
			Assertions.methodMessage("PASS: deleteCookieName, deleted: " + cookieName);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, "FAIL: deleteCookieName, cookie: " + cookieName, false);
		}
	}

	/**
	 * To Add new cookies by value
	 * 
	 * @param name
	 * @param value
	 * @author RahulSharma
	 */
	public void addNewCookie(String name, String value) {
		try {
			Cookie cook = new Cookie(name, value, value);
			getDriver().manage().addCookie(cook);
			Assertions.methodMessage("PASS: addNewCookie, deleted: " + name + " = " + value);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, "FAIL: addNewCookie, cookie: " + name + " = " + value, true);
		}
	}

	/**
	 * To Change cookies by value
	 * 
	 * @param cookieName
	 * @param cookieAttribute
	 * @param newCookieAttributeValue
	 * @author RahulSharma
	 */

	public void changeCookieValue(String cookieName, String cookieAttribute, String newCookieAttributeValue) {
		try {
			for (Cookie cook : getDriver().manage().getCookies()) {
				LOG.info(cook.getName());
			}
			Cookie cookie = getDriver().manage().getCookieNamed(cookieName);
			Cookie newCookie = null;
			getDriver().manage().deleteCookieNamed(cookieName);
			if (cookieAttribute.equalsIgnoreCase("Content")) {
				newCookie = new Cookie(cookie.getName(), newCookieAttributeValue, cookie.getDomain(), cookie.getPath(),
						cookie.getExpiry());
			}
			if (cookieAttribute.equalsIgnoreCase("Expires")) {
				int changeDate = Integer.parseInt(newCookieAttributeValue);
				Calendar cal = Calendar.getInstance();
				cal.add(6, changeDate);
				newCookie = new Cookie(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath(),
						cal.getTime());
			}
			getDriver().manage().deleteCookieNamed(cookieName);
			getDriver().manage().addCookie(newCookie);
			getDriver().navigate().refresh();
			if (cookieAttribute.equalsIgnoreCase("Content")) {
				Assertions.methodMessage("PASS: changeCookieValue: Changed " + cookieAttribute + " in cookie name: "
						+ newCookie.getName() + " from: " + cookie.getValue() + " to: " + newCookie.getValue());
			} else {
				Assertions.methodMessage("PASS: changeCookieValue: Changed " + cookieAttribute + " in cookie name: "
						+ newCookie.getName() + " from: " + cookie.getExpiry().toString() + " to: "
						+ newCookieAttributeValue);
			}
			for (Cookie cook : getDriver().manage().getCookies())
				LOG.info(cook.getName());
		} catch (Throwable t) {
			LOG.info(Decorist_Constants.ERROR, t);
			logReport(false, "FAIL: changeCookieValue: Failed to changed " + cookieAttribute + " in cookie name: "
					+ cookieName + " to: " + newCookieAttributeValue, false);
		}
	}

	/**
	 * To Verify cookies Exist
	 * 
	 * @param name
	 * @Author RahulSharma
	 */
	public void verifyCookieExists(String name) {
		try {
			boolean flag = false;
			for (Cookie cook : getDriver().manage().getCookies()) {
				if (cook.getName().equals(name))
					flag = true;
			}
			if (flag)
				logReport(true, name + " cookie Exists", false);
			else
				logReport(false, name + " cookie does'nt Exists", false);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, "Unable to Verify Cookie Exists", false);
		}
	}

	/**
	 * Scroll Horizontally
	 * 
	 * @author RahulSharma
	 * @param elememt
	 */
	public void scrolHorizental(String elememt) {

		try {
			WebElement problematicElement = getDriver().findElement(By.xpath(elememt));
			((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView()", problematicElement);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
		}
	}

	/**
	 * To entering text in any field
	 * 
	 * @param elements
	 * @param index
	 * @param text
	 * @author RahulSharma
	 */
	public void input(List<WebElement> elements, int index, String text) {
		try {
			Assertions.methodMessage(Decorist_Constants.ENTER_TXT + text);
			(elements.get(index)).sendKeys(text);
			logReport(true, Decorist_Constants.TXT_ENT_SCUCCESSFLY + index + "," + Decorist_Constants.INPUT_VAL + text, true);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.FAIL_TXT_INDEX + index + "," + Decorist_Constants.INPUT_VAL + text, true);
		}
	}

	/**
	 * To clear and entering text in any field
	 * 
	 * @param elements
	 * @param index
	 * @author RahulSharma
	 */
	public void clearTextBoxValue(List<WebElement> elements, int index) {
		try {
			Assertions.methodMessage(Decorist_Constants.VRFY_CLR_TXTBOX_VAL);
			elements.get(index).clear();
			logReport(true, Decorist_Constants.VRFYD_TXTBOX_CLR_VAL + Decorist_Constants.AT_INDEX + index, true);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.FAILED_CLR_TXTBOX_VAL + Decorist_Constants.AT_INDEX + index, true);
		}
	}

	/**
	 * To focus on pop up window
	 * 
	 * @param windowNumber
	 * @author RahulSharma
	 */
	public void focusPopupWindow(int windowNumber) {
		try {
			Assertions.methodMessage(Decorist_Constants.VRFY_FOCUS_POPUP);
			this.parentWindowHandle = getDriver().getWindowHandle();
			String window = (String) getDriver().getWindowHandles().toArray()[windowNumber];
			getDriver().switchTo().window(window);
			logReport(true, Decorist_Constants.VRFYD_FOCUS_POPUP, true);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.EXEP_WHEN_CHNG_FOCUS_POPUP, true);
		}
	}

	/**
	 * To focus on pop up window
	 * 
	 * @param title
	 * @author RahulSharma
	 */
	public void focusPopupWindow(String title) {
		try {
			Assertions.methodMessage(Decorist_Constants.VRFY_FOCUS_POPUP);
			this.parentWindowHandle = getDriver().getWindowHandle();
			for (String winHandle : getDriver().getWindowHandles()) {
				getDriver().switchTo().window(winHandle);
				if (getDriver().getTitle().contains(title))
					return;
			}
			logReport(true, Decorist_Constants.VRFYD_FOCUS_POPUP, true);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.EXEP_WHEN_CHNG_FOCUS_POPUP, true);
		}
	}

	/**
	 * To switching between frame
	 * 
	 * @Author RahulSharma
	 */

	public void focusIframe() {
		try {
			Assertions.methodMessage(Decorist_Constants.VRFY_FOCUS_IFRAME);
			this.parentWindowHandle = getDriver().getWindowHandle();
			getDriver().switchTo().frame(getDriver().findElement(By.tagName("iframe")));
			logReport(true, Decorist_Constants.VRFYD_FOCUS_IFRAME, true);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.EXEP_WHILE_CHNG_FOCUS_IFRAME + e.getMessage(), true);
		}
	}

	/**
	 * To switching between frame
	 * 
	 * @param frameNumber
	 * @Author RahulSharma
	 */
	public void focusIframe(int frameNumber) {
		try {
			Assertions.methodMessage(Decorist_Constants.VRFY_FOCUS_IFRAME);
			this.parentWindowHandle = getDriver().getWindowHandle();
			getDriver().switchTo().frame(getDriver().findElements(By.tagName("iframe")).get(frameNumber));
			logReport(true, Decorist_Constants.VRFYD_FOCUS_IFRAME, true);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.EXEP_WHILE_CHNG_FOCUS_IFRAME + e.getMessage(), true);
		}
	}

	/**
	 * To hover on any element
	 * 
	 * @param element
	 * @Author RahulSharma
	 */
	public void hover(WebElement element) {
		try {
			Assertions.methodMessage(Decorist_Constants.VRFY_HOVER_ELE);
			waitForElementPresent(element, "Element");
			highlightObject(element);
			Actions action = new Actions(getDriver());
			action.moveToElement(element).build().perform();
			Thread.sleep(10000);
			logReport(true, Decorist_Constants.VRFYD_HOVER_ELE, true);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.FAILED_HOVER_ELE + e.getMessage(), true);
		}
	}

	/**
	 * To handle alert
	 * 
	 * @param option
	 * @Author RahulSharma
	 */
	public void handleAlert(String option) {
		try {
			Assertions.methodMessage("Verify handle alert");
			Alert alert = getDriver().switchTo().alert();
			if ((option.equalsIgnoreCase("accept")) || (option.equalsIgnoreCase("y"))) {
				alert.accept();
			} else if ((option.equalsIgnoreCase("cancel")) || (option.equalsIgnoreCase("c"))) {
				alert.dismiss();
			}
			logReport(true, "Verified Alert Handeled choosing option" + option, true);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, " Failed unable to handle Alert", true);
		}
	}

	/**
	 * Close Button in pop up window
	 * 
	 * @param element
	 * @param name
	 * @Author RahulSharma
	 */
	public void clickCloseBtn(WebElement element, String name) {
		try {
			highlightObject(element);
			element.click();
			Assertions.methodMessage("Verified that " + name + " is clicked successfully");
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, " Failed to click on " + name, true);
		}
	}

	/**
	 * Creates instance of WebDriver with defines capabilities and Profile
	 * firefox,firefoxnocookies,firefoxnojavascript,firefoxnocookiesnojavascript
	 * ,firefoxprofile[profileaddress]
	 * chrome,chromenocookies,chromenojavascript,chromenocookiesnojavascript,
	 * chromeprofile[profileaddress]
	 * 
	 * @param browser
	 * @param browser1
	 * @param gridMachine
	 * @return WebDriver
	 * @Author RahulSharma
	 */
	protected WebDriver createInstance(String browser, String gridMachine, String browser1) {
		try {
			WebDriver driver = null;
			boolean enableGridParam = gridMachine.equalsIgnoreCase("true");
			if (enableGridParam) {
				//COMMENT CLEAR COOKIES FOR EDGE BROWSER EXECUTION
				if (browser.toLowerCase().contains(Decorist_Constants.EDGE)
						|| browser1.toLowerCase().contains(Decorist_Constants.EDGE)) {
						DesiredCapabilities edgeCapabilities = DesiredCapabilities.edge();
						edgeCapabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
						edgeCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
						System.setProperty(Decorist_Constants.WEBDRIVER_EDGE,
						System.getProperty(Decorist_Constants.USER_DIR) + Decorist_Constants.PATH_MICROSOFT_DRVIR_EXE);
						try {
						driver = new EdgeDriver(edgeCapabilities);
						browserCounterMSEdge = "edge";
						} catch (Exception e1) {
						LOG.info(Decorist_Constants.ERROR, e1);
						Assertions.testStepContinue(false, Decorist_Constants.CREATE_INSTANCE + Decorist_Constants.UNABLE_START);
						}
				}
				else if (browser.toLowerCase().contains(Decorist_Constants.FIREFOX)
						|| browser1.toLowerCase().contains(Decorist_Constants.FIREFOX)) {
					DesiredCapabilities firefoxCapabilities = DesiredCapabilities.firefox();
					firefoxCapabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
					firefoxCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					System.setProperty(Decorist_Constants.WEBDRIVER_GECKO,
							System.getProperty(Decorist_Constants.USER_DIR) + Decorist_Constants.PATH_GECKO_DRVIR_EXE);
					try {
						driver = new FirefoxDriver(firefoxCapabilities);
						browserCounterFF = "firefox";
					} catch (Exception e1) {
						LOG.info(Decorist_Constants.ERROR, e1);
						Assertions.testStepContinue(false, Decorist_Constants.CREATE_INSTANCE + Decorist_Constants.UNABLE_START);
					}
				} else if (browser.toLowerCase().contains(Decorist_Constants.CHROME)
						|| browser1.toLowerCase().contains(Decorist_Constants.CHROME)) {
					ChromeOptions chrome = new ChromeOptions();
					DesiredCapabilities chromeCapabilities = new DesiredCapabilities();
					System.setProperty(Decorist_Constants.WEBDRIVER_CHROME,
							System.getProperty(Decorist_Constants.USER_DIR) + Decorist_Constants.PATH_CHROME_DRVIR_EXE);
					chrome.addArguments(Decorist_Constants.STRT_MAXMIZD);
					chromeCapabilities.setCapability(ChromeOptions.CAPABILITY, chrome);
					try {
						driver = new ChromeDriver(chromeCapabilities);
						browserCounterChrome = "chrome";
					} catch (Exception e1) {
						LOG.info(Decorist_Constants.ERROR, e1);
						Assertions.testStepAbort(false, Decorist_Constants.CREATE_INSTANCE + Decorist_Constants.UNABLE_START);
						LOG.info(e1);
					}
				} else if (browser.toLowerCase().contains(Decorist_Constants.SAFARI)
						|| browser1.toLowerCase().contains(Decorist_Constants.SAFARI)) {
					new DesiredCapabilities();
					DesiredCapabilities safariCapabilities = DesiredCapabilities.safari();
					try {
						driver = new SafariDriver(safariCapabilities);
					} catch (Exception e1) {
						LOG.info(Decorist_Constants.ERROR, e1);
						Assertions.testStepContinue(false, Decorist_Constants.CREATE_INSTANCE + Decorist_Constants.UNABLE_START);
						LOG.info(e1);
					}
				} else if (browser.toLowerCase().contains(Decorist_Constants.IE)
						|| browser1.toLowerCase().contains(Decorist_Constants.IE)) {
					DesiredCapabilities internetexplorer = new DesiredCapabilities();
					DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
					internetexplorer.setCapability(
							InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
					ieCapabilities.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
					ieCapabilities.setCapability("nativeEvents", false);
					ieCapabilities.setCapability("unexpectedAlertBehaviour", "accept");
					ieCapabilities.setCapability("ignoreProtectedModeSettings", true);
					ieCapabilities.setCapability("disable-popup-blocking", true);
					ieCapabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
					ieCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					System.setProperty(Decorist_Constants.WEBDRIVER_IE,
							System.getProperty(Decorist_Constants.USER_DIR) + Decorist_Constants.PATH_IE_DRVIR_EXE);
					try {
						driver = new InternetExplorerDriver(ieCapabilities);
						browserCounterIe = "ie";
					} catch (Exception e1) {
						LOG.info(Decorist_Constants.ERROR, e1);
						Assertions.testStepAbort(false, Decorist_Constants.CREATE_INSTANCE + Decorist_Constants.UNABLE_START);
						LOG.info(e1);
					}
				}
				driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
				driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
				driver.manage().window().maximize();
				localThread.set(driver);
			}
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			Assertions.testStepAbort(false, Decorist_Constants.CREATE_INSTANCE + Decorist_Constants.UNABLE_START_BRWSR);
		}
		return (WebDriver) localThread.get();
	}

	/**
	 * get unique Id
	 * 
	 * @return uniqueid
	 * @Author RahulSharma
	 */
	public String getUniqueID() {
		String uniqueId = "";
		try {
			UUID id = UUID.randomUUID();
			uniqueId = id.toString();
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
		}
		return uniqueId;
	}

	/**
	 * verify current Url
	 * 
	 * @param url
	 * @Author RahulSharma
	 */
	public void verifyCurrentUrl(String url) {
		Assertions.methodMessage(Decorist_Constants.VRFY_URL);
		if (getDriver().getCurrentUrl().equalsIgnoreCase(url)) {
			Assertions.methodMessage(Decorist_Constants.VRFYD_URL);
		} else
			Assertions.methodMessage(Decorist_Constants.FAILED_URL);
	}

	/**
	 * verify Element is absent
	 * 
	 * @param locator
	 * @param name
	 * @Author RahulSharma
	 */
	public void verifyElementIsAbsent(String locator, String name) {
		try {
			WebElement element = getDriver().findElement(By.xpath(locator));
			Assertions.methodMessage(
					MessageFormat.format(Decorist_Constants.FAIL_ELEMENT_FOUND, element.getAttribute("name")));
		} catch (NoSuchElementException e) {
			LOG.info(Decorist_Constants.ERROR, e);
			Assertions.methodMessage(MessageFormat.format(Decorist_Constants.VRFY_ELE_NOT_PAGE, name));
		}
	}

	/**
	 * write excel
	 * 
	 * @param users
	 * @author RahulSharma
	 */
	public static void writexcel(List<String> users) {
		File file = new File(System.getProperty(Decorist_Constants.USER_DIR) + "\\Resource\\Data\\Users.xlsx");
		Workbook workbook = null;
		Sheet sheet = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			workbook = WorkbookFactory.create(fis);
			sheet = workbook.getSheetAt(0);
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_hh-mm-ss");
			String time1 = dateFormat.format(now);
			Row row = null;
			int rowCount = 0;
			for (String email : users) {
				rowCount = sheet.getLastRowNum();
				if (rowCount == 0 && sheet.getRow(rowCount) == null) {
					row = sheet.createRow(rowCount);
				} else {
					row = sheet.createRow(rowCount + 1);
				}
				LOG.info("rowCount " + (rowCount + 1));
				Cell emailCell = row.createCell(0, Cell.CELL_TYPE_STRING);
				Cell timeCell = row.createCell(1, Cell.CELL_TYPE_STRING);
				emailCell.setCellValue(email);
				timeCell.setCellValue(time1);
			}
			fis.close();
			FileOutputStream outputStream = new FileOutputStream(file);
			LOG.info(workbook.getSheetAt(0).getSheetName() + "****************");
			workbook.write(outputStream);
			outputStream.close();
			workbook.close();
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
		}
	}

	/**
	 * Create a folder (if not exists) under Downloads directory
	 * 
	 * @param folderName
	 * @author RahulSharma
	 */
	public void createFolder(String folderName) {
		String filePath = Decorist_Constants.C_USERS + System.getProperty(Decorist_Constants.USER_NAME) + Decorist_Constants.DWNDLS;
		try {
			File file = new File(filePath + "\\" + folderName);
			if (!file.exists()) {
				if (file.mkdir()) {
					LOG.info("Directory is created!");
				} else {
					LOG.info("Failed to create directory!");
				}
			}
		} catch (Exception ex) {
			LOG.info(Decorist_Constants.ERROR, ex);
		}
	}

	/**
	 * To generate a random alpha numeric string
	 * 
	 * @param strLength
	 * @return String
	 * @author RahulSharma
	 */
	public String generateRandomString(int strLength) {
		StringBuilder strBuilder = new StringBuilder(strLength);
		try {
			final String ab = Decorist_Constants.ALPHANUM;
			Random rnd = new Random();
			for (int index = 0; index < strLength; index++)
				strBuilder.append(ab.charAt(rnd.nextInt(ab.length())));
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.FAILED + Decorist_Constants.NOT_ABLETO + getCallerMtdName(), true);
		}
		return strBuilder.toString();
	}

	/**
	 * Incrementing String Value
	 * 
	 * @author RahulSharma
	 * @param strVal
	 * @return String
	 */
	public String stringValue(int strVal) {
		String val = "Auto" + strVal;
		return val;
	}

	/**
	 * Incrementing Value
	 * 
	 * @author RahulSharma
	 * @param val
	 * @return int
	 */

	public int numberVal(int val) {
		int number = 234765290 + val;
		return number;
	}

	/**
	 * Rondom Student_edu_Id String generator
	 * 
	 * @author RahulSharma
	 * @param id
	 * @return String
	 */

	public static String studentEduId007(int id) {
		String email = "EFAuto" + id + "@follett.edu";
		return email;
	}

	/**
	 * LMS_ID String generator
	 * 
	 * @author RahulSharma
	 * @param id
	 * @return String
	 */
	public static String lmsId9931(int id) {
		String lmsId = "AEFLMS9931" + id;
		LOG.info(lmsId);
		return lmsId;
	}

	/**
	 * SIS_ID String generator
	 * 
	 * @author RahulSharma
	 * @param id
	 * @return String
	 */
	public static String sisId9931(int id) {
		String sisId = "AEFSIS9931" + id;
		return sisId;
	}

	/**
	 * Random Student_edu_Id String generator
	 * 
	 * @author RahulSharma
	 * @param id
	 * @return String
	 */

	public static String studentEduId9931(int id) {
		String email = "EFAutoParent" + id + "@follett.edu";
		return email;
	}

	/**
	 * LMS_ID String generator
	 * 
	 * @param id
	 * @return String
	 * @author RahulSharma
	 */
	public static String lmsId007(int id) {
		String lmsId = "AEFLMS" + id;
		LOG.info(lmsId);
		return lmsId;
	}

	/**
	 * SIS_ID String generator
	 * 
	 * @author RahulSharma
	 * @param id
	 * @return String
	 */
	public static String sisId007(int id) {
		String sisId = "AEFSIS" + id;
		return sisId;
	}
	/**
	 * To convert XML String to List
	 * 
	 * @param levelString
	 * @author RahulSharma
	 */
	public List<String> convertLevelStrToList(String levelString) {
		List<String> levelList = new ArrayList<String>();
		try {
			String levelStr[] = levelString.split("\\r?\\n");
			for (int index = 1; index < levelStr.length - 1; index++) {
				levelStr[index] = levelStr[index].trim();
				levelList.add(levelStr[index]);
			}
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.FAILED + Decorist_Constants.NOT_ABLETO + getCallerMtdName(), true);
		}
		return levelList;
	}
}