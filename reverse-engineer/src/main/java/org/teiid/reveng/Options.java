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
package org.teiid.reveng;

import org.teiid.reveng.annotation.HibernateAnnotation;
import org.teiid.reveng.annotation.ProtobufAnnotation;
import org.teiid.reveng.api.AnnotationType;

/**
 * @author vanhalbert
 *
 */
public class Options {
	
	private static final HibernateAnnotation HIBERNATE = new HibernateAnnotation();
	private static final ProtobufAnnotation PROTOBUF = new ProtobufAnnotation();
	
	public enum Annotation_Type {
		Hibernate,
		Protobuf,
		Unknown		
	}
	
	
	private Annotation_Type annotation_type;
	
	
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
