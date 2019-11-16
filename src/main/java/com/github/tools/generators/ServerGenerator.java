/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.tools.generators;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.github.tools.annotations.ServiceDefinition;

/**
 * @author wuheng
 * @since 24
 * 
 *  mysqldump -h 172.17.0.2 -u root -p demo user> dbname_users.sql
 *  
 */

public class ServerGenerator {
	
	public static final String SERVER_NAME = "ApplicationServer";
	
	public static final String rootPath = System.getProperty("user.dir") + "/src/main/java/";
	
	public static final String PACKAGE = "package PACKAGE_NAME;\n\n";
	
	public static final String IMPORT  = "import IMPORT_NAME;\n";
	
	public static final String CLASS   = "@ServiceDefinition\npublic class ApplicationServer {\n\n";
	
	public static final String METHOD  = "\tpublic static void main(String[] args) {\n";
	
	public static final String END_CLASS  = "}";
	
	public static final String END_METHOD = "\t}\n\n";
	
	public String createServer(String packageName) throws Exception {
		return createServer(packageName, null);
	}
	
	public String createServer(String packageName, String mapperPackage) throws Exception {
		StringBuffer sb = new StringBuffer();
		
		sb.append(PACKAGE.replace("PACKAGE_NAME", packageName));
		
		if (mapperPackage != null) {
			sb.append(IMPORT.replace("IMPORT_NAME", "org.mybatis.spring.annotation.MapperScan"));
		}
		sb.append(IMPORT.replace("IMPORT_NAME", SpringApplication.class.getName()));
		sb.append(IMPORT.replace("IMPORT_NAME", EnableAutoConfiguration.class.getName()));
		sb.append(IMPORT.replace("IMPORT_NAME", SpringBootApplication.class.getName()));
		sb.append(IMPORT.replace("IMPORT_NAME", ComponentScan.class.getName()));
		sb.append(IMPORT.replace("IMPORT_NAME", ServiceDefinition.class.getName()));
		
		
		sb.append("\n\n");
		sb.append("@SpringBootApplication\n");
		sb.append("@EnableAutoConfiguration\n");
		
		sb.append("@ComponentScan(basePackages= {\"").append(packageName).append("\"")
						.append(",\"").append("com.github.kubesys.webfrk").append("\"})\n");
		
		if (mapperPackage != null) {
			sb.append("@MapperScan(basePackages= {\"").append(mapperPackage).append("\"})\n");
		}
		
		sb.append(CLASS);
		sb.append(METHOD);
		sb.append("\t\tSpringApplication.run(ApplicationServer.class, args);\n");
		sb.append(END_METHOD);
		sb.append(END_CLASS);
	
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		ServerGenerator sg = new ServerGenerator();
		System.out.println(sg.createServer("io.github.syswu.demo", "io.github.syswu.demo.mappers"));
	}
}
