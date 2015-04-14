package com.citrix.shared;

import com.citrixonline.piranha.PiranhaUtils;

public class RetryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3679914975606040183L;

	// Just end all retries
	public RetryException(String message) {
        super(message);
        PiranhaUtils.commandFailed(message);
	}


}
