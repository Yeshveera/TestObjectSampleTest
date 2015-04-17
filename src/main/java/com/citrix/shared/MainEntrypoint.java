package com.citrix.shared;

import com.citrixonline.piranha.COLTimeUtils;
import com.citrixonline.piranha.ControlType;
import com.citrixonline.piranha.androidclient.PiranhaAndroidClient;
import com.thoughtworks.selenium.Wait;
import com.thoughtworks.selenium.webdriven.commands.WaitForCondition;

public class MainEntrypoint {
	
	public static PiranhaAndroidClient c;
	static CustomLogger logger = CustomLogger.getLogger("Piranha Test");
			
	public static void main(String[] args) {
	       String ip = "localhost";
	        c = new PiranhaAndroidClient(ip, 7100, true);
	        boolean ret1 = c.robotium().waiter().waitForViewToBeEnabled(ControlType.TEXT, "</#JoinMeetingId/>", 30);
	        if(ret1){
	        		String imageStr = c.robotium().utils().takeScreenShot();
	        		logger.info("Image string : %s", imageStr);
	        		logger.info("Clear Join Meeting TextBox");
		        c.robotium().setter().clearEditText("</#JoinMeetingId/>");
        			
		        logger.info("Enter Join Meeting TextBox");
		        c.robotium().setter().setText("</#JoinMeetingId/>", "555-000-000");
		        
	        }
	        	
			logger.info("Wait for Error Dialog");		
			logger.info("if wait fails restart the insrtumentaion adb -d shell am instrument -w -e className com.citrixonline.universal.ui.activities.LauncherActivity -e pkgName com.citrixonline.android.gotomeeting com.citrixonline.piranha.androidserver/com.citrixonline.piranha.androidserver.PiranhaAndroidInstrumentation");

			new Wait() {
				
				@Override
				public boolean until() {
			        logger.info("Click Join Image Button");
			        c.robotium().clicker().clickImageView("</#JoinMeetingButton/>");
			        try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return c.robotium().waiter()
							.waitForViewToBeEnabled(ControlType.BUTTON, "OK", 5);
				}
			}.wait("Error Dialog did not show up", 60*1000, 2*1000);;
			

			logger.info("Click Ok Button in Error Dialog");
			c.robotium().clicker().clickButton("OK");
	        
	}
	
}
