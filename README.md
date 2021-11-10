# vaers-tools

<details open>
  <summary>GetCSVColumnInfo Tool</summary>
  
## GetCSVColumnInfo
  Tool that creates a set of column name and field length files to be used to create base     relational database tables.
  
  1. Download the AllVAERSDataCSVS.zip file from the following URL:
<https://vaers.hhs.gov/data.html>
  2. unzip the zip file mentioned above into the 'files' directory found in the vaers-tools project
  3. Update the vaers-tools.properties file if needed
  4. execute the main GetCSVColumnInfo class to create a set of column name and field length files to be used to create base relational database tables.
</details>

<details open>
  <summary>CreateBaseTableSQL Tool</summary>
  
## CreateBaseTableSQL
  Tool that creates a set of base relational database CREATE TABLE sql files (based on the json files created by the GetCSVColumnInfo tool.
  
  1. Execute the GetCSVColumnInfo tool first.
  2. execute the main CreateBaseTableSQL class to create a set of base relational database CREATE TABLE sql files (based on the json files created by the GetCSVColumnInfo tool.
</details>
