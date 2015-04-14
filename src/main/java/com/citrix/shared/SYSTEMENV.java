package com.citrix.shared;

public enum SYSTEMENV {
	
	
	QA3("QA3" , false ,  "QA3 Legacy"),
	WWW("WWW" , false ,  "LIVE Legacy" ),
	GLOBAL("GLOBAL" , true , "LIVE Global"),
	STAGE("STAGE" , false , "STAGE Legacy"),
	STAGEGLOBAL("STAGEGLOBAL" , true , "STAGE Global" ),  
	GLOBALRC1("GLOBALRC1" , true , "RC1 Global"),
	GLOBALED1("GLOBALED1" , true , "ED1 Global"),
	I2("I2" , false , "I2 Legacy");

	private String name;
	private boolean globalBroker;
	private String fixtureEnvName;
	public static String [] legacyPaths = {"/meeting/getInfo/", "/meeting/rest/sessions/"};
	public static String [] globalPaths = {"/meeting/rest/2/meetings/"};
	
	private SYSTEMENV(String name, boolean globalBroker , String fixtureEnvName) {
		this.name = name;
		this.globalBroker = globalBroker;
		this.fixtureEnvName = fixtureEnvName;
	}
	public String getName() {
		return name;
	}
	public boolean isGlobalBroker() {
		return globalBroker;
	}
	public String getBaseurl(boolean ishttps) {
		if(ishttps){
			String.format("https://%s.gotomeeting.com", getName().toLowerCase());
		}
		return String.format("http://%s.gotomeeting.com", getName().toLowerCase());
	}
	public String getfixtureEnvName() {
		return fixtureEnvName;
	}
	public String[] getPaths() {
		if(globalBroker){
			return globalPaths;
		}else {
			return legacyPaths;
		}
	}
	public String getURL(String meetingId){
		return String.format("%s%s%s", getBaseurl(false), getPaths()[0], meetingId);
	}
	
	public String getURL(String baseURL , String meetingId){
		return String.format("%s%s%s", baseURL, getPaths()[0], meetingId);
	}
}

