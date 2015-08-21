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
package org.teiid.embedded.util;

/**
 * @author vanhalbert
 *
 */
public class SampleClass {
	String parentDirectory;
	boolean supportsDirectQueryProcedure;
	
	
	/**
	 * @return supportsDirectQueryProcedure
	 */
	public boolean isSupportsDirectQueryProcedure() {
		return supportsDirectQueryProcedure;
	}
	/**
	 * @param supportsDirectQueryProcedure Sets supportsDirectQueryProcedure to the specified value.
	 */
	public void setSupportsDirectQueryProcedure(boolean supportsDirectQueryProcedure) {
		this.supportsDirectQueryProcedure = supportsDirectQueryProcedure;
	}
	public String getParentDirectory() {
		return this.parentDirectory;
	}
	public void setParentDirectory(String directory) {
		this.parentDirectory = directory;
	}
}
