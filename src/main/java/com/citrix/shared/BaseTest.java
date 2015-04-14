/*
 * Copyright (c) Citrix Online LLC
 * All Rights Reserved Worldwide.
 *
 * THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO CITRIX ONLINE
 * AND CONSTITUTES A VALUABLE TRADE SECRET.  Any unauthorized use,
 * reproduction, modification, or disclosure of this program is
 * strictly prohibited.  Any use of this program by an authorized
 * licensee is strictly subject to the terms and conditions,
 * including confidentiality obligations, set forth in the applicable
 * License and Co-Branding Agreement between Citrix Online LLC and
 * the licensee.
 */

package com.citrix.shared;

import java.io.File;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;


public class BaseTest extends Assert {
	
	protected String platform;
	protected String browserName;
	protected String browserVersion;
	public static SYSTEMENV systemenv;
	
	protected CustomLogger logger = CustomLogger.getLogger();

	private static String currentTestName = null;
	private static final String TESTREPORTDIR = new File(System.getProperty("user.dir") , "test-output").toString();
	
	private static final String TEST_PROPERTY_FILENAME_KEY = "testPropertyFileName";
	// there are two ways to override this value one by mvn property another by env variable setting
	private static final String TEST_PROPERTY_FILENAME_KEY_DEFAULT_VALUE = "config";
    static String bundleName = TEST_PROPERTY_FILENAME_KEY_DEFAULT_VALUE;
	
    
	protected static String testHost1;
	protected static int DEFAULT_APPIUM_PORT = 4723;
	protected static int MIN_TIME;
	protected static int MAX_TIME;
	

    
    public BaseTest() {
    	
        // check env variable property is set to get property file Name
        String envValue =       System.getenv(TEST_PROPERTY_FILENAME_KEY);
        if(StringUtils.isNotEmpty(envValue)){
                bundleName = envValue;
        }       
        
        // check mvn property is set
        String propertyValue =  System.getProperty (TEST_PROPERTY_FILENAME_KEY);
        if(StringUtils.isNotEmpty(propertyValue)){
                bundleName = propertyValue;
        }       
        
        String propertyfileName = bundleName + ".properties";
        
        logger.info("Using Property file : %s" , propertyfileName);
    	
    	TestProperties.setProperties(propertyfileName);
    	testHost1 = TestProperties.getProperty("TestData.AFTHOST.1");
    	MIN_TIME = TestProperties.getInt("timeout.min");
    	MAX_TIME = TestProperties.getInt("timeout.max");
     }
    
//    @BeforeSuite
//    public void handleBeforeSuite(){
//  
//    }
    
  
	@BeforeMethod
    public void handleTestStart(Method method) throws Exception
    {
    	String testName =  method.getName(); 
    	String fullClassName = this.getClass().getName();
    	setCurrentTestName(fullClassName + "#" + testName);
    	logger.testName(getCurrentTestName());
    	
    	//logger.logStart(" " + getCurrentTestName());
    }
    
    @AfterMethod
    public void handleTestEnd(ITestResult result) throws Exception
    {	
		if (result.isSuccess()) {
			logger.logEnd(" " + getCurrentTestName() + " : Passed. ");
		} else {
			logger.logEnd(" " + getCurrentTestName() + " : Failed. ");
		} 
    }
    	

	/**
	 * @return the currentTestName
	 */
	public static String getCurrentTestName() {
		return currentTestName;
	}

	/**
	 * @param testName the currentTestName to set
	 */
	private void setCurrentTestName(String testName) {
		currentTestName = testName;
	}

	public static File getScreenshotsDir() {
		File logdirfile = new File(TESTREPORTDIR , "screenshots");
		if(!logdirfile.exists()){
			logdirfile.mkdirs();
		}
		return logdirfile;
	}
	
}