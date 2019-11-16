/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.tools.generators;

import java.util.ArrayList;
import java.util.List;

import com.github.webfrk.utils.FileUtils;

/**
 * @author wuheng@otcaix.iscas.ac.cn
 * @since  2019/11/16
 * 
 *  mysqldump -h 172.17.0.2 -u root -p demo user> dbname_users.sql
 */

public class SpringBootGenerator {
	
	public static final String CODE_PATH   = System.getProperty("user.dir") + "/src/main/java/";
	
	public static final String CONFIG_PATH    = System.getProperty("user.dir") + "/src/main/resources/";

	protected final BootClass bootClass;
	
	protected final YML yml;
	
	protected final LOG4J log4j = new LOG4J();
	
	public SpringBootGenerator(BootClass bootClass, YML yml) {
		super();
		this.bootClass = bootClass;
		this.yml = yml;
	}
	
	public void generate() throws Exception {
		System.out.println("generating ApplicationServer.class");
		FileUtils.createFile(CODE_PATH, "ApplicationServer.java", bootClass.generate());
		System.out.println("generating application.yml");
		FileUtils.createFile(CONFIG_PATH, "application.yml", yml.generate());
		System.out.println("generating log4j.properties");
		FileUtils.createFile(CONFIG_PATH, "log4j.properties", log4j.generate());
	}

	/**
	 * generate ApplicationServer, which is used for starting web.
	 *
	 */
	public static class BootClass {
		
		public static final String COPYRIGHT   = "/**\r\n" + 
				" * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences\r\n" + 
				" */\r\n";
		
		public static String PACKAGE     = "package PACKAGE_NAME;\n\n";
		
		public static String IMPORT      = "import javax.servlet.http.HttpServletRequest;\r\n" + 
				"import javax.servlet.http.HttpServletResponse;\r\n" + 
				"\r\n" + 
				"import org.apache.log4j.Logger;\r\n" + 
				"import org.springframework.boot.SpringApplication;\r\n" + 
				"import org.springframework.boot.autoconfigure.EnableAutoConfiguration;\r\n" + 
				"import org.springframework.boot.autoconfigure.SpringBootApplication;\r\n" + 
				"import org.springframework.context.annotation.ComponentScan;\r\n" + 
				"import org.springframework.web.servlet.HandlerInterceptor;\r\n" + 
				"import org.springframework.web.servlet.config.annotation.InterceptorRegistry;\r\n" + 
				"import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;\r\n";
		
		public static final String AUTHOR      = "/**\r\n" + 
				" * @author wuheng@otcaix.iscas.ac.cn\r\n" + 
				" * @author xuyuanjia2017@otcaix.iscas.ac.cn\r\n" + 
				" * @since 2019/11/16\r\n" + 
				" * \r\n" + 
				" * <p>\r\n" + 
				" * The {@code ApplicationServer} class is used for starting web applications.\r\n" + 
				" * Please configure <code>src/main/resources/application.yml<code> and \r\n" + 
				" * <code>src/main/resources/log4j.properties<code> first.\r\n" + 
				" * \r\n" + 
				" * <p>\r\n" + 
				" * Note: if you do not need \r\n" + 
				" */\r\n";
		
		public static final String CLASS        = "public class ApplicationServer implements WebMvcConfigurer {\r\n" + 
				"\r\n" + 
				"	public final static Logger m_logger = Logger.getLogger(ApplicationServer.class);\r\n" + 
				"	\r\n" + 
				"	/**\r\n" + 
				"	 * program entry point\r\n" + 
				"	 * \r\n" + 
				"	 * @param args default is null\r\n" + 
				"	 */\r\n" + 
				"	public static void main(String[] args) {\r\n" + 
				"		SpringApplication.run(ApplicationServer.class, args);\r\n" + 
				"	}\r\n" + 
				"	@Override\r\n" + 
				"	public void addInterceptors(InterceptorRegistry registry) {\r\n" + 
				"		registry.addInterceptor(new CorsInterceptor())\r\n" + 
				"						.addPathPatterns(\"/**\");\r\n" + 
				"	}\r\n" + 
				"	\r\n" + 
				"	/**\r\n" + 
				"	 * <p>\r\n" + 
				"     * The {@code CorsInterceptor} class is used for solving the cross-domain issue.\r\n" + 
				"	 */\r\n" + 
				"	public static class CorsInterceptor implements HandlerInterceptor {\r\n" + 
				"\r\n" + 
				"		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)\r\n" + 
				"				throws Exception {\r\n" + 
				"			String origin = request.getHeader(\"Origin\");\r\n" + 
				"			response.setHeader(\"Access-Control-Allow-Origin\", origin);\r\n" + 
				"			response.setHeader(\"Access-Control-Allow-Headers\", \"Origin, X-Requested-With, Content-Type, Accept, \" +\r\n" + 
				"					\"WG-App-Version, WG-Device-Id, WG-Network-Type, WG-Vendor, WG-OS-Type, WG-OS-Version, WG-Device-Model, WG-CPU, WG-Sid, WG-App-Id, WG-Token, X-token\");\r\n" + 
				"			response.setHeader(\"Access-Control-Allow-Methods\", \"POST, GET, PUT, DELETE\");\r\n" + 
				"			response.setHeader(\"Access-Control-Allow-Credentials\", \"true\");\r\n" + 
				"			response.setContentType(\"application/json;charset=UTF-8\");\r\n" + 
				"			m_logger.info(\"Target URL:\" + request.getRequestURI());\r\n" + 
				"			return true;\r\n" + 
				"		}\r\n" + 
				"	}\r\n" + 
				"}";
		
		protected List<String> services;
		
		protected List<String> mappers;

		public BootClass(String pkgName) {
			this(pkgName, null, null);
		}
		
		public BootClass(String pkgName, List<String> services) {
			this(pkgName, services, null);
		}
		
		public BootClass(String pkgName, List<String> services, List<String> mappers) {
			
			if (pkgName == null) {
				throw new RuntimeException();
			}
			
			this.services = (services == null ) ? new ArrayList<String>(): services;
			this.mappers = (mappers == null ) ? new ArrayList<String>(): mappers;
			IMPORT = (mappers == null) ? IMPORT : IMPORT 
					+ "import org.mybatis.spring.annotation.MapperScan;\r\n";
			PACKAGE = PACKAGE.replace("PACKAGE_NAME", pkgName);
		}
		
		public String generate() {
			
			StringBuffer sb = new StringBuffer();
			sb.append(COPYRIGHT).append(IMPORT).append(AUTHOR);
			
			
			sb.append("@SpringBootApplication\n");
			sb.append("@EnableAutoConfiguration\n");
			sb.append("@ComponentScan(basePackages= {\"com.github.webfrk\"");
			for (String pkg : services) {
				sb.append(", \"" + pkg + "\"");
			}
			sb.append("})\n");
			
			if (mappers.size() != 0) {
				sb.append("@MapperScan(basePackages= {");
				for (String mapper : mappers) {
					sb.append("\"" + mapper + "\", ");
				}
				sb.deleteCharAt(sb.length() - 2).append("})\n");
			}
			
			
			sb.append(CLASS);
			return sb.toString();
		}
	}
	
	/**
	 * generate application.yml.
	 *
	 */
	public static class YML {
		
		public static final String YML_SERVER     = "server:\r\n" + 
				"  port: 8080\r\n" + 
				"  servlet:\r\n" + 
				"    context-path: /webfrk/1.0.0\r\n" + 
				"\r\n" + 
				"logging:\r\n" + 
				"  config: classpath:log4j.properties\n";
		
		public static String YML_MYSQL      = "spring:\r\n" + 
				"   main:\r\n" + 
				"    allow-bean-definition-overriding: true\r\n" + 
				"   datasource:\r\n" + 
				"     url: JDBC\r\n" + 
				"     username: USER\r\n" + 
				"     password: PWD\r\n" + 
				"     driverClassName: com.mysql.cj.jdbc.Driver";
		
		protected YML() {
			this(null, null, null);
		}
		
		protected YML(String url, String user, String pwd) {
			if (url == null && user == null && pwd == null) {
				YML_MYSQL = "";
			} else if (url != null && user != null && pwd != null) {
				YML_MYSQL.replace("JDBC", url)
					.replace("USER", user)
					.replace("PWD", pwd);
			} else {
				throw new RuntimeException();
			}
		}
		
		public String generate() {
			StringBuffer sb = new StringBuffer();
			sb.append(YML_SERVER).append(YML_MYSQL);
			return sb.toString();
		}
		
	}
	
	/**
	 * generate log4j.properties
	 *
	 */
	public static class LOG4J {
		
		public static final String CONFIG_LOG4J   = "# Set root logger level to DEBUG and its only appender to A1.\r\n" + 
				"log4j.rootLogger=INFO, A1\r\n" + 
				"\r\n" + 
				"# A1 is set to be a ConsoleAppender.\r\n" + 
				"log4j.appender.A1=org.apache.log4j.ConsoleAppender\r\n" + 
				"\r\n" + 
				"# A1 uses PatternLayout.\r\n" + 
				"log4j.appender.A1.Target=System.out\r\n" + 
				"log4j.appender.A1.layout=org.apache.log4j.PatternLayout\r\n" + 
				"log4j.appender.A1.layout.ConversionPattern=%-4r [%t] %-5p %c %x - %m%n\r\n" + 
				"\r\n" + 
				"# An alternative logging format:\r\n" + 
				"# log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1} - %m%n";
		
		public String generate() {
			return CONFIG_LOG4J;
		}
	}
	
	public static void main(String[] args) throws Exception {
		List<String> sers = new ArrayList<String>();
		sers.add("dev.examples.services");
		
		List<String> maps = new ArrayList<String>();
		maps.add("dev.examples.mappers");
		
		SpringBootGenerator.BootClass bc = new SpringBootGenerator.BootClass("com.github.webfrk", sers, maps);
		bc.generate();
		
		@SuppressWarnings("unused")
		String jdbc = "jdbc:mysql://127.0.0.1:3306/test";
		
		SpringBootGenerator.YML yml = new SpringBootGenerator.YML();
		yml.generate();
	}
}
