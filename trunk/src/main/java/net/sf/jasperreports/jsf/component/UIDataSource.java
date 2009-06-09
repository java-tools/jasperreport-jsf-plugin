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
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.validation.DataSourceValidator;
import net.sf.jasperreports.jsf.validation.DataSourceValidatorFactory;

/**
 * The Class UIDataSource.
 */
public class UIDataSource extends UIComponentBase {

    /** The Constant COMPONENT_FAMILY. */
    public static final String COMPONENT_FAMILY = "net.sf.jasperreports.DataSource";

    /** The Constant COMPONENT_TYPE. */
    public static final String COMPONENT_TYPE = "net.sf.jasperreports.DataSource";

    // Fields

    /** The driver class. */
    private String driverClass;

    /** The query. */
    private String query;

    /** The type. */
    private String type;

    /** The type set. */
    private boolean typeSet;

    /** The value. */
    private Object value;

    /**
     * Instantiates a new uI data source.
     */
    public UIDataSource() {
        super();
        setRendererType(null);
    }

    // Properties

    /**
     * Gets the driver class.
     * 
     * @return the driver class
     */
    public String getDriverClass() {
        if (driverClass != null) {
            return driverClass;
        }
        final ValueExpression ve = getValueExpression("driverClass");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return driverClass;
        }
    }

    /**
     * Sets the driver class.
     * 
     * @param driverClass the new driver class
     */
    public void setDriverClass(final String driverClass) {
        this.driverClass = driverClass;
    }

    /**
     * Gets the query.
     * 
     * @return the query
     */
    public String getQuery() {
        if (query != null) {
            return query;
        }
        final ValueExpression ve = getValueExpression("query");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return query;
        }
    }

    /**
     * Sets the query.
     * 
     * @param query the new query
     */
    public void setQuery(final String query) {
        this.query = query;
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public String getType() {
        if (typeSet) {
            return type;
        }
        final ValueExpression ve = getValueExpression("type");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return type;
        }
    }

    /**
     * Sets the type.
     * 
     * @param type the new type
     */
    public void setType(final String type) {
        this.type = type;
        typeSet = true;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public Object getValue() {
        if (value != null) {
            return value;
        }
        final ValueExpression ve = getValueExpression("value");
        if (ve != null) {
            try {
                return ve.getValue(getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return value;
        }
    }

    /**
     * Sets the value.
     * 
     * @param value the new value
     */
    public void setValue(final Object value) {
        this.value = value;
    }

    // UIComponent

    /*
     * (non-Javadoc)
     * @see javax.faces.component.UIComponent#getFamily()
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /*
     * (non-Javadoc)
     * @see
     * javax.faces.component.UIComponentBase#restoreState(javax.faces.context
     * .FacesContext, java.lang.Object)
     */
    @Override
    public void restoreState(final FacesContext context, final Object state) {
        final Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        driverClass = (String) values[1];
        query = (String) values[2];
        type = (String) values[3];
        typeSet = ((Boolean) values[4]).booleanValue();
        value = values[5];
    }

    /*
     * (non-Javadoc)
     * @seejavax.faces.component.UIComponentBase#saveState(javax.faces.context.
     * FacesContext)
     */
    @Override
    public Object saveState(final FacesContext context) {
        final Object[] values = new Object[6];
        values[0] = super.saveState(context);
        values[1] = driverClass;
        values[2] = query;
        values[3] = type;
        values[4] = Boolean.valueOf(typeSet);
        values[5] = value;
        return values;
    }

}
