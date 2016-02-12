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
package org.teiid.reveng.annotation;

import java.util.ArrayList;
import java.util.List;

import org.teiid.reveng.api.AnnotationType;
import org.teiid.reveng.api.Column;
import org.teiid.reveng.api.Table;
import org.teiid.reveng.api.TypesMapping;
//import org.infinispan.protostream.annotations.ProtoEnum;
//import org.infinispan.protostream.annotations.ProtoEnumValue;
//import org.infinispan.protostream.annotations.ProtoField;
//import org.infinispan.protostream.annotations.ProtoMessage;
//import org.infinispan.protostream.annotations.ProtoDoc;
//import org.infinispan.protostream.annotations.ProtoField;

/**
 * @author vanhalbert
 *
 */
public class ProtobufAnnotation implements AnnotationType {
	public static final String TABLE_INDEX = "@ProtoDoc(@Indexed)";
	public static final String PROTODOC = "@ProtoDoc";
	public static final String PROTOFIELD = "@ProtoField";
	
	public static final String INDEXED_YES = "index = true";
	public static final String INDEXED_NO = "index = false";
	
	private static List<String> IMPORTS;
	
	static {
		IMPORTS = new ArrayList<String>();
		
		IMPORTS.add("import org.infinispan.protostream.annotations.ProtoDoc;");
		IMPORTS.add("import org.infinispan.protostream.annotations.ProtoField;");

	}	

	public String getAnnotation(Table t) {
//		@ProtoDoc("@Indexed")
		return TABLE_INDEX;
	}
	
	public String getAnnotation(Column c) {
//   @ProtoDoc("@IndexedField(index = true, store = false)")
//	 @ProtoField(number = 1)

		if(c.isIndexed()) {
			return getAnnotation(INDEXED_YES, "true", c.getOrder());
//					PROTODOC + "\"(@IndexedField(" + INDEXED_YES + ", store = true)\r\t" + PROTOFIELD + "(number = " + c.getOrder() + ")\")";
		} 
		
		String sql_type = TypesMapping.getSqlNameByType(c.getType());
		// don't index;  Blob, Clob and Arrays
		if (sql_type.equals(TypesMapping.SQL_BLOB) || sql_type.equals(TypesMapping.SQL_ARRAY) ||
				 sql_type.equals(TypesMapping.SQL_CLOB)) {
			return getAnnotation(INDEXED_NO, "false", c.getOrder());
					//PROTODOC + "(@IndexedField(" + INDEXED_NO + ", store = false)\r\t" + PROTOFIELD + "(number = " + c.getOrder() + ")";
		} 
		
		return getAnnotation(INDEXED_YES, "true", c.getOrder());
				//PROTODOC + "(@IndexedField(" + INDEXED_YES + ", store = true)\r\t" + PROTOFIELD + "(number = " + c.getOrder() + ")";
	
	}
	
	private String getAnnotation(String index, String store, int order) {
		return PROTODOC + "(\"@IndexedField(" + index + ", store = " + store + ")\")\r\t" + PROTOFIELD + "(number = " + order + ")";
	}
	
	public List<String> getImports() {
		return IMPORTS;
	}
	
}
