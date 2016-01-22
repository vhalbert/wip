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
package org.teiid.reveng.metadata.db;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.teiid.reveng.api.Column;
import org.teiid.reveng.api.Table;

	/**
	 * A DBColumn defines a descriptor for a single database table column.
	 */
	public class DBColumn extends Column {
					    
	    protected String typeName;

		public DBColumn(Table parent, String name) {
	        super(parent, name);
	    }

		public String getAliasedName(String alias) {
	        return (alias != null) ? alias + '.' + this.getName() : this.getName();
	    }

		@Override
		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

	    @Override
	    public  int compareTo(Column o) {
		        return new CompareToBuilder()
		        .append(o.getName(), this.getName())
		        .append(o.getType(), getType())
		        .append(o.isIndexed(), isIndexed())
		        .append(o.getMaxLength(), getMaxLength())
		        .append(o.getScale(), getScale())
		        .toComparison();
		}
	    
	    
	}


