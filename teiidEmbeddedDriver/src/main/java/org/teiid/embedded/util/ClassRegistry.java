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
package org.teiid.embedded.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.teiid.embedded.component.TeiidConnectorWrapper;
import org.teiid.embedded.component.TeiidTranslatorWrapper;
import org.teiid.translator.ExecutionFactory;


/**
 * The ClassRegistryUtil is used to manage the classes registered with the cache, as well as,
 * a map of the associated {@link Method methods}, so that the methods don't have to be re-loaded
 * as different parts of the logic require their use.
 *  
 * @author vanhalbert
 *
 */
public class ClassRegistry {
	
	public static final String OBJECT_NAME = "o"; //$NON-NLS-1$
	
	private Properties translatorMapping;
	private Properties connectorMapping;
	
	private Map<String, Class<?>> translatorClasses = new HashMap<String, Class<?>>(3); // type, Class
	private Map<String, Class<?>> connectorClasses = new HashMap<String, Class<?>>(3); // type, Class

	/**
	 * Call to set the translator mapping that indicates which class to use for which translator type
	 * @param props <String, String>   // type ==> className
	 */
	public void setTranslatorTypeClassMapping(Properties props) {
		this.translatorMapping = props;
	}
	/**
	 * Call to set the connector mapping that indicates which class to use for which connector type
	 * @param props <String, String>   // type ==> className
	 */
	public void setConnectorTypeClassMapping(Properties props) {
		this.connectorMapping = props;
	}
	
	/**
	 * Call to get a TeiidTranslatorWrapper instance.  
	 * @param type
	 * @return ExecutionFactory<?, ?>
	 * @throws Exception
	 */
	public ExecutionFactory<?, ?> getFactoryClassInstance(String type) throws Exception {
		Class<?> clz =  this.translatorClasses.get(type);
		if (clz == null) {
			String className = translatorMapping.getProperty(type);
			
			if (className == null) {
				throw new Exception("No ExecutionFactory className match for type " + type);
			}
			clz = registerTranslatorClass(type, className);
		}
		
		return (ExecutionFactory<?, ?>) clz.newInstance();
	}	

	
	public TeiidConnectorWrapper getConnectorClassInstance(String type) throws Exception {
		Class<?> clz =  this.connectorClasses.get(type);
		if (clz == null) {
			String className = connectorMapping.getProperty(type);
			
			if (className == null) {
				throw new Exception("No connector mapping className for type " + type);
			}
			clz = registerConnectorClass(type, className);
		}
		return (TeiidConnectorWrapper) clz.newInstance();
	}

	private synchronized Class<?> registerTranslatorClass(String type, String className) throws Exception {
			
			Class<?> clz = loadClass(className, null);
			translatorClasses.put(type, clz);	
			return clz;
	}
	
	private synchronized Class<?> registerConnectorClass(String type, String className) throws Exception {

		Class<?> clz = loadClass(className, null);
		connectorClasses.put(type, clz);
		return clz;
	}

	
	public static Class<?> loadClass(String className, ClassLoader cl) throws Exception {

		if (cl != null) {
			return Class.forName(className, true, cl);
		}
		return Class.forName(className);

	}

    public void cleanUp() {
    	translatorClasses.clear();
    	connectorClasses.clear();
    }


}
