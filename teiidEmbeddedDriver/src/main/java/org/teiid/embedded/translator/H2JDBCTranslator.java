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

import org.teiid.embedded.TeiidTranslatorWrapper;
import org.teiid.embedded.configuration.TranslatorConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;


/**
 * @author vanhalbert
 *
 */
public class H2JDBCTranslator extends TeiidTranslatorWrapper {


	@Override
	public void initialize(EmbeddedServer server, TranslatorConfiguration config)
			throws Exception {

		H2ExecutionFactory executionFactory = new H2ExecutionFactory() ;
		
	   	
		this.applyProperties(executionFactory, config.getProperties());

//		executionFactory.setSupportsDirectQueryProcedure(true);
		executionFactory.start();
		server.addTranslator(config.getType(), executionFactory);
	}

}
