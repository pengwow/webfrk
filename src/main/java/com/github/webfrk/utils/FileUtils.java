/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.webfrk.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * @author wuheng@otcaix.iscas.ac.cn
 * @since  2019/11/16
 *
 *  
 */

public class FileUtils {

	public static void createFile(String dir, String name, String data) throws Exception {
		
		File fdir = new File(dir);
		
		if (!fdir.exists()) {
			fdir.mkdirs();
		}
		
		File file = new File(fdir, name);
		file.deleteOnExit();
		file.createNewFile();
		
		FileWriter fileWritter = new FileWriter(file, true);
		BufferedWriter out = new BufferedWriter(fileWritter);
		out.write(data);
		out.flush();
		out.close();
		
	}
}
