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
import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.BeforeClass;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.teiid.language.Select;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.exec.util.VDBUtility;

/**
 *
 */
public  class TestLinuxCommands {

	protected static ExecTranslatorExecutionFactory TRANSLATOR;
	
	protected static ExecTranslatorConnection CONN;

	@Mock
	private ExecutionContext context;
	
	@Mock
	private Select command;
	
	@BeforeClass
	public static void setUp() throws TranslatorException {
		
		CONN  = mock(ExecTranslatorConnection.class); 
		
		TRANSLATOR = new ExecTranslatorExecutionFactory();
		TRANSLATOR.start();
	}
	
	@Test public void testQuery() throws Exception {						
		Select command = (Select)VDBUtility.TRANSLATION_UTILITY.parseCommand("select results From ExecTable where arguments = 'ls'"); //$NON-NLS-1$
		
	
		performTest(command, 4, 1);
	}	
	
	protected List<Object> performTest(Select command, int rowcnt, int colCount) throws Exception {

		ExecTranslatorExecution exec = createExecution(command);
		
		return performTest(rowcnt, colCount, exec);
	}

	static List<Object> performTest(int rowcnt, int colCount, ExecTranslatorExecution exec)
			throws TranslatorException {
		exec.execute();
		
		List<Object> rows = new ArrayList<Object>();
		
		int cnt = 0;
		List<?> row = exec.next();
	
		while (row != null) {
			rows.add(row);
			Assert.assertEquals(colCount, row.size());
			System.out.println("Row value: " + row);
			++cnt;
			row = exec.next();
		}
			
		Assert.assertEquals("Did not get expected number of rows", rowcnt, cnt); //$NON-NLS-1$
		
		exec.close();
		return rows;
	}

	protected  ExecTranslatorExecution createExecution(Select command) throws Exception {
		return (ExecTranslatorExecution) TRANSLATOR.createExecution(command, context, null, CONN);
	}
  
}