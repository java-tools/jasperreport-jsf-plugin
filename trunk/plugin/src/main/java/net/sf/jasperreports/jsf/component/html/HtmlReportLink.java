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

import java.io.IOException;

import javax.faces.component.html.HtmlOutputLink;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.component.UIReportImplementor;
import net.sf.jasperreports.jsf.renderkit.html.OutputLinkRenderer;
import net.sf.jasperreports.jsf.renderkit.ReportRenderer;
import net.sf.jasperreports.jsf.spi.ValidatorLoader;
import net.sf.jasperreports.jsf.validation.Validator;

/**
 * The Class HtmlReportLink.
 */
public class HtmlReportLink extends HtmlOutputLink implements UIReport {

    /** The Constant COMPONENT_TYPE. */
    public static final String COMPONENT_TYPE =
            Constants.PACKAGE_PREFIX + ".HtmlReportLink";

    // Report implementor

    /** The impl. */
    private final UIReportImplementor impl;

    /**
     * Instantiates a new html report link.
     */
    public HtmlReportLink() {
        super();
        impl = new UIReportImplementor(this);
        setRendererType(OutputLinkRenderer.RENDERER_TYPE);
    }

    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.jsf.component.UIReport#getDataSource()
     */
    public String getDataSource() {
        return impl.getDataSource();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setDataSource(java.lang.String
     * )
     */
    public void setDataSource(final String dataSource) {
        impl.setDataSource(dataSource);
    }

    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.jsf.component.UIReport#getPath()
     */
    public String getPath() {
        return impl.getPath();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setPath(java.lang.String)
     */
    public void setPath(final String path) {
        impl.setPath(path);
    }

    public String getName() {
        return impl.getName();
    }

    public void setName(final String name) {
        impl.setName(name);
    }

    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.jsf.component.UIReport#getSubreportDir()
     */
    public String getSubreportDir() {
        return impl.getSubreportDir();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setSubreportDir(java.lang
     * .String)
     */
    public void setSubreportDir(final String subreportDir) {
        impl.setSubreportDir(subreportDir);
    }

    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.jsf.component.UIReport#getFormat()
     */
    public String getFormat() {
        return impl.getFormat();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setFormat(java.lang.String)
     */
    public void setFormat(final String type) {
        impl.setFormat(type);
    }

    public boolean isImmediate() {
        return impl.isImmediate();
    }

    public void setImmediate(boolean immediate) {
        impl.setImmediate(immediate);
    }

    @Override
    public String getFamily() {
        return super.getFamily() + "/" + UIReport.COMPONENT_FAMILY;
    }

    // UIReport encode methods
    public void encodeContent(final FacesContext context) throws IOException {
        final ReportRenderer renderer = (ReportRenderer) getRenderer(context);
        renderer.encodeContent(context, this);
    }

    public void encodeHeaders(final FacesContext context) throws IOException {
        final ReportRenderer renderer = (ReportRenderer) getRenderer(context);
        renderer.encodeHeaders(context, this);
    }

    // State saving/restoring methods

    /*
     * (non-Javadoc)
     *
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
     *
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

    @Override
    public void processValidators(FacesContext context) {
        super.processValidators(context);

        if (!isRendered()) {
            return;
        }

        final Validator validator = ValidatorLoader.getValidator(context, this);
        if (validator != null) {
            validator.validate(context, this);
        }
    }
    
}
