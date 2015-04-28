package org.testobject.piranha;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

import com.citrix.shared.BaseLogger;
import com.citrix.shared.BaseTest;
import com.citrix.shared.CustomLogger;
import com.citrix.shared.ImageUtils2;
import com.citrixonline.piranha.COLTimeUtils;
import com.citrixonline.piranha.PiranhaAssertionError;
import com.citrixonline.piranha.androidclient.PiranhaAndroidClient;
import com.google.gson.Gson;

public class TestObjectPiranha2 extends TestObjectPiranha{

	protected CustomLogger logger = CustomLogger.getLogger();

	private PiranhaAndroidClient piranhaClient;

	public TestObjectPiranha2(DesiredCapabilities desiredCapabilities) {
		super(desiredCapabilities);
	}
	
	public TestObjectPiranha2(String baseUrl, DesiredCapabilities desiredCapabilities) {
		super(baseUrl , desiredCapabilities);
		
	}


	public PiranhaAndroidClient getAndroidClient() {
		if(piranhaClient != null){
			return piranhaClient;
		}
        int port = getPort();
        String host = "localhost";

        logger.info("TestObject Piranha Session Started at %s:%s" , host, port);
        piranhaClient = new PiranhaAndroidClient(host, port, true);
        
//        logger.info("Hard Wait for %d Seconds to let app start in remote device" , WaitInMilliSeconds/1000);
//        COLTimeUtils.sleep(WaitInMilliSeconds);
        
        return piranhaClient;
	}
	
	
	public void updateTestReportStatus(boolean passed){
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(TESTOBJECT_BASE_URL + "rest/appium/v1/session/" + getSessionId());
		Map<String, Boolean> map1 = Collections.singletonMap("passed", passed);
		Gson gson = new Gson(); 
		String json = gson.toJson(map1); 
		target.path("test").request(new MediaType[] { MediaType.APPLICATION_JSON_TYPE })
		.put(Entity.json(json));
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

	public File takeScreenShot() {
		String imgstr = null;
		PiranhaAndroidClient c = getAndroidClient();
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
		File imgFile = new File(BaseTest.getScreenshotsDir() , fileName + formatter.format(calendar.getTime()) + ".png");
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
