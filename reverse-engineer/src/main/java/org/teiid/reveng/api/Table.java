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
package org.teiid.reveng.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author vanhalbert
 *
 */
public abstract class Table {
    private String name;
    private String className;
    
    private String remarks;
    
    private Map<String, Column> columns = new HashMap<String, Column>();


    public Table(String name) {
    	this.name = name;
    	this.className =  org.teiid.reveng.util.Util.columnNameToMemberName(this.name);
    }
	/**
	 * @return String
	 * @see org.teiid.reveng.api.Table#getName()
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return String
	 * @see org.teiid.reveng.api.Table#getRemarks()
	 */
	public String getRemarks() {
		return remarks;
	}
	
	public String getClassName() {
		return this.className;
	}

	public Column getColumn(String name) {
		return columns.get(name);
	}
	/**
	 * @param remarks Sets remarks to the specified value.
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public List<Column> getColumns() {
		List<Column> cols = new ArrayList<Column>(columns.size());
		cols.addAll(columns.values());
		return cols;
	}
	
	
	public void addColumn(Column column) {
		columns.put(column.getName(), column);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		Table t = (Table) obj;
		if (this.getName().equals(t.getName()) ) {
			List<Column> columns = this.getColumns();
			if (columns.size() != t.getColumns().size()) {
				return false;
			}
			for (Column c : columns) {
				Column tc = t.getColumn(c.getName());
				if (tc == null) return false;
				
				if (!c.equals(tc)) {
					return false;
				}
			}
			
		} else {
			return false;
		}
		
		return true;
	}	
	
	public abstract Column createColumn(String name);


}