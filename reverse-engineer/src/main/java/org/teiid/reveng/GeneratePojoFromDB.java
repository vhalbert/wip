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
package org.teiid.reveng;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.teiid.core.util.UnitTestUtil;
import org.teiid.reveng.metadata.db.DBMetadataProcessor;
import org.teiid.reveng.template.TemplateProcessing;

/**
 * @author vanhalbert
 *
 */
public class GeneratePojoFromDB {
	

	
	/**
	*  main method for DBGeneratePojo().  Table names are
	*  provided on the command-line.  Output is placed in the 
	*  current directory
	* @param args  provide table names on the command-line
	*        for each class to be generated
	*  Properties requried:
	*    ConfigFileName  - the name of a configuration file containing
	*                      the following required parameters:
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

	Properties props = System.getProperties();
	String configFileName = props.getProperty("ConfigFileName");

	//Database connection parameters from config file
	String jdbcDriver;
	String dbUrl;
//	String dbClassName;
	String user;
	String password;
	String dbSchema;
	String dbCatalog;
	
	Connection conn = null;

	//Strings for building output
//	String Constructor;
//	String Selectors;
//	String SetMethods;
//	String StaticInitializer; 
//	String sqlTypeString;
//	String ToString;

//	String javaTypeString = new String();

	  if (configFileName == null) {
	    System.err.println("ERROR: You must enter config file name on command line");
	    System.err.println("Usage: java -DConfigFileName=<config file name> Member");
	    System.exit(1);
	  }

	  try {
	      props.load(new FileInputStream(configFileName));
	  }
	  catch (IOException eIO) {
	      System.err.println("ERROR: load of " + configFileName + " failed: " + eIO.getMessage());
	      System.exit(2);
	  }

	  jdbcDriver = props.getProperty("Driver");
	  dbUrl = props.getProperty("DbUrl");
	  user = props.getProperty("User");
	  password = props.getProperty("Password");
	  dbSchema = props.getProperty("Schema", null);
	  dbCatalog = props.getProperty("Catalog", null);

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

	  try {
		  
		  DBMetadataProcessor metadata = new DBMetadataProcessor();
		  List<String> tables =  new ArrayList<String>();
		  
		    for (int argCounter = 0; argCounter < args.length; argCounter++) {
		    	tables.add(args[argCounter]);
		    }
//		  tables.add("%");
		  metadata.loadMetadata(conn, dbCatalog, dbSchema, tables);

		  Options o = new Options();
		  o.setAnnotationType(Options.Annotation_Type.Hibernate);
    	
		  String path = UnitTestUtil.getTestScratchPath() + File.separatorChar+ "execjavafiles";
		  File f = new File(path);
		  f.mkdirs();
    	
			TemplateProcessing tp = new TemplateProcessing(path, o);
			tp.processTables(metadata);
	  
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
