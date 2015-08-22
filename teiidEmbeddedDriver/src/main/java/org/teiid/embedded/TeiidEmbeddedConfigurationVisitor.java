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


/**
 * @author vanhalbert
 *
 */
public class TeiidEmbeddedConfigurationVisitor implements ConfigurationVisitor {
	private TeiidEmbeddedMgr manager;

	
	public TeiidEmbeddedConfigurationVisitor(TeiidEmbeddedMgr mgr) {
		this.manager = mgr;
	}

	@Override
	public void visit(Configuration config) throws Exception {
		
		ComponentWrapper cw = config.createComponentWrapperInstance(manager);
		cw.initialize(manager, config);
		
//		if (config instanceof EmbeddedServerConfiguration) {
//			manager.setEmbeddedConfiguration(  ((TeiidEmbeddedConfigurationWrapper) cw).getEmbeddedConfiguration() );
//		}
			
		
//		if (config instanceof TranslatorConfiguration) {
//			TranslatorConfiguration tc = (TranslatorConfiguration) config;
//			
//			try {
//				TeiidTranslatorWrapper tw =  manager.getClassRegistry().getTranslatorClassInstance(tc.getType());
//				tw.initialize(manager.getEmbeddedServer(), tc);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} else if (config instanceof ConnectorConfiguration) {
//			ConnectorConfiguration cc = (ConnectorConfiguration) config;
//			
//			try {
//				TeiidConnectorWrapper cw =  manager.getClassRegistry().getConnectorClassInstance(cc.getType());
//				cw.initialize(manager.getEmbeddedServer(), cc);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} else if (config instanceof EmbeddedServerConfiguration) {
//			EmbeddedServerConfiguration esc = (EmbeddedServerConfiguration) config;
//			
//			TeiidEmbeddedConfigurationWrapper tecw = new TeiidEmbeddedConfigurationWrapper();
//			
//			tecw.initialize(manager.getEmbeddedServer(), esc);
//			
//			manager.setEmbeddedConfiguration(tecw.getEmbeddedConfiguration());
//			
//		} else if (config instanceof TransactionMgrConfiguration) {
//			TransactionMgrConfiguration tmr = (TransactionMgrConfiguration) config;
//			
//			TeiidTransactionMgrWrapper tmw = (TeiidTransactionMgrWrapper) tmr.createComponentWrapperInstance(manager);
//			
//			
//			
//		}
	}

}
