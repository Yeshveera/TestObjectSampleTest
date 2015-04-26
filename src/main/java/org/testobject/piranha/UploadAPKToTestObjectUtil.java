package org.testobject.piranha;

import java.io.File;

import com.citrix.shared.BaseTest;

public class UploadAPKToTestObjectUtil {

	static String folder = "/Users/parameshwaranmurli/Dev/qa/Piranha/AndroidDriver_New/PiranhaAndroidClient/src/main/resources";
	static String apk = "piranha-android-server-5.0.30-SNAPSHOT.apk";
	public static void main(String[] args) {
		//TESTOBJECT_APIKEY should be specified in config.properties
		int frameworkAppId = TestObjectPiranha.uploadFrameworkApp("63050D418DD54CBA97D4E29896DE7017", new File(folder , apk));
		System.out.println("FW app id: " + frameworkAppId);
		return;
	}

}
