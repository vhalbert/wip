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


import org.junit.Test;

/**
 * @author vanhalbert
 *
 * NOTE:  Need a Teiid Server Running
 */
public class Test_QS_Util {
	
	private void performTest(String[] args) throws Exception {
		boolean failure = QS_UTIL.perform(args);
		
		if (failure) {
			throw new Exception("Failure");
		}
	}
	
	@Test( expected = Exception.class )
	public void testUtil() throws Exception {
		String[] args = new String[] {};
		performTest(args);
			
	}
	
	
	@Test( expected = Exception.class )
	public void testUtilOption1Error() throws Exception {
		String[] args = new String[] {"1"};
		performTest(args);
		
	}
	
	@Test( expected = Exception.class )
	public void testUtilOption1Error2() throws Exception {
		String[] args = new String[] {"1", "eap-installer.xml", ".", "xx"};
		performTest(args);
		
	}
	
	@Test public void testUtilOption1() throws Exception {
		String[] args = new String[] {"1", "eap-installer.xml", "."};
		performTest(args);
	}
	
	@Test public void testUtilOption2a() throws Exception {
		String[] args = new String[] {"2"};
		performTest(args);
		
	}
	
	@Test public void testUtilOption2b() throws Exception {
		String[] args = new String[] {"2", "localhost"};
		performTest(args);
		
	}
	
	@Test( expected = Exception.class )
	public void testUtilOption2c() throws Exception {
		String[] args = new String[] {"2", "localhost", "8888"};
		performTest(args);
		
	}
	
}
