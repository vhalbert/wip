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
package org.teiid.embedded.xml;

/**
 * @author vanhalbert
 *
 */
public interface TagNames {
    /**
     * Enumeration of XML element tag constants.
     */
    public static interface Configuation {
        public static final String ROOT = "configuration"; //$NON-NLS-1$        
    }   
    
    public static interface Embedded {
        public static final String ROOT = "embedded"; //$NON-NLS-1$
        public static final String TRANSACTIONMGR = "transactionmgr"; //$NON-NLS-1$
        
    }   
    
    public static interface TransactionMgr {
        public static final String ROOT = "transactionmgr"; //$NON-NLS-1$
        public static final String CLASS_NAME = "className"; //$NON-NLS-1$
        
    }      
    public static interface Translators {
    	public static final String ROOT = "translators"; //$NON-NLS-1$
        public static final String TRANSLATOR = "translator"; //$NON-NLS-1$
        
        public static final String CONNECTOR_NAME = "connectorName"; //$NON-NLS-1$
        
    }
    public static interface Connectors {
    	public static final String ROOT = "connectors"; //$NON-NLS-1$
        public static final String CONNECTOR = "connector"; //$NON-NLS-1$

        public static final String JNDI_NAME = "jndiName"; //$NON-NLS-1$
 
    } 
    
    public static interface VDBs {
        public static final String ROOT = "vdbs"; //$NON-NLS-1$
        public static final String VDB = "vdb"; //$NON-NLS-1$
    }
    
    public static interface Properties {
        public static final String ROOT = "properties"; //$NON-NLS-1$
        public static final String PROPERTY = "property"; //$NON-NLS-1$
        
    } 
    
    public static final class Common_Attributes {
         public static final String NAME = "name"; //$NON-NLS-1$
    }
    
    public static final class Common_Elements {
        public static final String TYPE = "type"; //$NON-NLS-1$
    	
    }
    
   
}
