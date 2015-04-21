package com.citrix.testObject.g2m.piranha;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testobject.piranha.DesiredCapabilities;
import org.testobject.piranha.TestObjectPiranha;

import com.citrix.shared.BaseTest;
import com.citrixonline.piranha.COLTimeUtils;
import com.citrixonline.piranha.ControlType;
import com.citrixonline.piranha.PiranhaAssertionError;
import com.citrixonline.piranha.androidclient.AndroidClientFactory;
import com.citrixonline.piranha.androidclient.PiranhaAndroidClient;
import com.citrixonline.piranha.androidclient.Robotium.RobotiumClient;
import com.google.gson.GsonBuilder;
import com.thoughtworks.selenium.Wait;

public class FirstTest extends BaseTest{
	
	  private static final String DEVICEID = "Samsung_Galaxy_S5_real";
	private TestObjectPiranha testObjectPiranha = null;
	  private PiranhaAndroidClient c = null;

	@BeforeClass
	  public void setup() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("testobject_api_key", TESTOBJECT_APIKEY);
        capabilities.setCapability("testobject_app_id", "8");
        capabilities.setCapability("testobject_framework_app_id", "7");
        capabilities.setCapability("testobject_device", DEVICEID);
        capabilities.setCapability("testobject_suite_name" , "G2M Suite");
        	capabilities.setCapability("testobject_test_name" , "FirstTest");
        	
        Map<String, String> piranhaCaps = new HashMap<String, String>();
        piranhaCaps.put("className", "com.citrixonline.universal.ui.activities.LauncherActivity");
        piranhaCaps.put("intent",
                "com.citrixonline.piranha.androidserver/com.citrixonline.piranha.androidserver.PiranhaAndroidInstrumentation");
        piranhaCaps.put("packageName", "com.citrixonline.android.gotomeeting");

        capabilities.setCapability("piranha_params", new GsonBuilder().create().toJson(piranhaCaps));

        TestObjectPiranha testObjectPiranha = null;

        logger.info("Getting testObjectPiraha with capabilities fror Device : %s" , DEVICEID);
        testObjectPiranha = new TestObjectPiranha(capabilities);

        int port = testObjectPiranha.getPort();
        //final PiranhaAndroidClient c = new PiranhaAndroidClient(ip, 7100, true);
        logger.info("Port is: " + port);
        
        c = new PiranhaAndroidClient("localhost", port, true);

        logger.info("Wait for 30 Seconds ");
        COLTimeUtils.sleep(30*1000);
	  }
	
	  @Test
	  public void FirstTest(){
	        boolean ret1 = c.robotium().waiter().waitForViewToBeEnabled(ControlType.TEXT, "</#JoinMeetingId/>", 30);
	        if(ret1){

	        		takeScreenShot();
	        		logger.info("Clear Join Meeting TextBox");
		        c.robotium().setter().clearEditText("</#JoinMeetingId/>");
      			
		        logger.info("Enter Join Meeting TextBox");
		        c.robotium().setter().setText("</#JoinMeetingId/>", "555-000-000");
		        
	        }
	        	
			logger.info("Wait for Error Dialog");		
			String errmsg = "if wait fails restart the instrumentaion \n 'adb -d shell am instrument -w \n -e className com.citrixonline.universal.ui.activities.LauncherActivity \n -e pkgName com.citrixonline.android.gotomeeting com.citrixonline.piranha.androidserver/com.citrixonline.piranha.androidserver.PiranhaAndroidInstrumentation \n";
			

			new Wait() {
				
				@Override
				public boolean until() {
			        logger.info("Click Join Image Button");
			        c.robotium().clicker().clickImageView("</#JoinMeetingButton/>");
			        COLTimeUtils.sleep(1*1000);
					return c.robotium().waiter()
							.waitForViewToBeEnabled(ControlType.BUTTON, "OK", 10);
				}
			}.wait("Error Dialog did not show up " + errmsg, 60*1000, 10*1000);;
  			COLTimeUtils.sleep(1*1000);
			takeScreenShot();


			logger.info("Click Ok Button in Error Dialog");
			c.robotium().clicker().clickButton("OK");
	  }
	  
	  @AfterClass
	  public void tearDown() {
          if (testObjectPiranha != null) {
        	  	logger.info("Closing TestObject Session", testObjectPiranha.getSessionID());
              testObjectPiranha.close();
          }
	  }
	  
	private void takeScreenShot() {
	    try {
	    		c.robotium().utils().takeScreenShot();
	    } catch (Exception e) {
	    		logger.error("Unable to Take ScreenShot - Exception");
	    } catch (PiranhaAssertionError e1){
	    		logger.error("Unable to Take ScreenShot - PiranhaAssertionError");
	    }
	}
	
}
