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


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.teiid.language.Condition;
import org.teiid.language.Select;
import org.teiid.logging.LogConstants;
import org.teiid.logging.LogManager;
import org.teiid.translator.DataNotAvailableException;
import org.teiid.translator.ResultSetExecution;
import org.teiid.translator.TranslatorException;


/**
 * Represents the execution of a command.
 */
public class ExecTranslatorExecution implements ResultSetExecution {


    private Select command;
    
    // Execution state
    Iterator<List<String>> results;
    int[] neededColumns;

    /**
     * @param command 
     * 
     */
    
    public ExecTranslatorExecution(Select command) {
        this.command = command;
    }
    
    public void execute() throws TranslatorException {
        // Log our command
        LogManager.logDetail(LogConstants.CTX_CONNECTOR, ExecTranslatorPlugin.UTIL.getString("execute_query", new Object[] { "ExecTranslator", command })); //$NON-NLS-1$

        Condition criterion = command.getWhere();
        
        if (criterion == null) {
        	throw new TranslatorException("ExecExection.Must_have_criteria");
        }
        List<String> commands = ExecVisitor.getWhereClauseCommands(criterion);
        if (commands == null || commands.isEmpty()) {
        	throw new TranslatorException("ExecExecution.Must_have_criteria"); //$NON-NLS-1$
        }
//        String command = ""; //$NON-NLS-1$
//        int i = 2;
//        for (String c : commands) {
//        	command += c.trim();
//        	i++;
//        }
//        Map whereVariables = ExecVisitor.getWhereClauseMap(criterion);
//        if(whereVariables.isEmpty())
//            throw new TranslatorException("ExecExecution.Must_have_criteria"); //$NON-NLS-1$
// 

//        
//        
//        for(Iterator it = whereVariables.keySet().iterator(); it.hasNext();)
//        {
//            String whereKey = (String)it.next();
//            String v = ((String)whereVariables.get(whereKey)).trim();
// //           isValid(v);
//            command += v;
//            i++;
//        }
        try {
            execute(commands);
        }   catch(Exception e)
        {
 //           env.getLogger().logError("Execution Error", e); //$NON-NLS-1$
        //    e.printStackTrace();
            throw new TranslatorException(e.getMessage());
     
        }   
    }

//     private void execute(String command) throws TranslatorException {
//        
//		List<Object> results = new ArrayList<Object>();
//
//		if (criterion == null) {
//			throw new TranslatorException("Criteria is required to execute a command");
//		}
//        // build the system command we want to run
//        List<String> commands = new ArrayList<String>();
//        commands.add("/bin/sh");
//        commands.add("-c");
//        commands.add("ls -l /var/tmp | grep tmp");
//        
//        
//        
//     }
     
     private void execute(List<String> commands) throws TranslatorException {

        // execute the command
        SystemCommandExecutor commandExecutor = new SystemCommandExecutor(commands);
        int result = -1;
		try {
			result = commandExecutor.executeCommand();
		} catch (IOException e) {
		} catch (InterruptedException e) {
		}

        // get the stdout and stderr from the command that was run
		List<List<String>> stdout = commandExecutor.getStandardOutputFromCommand();
		List<List<String>> stderr = commandExecutor.getStandardErrorFromCommand();
        
		logErrors(stderr);
		
		this.results = stdout.iterator();
		
        // print the stdout and stderr
//        System.out.println("The numeric result of the command was: " + result);
//        System.out.println("STDOUT:");
//        System.out.println(stdout);
//        System.out.println("STDERR:");
//        System.out.println(stderr); 
    }    


    public List<?> next() throws TranslatorException, DataNotAvailableException {
        if (results.hasNext()) {
        	List<Object> output = new ArrayList<Object>(1);
        	output.add(results.next());
            return output;
        }
        return null;
    }

//    /**
//     * @param row
//     * @param neededColumns
//     * @return  List
//     */
//    static List<Object> projectRow(List<?> row, int[] neededColumns) {
//        List<Object> output = new ArrayList<Object>(neededColumns.length);
//        
//        for(int i=0; i<neededColumns.length; i++) {
//            output.add(row.get(neededColumns[i]-1));
//        }
//        
//        return output;    
//    }
    
    private void logErrors(List<List<String>> errors) {
    	for (List<String> rows:errors) {
    		for (String row:rows) {
    			LogManager.logError(LogConstants.CTX_CONNECTOR, "<ExecTranslator> Error: " + row);
    		}
    	}
    	
    }

    public void close() {
        LogManager.logDetail(LogConstants.CTX_CONNECTOR, ExecTranslatorPlugin.UTIL.getString("close_query")); //$NON-NLS-1$

    
    }

    public void cancel() throws TranslatorException {
        LogManager.logDetail(LogConstants.CTX_CONNECTOR, ExecTranslatorPlugin.UTIL.getString("cancel_query")); //$NON-NLS-1$
    }
}
