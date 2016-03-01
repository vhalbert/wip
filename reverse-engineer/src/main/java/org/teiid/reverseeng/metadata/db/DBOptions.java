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
package org.teiid.reverseeng.metadata.db;

import java.util.List;

import org.teiid.reverseeng.Options;

/**
 * @author vanhalbert
 *
 */
public class DBOptions extends Options {
	
	public interface DBParms {
		public static final String DRIVER = "Driver";
		public static final String URL = "Url";
		public static final String USER = "User";
		public static final String PASSWORD = "Password";
		public static final String SCHEMA = "Schema";
		public static final String CATALOG = "Catalog";
	}

	private String dbCatalog;
	private String dbSchema;
	private List<String> tables;
	/**
	 * @return dbCatalog
	 */
	public String getDbCatalog() {
		return dbCatalog;
	}
	/**
	 * @param dbCatalog Sets dbCatalog to the specified value.
	 */
	public void setDbCatalog(String dbCatalog) {
		this.dbCatalog = dbCatalog;
	}
	/**
	 * @return dbSchema
	 */
	public String getDbSchema() {
		return dbSchema;
	}
	/**
	 * @param dbSchema Sets dbSchema to the specified value.
	 */
	public void setDbSchema(String dbSchema) {
		this.dbSchema = dbSchema;
	}
	/**
	 * @return tables
	 */
	public List<String> getTables() {
		return tables;
	}
	/**
	 * @param tables Sets tables to the specified value.
	 */
	public void setTables(List<String> tables) {
		this.tables = tables;
	}
	
	
	
}
