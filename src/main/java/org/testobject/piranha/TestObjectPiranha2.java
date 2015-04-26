package org.testobject.piranha;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.citrix.shared.CustomLogger;
import com.citrixonline.piranha.COLTimeUtils;
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


	public PiranhaAndroidClient getAndroidClient(long WaitInMilliSeconds) {
		if(piranhaClient != null){
			return piranhaClient;
		}
        int port = getPort();
        String host = "localhost";

        logger.info("TestObject Piranha Session Started at %s:%s" , host, port);
        piranhaClient = new PiranhaAndroidClient(host, port, true);
        
        logger.info("Hard Wait for %d Seconds to let app start in remote device" , WaitInMilliSeconds/1000);
        COLTimeUtils.sleep(WaitInMilliSeconds);
        
        return piranhaClient;
	}
	
	
//	public void updateTestReportStatus(boolean passed){
//		Client client = ClientBuilder.newClient();
//		WebTarget target = client.target(TESTOBJECT_BASE_URL + "rest/appium/v1/session/" + getSessionId());
//		Map<String, Boolean> map1 = Collections.singletonMap("passed", passed);
//		Gson gson = new Gson(); 
//		String json = gson.toJson(map1); 
//		target.path("test").request(new MediaType[] { MediaType.APPLICATION_JSON_TYPE })
//		.put(Entity.json(json));
//	}
	
}
