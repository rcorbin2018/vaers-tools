package com.nibroc.vaers.tools.sql;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.nibroc.vaers.tools.common.CommonMethods;

public class CreateBaseTableSQL {
	
	public static void readJsonFileAndCreateSQLFiles(String jsonFileLocation, String tableName, String sqlFileDirLocation) {
		FileWriter fileWriter = null;
		PrintWriter printWriter = null;
		JSONParser parser = new JSONParser();
		try {
			File newDir = new File(sqlFileDirLocation);
			if(!newDir.exists()) {
				newDir.mkdirs();
			}
			fileWriter = new FileWriter(sqlFileDirLocation + tableName + "-base-table.sql");
			printWriter = new PrintWriter(fileWriter);
			Object obj = parser.parse(new FileReader(jsonFileLocation));
			JSONArray jsonArray = (JSONArray) obj;
			Iterator<?> iteratorArray = jsonArray.iterator();
			while (iteratorArray.hasNext()) {
				JSONObject jsonObject = (JSONObject)iteratorArray.next();
				printWriter.println("CREATE TABLE " + tableName + " (");
				printWriter.println("VAERS_ID INT NOT NULL,");
				for(Iterator<?> iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
				    String key = (String) iterator.next();
				    //System.out.println(key + "= "+ jsonObject.get(key));
				    printWriter.println(key + " VARCHAR(" + jsonObject.get(key) + "),");
				}
				printWriter.println("PRIMARY KEY (VAERS_ID)");
				printWriter.println(");");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try{printWriter.close();} catch (Exception ex) {};
			try{fileWriter.close();} catch (Exception ex) {};
		}
	}
	
	public static void main(String[] args) throws Exception {
		Properties propertiesFile = CommonMethods.readPropertiesFile("vaers-tools.properties");
		String outputDirStringGetCSVColumnInfo = propertiesFile.getProperty("getCSVColumnInfo.outputDirString");
		String outputDirStringCurrentFiles = "/" + CommonMethods.getPath() + outputDirStringGetCSVColumnInfo + "/CurrentFiles/";
		String outputDirStringCreateBaseTableSQL = propertiesFile.getProperty("createBaseTableSQL.outputDirString");
		String outputDirSQLFile = "/" + CommonMethods.getPath() + "/" + outputDirStringCreateBaseTableSQL;
		List<String> listOfCurrentFiles = new ArrayList<String>();
		listOfCurrentFiles = CommonMethods.listFilesInDir(new File(outputDirStringCurrentFiles), "ColumnNamesAndLengthsFile", listOfCurrentFiles);
		for (int j = 0; j < listOfCurrentFiles.size(); j++) {
			String vaersDataFileName = listOfCurrentFiles.get(j).split("/")[listOfCurrentFiles.get(j).split("/").length - 1];
			String tableName = vaersDataFileName.split("-")[0];
			readJsonFileAndCreateSQLFiles(outputDirStringCurrentFiles + vaersDataFileName, tableName, outputDirSQLFile);
		}
	}

}
