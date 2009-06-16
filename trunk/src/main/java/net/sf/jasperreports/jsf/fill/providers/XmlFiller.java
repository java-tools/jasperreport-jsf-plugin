/*
 * JaspertReports JSF Plugin Copyright (C) 2009 A. Alonso Dominguez
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version. This library is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA A.
 *
 * Alonso Dominguez
 * alonsoft@users.sf.net
 */
package net.sf.jasperreports.jsf.fill.providers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.fill.AbstractJRDataSourceFiller;
import net.sf.jasperreports.jsf.fill.FillerException;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.spi.ResourceLoader;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * The Class XmlFiller.
 */
public final class XmlFiller extends AbstractJRDataSourceFiller {

	protected XmlFiller(final UIDataSource dataSource) {
		super(dataSource);
	}

	@Override
	protected JRDataSource getJRDataSource(FacesContext context)
			throws FillerException {
		JRDataSource dataSource;

		final Document xmlDocument = getXmlDocument(context);
		final String query = getDataSourceComponent().getQuery();
		try {
			if ((query != null) && (query.length() > 0)) {
				dataSource = new JRXmlDataSource(xmlDocument, 
						parseQuery(query));
			} else {
				dataSource = new JRXmlDataSource(xmlDocument);
			}
		} catch (final JRException e) {
			throw new FillerException(e);
		}
		return dataSource;
	}

	private Document getXmlDocument(FacesContext context) 
	throws FillerException {
		Object value = getDataSourceComponent().getValue();
		if(value instanceof Document) {
			return (Document) value;
		}
		
		InputStream stream = null;
		boolean closeStream = true;
		try {
			if(value instanceof File) {
				stream = new FileInputStream((File) value);
			} else if(value instanceof URL) {
				stream = ((URL) value).openStream();
			} else if(value instanceof String) {
				Resource resource = ResourceLoader
						.getResource(context, (String) value);
				stream = resource.getInputStream();
			} else if(value instanceof InputStream) {
				stream = (InputStream) value;
				closeStream = false;
			}
		} catch(IOException e) {
			throw new FillerException(e);
		}
		
		if(stream == null) {
			throw new FillerException("Unrecognized XML "
					+ "data source type: " + value.getClass());
		}
		
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();		
			return builder.parse(stream);
		} catch (SAXException e) {
			throw new FillerException(e);
		} catch (ParserConfigurationException e) {
			throw new FillerException(e);
		} catch (IOException e) {
			throw new FillerException(e);
		} finally {
			if(stream != null && closeStream) {
				try {
					stream.close();
				} catch(IOException e) { }
			}
			stream = null;
		}
	}
	
	private String parseQuery(String query) {
		List<Object> params = new ArrayList<Object>();
		for(UIComponent kid : getDataSourceComponent().getChildren()) {
			if(!(kid instanceof UIParameter)) {
				continue;
			}
			
			Object value = ((UIParameter) kid).getValue();
			params.add(value);
		}
		
		return MessageFormat.format(query, 
				params.toArray(new Object[params.size()]));
	}
	
}
