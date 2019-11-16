/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.webfrk.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Class <code>HandlerManager<code> is used for registering various service.
 * 
 * @author wuheng
 * @since  2019.2.20
 */
@Component
public final class HandlerManager  {

	/**
	 * servlet handlers
	 */
	protected static Map<String, Method> servletHandlers = new HashMap<String, Method>();

	/**
	 * @throws Exception
	 */
	public HandlerManager() {
	}

	/**
	 * @param serviceModule   service module
	 * @param serviceName     service name
	 */
	public boolean addHandler(String serviceModule, Method serviceName) {
		try {
			servletHandlers.put(serviceModule + "/" + serviceName.getName(), serviceName);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * @param servletPath   servlet
	 * @return              the related method
	 * @throws Exception    exception
	 */
	public Method geHandler(String servletPath) throws Exception {
		return servletHandlers.get(servletPath);
	}

		
}
