package com.citrix.shared;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.web.util.HtmlUtils;
import org.testng.Reporter;

@SuppressWarnings("static-access")
public class BaseLogger {

	private Logger logger;
	private Reporter testNGReporter;

	// Static factories
	public static BaseLogger getLogger(Class<?> clazz) {
		return new BaseLogger(clazz);
	}

	public static BaseLogger getLogger(String className) {
		return new BaseLogger(className);
	}

	// Contructors
	public BaseLogger(Class<?> clazz){
		this.logger = Logger.getLogger(clazz);
	}
	

	public BaseLogger(String className) {
		this.logger = Logger.getLogger(className);
	}

	// methods
	public void info(String message) {
		logger.info(message);
		reportToTestNg(message);
	}

	public void debug(String message) {
		logger.debug(message);
		reportToTestNg("[debug]" + message);
	}

	public void warn(String message) {
		logger.warn(message);
		reportToTestNg("[warn]" + message);
	}

	public void error(String message) {
		logger.error(message);
		reportToTestNg("[error]" + message);
	}

	public void fatal(String message) {
		logger.fatal(message);
		reportToTestNg("[fatal]" + message);		
	}

	private void reportToTestNg(String message) {
	    System.setProperty("org.uncommons.reportng.escape-output", "false");
		testNGReporter.log(message + "<br/>");
	}
	
	public void reportScreenShotToTestNgReport(File imgFile) {
	      System.setProperty("org.uncommons.reportng.escape-output", "false");	  
	      testNGReporter.log("<p><img width=\"256\" src=\"" + imgFile.getAbsoluteFile()  + "\" alt=\"screenshot at " + new Date()+ "\"/></p></a><br />"); 
	}
		
}
