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
public class AutoIncrementHanler implements AbstractHanler {

	public void fillSql(StringBuffer sb, Object annotation, Field field) {
		sb.append(" NOT NULL AUTO_INCREMENT");
	}

	public String toString() {
		return "";
	}
	

}
