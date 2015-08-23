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
import org.teiid.resource.adapter.cassandra.CassandraManagedConnectionFactory;

/**
 * @author vanhalbert
 *
 */
public class CassandraConnector extends TeiidConnectorWrapper {
	public static final String ADDRESS_PROPERTY = "Address";
	public static final String KEYSPACE_PROPERTY = "Keyspace";
	
	private static final String CASSANDRA_PROP_FILE = "cassandra.properties";

	private String address = null;
	private String keyspace = null;

	@Override
	public void initialize(TeiidEmbeddedMgr manager, Configuration config)
			throws Exception {
		
		initCassandraProperties();
		
		ConnectorConfiguration cc = (ConnectorConfiguration) config;
    	
		CassandraManagedConnectionFactory managedconnectionFactory = new CassandraManagedConnectionFactory();
    	
		if (address != null) {
			config.setProperty(ADDRESS_PROPERTY, address);
		}
		if (keyspace != null) {
			config.setProperty(KEYSPACE_PROPERTY, keyspace);
		}
		
    	this.applyProperties(managedconnectionFactory, config.getProperties());
    	
		manager.getEmbeddedServer().addConnectionFactory(cc.getJndiName(), managedconnectionFactory.createConnectionFactory());

	}
	
	private void initCassandraProperties() throws IOException {
		URL urlToFile = ClassLoader.getSystemClassLoader().getResource(CASSANDRA_PROP_FILE);

		if (urlToFile == null) {
			throw new RuntimeException("Unable to get URL for file " + "cassandra.properties");
		}
		
		Properties props = PropertiesUtils.loadFromURL(urlToFile);
		if(props.getProperty("cassandra.address") != null) {
			address = props.getProperty("cassandra.address");
		}
		
		if(props.getProperty("cassandra.keyspace") != null){
			keyspace = props.getProperty("cassandra.keyspace");
		}

	}
	

}
