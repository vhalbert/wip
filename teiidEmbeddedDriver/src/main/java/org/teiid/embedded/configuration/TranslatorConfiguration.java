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

import org.teiid.embedded.ComponentWrapper;
import org.teiid.embedded.Configuration;
import org.teiid.embedded.TeiidEmbeddedMgr;
import org.teiid.embedded.component.TeiidTranslatorWrapper;


/**
 * @author vanhalbert
 *
 */
public class TranslatorConfiguration extends Configuration {
	
	private String connectorName;
	
	// Not every translator will have a defined connector in the configuration.  For those that don't,
	// the user will be calling driver to add the connection factory. 
	private boolean hasConnector = false;

	/**
	 * @return hasConnector
	 */
	public boolean isHasConnector() {
		return hasConnector;
	}

	/**
	 * Returns the name of the connector.  This must match one of the named connectors that were defined.
	 * @return connectorName
	 */
	public String getConnectorName() {
		return connectorName;
	}
	/**
	 * @param connectorName Sets connectorName to the specified value.
	 */
	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
		this.hasConnector = true;
	}
	
	
	@Override
	public  ComponentWrapper createComponentWrapperInstance(TeiidEmbeddedMgr manager) throws Exception {
//		TeiidTranslatorWrapper tw =  manager.getClassRegistry().getTranslatorClassInstance(getType());
		TeiidTranslatorWrapper tw = new TeiidTranslatorWrapper();
		tw.initialize(manager, this);
		return tw;

	}

}
