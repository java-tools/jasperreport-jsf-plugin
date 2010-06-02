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
package net.sf.jasperreports.jsf.component.html;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.renderkit.html.OutputLinkRenderer;

/**
 * The Class HtmlReportLink.
 */
public class HtmlReportLink extends UIReport {

    /** The Constant COMPONENT_TYPE. */
    public static final String COMPONENT_TYPE =
            Constants.PACKAGE_PREFIX + ".HtmlReportLink";

    private String target;
    private String style;
    private String styleClass;

    /**
     * Instantiates a new html report link.
     */
    public HtmlReportLink() {
        super();
        setRendererType(OutputLinkRenderer.RENDERER_TYPE);
    }

    public String getTarget() {
        if (target != null) {
            return target;
        }
        ValueExpression ve = getValueExpression("target");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return target;
        }
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getStyle() {
        if (style != null) {
            return style;
        }
        ValueExpression ve = getValueExpression("style");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return style;
        }
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        if (styleClass != null) {
            return styleClass;
        }
        ValueExpression ve = getValueExpression("styleClass");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return styleClass;
        }
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        target = (String) values[1];
        style = (String) values[2];
        styleClass = (String) values[3];
    }

    @Override
    public Object saveState(FacesContext context) {
        Object[] values = new Object[4];
        values[0] = super.saveState(context);
        values[1] = target;
        values[2] = style;
        values[3] = styleClass;
        return values;
    }

}
