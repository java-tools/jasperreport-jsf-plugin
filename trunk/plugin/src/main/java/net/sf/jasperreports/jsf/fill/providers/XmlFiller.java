/*
 * JaspertReports JSF Plugin Copyright (C) 2010 A. Alonso Dominguez
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
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

    private static final Logger logger = Logger.getLogger(
            BeanFiller.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    protected XmlFiller(final UIDataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected JRDataSource getJRDataSource(final FacesContext context)
            throws FillerException {
        JRDataSource dataSource;

        final Document xmlDocument = getXmlDocument(context);
        if (xmlDocument == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "JRJSF_0020",
                        getDataSourceComponent().getClientId(context));
            }
            dataSource = new JREmptyDataSource();
        } else {
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
        }
        return dataSource;
    }

    private Document getXmlDocument(final FacesContext context)
            throws FillerException {
        final Object value = getDataSourceComponent().getValue();
        if (value == null) {
            return null;
        }

        if (value instanceof Document) {
            return (Document) value;
        }

        InputStream stream = null;
        boolean closeStream = true;
        try {
            if (value instanceof File) {
                stream = new FileInputStream((File) value);
            } else if (value instanceof URL) {
                stream = ((URL) value).openStream();
            } else if (value instanceof String) {
                final Resource resource = ResourceLoader.getResource(context,
                        (String) value);
                stream = resource.getInputStream();
            } else if (value instanceof InputStream) {
                stream = (InputStream) value;
                closeStream = false;
            }
        } catch (final IOException e) {
            throw new FillerException(e);
        }

        if (stream == null) {
            throw new FillerException("Unrecognized XML "
                    + "data source type: " + value.getClass());
        }

        try {
            return parseStream(stream);
        } finally {
            if (stream != null && closeStream) {
                try {
                    stream.close();
                } catch (final IOException e) {
                }
            }
            stream = null;
        }
    }

    private String parseQuery(final String query) {
        final List<Object> params = new ArrayList<Object>();
        for (final UIComponent kid : getDataSourceComponent().getChildren()) {
            if (!(kid instanceof UIParameter)) {
                continue;
            }

            final Object value = ((UIParameter) kid).getValue();
            params.add(value);
        }

        return MessageFormat.format(query, params.toArray(new Object[params.size()]));
    }

    private Document parseStream(final InputStream stream)
            throws FillerException {
        Document result;
        try {
            final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = builderFactory.newDocumentBuilder();
            result = builder.parse(stream);
        } catch (final SAXException e) {
            throw new FillerException(e);
        } catch (final ParserConfigurationException e) {
            throw new FillerException(e);
        } catch (final IOException e) {
            throw new FillerException(e);
        }
        return result;
    }
}
