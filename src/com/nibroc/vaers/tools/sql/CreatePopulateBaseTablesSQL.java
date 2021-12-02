package com.nibroc.vaers.tools.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.nibroc.vaers.tools.common.CommonMethods;
import com.nibroc.vaers.tools.csv.CSV;

public class CreatePopulateBaseTablesSQL {

	public static void readCSVFilesAndCreateInsertSQL(String vaersCSVFilesDirString, String outputDirSQLFiles, String fileNameSearchBy) {
		FileWriter fileWriter = null;
		PrintWriter printWriter = null;
		try {
			File newDir = new File(outputDirSQLFiles);
			if(!newDir.exists()) {
				newDir.mkdirs();
			}
			fileWriter = new FileWriter(outputDirSQLFiles + fileNameSearchBy + "-table-inserts.sql");
			printWriter = new PrintWriter(fileWriter);
			File inputDir = new File(vaersCSVFilesDirString);
			List<String> listOfFilePaths = new ArrayList<String>();
			listOfFilePaths = CommonMethods.listFilesInDir(inputDir, fileNameSearchBy, listOfFilePaths);
			for (int j = 0; j < listOfFilePaths.size(); j++) {
				System.out.println("processing path = " + listOfFilePaths.get(j) + "...");
				InputStream in = new FileInputStream(listOfFilePaths.get(j));
			    CSV csv = new CSV(true, ',', in);
			    List<String> fieldNames = null;
			    if (csv.hasNext()) {
			    	fieldNames = new ArrayList<String>(csv.next());
			    }
			    while (csv.hasNext()) {
			    	String insertStatement = "INSERT INTO " + fileNameSearchBy + " ";
				    String columns = "(";
				    String values = "(";
			    	List<String> rowList = csv.next();
			    	for (int i = 0; i < fieldNames.size(); i++) {
			    		boolean allFieldsLastOne = false;
			    		if(fieldNames.size() == rowList.size()) {
			    			if(rowList.size() == (i + 1)) {
			    				allFieldsLastOne = true;
			    			}
			    		}
			    		if(rowList.size() > i && !allFieldsLastOne) {
				    		columns = columns + fieldNames.get(i) + ", ";
				    		if(i == 0) {
				    			values = values + rowList.get(i) + ", ";
				    		} else {
				    			if(fieldNames.get(i).contains("DATE")) {
				    				values = values + convertDateString(rowList.get(i)) + ", ";
				    			} else {
				    				values = values + "'" + escapeString(rowList.get(i)) + "', ";
				    			}
				    		}
			    		}
			    		if(rowList.size() == i || allFieldsLastOne) {
			    			columns = columns + fieldNames.get(i);
			    			if(fieldNames.get(i).contains("DATE")) {
			    				values = values + convertDateString(rowList.get(i - 1));
			    			} else {
			    				values = values + "'" + escapeString(rowList.get(i - 1)) + "'";
			    			}
			    		}
			    	}
			    	insertStatement = insertStatement + columns + ") VALUES " + values + ");";
			    	printWriter.println(insertStatement);
			    }
			    in.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try{printWriter.close();} catch (Exception ex) {};
			try{fileWriter.close();} catch (Exception ex) {};
		}
	}
	
	public static String escapeString(String stringToEscape) {
		String returnEscapedString = "";
		returnEscapedString = stringToEscape.replaceAll("'", "''")
											.replaceAll(";", "")
											.replaceAll("\\\\", "/");
		return returnEscapedString;
	}
	
	public static String convertDateString(String stringToConvert) {
		String returnConvertedString = "";
		if(stringToConvert != null && stringToConvert.trim().length() == 0) {
			return "NULL";
		}
		String[] splitDateArray = stringToConvert.split("/");
		returnConvertedString = splitDateArray[2] + "-" + splitDateArray[0] + "-" + splitDateArray[1];
		return "'" + returnConvertedString + "'";
	}
	
	public static void main(String[] args) throws Exception {
		Properties propertiesFile = CommonMethods.readPropertiesFile("vaers-tools.properties");
		String outputDirStringCreatePopulateBaseTablesSQL = propertiesFile.getProperty("createPopulateBaseTablesSQL.outputDirString");
		String outputDirSQLFiles = "/" + CommonMethods.getPath() + "/" + outputDirStringCreatePopulateBaseTablesSQL;
		String vaersCSVFilesDirString = "/" + CommonMethods.getPath() + propertiesFile.getProperty("vaersCSVFilesDirString");
		readCSVFilesAndCreateInsertSQL(vaersCSVFilesDirString, outputDirSQLFiles, "VAERSDATA");
		readCSVFilesAndCreateInsertSQL(vaersCSVFilesDirString, outputDirSQLFiles, "VAERSSYMPTOMS");
		readCSVFilesAndCreateInsertSQL(vaersCSVFilesDirString, outputDirSQLFiles, "VAERSVAX");
	}

}
