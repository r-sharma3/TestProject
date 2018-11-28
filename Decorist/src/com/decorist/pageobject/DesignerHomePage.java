/***********************************Header Start*********************************************************************************
 * Application/ Module Name                      	   : eFollett/FMS
 * Class Name                                  		   : HomePage
 * Owner                                                : AutomationTeam
 ***********************************************************************
 * Creation /Modification Log: 
 * Date                     By                                Notes                                    
 ---------                ----------                      ---------
 * 11/30/2015			 AutomationTeam				 Initial draft
 * 
 ***********************************************************************
 * Review/Feedback Log: 
 * Date                     By                                Notes                                    
 * 
 ***********************************************************************
 * Functional Test Coverage Description   			   : Identified and defined all web elements in Home page													   
 ***********************************Header End*********************************************************************************/
package com.decorist.pageobject;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.How;

public class DesignerHomePage {

		@FindBy(how=How.ID,using="header-board-builder")
		public static WebElement lnk_createBoard;
		
		@FindBy(how=How.ID,using="header-boards")
		public static WebElement lnk_yourBoards;
		
		@FindBy(how=How.LINK_TEXT,using="Your Projects")
		public static WebElement lnk_yourProjects;
		
		@FindBy(how=How.CSS,using=".black-inverse")
		public WebElement elementHomeLogInButton;
		
		@FindBy(how=How.ID,using="account-content")
		public WebElement elementLogInPopUp;
		
		@FindBy(how=How.CSS,using="input.fb-email-form")
		public WebElement elementInputEmailField;
		
		@FindBy(how=How.CSS,using="input.fb-psw-form")
		public WebElement elementInputPasswordField;
		
		@FindBy(how=How.ID,using="email-btn")
		public WebElement elementLogInButton;
		
		//@FindBys(CSS = "div.designer-name")
		//List<WebElements> elementDesignerName;
		
		@FindBys(@FindBy(css="div.designer-name"))
        private List<WebElement> elementDesignerName;

        public List<WebElement> getAllData() {
            return elementDesignerName;
        }
        
        public Boolean verifyDesignerExists(String designerName)
    	{
    		for (WebElement row : elementDesignerName) 
    		{
    			if(row.getText().equals(designerName))
    			{
    				return true;
    			}
    		}
    		return false;
    }
}