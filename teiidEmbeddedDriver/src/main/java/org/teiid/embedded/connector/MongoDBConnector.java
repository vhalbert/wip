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

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.teiid.core.util.PropertiesUtils;
import org.teiid.embedded.Configuration;
import org.teiid.embedded.TeiidEmbeddedMgr;
import org.teiid.embedded.component.TeiidConnectorWrapper;
import org.teiid.embedded.configuration.ConnectorConfiguration;
import org.teiid.resource.adapter.mongodb.MongoDBManagedConnectionFactory;

/**
 * @author vanhalbert
 *
 */
public class MongoDBConnector extends TeiidConnectorWrapper {
	public static final String SERVERLIST_PROPERTY = "RemoteServerList";
	public static final String DBNAME_PROPERTY = "Database";
	
	private static final String MONGODB_PROP_FILE = "mongodb.properties";

	private String serverlist = null;
	private String dbname = null;

	@Override
	public void initialize(TeiidEmbeddedMgr manager, Configuration config)
			throws Exception {
		
		initCassandraProperties();
		
		ConnectorConfiguration cc = (ConnectorConfiguration) config;
		
		MongoDBManagedConnectionFactory managedconnectionFactory = new MongoDBManagedConnectionFactory();
    	
		if (serverlist != null) {
			config.setProperty(SERVERLIST_PROPERTY, serverlist);
		}
		if (dbname != null) {
			config.setProperty(DBNAME_PROPERTY, dbname);
		}
		
    	this.applyProperties(managedconnectionFactory, config.getProperties());
    	
		manager.getEmbeddedServer().addConnectionFactory(cc.getJndiName(), managedconnectionFactory.createConnectionFactory());

	}
	
	private void initCassandraProperties() throws IOException {
		URL urlToFile = ClassLoader.getSystemClassLoader().getResource(MONGODB_PROP_FILE);

		if (urlToFile == null) {
			throw new RuntimeException("Unable to get URL for file " + MONGODB_PROP_FILE );
		}
		
		Properties props = PropertiesUtils.loadFromURL(urlToFile);
		
		if(props.getProperty("server.list") != null) {
			serverlist = props.getProperty("server.list");
		}
		
		if(props.getProperty("db.name") != null){
			dbname = props.getProperty("db.name");
		}

	}
	

}
