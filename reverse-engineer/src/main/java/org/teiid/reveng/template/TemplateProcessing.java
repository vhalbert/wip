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
package org.teiid.reveng.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.teiid.reveng.Options;
import org.teiid.reveng.api.AnnotationType;
import org.teiid.reveng.api.Column;
import org.teiid.reveng.api.MetadataProcessor;
import org.teiid.reveng.api.Table;

/**
 * @author vanhalbert
 *
 */
public class TemplateProcessing {

	private String path;
	private Options options;
	private AnnotationType annotationType;
	
	public TemplateProcessing() {
	}

	public TemplateProcessing(String outpath) {
		this.path = outpath;
	}

	public TemplateProcessing(String outpath, Options options) {
		this(outpath);
		this.options = options;
		annotationType = options.getAnnotationTypeInstance();
	}

	
	public void processTables(MetadataProcessor metadata)
			throws FileNotFoundException {

		List<Table> tables = metadata.getTableMetadata();
		for (Table t : tables) {

			String className = t.getClassName();
			// className = className + "Table";
			String fileName = className + ".java";

			File outputFile = null;
			if (this.path != null) {
				outputFile = new File(path, fileName);
			} else {
				outputFile = new File(fileName);
			}
			
			System.out.println("*** Writing java file: " + outputFile.getAbsolutePath());

			FileOutputStream fileOutput = new FileOutputStream(outputFile);

			PrintWriter outputStream = new PrintWriter(fileOutput);

			printHeader(outputStream, t);
			printImports(outputStream, t);
			
			printClass(outputStream, t);
			printAttributes(outputStream, t);
			printGetterSetters(outputStream, t);
			printToString(outputStream, t);
			printFooter(outputStream, t);
			
			System.out.println("*** Completed Writing java file: " + outputFile.getAbsolutePath());

		}
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
			String a = annotationType.getAnnotation(t);
			outputStream.println(a);
			
		}
		outputStream.println("public class " + t.getClassName()
				+ " implements Serializable {");

		
	} // printClass
	
	protected void printAttributes(PrintWriter outputStream, Table t) {
		List<Column> columns = t.getColumns();

		for (Column c : columns) {
			if (annotationType != null) {
				outputStream.println("\r");

				String a = annotationType.getAnnotation(c);
				outputStream.println("\t" + a);				
			}

			outputStream.println(buildAttributeStatement(c));
		}
	}

	protected void printGetterSetters(PrintWriter outputStream, Table t) {
		outputStream.println("\r");

		List<Column> columns = t.getColumns();

		for (Column c : columns) {

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
