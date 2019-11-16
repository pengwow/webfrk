/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package dev.examples.services;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.github.tools.annotations.ServiceDefinition;
import com.github.webfrk.core.HttpBodyHandler;


/**
 * @author  wuheng(@iscas.ac.cn)
 * @since   2019.2.20
 *
 */
@ServiceDefinition
public class MockService extends HttpBodyHandler {

	public String createMock(@Valid Mocker mocker) {
		return mocker.getName();
	}
	
	public String getMock(Map<String, String> map) {
		return map.get("dep");
	}
	
	public String listMock() {
		return "mocker";
	}
	
	public String listMock3(Map<String, String> map) {
		return map.get("a");
	}
	
	public String deleteMock(@NotNull String name) {
		return name;
	} 
	
	public static class Mocker {
		
		@Length(min = 10, max = 20, message = "{user.name.length.illegal}")  
		protected String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
}