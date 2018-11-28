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
 * Functional Test Coverage Description   			   : Used to execute all failed test cases after the completion of test suite execution														   
 ***********************************Header End*********************************************************************************/
package com.decorist.businessrules;

import java.util.ArrayList;
import java.util.List;
import org.testng.TestNG;

public class Runner {
	public static void main(String[] args) {
		TestNG runner = new TestNG();
		List<String> list = new ArrayList<>();
		list.add(System.getProperty(Decorist_Constants.USER_DIR) + "\\test-output\\Parallel test suite\\testng-failed.xml");
		runner.setTestSuites(list);
		runner.run();
	}
}
