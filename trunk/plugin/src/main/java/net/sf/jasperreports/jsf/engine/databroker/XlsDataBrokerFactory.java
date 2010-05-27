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
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import jxl.Workbook;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRXlsDataSource;
import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.component.UIDataBroker;
import net.sf.jasperreports.jsf.engine.databroker.DataBroker;
import net.sf.jasperreports.jsf.engine.databroker.JRDataSourceBroker;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.ResourceLoader;

/**
 *
 * @author aalonsodominguez
 */
public class XlsDataBrokerFactory extends AbstractDataBrokerFactory {

    /** The logger instance. */
    private static final Logger logger = Logger.getLogger(
            XlsDataBrokerFactory.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    public DataBroker createDataBroker(FacesContext context,
            UIDataBroker component) {
        JRDataSource dataSource;
        final Object value = component.getData();
        if (value == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "JRJSF_0020",
                        component.getClientId(context));
            }
            dataSource = new JREmptyDataSource();
        } else {
            try {
                if (value instanceof File) {
                    dataSource = new JRXlsDataSource((File) value);
                } else if (value instanceof InputStream) {
                    dataSource = new JRXlsDataSource((InputStream) value);
                } else if (value instanceof Workbook) {
                    dataSource = new JRXlsDataSource((Workbook) value);
                } else if (value instanceof String) {
                    Resource resource = ResourceLoader.getResource(context,
                            component, (String) value);
                    dataSource = new JRXlsDataSource(resource.getInputStream());
                } else {
                    throw new JRFacesException("Unrecognized value type: " +
                            value.getClass().getName());
                }
            } catch (JRException e) {
                throw new JRFacesException(e);
            } catch (IOException e) {
                throw new JRFacesException(e);
            }
        }
        return new JRDataSourceBroker(dataSource);
    }

}
