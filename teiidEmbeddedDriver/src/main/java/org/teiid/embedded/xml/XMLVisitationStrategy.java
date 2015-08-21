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

package org.teiid.embedded.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.teiid.core.TeiidComponentException;
import org.teiid.embedded.EmbeddedPlugin;
import org.teiid.embedded.TELogger;
import org.teiid.embedded.configuration.BaseConfiguration;
import org.teiid.embedded.configuration.ConnectorConfiguration;
import org.teiid.embedded.configuration.EmbeddedServerConfiguration;
import org.teiid.embedded.configuration.TranslatorConfiguration;




/**
 * <P> This program will parse the XML configuration file that is used
 * to drive the configuring of embedded server, translator, connectors, etc.
 * </P>
 *
 */
@SuppressWarnings("nls")
public class XMLVisitationStrategy {

	private Document embeddedConfigDoc;
	
    public XMLVisitationStrategy() {
    }

    public void init(File xmlFile) throws IOException, JDOMException {
        SAXBuilder builder = SAXBuilderHelper.createSAXBuilder(false);
        embeddedConfigDoc = builder.build(xmlFile);
    }
    
    public EmbeddedServerConfiguration getEmbeddedServerConfiguration() throws TeiidComponentException {
        Element embeddedElement = embeddedConfigDoc.getRootElement().getChild(TagNames.Embedded.ROOT);
        
        EmbeddedServerConfiguration config = new EmbeddedServerConfiguration();
        
        setCommonConfiguration(embeddedElement, config);
          
        return config;
    }
    
    public Map<String,TranslatorConfiguration> getTranslatorConfigurations() throws TeiidComponentException {
    	
    	Map<String,TranslatorConfiguration> translators = new HashMap<String,TranslatorConfiguration>();
        Element translatorsRoot = embeddedConfigDoc.getRootElement().getChild(TagNames.Translators.ROOT);
          
        if (translatorsRoot == null) {
      		throw new RuntimeException("Invalid structured xml file, no " + TagNames.Translators.ROOT + " root element found");
      	  
        }
        List<Element> translatorElements = translatorsRoot.getChildren(TagNames.Translators.TRANSLATOR);
        Iterator iter = translatorElements.iterator();
        while ( iter.hasNext() ) {
            Element transElement = (Element) iter.next();
            TranslatorConfiguration t = new TranslatorConfiguration();
            
            setCommonConfiguration(transElement, t);
           
            String type = transElement.getChildText(TagNames.Translators.TYPE);
            if (type == null || type.trim().length() == 0) {
            	throw new TeiidComponentException(EmbeddedPlugin.Util.getString(
    					"EMBDF10003", t.getName()));
            }

            t.setType(type);
            
            /* 
             * NOTE: allowing for no connector to be defined for a translator
             */
            String connector_name = transElement.getChildText(TagNames.Translators.CONNECTOR_NAME);
//            if (connector_name == null || connector_name.trim().length() == 0) {
//            	throw new TeiidComponentException(EmbeddedPlugin.Util.getString(
//    					"EMBDF10004", t.getName()));
//            }

            if (connector_name != null) {
            	t.setConnectorName(connector_name);
            } 
            
            translators.put(t.getName(), t);
        }
        
        return translators;
    }
    
    public Map<String,ConnectorConfiguration> getConnectorConfigurations() throws TeiidComponentException {
    	
    	Map<String,ConnectorConfiguration> connectors = new HashMap<String,ConnectorConfiguration>();
    	
        Element connectorsRoot = embeddedConfigDoc.getRootElement().getChild(TagNames.Connectors.ROOT);
        
        if (connectorsRoot == null) {
        	TELogger.log("WARNING: No " + TagNames.Connectors.ROOT + " root element found to configure connectors");
      	  	return null;
        }
 
        List<Element> connectorElements = connectorsRoot.getChildren(TagNames.Connectors.CONNECTOR);
        Iterator iter = connectorElements.iterator();
        while ( iter.hasNext() ) {
            Element connectorElement = (Element) iter.next();
            ConnectorConfiguration c = new ConnectorConfiguration();
            
            String jndiName = connectorElement.getChildText(TagNames.Connectors.JNDI_NAME);
            if (jndiName != null) {
            	c.setJndiName(jndiName);
            }
            
            setCommonConfiguration(connectorElement, c);
            
            String type = connectorElement.getChildText(TagNames.Connectors.TYPE);
            if (type == null || type.trim().length() == 0) {
            	throw new TeiidComponentException(EmbeddedPlugin.Util.getString(
    					"EMBDF10003", c.getName()));
            }

            c.setType(type);
            connectors.put(c.getName(), c);
        }
        
        return connectors;
    }  
    
    public List<String> getVDBs() throws TeiidComponentException {
       	List<String> vdbs = new ArrayList<String>();
        Element vdbsRoot = embeddedConfigDoc.getRootElement().getChild(TagNames.VDBs.ROOT);
          
        if (vdbsRoot == null) {
        	return Collections.EMPTY_LIST;
        }
        List<Element> vdbElements = vdbsRoot.getChildren(TagNames.VDBs.VDB);
        Iterator iter = vdbElements.iterator();
        while ( iter.hasNext() ) {
            Element vdbE = (Element) iter.next();
            String name = vdbE.getValue();
            if (name != null && name.trim().length() > 0) {
            	vdbs.add(name);
            }
        }
            
        return vdbs;
    }
            
    private void setCommonConfiguration(Element element, BaseConfiguration bo) throws TeiidComponentException {
    	if (element == null) {
    		throw new RuntimeException("Invalid structured xml file, no element found for " + bo.getClass().getSimpleName());
    	}
    	
    	String name = element.getAttributeValue(TagNames.Attributes.NAME);
        
        if (name == null || name.trim().length() == 0) {
        	throw new TeiidComponentException(EmbeddedPlugin.Util.getString(
					"EMBDF10000", "NULL", bo.getClass().getSimpleName()));
        }
        bo.setName( name );
        
        Properties props = getProperties(element);
        
        if (props != null) {
        	bo.setProperties(props);
        }

    }
            
	private Properties getProperties(Element parent) throws TeiidComponentException{
		Element propertiesElement = parent.getChild(TagNames.Properties.ROOT);
		if (propertiesElement == null) return null;
		
		List<Element> propertyChildren = propertiesElement.getChildren();
		if (propertyChildren == null) {
			return null;
		}
		
		Properties props = new Properties();
		int i = 0;
		final Iterator<Element> iter = propertyChildren.iterator();
		while (iter.hasNext()) {
			final Element propElement = (Element) iter.next();
				String name = propElement.getAttributeValue(TagNames.Attributes.NAME);
				String value = propElement.getValue();
				
				if (name == null) {
			      	throw new TeiidComponentException(EmbeddedPlugin.Util.getString(
							"EMBDF10001", parent.getName()));
					
				}
				if (value == null) {
			      	throw new TeiidComponentException(EmbeddedPlugin.Util.getString(
							"EMBDF10002", name));
		 					
				}
				
				props.setProperty(name, value);
		}

		return props;
	}
 }
