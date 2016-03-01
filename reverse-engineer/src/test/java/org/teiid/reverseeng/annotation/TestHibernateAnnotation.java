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
package org.teiid.reverseeng.annotation;


import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.teiid.reverseeng.annotation.HibernateAnnotation;
import org.teiid.reverseeng.api.AnnotationType;
import org.teiid.reverseeng.api.Column;
import org.teiid.reverseeng.api.Table;

/**
 * @author vanhalbert
 *
 */
public class TestHibernateAnnotation {
	
	private static AnnotationType HA;
	
	class TTable extends Table {

		/**
		 * @param name
		 */
		public TTable(String name) {
			super(name);
			
		}

		@Override
		public Column createColumn(String name) {
			Column col = new Column(this, name);
			this.addColumn(col);
			return col;
		}
	}
	
    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
       	HA = new HibernateAnnotation();      	
   }

   @Test
    public void testTableAnnotation() throws Exception {
    	Table parenta = new TTable("TableA");
    	
    	String tannotation = HA.getClassAnnotation(parenta);
    	
    	assertTrue(tannotation.startsWith(HibernateAnnotation.TABLE_INDEX));

   }
	
   @Test
   public void testIndexedColumnAnnotation() throws Exception {
   	Table parenta = new TTable("TableA");
      	
   	Column col1 = parenta.createColumn("col-01");
   	col1.setIsIndexed(true);
   	String cannotation = HA.getAttributeAnnotation(col1);
   	
   	assertTrue(cannotation.startsWith(HibernateAnnotation.COLUMN_INDEX));
   	assertTrue(cannotation.indexOf(HibernateAnnotation.INDEXED_YES) > 0);
   	
		
   }

   @Test
   public void testColumnAnnotation() throws Exception {
   	Table parenta = new TTable("TableA");
      	
   	Column col1 = parenta.createColumn("col-01");
   	col1.setType(java.sql.Types.DOUBLE);
   	String cannotation = HA.getAttributeAnnotation(col1);
   	
   	assertTrue(cannotation.startsWith(HibernateAnnotation.COLUMN_INDEX));
   	assertTrue(cannotation.indexOf(HibernateAnnotation.INDEXED_YES) > 0);
   	
		
   }
   
   @Test
   public void testNonIndexedAnnotation() throws Exception {
   	Table parenta = new TTable("TableA");
      	
   	Column col1 = parenta.createColumn("col-01");
   	col1.setType(java.sql.Types.BLOB);
   	String cannotation = HA.getAttributeAnnotation(col1);
   	
   	assertTrue(cannotation.startsWith(HibernateAnnotation.COLUMN_INDEX));
   	assertTrue(cannotation.indexOf(HibernateAnnotation.INDEXED_NO) > 0);
   	
		
   }
  
   
}
