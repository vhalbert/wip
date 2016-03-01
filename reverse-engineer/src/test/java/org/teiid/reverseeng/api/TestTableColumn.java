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
package org.teiid.reverseeng.api;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.teiid.reverseeng.api.Column;
import org.teiid.reverseeng.api.Table;

/**
 * @author vanhalbert
 *
 */
public class TestTableColumn {
	
//	class TColumn extends Column { 
//	     /**
//		 * @param parent 
//	     * @param name
//		 */
//		public TColumn(Table parent, String name) {
//			super(parent, name);
//			
//		}

//		@Override
//		public String getTypeName() {
//			return "NA";
//		}

//		@Override
//		public int compareTo(Column o) {
//			if (this == o) {
//				return 0;
//			} if (this.getName().equals(o.getName())) {
//				return 0;
//			}
//			
//			return -1;
//		}
		
//	}
	
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
	
    @Test
    public void testEquals() throws Exception {
    	Table parenta = new TTable("TableA");
    	
    	Column col1 = parenta.createColumn("col-01");
    	
       	Table parentax = new TTable("TableA");
    			//new TColumn(parent, "col-01");
    	Column col2 = parentax.createColumn("col-01");
    	
    	assertEquals(col1, col2);  	
    	assertEquals(parenta, parentax);
    	
		
    }
	
    @Test
    public void testNotEquals() throws Exception {
    	Table parenta = new TTable("TableA") ;
    	
    	Column col1 = parenta.createColumn("col-01");
    	
       	Table parentax = new TTable("TableA") ;
    			//new TColumn(parent, "col-01");
    	Column col2 = parentax.createColumn("col-02");
    	
     	// test same named parents, but different columns
    	assertNotEquals(col1, col2);   
    	assertNotEquals(parenta, parentax);
    	
       	Table parent2 = new TTable("TableB");
       	Column col1x = parent2.createColumn("col-01");
       	
       	// test same named columns, but different parents
    	assertNotEquals(col1, col1x);   	
    	assertNotEquals(parenta, parent2);   	
		
    }
}
