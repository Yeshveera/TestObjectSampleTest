package com.citrix.shared;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.citrixonline.piranha.PiranhaUtils;

public class WGet2 {
	
	static CustomLogger logger = CustomLogger.getLogger();

	/**
	 * Download gzip file from link to localDirectory and unzip it
	 * and return handle to the file
	 * @param link
	 * @param fullPathToLocalDirectory
	 */
	public static void DownloadFileAndUnZipGZIPFile(String link, File fullPathToLocalDirectory){
		File gzipFile = DownloadFile(link, fullPathToLocalDirectory, false);
		try {
			CompressionUtil.gunzipDecompress(gzipFile);
		} catch (IOException e) {
			e.printStackTrace();
			PiranhaUtils.commandFailed("Unarchiving Failed for file :" + gzipFile.getAbsolutePath() );
		}
	}
	
	/**
	 * download file to a directory.
	 * @param link : http://mysite.com/download/myfile
	 * @param fullPathToLocalDirectory - example /usr/temp/ , the directory will be made if needed.
	 * @param silent - if true the command will not throw {@link AssertionError}
	 * @return file Handle to the Downloaded file
	 */
	public static File DownloadFile(String link,
			File fullPathToLocalDirectory , boolean silent) {		
		checkDirExistsOrMakeit(fullPathToLocalDirectory);
		String fileName = getFileNameToBeDownloaded(link);
		File fullDstfilePath = new File(fullPathToLocalDirectory, fileName);
		try {
			DownloadFile(link, fullDstfilePath);
		} catch (Exception e) {
			if(!silent) {
				e.printStackTrace();
				PiranhaUtils.commandFailed("Could not download binary from url :  "
						+ link + " to directory :  "
						+ fullPathToLocalDirectory.getAbsolutePath());
			}
		}
		return fullDstfilePath;
	}
		
	private static void DownloadFile(String link, File filePath) throws IOException {
		PerformanceCounter.start();
		URL url = new URL(link);
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		Map<String, java.util.List<String>> header = http.getHeaderFields();
		while (isRedirected(header)) {
			link = header.get("Location").get(0);
			url = new URL(link);
			http = (HttpURLConnection) url.openConnection();
			header = http.getHeaderFields();
		}
		InputStream input = http.getInputStream();
		byte[] buffer = new byte[150*1024];
		int n = -1;
		OutputStream output = new FileOutputStream(filePath);
		while ((n = input.read(buffer)) != -1) {
			output.write(buffer, 0, n);
		}
		output.close();
		PerformanceCounter.stop();
		logger.debug("Time taken to download: %s seconds",
				PerformanceCounter.getElapsedTimeinSeconds());
	}
	
	private static boolean isRedirected(
			Map<String, java.util.List<String>> header) {
		for (String hv : header.get(null)) {
			if (hv.contains(" 301 ") || hv.contains(" 302 "))
				return true;
		}
		return false;
	}

	private static String getFileNameToBeDownloaded(String fullDownloadURL) {
		String[] parts = StringUtils.split(fullDownloadURL, "/");
		String fileName = parts[parts.length - 1];
		return fileName;
	}

	private static void checkDirExistsOrMakeit(File dir) {
		if (!dir.exists()) {
			try {
				FileUtils.forceMkdir(dir);
			} catch (IOException e) {
				e.printStackTrace();
				PiranhaUtils.commandFailedShutDownJVM("Could not make Dir :"
						+ dir.getAbsolutePath());
			}
		}
		if(!dir.isDirectory()){
			PiranhaUtils.commandFailedShutDownJVM("File is not a Directory :"
					+ dir.getAbsolutePath());
		}
	}

}
