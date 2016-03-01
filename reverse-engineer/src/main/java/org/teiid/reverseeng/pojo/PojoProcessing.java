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
package org.teiid.reverseeng.pojo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import org.teiid.core.util.StringUtil;
import org.teiid.reverseeng.Options;
import org.teiid.reverseeng.api.AnnotationType;
import org.teiid.reverseeng.api.Column;
import org.teiid.reverseeng.api.MetadataProcessor;
import org.teiid.reverseeng.api.Table;

/**
 * @author vanhalbert
 *
 */
public class PojoProcessing {

	private String path;
	private String packageName;
	private String pojoJarName;
	private AnnotationType annotationType;
	
	public PojoProcessing() {
	}

	public PojoProcessing(String outpath) {
		this.path = outpath;
	}

	public PojoProcessing(Options options) {
		this.path = options.getProperty(Options.Parms.BUILD_LOCATION);
		if (path == null) path = Options.Parms_Defaults.DEFAULT_BUILD_LOCATION;
		annotationType = options.getAnnotationTypeInstance();
		
		packageName = options.getProperty(Options.Parms.POJO_PACKAGE_NAME);
		if (packageName == null) {
			packageName = Options.Parms_Defaults.DEFAULT_POJO_PACKAGE_NAME;
		}

		pojoJarName = options.getProperty(Options.Parms.POJO_JAR_NAME);
		if (pojoJarName == null) {
			pojoJarName = Options.Parms_Defaults.DEFAULT_POJO_JAR_NAME;
		}
	
	}

	
	public void processTables(MetadataProcessor metadata)
			throws Exception {
		
		DynamicCompilation dc = new DynamicCompilation();

		File f = new File(path);
		if (f.exists()) {
			f.delete();
		}
		
		List<String> nodes = StringUtil.getTokens(packageName, ".");
		
		StringBuffer pathLoc = new StringBuffer();
	
		for (String n : nodes) {
			f = new File(f.getAbsolutePath(), n);
			pathLoc.append(n).append(File.separator);
			
			f.mkdirs();
		}

		List<Table> tables = metadata.getTableMetadata();
		for (Table t : tables) {
			
			String className = t.getClassName();
			// className = className + "Table";
			String fileName = className + ".java";
			
			File outputFile = new File(f.getAbsolutePath(), fileName);
						
			System.out.println("*** Writing java file: " + outputFile.getAbsolutePath());

			FileOutputStream fileOutput = new FileOutputStream(outputFile);

			PrintWriter outputStream = new PrintWriter(fileOutput);

			printPackage(outputStream, packageName);
			printHeader(outputStream, t);
			printImports(outputStream, t);
			
			printClass(outputStream, t);
			printAttributes(outputStream, t);
			printGetterSetters(outputStream, t);
			printToString(outputStream, t);
			printFooter(outputStream, t);
			
			System.out.println("*** Completed Writing java file: " + outputFile.getAbsolutePath());
			
			dc.addFile(outputFile, packageName + "." + className);

		}
		
		dc.compile(f, pathLoc.toString(), pojoJarName);	
		
	}
	
	protected void printPackage(PrintWriter outputStream, String packageName) throws IOException {
		
		InputStream is = getClass().getClassLoader().getResourceAsStream("org/teiid/reverseeng/template.txt");
		if (is != null) { 
			for( int c = is.read(); c != -1; c = is.read() ) {
				outputStream.print((char) c);
			}
		}
		
		outputStream.println("package " + packageName + ";");
		
	}

	/**
	 * prints standard source file header to outputStream
	 * 
	 * @param outputStream
	 *            where to print the header
	 * @param t
	 *            the Table representing the class
	 */
	protected void printHeader(PrintWriter outputStream, Table t) {

		outputStream.println("/**");
		outputStream.println("* Maps a relational database table "
				+ t.getName() + " to a java object, " + t.getClassName());
		outputStream.println("*");
		outputStream.println("* " + (t.getRemarks() != null ? t.getRemarks() : ""));
		outputStream.println("*");
		outputStream.println("* @author	ReverseEngineer");
		outputStream.println("*/");


	} // printHeader()

	/**
	 * prints imports to outputStream
	 * 
	 * @param outputStream
	 *            where to print the header
	 * @param t
	 *            the Table representing the class
	 */
	protected void printImports(PrintWriter outputStream, Table t) {

		outputStream.println("import java.io.Serializable;");
		outputStream.println("import java.sql.*;");
		outputStream.println("import java.util.*;");
		
		if (annotationType != null) {
			List<String> imports = annotationType.getImports();
			for(String i : imports) {
				outputStream.println(i);
			}		
		}		
	} // printImports()	
		
	protected void printClass(PrintWriter outputStream, Table t) {
		outputStream.println("\r");
		if (annotationType != null) {
			String a = annotationType.getClassAnnotation(t);
			if (a != null) {
				outputStream.println(a);
			}
		}
		outputStream.println("public class " + t.getClassName()
				+ " implements Serializable {");

		
	} // printClass
	
	protected void printAttributes(PrintWriter outputStream, Table t) {
		List<Column> columns = t.getColumns();

		for (Column c : columns) {
			if (annotationType != null) {
				outputStream.println("\r");

				String a = annotationType.getAttributeAnnotation(c);
				if (a != null) {
					outputStream.println("\t" + a);			
				}
			}

			outputStream.println(buildAttributeStatement(c));
		}
	}

	protected void printGetterSetters(PrintWriter outputStream, Table t) {
		outputStream.println("\r");

		List<Column> columns = t.getColumns();

		for (Column c : columns) {
			if (annotationType != null) {
				outputStream.println("\r");
	
				String a = annotationType.getGetterMethodAnnotation(c);
				if (a != null) {
					outputStream.println("\t" + a);			
				}
			}
			outputStream.println(buildGetStatement(c));

			outputStream.println(buildSetStatement(c));
		}

	}

	protected void printToString(PrintWriter outputStream, Table t) {
		outputStream.println(buildToString(t));
		
	}

	protected void printFooter(PrintWriter outputStream, Table t) {

		outputStream.println("} // class " + t.getClassName());
		outputStream.close();

	}

	/**
	 * Called to build the Attribute statement based on the Column
	 * 
	 * @param column
	 * @return Attribute statement
	 */
	public String buildAttributeStatement(Column column) {

		StringBuffer result = new StringBuffer();

		result.append("\tprivate  ");
		result.append(column.getJavaType());
		result.append(" m_");
		result.append(column.getMemberName());
		result.append(";");

		return result.toString();

	}

	/**
	 * builds a GET statement based on the Column
	 * 
	 * @param column
	 * @return the get statement string
	 */

	public String buildGetStatement(Column column) {

		StringBuffer result = new StringBuffer();

		result.append("\tpublic ");
		result.append(column.getJavaType());
		result.append(" get");
		result.append(column.getMemberName());
		result.append("( ) { \r");
		result.append("\t\treturn this.m_");
		result.append(column.getMemberName());
		result.append(";");
		result.append("\r\t}");

		return result.toString();
	}

	/**
	 * Called to build a SET based on the Column
	 * 
	 * @param column
	 * @return the set statement string
	 */

	public String buildSetStatement(Column column) {

		StringBuffer result = new StringBuffer();

		result.append("\tpublic void set");
		result.append(column.getMemberName());
		result.append("( ");
		result.append(column.getJavaType());
		result.append(" ");
		result.append(column.getName());
		result.append(") { \r\t\t this.m_");
		result.append(column.getMemberName());
		result.append(" = ");
		result.append(column.getName());
		result.append("; \r\t}");

		return result.toString();
	}

	/**
	 * Called to build the toString based on the Table
	 * 
	 * @param table
	 * @return the set statement string
	 */

	public String buildToString(Table table) {

		StringBuffer result = new StringBuffer();
		result.append("\tpublic String toString()  {\n\t\tStringBuffer output = new StringBuffer();\n");

		List<Column> columns = table.getColumns();

		for (Column c : columns) {

			result.append("\t\toutput.append(\"" + c.getName() + ":\");\n");
			result.append("\t\toutput.append(get" + c.getMemberName() + "());\n");
			result.append("\t\toutput.append(\"\\n\");\n");

		}

		result.append("\n\t\treturn output.toString();\n\t}\n");

		return result.toString();
	}
}
