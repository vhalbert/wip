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
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;


import org.teiid.core.util.UnitTestUtil;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.loopback.LoopbackExecutionFactory;


/**
 * This is used to expose the metadata from the portfolio-vdb.xml.
 */
@SuppressWarnings("nls")
public class TeiidEmbeddedPortfolio {
	
	private Connection conn;
	
	public void initialize() throws Exception {

		EmbeddedServer server = new EmbeddedServer();
		
		LoopbackExecutionFactory executionFactory = new LoopbackExecutionFactory();
		executionFactory.setSupportsDirectQueryProcedure(true);
		executionFactory.start();
		server.addTranslator("loopback", executionFactory);
		
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		server.start(config);
		
		String p = UnitTestUtil.getTestDataPath()+ "/portfolio-vdb.xml";
    	
		server.deployVDB(new FileInputStream(new File(p)));
		
		conn = server.getDriver().connect("jdbc:teiid:Portfolio", null);
	}
	
	public Connection getConnection() {
		return conn;
	}
	
	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
		}
	}

}
