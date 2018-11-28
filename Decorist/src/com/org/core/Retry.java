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
 * Functional Test Coverage Description   			   : Set the maximum try to execute failed test cases.														   
 ***********************************Header End*********************************************************************************/
package com.org.core;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {
	private int count = 0;
	private static int maxTry = 0;
	@Override
	public boolean retry(ITestResult iTestResult) {
		if (!iTestResult.isSuccess()) { 
			if (count < maxTry) { 
				count++; 
				iTestResult.setStatus(ITestResult.FAILURE); 
				return true; 
			} else {
				iTestResult.setStatus(ITestResult.FAILURE); 
			}
		} else {
			iTestResult.setStatus(ITestResult.SUCCESS); 
		}
		return false;
	}
}
