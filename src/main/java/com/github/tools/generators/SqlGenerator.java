/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.tools.generators;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Size;

import org.apache.log4j.Logger;

import com.github.tools.annotations.api.FieldDescriber;
import com.github.tools.annotations.mysql.ForeignKey;
import com.github.tools.annotations.mysql.JavaBean;
import com.github.tools.annotations.mysql.PrivateKey;
import com.github.tools.handlers.AbstractHanler;
import com.github.webfrk.utils.JavaUtils;
import com.mysql.cj.jdbc.Driver;

import dev.examples.models.User;


/**
 * @author wuheng
 * @since 2019.3.16
 * 
 *  mysqldump -h 172.17.0.2 -u root -p demo user> dbname_users.sql
 *  
 *  <>br
 * 数据类型 	指定值和范围<br>
 * char 	      String(0~255) <br>
 * varchar 	   String(0~255) <br>
 * tinytext 	String(0~255) <br>
 * text 	      String(0~65536) <br>
 * blob 	      String(0~65536) <br>
 * mediumtext 	String(0~16777215) <br>
 * mediumblob 	String(0~16777215) <br>
 * longblob 	String(0~4294967295) <br>
 * longtext 	String(0~4294967295) <br>
 * tinyint 	Integer(-128~127) <br>
 * smallint 	Integer(-32768~32767) <br>
 * mediumint 	Integer(-8388608~8388607) <br>
 * int 	      Integer(-214847668~214847667) <br>
 * bigint 	Integer(-9223372036854775808~9223372036854775807) <br>
 * float 	decimal(精确到23位小数) <br>
 * double 	decimal(24~54位小数) <br>
 * decimal 	将double转储为字符串形式 <br>
 * date 	       YYYY-MM-DD <br>
 * datetime 	YYYY-MM-DD HH:MM:SS <br>
 * timestamp 	YYYYMMDDHHMMSS <br>
 * time 	      HH:MM:SS <br>
 * enum 	      选项值之一 <br>
 * set 	      选项值子集 <br>
 * boolean 	tinyint(1) <br>
 *  
 */

public class SqlGenerator {
	
	/**
	 * m_logger
	 */
	public final static Logger m_logger = Logger.getLogger(SqlGenerator.class);

	/**
	 * database name
	 */
	protected String dbName;

	/**
	 * connection
	 */
	protected final Connection conn;

	/**
	 * @param jdbcUrl    jdbc
	 * @param driver     driver
	 * @param username   username
	 * @param password   password
	 * @throws Exception unable to init mysql client
	 */
	public SqlGenerator(String jdbcUrl, 
								String driver, 
								String username, 
								String password) throws Exception {
		Class.forName(driver);
		this.dbName = getDBName(jdbcUrl);
		this.conn = DriverManager.getConnection(
				jdbcUrl.substring(0, jdbcUrl.length() - dbName.length()), 
				username, password);
	}

	/**
	 * @param dbName database name
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * @return database name
	 */
	public String getDbName() {
		return dbName;
	}

	/**
	 * @return connection
	 */
	public Connection getConnection() {
		return conn;
	}

	/*******************************************
	 * 
	 *   Database operates
	 *
	 *********************************************/

	/**
	 * @return true if database exists, otherwise return false
	 * @throws Exception mysql exception
	 */
	public boolean existCatalog() throws Exception {
		String sql = "SELECT * FROM information_schema.SCHEMATA where SCHEMA_NAME='" + dbName + "'";
		m_logger.info("Check database:" + sql);
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		return rs.next();
	}

	/**
	 * create database
	 * 
	 * @throws Exception mysql exception
	 */
	public void createCatalog() throws Exception {
		String sql = "CREATE DATABASE " + dbName;
		m_logger.info("Create database:" + sql);
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.execute();
	}

	/**
	 * @return delete database
	 * @throws Exception mysql exception
	 */
	public boolean DropCatalog() throws Exception {
		String sql = "DROP DATABASE " + dbName;
		m_logger.info("Drop database:" + sql);
		PreparedStatement pstmt = conn.prepareStatement(sql);
		return pstmt.execute();
	}

	/*******************************************************
	 * 
	 * Table operates
	 * 
	 *********************************************************/
	
	
	/**
	 * @param clazz  class
	 * @return true if the table exists, otherwise return false
	 * @throws Exception mysql exception
	 */
	public boolean existTable(Class<?> clazz) throws Exception {
		return existTable(clazz.getSimpleName().toLowerCase());
	}
	
	/**
	 * @param name  class name
	 * @return true if the table exists, otherwise return false
	 * @throws Exception mysql exception
	 */
	public boolean existTable(String name) throws Exception {
		String sql = "SELECT DISTINCT t.table_name, n.SCHEMA_NAME FROM "
				+ "information_schema.TABLES t, information_schema.SCHEMATA n "
				+ "WHERE t.table_name = '" + name + "' AND n.SCHEMA_NAME = '" + dbName + "'";
		m_logger.info("Check table:" + sql);
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		return rs.next();
	}

	/**
	 * @param clazz    create table based on class
	 * @throws Exception  mysql exception
	 */
	public void createTable(Class<?> clazz) throws Exception {
		String sql = getCreateTableSql(clazz);
		conn.setCatalog(dbName);
		PreparedStatement pstmt = conn.prepareStatement(sql); 
		pstmt.execute();
	}

	/**
	 * @param clazz class
	 * @return sql
	 * @throws Exception mysql exception
	 */
	public static String getCreateTableSql(Class<?> clazz) throws Exception {
		StringBuffer sb = new StringBuffer().append("CREATE TABLE ")
				.append(clazz.getSimpleName().toLowerCase()).append("(");
		
		List<String> privateKeys = new ArrayList<String>();
		List<String> foreignKeys = new ArrayList<String>();
		
		for (Field field : clazz.getDeclaredFields()) {
			// filter static
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			
			// item name
			String type = JavaUtils.getJavaType(field.getType());
			sb.append(field.getName()).append(" ").append(getJDBCTypeFrom(type));
			if (type.equals(String.class.getName())) {
				Size size = field.getAnnotation(Size.class);
				if (size == null || size.max() == Integer.MAX_VALUE) {
					m_logger.info("You need to set the max size of item " + field.getName());
					System.exit(1);
				}
				sb.append("(").append(size.max()).append(")");
			}
			
			// item desc
			Annotation[] annotations = field.getDeclaredAnnotations();
			for (Annotation annotation : annotations) {
				String annotationName = annotation.annotationType().getName();
				if (annotationName.equals(FieldDescriber.class.getName())) {
					continue;
				} else if (annotationName.equals(PrivateKey.class.getName())) {
					privateKeys.add(field.getName());
				} else if (annotationName.equals(ForeignKey.class.getName())) {
					ForeignKey fk = (ForeignKey) annotation;
					foreignKeys.add("foreign key(" + field.getName() + ") references"
							+ fk.table()+"("+ fk.item()+ "),");
				} else if (annotationName.startsWith(JavaBean.class.getPackage().getName())) {
					Method method = annotation.annotationType().getMethod("handler");
					Class<?> handlerClass = (Class<?>) method.invoke(annotation);
					AbstractHanler handler = (AbstractHanler) handlerClass.newInstance();
					handler.fillSql(sb, annotation, field);
				}
			}
			
			// default
			sb.append(",");
		}
		
		// private and foreign keys
		if (privateKeys.size() > 0) {
			sb.append("primary key(");
			for (String pk : privateKeys) {
				sb.append(pk).append(",");
			}
			sb.delete(sb.length()-1, sb.length());
			sb.append("),");
		}
		
		for (String fk : foreignKeys) {
			sb.append(fk);
		}
		// delete ','
		sb.delete(sb.length()-1, sb.length())
					.append(")ENGINE=InnoDB DEFAULT CHARSET=utf8");
		m_logger.info("Create table:" + sb.toString());
		return sb.toString();
	}


	/** 
	 * 
	 * @param url url
	 * @return get database name
	 */ 
	protected static String getDBName(String url) {
		int idx = url.lastIndexOf("/");
		return url.substring(idx + 1);
	}

	/**
	 * mapping
	 */
	private static final Map<String, String> mapping = new HashMap<String, String>();
	
	static {
		mapping.put(String.class.getName(), "VARCHAR");
		mapping.put(Integer.class.getName(), "INT");
		mapping.put(Float.class.getName(), "FLOAT");
		mapping.put(Double.class.getName(), "DOUBLE");
		mapping.put(InputStream.class.getName(), "BLOB");
		mapping.put(Date.class.getName(), "datetime");
	}
	
	/**
	 * @param javaType java type
	 * @return jdbc type from java type
	 */
	protected static String getJDBCTypeFrom(String javaType) {
		return mapping.get(javaType);
	}
	
	public static void main(String[] args) throws Exception {
		SqlGenerator sg = new SqlGenerator("jdbc:mysql://127.0.0.1:3306/test",
				Driver.class.getName(), "root", "onceas");
		sg.createCatalog();
		sg.createTable(User.class);
	}
	
}
