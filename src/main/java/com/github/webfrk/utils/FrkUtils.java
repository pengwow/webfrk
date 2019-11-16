/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.webfrk.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.alibaba.fastjson.JSON;
import com.github.webfrk.core.HttpBodyHandler;

/**
 * @author  wuheng
 * @since   2019.2.20
 */
public class FrkUtils {

	/**********************************
	 *  String utils
	 **********************************/
	
	/**
	 * convert Java object to JSON String
	 * 
	 * @param obj   obejct
	 * @return      return JSON String   
	 */
	public static String toJSONString(Object obj) {
		if (isNull(obj)) {
			throw new NullPointerException("Object is null");
		}
		return JSON.toJSONString(obj);
	}

	/**
	 * check whether the object is empty or not
	 * 
	 * @param obj  object
	 * @return     return true if 'obj' is not null, otherwise return false
	 */
	public static boolean isNull (Object obj) {
		return (obj == null) ? true : false;
	}
	
	/**
	 * @param name  object name
	 * @return      lowercase
	 */
	public static String getServiceModule(String classname) {
		String name = classname.substring(0, classname.length() - HttpBodyHandler.POSTFIX.length());
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}
	
	/**
	 * @param method     method name
	 * @return           service type name
	 */
	public static String getServiceType(Method method) {
		String methodName = method.getName();
		return methodName.substring(0, 
				methodName.length() - "Request".length());
	}
	
	/**********************************
	 *  JSR 303
	 **********************************/
	
	protected static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	public static <T> ValidationResult validateEntity(T obj) {
		ValidationResult result = new ValidationResult();
		Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);

		if (set != null && set.size() != 0) {

			result.setHasErrors(true);
			Map<String, String> errorMsg = new HashMap<String, String>();

			for (ConstraintViolation<T> cv : set) {
				errorMsg.put(cv.getPropertyPath().toString(), cv.getMessage());
			}
			result.setErrorMsg(errorMsg);
		}
		return result;
	}
	
	public static class ValidationResult {
		 
	    private boolean             hasErrors;
	 
	    private Map<String, String> errorMsg;

		public boolean isHasErrors() {
			return hasErrors;
		}

		public void setHasErrors(boolean hasErrors) {
			this.hasErrors = hasErrors;
		}

		public Map<String, String> getErrorMsg() {
			return errorMsg;
		}

		public void setErrorMsg(Map<String, String> errorMsg) {
			this.errorMsg = errorMsg;
		}
	    
	}
}
