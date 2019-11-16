/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.tools.generators;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import com.github.tools.annotations.api.FieldDescriber;
import com.github.webfrk.utils.JavaUtils;
import com.github.webfrk.utils.JsonUtils;

import dev.examples.mappers.UserMapper;

/**
 * @author wuheng
 * @since 24
 * 
 *  mysqldump -h 172.17.0.2 -u root -p demo user> dbname_users.sql
 *  
 */

public class APIGenerator {
	
	final static String URL = "URL_PREFIX/SERVICE_TYPE/SERVICE_NAME\n\n";
	
	final static String JSON = "\n```\nJSON\n```\n\n";
	
	public String createAPI(String prefix, Class<?> clazz) throws Exception {
		
		if (prefix.endsWith("/")) {
			prefix = prefix.substring(0, prefix.length() - 1);
		}
		
		String mapperName = clazz.getSimpleName();
		String serviceType = mapperName.substring(0, mapperName.length() - "Mapper".length());
		
		
		StringBuffer sb = new StringBuffer();
		for (Method method : clazz.getDeclaredMethods()) {
			sb.append("## ").append(method.getAnnotation(FieldDescriber.class).value()).append("\n\n");
			
			String mn = method.getName();
			if (mn.startsWith("list") 
					|| mn.startsWith("describe")
					|| mn.startsWith("retrieve")
					|| mn.startsWith("get")) {
				sb.append("GET:");
			} else if (mn.startsWith("add")
					|| mn.startsWith("create")
					|| mn.startsWith("new")) {
				sb.append("POST:");
			} else if (mn.startsWith("remove")
					|| mn.startsWith("delete")) {
				sb.append("POST or DELETE:");
			} else if (mn.startsWith("update")
					|| mn.startsWith("modify")
					|| mn.startsWith("replace")) {
				sb.append("POST or PUT:");
			} else {
				continue;
			}
			
			sb.append(URL.replace("URL_PREFIX", prefix)
											.replace("SERVICE_TYPE", serviceType)
											.replace("SERVICE_NAME", method.getName()));
			
			
			if (method.getParameterCount() == 1) {
				String param = method.getParameterTypes()[0].getName();
				if (!JavaUtils.isMap(param)
						&& !JavaUtils.isList(param)
						&& !JavaUtils.isSet(param)
						&& !JavaUtils.isPrimitive(param)) {
					sb.append(JSON.replace("JSON", JsonUtils.createJSON(method.getParameterTypes()[0])));
				} else {
					Map<String, String> map = new HashMap<String, String>();
					map.put(param, method.getParameterTypes()[0].getName());
					sb.append(JSON.replace("JSON", JsonUtils.createJSON(map)));
				}
			} else if (method.getParameterCount() > 1) {
				Map<String, String> map = new HashMap<String, String>();
				for (Parameter param : method.getParameters()) {
					map.put(param.getName(), param.getType().getName());
				}
				sb.append(JSON.replace("JSON", JsonUtils.createJSON(map)));
			}
		}
		
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		APIGenerator ag = new APIGenerator();
		System.out.println(ag.createAPI("http://127.0.0.1:9090/application/v2.0", UserMapper.class));
	}
}
