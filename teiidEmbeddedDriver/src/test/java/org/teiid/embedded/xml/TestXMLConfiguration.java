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

import java.util.Iterator;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.teiid.embedded.configuration.ConnectorConfiguration;
import org.teiid.embedded.configuration.TranslatorConfiguration;

/**
 * @author vanhalbert
 *
 */
public class TestXMLConfiguration {

	@Test public void testBenchMarkConfiguration() throws Exception {						
		
		XMLConfiguration c = new XMLConfiguration();
		c.configureEmbedded("BenchmarkConfiguration.xml");
		
		Assert.assertNotNull(c.getEmbeddedConfiguration());
		Assert.assertNotNull(c.getConnectors());
		Assert.assertNotNull(c.getTranslators());
		Assert.assertNotNull(c.getVDBs());
		
		Assert.assertEquals(2, c.getTranslators().size());
		Assert.assertEquals(1, c.getConnectors().size());
		Assert.assertEquals(2, c.getVDBs().size());
		
		Map<String, TranslatorConfiguration> mt = c.getTranslators();
		
		Iterator<String> mtIt = mt.keySet().iterator();
		while (mtIt.hasNext()) {
			String k = mtIt.next();
			TranslatorConfiguration t = mt.get(k);
			if (t.getName().equals("file-translator")) {
				Assert.assertEquals(0, t.getProperties().size());
			} else if (t.getName().equals("h2-translator")) {
				Assert.assertEquals(1, t.getProperties().size());
			}
		}
		Map<String, ConnectorConfiguration> ct = c.getConnectors();
		Iterator<String> ctIt = ct.keySet().iterator();
		while (ctIt.hasNext()) {
			String k = ctIt.next();
			ConnectorConfiguration t = ct.get(k);
			Assert.assertEquals(1, t.getProperties().size());
			if (k.equals("file-connector")) {
				Assert.assertEquals(t.getJndiName(), "java:/marketdata-file");
				Assert.assertEquals(1, t.getProperties().size());
			} else if (k.equals("h2-connector")) {
				Assert.assertEquals(t.getJndiName(), "java:/accounts-ds");
				Assert.assertEquals(0, t.getProperties().size());
			}
		}

		
	}	
	
}
