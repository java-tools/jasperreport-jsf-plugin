/*
 * JaspertReports JSF Plugin Copyright (C) 2011 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.engine.converters;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.NamingContainer;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.component.UISource;
import net.sf.jasperreports.jsf.convert.ConverterException;
import net.sf.jasperreports.jsf.convert.SourceConverter;
import net.sf.jasperreports.jsf.engine.Source;
import net.sf.jasperreports.jsf.engine.SourceException;
import net.sf.jasperreports.jsf.engine.ConnectionWrapper;
import net.sf.jasperreports.jsf.engine.JRDataSourceWrapper;

/**
 * Base source converter class.
 *
 * @author A. Alonso Dominguez
 */
public class SourceConverterBase implements SourceConverter {

    /**
     *
     */
    private static final long serialVersionUID = -7915218134982872003L;

    private static final Logger logger = Logger.getLogger(
            SourceConverterBase.class.getPackage().getName(),
            Constants.LOG_MESSAGES_BUNDLE);

    protected static final Source NULL_SOURCE =
            new JRDataSourceWrapper(new JREmptyDataSource());

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
            return NULL_SOURCE;
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
                source = resolveSource(context, component, value);
            } catch (SourceException e) {
                throw new ConverterException(e);
            }
        }

        if (source == null) {
            throw new ConverterException("Couldn't convert value '" + value +
                                         "' to a source object for component: "
                                         + component.getClientId(context));
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
            throw new ConverterException("Unrecognized source type: " + source.
                    getClass().getName());
        }
    }

    protected Source createSource(FacesContext context,
            UIComponent component, Object value)
            throws SourceException {
        return NULL_SOURCE;
    }

    private Source resolveSource(FacesContext context, UIComponent component,
            Object value)
            throws SourceException {
        Source result = null;
        if ((value instanceof String) && (component instanceof UIReport)) {
            // Look for a UISource component that may have the string value
            // as component id in the same view container
            UISource source = resolveSourceId(context,
                                              (UIReport) component,
                                              (String) value);
            if (source != null) {
                result = source.getSubmittedSource();
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, "JRJSF_0036", new Object[]{
                                component.getClientId(context),
                                source.getClientId(context)
                            });
                }
            }
        }

        if (result == null) {
            result = createSource(context, component, value);
        }
        return result;
    }

    private UISource resolveSourceId(FacesContext context,
            UIReport report, String sourceId) {
    	// Try to find source component using 'sourceId' as an
    	// absolute ID
    	UIViewRoot viewRoot = context.getViewRoot();
        UISource result = (UISource) viewRoot.findComponent(sourceId);
        if (result == null) {
	        UIComponent component;
	
	        do {
	            component = getNamingContainer(report);
	            String id = component.getClientId(context) + ":" + sourceId;
	            UIComponent found = component.findComponent(id);
	            if ((found != null) && (found instanceof UISource)) {
	                result = (UISource) found;
	                break;
	            }
	        } while (!(component instanceof UIViewRoot));
        }
        return result;
    }

    private UIComponent getNamingContainer(UIComponent child) {
        UIComponent component = child;
        do {
            component = component.getParent();
        } while (!(component instanceof NamingContainer));
        return component;
    }
}
