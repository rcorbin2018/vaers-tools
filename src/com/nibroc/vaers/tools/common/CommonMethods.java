package com.nibroc.vaers.tools.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

public class CommonMethods {
	
	public static Properties readPropertiesFile(String fileName) throws IOException {
        FileInputStream fis = null;
        Properties prop = null;
		try {
			fis = new FileInputStream("/" + getPath() + "/" + fileName);
			prop = new Properties();
			prop.load(fis);
		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} finally {
			fis.close();
		}
		return prop;
	}
	
	public static String getPath() {
		String path = Paths.get("").toAbsolutePath().toString();
		return path;
	}

	public static void copyFilesFromTo(String fromDir, String toDir) {
		try {
			File outputDir = new File(toDir);
	        if (!outputDir.exists()) {
	        	outputDir.mkdirs();
			}
	        File src = new File(fromDir);
	        File dest = new File(toDir);
	        FileUtils.copyDirectory(src, dest);
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
	}
	
	public static List<String> listFilesInDir(File dir, String fileNameSearchBy, List<String> returnList) {
		if(dir.isDirectory()) {
			if(!dir.isHidden()) {
				String[] children = dir.list();
				for (int i = 0; i < children.length; i++) {
					returnList = listFilesInDir(new File(dir, children[i]), fileNameSearchBy, returnList);
				}
			}
		} else {
			String absolutePath = dir.getAbsolutePath();
			String fileName = "";
			if(absolutePath != null && absolutePath.length() > 0) {
				for(int i = absolutePath.length() - 1; 0 <= i; i--) {
					String currentChar = absolutePath.substring(i, i + 1);
					if(currentChar.equalsIgnoreCase("\\") || currentChar.equalsIgnoreCase("/")) {
						fileName = absolutePath.substring(i + 1);
						break;
					}
				}
			}
			if(fileName.indexOf(fileNameSearchBy) > -1) {
				returnList.add(absolutePath);
			}
		}
		return returnList;
	}

}
