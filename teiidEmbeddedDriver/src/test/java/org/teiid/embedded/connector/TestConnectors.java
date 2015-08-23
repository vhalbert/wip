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

import org.junit.Assert;

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
		
		TeiidEmbeddedDriver.LOG_COMPONENT_SET_METHODS = true;
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
	
	@Test public void testCassandraConnector() throws Exception {	
		ConnectorConfiguration config = new ConnectorConfiguration();
		config.setName("cassandraConnector");
		config.setJndiName("java:/demoCassandra");
		
		Properties props = new Properties();
		props.setProperty(CassandraConnector.ADDRESS_PROPERTY, "196.156.8.100");
		props.setProperty(CassandraConnector.KEYSPACE_PROPERTY, "example");
		config.setProperties(props);
		
		CassandraConnector ft = new CassandraConnector();
		ft.initialize(MANAGER, config);
		
		// verify the properties from the cassandra.properties was loaded and overriding
		// the above settings.
		Assert.assertEquals(config.getProperties().getProperty(CassandraConnector.ADDRESS_PROPERTY), "10.66.218.46") ;
		Assert.assertEquals(config.getProperties().getProperty(CassandraConnector.KEYSPACE_PROPERTY), "demo") ;
		
	}
	
	@Test public void testWSConnector() throws Exception {	
		ConnectorConfiguration config = new ConnectorConfiguration();
		config.setName("wsConnector");
		config.setJndiName("java:/CustomerRESTWebSvcSource");
		
		Properties props = new Properties();
		config.setProperties(props);
		
		WSConnector ft = new WSConnector();
		ft.initialize(MANAGER, config);
		
	}	
	
	@Test public void testMongoDBConnector() throws Exception {	
		ConnectorConfiguration config = new ConnectorConfiguration();
		config.setName("mongodbConnector");
		config.setJndiName("java:/mongoDS");
		
		Properties props = new Properties();
		props.setProperty(MongoDBConnector.SERVERLIST_PROPERTY, "198.166.218.46:27017");
		props.setProperty(MongoDBConnector.DBNAME_PROPERTY, "example");
		config.setProperties(props);
		
		MongoDBConnector ft = new MongoDBConnector();
		ft.initialize(MANAGER, config);
		
		// verify the properties from the mongodb.properties was loaded and overriding
		// the above settings.
		Assert.assertEquals(config.getProperties().getProperty(MongoDBConnector.SERVERLIST_PROPERTY), "10.66.218.46:27017") ;
		Assert.assertEquals(config.getProperties().getProperty(MongoDBConnector.DBNAME_PROPERTY), "mydb") ;
		
	}	
	
	@AfterClass
	public static  void shutDown() {
		DRIVER.shutdown();
	}

}
