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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.citrix.aft.AFTECResultException;
import com.citrix.aft.proto.AFTAgentLib.AFTAgent.EOperatingSystem;
import com.citrixonline.aft.utils.AFTConnectionManager;
import com.citrixonline.piranha.PiranhaException;
import com.citrixonline.piranha.PiranhaUtils;

/**
 * Gives access to basic utility methods.
 * 
 */
public class CommonUtils {

	private static CustomLogger logger = CustomLogger.getLogger();

	/**
	 * @return True if local machine is Windows, else false.
	 */
	public static boolean isLocalMachineWindowsPlatform() {
		String os = System.getProperty("os.name");
		if (os != null && os.toLowerCase().contains("windows"))
			return true;
		else
			return false;

	}

	/**
	 * @return True if local machine is Mac, else false.
	 */
	public static boolean isLocalMachineMacPlatform() {
		String os = System.getProperty("os.name");
		if (os != null && os.toLowerCase().contains("mac")
				|| os.toLowerCase().contains("darwin"))
			return true;
		else
			return false;
	}

	/**
	 * @return {@link com.citrix.aft.proto.AFTAddon.SystemInfo.GetOSInfoResponse.EOperatingSystem}
	 *         value for local OS.
	 */
	public static EOperatingSystem getLocalOS() {

		EOperatingSystem localOs = EOperatingSystem.eUnknownOperatingSystem;
		String localOsStr = System.getProperty("os.name");
		if (localOsStr.contains("Windows")) {
			localOs = EOperatingSystem.eWin32;
		} else if (localOsStr.contains("Mac OS")) {
			localOs = EOperatingSystem.eDarwin;
		} else if (localOsStr.contains("Linux")) {
			localOs = EOperatingSystem.eLinux;
		}
		return localOs;
	}

	/**
	 * Get the AFT minor version number that is installed on the specified host.
	 * 
	 * @param host
	 * @return AFT minor version.
	 */
	public static int getAFTMinorVersion(final String host) {
		try {
			return Integer.parseInt(AFTConnectionManager.getAFT(host)
					.getAFTAgent().getVersion().split("_")[1]);
		} catch (NumberFormatException e) {
			PiranhaUtils.commandFailed(new PiranhaException("",
					"NumberFormatException", e));
		} catch (AFTECResultException e) {
			PiranhaUtils.commandFailed(new PiranhaException("",
					"AFTECResultException", e));
		}
		return 0;

	}

	/**
	 * Generate random five digit {@link Integer}.
	 * 
	 * @return Random five digit number.
	 */
	public static int generateRandomFiveDigitInt() {
		int randomInt;

		randomInt = (int) (Math.random() * 90000) + 10000;

		return randomInt;
	}

	/**
	 * example usage :
	 * 
	 * CommonUtils.combinePath("/Users/johndoe/"); 
	 * CommonUtils.combinePath("/", "Users", "johndoe");
	 * CommonUtils.combinePath(new String[] { "/", "Users",
	 * "johndoe", "arrayUsage" });
	 * 
	 * @param paths
	 * @return {@link String}
	 */
	public static String combinePath(String... paths) {
		if (paths.length == 0) {
			return "";
		}
		File file = new File(paths[0]);

		for (int i = 1; i < paths.length; i++) {
			file = new File(file, paths[i]);
		}

		return file.getPath();
	}


	public static void writeFileContentsToStandardErrorOutput(File file) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		int oneByte;
		try {
			while ((oneByte = fis.read()) != -1) {
				System.err.write(oneByte);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.flush();
	}
}

