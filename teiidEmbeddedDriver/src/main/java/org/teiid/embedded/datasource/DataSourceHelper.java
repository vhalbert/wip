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
package org.teiid.embedded.datasource;

import javax.resource.ResourceException;
import javax.sql.DataSource;

//import org.jboss.jca.adapters.jdbc.local.LocalManagedConnectionFactory;
//import org.jboss.jca.core.api.connectionmanager.pool.PoolConfiguration;
//import org.jboss.jca.core.connectionmanager.notx.NoTxConnectionManagerImpl;
//import org.jboss.jca.core.connectionmanager.pool.strategy.OnePool;

/**
 *  The 'newDataSource' method return a ironjacamar WrapperDataSource which used to set EmbeddedServer's named connection factory:
 * 
 * <pre>
 *  EmbeddedServer server = new EmbeddedServer();
 *  ...
 *  DataSource ds = DataSourceHelper.newDataSource("${driver}", "${url}", "${user}", "${password}");
 *  server.addConnectionFactory("name", ds);
 *  ...
 * </pre>
 * 
 * 
 * 
 * @author vanhalbert
 *
 */
public class DataSourceHelper {


	public static DataSource newDataSource(String driverClass, String connURL, String user, String password) throws ResourceException{
//
//		LocalManagedConnectionFactory mcf = new LocalManagedConnectionFactory();
//		
//		mcf.setDriverClass(driverClass);
//		mcf.setConnectionURL(connURL);
//		mcf.setUserName(user);
//		mcf.setPassword(password);
//		
//		NoTxConnectionManagerImpl cm = new NoTxConnectionManagerImpl();
//		OnePool pool = new OnePool(mcf, new PoolConfiguration(), false);
//		pool.setConnectionListenerFactory(cm);
//		cm.setPool(pool);
//		
//		return (DataSource) mcf.createConnectionFactory(cm);
		return null;
	}
}
