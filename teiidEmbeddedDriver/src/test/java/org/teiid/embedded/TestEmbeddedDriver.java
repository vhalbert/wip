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
package org.teiid.embedded;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

/**
 * @author vanhalbert
 *
 */
public class TestEmbeddedDriver {

	@Test 
	public void testDriver() throws Exception {
		
		
		 TeiidEmbeddedDriver server = new TeiidEmbeddedDriver();
		 server.initialize("test-embedded-driver-configuration.xml");

		 server.startServer();
	
		 // NOTE:  not needing to call server.deployVDB(...) 
		 //			because the VDB is specified in the configuration file
		 
		Connection c = server.getConnection("jdbc:teiid:Portfolio", null);
		
		execute(c, "select * from StockPrices", false);
		
		server.shutdown();

	}

	private static void execute(Connection connection, String sql, boolean closeConn) throws Exception {
		try {
			Statement statement = connection.createStatement();
			
			boolean hasResults = statement.execute(sql);
			if (hasResults) {
				ResultSet results = statement.getResultSet();
				ResultSetMetaData metadata = results.getMetaData();
				int columns = metadata.getColumnCount();
				System.out.println("Results");
				for (int row = 1; results.next(); row++) {
					System.out.print(row + ": ");
					for (int i = 0; i < columns; i++) {
						if (i > 0) {
							System.out.print(",");
						}
						System.out.print(results.getString(i+1));
					}
					System.out.println();
				}
				results.close();
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null && closeConn) {
				connection.close();
			}
		}		
	}

	
}
