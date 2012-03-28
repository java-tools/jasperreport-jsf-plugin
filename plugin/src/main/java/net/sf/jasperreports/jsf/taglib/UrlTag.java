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
package net.sf.jasperreports.jsf.taglib;

import net.sf.jasperreports.jsf.component.UIUrl;
import net.sf.jasperreports.jsf.renderkit.UrlRenderer;

import javax.faces.component.UIComponent;

public class UrlTag extends OutputReportTag {

    private String var;

    public void setVar(String var) {
        this.var = var;
    }

    @Override
    public String getComponentType() {
        return UIUrl.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return UrlRenderer.RENDERER_TYPE;
    }

    @Override
    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        UIUrl url = (UIUrl) component;
        if (var != null) {
            url.setVar(var);
        }
    }

    @Override
    public void release() {
        var = null;
        super.release();
    }
}
