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

import static org.mockito.Mockito.mock;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.teiid.language.Select;
import org.teiid.metadata.BaseColumn.NullType;
import org.teiid.metadata.Datatype;
import org.teiid.metadata.MetadataFactory;
import org.teiid.metadata.Table;
import org.teiid.query.metadata.SystemMetadata;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.TranslatorException;


@SuppressWarnings("nls")
public class TestExecExecutionFactory {
	
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

//	@Test
	public void testFactory() throws Exception {
			
		ExecTranslatorExecution exec = (ExecTranslatorExecution) TRANSLATOR.createExecution(command, context, null, CONN);
		
		assertNotNull(exec);
	}	


}
