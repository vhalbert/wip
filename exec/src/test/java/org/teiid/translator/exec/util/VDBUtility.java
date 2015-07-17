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
package org.teiid.translator.exec.util;
 
import java.io.File;

import org.teiid.cdk.api.TranslationUtility;
import org.teiid.core.util.UnitTestUtil;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.translator.exec.ExecTranslatorConnection;

@SuppressWarnings("nls")
public class VDBUtility {
	
	protected static ExecTranslatorConnection CONN;
	
	public static TranslationUtility TRANSLATION_UTILITY = null;

	public static RuntimeMetadata RUNTIME_METADATA = null;
	
	static {
		File f = new File(UnitTestUtil.getTestDataPath() + File.separatorChar + File.separatorChar + "ExecVDB.vdb");
		System.out.println("TestDataPath " + f.getAbsolutePath());
		try {
			TRANSLATION_UTILITY = new TranslationUtility("ExecVDB.vdb", f.toURI().toURL());
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		RUNTIME_METADATA = VDBUtility.TRANSLATION_UTILITY.createRuntimeMetadata();
	}	
//
//
//	private static MetadataFactory createSchema() {
//		CONN  = mock(ExecTranslatorConnection.class); 
//		
//		ExecTranslatorExecutionFactory translator;
//
//		MetadataFactory mf = null;
//		translator = new ExecTranslatorExecutionFactory();
//		try {
//			translator.start();
//
//			mf = new MetadataFactory("vdb", 1, "objectvdb",
//					SystemMetadata.getInstance().getRuntimeTypeMap(),
//					new Properties(), null);
//	
//			translator.getMetadataProcessor().process(mf, CONN);
//		
//		} catch (TranslatorException e) {
//			
//		}
//
//		return mf;
//	}
//
//	  public static QueryMetadataInterface examplePersons() { 
//	    	MetadataStore store = new MetadataStore();
//	    	Schema objectModel = createSchema().getSchema();
//	    	store.addSchema(objectModel);
//	        
////	        Schema personViewSchema = RealMetadataFactory.createVirtualModel("Person_View_Model", store);
////	        
////	        String query = "SELECT t.PersonId, t.PersonName, t.Email FROM Persons_Object_Model.Persons";
////	        QueryNode qn = new QueryNode(query);
////	        
////	        // Create Contact group
////	        Table viewTable = RealMetadataFactory.createVirtualGroup("Person_View", personViewSchema, qn); //$NON-NLS-1$
////	       
////	        viewTable.setNameInSource("Persons_View"); //$NON-NLS-1$    
////	        viewTable.setProperty("Supports Query", Boolean.TRUE.toString()); //$NON-NLS-1$
////	        // Create Columns
////	        String[] elemNames = new String[] {
////	            "PersonID", "PersonName", "Email"  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
////	        };
////	        String[] elemTypes = new String[] {  
////	            DataTypeManager.DefaultDataTypes.INTEGER, DataTypeManager.DefaultDataTypes.STRING, DataTypeManager.DefaultDataTypes.STRING
////	        };
////	        // List<Column> personViewCols = 
////	        RealMetadataFactory.createElements(viewTable, elemNames, elemTypes);
////
////	        
////	        query = "SELECT t.PersonId, t.PersonName, t.Email, p.PhoneNumber, p.PhoneType FROM Persons, PhoneNumber";
////	        QueryNode phqn = new QueryNode(query);
////	        
////	        // Create Contact group
////	        Table phoneviewTable = RealMetadataFactory.createVirtualGroup("PersonPhone_View", personViewSchema, phqn); //$NON-NLS-1$
////	       
////	        phoneviewTable.setNameInSource("PersonsPhones_View"); //$NON-NLS-1$    
////	        phoneviewTable.setProperty("Supports Query", Boolean.TRUE.toString()); //$NON-NLS-1$
////	        // Create Columns
////	        String[] pvNames = new String[] {
////	            "PersonID", "PersonName", "Email", "PhoneNumber", "PhoneType"  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
////	        };
////	        String[] pvTypes = new String[] {  
////	            DataTypeManager.DefaultDataTypes.INTEGER, DataTypeManager.DefaultDataTypes.STRING, DataTypeManager.DefaultDataTypes.STRING, DataTypeManager.DefaultDataTypes.STRING, DataTypeManager.DefaultDataTypes.STRING
////	        };
////	        
////	        //List<Column> viewCols = 
////	        RealMetadataFactory.createElements(phoneviewTable, pvNames, pvTypes);
//       
//	        return new TransformationMetadata(null, new CompositeMetadataStore(store), null, RealMetadataFactory.SFM.getSystemFunctions(), null);
//	    }    
//
//		public static TranslationUtility TRANSLATION_UTILITY = new TranslationUtility(examplePersons());
//		
//		public static RuntimeMetadata RUNTIME_METADATA = TRANSLATION_UTILITY.createRuntimeMetadata();
//
//	

}
