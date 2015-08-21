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
package org.teiid.embedded.translator;

import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.teiid.embedded.configuration.TranslatorConfiguration;
import org.teiid.runtime.EmbeddedServer;

/**
 * @author vanhalbert
 *
 */
public class TestTranslators {
	static EmbeddedServer SERVER;
	
	@BeforeClass
	public static void onlyOnce() {
		SERVER = new EmbeddedServer();
		
	}
	@Test public void testFileTranslator() throws Exception {	
		TranslatorConfiguration config = new TranslatorConfiguration();
		config.setName("fileTranslator");
		config.setConnectorName("fileConnector");
		config.setType("file");
		
		Properties props = new Properties();
		config.setProperties(props);
		
		FileTranslator ft = new FileTranslator();
		ft.initialize(SERVER, config);
		
	}
	
	@Test public void testH2Translator() throws Exception {	
		TranslatorConfiguration config = new TranslatorConfiguration();
		config.setName("h2-Translator");
		config.setConnectorName("h2-Connector");
		config.setType("h2");
		
		Properties props = new Properties();
		props.setProperty("SupportsDirectQueryProcedure", "true");
		config.setProperties(props);
		
		H2JDBCTranslator ft = new H2JDBCTranslator();
		ft.initialize(SERVER, config);
		
	}	
	
	@AfterClass
	public static void shutDown() {
		SERVER.stop();
	}

}
