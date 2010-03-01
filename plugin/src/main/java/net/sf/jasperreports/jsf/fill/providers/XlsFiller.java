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
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.fill.AbstractJRDataSourceFiller;
import net.sf.jasperreports.jsf.fill.FillerException;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.spi.ResourceLoader;

/**
 * Fills the report contents using data coming from a
 * MS Office Excel file.
 * <p>
 * <b>Requires JasperReports 3.7.x</b>
 *
 * @author A. Alonso Dominguez
 */
public final class XlsFiller extends AbstractJRDataSourceFiller {

    /** The logger instance. */
    private static final Logger logger = Logger.getLogger(
            XlsFiller.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    /**
     * Creates a new XlsFiller instance.
     *
     * @param dataSource the data source component
     */
    protected XlsFiller(final UIDataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected JRDataSource getJRDataSource(final FacesContext context)
            throws FillerException {
        JRDataSource dataSource;
        final Object value = getDataSourceComponent().getValue();
        if (value == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "JRJSF_0020",
                        getDataSourceComponent().getClientId(context));
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
                            getDataSourceComponent(), (String) value);
                    dataSource = new JRXlsDataSource(resource.getInputStream());
                } else {
                    throw new FillerException("Unrecognized value type: " +
                            value.getClass().getName());
                }
            } catch (JRException e) {
                throw new FillerException(e);
            } catch (IOException e) {
                throw new FillerException(e);
            }
        }
        return dataSource;
    }

}
