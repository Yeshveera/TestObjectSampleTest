package com.citrix.testObject.g2m.piranha;

import groovy.transform.Synchronized;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;
import org.testobject.piranha.DesiredCapabilities;
import org.testobject.piranha.TestObjectPiranha;

import com.citrix.shared.BaseLogger;
import com.citrix.shared.ImageUtils2;
import com.citrixonline.piranha.COLTimeUtils;
import com.citrixonline.piranha.ControlType;
import com.citrixonline.piranha.PiranhaAssertionError;
import com.citrixonline.piranha.androidclient.PiranhaAndroidClient;
import com.google.gson.GsonBuilder;
import com.thoughtworks.selenium.Wait;

public class FirstTest extends TestObjectAndroidTest{

	public TestObjectPiranha setup(String deviceID) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("testobject_api_key", TESTOBJECT_APIKEY);
        capabilities.setCapability("testobject_app_id", "8");
        capabilities.setCapability("testobject_framework_app_id", "7");
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
        TestObjectPiranha testObjectPiranha = new TestObjectPiranha(capabilities);
        
        return testObjectPiranha;
	  }
	
	  @Test(dataProvider = "getDeviceID")
	  public void FirstG2MAndroidTest(String deviceID){
		TestObjectPiranha p = null;
		try {
			
			synchronized (deviceID) {
				logger.clearAllTags();
				logger.addTag(deviceID);
			}
			
			p = setup(deviceID);
			runTest(p);
		}finally{
			tearDown(p);
		}
	  }
	  
	  private void runTest(TestObjectPiranha p){
		  	
	        final PiranhaAndroidClient c  = p.getAndroidClient(30*1000);
			boolean ret1 = c  .robotium().waiter().waitForViewToBeEnabled(ControlType.TEXT, "</#JoinMeetingId/>", 30);
	        if(ret1){

	        		takeScreenShot(c);
	        		logger.info("Clear Join Meeting TextBox");
		        c.robotium().setter().clearEditText("</#JoinMeetingId/>");
    			
		        logger.info("Enter Join Meeting TextBox");
		        c.robotium().setter().setText("</#JoinMeetingId/>", "555-000-000");
      			takeScreenShot(c);

	        }
	        	
	        takeScreenShot(c);
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
			takeScreenShot(c);


			logger.info("Click Ok Button in Error Dialog");
			c.robotium().clicker().clickButton("OK");
	  }

	  public void tearDown(TestObjectPiranha testObjectPiranha) {
          if (testObjectPiranha != null) {
        	  	logger.info("Closing TestObject Session", testObjectPiranha.getSessionID());
              testObjectPiranha.close();
          }
	  }
	
	private void openScreenShot(File f) {
		Desktop dt = Desktop.getDesktop();
	    try {
			dt.open(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public File takeScreenShot(PiranhaAndroidClient c) {
		String imgstr = null;
		try {
			imgstr = c.robotium().utils().takeScreenShot();
		} catch (Exception e) {
			logger.error("Unable to Take ScreenShot - Exception");
			return null;
		} catch (PiranhaAssertionError e1) {
			logger.error("Unable to Take ScreenShot - PiranhaAssertionError");
//			e1.printStackTrace();
			return null;
		}
		if(StringUtils.isBlank(imgstr)){
			return null;
		}
		BufferedImage newImg = ImageUtils2.decodeToImage(imgstr);
		try {
			File f = saveFile(newImg);		
			//openScreenShot(f);
			return f;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.error("Screenshot failed");
		return null;
	}
		
	private File saveFile(BufferedImage screen) throws IOException{

		String fileName = "";
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		File imgFile = new File(getScreenshotsDir() , fileName + formatter.format(calendar.getTime()) + ".png");
        try {
			ImageIO.write(screen, "png", imgFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(imgFile.exists()){
			logger.debug("Screenshot can be found here:  %s", imgFile.getAbsolutePath());
		} else {
			logger.error("Error in taking screenshot");
		}
		BaseLogger.getLogger(this.getClass()).reportScreenShotToTestNgReport(imgFile);
		return imgFile;

	}
	
}
