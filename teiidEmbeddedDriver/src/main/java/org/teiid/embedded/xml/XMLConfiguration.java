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
package org.teiid.embedded.xml;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.teiid.core.TeiidComponentException;
import org.teiid.embedded.EmbeddedPlugin;
import org.teiid.embedded.configuration.ConnectorConfiguration;
import org.teiid.embedded.configuration.EmbeddedServerConfiguration;
import org.teiid.embedded.configuration.TransactionMgrConfiguration;
import org.teiid.embedded.configuration.TranslatorConfiguration;

/**
 * @author vanhalbert
 *
 */
public class XMLConfiguration {

	EmbeddedServerConfiguration embeddedConfig;
	TransactionMgrConfiguration transMgrConfig;
	Map<String, TranslatorConfiguration> translators;
	Map<String, ConnectorConfiguration> connectors;
	List<String> vdbs;

	public void createConfigurationComponents(String fileName) throws Exception {

		URL urlToFile = XMLConfiguration.class.getClassLoader().getResource(fileName);
		if (urlToFile == null) {
			throw new RuntimeException("Unable to get URL for file " + fileName);
		}
		File xmlFile = new File(urlToFile.toURI());		
		
		XMLVisitationStrategy strategy = new XMLVisitationStrategy();
		strategy.init(xmlFile);
		
		embeddedConfig = strategy.getEmbeddedServerConfiguration();
		transMgrConfig = strategy.getTransactionMgr();
		translators = strategy.getTranslatorConfigurations();
		connectors = strategy.getConnectorConfigurations();
				
		Iterator<String> mtIt = translators.keySet().iterator();
		while (mtIt.hasNext()) {
			String k = mtIt.next();
			TranslatorConfiguration t = translators.get(k);
			
			if (t.getConnectorName() != null) {
				ConnectorConfiguration cc = connectors.get(t.getConnectorName());
				if (cc == null) {
	            	throw new TeiidComponentException(EmbeddedPlugin.Util.getString(
	    					"EMBDF10004", t.getConnectorName()));
				} 
			}

		}
		
		vdbs = strategy.getVDBs();
		
	}
	
	public EmbeddedServerConfiguration getEmbeddedConfiguration() {
		return embeddedConfig;
	}
	
	public TransactionMgrConfiguration getTransactionMgrConfiguration() {
		return transMgrConfig;
	}
	
	public Collection<TranslatorConfiguration> getTranslators() {
		return translators.values();
	}

	public Collection<ConnectorConfiguration> getConnectors() {
		return connectors.values();
	}
	
	public List<String> getVDBs() {
		return vdbs;
	}
	
}
