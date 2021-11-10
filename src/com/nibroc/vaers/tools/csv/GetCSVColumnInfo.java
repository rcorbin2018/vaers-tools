package com.nibroc.vaers.tools.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nibroc.vaers.tools.common.CommonMethods;

public class GetCSVColumnInfo {
	
	public void createColumnNamesAndLengthsFile(String outputDirString, String outputFileName, String inputDirString, String fileNameSearchBy) {
		try {	
			File outputDir = new File(outputDirString);
	        if (!outputDir.exists()) {
	        	outputDir.mkdirs();
			}
			FileWriter outputFileWriter  = new FileWriter(outputDirString + outputFileName);
			PrintWriter outputStream  = new PrintWriter(outputFileWriter);
			File inputDir = new File(inputDirString);
			List<String> listOfFilePaths = new ArrayList<String>();
			listOfFilePaths = CommonMethods.listFilesInDir(inputDir, fileNameSearchBy, listOfFilePaths);
			//System.out.println(listOfFilePaths);
			List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
			Map<String, Integer> obj = new HashMap<String, Integer>();
			for (int j = 0; j < listOfFilePaths.size(); j++) {
				System.out.println("processing path = " + listOfFilePaths.get(j) + "...");
				InputStream in = new FileInputStream(listOfFilePaths.get(j));
			    CSV csv = new CSV(true, ',', in);
			    List<String> fieldNames = null;
			    if (csv.hasNext()) {
			    	fieldNames = new ArrayList<String>(csv.next());
			    }
			    while (csv.hasNext()) {
			        List < String > x = csv.next();
			        for (int i = 0; i < fieldNames.size(); i++) {
			        	if(x.size() > i) {
			        		if(obj.get(fieldNames.get(i)) == null) {
			        			obj.put(fieldNames.get(i), Integer.valueOf((x.get(i)).length()));
			        		} else {
			        			Integer currentFieldLength = obj.get(fieldNames.get(i));
			        			if(currentFieldLength.intValue() < (x.get(i)).length()) {
			        				obj.put(fieldNames.get(i), Integer.valueOf((x.get(i)).length()));
			        			}
			        		}
			        	}
			        }
			    }
			}
			list.add(obj);
		    ObjectMapper mapper = new ObjectMapper();
		    mapper.enable(SerializationFeature.INDENT_OUTPUT);
		    mapper.writeValue(outputStream, list);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		Properties propertiesFile = CommonMethods.readPropertiesFile("vaers-tools.properties");
		String outputDirString = propertiesFile.getProperty("getCSVColumnInfo.outputDirString");
		String outputDirStringCurrentFiles = "/" + CommonMethods.getPath() + outputDirString + "/CurrentFiles/";
		String dateTimeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm"));
		String outputDirStringWithDate = "/" + CommonMethods.getPath() + outputDirString + "/PreviousFiles/" + dateTimeStamp + "/";
		String outputFileName = propertiesFile.getProperty("getCSVColumnInfo.outputFileName");
		String inputDirString = "/" + CommonMethods.getPath() + propertiesFile.getProperty("getCSVColumnInfo.inputDirString");
		GetCSVColumnInfo getCSVColumnInfo = new GetCSVColumnInfo();
		getCSVColumnInfo.createColumnNamesAndLengthsFile(outputDirStringCurrentFiles, "VAERSDATA-" + outputFileName, inputDirString, "VAERSDATA");
		getCSVColumnInfo.createColumnNamesAndLengthsFile(outputDirStringCurrentFiles, "VAERSSYMPTOMS-" + outputFileName, inputDirString, "VAERSSYMPTOMS");
		getCSVColumnInfo.createColumnNamesAndLengthsFile(outputDirStringCurrentFiles, "VAERSVAX-" + outputFileName, inputDirString, "VAERSVAX");
		CommonMethods.copyFilesFromTo(outputDirStringCurrentFiles, outputDirStringWithDate);
	}
}
