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

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.teiid.core.util.PropertiesUtils;

/**
 * @author vanhalbert
 *
 */
public class EmbeddedUtil {

	public static void setProperties(Object obj, Properties props) {
		
		Iterator<Object> it = props.keySet().iterator();
		while(it.hasNext()) {
			String k = (String) it.next();
			String v = props.getProperty(k);
			PropertiesUtils.setBeanProperty(obj,
					k, v);
		}
	}
	
	/**
	 * Call to set bean property, but only perform it for those specified
	 * for the <code>useprops</code>
	 * @param obj
	 * @param useprops are the only properties to apply
	 * @param props
	 */
	public static void setProperties(Object obj, List<String> useprops, Properties props) {
		
		for (String propName : useprops) {
			String v = props.getProperty(propName);
			if (v != null) {
				PropertiesUtils.setBeanProperty(obj,
					propName, v);
			}
		}

	}	
	
	public static String getStoreDir() {
		String defDir = getSystemProperty("user.home") + File.separator + ".teiid/embedded/data"; //$NON-NLS-1$ //$NON-NLS-2$
		return getSystemProperty("teiid.embedded.txStoreDir", defDir);
	}


	public static String getSystemProperty(final String name, final String value) {
		return AccessController.doPrivileged(new PrivilegedAction<String>(){

			public String run() {
				return System.getProperty(name, value);
			}});
	}
	
	public static String getSystemProperty(final String name) {
		return AccessController.doPrivileged(new PrivilegedAction<String>(){

			public String run() {
				return System.getProperty(name);
			}});
	}	
}
