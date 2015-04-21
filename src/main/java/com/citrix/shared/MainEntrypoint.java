package com.citrix.shared;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import com.citrixonline.piranha.COLTimeUtils;
import com.citrixonline.piranha.ControlType;
import com.citrixonline.piranha.androidclient.PiranhaAndroidClient;
import com.thoughtworks.selenium.Wait;
import com.thoughtworks.selenium.webdriven.commands.WaitForCondition;

public class MainEntrypoint {
	
	public static PiranhaAndroidClient c;
	static CustomLogger logger = CustomLogger.getLogger("Piranha Test");
			
	public static void main(String[] args) {
	       String ip = "localhost";
	        c = new PiranhaAndroidClient(ip, 7100, true);
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
	
	private static void openScreenShot(File f) {
		Desktop dt = Desktop.getDesktop();
	    try {
			dt.open(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public static File takeScreenShot() {
		String imgstr = c.robotium().utils().takeScreenShot();
		BufferedImage newImg = ImageUtils2.decodeToImage(imgstr);
		try {
			File f = saveFile(newImg);		
			openScreenShot(f);
			return f;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.error("Screenshot failed");
		return null;
	}
	
	private static File saveFile(BufferedImage screen) throws IOException{

		String fileName = "";
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		File screenShotDir = new File(CommonUtils.combinePath(Paths.get(".").toAbsolutePath().normalize().toString() , "Screenshot"));
		if(!screenShotDir.exists())
			screenShotDir.mkdirs();	
		File imgFile = new File(screenShotDir , fileName + formatter.format(calendar.getTime()) + ".png");
        try {
			ImageIO.write(screen, "png", imgFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(imgFile.exists()){
			logger.info("Screenshot can be found here:  %s", imgFile.getAbsolutePath());
		} else {
			logger.error("Error in taking screenshot");
		}
	//	BaseLogger.getLogger(this.getClass()).reportScreenShotToTestNgReport(imgFile);
		return imgFile;

	}
	
	
}
