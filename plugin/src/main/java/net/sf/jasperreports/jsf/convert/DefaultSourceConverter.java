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
package net.sf.jasperreports.jsf.convert;

import java.sql.Connection;
import java.sql.SQLException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.jsf.engine.Source;
import net.sf.jasperreports.jsf.engine.SourceException;
import net.sf.jasperreports.jsf.engine.source.ConnectionWrapper;
import net.sf.jasperreports.jsf.engine.source.JRDataSourceWrapper;

/**
 *
 * @author antonio.alonso
 */
public class DefaultSourceConverter implements SourceConverter {

    public Source convertFromValue(FacesContext context,
            UIComponent component, Object value)
    throws ConverterException {
        if (context == null) {
            throw new IllegalArgumentException("'context'");
        }
        if (component == null) {
            throw new IllegalArgumentException("'context'");
        }

        if (value == null) {
            return null;
        }

        Source source = null;
        if (value instanceof Source) {
            source = (Source) value;
        } else if (value instanceof Connection) {
            source = new ConnectionWrapper((Connection) value);
        } else if (value instanceof DataSource) {
            try {
                source = new ConnectionWrapper(
                        ((DataSource) value).getConnection());
            } catch (SQLException e) {
                throw new ConverterException(e);
            }
        } else if (value instanceof JRDataSource) {
            source = new JRDataSourceWrapper((JRDataSource) value);
        } else {
            try {
                source = createSource(context, component, value);
            } catch (SourceException e) {
                throw new ConverterException(e);
            }
        }

        if (source == null) {
            throw new ConverterException("Couldn't convert value '" +
                    value + "' to a source object for component: " +
                    component.getClientId(context));
        }

        return source;
    }

    public Object convertFromSource(FacesContext context,
            UIComponent component, Source source)
    throws ConverterException {
        if (context == null) {
            throw new IllegalArgumentException("'context'");
        }
        if (component == null) {
            throw new IllegalArgumentException("'context'");
        }

        if (source == null) {
            return null;
        }

        if (source instanceof ConnectionWrapper) {
            return ((ConnectionWrapper) source).getConnection();
        } else if (source instanceof JRDataSourceWrapper) {
            return ((JRDataSourceWrapper) source).getDataSource();
        } else {
            throw new ConverterException("Unrecognized source type: " +
                    source.getClass().getName());
        }
    }

    protected Source createSource(FacesContext context,
            UIComponent component, Object value)
    throws SourceException {
        return null;
    }

}
