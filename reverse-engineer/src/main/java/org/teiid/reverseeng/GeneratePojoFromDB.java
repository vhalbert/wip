/*
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */
package org.teiid.reverseeng;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.teiid.core.util.StringUtil;
import org.teiid.reverseeng.metadata.db.DBOptions;

/**
 * @author vanhalbert
 *
 */
public class GeneratePojoFromDB {
	

	
	/**
	*  main method for DBGeneratePojo().  
	*  
	*  Requires 2 arguments:
	*  1) Comma separated list of Table names or wildcard
	*  2) Configuration File name, including full path
	*
	* @param args  provide table names and configuration file name
	*
	* <p>
	* The configuration file will be a properties file that contains:
	* <pre>
	*               DbUrl="jdbc:teiid:VDB@mm://host:port";
	*               Driver="org.jboss.teiid.Driver";
	*               User="user";
	*               Password="";
	*               Schema = "Northwind";
	*               Catalog = null;
	*</pre>
	*/  

	public static void main (String args[]) {
		
		if (args == null || args.length != 2) {
		      System.err.println("Invalid arguments, must pass in 2 arguments:   [tableName,tableName,..] [configFileName]");
		      System.exit(2);
		}
		String configFileName = args[1];

	Properties props = System.getProperties();

	//Database connection parameters from config file
	String jdbcDriver;
	String dbUrl;
	String user;
	String password;
	String dbSchema;
	String dbCatalog;
	String annotation;
	String build_location;
	String packagename;
	String pojoJarName;
	
	Connection conn = null;

	  if (configFileName == null) {
	    System.err.println("ERROR: You must enter config file name on command line");
	    System.err.println("Usage: java {tableName, ..}  {config file name}");
	    System.exit(1);
	  }

	  try {
	      props.load(new FileInputStream(configFileName));
	  }
	  catch (IOException eIO) {
	      System.err.println("ERROR: load of " + configFileName + " failed: " + eIO.getMessage());
	      System.exit(2);
	  }
	  
	  if (props.isEmpty()) {
	      System.err.println("ERROR: load of " + configFileName + " contained no properties");
	      System.exit(2);
		  
	  }

	  jdbcDriver = props.getProperty(DBOptions.DBParms.DRIVER);
	  dbUrl = props.getProperty(DBOptions.DBParms.URL);
	  user = props.getProperty(DBOptions.DBParms.USER);
	  password = props.getProperty(DBOptions.DBParms.PASSWORD);
	  dbSchema = props.getProperty(DBOptions.DBParms.SCHEMA, null);
	  dbCatalog = props.getProperty(DBOptions.DBParms.CATALOG, null);
	  annotation =props.getProperty(Options.Parms.ANNOTATION, null);
	  build_location = props.getProperty(Options.Parms.BUILD_LOCATION, null);
	  packagename = props.getProperty(Options.Parms.POJO_PACKAGE_NAME, null);
	  pojoJarName = props.getProperty(Options.Parms.POJO_JAR_NAME, null);
	  

	  try {
	      conn = connect(jdbcDriver, dbUrl, user, password);
	  }
	  catch (ClassNotFoundException eClass) {
	      System.err.println("ERROR: Driver couldn't be loaded for: " + jdbcDriver);
	      System.err.println("Error message is: " + eClass.getMessage());
	      System.exit(3);
	  }
	  catch (SQLException eSQL) {
	      System.err.println("ERROR: connect failed: " + eSQL.getMessage());
	      System.err.println("Check your configuration file: " + configFileName);
	      System.exit(3);
	  }
	  
	  if (conn == null) return;

 	  try {
 		  List<String> tableNames = StringUtil.getTokens(args[0], ",");
 
		  DBOptions options = new DBOptions();
		  
		  options.setDbCatalog(dbCatalog);
		  options.setDbSchema(dbSchema);
		  options.setTables(tableNames);

		  options.setProperty(Options.Parms.BUILD_LOCATION, build_location);
//		  options.setProperty(Options.Pojo_Properties.BUILD_LOCATION, UnitTestUtil.getTestScratchPath() + File.separatorChar+ "execjavafiles");
		  
		  options.setProperty(Options.Parms.ANNOTATION, annotation);
		  options.setProperty(Options.Parms.POJO_PACKAGE_NAME, packagename);
		  options.setProperty(Options.Parms.POJO_JAR_NAME, pojoJarName);

		  ReverseEngineerFactory.perform(conn, options);
	  
	  } catch (Exception e) {
	    System.err.println("ERROR: exception caught: " + e.getMessage());
	    e.printStackTrace();
	  } finally {
		  try {
			conn.close();
		} catch (SQLException e) {
		}
	  }
	} //main()
	
	/** 
	*  connect to database server.  Assumes driver is already loaded.
	*  @param	dbUrl     url to database server, example:
	*                           <pre>jdbc:db2:SAMPLE</pre> to connect to SAMPLE
	*                           database on DB2 server
	*  @param	user      the user name to user for connection
	*  @param	password  the user's password
	 * @return Connection
	 * @throws SQLException 
	*/
	public static Connection connect ( String dbUrl, 
	  String  user, String password) throws SQLException {

	  Connection DBCon = null;

	  try {
	    DBCon = DriverManager.getConnection(dbUrl, user, password);
	  }
	  catch (SQLException e) {
	    //log an error message
	    throw e;
	  }

	  return DBCon;

	} //connect
	
	/**
	* connect to database server 
	* @param  driver    the JDBC driver to use for connection
	* @param  dbUrl    the database connection url
	* @param  user    database user name
	* @param  password  the database user's password
	* @return  a valid java.sql.Connection
    * @throws ClassNotFoundException 
	* @throws  java.sql.SQLException
	*/

	public static Connection connect ( String driver, String dbUrl, 
	  String  user, String password) throws ClassNotFoundException, SQLException {

	  Connection DBCon;

	  try {
	    //load driver if not already loaded as a side-effect of this call
	    Class.forName(driver);
	  }
	  catch (ClassNotFoundException e) {
	    //log an error message
	    throw e;
	  }

	  try {
	    DBCon = connect(dbUrl, user, password);
	  }
	  catch (SQLException e) {
	    //log an error message
	    throw e;
	  }

	  return DBCon;
	}	
}
