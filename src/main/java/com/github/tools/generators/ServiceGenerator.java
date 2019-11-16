/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.tools.generators;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.tools.annotations.ServiceDefinition;
import com.github.webfrk.core.HttpBodyHandler;
import com.github.webfrk.utils.ClassUtils;
import com.github.webfrk.utils.JavaUtils;

/**
 * @author wuheng
 * @since 24
 * 
 *  mysqldump -h 172.17.0.2 -u root -p demo user> dbname_users.sql
 *  
 */

public class ServiceGenerator {
	
	public static final String rootPath = System.getProperty("user.dir") + "/src/main/java/";
	
	public static final String PACKAGE = "package PACKAGE_NAME;\n\n";
	
	public static final String IMPORT  = "import IMPORT_NAME;\n";
	
	public static final String CLASS   = "@ServiceDefinition\npublic class CLASS_NAME extends HttpBodyHandler {\n\n";
	
	public static final String FIELD   = "\t@Autowired\n\tprivate FIELD_TYPE FIELD_NAME;\n\n";
	
	public static final String METHOD  = "\tpublic METHOD_TYPE METHOD_NAME(";
	
	public static final String PROXY  = "PROXY_NAME.PROXY_METHOD(PROXY_PARAM)";
	
	public static final String RETURN = "\t\treturn ";
	
	public static final String VOID =   "\t\t ";
	
	public static final String END_PARAM  = ") {\n";
	
	public static final String END_CLASS  = "}";
	
	public static final String END_METHOD = ";\n\t}\n\n";
	
	public String createService(Class<?> clazz) throws Exception {
		StringBuffer sb = new StringBuffer();
		String mapperPath = clazz.getPackage().getName();
		int index = mapperPath.lastIndexOf(".");
		String servicePath = mapperPath.substring(0, index);
		String serviceName = clazz.getSimpleName().replace("Mapper", "Service");
		
		StringBuffer isb = new StringBuffer();
		isb.append(IMPORT.replace("IMPORT_NAME", clazz.getName()));
		isb.append(IMPORT.replace("IMPORT_NAME", Autowired.class.getName()));
		isb.append(IMPORT.replace("IMPORT_NAME", HttpBodyHandler.class.getName()));
		isb.append(IMPORT.replace("IMPORT_NAME", ServiceDefinition.class.getName()));
		Set<String> importSet = new HashSet<String>();
		
		
		String field = clazz.getSimpleName().substring(0, 1).toLowerCase() 
				+ clazz.getSimpleName().substring(1);
		
		StringBuffer msb = new StringBuffer();
		
		for (Method method : clazz.getDeclaredMethods()) {
			
			msb.append(METHOD.replace("METHOD_TYPE", method.getGenericReturnType().getTypeName())
												.replace("METHOD_NAME", method.getName()));
			
			if (method.getParameterCount() > 1) {
				addImport(isb, importSet, Map.class.getName());
				msb.append("Map<String, String> map");
			} else if (method.getParameterCount() == 1) {
				String param = method.getParameterTypes()[0].getName();
				if (!JavaUtils.isMap(param)
						&& !JavaUtils.isList(param)
						&& !JavaUtils.isSet(param)
						&& !JavaUtils.isPrimitive(param)) {
					addImport(isb, importSet, method.getParameterTypes()[0].getName());
					msb.append(method.getParameterTypes()[0].getSimpleName())
									.append(" ").append(method.getParameters()[0].getName());
				} else {
					addImport(isb, importSet, Map.class.getName());
					msb.append("Map<String, String> map");
				}
			}
			msb.append(END_PARAM);
			
			
			
			if (method.getReturnType().getSimpleName().equals("void")) {
				msb.append(VOID);
			} else {
				msb.append(RETURN);
			}
			
			String mparams = "";
			if (method.getParameterCount() > 1) {
				mparams = getParams(method);
			} else if (method.getParameterCount() == 1) {
				String param = method.getParameterTypes()[0].getName();
				if (!JavaUtils.isMap(param)
						&& !JavaUtils.isList(param)
						&& !JavaUtils.isSet(param)
						&& !JavaUtils.isPrimitive(param)) {
					mparams = method.getParameters()[0].getName();
				} else {
					mparams = getParams(method);
				}
			}
			msb.append(PROXY.replace("PROXY_NAME", field)
					.replace("PROXY_METHOD", method.getName())
					.replace("PROXY_PARAM", mparams));
			msb.append(END_METHOD);
		}
		
		
		sb.append(PACKAGE.replace("PACKAGE_NAME", servicePath));
		sb.append(isb).append("\n");
		sb.append(CLASS.replace("CLASS_NAME", serviceName));
		sb.append(FIELD.replace("FIELD_TYPE", clazz.getSimpleName()).replace("FIELD_NAME", field));
		sb.append(msb);
		sb.append(END_CLASS);
		return sb.toString();
	}

	private String getParams(Method method) throws Exception {
		StringBuffer psb = new StringBuffer();
		for (Parameter param : method.getParameters()) {
			String paramType = param.getType().getName();
			if (paramType.equals(String.class.getName())) {
				psb.append("map.get(\"").append(param.getName()).append("\"),");
			} else if (paramType.equals(Integer.class.getName())
					|| paramType.equals("int")) {
				psb.append("Integer.parseInt(map.get(\"").append(param.getName()).append("\")),");
			} else if (paramType.equals(Float.class.getName())
					|| paramType.equals("float")) {
				psb.append("Float.parseFloat(map.get(\"").append(param.getName()).append("\")),");
			} else if (paramType.equals(Double.class.getName())
					|| paramType.equals("double")) {
				psb.append("Double.parseDouble(map.get(\"").append(param.getName()).append("\")),");
			} else {
				throw new Exception("Unsupport " + paramType);
			}
		}
		return psb.substring(0, psb.length() - 1);
	}

	private void addImport(StringBuffer importSB, Set<String> importSet, String packageName) {
		if (!importSet.contains(packageName) &&
				!packageName.startsWith("java.lang") &&
				packageName.indexOf(".") != -1) {
			importSet.add(packageName);
			importSB.append(IMPORT.replace("IMPORT_NAME", packageName));
		}
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws Exception {
		ServiceGenerator sg = new ServiceGenerator();
		for (Class clazz: ClassUtils.scan("io.github.syswu.demo.mappers", Mapper.class)) {
			System.out.println(sg.createService(clazz));
		}
	}
}
