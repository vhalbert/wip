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
package org.teiid.embedded.transactionmgr;

import java.util.ArrayList;
import java.util.List;

import org.teiid.embedded.Configuration;
import org.teiid.embedded.TeiidEmbeddedMgr;
import org.teiid.embedded.component.TeiidTransactionMgrWrapper;
import org.teiid.embedded.util.EmbeddedUtil;

import com.arjuna.ats.arjuna.common.ObjectStoreEnvironmentBean;
import com.arjuna.ats.arjuna.common.arjPropertyManager;
import com.arjuna.common.internal.util.propertyservice.BeanPopulator;

/**
 * @author vanhalbert
 *
 */
public class ArjunaTransactionManager extends TeiidTransactionMgrWrapper {
	
//	@Override
//	public TransactionManager getTransactionManager() throws Exception {
//	
//		
//		this.applyProperties(arjPropertyManager.getCoreEnvironmentBean(), getProperties());
//		
//		
//		
//		return com.arjuna.ats.jta.TransactionManager.transactionManager();
//	}


	@Override
	public void initialize(TeiidEmbeddedMgr manager, Configuration config) throws Exception {
		
		List<String> propNames = new ArrayList<String>(3);
		propNames.add("NodeIdentifier");
		propNames.add("SocketProcessIdPort");
		propNames.add("SocketProcessIdMaxPorts");
		
		EmbeddedUtil.setProperties(arjPropertyManager.getCoreEnvironmentBean(), propNames, config.getProperties());

		propNames = new ArrayList<String>(3);
		propNames.add("EnableStatistics");
		propNames.add("DefaultTimeout");
		propNames.add("TransactionStatusManagerEnable");
		propNames.add("TxReaperTimeout");
		
		EmbeddedUtil.setProperties(arjPropertyManager.getCoordinatorEnvironmentBean(), propNames, config.getProperties());
		
		String storeDir = EmbeddedUtil.getStoreDir();
		
		arjPropertyManager.getObjectStoreEnvironmentBean().setObjectStoreDir(storeDir);
		
		BeanPopulator.getNamedInstance(ObjectStoreEnvironmentBean.class, "communicationStore").setObjectStoreDir(storeDir); //$NON-NLS-1$

		this.applyProperties(arjPropertyManager.getCoreEnvironmentBean(), config.getProperties());
		
//		
//		return com.arjuna.ats.jta.TransactionManager.transactionManager();

		manager.getEmbeddedConfiguration().setTransactionManager(com.arjuna.ats.jta.TransactionManager.transactionManager());
	}

}