/************************************Header Start*********************************************************************************
 * Application/ Module Name                      	   : Decorist/Designer
 * Test/ Function Name                                 : All reusable methods
 * Owner                                               : AutomationTeam
 ***********************************************************************
 * Creation /Modification Log: 
 * Date                     By                              Notes                                    
			 ---------                ----------                       ---------
 *
 ***********************************************************************
 * Review/Feedback Log: 
 * Date                     By                              Notes                                    
			 ---------                 --------                   	   ----------
 * [Date]                   [Reviewer]                   [Brief description of the review/feedback comments]
 ***********************************************************************
 * Functional Test Coverage Description   			   : Defined functional reusable methods 														   
 ***********************************Header End*********************************************************************************/
package com.decorist.businessrules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
//import java.io.PrintStream;
//import java.text.MessageFormat;
import java.text.SimpleDateFormat;
//import java.util.ArrayList;
import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
import java.util.Locale;
//import java.util.Map;
//import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.Keys;
//import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.decorist.pageobject.DesignerHomePage;
//import com.google.common.collect.Sets;
//import com.org.core.Assertions;
import com.org.core.CoreFunctions;

public class BusinessFunctions extends CoreFunctions {
	static Logger LOG = Logger.getLogger(BusinessFunctions.class);

	/**
	 * define here all global variables
	 * 
	 */
	public boolean isMultipleCourse = false;
	String dATEFORMATNOW = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	SimpleDateFormat sdf = new SimpleDateFormat(dATEFORMATNOW, Locale.US);
	String format = "MMddyyyy";
	SimpleDateFormat sdf1 = new SimpleDateFormat(format);
	Date date = new Date();

	/**
	 * Create Excel data
	 * 
	 * @author 
	 * @param numberOfLinesDataRequired
	 * @param store
	 */
	public void createExcelData(int numberOfLinesDataRequired, int store) {
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		File xlsxFile = null;
		try {
			xlsxFile = new File(System.getProperty(Decorist_Constants.USER_DIR) + "\\Resource\\TestData\\"
					+ "Decorist_Constants.Student_EXCELFILENAME + Decorist_Constants.FILE_EXT");
			if (!xlsxFile.exists()) {
				XSSFWorkbook workbook = new XSSFWorkbook();
				workbook.createSheet("Decorist_Constants.Student_EXCELSHEETNAME");
				try {
					FileOutputStream output = new FileOutputStream(new File(System.getProperty(Decorist_Constants.USER_DIR)
							+ "\\Resource\\TestData\\" + "Decorist_Constants.Student_EXCELFILENAME + Decorist_Constants.FILE_EXT"));
					workbook.write(output);
					workbook.close();
					output.close();
				} catch (Exception e) {
					LOG.info(Decorist_Constants.ERROR, e);
				}
			} else {

				FileInputStream fin = new FileInputStream(System.getProperty(Decorist_Constants.USER_DIR)
						+ "\\Resource\\TestData\\" + "Decorist_Constants.Student_EXCELFILENAME" + "Decorist_Constants.FILE_EXT");
				XSSFWorkbook workbook1 = new XSSFWorkbook(fin);
				XSSFSheet sheet1 = workbook1.getSheet("Decorist_Constants.Student_EXCELSHEETNAME");
				int rowCount = sheet1.getLastRowNum();
				if (rowCount == 0) {
					Row r = sheet1.createRow(0);
					r.createCell(0).setCellValue("1.2");
					r.createCell(1).setCellValue("STUDENTREG");
					r.createCell(2).setCellValue(sdf.format(new Date()));
				}
				for (int index = rowCount + 1; index <= numberOfLinesDataRequired + rowCount;) {
					String email;
					String LMSID;
					String SISID;
					int storeNo = 007;
					if (storeNo == store) {
						email = studentEduId007(index);
						LMSID = lmsId007(index);
						SISID = sisId007(index);
					} else {
						email = studentEduId9931(index);
						LMSID = lmsId9931(index);
						SISID = sisId9931(index);
					}
					int num = numberVal(index);
					String txt = stringValue(index);
					while (sheet1.getRow(index - 1) != null) {
						index++;
					}
					Row row = sheet1.createRow(index - 1);
					row.createCell(0).setCellValue("A");
					row.createCell(1).setCellValue(7);
					row.createCell(2).setCellValue(SISID);
					row.createCell(3).setCellValue(txt);
					row.createCell(4).setCellValue("");
					row.createCell(5).setCellValue("EFAUTO");
					row.createCell(6).setCellValue(email);
					row.createCell(7).setCellValue(num);
					row.createCell(8).setCellValue("");
					row.createCell(9).setCellValue(LMSID);
					row.createCell(10).setCellValue(getdatafromXml("Store7Program"));
					row.createCell(11).setCellValue(getdatafromXml("Store7Term"));
					row.createCell(12).setCellValue("");
					row.createCell(13).setCellValue("S");
					row.createCell(14).setCellValue(getdatafromXml("USStreetAddress"));
					row.createCell(15).setCellValue("");
					row.createCell(16).setCellValue("");
					row.createCell(17).setCellValue(getdatafromXml("USCity"));
					row.createCell(18).setCellValue(getdatafromXml("USState1"));
					row.createCell(19).setCellValue(Integer.parseInt(getdatafromXml("USZipCode1")));
					row.createCell(20).setCellValue("USA");
					row.createCell(21).setCellValue("DFLT");
					row.createCell(22).setCellValue("DFLT");
					row.createCell(23).setCellValue(getdatafromXml("Store7Course"));
					row.createCell(24).setCellValue(getdatafromXml("Store7Section"));
					row.createCell(25).setCellValue("");
					row.createCell(26).setCellValue("CO");
					row.createCell(27).setCellValue("");
					row.createCell(28).setCellValue("");
					row.createCell(29).setCellValue("S");
					row.createCell(30).setCellValue(sdf.format(new Date()));
				}
				fin.close();
				FileOutputStream output1 = new FileOutputStream(System.getProperty(Decorist_Constants.USER_DIR)
						+ "\\Resource\\TestData\\" + "Decorist_Constants.Student_EXCELFILENAME" + "Decorist_Constants.FILE_EXT");
				workbook1.write(output1);
				workbook1.close();
				output1.close();
			}
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
		}
	}

	/**
	 * To hover on element
	 * 
	 * @param element
	 * @author 
	 */
	public void hoverOnElement(WebElement element) {
		try {
			String javaScript = "var evObj = document.createEvent('MouseEvents');"
					+ "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
					+ "arguments[0].dispatchEvent(evObj);";
			((JavascriptExecutor) getDriver()).executeScript(javaScript, element);
		} catch (Exception e) {
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.FAILED + Decorist_Constants.NOT_ABLETO + getCallerMtdName(), true);
		}
	}
	
	public void loginAsDesigner() {
		try
		{
			getDriver().manage().window().maximize();
			getDriver().findElement(By.cssSelector(".black-inverse")).click();
			DesignerHomePage Homepage =PageFactory.initElements(getDriver(), DesignerHomePage.class);
			waitElementClickable(Homepage.elementHomeLogInButton, "Home Page Login Button");
			Thread.sleep(6000);
			click(Homepage.elementHomeLogInButton, "Home Page Login Button");
			Thread.sleep(6000);
			isElementPresent(Homepage.elementLogInPopUp);
			clearAndSendKey(Homepage.elementInputEmailField, "rahul.sharma+user@decorist.com");
			clearAndSendKey(Homepage.elementInputPasswordField, "Password12");
			click(Homepage.elementLogInButton, "Login Button");
		} catch (Exception e)
		{
			LOG.info(Decorist_Constants.ERROR, e);
			logReport(false, Decorist_Constants.FAILED + Decorist_Constants.NOT_ABLETO + getCallerMtdName(), true);
		}
	}
	
}
