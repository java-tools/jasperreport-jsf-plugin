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
package net.sf.jasperreports.jsf.taglib;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

import net.sf.jasperreports.jsf.component.html.HtmlReportLink;
import net.sf.jasperreports.jsf.renderkit.LinkRenderer;

/**
 * The Class ReportLinkTag.
 */
public class ReportLinkTag extends AbstractReportTag {

    /** The target. */
    private ValueExpression target;

    /**
     * Sets the target.
     * 
     * @param target the new target
     */
    public void setTarget(final ValueExpression target) {
        this.target = target;
    }

    // TagSupport

    /*
     * (non-Javadoc)
     * @see net.sf.jasperreports.jsf.taglib.AbstractReportTag#release()
     */
    @Override
    public void release() {
        super.release();
        target = null;
    }

    // UIComponentELTag

    /*
     * (non-Javadoc)
     * @see javax.faces.webapp.UIComponentTagBase#getComponentType()
     */
    @Override
    public String getComponentType() {
        return HtmlReportLink.COMPONENT_TYPE;
    }

    /*
     * (non-Javadoc)
     * @see javax.faces.webapp.UIComponentTagBase#getRendererType()
     */
    @Override
    public String getRendererType() {
        return LinkRenderer.RENDERER_TYPE;
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.jsf.taglib.AbstractReportTag#setProperties(javax
     * .faces.component.UIComponent)
     */
    @Override
    protected void setProperties(final UIComponent component) {
        super.setProperties(component);
        final HtmlReportLink report = (HtmlReportLink) component;

        if (target != null) {
            if (target.isLiteralText()) {
                report.setTarget(target.getExpressionString());
            } else {
                report.setValueExpression("target", target);
            }
        }
    }

}
