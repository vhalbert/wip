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

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Properties;

import org.teiid.core.util.PropertiesUtils;
import org.teiid.embedded.configuration.ConnectorConfiguration;
import org.teiid.embedded.configuration.TranslatorConfiguration;
import org.teiid.embedded.util.ClassRegistry;
import org.teiid.embedded.xml.XMLConfiguration;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;

/**
 * @author vanhalbert
 *
 */
public class TeiidEmbeddedMgr {

	private static final String CONNECTOR_MAPPING_FILE = "org/teiid/embedded/ConnectorMapping.txt";
	private static final String TRANSLATOR_MAPPING_FILE = "org/teiid/embedded/TranslatorMapping.txt";


	private TeiidEmbeddedDriver driver;
	private EmbeddedConfiguration config;
	private XMLConfiguration xmlConfig;
	
	private ClassRegistry registry = new ClassRegistry();
	
	public TeiidEmbeddedMgr(TeiidEmbeddedDriver driver) {
		this.driver = driver;
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
		
		xmlConfig = new XMLConfiguration();
		xmlConfig.createConfigurationComponents(configurationFileName);
		
		TeiidEmbeddedConfigurationVisitor tecVisitor = new TeiidEmbeddedConfigurationVisitor(this);
		
		// NOTE:  the order of processing (visiting) the configuration objects is important.
		//			as the EmbeddedConfiguration needs to be configured prior to
		//			configuring the TransactionMgr.
		xmlConfig.getEmbeddedConfiguration().accept(tecVisitor);
		
		xmlConfig.getTransactionMgrConfiguration().accept(tecVisitor);
		
		Collection<TranslatorConfiguration> mt = xmlConfig.getTranslators();
		for (TranslatorConfiguration tc : mt) {
			tc.accept(tecVisitor);
		}
		Collection<ConnectorConfiguration> ct = xmlConfig.getConnectors();
		for (ConnectorConfiguration cc : ct) {
			cc.accept(tecVisitor);
		}

	}
	
	public ClassRegistry getClassRegistry()
	{
		return this.registry;
	}
	
	public TeiidEmbeddedDriver getTeiidEmbeddedDriver() {
		return this.driver;
	}
	
	public EmbeddedServer getEmbeddedServer() {
		return this.driver.getEmbeddedServer();
	}
	
	public EmbeddedConfiguration getEmbeddedConfiguration() {
		return this.config;
	}
	
	public void setEmbeddedConfiguration(EmbeddedConfiguration ec) {
		this.config = ec;
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

}
