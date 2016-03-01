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
package org.teiid.reverseeng.pojo;


import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.teiid.core.util.UnitTestUtil;
import org.teiid.reverseeng.Options;
import org.teiid.reverseeng.ReverseEngineerFactory;
import org.teiid.reverseeng.metadata.db.DBMetadataProcessor;
import org.teiid.reverseeng.metadata.db.DBOptions;
import org.teiid.reverseeng.pojo.PojoProcessing;

/**
 * @author vanhalbert
 *
 */
public class TestPojoProcessing {
	
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
    }
    
    @Test
    public void testBuildingTemplate() throws Exception {
		  List<String> tables =  new ArrayList<String>();
		  tables.add("%");

    	DBOptions options = new DBOptions();
		  
		  options.setDbCatalog(null);
		  options.setDbSchema(null);
		  options.setTables(tables);
		  
		  options.setProperty(Options.Parms.BUILD_LOCATION, UnitTestUtil.getTestScratchPath() + File.separatorChar+ "noannotations");
		  
//		  options.setAnnotationType(Options.Annotation_Type.Hibernate);
		  
		  ReverseEngineerFactory.perform(conn, options);
		    	  
//		  DBMetadataProcessor metadata = DBMetadataProcessor.loadMetadata(conn, options);
  	
//			PojoProcessing tp = new PojoProcessing(options);
//			tp.processTables(metadata);
    	
//    	
//    	
//    	
//		  DBMetadataProcessor metadata = DBMetadataProcessor.loadMetadata(conn, null, null, tables);
//
//		  String path = UnitTestUtil.getTestScratchPath() + File.separatorChar+ "noannotations";
//		  File f = new File(path);
//		  f.mkdirs();
//    	
//			TemplateProcessing tp = new TemplateProcessing(path);
//			tp.processTables(metadata);
    }
    
    @Test
    public void testBuildingTemplateWithHibernateAnnotations() throws Exception {
		  List<String> tables =  new ArrayList<String>();
		  tables.add("%");

		  DBOptions options = new DBOptions();
		  
		  options.setDbCatalog(null);
		  options.setDbSchema(null);
		  options.setTables(tables);
		  
		  options.setProperty(Options.Parms.BUILD_LOCATION, UnitTestUtil.getTestScratchPath() + File.separatorChar+ "hibernateannotations");
		  
		  options.setAnnotationType(Options.Annotation_Type.Hibernate);

		  ReverseEngineerFactory.perform(conn, options);
		  
//		  DBMetadataProcessor metadata = DBMetadataProcessor.loadMetadata(conn, options);
//	
//			PojoProcessing tp = new PojoProcessing(options);
//			tp.processTables(metadata);    	
    	
// 
//		  Options o = new Options();
//		  o.setAnnotationType(Options.Annotation_Type.Hibernate);
//    	
//		  String path = UnitTestUtil.getTestScratchPath() + File.separatorChar+ "hibernateannotations";
//		  File f = new File(path);
//		  f.mkdirs();
//    	
//			TemplateProcessing tp = new TemplateProcessing(path, o);
//			tp.processTables(metadata);
    }   

    @Test
    public void testBuildingTemplateWithProtobufAnnotations() throws Exception {
		  List<String> tables =  new ArrayList<String>();
		  tables.add("%");

		  DBOptions options = new DBOptions();
		  
		  options.setDbCatalog(null);
		  options.setDbSchema(null);
		  options.setTables(tables);
		  
		  options.setProperty(Options.Parms.BUILD_LOCATION, UnitTestUtil.getTestScratchPath() + File.separatorChar+ "protobufannotations");
		  
		  options.setAnnotationType(Options.Annotation_Type.Protobuf);
		    	  
		  ReverseEngineerFactory.perform(conn, options);

//		  DBMetadataProcessor metadata = DBMetadataProcessor.loadMetadata(conn, options);
//	
//			PojoProcessing tp = new PojoProcessing(options);
//			tp.processTables(metadata);    	 	
    	
    	
//    	
//		  List<String> tables =  new ArrayList<String>();
//		  tables.add("%");
//		  
//		  DBMetadataProcessor metadata = DBMetadataProcessor.loadMetadata(conn, null, null, tables);
//
//		  Options o = new Options();
//		  o.setAnnotationType(Options.Annotation_Type.Protobuf);
//    	
//		  String path = UnitTestUtil.getTestScratchPath() + File.separatorChar+ "protobufannotations";
//		  File f = new File(path);
//		  f.mkdirs();
//    	
//			TemplateProcessing tp = new TemplateProcessing(path, o);
//			tp.processTables(metadata);
    }   
}
