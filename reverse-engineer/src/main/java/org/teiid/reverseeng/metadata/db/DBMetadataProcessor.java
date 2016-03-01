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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.teiid.core.TeiidException;
import org.teiid.core.TeiidProcessingException;
import org.teiid.reverseeng.ReverseEngineerPlugin;
import org.teiid.reverseeng.api.Column;
import org.teiid.reverseeng.api.MetadataProcessor;
import org.teiid.reverseeng.api.Table;
import org.teiid.reverseeng.api.Column.NullType;

/**
 * @author vanhalbert
 *
 */
public final class DBMetadataProcessor implements MetadataProcessor {

	private List<Table> tableMetadata = new ArrayList<Table>();
	private String quoteString;
	private String[] tableTypes = new String[] { "TABLE", "VIEW",};
	private List<String> tableNamePattern = new ArrayList<String>();
	private String catalogPattern = "%";
	private String schemaPattern = "%";
	
	public List<Table> getTableMetadata() {
		return this.tableMetadata;
	}
	
	public static  DBMetadataProcessor loadMetadata(Connection connection, DBOptions options) throws Exception {
		DBMetadataProcessor dbmp = new DBMetadataProcessor();
		dbmp.performLoad(connection, options);
		return dbmp;
	}
		
	private void performLoad(Connection connection, DBOptions options) throws Exception {
		
		tableNamePattern.add( "%");
		
		DatabaseMetaData metadata = connection.getMetaData();
		quoteString = metadata.getIdentifierQuoteString();
		if (quoteString != null && quoteString.trim().length() == 0) {
			quoteString = null;
		}
		
		String schema = options.getDbSchema();
		String catalog = options.getDbCatalog();
		List<String> tableNames = options.getTables();

		loadTables( (schema != null ? schema : schemaPattern),
				(catalog != null ? catalog : catalogPattern),
				(tableNames != null  ? tableNames : tableNamePattern), metadata);
	}
	
	private void loadTables(String schema, String catalog, List<String> tableCriteria,
			DatabaseMetaData metadata) throws TeiidProcessingException, TeiidException {
		DBTable dbtable = null;
		ResultSet tables = null;
		try {
			for (String tName : tableCriteria) {
			
				tables = metadata.getTables(catalog, schema, tName, tableTypes);
				while (tables.next()) {
					String tableCatalog = tables.getString(1);
					String tableSchema = tables.getString(2);
					String tableName = tables.getString(3);
					String remarks = tables.getString(5);
					
					dbtable = new DBTable(tableName);
					dbtable.setRemarks((remarks != null ? "* " + remarks : "*") + "\r*\r* Created " + new Date() + "\r* From (catalog:schema:table): " + tableCatalog + ":" + tableSchema + ":" + tableName );
					
					ResultSet columns = metadata.getColumns(tableCatalog, tableSchema, tableName, null);
					processColumns(dbtable, columns);
					
					setPrimaryKeys(metadata, dbtable, tableCatalog, tableSchema);
					setIndexes(metadata, dbtable, tableCatalog, tableSchema); 
					
					tableMetadata.add(dbtable);
				}	
			}
		} catch (SQLException sqle) {
			throw new TeiidException(sqle);
			
		} finally {
			if (tables != null) {
				try {
					tables.close();
				} catch (SQLException e) {
				}
			}
		}
		if (dbtable == null)  {
			throw new TeiidProcessingException(ReverseEngineerPlugin.Util.gs(ReverseEngineerPlugin.Event.NO_METADATA, catalog, schema));

		}
			
	}
	
	private void processColumns(DBTable dbTable, ResultSet columns)
			throws SQLException {
		int rsColumns = columns.getMetaData().getColumnCount();
		int order = 0;
		while (columns.next()) {

			addColumn(columns, dbTable, rsColumns, ++order);
		}
		columns.close();
	}
	
	/**
	 * Add a column to the given table based upon the current row of the columns resultset
	 * @param columns
	 * @param dbtable 
	 * @param rsColumns
	 * @param order
	 * @throws SQLException
	 */
	private void addColumn(ResultSet columns, DBTable dbtable, int rsColumns, int order) throws SQLException {
//		String tableCatalog = columns.getString(1);
//		String tableSchema = columns.getString(2);
//		String tableName = columns.getString(3);

		String columnName = columns.getString(4);
		DBColumn column = dbtable.createColumn(columnName);
		
		int type = columns.getInt(5);
		String typeName = columns.getString(6);
		int columnSize = columns.getInt(7);
		column.setType(type);
		column.setTypeName(typeName);
		column.setOrder(order);
//		String runtimeType = getRuntimeType(type, typeName, columnSize);
		//note that the resultset is already ordered by position, so we can rely on just adding columns in order
//		column.setNameInSource(quoteName(columnName));
		column.setPrecision(columnSize);
		column.setMaxLength(columnSize);
//		column.setNativeType(typeName);
//		column.setRadix(columns.getInt(10));
		column.setNullType(NullType.values()[columns.getShort(11)]);
		String remarks = columns.getString(12);
		column.setRemarks(remarks);
		String defaultValue = columns.getString(13);
		
		column.setDefaultValue(defaultValue);
		
//		dbtable.addColumn(column);
		
//		column.setDefaultValue(defaultValue);
//		if (defaultValue != null && type == Types.BIT && TypeFacility.RUNTIME_NAMES.BOOLEAN.equals(runtimeType)) {
//			//try to determine a usable boolean value
//            if(defaultValue.length() == 1) {
//                int charIntVal = defaultValue.charAt(0);
//                // Set boolean FALse for incoming 0, TRUE for 1
//                if(charIntVal==0) {
//                    column.setDefaultValue(Boolean.FALSE.toString());
//                } else if(charIntVal==1) {
//                    column.setDefaultValue(Boolean.TRUE.toString());
//                }
//			} else { //SQLServer quotes bit values
//                String trimedDefault = defaultValue.trim();
//                if (defaultValue.startsWith("(") && defaultValue.endsWith(")")) { //$NON-NLS-1$ //$NON-NLS-2$
//                    trimedDefault = defaultValue.substring(1, defaultValue.length() - 1);
//                }
//                column.setDefaultValue(trimedDefault);
//            }
//		}
//		column.setCharOctetLength(columns.getInt(16));
//		if (rsColumns >= 23) {
//			column.setAutoIncremented("YES".equalsIgnoreCase(columns.getString(23))); //$NON-NLS-1$
//		}
//		return column;
	}
	
	private void setPrimaryKeys(DatabaseMetaData metadata, DBTable dbTable, String catalogName, String schemaName)
		throws SQLException {
		ResultSet pks = metadata.getPrimaryKeys(catalogName, schemaName, dbTable.getName());
		while (pks.next()) {
			String columnName = pks.getString(4);
			
			Column col = dbTable.getColumn(columnName);
			col.setIsIndexed(true);

		}

		pks.close();
	}
	
	private void setIndexes(DatabaseMetaData metadata, DBTable dbTable, String catalogName, String schemaName) throws SQLException {

		ResultSet indexInfo = metadata.getIndexInfo(catalogName, schemaName, dbTable.getName(), false, false);
		while (indexInfo.next()) {

			String columnName = indexInfo.getString(9);
			
			if (columnName == null || dbTable.getColumn(columnName) == null) {
//					LogManager.logDetail(LogConstants.CTX_CONNECTOR, "Skipping the import of non-simple index", indexInfo.getString(6)); //$NON-NLS-1$
				continue;
			}
			Column col = dbTable.getColumn(columnName);
			col.setIsIndexed(true);
		}

		indexInfo.close();
	}	
	
	

}
