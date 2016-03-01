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
package org.teiid.reverseeng.annotation;


import java.util.ArrayList;
import java.util.List;

import org.teiid.reverseeng.api.AnnotationType;
import org.teiid.reverseeng.api.Column;
import org.teiid.reverseeng.api.Table;
import org.teiid.reverseeng.api.TypesMapping;

/**
 * @author vanhalbert
 *
 */
public class HibernateAnnotation implements AnnotationType {
	public static final String TABLE_INDEX = "@Indexed";
	public static final String COLUMN_INDEX = "@Field";
	
	public static final String INDEXED_YES = "index=Index.YES";
	public static final String INDEXED_NO = "index=Index.NO";
	
	private static List<String> IMPORTS;
	
	static {
		IMPORTS = new ArrayList<String>();
		
		IMPORTS.add("import org.hibernate.search.annotations.*;");

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.teiid.reverseeng.api.AnnotationType#getClassAnnotation(org.teiid.reverseeng.api.Table)
	 */
	public String getClassAnnotation(Table t) {
//		@Indexed(index="Trade")
		return TABLE_INDEX ;
		//+ ";" "(index=" + t.getClassName() + ")";
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.teiid.reverseeng.api.AnnotationType#getAttributeAnnotation(org.teiid.reverseeng.api.Column)
	 */
	public String getAttributeAnnotation(Column c) {
//@Field(index=Index.Yes, store=Store.YES, analyze=Analyze.NO)
		if(c.isIndexed()) {
			return COLUMN_INDEX + "(" + INDEXED_YES + ", store=Store.YES, analyze=Analyze.NO)";
		} 
		
		String sql_type = TypesMapping.getSqlNameByType(c.getType());
		if (sql_type == null) {
			throw new UnsupportedOperationException("Column type " + c.getType() + "[" + c.getTypeName() + "]" + "is not defined in the TypesMappings");
		}
		// don't index;  Blob, Clob and Arrays
		if (sql_type == null || sql_type.equals(TypesMapping.SQL_BLOB) || sql_type.equals(TypesMapping.SQL_ARRAY) ||
				 sql_type.equals(TypesMapping.SQL_CLOB)) {
			return COLUMN_INDEX + "(" + INDEXED_NO + ", store=Store.YES, analyze=Analyze.NO)";
			
		} else if (sql_type.equals(TypesMapping.SQL_DATE)) {
			return COLUMN_INDEX + " @DateBridge(resolution=Resolution.MINUTE)";
//			@Field @DateBridge(resolution=Resolution.MINUTE)
		}
	
		return COLUMN_INDEX + "(" + INDEXED_YES + ", store=Store.YES, analyze=Analyze.NO)";
	
	}
	
	public String getGetterMethodAnnotation(Column c) {
		return null;
	}

	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.teiid.reverseeng.api.AnnotationType#getImports()
	 */
	public List<String> getImports() {
		return IMPORTS;
	}
}
