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

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.fill.AbstractJRDataSourceFiller;
import net.sf.jasperreports.jsf.fill.FillerException;

/**
 * The Class BeanFiller.
 */
public final class BeanFiller extends AbstractJRDataSourceFiller {

    private static final Logger logger = Logger.getLogger(
            BeanFiller.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    protected BeanFiller(final UIDataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected JRDataSource getJRDataSource(final FacesContext context)
            throws FillerException {
        JRDataSource dataSource;

        final Object value = getDataSourceComponent().getData();
        if (value == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "JRJSF_0020",
                        getDataSourceComponent().getClientId(context));
            }
            dataSource = new JREmptyDataSource();
        } else if (value instanceof Collection<?>) {
            dataSource = new JRBeanCollectionDataSource((Collection<?>) value);
        } else {
            Object[] beanArray;
            if (!value.getClass().isArray()) {
                beanArray = new Object[]{ value };
            } else {
                beanArray = (Object[]) value;
            }
            dataSource = new JRBeanArrayDataSource(beanArray);
        }
        return dataSource;
    }
}
