package com.citrix.testObject.g2m.piranha;


import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testobject.piranha.DesiredCapabilities;
import org.testobject.piranha.TestObjectPiranha2;

import com.citrixonline.piranha.COLTimeUtils;
import com.citrixonline.piranha.ControlType;
import com.citrixonline.piranha.androidclient.PiranhaAndroidClient;
import com.google.gson.GsonBuilder;
import com.thoughtworks.selenium.Wait;

public class FirstTest extends TestObjectAndroidTest{

	public TestObjectPiranha2 setup(String deviceID) {
		boolean isAvailable = checkDeviceIsAvailable(deviceID);
		if(isAvailable){
			logger.info("Device '%s' is available", deviceID);
		} else {
			logger.error("Device '%s' is Unavailable" , deviceID);
			fail("Device unavailable");
		}
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("testobject_api_key", TESTOBJECT_APIKEY);
        capabilities.setCapability("testobject_app_id", "8");
        capabilities.setCapability("testobject_framework_app_id", "9"); // id 7 should work too
        capabilities.setCapability("testobject_device", deviceID);
        capabilities.setCapability("testobject_suite_name" , "G2M");
        	capabilities.setCapability("testobject_test_name" , "FirstG2MAndroidTest");
        	
        Map<String, String> piranhaCaps = new HashMap<String, String>();
        piranhaCaps.put("className", "com.citrixonline.universal.ui.activities.LauncherActivity");
        piranhaCaps.put("intent",
                "com.citrixonline.piranha.androidserver/com.citrixonline.piranha.androidserver.PiranhaAndroidInstrumentation");
        piranhaCaps.put("packageName", "com.citrixonline.android.gotomeeting");

        capabilities.setCapability("piranha_params", new GsonBuilder().create().toJson(piranhaCaps));

        logger.info("Getting Device '%s' from TestObject" , deviceID);
        TestObjectPiranha2 testObjectPiranha = new TestObjectPiranha2(capabilities);
        setTestObjectPiranha(testObjectPiranha);
        return testObjectPiranha;
	  }
	


	@Test(dataProvider = "getDeviceID")
	  public void FirstG2MAndroidTest(String deviceID){
		TestObjectPiranha2 testObjectPiranha = null;
		logger.clearAllTags();
		logger.addTag(deviceID);			
		testObjectPiranha = setup(deviceID);
		runTest(testObjectPiranha);		
	  }

	private void runTest(TestObjectPiranha2 testObjectPiranha){
		  	
	        final PiranhaAndroidClient c  = testObjectPiranha.getAndroidClient();
			boolean ret1 = c.robotium().waiter().waitForViewToBeEnabled(ControlType.TEXT, "</#JoinMeetingId/>", 30);
	        if(ret1){
	        		testObjectPiranha.takeScreenShot();
	        		logger.info("Clear Join Meeting TextBox");
		        c.robotium().setter().clearEditText("</#JoinMeetingId/>");
    			
		        logger.info("Enter Join Meeting TextBox");
		        c.robotium().setter().setText("</#JoinMeetingId/>", "555-000-000");
        		testObjectPiranha.takeScreenShot();
	        }
	        	
    			testObjectPiranha.takeScreenShot();
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
    			testObjectPiranha.takeScreenShot();


			logger.info("Click Ok Button in Error Dialog");
			c.robotium().clicker().clickButton("OK");
	  }


	


}
