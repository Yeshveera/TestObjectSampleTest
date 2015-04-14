package com.citrix.web.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {

	private int retryCount = 0;
	private int maxRetryCount = 3;

	public boolean retry(ITestResult result) {
		if(retryCount < maxRetryCount ) { 
			retryCount++; 
			return true; 
		} 
		return false; 
		
	}

}
