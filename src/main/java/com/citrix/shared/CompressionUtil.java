package com.citrix.shared;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import com.citrixonline.piranha.PiranhaUtils;

public class CompressionUtil {

	private static final int BUFFER = 1500 * 1024;
	static String filePath = "/Volumes/Backup/Dev/depot/Endpoint/Omega/main/Java/OmegaSystemTests/bamboo-bin/_output/iOS_Debug/iphone/iOSFixture-Debug_device.tar.gz";
	static String filePathToCompress = "/Volumes/Backup/Dev/depot/Endpoint/Omega/main/Java/OmegaSystemTests/bambooBuilds/_deploy/Applications_macosx_fat/bin/SharingApp_d.app";
	
	public static void main(String [] args) throws IOException{
		File file = new File(filePathToCompress);
		if(file.exists()){
//			gunzipDecompress(file);
			compressToTarGZ(file);
		}
	}


	public static final void gunzipDecompress(File input) throws IOException {
		if(CommonUtils.isLocalMachineMacPlatform()){
			decompress(input, true); // this if faster than below code
			return;
		}
		FileInputStream fin = new FileInputStream(input);
		BufferedInputStream in = new BufferedInputStream(fin);
		GzipCompressorInputStream gzIn = new GzipCompressorInputStream(in);
		TarArchiveInputStream tarIn = new TarArchiveInputStream(gzIn);
		TarArchiveEntry entry = null;

		while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
			System.out.println("Extracting: " + entry.getName());
			if (entry.isDirectory()) {
				File f = new File(input.getParent(), entry.getName());
				f.mkdirs();
			}
			else {
				File f = new File(input.getParent(), entry.getName());
				FileOutputStream fos = new FileOutputStream(f);
				getBytes(tarIn, fos);
				safeClose(fos);
			}
		}
		safeClose(tarIn);
		System.out.println("untar completed successfully!!");
	}
	
    private static final void decompress(File input , boolean silent){
    	CommandExecutor executor = CommandExecutor.Factory.createLocalCommmandExecutor();
    	String executable = "tar";
		List<String> commands = new ArrayList<String>();
		commands.add("xvzf");
		commands.add(input.getAbsolutePath());
		boolean failsOnErrorOutput = false;
		try {
			executor.executeCommand(executable, commands, new File(input.getParent()), failsOnErrorOutput );
		} catch (ExecutionException e) {
			if(!silent) {
				e.printStackTrace();
				PiranhaUtils.commandFailed("Failed to uncompress file :" + input.getAbsolutePath());
			}
		}
    }

	public static final File compressToTarGZ(File input) throws IOException {
		if (CommonUtils.isLocalMachineMacPlatform()) {
			return compress(input, false);
		}
		File targzFile = new File(input.getParent(), input.getName()
				+ ".tar.gz");
		FileOutputStream fOut = new FileOutputStream(targzFile);
		BufferedOutputStream bOut = new BufferedOutputStream(fOut);
		GzipCompressorOutputStream gzOut = new GzipCompressorOutputStream(bOut);
		TarArchiveOutputStream tOut = new TarArchiveOutputStream(gzOut);
		addFileToTarGz(tOut, input.getAbsolutePath(), "");

		safeClose(tOut);
		safeClose(gzOut);
		safeClose(bOut);
		safeClose(fOut);
		return targzFile;
	}
    
	private static void addFileToTarGz(TarArchiveOutputStream tOut, String path, String base) throws IOException  {
	    File f = new File(path);
	    String entryName = base + f.getName();
	    TarArchiveEntry tarEntry = new TarArchiveEntry(f, entryName);
	    tOut.putArchiveEntry(tarEntry);
	
	    if (f.isFile()) {
	        IOUtils.copy(new FileInputStream(f), tOut);
	        tOut.closeArchiveEntry();
	    } else {
	        tOut.closeArchiveEntry();
	        File[] children = f.listFiles();
	        if (children != null){
	            for (File child : children) {
		            System.out.println("Adding : " + child.getAbsolutePath().replace(f.getAbsolutePath(), ""));
	                addFileToTarGz(tOut, child.getAbsolutePath(), entryName + "/");
	            }
	        }
	    }
	}
    
    private static final File compress(File input , boolean silent){
    	CommandExecutor executor = CommandExecutor.Factory.createLocalCommmandExecutor();
    	String executable = "tar";
		List<String> commands = new ArrayList<String>();
		commands.add("-zcvf");
		File targzFile = new File(input.getParent() , input.getName() + ".tar.gz");
		commands.add(input.getName() + ".tar.gz");
		commands.add(input.getName());
		boolean failsOnErrorOutput = false;
		try {
			executor.executeCommand(executable, commands, new File(input.getParent()) , failsOnErrorOutput );
		} catch (ExecutionException e) {
			if(!silent) {
				e.printStackTrace();
				PiranhaUtils.commandFailed("Failed to compress file :" + input.getAbsolutePath());
			}
		}
		return targzFile;
    }
    
    /**
     * <p>Read all the bytes from an InputStream and write them to an OutputStream.</p>
     * <p>Buffers maximum of 1000KB at a time.</p>
     * @param in
     * @param out
     * @throws java.io.IOException
     */
    private static void getBytes(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[BUFFER];
        for (int bytesRead = in.read(buf); bytesRead > -1; bytesRead = in.read(buf)) {
            out.write(buf, 0, bytesRead);
        }
        // flush
        out.flush();
    }
    
    /**
     * <p>Safely closes an InputStream by invoking the close() method. Any errors thrown are silently discard, and no NullPointerException is thrown if the InputStream reference passed is null.</p>
     * @param in InpuStream to safely close.
     */
    private static final void safeClose(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
    }
    
    /**
     * <p>A helper method to safely close the given OutputStream object via invoking flush() followed by close(). Any exceptions thorwn are discarded silently, including NullPointerExceptions that may be thrown if the reference is null.</p>
     * @param out OuputStream to safely close.
     */
    private static final void safeClose(OutputStream out) {
        if (out != null) {
            try {
                out.flush();
            } catch (Exception e) {
            }
            try {
                out.close();
            } catch (Exception e) {
            }
        }
    }
}
