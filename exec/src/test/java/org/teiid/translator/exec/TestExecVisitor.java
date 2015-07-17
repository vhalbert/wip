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
package org.teiid.translator.exec;

//import static org.junit.  Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.teiid.language.Select;
import org.teiid.translator.exec.util.VDBUtility;

/**
 *
 */
public class TestExecVisitor {

	
	@Test public void testQuery1() throws Exception {						
		Select command = (Select)VDBUtility.TRANSLATION_UTILITY.parseCommand("select results From ExecTable where arguments = 'ls' "); //$NON-NLS-1$
		
		List<String> r =  ExecVisitor.getWhereClauseCommands(command.getWhere());
		
		assertNotNull(r);
		assertTrue(!r.isEmpty());
		
		String one = r.get(0);
		
		assertEquals("Not equal", "ls", one);
		
	}	
	
	@Test public void testQuery2() throws Exception {						
		Select command = (Select)VDBUtility.TRANSLATION_UTILITY.parseCommand("select results From ExecTable where arguments = 'ls' and delimiter = ';'"); //$NON-NLS-1$
		
		List<String> r =  ExecVisitor.getWhereClauseCommands(command.getWhere());
		
		assertNotNull(r);
		assertTrue(!r.isEmpty());
		
		String one = r.get(0);
		
		assertEquals("Not equal", "ls", one);
		
	}		

	@Test public void testQuery3() throws Exception {						
		Select command = (Select)VDBUtility.TRANSLATION_UTILITY.parseCommand("select results From ExecTable where arguments = 'ps -ef'"); //$NON-NLS-1$
		
		List<String> r =  ExecVisitor.getWhereClauseCommands(command.getWhere());
		
		assertNotNull(r);
		assertTrue(!r.isEmpty());
		
		String one = r.get(0);
		
		assertEquals("Not equal", "ps -ef", one);
		
	}	
	
	@Test public void testQuery4() throws Exception {						
		Select command = (Select)VDBUtility.TRANSLATION_UTILITY.parseCommand("select results From ExecTable where arguments = 'ps -ef' and delimiter = ';'"); //$NON-NLS-1$
		
		List<String> r =  ExecVisitor.getWhereClauseCommands(command.getWhere());
		
		assertNotNull(r);
		assertTrue(!r.isEmpty());
		
		String one = r.get(0);
		
		assertEquals("Not equal", "ps -ef", one);
		
	}	
	
	/*
	 * Test the boundary of parsing
	 */
	@Test public void testQuery5() throws Exception {	
		String delimiter = ";";
		String args = "";
		List<String> compareResults = new ArrayList<String>();
		
		int k = 1;
		for (int i = 10; i < 20; i++, k++) {
					
			String p = String.valueOf(i);
			
			args += p + delimiter;
			compareResults.add(p);
			
			Select command = (Select)VDBUtility.TRANSLATION_UTILITY.parseCommand("select results From ExecTable where arguments = '" + args + "' and delimiter = '" + delimiter + "'"); //$NON-NLS-1$
			
			List<String> r =  ExecVisitor.getWhereClauseCommands(command.getWhere());
			
			assertNotNull(r);
			assertTrue(!r.isEmpty());
			
			for (int y=0; y<k; y++) {
				assertEquals(y + " Not equal on " + i, compareResults.get(y), r.get(y));
				
			}

		}
	
	}	
//	
//	@Test public void testQueryGetIn1Trade() throws Exception {						
//		Select command = (Select)VDBUtility.TRANSLATION_UTILITY.parseCommand("select * From Trade_Object.Trade as T where TradeID in ('2', '3')"); //$NON-NLS-1$
//		
//	
//		performTest(command, 2, 4);
//	}		
	
}