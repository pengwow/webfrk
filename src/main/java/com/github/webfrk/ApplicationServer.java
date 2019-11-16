/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.webfrk;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wuheng@otcaix.iscas.ac.cn
 * @author xuyuanjia2017@otcaix.iscas.ac.cn
 * @since 2019/11/16
 * 
 * <p>
 * The {@code ApplicationServer} class is used for starting web applications.
 * Please configure <code>src/main/resources/application.yml<code> and 
 * <code>src/main/resources/log4j.properties<code> first.
 * 
 * <p>
 * Note: if you do not need 
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages= {"com.github.webfrk", "dev.examples.services"})
@MapperScan(basePackages= {"dev.examples.mappers"})
public class ApplicationServer implements WebMvcConfigurer {

	public final static Logger m_logger = Logger.getLogger(ApplicationServer.class);
	
	/**
	 * program entry point
	 * 
	 * @param args default is null
	 */
	public static void main(String[] args) {
		SpringApplication.run(ApplicationServer.class, args);
	}
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new CorsInterceptor())
						.addPathPatterns("/**");
	}
	
	/**
	 * <p>
     * The {@code CorsInterceptor} class is used for solving the cross-domain issue.
	 */
	public static class CorsInterceptor implements HandlerInterceptor {

		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
				throws Exception {
			String origin = request.getHeader("Origin");
			response.setHeader("Access-Control-Allow-Origin", origin);
			response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " +
					"WG-App-Version, WG-Device-Id, WG-Network-Type, WG-Vendor, WG-OS-Type, WG-OS-Version, WG-Device-Model, WG-CPU, WG-Sid, WG-App-Id, WG-Token, X-token");
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setContentType("application/json;charset=UTF-8");
			m_logger.info("Target URL:" + request.getRequestURI());
			return true;
		}
	}
}
