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
     - mvn clean install
     - java -cp vaers-tools-1.0.0.jar com.nibroc.vaers.tools.csv.GetCSVColumnInfo
</details>

<details open>
  <summary>CreateBaseTableSQL Tool</summary>
  
## CreateBaseTableSQL
  Tool that creates a set of base relational database CREATE TABLE sql files (based on the json files created by the GetCSVColumnInfo tool.
  
  1. Execute the GetCSVColumnInfo tool first.
  2. execute the main CreateBaseTableSQL class to create a set of base relational database CREATE TABLE sql files (based on the json files created by the GetCSVColumnInfo tool.
     - mvn clean install
     - java -cp vaers-tools-1.0.0.jar com.nibroc.vaers.tools.sql.CreateBaseTableSQL
</details>

<details open>
  <summary>CreatePopulateBaseTablesSQL Tool</summary>
  
## CreatePopulateBaseTablesSQL
  Tool that creates a set of base relational database INSERT TABLE sql files. These sql files will populate the base tables created by the CreateBaseTableSQL tool.
  
  1. Execute both the GetCSVColumnInfo and CreateBaseTableSQL tools first.
  2. execute the main CreatePopulateBaseTablesSQL class to create a set of base relational database INSERT TABLE sql files.
     - mvn clean install
     - java -cp vaers-tools-1.0.0.jar com.nibroc.vaers.tools.sql.CreatePopulateBaseTablesSQL
</details>

<details open>
  <summary>Create and import into mysql</summary>
  
## Create and import into mysql

  1. Both Docker and mysql must be installed locally, then run the following:
     - docker pull mysql
     - docker run -p 3307:3306 --name vaers-mysql -e MYSQL_ROOT_PASSWORD=admin -d mysql:latest
     - mysql -u root -h 127.0.0.1 -P 3307 -p -e "CREATE DATABASE vaers"
     - cd to <CreateBaseTableSQL> directory
     - mysql -u root -h 127.0.0.1 -P 3307 vaers -p < VAERSDATA-mysql-base-table.sql
     - mysql -u root -h 127.0.0.1 -P 3307 vaers -p < VAERSSYMPTOMS-mysql-base-table.sql
     - mysql -u root -h 127.0.0.1 -P 3307 vaers -p < VAERSVAX-mysql-base-table.sql
     - cd to <CreatePopulateBaseTablesSQL> directory
     - mysql -u root -h 127.0.0.1 -P 3307 vaers -p < VAERSDATA-table-inserts.sql
     - mysql -u root -h 127.0.0.1 -P 3307 vaers -p < VAERSSYMPTOMS-table-inserts.sql
     - mysql -u root -h 127.0.0.1 -P 3307 vaers -p < VAERSVAX-table-inserts.sql
</details>
