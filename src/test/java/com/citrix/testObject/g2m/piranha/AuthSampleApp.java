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

public class AuthSampleApp extends TestObjectAndroidTest{

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
        capabilities.setCapability("testobject_app_id", "2");
        capabilities.setCapability("testobject_framework_app_id", "4"); 
        capabilities.setCapability("testobject_device", deviceID);
        capabilities.setCapability("testobject_suite_name" , "AuthSample");
            capabilities.setCapability("testobject_test_name" , "AuthSampleAppTest");
            
        Map<String, String> piranhaCaps = new HashMap<String, String>();
        piranhaCaps.put("className", "com.citrix.authsample1.AuthActivity");
        piranhaCaps.put("intent",
                "com.citrixonline.piranha.androidserver/com.citrixonline.piranha.androidserver.PiranhaAndroidInstrumentation");
        piranhaCaps.put("packageName", "com.citrix.authsample1");

        capabilities.setCapability("piranha_params", new GsonBuilder().create().toJson(piranhaCaps));

        logger.info("Getting Device '%s' from TestObject" , deviceID);
        TestObjectPiranha2 testObjectPiranha = new TestObjectPiranha2(capabilities);
        setTestObjectPiranha(testObjectPiranha);
        return testObjectPiranha;
      }

    @Test(dataProvider = "getDeviceID")
      public void AuthSampleAppTest(String deviceID){
        TestObjectPiranha2 testObjectPiranha = null;
        logger.clearAllTags();
        logger.addTag(deviceID);            
        testObjectPiranha = setup(deviceID);
        runTest(testObjectPiranha);     
      }

    private void runTest(TestObjectPiranha2 testObjectPiranha){
            
            final PiranhaAndroidClient c  = testObjectPiranha.getAndroidClient();
            
           testObjectPiranha.takeScreenShot();
      }


    


}
