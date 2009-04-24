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
package net.sf.jasperreports.jsf.renderkit;

import java.io.IOException;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import net.sf.jasperreports.jsf.ReportPhaseListener;
import net.sf.jasperreports.jsf.util.Util;

/**
 * The Class AbstractReportRenderer.
 */
abstract class AbstractReportRenderer extends Renderer {

    /** The Constant PASSTHRU_ATTRS. */
    private static final String[] PASSTHRU_ATTRS = {
            "dir", "lang", "title", "style", "datafld", "datasrc",
            "dataformatas", "ondblclick", "onmousedown", "onmousemove",
            "onmouseout", "onmouseover", "onmouseup", "accesskey", "tabindex"
    };

    /*
     * (non-Javadoc)
     * @see javax.faces.render.Renderer#getRendersChildren()
     */
    @Override
    public final boolean getRendersChildren() { // NOPMD by antonio.alonso on
                                                // 20/10/08 15:41
        return true;
    }

    /**
     * Builds the report uri.
     * 
     * @param context the context
     * @param report the report
     * 
     * @return the string
     */
    protected final String buildReportURI(final FacesContext context,
            final UIComponent report) {
        final StringBuffer reportURI = new StringBuffer(
                ReportPhaseListener.BASE_URI);

        final String mapping = Util.getFacesMapping(context);
        if (Util.isPrefixMapped(mapping)) {
            reportURI.insert(0, mapping);
        } else {
            reportURI.append(mapping);
        }

        reportURI.append('?').append(ReportPhaseListener.PARAM_CLIENTID);
        reportURI.append('=').append(report.getClientId(context));

        final ViewHandler viewHandler = context.getApplication()
                .getViewHandler();
        return context.getExternalContext().encodeResourceURL(
                viewHandler.getResourceURL(context, reportURI.toString()));
    }

    /**
     * Render id attribute.
     * 
     * @param context the context
     * @param report the report
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected final void renderIdAttribute(final FacesContext context,
            final UIComponent report) throws IOException {
        final String id = report.getId();
        if ((id != null) && !id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX)) {
            final ResponseWriter writer = context.getResponseWriter();
            writer.writeAttribute("id", report.getClientId(context), "id");
        }
    }

    /**
     * Render attributes.
     * 
     * @param writer the writer
     * @param report the report
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected void renderAttributes(final ResponseWriter writer,
            final UIComponent report) throws IOException {
        final String styleClass = (String) report.getAttributes().get(
                "styleClass");
        if (styleClass != null) {
            writer.writeAttribute("class", styleClass, null);
        }

        for (final String attrName : PASSTHRU_ATTRS) {
            final Object value = report.getAttributes().get(attrName);
            if (value != null) {
                writer.writeAttribute(attrName, value, null);
            }
        }
    }

}
