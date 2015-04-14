package com.citrix.web.utils;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.Augmenter;

import com.citrix.shared.BaseLogger;
import com.citrix.shared.BaseTest;
import com.citrix.shared.CustomLogger;

public class TakeBrowserScreenshot2 {
	
	private static final String SCREENSHOTDIR = BaseTest.getScreenshotsDir().toString();

	private final String screenshotFolder = SCREENSHOTDIR;
	CustomLogger logger = CustomLogger.getLogger();
	private WebDriver driver;
	private Capabilities capabilities;
	private String browserName;
	private String browserVersion;
	private String platform;
	

	public TakeBrowserScreenshot2(WebDriver driver) {
		this.driver = driver;
		if (driver instanceof HasCapabilities) {
			capabilities = ((HasCapabilities) driver).getCapabilities();
			browserName = capabilities.getBrowserName().toUpperCase();
			browserVersion = capabilities.getVersion();
			platform = capabilities.getPlatform().toString();
		}
	}

	public File shoot() throws IOException {
		if(null == driver){
			logger.error("driver is null");
			return null;
		}
	    driver = new Augmenter().augment(driver);
		File screen = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		return saveFile(screen);
	}
	
	public File shoot(WebElement element) throws IOException {
		if(!element.isDisplayed()){
			logger.error("Element [%s] not found", element);
		}
		if(null == driver){
			logger.error("driver is null");
			return null;
		}
		((JavascriptExecutor)driver).executeScript("arguments[0].setAttribute('style', arguments[1]);",element, "color: Red; border: 2px solid red;");
	    driver = new Augmenter().augment(driver);
		File screen = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
	
		Point p = element.getLocation();
		int width = element.getSize().getWidth();
		int height = element.getSize().getHeight();
		Rectangle rect = new Rectangle(width, height);
		BufferedImage img = ImageIO.read(screen);
		BufferedImage dest = img.getSubimage(element.getLocation().getX(), element.getLocation().getX(), element.getSize().getWidth(),
				element.getSize().getHeight());
		ImageIO.write(dest, "png", screen);
		return saveFile(screen);
	}
	
	public File saveFile(File screen) throws IOException{
		
		String fileName = "";
		if (capabilities != null){
			fileName = browserName + "_" + browserVersion + "_" + platform + "_";
		}
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		File imgFile = new File(screenshotFolder , fileName + formatter.format(calendar.getTime()) + ".png");
		FileUtils.copyFile(screen,
				imgFile);
		if(imgFile.exists()){
			logger.debug("Screenshot can be found here:  %s", imgFile.getAbsolutePath());
		} else {
			logger.error("Error in taking screenshot");
		}
		BaseLogger.getLogger(this.getClass()).reportScreenShotToTestNgReport(imgFile);
		return imgFile;

	}
}
