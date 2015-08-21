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


/**
 * @author vanhalbert
 *
 */
public class TranslatorConfiguration extends BaseConfiguration {
	
	private String type;
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
	 * Returns the translator type.  This is the translator type used in the mapping of 
	 * the source model to the data source.
	 * @return type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type Sets type to the specified value.
	 */
	public void setType(String type) {
		this.type = type;
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

}
