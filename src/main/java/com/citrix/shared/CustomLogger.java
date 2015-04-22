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

import java.util.ArrayList;
import java.util.Iterator;



/**
 * A class that wraps a Log4J logger, providing some handy additions for
 * convenience like customizable tag information and string formatting.
 */
@SuppressWarnings("rawtypes")
public class CustomLogger {

    // factories

    public static CustomLogger getLogger(Class clazz) {
        return getLogger(clazz.getName());
    }

    public static CustomLogger getLogger() {
        return getLogger(getCallerClassName());
    }

    public static CustomLogger getLogger(String name) {
        return new CustomLogger(name);
    }

    private static String getCallerClassName() {
        return new Exception().getStackTrace()[2].getClassName();
    }
    
    /**
     * Return the base logger.
     *
     * @return the base logger
     */
    public BaseLogger getBase() { return _base; }

    
    /**
     * Constructor. Create a new Omega that wraps the specified base logger.
     *
     * @param base  the base logger
     */
    public CustomLogger( BaseLogger base ) {

        _base = base;
        _tags = new ArrayList<String>();
        _cachedTag = "";
    }

    /**
     * Constructor. Create a new Omega logger that will wrap a Log4J logger
     * identified by the given class.
     *
     * @param clas  the class which identifies the Log4J logger
     */
    public CustomLogger( Class<?> clas ) { this( BaseLogger.getLogger( clas ) ); }


    public CustomLogger( String className ) { this( BaseLogger.getLogger( className ) ); }

    /**
     * Add the specified tag information to this logger. This method returns
     * a reference to this logger so that calls may be chained.
     *
     * @param tag  the tag information
     * @return a reference to this logger
     */
    public CustomLogger addTag( String tag ) {

        _tags.add( tag );
        _cachedTag = _formatTags();

        return this;
    }

    /**
     * Remove the specified tag information to this logger. This method returns
     * a reference to this logger so that calls may be chained.
     *
     * @param tag  the tag information
     * @return a reference to this logger
     */
    public CustomLogger removeTag( String tag ) {

        _tags.remove( tag );
        _cachedTag = _formatTags();

        return this;
    }

    
    public CustomLogger clearAllTags() {

        _tags.clear();
        _cachedTag = _formatTags();

        return this;
    }
    /**
     * Output a formatted test name.  The test will have the name centered between bars on its own
     * lines.
     *
     * @param format    the format string
     * @param objects   values to format
     */
    public void testName( String format, Object... objects ) {

        String msg = String.format(formatTestName( format ), objects );
        _base.info( msg );
    }


    /**
     * Output a formatted log line at the debug log level.
     *
     * @param format   the format string
     * @param objects  values to format
     */
    public void debug( String format, Object... objects ) {

        String msg = String.format( format, objects );
        _base.debug( _cachedTag + msg );
    }

    /**
     * Output a formatted log line at the info log level.
     *
     * @param format   the format string
     * @param objects  values to format
     */
    public void info( String format, Object... objects ) {

        String msg = String.format( format, objects );
        _base.info( _cachedTag + msg );
    }

    /**
     * Output a formatted log line at the warn log level.
     *
     * @param format   the format string
     * @param objects  values to format
     */
    public void warn( String format, Object... objects ) {

        String msg = String.format( format, objects );
        _base.warn( _cachedTag + msg );
    }

    /**
     * Output a formatted log line at the error log level.
     *
     * @param format   the format string
     * @param objects  values to format
     */
    public void error( String format, Object... objects ) {

        String msg = String.format( format, objects );
        _base.error( _cachedTag + msg );
    }

    /**
     * Output a formatted log line at the fatal log level.
     *
     * @param format     the format string
     * @param objects    the values to format
     */
    public void fatal( String format, Object... objects ) {

        String msg = String.format( format, objects );
        _base.fatal( _cachedTag + msg );
    }


    /**
     * Log test end.
     * @param testDescription Test description.
     */
    public void logEnd(final String testDescription) {
        String message = "############### Ending Test "
            + " :- " + testDescription + " ###############";
        _base.info(formatTestStartandEnd(message));
    }

    /**
     * Log test message. This message has '#########' prepended and appended to it.
     * @param testMessage Test message.
     */
    public void logMessage(final String testMessage) {
        StringBuffer str = new StringBuffer();
        str.append("\t\t#########");
        str.append(testMessage);
        str.append("#########");
        _base.info(str.toString());
    }

    /**
     * This method is to be used for logging any set up steps required to
     * run a test case.
     * @param str Test step description.
     */
    public void logSetUpStep(final String str) {        
    	String message = "############### SetUp ## "
            + " :- " + str + " ###############";
    	_base.info(message);
    }
    
    public void logTearDownStep(final String str) {        
    	String message = "############### TearDown ## "
            + " :- " + str + " ###############";
    	_base.info(message);
    }

    /**
     * Log test start.
     * @param testDescription Test description.
     */
    public void logStart(final String testDescription) {
        String message = "############### Beginning Test "
            + " :- " + testDescription + " ###############";
        _base.info(formatTestStartandEnd(message));
    }

    
    /**
     * Log test step.
     * @param format Test step description.
     */
    public void logTestStep(String format, Object... args ) {
    	_base.info("\t" + String.format(format, args));
    }
    
    /**
     * Log test step.
     * @param str Test step description.
     */
    public void logTestStep(final String str) {
    	_base.info("\t" + str);
    }

    /**
     * Log test verification step.
     * @param desc Test step description.
     */
    public void logVerification(final String desc) {
        _base.info(formatTestVerification(desc));
    }
    
    /**
     * Log test verification step.
     * @param desc Test step description.
     */
    public void logVerification(final String desc , Object ...args) {
        _base.info(formatTestVerification(String.format(desc , args)));
    }

    /**
     * Format the list of tags for a log message.
     *
     * @return the formatted tag string
     */
    private String _formatTags() {

        StringBuffer buf = new StringBuffer();
        Iterator<String> iter = _tags.iterator();
        while( iter.hasNext() ) {
            if( buf.length() == 0 ) buf.append( "<" ).append( iter.next() );
            else buf.append( ':' ).append( iter.next() );
        }
        if( buf.length() > 0 ) buf.append( "> - " );
        return buf.toString();
    }


	/**
	 * Returns a string that incorporates the argument into five lines of text:
	 * blank, overline, argument, underline, blank. Each non-blank line is
	 * indented.
	 * 
	 * @param testName
	 *            the string to so format.
	 * @return
	 */
	private String formatTestName(String testName) {
		String formatted = null;
		int length = testName.length();
		String line = "=";
		for (int i = 1; i < length; i++) {
			line = line + "=";
		}
		formatted = "\n" + "\n  " + line + "\n  " + testName + "\n  " + line
				+ "\n";
		return formatted;
	}

    /**
     * Formats test step for logging start and end of test
     * @param text Test text.
     * @return Formatted string.
     */
    private String formatTestStartandEnd(final String text) {
        String retVal = null;
        StringBuffer line = new StringBuffer();
        for (int i = 0; i < text.length(); i++) {
            line.append("-");
        }
        retVal = "\n" + line.toString() + "\n" + text + "\n" + line.toString();
        return retVal;
    }

    /**
     * Formats test verification step for logging.
     * @param text Test text.
     * @return Formatted string.
     */
    private String formatTestVerification(final String text) {
        return "\t\t....Verification : " + text;
    }

    
    /** The base Log4J logger. */
    private BaseLogger _base;
    /** A tags to prepend to log messages. */
    private ArrayList<String> _tags;
    /** A cached version of the formatted tag string. */
    private String _cachedTag;
}
