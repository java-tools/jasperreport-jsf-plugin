/*
 * JaspertReports JSF Plugin Copyright (C) 2008 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.component;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * The Class UIReportImplementor.
 */
public final class UIReportImplementor implements UIReport, StateHolder {

    /** The callback. */
    private final UIComponentBase callback;

    /** The data source. */
    private String dataSource;

    /** The path. */
    private String path;

    /** The subreport dir. */
    private String subreportDir;

    /** The format. */
    private String format;

    /**
     * Instantiates a new uI report implementor.
     * 
     * @param callback the callback
     */
    public UIReportImplementor(final UIComponentBase callback) {
        super();
        if (callback == null) {
            throw new IllegalArgumentException();
        }
        if (!(callback instanceof UIReport)) {
        	throw new IllegalArgumentException();
        }
        this.callback = callback;
    }

    /*
     * (non-Javadoc)
     * @see net.sf.jasperreports.jsf.component.UIReport#getDataSource()
     */
    public String getDataSource() {
        if (dataSource != null) {
            return dataSource;
        }
        final ValueExpression ve = getValueExpression("dataSource");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return dataSource;
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setDataSource(java.lang.String
     * )
     */
    public void setDataSource(final String dataSource) {
        this.dataSource = dataSource;
    }

    /*
     * (non-Javadoc)
     * @see net.sf.jasperreports.jsf.component.UIReport#getPath()
     */
    public String getPath() {
        if (path != null) {
            return path;
        }
        final ValueExpression ve = getValueExpression("path");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return path;
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setPath(java.lang.String)
     */
    public void setPath(final String path) {
        this.path = path;
    }

    /*
     * (non-Javadoc)
     * @see net.sf.jasperreports.jsf.component.UIReport#getSubreportDir()
     */
    public String getSubreportDir() {
        if (subreportDir != null) {
            return subreportDir;
        }
        final ValueExpression ve = getValueExpression("subreportDir");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return subreportDir;
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setSubreportDir(java.lang
     * .String)
     */
    public void setSubreportDir(final String subreportDir) {
        this.subreportDir = subreportDir;
    }

    /*
     * (non-Javadoc)
     * @see net.sf.jasperreports.jsf.component.UIReport#getFormat()
     */
    public String getFormat() {
        if (format != null) {
            return format;
        }
        final ValueExpression ve = getValueExpression("format");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return format;
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setFormat(java.lang.String)
     */
    public void setFormat(final String type) {
        format = type;
    }

    /*
     * (non-Javadoc)
     * @see javax.faces.component.StateHolder#isTransient()
     */
    public boolean isTransient() {
        return callback.isTransient();
    }
    
    /*
     * (non-Javadoc)
     * @seejavax.faces.component.StateHolder#restoreState(javax.faces.context.
     * FacesContext, java.lang.Object)
     */
    public void restoreState(final FacesContext context, final Object state) {
        final Object[] values = (Object[]) state;
        dataSource = (String) values[0];
        path = (String) values[1];
        subreportDir = (String) values[2];
        format = (String) values[3];
    }

    /*
     * (non-Javadoc)
     * @see
     * javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext
     * )
     */
    public Object saveState(final FacesContext context) {
        final Object[] values = new Object[4];
        values[0] = dataSource;
        values[1] = path;
        values[2] = subreportDir;
        values[3] = format;
        return values;
    }

    /*
     * (non-Javadoc)
     * @see javax.faces.component.StateHolder#setTransient(boolean)
     */
    public void setTransient(final boolean newTransientValue) {
        callback.setTransient(newTransientValue);
    }

    /**
     * Gets the faces context.
     * 
     * @return the faces context
     */
    private FacesContext getFacesContext() {
        final FacesContext context = FacesContext.getCurrentInstance();
        if (context == null) {
            throw new IllegalStateException();
        }
        return context;
    }

    /**
     * Gets the value expression.
     * 
     * @param key the key
     * 
     * @return the value expression
     */
    private ValueExpression getValueExpression(final String key) {
        return callback.getValueExpression(key);
    }

}
