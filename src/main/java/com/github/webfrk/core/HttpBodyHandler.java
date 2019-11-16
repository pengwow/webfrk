/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.webfrk.core;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.github.webfrk.utils.FrkUtils;



/**
 * @author  wuheng
 * @since   2019.2.20
 */
public abstract class HttpBodyHandler implements CommandLineRunner, ApplicationContextAware {

	/**
	 * postfix
	 */
	public final static String POSTFIX = "Service";
	
	/**
	 * logger
	 */
	public final static Logger m_logger = Logger.getLogger(HttpController.class);
	
	/**
	 * All handers
	 */
	@Autowired
	protected HandlerManager configure;
	
	/**
	 * app context
	 */
	protected static ApplicationContext ctx;
	
	/**
	 * 
	 */
	public HttpBodyHandler() {
		super();
	}

	/**********************************************************
	 * 
	 * 
	 * 
	 **********************************************************/
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (ctx == null) {
			ctx = applicationContext;
		}
	}
	
	@Override
	public void run(String... args) throws Exception {

		// get service module based on classname
		String classname = getClass().getSimpleName();

		// cannot convert inner classname to service module
		if (classname.indexOf("/") != -1) {
			throw new Exception("Unsupport inner class");
		}

		// service module name must be ends with 'Service'
		if (!classname.endsWith(POSTFIX)) {
			throw new Exception("Customized service name must end with 'Service'");
		}

		// register services in the specified service module
		registerService(FrkUtils.getServiceModule(classname));
	}

	private void registerService(String serviceModule) throws Exception {
		
		// In our design, the method name with some rules is also the service name,
		// It's impossible to exist duplicated services so and we unsupport polymorphism
		Set<String> registered = new HashSet<String>();
		
		for (Method service : getClass().getDeclaredMethods()) {

			if (service.getModifiers() != Modifier.PUBLIC) {
				continue;
			}
			
			// if exist duplicated services
			if (registered.contains(service.getName())) {
				throw new Exception("Unsupport polymorphism, "
								+ "please reengineer some methods in class '" 
								+ getClass().getName() + "'");
			}
			
			// The rules (a method is also a service) include
			// 1. it is a public method
			// 2. see <code>HandlerManager.addHandler<code> 
			if (Modifier.isPublic(service.getModifiers())
					&& configure.addHandler(serviceModule, service)) {
				m_logger.info("servelet path '" + serviceModule + "/" 
							+ service.getName()  + "' registered sucessful.");
				registered.add(service.getName());
			}
		}
	}
	
}
