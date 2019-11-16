/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.tools.handlers;

import java.lang.reflect.Field;

/**
 * @author wuheng
 * @since 2019.3.16
 * 
 */
public class NotNullHanler implements AbstractHanler {

	public void fillSql(StringBuffer sb, Object annotation, Field field) {
		sb.append(" NOT NULL");
	}

	@Override
	public String toString() {
		return "";
	}

}
