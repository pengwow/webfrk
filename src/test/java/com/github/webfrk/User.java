/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.webfrk;

import java.io.InputStream;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.github.tools.annotations.api.FieldDescriber;
import com.github.tools.annotations.mysql.AutoIncrement;
import com.github.tools.annotations.mysql.DefaultValue;
import com.github.tools.annotations.mysql.JavaBean;
import com.github.tools.annotations.mysql.NotNull;
import com.github.tools.annotations.mysql.PrivateKey;
import com.github.tools.generators.SqlGenerator;

/**
 * @author wuheng
 * @since 2019.3.7
 *
 */

@JavaBean
public class User {

	@PrivateKey
	@AutoIncrement
	@FieldDescriber("用户的ID")
	private int id;

	@Size(max = 20)
	@NotNull
	@FieldDescriber("用户名")
	private String name;

	@Size(min = 8, max = 20)
	@NotNull
	@FieldDescriber("用户密码")
	private String pwd;

	@DefaultValue("11")
	@Min(8)
	@Max(100)
	@FieldDescriber("用户年龄")
	private int age;

	@DefaultValue("baseball")
	@Size(max = 200)
	@FieldDescriber("用户爱好")
	private String habits;

	@FieldDescriber("用户头像")
	private InputStream photo;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getHabits() {
		return habits;
	}

	public void setHabits(String habits) {
		this.habits = habits;
	}

	public InputStream getPhoto() {
		return photo;
	}

	public void setPhoto(InputStream photo) {
		this.photo = photo;
	}

	public static String insertUser() {
		String sql = new SQL().INSERT_INTO("user").VALUES("name", "?").VALUES("pwd", "?").VALUES("age", "?")
				.VALUES("habits", "?").VALUES("photo", "?").toString();
		return sql;
	}

	public static String selectUser() {
		String sql = new SQL().SELECT("*").FROM("user").toString();
		return sql;
	}

	public static String deleteUserByName() {
		String sql = new SQL().DELETE_FROM("user").WHERE("name = ?").toString();
		return sql;
	}

	public static String updateUserAge() {
		String sql = new SQL().UPDATE("user").SET("age = ?").WHERE("name = ?").toString();
		return sql;
	}

	final static String INSERT = "inserUser";

	final static String SELECT = "selectUser";

	final static String UPDATE = "updateUserAge";

	final static String DELETE = "deleteUserByName";

	public static void main(String[] args) throws Exception {

		SqlGenerator sql = new SqlGenerator("jdbc:mysql://172.17.0.2:3306/hahaha",
				Driver.class.getName(), "root", "onceas");
		sql.createCatalog();
		sql.createTable(User.class);
		System.out.println(updateUserAge());

		Configuration config = new Configuration();
		TransactionFactory tf = new JdbcTransactionFactory();

		DataSource ds = new PooledDataSource("com.mysql.cj.jdbc.Driver",
				"jdbc:mysql://172.17.0.2:3306/demo?characterEncoding=UTF-8", "root", "onceas");

		Environment environment = new Environment("development", tf, ds);
		config.setEnvironment(environment);
		config.addMapper(User.class);

		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(config);

//		initInsert(config);
//		initSelect(config);
//		initDelete(config);
//		initUpdate(config);
//
//		sqlSessionFactory = new SqlSessionFactoryBuilder().build(config);
//		SqlSession session = sqlSessionFactory.openSession();

//		session.insert(INSERT, createUser());

//		List<User> users = session.selectList(SELECT);
//		System.out.println(users.get(0).getAge());

//		session.delete(DELETE, createUser());
//		session.update(UPDATE, createUser2());
//
//		session.commit();
	}

	// =============================================================================
	private static void initInsert(Configuration config) {
		MappedStatement cms = createInsertMappedStatement(config, registerInsertParameters(config));
		config.addMappedStatement(cms);
		config.addKeyGenerator(INSERT, Jdbc3KeyGenerator.INSTANCE);
	}

	private static MappedStatement createInsertMappedStatement(Configuration config, List<ParameterMapping> list) {
		MappedStatement.Builder builder = new MappedStatement.Builder(config, INSERT,
				new StaticSqlSource(config, insertUser(), list), SqlCommandType.INSERT);
		MappedStatement ms = builder.build();
		builder.keyProperty("name, pwd, age, habits, photo");
		ParameterMap parameterMap = new ParameterMap.Builder(config, INSERT, User.class, list).build();
		builder.parameterMap(parameterMap);
		builder.keyGenerator(Jdbc3KeyGenerator.INSTANCE);
		return ms;
	}

	private static List<ParameterMapping> registerInsertParameters(Configuration config) {
		List<ParameterMapping> list = new ArrayList<ParameterMapping>();
		list.add(new ParameterMapping.Builder(config, "name", String.class).build());
		list.add(new ParameterMapping.Builder(config, "pwd", String.class).build());
		list.add(new ParameterMapping.Builder(config, "age", Integer.class).build());
		list.add(new ParameterMapping.Builder(config, "habits", String.class).build());
		list.add(new ParameterMapping.Builder(config, "photo", InputStream.class).build());
		return list;
	}

	// =============================================================================

	private static void initSelect(Configuration config) {
		MappedStatement cms = createSelectMappedStatement(config, registerSelectParameters(config));
		config.addMappedStatement(cms);
		config.addKeyGenerator(SELECT, Jdbc3KeyGenerator.INSTANCE);
	}

	private static MappedStatement createSelectMappedStatement(Configuration config, List<ParameterMapping> list) {
		MappedStatement.Builder builder = new MappedStatement.Builder(config, SELECT,
				new StaticSqlSource(config, selectUser(), list), SqlCommandType.SELECT);
		MappedStatement ms = builder.build();
		ParameterMap parameterMap = new ParameterMap.Builder(config, SELECT, User.class, list).build();
		builder.parameterMap(parameterMap);
		builder.keyGenerator(Jdbc3KeyGenerator.INSTANCE);
		List<ResultMap> resultMaps = new ArrayList<ResultMap>();
		List<ResultMapping> mapping = new ArrayList<ResultMapping>();
		resultMaps.add(new ResultMap.Builder(config, SELECT, User.class, mapping).build());
		builder.resultMaps(resultMaps);
		return ms;
	}

	private static List<ParameterMapping> registerSelectParameters(Configuration config) {
		List<ParameterMapping> list = new ArrayList<ParameterMapping>();
		return list;
	}

	// =============================================================================

	private static void initDelete(Configuration config) {
		MappedStatement cms = createDeleteMappedStatement(config, registerDeleteParameters(config));
		config.addMappedStatement(cms);
		config.addKeyGenerator(DELETE, Jdbc3KeyGenerator.INSTANCE);
	}

	private static MappedStatement createDeleteMappedStatement(Configuration config, List<ParameterMapping> list) {
		MappedStatement.Builder builder = new MappedStatement.Builder(config, DELETE,
				new StaticSqlSource(config, deleteUserByName(), list), SqlCommandType.DELETE);
		MappedStatement ms = builder.build();
		builder.keyProperty("name");
		ParameterMap parameterMap = new ParameterMap.Builder(config, DELETE, User.class, list).build();
		builder.parameterMap(parameterMap);
		builder.keyGenerator(Jdbc3KeyGenerator.INSTANCE);
		return ms;
	}

	private static List<ParameterMapping> registerDeleteParameters(Configuration config) {
		List<ParameterMapping> list = new ArrayList<ParameterMapping>();
		list.add(new ParameterMapping.Builder(config, "name", String.class).build());
		return list;
	}

	// =============================================================================

	private static void initUpdate(Configuration config) {
		MappedStatement cms = createUpdateMappedStatement(config, registerUpdateParameters(config));
		config.addMappedStatement(cms);
		config.addKeyGenerator(UPDATE, Jdbc3KeyGenerator.INSTANCE);
	}

	private static MappedStatement createUpdateMappedStatement(Configuration config, List<ParameterMapping> list) {
		MappedStatement.Builder builder = new MappedStatement.Builder(config, UPDATE,
				new StaticSqlSource(config, updateUserAge(), list), SqlCommandType.UPDATE);
		MappedStatement ms = builder.build();
		builder.keyProperty("age,name");
		ParameterMap parameterMap = new ParameterMap.Builder(config, UPDATE, User.class, list).build();
		builder.parameterMap(parameterMap);
		builder.keyGenerator(Jdbc3KeyGenerator.INSTANCE);
		return ms;
	}

	private static List<ParameterMapping> registerUpdateParameters(Configuration config) {
		List<ParameterMapping> list = new ArrayList<ParameterMapping>();
		list.add(new ParameterMapping.Builder(config, "age", Integer.class).build());
		list.add(new ParameterMapping.Builder(config, "name", String.class).build());
		return list;
	}

	// =============================================================================
	private static User createUser() {
		User user = new User();
		user.setName("henry");
		user.setAge(11);
		user.setPwd("123456");
		return user;
	}

	private static User createUser2() {
		User user = new User();
		user.setName("henry");
		user.setAge(12);
		user.setPwd("123456");
		return user;
	}
}
