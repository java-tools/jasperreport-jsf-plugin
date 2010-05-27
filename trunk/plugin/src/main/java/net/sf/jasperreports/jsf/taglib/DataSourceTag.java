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
package net.sf.jasperreports.jsf.taglib;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;

import net.sf.jasperreports.jsf.component.UIDataBroker;

/**
 * The Class DataSourceTag.
 */
public class DataSourceTag extends UIComponentELTag {

    private ValueExpression data;
    /** The query. */
    private ValueExpression query;
    /** The type. */
    private ValueExpression type;
    /** The value. */
    private ValueExpression value;

    public void setData(ValueExpression data) {
        this.data = data;
    }

    /**
     * Sets the query.
     *
     * @param query
     *            el query a establecer
     */
    public void setQuery(final ValueExpression query) {
        this.query = query;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            el type a establecer
     */
    public void setType(final ValueExpression type) {
        this.type = type;
    }

    /**
     * Sets the value.
     *
     * @param value
     *            el value a establecer
     */
    public void setValue(final ValueExpression value) {
        this.value = value;
    }

    // TagSupport

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.webapp.UIComponentELTag#release()
     */
    @Override
    public void release() {
        super.release();
        data = null;
        query = null;
        type = null;
        value = null;
    }

    // UIComponentELTag

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.webapp.UIComponentTagBase#getComponentType()
     */
    @Override
    public String getComponentType() {
        return UIDataBroker.COMPONENT_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.webapp.UIComponentTagBase#getRendererType()
     */
    @Override
    public String getRendererType() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.webapp.UIComponentELTag#setProperties(javax.faces.component
     * .UIComponent)
     */
    @Override
    protected void setProperties(final UIComponent component) {
        super.setProperties(component);
        final UIDataBroker dataSource = (UIDataBroker) component;

        if (data != null) {
            if (data.isLiteralText()) {
                dataSource.setData(query.getExpressionString());
            } else {
                dataSource.setValueExpression("data", data);
            }
        }

        if (query != null) {
            if (query.isLiteralText()) {
                dataSource.setQuery(query.getExpressionString());
            } else {
                dataSource.setValueExpression("query", query);
            }
        }

        if (type != null) {
            if (type.isLiteralText()) {
                dataSource.setType(type.getExpressionString());
            } else {
                dataSource.setValueExpression("type", type);
            }
        }

        if (value != null) {
            if (value.isLiteralText()) {
                dataSource.setData(value.getExpressionString());
            } else {
                dataSource.setValueExpression("value", value);
            }
        }
    }
}
