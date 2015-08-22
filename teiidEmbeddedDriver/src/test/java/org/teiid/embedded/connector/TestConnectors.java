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
package org.teiid.embedded.connector;

import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.teiid.embedded.TeiidEmbeddedDriver;
import org.teiid.embedded.TeiidEmbeddedMgr;
import org.teiid.embedded.configuration.ConnectorConfiguration;

/**
 * @author vanhalbert
 *
 */
public class TestConnectors {
	static TeiidEmbeddedDriver DRIVER;
	static TeiidEmbeddedMgr MANAGER;
	
	@BeforeClass
	public static void onlyOnce() {
		DRIVER = new TeiidEmbeddedDriver();
		MANAGER = new TeiidEmbeddedMgr(DRIVER);		
	}
	
	@Test public void testFileConnector() throws Exception {	
		ConnectorConfiguration config = new ConnectorConfiguration();
		config.setName("fileConnector");
		config.setJndiName("java:/marketdata-file");
		
		Properties props = new Properties();
		props.setProperty("ParentDirectory", "data");
		config.setProperties(props);
		
		FileConnector ft = new FileConnector();
		ft.initialize(MANAGER, config);
		
	}
	
	
	@AfterClass
	public static  void shutDown() {
		DRIVER.shutdown();
	}

}
