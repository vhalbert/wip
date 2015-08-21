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
package org.teiid.embedded.configuration;

import org.teiid.embedded.TeiidTransactionMgrWrapper;
import org.teiid.embedded.util.ClassRegistry;
import org.teiid.embedded.util.EmbeddedUtil;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;


/**
 * @author vanhalbert
 *
 */
public class EmbeddedServerConfiguration extends BaseConfiguration {
	private EmbeddedConfiguration config;
	
	private String transactionMgrClassName;
	
	/**
	 * @return transactionMgrClassName
	 */
	public String getTransactionMgrClassName() {
		return transactionMgrClassName;
	}

	/**
	 * @param transactionMgrClassName Sets transactionMgrClassName to the specified value.
	 */
	public void setTransactionMgrClassName(String transactionMgrClassName) {
		this.transactionMgrClassName = transactionMgrClassName;
	}

	public void initialize(EmbeddedServer server)
			throws Exception {
    	
		config = new EmbeddedConfiguration();
     	
		if (this.getProperties() != null) {
			EmbeddedUtil.setProperties(config, getProperties());
		}
    	
		if (this.transactionMgrClassName != null) {					
			Class<?> clz = ClassRegistry.loadClass(getTransactionMgrClassName(), this.getClass().getClassLoader());
			TeiidTransactionMgrWrapper wrapper = (TeiidTransactionMgrWrapper)  clz.newInstance();
			config.setTransactionManager(wrapper.getTransactionManager()); 
		}
	}
	
	public EmbeddedConfiguration getEmbeddedConfiguration() {
		return this.config;
	}
}
