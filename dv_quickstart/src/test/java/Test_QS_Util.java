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


import java.io.File;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author vanhalbert
 *
 * NOTE:  Need a Teiid Server Running
 */
public class Test_QS_Util {

	
	@BeforeClass
	public static void initClass() throws Exception {
		try {
			TestServer.createServer(QS_UTIL.DEFAULT_PORT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void shutDown() {
		try {
			TestServer.stopServer();
		} catch (Exception e) {
		}
	}
	
	private void performTest(String[] args, int expected) throws Exception {
		int failure = QS_UTIL.perform(args);
		
		if (failure != expected) {				
			throw new Exception("UNKNOWN Failure: " + failure);
		}
	}
	
	@Test( expected = Exception.class )
	public void testUtil() throws Exception {
		String[] args = new String[] {};
		performTest(args, -1);
			
	}
	
	
	@Test( expected = Exception.class )
	public void testUtilOption1Error() throws Exception {
		String[] args = new String[] {"1"};
		performTest(args, -1);
		
	}
	
	@Test( expected = Exception.class )
	public void testUtilOption1Error2() throws Exception {
		String[] args = new String[] {"1", "eap-installer.xml", ".", "xx"};
		performTest(args, -1);
		
	}
	
	@Test public void testUtilOption1() throws Exception {
		String[] args = new String[] {"1", "./src/test/resources/auto_install.xml.variables", "pwd", "mypassword"};
		performTest(args, 0);
	}
	
	@Test( expected = Exception.class )
	public void testUtilOption2a() throws Exception {
		String[] args = new String[] {"2"};
		performTest(args, -1);
		
	}
	
	@Test public void testUtilOption2b() throws Exception {
		String[] args = new String[] {"2", "localhost"};
		performTest(args, QS_UTIL.HOSTPORT_FOUND);
		
	}
	
	@Test
	public void testUtilOption2c() throws Exception {
		String[] args = new String[] {"2", QS_UTIL.DEFAULT_HOST, String.valueOf(QS_UTIL.DEFAULT_PORT)};
		performTest(args, QS_UTIL.HOSTPORT_FOUND);
		
	}
	
	@Test
	//( expected = Exception.class )
	public void testUtilOption2d() throws Exception {
		String[] args = new String[] {"2", "localhost", "8888"};
		performTest(args, QS_UTIL.HOSTPORT_NOT_FOUND);
		
	}
	
	@Test public void testUtilOption4() throws Exception {
		File target = new File("./target/downloaded_file.pdf");
		
		String[] args = new String[] {"4", "http://stlab.adobe.com/wiki/images/d/d3/Test.pdf", target.getAbsolutePath()};
		performTest(args, 0);
		
		Assert.assertTrue(target.exists());
		Assert.assertTrue(target.length() > 0);
		//if (target.exists())
	}
}
