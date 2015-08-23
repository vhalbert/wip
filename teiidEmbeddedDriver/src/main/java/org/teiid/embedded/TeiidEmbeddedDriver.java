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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.teiid.core.TeiidProcessingException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.TranslatorException;


/**
 * The TeiidEmbeddedManager is the controller for using Teiid Embedded.  
 * To use it, need to do the following:
 * <li>Define XML formatted configuration file, see BenchmarkConfiguration.xml under src/test/resources for an example.</li>
 * <li>For data sources like JDBC, they need to be created and a Data Source instance (with jndiName) configured. 
 * 
 * 
 * To use the TeiidEmbeddedDriver, the following is an example of the process flow for usage:
 * 		TeiidEmbeddedDriver driver = new TeiidEmbeddedDriver();
 * 		String configFileName = ...
 * 		driver.initialize(configFileName);
 * 
 *  --- the following configuring of the data source is optional, depending on the type of data source connecting to ---
 * 		DataSource ds = .....
 * 		driver.getEmbeddedServer().addConnectionFactory("jndi-name", ds);
 * 
 * 		driver.startServer();
 * 
 * 		driver.deployVDB(vdbFileName);
 * 
 * 		driver.getConnection(jdbcURL, properties);
 * 
 */
@SuppressWarnings("nls")
public class TeiidEmbeddedDriver {
	
	public static boolean LOG_COMPONENT_SET_METHODS = false;

	private EmbeddedServer server;
	private TeiidEmbeddedMgr manager;
	
	private boolean isInitialized = false;
	
	public TeiidEmbeddedDriver() {
		server = new EmbeddedServer();
	}
	
	/**
	 * Call to initialize the Teiid Embedded Server by doing the following
	 * <li>parse the configuration file to determine how the environment will be configured
	 * <li>create translators and connectors
	 * <li>create the embedded server instance
	 * 
	 * For those connectors that need to be configured with a DataSource, now call
	 * #getEmbeddedServer().addConnectionFactory("jndi-name", dataSourceReference);
	 * @param configurationFileName
	 * @throws Exception 
	 */
	public void initialize(String configurationFileName) throws Exception {
		manager = new TeiidEmbeddedMgr(this);
		manager.initialize(configurationFileName);
		this.isInitialized = true;
	}
	
	public void startServer() throws Exception {
		isInitialized();
		
		server.start(manager.getEmbeddedConfiguration());
		
		manager.deployVDBs();
	}
	/**
	 * Call to deploy a VDB xml file
	 * @param vdbFileName
	 * @throws TeiidProcessingException(e);
	 */
	public void deployVDB(String vdbFileName) throws TeiidProcessingException {
		isInitialized();
		
		File f = new File(vdbFileName);
		if (!f.exists()) {
			throw new TeiidProcessingException(EmbeddedPlugin.Util.getString(EmbeddedPlugin.Event.EMBDR10002.toString(), f.getAbsolutePath()));			
		}

		try {
			deployVDB(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			throw new TeiidProcessingException(e);
		}
	}
	
	/**
	 * Call to deploy a VDB using InputStream
	 * @param inputStream
	 * @throws TeiidProcessingException
	 */
	public void deployVDB(InputStream inputStream) throws TeiidProcessingException {
		isInitialized();
		
		try {
			server.deployVDB(inputStream);
		} catch (ConnectorManagerException e) {
			throw new TeiidProcessingException(e);
		} catch (TranslatorException e) {
			throw new TeiidProcessingException(e);
		} catch (IOException e) {
			throw new TeiidProcessingException(e);
		}
	}
	
	/**
	 * Call to deploy a VDB Zip xml file by passing in its= URL
	 * @param vdbURL
	 * @throws TeiidProcessingException
	 */
	public void deployVDBZip(URL vdbURL) throws TeiidProcessingException {
		isInitialized();
		
		try {
			server.deployVDBZip(vdbURL);
		} catch (ConnectorManagerException | TranslatorException | IOException
				| URISyntaxException e) {
			
			throw new TeiidProcessingException(e);
		}
	}
	
	/** 
	 * Call to obtain a Connection based on the <code>url</code>
	 * @param url
	 * @param props
	 * @return Connection
	 * @throws TeiidProcessingException
	 * @throws SQLException 
	 */
	public Connection getConnection(String url, Properties props)  throws TeiidProcessingException, SQLException {
		isInitialized();

		return server.getDriver().connect(url, props);
	}
	
	public void shutdown() {
		if( server != null) {
			server.stop();
		}
	}
	
	public EmbeddedServer getEmbeddedServer() {
		return this.server;
	}
	
	private void isInitialized() throws TeiidProcessingException {
		if (isInitialized) return;
		
		if (manager == null) {
			throw new TeiidProcessingException(EmbeddedPlugin.Util.getString(EmbeddedPlugin.Event.EMBDR10001.toString()));
	
		}
	}

	
}
