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
package net.sf.jasperreports.jsf.taglib.html;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

import net.sf.jasperreports.jsf.component.html.HtmlReportLink;
import net.sf.jasperreports.jsf.renderkit.html.OutputLinkRenderer;
import net.sf.jasperreports.jsf.taglib.AbstractReportTag;

import static net.sf.jasperreports.jsf.util.ComponentUtil.*;

/**
 * The Class ReportLinkTag.
 */
public class HtmlReportLinkTag extends AbstractReportTag {

    /** The target. */
    private ValueExpression target;
    private ValueExpression style;
    private ValueExpression styleClass;

    /**
     * Sets the target.
     *
     * @param target
     *            the new target
     */
    public void setTarget(final ValueExpression target) {
        this.target = target;
    }

    public void setStyle(ValueExpression style) {
        this.style = style;
    }

    public void setStyleClass(ValueExpression styleClass) {
        this.styleClass = styleClass;
    }

    // TagSupport

    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.jsf.taglib.AbstractReportTag#release()
     */
    @Override
    public void release() {
        super.release();
        target = null;
        style = null;
        styleClass = null;
    }

    // UIComponentELTag

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.webapp.UIComponentTagBase#getComponentType()
     */
    @Override
    public String getComponentType() {
        return HtmlReportLink.COMPONENT_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.webapp.UIComponentTagBase#getRendererType()
     */
    @Override
    public String getRendererType() {
        return OutputLinkRenderer.RENDERER_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.jsf.taglib.AbstractReportTag#setProperties(javax
     * .faces.component.UIComponent)
     */
    @Override
    protected void setProperties(final UIComponent component) {
        super.setProperties(component);

        setStringAttribute(component, "target", target);
        setStringAttribute(component, "style", style);
        setStringAttribute(component, "styleClass", styleClass);
    }
}
