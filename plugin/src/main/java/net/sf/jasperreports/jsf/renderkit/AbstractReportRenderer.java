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
package net.sf.jasperreports.jsf.renderkit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.jsf.ReportPhaseListener;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.config.Configuration;
import net.sf.jasperreports.jsf.export.Exporter;
import net.sf.jasperreports.jsf.fill.Filler;
import net.sf.jasperreports.jsf.spi.ExporterLoader;
import net.sf.jasperreports.jsf.spi.FillerLoader;
import net.sf.jasperreports.jsf.util.ExternalContextHelper;
import net.sf.jasperreports.jsf.util.Util;

/**
 * The Class AbstractReportRenderer.
 */
abstract class AbstractReportRenderer extends Renderer implements
        ReportRenderer {

    /** The Constant PASSTHRU_ATTRS. */
    private static final String[] PASSTHRU_ATTRS = {"dir", "lang", "title",
        "style", "datafld", "datasrc", "dataformatas", "ondblclick",
        "onmousedown", "onmousemove", "onmouseout", "onmouseover",
        "onmouseup", "accesskey", "tabindex"};

    private static final Logger logger = Logger.getLogger(
            AbstractReportRenderer.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.render.Renderer#getRendersChildren()
     */
    @Override
    public final boolean getRendersChildren() { // NOPMD by antonio.alonso on
        // 20/10/08 15:41
        return true;
    }

    public void encodeContent(final FacesContext context, final UIReport report)
            throws IOException {
        if (context == null) {
            throw new IllegalArgumentException("Faces' context is null");
        }
        if (report == null) {
            throw new IllegalArgumentException("Report component is null");
        }

        final String clientId = ((UIComponent) report).getClientId(context);

        final Filler filler = FillerLoader.getFiller(context, report);
        logger.log(Level.FINE, "JRJSF_0006", clientId);
        final JasperPrint filledReport = filler.fill(context, report);

        final ExternalContextHelper helper = ExternalContextHelper.getInstance(context);
        final Exporter exporter = ExporterLoader.getExporter(context, report);
        final ByteArrayOutputStream reportData = new ByteArrayOutputStream();
        try {
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "JRJSF_0010", new Object[]{clientId,
                            exporter.getContentType()});
            }
            exporter.export(context, filledReport, reportData);
            helper.writeResponse(context, exporter.getContentType(), reportData.toByteArray());
        } finally {
            try {
                reportData.close();
            } catch (final IOException e) {
                // ignore
            }
        }
    }

    public void encodeHeaders(final FacesContext context, final UIReport report)
            throws IOException {
        if (context == null) {
            throw new IllegalArgumentException("Faces' context is null");
        }
        if (report == null) {
            throw new IllegalArgumentException("Report component is null");
        }

        final ExternalContextHelper helper = ExternalContextHelper.getInstance(context);
        helper.writeHeaders(context, this, report);
    }

    /**
     * Builds the report uri.
     *
     * @param context
     *            the context
     * @param report
     *            the report
     *
     * @return the string
     */
    protected final String buildReportURI(final FacesContext context,
            final UIComponent report) {
        final StringBuffer reportURI = new StringBuffer(
                ReportPhaseListener.BASE_URI);

        final Configuration config = Configuration.getInstance(context);
        String mapping = Util.getInvocationPath(context);
        if (!config.getFacesMappings().contains(mapping)) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "JRJSF_0021", new Object[]{mapping,
                            config.getDefaultMapping()});
            }
            mapping = config.getDefaultMapping();
        }

        if (Util.isPrefixMapped(mapping)) {
            reportURI.insert(0, mapping);
        } else {
            reportURI.append(mapping);
        }

        reportURI.append('?').append(ReportPhaseListener.PARAM_CLIENTID);
        reportURI.append('=').append(report.getClientId(context));
		reportURI.append('&').append(ReportPhaseListener.PARAM_VIEWSTATE);
		reportURI.append('=').append(getViewState(context));

        final ViewHandler viewHandler = context.getApplication().getViewHandler();
        return context.getExternalContext().encodeResourceURL(
                viewHandler.getResourceURL(context, reportURI.toString()));
    }

	protected String getViewState(final FacesContext context) {
		return "";
	}
	
    protected final void registerComponent(final FacesContext context,
            final UIComponent report) {
        final String clientId = report.getClientId(context);
        context.getExternalContext().getSessionMap().put(
                ReportPhaseListener.REPORT_COMPONENT_KEY_PREFIX + clientId,
                report);
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "JRJSF_0013", clientId);
        }
    }

    /**
     * Render id attribute.
     *
     * @param context
     *            the context
     * @param report
     *            the report
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
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
     * @param writer
     *            the writer
     * @param report
     *            the report
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
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
