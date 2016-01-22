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
package org.teiid.reveng.api;

import org.teiid.reveng.util.StringBuilderUtil;


/**
 * @author vanhalbert
 *
 */
public abstract class Column implements Comparable<Column> {
	
	public enum NullType {
		No_Nulls {
			@Override
			public String toString() {
				return "No Nulls"; //$NON-NLS-1$
			}
		},
		Nullable,
		Unknown		
	}
	
	private Table parent;

    private String name;
    
    /**
     * The converted name for camel case
     */
    private String memberName;
    
    /**
     * Defines JDBC type of the column.
     */
    private int type = TypesMapping.NOT_DEFINED;
    
    private String javaType = null;
    
    /**
     * Defines whether the attribute is indexed
     */
    private boolean isIndexed;

    // The length of CHAR or VARCHAr or max num of digits for DECIMAL.
    private int maxLength = -1;

    private int scale = -1;

    private int precision = -1;
    
    private String defaultValue = null;

    private NullType nullType;
    
    private String remarks;    


    public Column(Table parent, String name) {
    	this.parent = parent;
    	this.name = name;
    	memberName = org.teiid.reveng.util.Util.columnNameToMemberName(this.name);
    }
    
    public Table getParent() {
    	return parent;
    }
    
    public String getName() {
        return name;
    }
    
    public String getMemberName() {
    	return memberName;
    }
    
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        
        this.javaType = TypesMapping.getJavaBySqlType(type);
    }   
    
    public String getJavaType() {
    	return this.javaType;
    }
    
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isIndexed() {
        return isIndexed;
    }

    public void setIsIndexed(boolean indexed) {
    	this.isIndexed = indexed;
    }

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}
    
    public boolean isMandatory() {
        return (nullType == NullType.No_Nulls ? true : false);
    }

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public NullType getNullType() {
		return nullType;
	}


	public void setNullType(NullType nullType) {
		this.nullType = nullType;
	}

    public int getMaxLength() {
        return maxLength;
    }

    /**
     * Sets the length of character or binary type or max num of digits for
     * DECIMAL.
     * @param maxLength 
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
    
    @Override
	public String toString() {
    	StringBuilderUtil sbu = new StringBuilderUtil(this).append("name", getName());
        String type = TypesMapping.getSqlNameByType(getType());

        sbu.append("DBColumn", type != null ? type : "type(" + getType() + ")");
       
        if (scale > -1 || precision > -1) {
        	 sbu.append("scale", "[" + scale + ", " + precision + "]");
        }

        if (maxLength > -1) {
        	sbu.append("maxLength", "[" + maxLength + "]");
        }

        if (isIndexed) {
        	sbu.append("isIndexed", "[" + isIndexed + "]");
            
        } else if (isMandatory()) {
        	sbu.append("Mandatory", "[" + isMandatory() + "]");
        }

        return sbu.toString();
    }   
    
    

    
//	public abstract String getAliasedName();
    
    
	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		Column c = (Column) obj;
		if (this.getParent() == null || c.getParent() == null) return false;
		
		if (this.getParent() == c.getParent() || 
				this.getParent().getName().equals(c.getParent().getName())) {
			if (this.getName().equals(c.getName()) &&
				this.getType() == c.getType() &&
				this.getPrecision() == c.getPrecision() &&
				this.getScale() == c.getScale() &&
				this.isIndexed() == c.isIndexed() &&
				this.isMandatory() == c.isMandatory() ) {
					return true;
			}
					
		}
		return false;
	}

	/**
	 * @return typeName
	 */
	public abstract String getTypeName();

	/**
	 *
	 * @param o 
	 * @return int
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public abstract int compareTo(Column o);

}