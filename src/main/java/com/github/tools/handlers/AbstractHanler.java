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
public interface AbstractHanler {

	public void fillSql(StringBuffer sb, Object annotation, Field field);
}
