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
package org.teiid.reveng.template;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.teiid.reveng.metadata.db.DBMetadataProcessor;

import org.teiid.core.util.UnitTestUtil;

/**
 * @author vanhalbert
 *
 */
public class TestTemplateProcessing {
	
	static TeiidEmbeddedPortfolio VDB = new TeiidEmbeddedPortfolio();
	
    static Connection conn = null;
    
    
    @Before
    public void setUp() throws Exception {
 //       dbmd = new DatabaseMetaDataImpl((ConnectionImpl) conn);
    }

    @AfterClass
    public static void oneTimeTearDown() throws Exception {
    	if (VDB != null) {
            VDB.closeConnection();
        }
    }    
    
    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
       	VDB.initialize();
        
       	conn = VDB.getConnection();
       	
//    	server = new FakeServer(true);
//    	server.setThrowMetadataErrors(false); //there are invalid views due to aggregate datatype changes
//    	server.deployVDB("QT_Ora9DS", UnitTestUtil.getTestDataPath()+"/QT_Ora9DS_1.vdb");
//        conn = server.createConnection("jdbc:teiid:QT_Ora9DS"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    @Test
    public void testBuildingTemplate() throws Exception {
    	
		  DBMetadataProcessor metadata = new DBMetadataProcessor();
		  List<String> tables =  new ArrayList<String>();
		  tables.add("%");
		  metadata.loadMetadata(conn, null, null, tables);

    	
			TemplateProcessing tp = new TemplateProcessing(UnitTestUtil.getTestScratchPath());
			tp.processTables(metadata);
    }

}
