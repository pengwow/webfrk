/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.webfrk.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * @author wuheng
 * @since  2019.3.7
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
		
		Thread.sleep(2000);
		
		FileWriter fileWritter = new FileWriter(file, true);
		BufferedWriter out = new BufferedWriter(fileWritter);
		out.write(data);
		out.flush();
		out.close();
		
		Thread.sleep(3000);
	}
}
