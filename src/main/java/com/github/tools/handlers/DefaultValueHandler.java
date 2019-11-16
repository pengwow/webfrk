/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.tools.handlers;

import java.lang.reflect.Field;

import com.github.tools.annotations.mysql.DefaultValue;
import com.github.webfrk.utils.JavaUtils;

/**
 * @author wuheng
 * @since 2019.3.16
 * 
 */
public class DefaultValueHandler implements AbstractHanler {

	public void fillSql(StringBuffer sb, Object annotation, Field field) {
		sb.append(" DEFAULT ");
		DefaultValue defValue = (DefaultValue) annotation;
		String type = JavaUtils.getJavaType(field.getType());
		if (type.equals(String.class.getName())) {
			sb.append("'").append(defValue.value()).append("'");
		} else if (type.equals(Integer.class.getName())) {
			sb.append(Integer.parseInt(defValue.value()));
		} else if (type.equals(Float.class.getName())) {
			sb.append(Float.parseFloat(defValue.value()));
		} else if (type.equals(Double.class.getName())) {
			sb.append(Double.parseDouble(defValue.value()));
		}
	}

	public String toString() {
		return "";
	}
	
}
