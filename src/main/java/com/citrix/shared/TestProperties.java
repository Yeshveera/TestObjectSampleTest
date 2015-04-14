package com.citrix.shared;

import java.util.Properties;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.citrixonline.piranha.PiranhaException;
import com.citrixonline.piranha.PiranhaUtils;

public class TestProperties {
    
    static private Properties properties;
    static private boolean isCI = false;
    
    static Logger logger = Logger.getLogger(TestProperties.class);
    /**
     *  make sure the file is in src/main/resources directory
     * 
     * @param propertyFileName like config.properties
     */
    private TestProperties(){
        
    }
    
    /**
     * Initializes properties this method is called from BaseTest
     * @param propertyFileName File containing the properties you want to read.
     * note: assembly jars cannot be created with this way of setting properties, need to move to 
     * resource bundles later.
     */
    public static void setProperties(final String  propertyFileName) {
    	java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
    	java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(java.util.logging.Level.OFF);

    	System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
    	if(propertyFileName.toLowerCase().contains("ci")){
    		isCI = true;
    	}
        String fullpropertyFileName = "src/main/resources/" + propertyFileName;
        
        
  //      GlobalProperties.setProperties(fullpropertyFileName);
        try {
            String filePath = getBaseDirectory() + fullpropertyFileName;
            properties = PiranhaUtils.loadProperties(filePath);
        } catch (Exception e) {
        		PiranhaUtils.commandFailed(new PiranhaException("", "Check property file exists!!! Exiting the test...", e));
			System.exit(1);
        }
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");    
            
         return;
        
    }
    
    /**
     * Gets the base directory of the tests being run. Note: This value will get
     * set if running suites in parallel. It will not get set otherwise. It only
     * needs to be set when running suites in parallel from the master POM path.
     * It does not need to be set if running from within the test project
     * directory.
     * @return Base directory path or empty string.
     */
    private static String getBaseDirectory() {
        String path = System.getProperty("baseDir");
        String fs = System.getProperty("file.separator");
        if (StringUtils.isBlank(path)) {
            return "";
        } else {
            return path + fs;
        }
    }
    
    /**
     * Gets the property value with the specified key name.
     * @param keyName Key for the value you wish to retrieve.
     * @return String containing the value.
     */
    public static String getProperty(final String keyName) {
        if(properties == null){
        		PiranhaUtils.commandFailed("Set test properties  -> TestProperties.setProperties()");
			System.exit(1);
        }
        
        String retValue = null;

        // check env variable property is set to get property file Name
        String envValue =       System.getenv(keyName);
		if(StringUtils.isNotEmpty(envValue)){
                retValue = envValue;
        }       
        
        // check mvn property is set
        String propertyValue =  System.getProperty (keyName);
        if(StringUtils.isNotEmpty(propertyValue)){
                retValue = propertyValue;
        }
        
        if(StringUtils.isNotEmpty(retValue)){
        		return retValue;
        }
        return properties.getProperty(keyName.trim());
    }

    /**
     * Gets the property value with the specified key name. Set the value to the
     * default value if the value is not found.
     * @param keyName Key for the value you wish to retrieve.
     * @param defaultValue Default value to use if no value is found.
     * @return String containing the value (either from properties or default).
     */
    public static String getProperty(final String keyName, final String defaultValue) {
        if(properties == null){
        		PiranhaUtils.commandFailed("Set test properties -> TestProperties.setProperties()");
			System.exit(1);
        }
        return properties.getProperty(keyName.trim(), defaultValue.trim());
    }
    
    /**
     * Gets the boolean value with the specified key name.
     * @param keyName Key for the value you wish to retrieve.
     * @return String containing the value.
     */
    public static boolean getBoolean(final String keyName) {
        if(properties == null){
        		PiranhaUtils.commandFailed("Set test properties  -> TestProperties.setProperties()");
			System.exit(1);
        }
        return Boolean.parseBoolean(properties.getProperty(keyName.trim()));
    }
    
	/**
	 * Gets the integer value of the specified key.
	 * @param keyName Key for the value you wish to retrieve.
	 * @return Integer value
	 */
    public static int getInt(String keyName) {

		String tmp = getProperty(keyName);
		return Integer.parseInt(tmp);

	}

    
    
    public static boolean isCI() {
    		return isCI;
    }
}
