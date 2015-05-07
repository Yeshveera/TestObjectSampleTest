package com.citrix.testObject.g2m.piranha;

import java.util.Arrays;
import java.util.List;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.collections.Lists;
import org.testobject.piranha.TestObjectDevice;
import org.testobject.piranha.TestObjectPiranha;
import org.testobject.piranha.TestObjectPiranha2;

import com.citrix.shared.BaseTest;

public class TestObjectAndroidTest extends BaseTest {

//	private static final String Cat_B15_real = "Cat_B15_real"; // Did get the device
	private static final String LG_G3_real = "LG_G3_real"; // ScreenShot works
	private static final String HTC_Nexus_9_real = "HTC_Nexus_9_real"; // ScreenShot works
	private static final String HTC_One_M8_real = "HTC_One_M8_real"; // ScreenShot works
	private static final String LG_Nexus_4_E960_real = "LG_Nexus_4_E960_real"; // ScreenShot works
	private static final String Asus_Google_Nexus_7_2013_real = "Asus_Google_Nexus_7_2013_real";
//	private static final String DEVICEID = "";
	private static TestObjectPiranha2 testObjectPiranha;

	//private static final String Samsung_Galaxy_S5_real = "Samsung_Galaxy_S5_real"; // screenshots don't work

	protected static void setTestObjectPiranha(TestObjectPiranha2 t) {
		testObjectPiranha = t;
	}
	
	public static Object[][] deviceList() {
		return new Object[][] { 
			{LG_G3_real},
/*			{HTC_Nexus_9_real},
			{HTC_One_M8_real},
			{Asus_Google_Nexus_7_2013_real},
			{LG_Nexus_4_E960_real}*/
		};
	}
	
//	@DataProvider(name = "getDeviceID" , parallel = true)
	@DataProvider(name = "getDeviceID")
	public static Object[][] getDeviceID() {
		List<Object[]> result = Lists.newArrayList();
		result.addAll(Arrays.asList(deviceList()));
		return result.toArray(new Object[result.size()][]);
	}
	
    @AfterMethod
    public void handleTestEnd2(ITestResult result) throws Exception
    {	
		if (result.isSuccess()) {
			testObjectPiranha.updateTestReportStatus(true);
		} else {
			testObjectPiranha.updateTestReportStatus(false);
		} 
		tearDown(testObjectPiranha);
		testObjectPiranha = null;
    }
	
	public boolean checkDeviceIsAvailable(String deviceID) {
		for (TestObjectDevice device : TestObjectPiranha.listDevices()) {
			if (device.isAvailable && device.id.equalsIgnoreCase(deviceID)) {
				return true;
			}
		}
		return false;
	}
	
	 private void tearDown(TestObjectPiranha2 testObjectPiranha) {
          if (testObjectPiranha != null) {
        	  	logger.info("Closing TestObject Session", testObjectPiranha.getSessionId());
            testObjectPiranha.close();
          }
	  }
}
