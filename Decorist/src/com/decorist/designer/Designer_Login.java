package com.decorist.designer;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.decorist.businessrules.BusinessFunctions;
import com.decorist.businessrules.Decorist_Constants;
import com.org.core.Assertions;
import com.org.core.Retry;

@Listeners(com.org.core.Report.class)
public class Designer_Login extends BusinessFunctions {
	@Test(retryAnalyzer = Retry.class)
	public void nuRentalDigitalGmStoreGC() {
		try {
			Assertions.initExtentReport(this.getClass().getSimpleName(), true);
			envinfo();
			Assertions.testStepMessage(Decorist_Constants.STEP_1 + "Launch Decorist URL");
			Assertions.testStepMessage(Decorist_Constants.STEP_2 + "Login to the Application as Designer");
			loginAsDesigner();
		} catch (Exception e) {
			logReport(false, Decorist_Constants.FAILED + e.getMessage(), true);
		}
	}
}
