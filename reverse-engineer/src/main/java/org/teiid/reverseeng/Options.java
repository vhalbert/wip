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
package org.teiid.reverseeng;

import java.util.Properties;

import org.teiid.reverseeng.annotation.HibernateAnnotation;
import org.teiid.reverseeng.annotation.ProtobufAnnotation;
import org.teiid.reverseeng.api.AnnotationType;

/**
 * @author vanhalbert
 *
 */
public class Options {
	
	private static final HibernateAnnotation HIBERNATE = new HibernateAnnotation();
	private static final ProtobufAnnotation PROTOBUF = new ProtobufAnnotation();
	
	public interface Parms {
		/* [Optional] The location the java files will be created */
		public static final String BUILD_LOCATION = "build_location";
		/* [Optional] The package name to use for the java files */
		public static final String POJO_PACKAGE_NAME = "pojo_package_name";
		/* [Optional] The pojo jar name  */
		public static final String POJO_JAR_NAME = "pojo_jar_name";

		/* [Optional] Choose either Hiberanate or Protobuf annotations to the java file */
		public static final String ANNOTATION = "annotation";
		
	}
	
	public interface Parms_Defaults {
		public static final String DEFAULT_POJO_PACKAGE_NAME = "org.teiid.pojo";
		public static final String DEFAULT_BUILD_LOCATION = ".";
		public static final String DEFAULT_POJO_JAR_NAME = "pojo.jar";
	}
	
	private Properties properties = new Properties();
	
	public enum Annotation_Type {
		Hibernate,
		Protobuf,
		Unknown		
	}
	
	
	private Annotation_Type annotation_type;
	

	public void setProperty(String name, String value) {	
		if (value == null) return;
		
		properties.setProperty(name, value);
		if (name.equals(Parms.ANNOTATION)) {
			setAnnotationType(Annotation_Type.valueOf(value));
		}
	}
	
	public String getProperty(String property_name) {
		return this.properties.getProperty(property_name);
	}
	
	public void setAnnotationType(Annotation_Type type) {
		this.annotation_type = type;
	}
	
	public Annotation_Type getAnnotationType() {
		return this.annotation_type;
	}
	
	public boolean useHibernateAnnotations() {
		return (annotation_type == Annotation_Type.Hibernate);
	}
	
	
	public boolean useProtobufAnnotations() {
		return (annotation_type == Annotation_Type.Protobuf);
	}
	
	public AnnotationType getAnnotationTypeInstance() {
		if (useHibernateAnnotations()) { 
			return HIBERNATE;
		}
		if (useProtobufAnnotations()) {
			return PROTOBUF;
		}
		
		return null;
	}

}
