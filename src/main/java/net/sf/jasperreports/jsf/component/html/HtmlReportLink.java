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
package net.sf.jasperreports.jsf.component.html;

import javax.el.ValueExpression;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.component.UIReportImplementor;
import net.sf.jasperreports.jsf.renderkit.LinkRenderer;

// TODO: Auto-generated Javadoc
/**
 * The Class HtmlReportLink.
 */
public class HtmlReportLink extends HtmlOutputLink implements UIReport {

    /** The Constant COMPONENT_TYPE. */
    public static final String COMPONENT_TYPE = "net.sf.jasperreports.HtmlReportLink";

    // Report implementor

    /** The impl. */
    private final UIReport impl;

    /**
     * Instantiates a new html report link.
     */
    public HtmlReportLink() {
        super();
        impl = new UIReportImplementor(
                new UIReportImplementor.ComponentCallback() {

                    public FacesContext getFacesContext() {
                        return HtmlReportLink.this.getFacesContext();
                    }

                    public ValueExpression getValueExpression(final String key) {
                        return HtmlReportLink.this.getValueExpression(key);
                    }

                    public boolean isTransient() {
                        return HtmlReportLink.this.isTransient();
                    }

                    public void setTransient(final boolean transientFlag) {
                        HtmlReportLink.this.setTransient(transientFlag);
                    }

                });
        setRendererType(LinkRenderer.RENDERER_TYPE);
    }

    /*
     * (non-Javadoc)
     * @see net.sf.jasperreports.jsf.component.UIReport#getDataSource()
     */
    public String getDataSource() {
        return impl.getDataSource();
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setDataSource(java.lang.String
     * )
     */
    public void setDataSource(final String dataSource) {
        impl.setDataSource(dataSource);
    }

    /*
     * (non-Javadoc)
     * @see net.sf.jasperreports.jsf.component.UIReport#getPath()
     */
    public String getPath() {
        return impl.getPath();
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setPath(java.lang.String)
     */
    public void setPath(final String path) {
        impl.setPath(path);
    }

    /*
     * (non-Javadoc)
     * @see net.sf.jasperreports.jsf.component.UIReport#getSubreportDir()
     */
    public String getSubreportDir() {
        return impl.getSubreportDir();
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setSubreportDir(java.lang
     * .String)
     */
    public void setSubreportDir(final String subreportDir) {
        impl.setSubreportDir(subreportDir);
    }

    /*
     * (non-Javadoc)
     * @see net.sf.jasperreports.jsf.component.UIReport#getFormat()
     */
    public String getFormat() {
        return impl.getFormat();
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setFormat(java.lang.String)
     */
    public void setFormat(final String type) {
        impl.setFormat(type);
    }

    /*
     * (non-Javadoc)
     * @see javax.faces.component.UIOutput#getFamily()
     */
    @Override
    public String getFamily() {
        return UIReport.COMPONENT_FAMILY;
    }

    // State saving/restoring methods

    /*
     * (non-Javadoc)
     * @see
     * javax.faces.component.html.HtmlOutputLink#restoreState(javax.faces.context
     * .FacesContext, java.lang.Object)
     */
    @Override
    public void restoreState(final FacesContext context, final Object state) {
        final Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        impl.restoreState(context, values[1]);
    }

    /*
     * (non-Javadoc)
     * @see
     * javax.faces.component.html.HtmlOutputLink#saveState(javax.faces.context
     * .FacesContext)
     */
    @Override
    public Object saveState(final FacesContext context) {
        final Object[] values = new Object[2];
        values[0] = super.saveState(context);
        values[1] = impl.saveState(context);
        return values;
    }

}
