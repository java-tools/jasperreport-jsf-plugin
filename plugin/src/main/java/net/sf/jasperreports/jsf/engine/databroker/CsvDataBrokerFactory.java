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
package net.sf.jasperreports.jsf.engine.databroker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.component.UIDataBroker;
import net.sf.jasperreports.jsf.engine.databroker.DataBroker;
import net.sf.jasperreports.jsf.engine.databroker.JRDataSourceBroker;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.ResourceException;
import net.sf.jasperreports.jsf.resource.ResourceLoader;

/**
 *
 * @author aalonsodominguez
 */
public class CsvDataBrokerFactory extends AbstractDataBrokerFactory {

    private static final Logger logger = Logger.getLogger(
            CsvDataBrokerFactory.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    public DataBroker createDataBroker(FacesContext context,
            UIDataBroker component) {
        JRDataSource dataSource = null;
        InputStream dataSourceStream = null;
        boolean closeStream = true;

        String clientId = component.getClientId(context);

        final Object value = component.getData();
        if (value == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "JRJSF_0020", clientId);
            }
            dataSource = new JREmptyDataSource();
        } else if (value instanceof URL) {
            try {
                dataSourceStream = ((URL) value).openStream();
            } catch (final IOException e) {
                throw new JRFacesException(e);
            }
        } else if (value instanceof InputStream) {
            dataSourceStream = (InputStream) value;
            closeStream = false;
        } else if (value instanceof String) {
            try {
                final Resource resource = ResourceLoader.getResource(context,
                        component, (String) value);
                dataSourceStream = resource.getInputStream();
            } catch (final ResourceException e) {
                throw e;
            } catch (final IOException e) {
                throw new JRFacesException(e);
            }
        } else if (value instanceof File) {
            try {
                dataSource = new JRCsvDataSource((File) value);
            } catch (final FileNotFoundException e) {
                throw new JRFacesException(e);
            }
        }

        if (dataSource == null) {
            if (dataSourceStream == null) {
                throw new JRFacesException("CSV datasource needs a valid File, "
                        + "URL, InputStream or string");
            } else {
                dataSource = new JRCsvDataSource(dataSourceStream);
            }
        }

        JRDataSourceBroker broker = new CsvDataSourceBroker(clientId, dataSource,
                (closeStream ? dataSourceStream : null));
        return broker;
    }

    private class CsvDataSourceBroker extends JRDataSourceBroker {

        private String clientId;
        private InputStream stream;

        public CsvDataSourceBroker(String clientId, JRDataSource dataSource,
                InputStream stream) {
            super(dataSource);
            this.clientId = clientId;
            this.stream = stream;
        }

        public void dispose() {
            if (stream == null) return;

            try {
                stream.close();
            } catch(IOException e) {
                // TODO log the exception
            }
        }

    }

}
