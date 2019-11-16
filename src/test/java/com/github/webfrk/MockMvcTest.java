/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.webfrk;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.github.webfrk.core.HttpConstants;


/**
 * @author wuheng@otcaix.iscas.ac.cn
 * @since  2019.11.16
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.github.webfrk.core.HttpController.class)
@AutoConfigureMockMvc
@ComponentScan(basePackages= {"com.github.kubesys.webfrk", "io.github.syswu"})
public class MockMvcTest  {

	public final static String INVALID_GET_REQUEST_PATH = "/mock/listMock2";
	
	public final static String VALID_GET_REQUEST_PATH = "/mock/listMock";
	
	@Autowired
	private MockMvc mvc;

	@Test
	public void testInvalidGetRequestBody() throws Exception {
		@SuppressWarnings("deprecation")
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(
							INVALID_GET_REQUEST_PATH).accept(MediaType.APPLICATION_JSON_UTF8);
		mvc.perform(builder)
				.andExpect(status().isOk())
				.andExpect(jsonPath("status").value(HttpConstants.HTTP_RESPONSE_STATUS_FAILED))
				.andExpect(jsonPath("message").value(HttpConstants.EXCEPTION_INVALID_REQUEST_URL));
	}
	
	@Test
	public void testValidGetRequestBody() throws Exception {
		@SuppressWarnings("deprecation")
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(
				VALID_GET_REQUEST_PATH).accept(MediaType.APPLICATION_JSON_UTF8);
		mvc.perform(builder)
				.andExpect(status().isOk())
				.andExpect(jsonPath("status").value(HttpConstants.HTTP_RESPONSE_STATUS_OK))
				.andExpect(jsonPath("result").value("mocker"));
	}
	
}
