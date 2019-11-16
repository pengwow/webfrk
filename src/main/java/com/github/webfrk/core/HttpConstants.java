/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.webfrk.core;

/**
 * @author wuheng
 * @since  2019.2.20
 * 
 */
public class HttpConstants {

	/*****************************************************************
	 * 
	 *                       HTTP Response
	 * 
	 *****************************************************************/
	public final static String HTTP_RESPONSE_STATUS_OK = "Success";

	public final static String HTTP_RESPONSE_STATUS_FAILED = "Failure";
	
	/******************************************************************
	 * 
	 *                       Http Handler Exceptions
	 * 
	 ******************************************************************/
	
	public final static String EXCEPTION_INVALID_REQUEST_URL = "Invalid servlet path, or invalid parameters was requested";
	
	public final static String EXCEPTION_INVALID_SERVICE_ANOTATION = "Invalid ServiceDefinition was requested";
}
