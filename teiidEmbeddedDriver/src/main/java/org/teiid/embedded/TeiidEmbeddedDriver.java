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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.teiid.core.util.PropertiesUtils;
import org.teiid.embedded.configuration.ConnectorConfiguration;
import org.teiid.embedded.configuration.TranslatorConfiguration;
import org.teiid.embedded.util.ClassRegistry;
import org.teiid.embedded.xml.XMLConfiguration;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;


/**
 * The TeiidEmbeddedManager is the controller for using Teiid Embedded.  
 * To use it, need to do the following:
 * <li>Define XML formatted configuration file, see BenchmarkConfiguration.xml under src/test/resources for an example.</li>
 * <li>For data sources like JDBC, they need to be created and a Data Source instance (with jndiName) configured. 
 * 
 * 
 * To use the TeiidEmbeddManager, the following is an example of the process flow for usage:
 * 		TeiidEmbeddedManager tem = new TeiidEmbeddedManager();
 * 		String configFileName = ...
 * 		tem.initialize(configFileName);
 * 
 *  --- the following configuring of the data source is optional, depending on the type of data source connecting to ---
 * 		DataSource ds = .....
 * 		tem.getEmbeddedServer().addConnectionFactory("jndi-name", ds);
 * 
 * 		tem.startServer();
 * 
 * 		tem.deployVDB(vdbFileName);
 * 
 * 		tem.getConnection(jdbcURL, properties);
 * 
 */
@SuppressWarnings("nls")
public class TeiidEmbeddedDriver {
	private static final String TRANSLATOR_MAPPING_FILE = "org/teiid/embedded/TranslatorMapping.txt";
	private static final String CONNECTOR_MAPPING_FILE = "org/teiid/embedded/ConnectorMapping.txt";
	
	private EmbeddedServer server;
	private EmbeddedConfiguration config;
	private XMLConfiguration xmlConfig;
	
	private ClassRegistry registry = new ClassRegistry();
	
	public TeiidEmbeddedDriver() {
		
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
		
		loadMappingFiles();
		
		server = new EmbeddedServer();
		xmlConfig = new XMLConfiguration();
		xmlConfig.configureEmbedded(configurationFileName);
		
		TeiidEmbeddedConfiguration tecVisitor = new TeiidEmbeddedConfiguration(this);
		
		xmlConfig.getEmbeddedConfiguration().accept(tecVisitor);
		
		Map<String, TranslatorConfiguration> mt = xmlConfig.getTranslators();
		Iterator<String> mtIt = mt.keySet().iterator();
		while (mtIt.hasNext()) {
			String k = mtIt.next();
			TranslatorConfiguration t = mt.get(k);
			t.accept(tecVisitor);
		}
		Map<String, ConnectorConfiguration> ct = xmlConfig.getConnectors();
		Iterator<String> ctIt = ct.keySet().iterator();
		while (ctIt.hasNext()) {
			String k = ctIt.next();
			ConnectorConfiguration c = ct.get(k);
			c.accept(tecVisitor);
		}

		config = tecVisitor.getEmbeddedServerConfiguation().getEmbeddedConfiguration();

	}
	
	public void startServer() {
		server.start(config);
	}
	/**
	 * Call to deploy a VDB xml file
	 * @param vdbFileName
	 * @throws Exception
	 */
	public void deployVDB(String vdbFileName) throws Exception {
		URL urlToFile = TeiidEmbeddedDriver.class.getClassLoader().getResource(vdbFileName);
		if (urlToFile == null) {
			throw new RuntimeException("Unable to get URL for vdb file " + vdbFileName);
		}
		File vdbFile = new File(urlToFile.toURI());		

		deployVDB(new FileInputStream(vdbFile));
	}
	
	/**
	 * Call to deploy a VDB using InputStream
	 * @param inputStream
	 * @throws Exception
	 */
	public void deployVDB(InputStream inputStream) throws Exception {

		server.deployVDB(inputStream);
	}
	
	/**
	 * Call to deploy a VDB Zip xml file
	 * @param vdbFileName
	 * @throws Exception
	 */
	public void deployVDBZip(String vdbFileName) throws Exception {
		URL urlToFile = TeiidEmbeddedDriver.class.getClassLoader().getResource(vdbFileName);
		if (urlToFile == null) {
			throw new RuntimeException("Unable to get URL for vdb zip file " + vdbFileName);
		}
		
		server.deployVDBZip(urlToFile);
	}
	
	/** 
	 * Call to obtain a Connection based on the <code>url</code>
	 * @param url
	 * @param props
	 * @return Connection
	 * @throws Exception
	 */
	public Connection getConnection(String url, Properties props) throws Exception {
		return server.getDriver().connect("jdbc:teiid:Portfolio", props);
	}
	

	
	public void shutdown() {
		server.stop();
	}

	protected ClassRegistry getClassRegistry()
	{
		return this.registry;
	}
	
	public EmbeddedServer getEmbeddedServer() {
		return this.server;
	}
	
	private void loadMappingFiles() throws IOException {
		
		URL urlToFile = TeiidEmbeddedDriver.class.getClassLoader().getResource(TRANSLATOR_MAPPING_FILE);
		if (urlToFile == null) {
			throw new RuntimeException("Unable to get URL for file " + TRANSLATOR_MAPPING_FILE);
		}

		Properties translatorMapping = PropertiesUtils.loadFromURL(urlToFile);

		urlToFile = TeiidEmbeddedDriver.class.getClassLoader().getResource(CONNECTOR_MAPPING_FILE);
		if (urlToFile == null) {
			throw new RuntimeException("Unable to get URL for file " + CONNECTOR_MAPPING_FILE);
		}

		Properties connectorMapping = PropertiesUtils.loadFromURL(urlToFile);
		
		registry.setConnectorTypeClassMapping(connectorMapping);
		registry.setTranslatorTypeClassMapping(translatorMapping);
	}

	public static void main(String[] args) throws Exception {
	
	}

}
