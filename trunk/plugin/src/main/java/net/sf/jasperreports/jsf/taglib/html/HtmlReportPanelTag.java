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

import net.sf.jasperreports.jsf.component.html.HtmlReportPanel;
import net.sf.jasperreports.jsf.renderkit.html.PanelRenderer;
import net.sf.jasperreports.jsf.taglib.AbstractReportTag;

/**
 * The Class ReportTag.
 */
public class HtmlReportPanelTag extends AbstractReportTag {

    /** The frameborder. */
    private ValueExpression frameborder;
    /** The marginheight. */
    private ValueExpression marginheight;
    /** The marginwidth. */
    private ValueExpression marginwidth;
    /** The height. */
    private ValueExpression height;
    /** The width. */
    private ValueExpression width;

    /**
     * Sets the frameborder.
     *
     * @param frameborder
     *            el frameborder a establecer
     */
    public void setFrameborder(final ValueExpression frameborder) {
        this.frameborder = frameborder;
    }

    /**
     * Sets the height.
     *
     * @param height
     *            el height a establecer
     */
    public void setHeight(final ValueExpression height) {
        this.height = height;
    }

    /**
     * Sets the marginheight.
     *
     * @param marginheight
     *            el marginheight a establecer
     */
    public void setMarginheight(final ValueExpression marginheight) {
        this.marginheight = marginheight;
    }

    /**
     * Sets the marginwidth.
     *
     * @param marginwidth
     *            el marginwidth a establecer
     */
    public void setMarginwidth(final ValueExpression marginwidth) {
        this.marginwidth = marginwidth;
    }

    /**
     * Sets the width.
     *
     * @param width
     *            el width a establecer
     */
    public void setWidth(final ValueExpression width) {
        this.width = width;
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
        frameborder = null;
        marginheight = null;
        marginwidth = null;
        height = null;
        width = null;
    }

    // UIComponentELTag

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.webapp.UIComponentTagBase#getComponentType()
     */
    @Override
    public String getComponentType() {
        return HtmlReportPanel.COMPONENT_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.webapp.UIComponentTagBase#getRendererType()
     */
    @Override
    public String getRendererType() {
        return PanelRenderer.RENDERER_TYPE;
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
        final HtmlReportPanel report = (HtmlReportPanel) component;

        if (frameborder != null) {
            if (frameborder.isLiteralText()) {
                report.setFrameborder(Boolean.parseBoolean(frameborder.getExpressionString()));
            } else {
                report.setValueExpression("frameborder", frameborder);
            }
        }

        if (marginheight != null) {
            if (marginheight.isLiteralText()) {
                report.setMarginheight(marginheight.getExpressionString());
            } else {
                report.setValueExpression("marginheight", marginheight);
            }
        }

        if (marginwidth != null) {
            if (marginwidth.isLiteralText()) {
                report.setMarginwidth(marginwidth.getExpressionString());
            } else {
                report.setValueExpression("marginwidth", marginwidth);
            }
        }

        if (height != null) {
            if (height.isLiteralText()) {
                report.setHeight(height.getExpressionString());
            } else {
                report.setValueExpression("height", height);
            }
        }

        if (width != null) {
            if (width.isLiteralText()) {
                report.setWidth(width.getExpressionString());
            } else {
                report.setValueExpression("width", width);
            }
        }
    }
}
