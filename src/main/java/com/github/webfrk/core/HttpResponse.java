/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.webfrk.core;

/**
 * @author  wuheng
 * @since   2019.2.20
 * 
 * <p>
 * The {@code HttpResponse} class represents the return
 * value should be bound to the web response body.
 */
public class HttpResponse {

	/**
	 * neither Success or Failure
	 */
	protected String status;
	
	/**
	 * it represents the exception information,
	 * otherwise it should be null
	 */
	protected String message;
	
	/**
	 * if it is not an exception, the response
	 * is the object.
	 */
	protected Object result;
	
	public HttpResponse() {
		super();
	}
	
	public HttpResponse(String status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public HttpResponse(String status, Object result) {
		super();
		this.status = status;
		this.result = result;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}
