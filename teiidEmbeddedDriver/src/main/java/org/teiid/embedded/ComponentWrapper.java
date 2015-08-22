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

import java.lang.reflect.Method;
import java.util.Properties;

import org.teiid.embedded.util.EmbeddedUtil;

/**
 * The implementation of the ComponentWrapper represents the actual component logic that is 
 * used for configuring a specific component in the EmbeddedServer.
 * 
 * Examples implementations:
 * <li>each translator will be implemented
 * <li>each resource adapter or data sources that don't need external resources
 * <li>transaction manager
 * <li>embedded configuration that controls embedded server
 * 
 * 
 * @author vanhalbert
 *
 */
public abstract class ComponentWrapper {
	
	public static boolean LOG_SETTINGS = TeiidEmbeddedDriver.LOG_COMPONENT_SET_METHODS;

	public abstract void initialize(TeiidEmbeddedMgr manager, Configuration config) throws Exception ;

	protected void applyProperties(Object object, Properties props) {
		if (LOG_SETTINGS) {
			TELogger.log("\r---- Component: " + object.getClass().getSimpleName() + "\r");
			Method[] ms = object.getClass().getMethods();
			for (Method m: ms) {
				if (m.getName().startsWith("set") && m.getParameterTypes().length == 1) {
					TELogger.log("\t" + m.getName() + " (type) " + m.getParameterTypes()[0]  + "\r");
				}
			}
			TELogger.log("---- End Component: " + object.getClass().getSimpleName());
		}
		
		if (props != null) {
			EmbeddedUtil.setProperties(object, props);
		}
	}

	

}
