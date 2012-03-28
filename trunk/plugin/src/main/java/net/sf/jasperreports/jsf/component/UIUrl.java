/*
 * JaspertReports JSF Plugin Copyright (C) 2012 A. Alonso Dominguez
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

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.renderkit.UrlRenderer;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

public class UIUrl extends UIOutputReport {

    public static final String COMPONENT_TYPE =
            Constants.PACKAGE_PREFIX + ".Url";

    private String var;

    public UIUrl() {
        super();
        setRendererType(UrlRenderer.RENDERER_TYPE);
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    @Override
    public void setValueExpression(String name, ValueExpression ve) {
        if ("var".equals(name)) {
            throw new IllegalArgumentException("'var' can't be a regular expression");
        }
        super.setValueExpression(name, ve);
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        var = (String) values[1];
    }

    @Override
    public Object saveState(FacesContext context) {
        Object[] state = new Object[2];
        state[0] = super.saveState(context);
        state[1] = var;
        return state;
    }
}
