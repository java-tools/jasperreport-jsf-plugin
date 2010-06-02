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
package net.sf.jasperreports.jsf.engine.source;

import net.sf.jasperreports.jsf.engine.ReportSource;
import net.sf.jasperreports.jsf.engine.ReportSourceFactory;
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
import net.sf.jasperreports.jsf.component.UIReportSource;
import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.engine.ReportSourceException;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.ResourceException;

/**
 *
 * @author aalonsodominguez
 */
public class CsvReportSourceFactory implements ReportSourceFactory {

    private static final Logger logger = Logger.getLogger(
            CsvReportSourceFactory.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    public ReportSource createSource(FacesContext context,
            UIReportSource component) {
        final JRFacesContext jrContext = JRFacesContext.getInstance(context);
        
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
                throw new ReportSourceException(e);
            }
        } else if (value instanceof InputStream) {
            dataSourceStream = (InputStream) value;
            closeStream = false;
        } else if (value instanceof String) {
            try {
                final Resource resource = jrContext.createResource(context,
                        component, (String) value);
                dataSourceStream = resource.getInputStream();
            } catch (final ResourceException e) {
                throw e;
            } catch (final IOException e) {
                throw new ReportSourceException(e);
            }
        } else if (value instanceof File) {
            try {
                dataSource = new JRCsvDataSource((File) value);
            } catch (final FileNotFoundException e) {
                throw new ReportSourceException(e);
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

        return new CsvReportSource(clientId, dataSource,
                (closeStream ? dataSourceStream : null));
    }

    private class CsvReportSource extends JRDataSourceHolder {

        private String clientId;
        private InputStream stream;

        public CsvReportSource(String clientId, JRDataSource dataSource,
                InputStream stream) {
            super(dataSource);
            this.clientId = clientId;
            this.stream = stream;
        }

        public void dispose() throws Exception {
            if (stream != null) {
                stream.close();
                stream = null;
            }
            super.dispose();
        }

    }

}