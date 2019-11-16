/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.tools.generators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Description;

import com.github.tools.annotations.api.FieldDescriber;
import com.github.tools.annotations.mysql.JavaBean;

import dev.examples.models.User;

/**
 * @author wuheng
 * @since 2019.3.17
 * 
 *  mysqldump -h 172.17.0.2 -u root -p demo user> dbname_users.sql
 *  
 */

public class DocGenerator {
	
	public String createWiki(Class<?> clazz) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("## 数据表 ").append(clazz.getSimpleName().toLowerCase()).append("\n");
		sb.append("\n").append("SQL语句").append("\n");
		sb.append("\n").append("```");
		for (String str : SqlGenerator.getCreateTableSql(clazz).split(",")) {
			sb.append("\n").append(str).append(",");
		}
		sb.delete(sb.length()-1, sb.length()).append("\n").append("```");
		
		List<Field> filterFields = new ArrayList<Field>();
		for (Field field : clazz.getDeclaredFields()) {
			if (!Modifier.isStatic(field.getModifiers())) {
				filterFields.add(field);
			}
		}
		
		List<String> categories = new ArrayList<String>();
		categories.add("名称");
		categories.add("类型");
		categories.add("描述");
		
		sb.append("\n").append("|");
		// title
		for (int i = 0; i < categories.size(); i++) {
			sb.append(categories.get(i)).append("\t|");
		}
		sb.append("\n").append("|");
		// align
		for (int i = 0; i < categories.size(); i++) {
			sb.append(" ----- |");
		}
		for (Field f : filterFields) {
			sb.append("\n").append("|");
			sb.append(f.getName()).append("|");
			
			Annotation[] as = f.getAnnotations();
			for (Annotation annotation : as) {
				String annotationName = annotation.annotationType().getName();
				if (annotationName.equals(FieldDescriber.class.getName())) {
					continue;
				} else if (annotationName.startsWith(JavaBean.class.getPackage().getName())) {
					sb.append(annotation.annotationType().getMethod("desc").invoke(annotation)).append(",");
				}
			}
			
			if (sb.charAt(sb.length()-1) == ',') {
				sb.delete(sb.length()-1, sb.length()).append("|");
			} else {
				sb.append("|");
			}
			
			Description desc = f.getAnnotation(Description.class);
			sb.append(desc.value()).append("|");
		}
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		DocGenerator wg = new DocGenerator();
		System.out.println(wg.createWiki(User.class));
	}
}
