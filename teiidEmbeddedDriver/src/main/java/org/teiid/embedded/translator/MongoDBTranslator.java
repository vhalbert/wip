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

import org.teiid.embedded.Configuration;
import org.teiid.embedded.TeiidEmbeddedMgr;
import org.teiid.embedded.component.TeiidTranslatorWrapper;
import org.teiid.translator.cassandra.CassandraExecutionFactory;
import org.teiid.translator.mongodb.MongoDBExecutionFactory;


/**
 * @author vanhalbert
 *
 */
public class MongoDBTranslator extends TeiidTranslatorWrapper {

	
	@Override
	public void initialize(TeiidEmbeddedMgr manager, Configuration config)
			throws Exception {
		
		MongoDBExecutionFactory factory = new MongoDBExecutionFactory();
		this.applyProperties(factory, config.getProperties());
		
		factory.start();
		manager.getEmbeddedServer().addTranslator(config.getName(), factory);
		
	}

}
