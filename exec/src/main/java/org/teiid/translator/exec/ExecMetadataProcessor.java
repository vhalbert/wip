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

package org.teiid.translator.exec;

import org.teiid.metadata.BaseColumn.NullType;
import org.teiid.metadata.Column;
import org.teiid.metadata.Column.SearchType;
import org.teiid.metadata.MetadataFactory;
import org.teiid.metadata.Table;
import org.teiid.translator.MetadataProcessor;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.TypeFacility;


/**
 * The BaseMetadataProcess is the core logic for providing metadata to the translator.
 */
public class ExecMetadataProcessor implements MetadataProcessor<ExecTranslatorConnection>{
	
	public static final String ARGUMENTS = "arguments";
	public static final String DELIMITER = "delimiter";
	public static final String RESULTS = "results";
	
	protected boolean isUpdatable = false;

	public void process(MetadataFactory mf, ExecTranslatorConnection conn) throws TranslatorException {
		String tableName = "ExecTable";
		Table table = mf.getSchema().getTable(tableName);
		if (table != null) {
			//TODO: probably an error
			return;
		}
		table = mf.addTable(tableName);
		table.setSupportsUpdate(false);
//		table.setNameInSource(cacheName); 

		addColumn(mf, ARGUMENTS, String.class, null, SearchType.Searchable, table, false); //$NON-NLS-1$
		addColumn(mf, DELIMITER, String.class, null, SearchType.Searchable, table, false); //$NON-NLS-1$
		addColumn(mf, RESULTS, String.class, null, SearchType.Unsearchable, table, true); //$NON-NLS-1$

	}

	protected Column addColumn(MetadataFactory mf, String columnName, Class<?> type, String nis, SearchType searchType, Table entityTable, boolean selectable) {
		Column c = entityTable.getColumnByName(columnName);
		if (c != null) {
			//TODO: there should be a log here
			return c;
		}
		c = mf.addColumn(columnName, TypeFacility.getDataTypeName(TypeFacility.getRuntimeType(type)), entityTable);
		if (nis != null) {
			c.setNameInSource(nis);
		}
		
		
		if (type.isArray()) {
			c.setNativeType(type.getSimpleName());
		} else if (type.isEnum()) {
			c.setNativeType(Enum.class.getName());
		} else {
			c.setNativeType(type.getName());
		}
		
		c.setUpdatable(false);
		c.setSearchType(searchType);
//		c.setNativeType(type.getName());
		c.setSelectable(selectable);
		if (type.isPrimitive()) {
			c.setNullType(NullType.No_Nulls);
		}
		return c;
	}
	
}
