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

import java.util.Properties;


/**
 * A Configuration implementation represents the types of components that will be
 * created and configured for a given instance of {@link TeiidEmbeddedDriver}
 * 
 * Example:
 * <li>Embedded Configuration itself</li>
 * <li>TransactionMgr</li>
 * <li>Translator/Connector<li>
 * 
 * Each implementation will be processed by the {@link ConfigurationVisitor}, to be
 * used for the creation of {@link ComponentWrapper}
 * 
 * @author vanhalbert
 */
public abstract class Configuration {
	
	public abstract ComponentWrapper createComponentWrapperInstance(TeiidEmbeddedMgr manager) throws Exception;
	
	private String name = "NoName";
	private Properties properties = null;
	private String type = null;
	
	public void accept(ConfigurationVisitor visitor) throws Exception {
		visitor.visit( this);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	/**
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
	
	public void setProperties(Properties props) {
		this.properties = props;
	}
	
	public void setProperty(String key, String value) {
		this.properties.setProperty(key, value);
	}
	
	public Properties getProperties() {
		return this.properties;
	}
	
	protected ComponentWrapper createComponentWrapperInstance(String className) throws Exception {
		Class<?> clz =  Class.forName(className);
		return (ComponentWrapper) clz.newInstance();	
	}
}
