/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.engine.databroker;

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
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.component.UIDataBroker;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.ResourceLoader;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author aalonsodominguez
 */
public class XmlDataBrokerFactory extends AbstractDataBrokerFactory {

    private static final Logger logger = Logger.getLogger(
            XmlDataBrokerFactory.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    public DataBroker createDataBroker(FacesContext context,
            UIDataBroker component) {
        JRDataSource dataSource;

        final Document xmlDocument;
        try {
            xmlDocument = getXmlDocument(context, component);
        } catch (final ParserConfigurationException ex) {
            throw new JRFacesException(ex);
        } catch (final SAXException ex) {
            throw new JRFacesException(ex);
        } catch (final IOException ex) {
            throw new JRFacesException(ex);
        }

        if (xmlDocument == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "JRJSF_0020",
                        component.getClientId(context));
            }
            dataSource = new JREmptyDataSource();
        } else {
            final String query = component.getQuery();
            try {
                if ((query != null) && (query.length() > 0)) {
                    dataSource = new JRXmlDataSource(xmlDocument,
                            parseQuery(context, component, query));
                } else {
                    dataSource = new JRXmlDataSource(xmlDocument);
                }
            } catch (final JRException e) {
                throw new JRFacesException(e);
            }
        }

        return new JRDataSourceBroker(dataSource);
    }

    protected Document getXmlDocument(final FacesContext context,
            UIDataBroker component)
            throws ParserConfigurationException, SAXException, IOException {
        final Object data = component.getData();
        if (data == null) {
            return null;
        }

        Document document;
        // First of all detect pre-built doms
        if (data instanceof Document) {
            document = (Document) data;
        } else if (data instanceof DOMSource) {
            final DOMSource source = (DOMSource) data;
            final DocumentBuilder builder = getDocumentBuilder();
            document = builder.newDocument();
            document.appendChild(source.getNode());
        } else if ((data instanceof InputSource)
                || (data instanceof SAXSource)) {
            InputSource inputSource;
            if (data instanceof SAXSource) {
                final SAXSource source = (SAXSource) data;
                inputSource = source.getInputSource();
            } else {
                inputSource = (InputSource) data;
            }
            final DocumentBuilder builder = getDocumentBuilder();
            document = builder.parse(inputSource);
        } else {
            InputStream stream = null;
            boolean closeStream = true;
            if (data instanceof StreamSource) {
                final StreamSource source = (StreamSource) data;
                stream = source.getInputStream();
            } else if (data instanceof File) {
                stream = new FileInputStream((File) data);
            } else if (data instanceof URL) {
                stream = ((URL) data).openStream();
            } else if (data instanceof String) {
                final Resource resource = ResourceLoader.getResource(context,
                        component, (String) data);
                stream = resource.getInputStream();
            } else if (data instanceof InputStream) {
                stream = (InputStream) data;
                closeStream = false;
            }

            if (stream == null) {
                throw new JRFacesException("Unrecognized XML "
                        + "data source type: " + data.getClass());
            }

            try {
                final DocumentBuilder builder = getDocumentBuilder();
                document = builder.parse(stream);
            } finally {
                if (stream != null && closeStream) {
                    try {
                        stream.close();
                    } catch (final IOException e) { }
                }
                stream = null;
            }
        }

        return document;
    }

    private String parseQuery(final FacesContext context,
            UIDataBroker component, final String query) {
        final List<Object> params = new ArrayList<Object>();

        for (final UIComponent kid : component.getChildren()) {
                 if (!(kid instanceof UIParameter)) {
                continue;
            }

            final Object value = ((UIParameter) kid).getValue();
            params.add(value);
        }

        return MessageFormat.format(query,
                params.toArray(new Object[params.size()]));
    }

    private DocumentBuilder getDocumentBuilder()
            throws ParserConfigurationException {
        final DocumentBuilderFactory builderFactory =
                DocumentBuilderFactory.newInstance();
        return builderFactory.newDocumentBuilder();
    }

}
