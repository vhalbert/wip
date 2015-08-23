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
package org.teiid.embedded.component;

import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.teiid.embedded.TeiidEmbeddedDriver;
import org.teiid.embedded.TeiidEmbeddedMgr;
import org.teiid.embedded.configuration.TranslatorConfiguration;

/**
 * @author vanhalbert
 *
 */
public class TestTranslators {
	static TeiidEmbeddedDriver DRIVER;
	static TeiidEmbeddedMgr MANAGER;
	
	@BeforeClass
	public static void onlyOnce() {
		DRIVER = new TeiidEmbeddedDriver();

		MANAGER = new TeiidEmbeddedMgr(DRIVER);	
		try {
			MANAGER.initialize("BenchmarkConfiguration.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TeiidEmbeddedDriver.LOG_COMPONENT_SET_METHODS = true;
	}	
	
	@Test public void testFileTranslator() throws Exception {	
		TranslatorConfiguration config = new TranslatorConfiguration();
		config.setName("fileTranslator");
		config.setConnectorName("fileConnector");
		config.setType("file");
		
		Properties props = new Properties();
		config.setProperties(props);
		
		TeiidTranslatorWrapper ft = (TeiidTranslatorWrapper) config.createComponentWrapperInstance(MANAGER);
		ft.initialize(MANAGER, config);
		
	}
	
	@Test public void testH2Translator() throws Exception {	
		TranslatorConfiguration config = new TranslatorConfiguration();
		config.setName("h2-Translator");
		config.setConnectorName("h2-Connector");
		config.setType("h2");
		
		Properties props = new Properties();
		props.setProperty("SupportsDirectQueryProcedure", "true");
		config.setProperties(props);
		
		TeiidTranslatorWrapper ft = (TeiidTranslatorWrapper) config.createComponentWrapperInstance(MANAGER);
		ft.initialize(MANAGER, config);
		
	}	
	
	@Test public void testCassandraTranslator() throws Exception {	
		TranslatorConfiguration config = new TranslatorConfiguration();
		config.setName("cassandra-Translator");
		config.setConnectorName("cassandra-Connector");
		config.setType("cassandra");
		
		Properties props = new Properties();
		config.setProperties(props);
		
		TeiidTranslatorWrapper ft = (TeiidTranslatorWrapper) config.createComponentWrapperInstance(MANAGER);
		ft.initialize(MANAGER, config);
		
	}	
	
	@Test public void testExcelTranslator() throws Exception {	
		TranslatorConfiguration config = new TranslatorConfiguration();
		config.setName("excel-Translator");
		config.setConnectorName("fileConnector");
		config.setType("excel");
		
		Properties props = new Properties();
		props.setProperty("SupportsDirectQueryProcedure", "true");
		config.setProperties(props);
		
		TeiidTranslatorWrapper ft = (TeiidTranslatorWrapper) config.createComponentWrapperInstance(MANAGER);
		ft.initialize(MANAGER, config);
		
	}	
	
	@Test public void testWSTranslator() throws Exception {	
		TranslatorConfiguration config = new TranslatorConfiguration();
		config.setName("wsTranslator");
		config.setConnectorName("wsConnector");
		config.setType("ws");
		
		Properties props = new Properties();
		config.setProperties(props);
		
		TeiidTranslatorWrapper ft = (TeiidTranslatorWrapper) config.createComponentWrapperInstance(MANAGER);
		ft.initialize(MANAGER, config);
		
	}
	
	@Test public void testMongoDBTranslator() throws Exception {	
		TranslatorConfiguration config = new TranslatorConfiguration();
		config.setName("mongodb-Translator");
		config.setConnectorName("mongodb-Connector");
		config.setType("mongodb");
		
		Properties props = new Properties();
		config.setProperties(props);
		
		TeiidTranslatorWrapper ft = (TeiidTranslatorWrapper) config.createComponentWrapperInstance(MANAGER);
		ft.initialize(MANAGER, config);
		
	}	
	
	@AfterClass
	public static void shutDown() {
		DRIVER.shutdown();
	}

}
