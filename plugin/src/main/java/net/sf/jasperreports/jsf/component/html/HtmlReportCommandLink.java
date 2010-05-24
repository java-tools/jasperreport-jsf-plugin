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
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.component.UIReportImplementor;
import net.sf.jasperreports.jsf.renderkit.ReportRenderer;
import net.sf.jasperreports.jsf.renderkit.html.CommandLinkRenderer;
import net.sf.jasperreports.jsf.spi.ValidatorLoader;
import net.sf.jasperreports.jsf.validation.Validator;

/**
 *
 * @author aalonsodominguez
 */
public class HtmlReportCommandLink extends HtmlCommandLink implements UIReport {

    public static final String COMPONENT_TYPE = 
            Constants.PACKAGE_PREFIX + ".HtmlReportCommandLink";
    
    private final UIReportImplementor impl;

    public HtmlReportCommandLink() {
        super();
        impl = new UIReportImplementor(this);
        setRendererType(CommandLinkRenderer.RENDERER_TYPE);
    }

    public String getDataSource() {
        return impl.getDataSource();
    }

    public void setDataSource(String dataSource) {
        impl.setDataSource(dataSource);
    }

    public String getName() {
        return impl.getName();
    }

    public void setName(String name) {
        impl.setName(name);
    }

    public String getPath() {
        return impl.getPath();
    }

    public void setPath(String path) {
        impl.setPath(path);
    }

    public String getSubreportDir() {
        return impl.getSubreportDir();
    }

    public void setSubreportDir(String subreportDir) {
        impl.setSubreportDir(subreportDir);
    }

    public String getFormat() {
        return impl.getFormat();
    }

    public void setFormat(String format) {
        impl.setFormat(format);
    }

    @Override
    public String getFamily() {
        return super.getFamily() + "/" + UIReport.COMPONENT_FAMILY;
    }

    public void encodeContent(FacesContext context) throws IOException {
        ReportRenderer renderer = (ReportRenderer) getRenderer(context);
        renderer.encodeContent(context, this);
    }

    public void encodeHeaders(FacesContext context) throws IOException {
        ReportRenderer renderer = (ReportRenderer) getRenderer(context);
        renderer.encodeHeaders(context, this);
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        final Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        impl.restoreState(context, values[1]);
    }

    @Override
    public Object saveState(FacesContext context) {
        Object[] values = new Object[2];
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
