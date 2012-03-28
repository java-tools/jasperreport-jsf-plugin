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
package net.sf.jasperreports.jsf.renderkit;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.util.ComponentUtil;
import net.sf.jasperreports.jsf.util.ReportURI;
import net.sf.jasperreports.jsf.util.ReportURIEncoder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class UrlRenderer extends ReportRenderer {

    /** Renderer content disposition. */
    public static final String CONTENT_DISPOSITION = "attachment";

    /** The renderer type. */
    public static final String RENDERER_TYPE =
            Constants.PACKAGE_PREFIX + ".Url";

    /**
     * Obtains the renderer's content disposition.
     *
     * @return the renderer's content disposition.
     */
    @Override
    public String getContentDisposition() {
        return CONTENT_DISPOSITION;
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (context == null) {
            throw new IllegalArgumentException("'context' can't be null");
        }
        if (component == null) {
            throw new IllegalArgumentException("'component' can't be null");
        }

        String var = ComponentUtil.getStringAttribute(component, "var", null);
        if (var != null && var.length() > 0) {
            ReportURI uri = ReportURIEncoder.encodeReportURI(context, (UIReport) component);
            context.getExternalContext().getRequestMap().put(var, uri);
        }

        registerReportView(context);
    }
}
