This tool will reverse engineer a relational table into a pojo java class.  Then will compile and jar'up the classes.

Options:
-  Annotation :  Protobuf or Hibernate

Protobuf for accessing JDG remote cache via hot rod client
Hibernate for access JDG in library mode


-  	pojo_package_name : package name to use in the java class
-	pojo_jar_name :  the jar name to be assigned


Take a look at the config.properties and config_teiid.properties in src/test/resources as configuration file examples that define 
the connection and option properties.

------------------

To Execute:

1)  java -jar

java -jar reverse-engineer-{version}.jar [tableName/wildcard[,tableName,..]] [pathToConfigFile]

where:
- tableName/wildcard : comma seperated list of table names to reverse engineer, or wild card '%' to do all tables
- pathToConfigFile :  configuration file that defines connection properties and other related controls

Example:   java -jar reverse-engineer-{version}.jar Person  ./src/test/resources/config_teiid.properties


2)  using maven

mvn java:exec  -Dtablenames=[table1,table2,..]  -Dconfigfilename=[configFileName]

where:
- tableNames : comma seperated list of table names to reverse engineer, or wild card '%' to do all tables
- configFileName :  configuration file that defines connection properties and other related controls



----------------

Design Notes:

ReverseEngineerFactory can be called, passing in a Connection and Options.  This will perform the whole process.

GeneratePojoFromDB is an application for executing the process.  It's what is called when running java:exec.

