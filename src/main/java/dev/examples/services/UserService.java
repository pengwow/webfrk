/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package dev.examples.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.tools.annotations.ServiceDefinition;
import com.github.webfrk.core.HttpBodyHandler;

import dev.examples.mappers.UserMapper;
import dev.examples.models.User;


/**
 * @author  wuheng(@iscas.ac.cn)
 * @since   2019.2.20
 *
 */
@ServiceDefinition
public class UserService extends HttpBodyHandler {

	@Autowired
	protected UserMapper userMapper;
	
	public void addNewUser(User user) {
		userMapper.addNewUser(user);
	}
}