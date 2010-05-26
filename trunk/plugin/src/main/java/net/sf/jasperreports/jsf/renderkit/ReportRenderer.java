/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.renderkit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.config.Configuration;
import net.sf.jasperreports.jsf.export.Exporter;
import net.sf.jasperreports.jsf.fill.Filler;
import net.sf.jasperreports.jsf.spi.ExporterLoader;
import net.sf.jasperreports.jsf.spi.FillerLoader;
import net.sf.jasperreports.jsf.util.ExternalContextHelper;
import net.sf.jasperreports.jsf.util.Util;

/**
 *
 * @author aalonsodominguez
 */
public abstract class ReportRenderer extends Renderer {

    private static final Logger logger = Logger.getLogger(
            ReportRenderer.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    public abstract String getContentDisposition();

    public void encodeContent(final FacesContext context, final UIReport component)
            throws IOException {
        if (context == null) {
            throw new IllegalArgumentException("Faces' context is null");
        }
        if (component == null) {
            throw new IllegalArgumentException("Report component is null");
        }

        final String clientId = component.getClientId(context);

        final Filler filler = FillerLoader.getFiller(context, component);
        logger.log(Level.FINE, "JRJSF_0006", clientId);
        final JasperPrint filledReport = filler.fill(context, component);

        final ExternalContextHelper helper = ExternalContextHelper.getInstance(
                context.getExternalContext());
        final Exporter exporter = ExporterLoader.getExporter(context, component);
        final ByteArrayOutputStream reportData = new ByteArrayOutputStream();
        try {
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "JRJSF_0010", new Object[]{clientId,
                            exporter.getContentType()});
            }
            exporter.export(context, filledReport, reportData);
            helper.writeResponse(context.getExternalContext(),
                    exporter.getContentType(), reportData.toByteArray());
        } finally {
            try {
                reportData.close();
            } catch (final IOException e) { }
        }
    }

    public String encodeContentDisposition(
            final UIReport component, final String encoding)
            throws IOException {
        final StringBuffer disposition = new StringBuffer();
        if (component.getName() != null) {
            disposition.append(getContentDisposition());
            disposition.append("; filename=");
            disposition.append(URLEncoder.encode(component.getName(), encoding));
        }
        return disposition.toString();
    }

    public void encodeHeaders(final FacesContext context, final UIReport component)
            throws IOException {
        if (context == null) {
            throw new IllegalArgumentException("Faces' context is null");
        }
        if (component == null) {
            throw new IllegalArgumentException("Report component is null");
        }

        final ExternalContextHelper helper = ExternalContextHelper.getInstance(
                context.getExternalContext());
        helper.writeHeaders(context.getExternalContext(), this, component);
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
    public String encodeReportURL(final FacesContext context,
            final UIComponent component) {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        if (component == null) {
            throw new IllegalArgumentException();
        }

        final StringBuffer reportURI = new StringBuffer(Constants.BASE_URI);

        final Configuration config = Configuration.getInstance(
                context.getExternalContext());
        final ExternalContextHelper helper = ExternalContextHelper.getInstance(
                context.getExternalContext());

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

        reportURI.append('?').append(Constants.PARAM_CLIENTID);
        reportURI.append('=').append(component.getClientId(context));
        reportURI.append('&').append(Constants.PARAM_VIEWID);
        reportURI.append('=').append(helper.getViewId(
                context.getExternalContext()));

        final ViewHandler viewHandler = context.getApplication()
                .getViewHandler();
        final String result = context.getExternalContext().encodeResourceURL(
                viewHandler.getResourceURL(context, reportURI.toString()));
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "JRJSF_0031", result);
        }
        return result;
    }

    protected final void registerReportView(final FacesContext context) {
        if (Boolean.TRUE.equals(context.getExternalContext().getRequestMap()
                .get(Constants.ATTR_REPORT_VIEW))) {
            return;
        }

        final ExternalContextHelper helper = ExternalContextHelper.getInstance(
                context.getExternalContext());
        final String viewId = helper.getViewId(context.getExternalContext());

        context.getExternalContext().getRequestMap().put(
                Constants.ATTR_REPORT_VIEW, Boolean.TRUE);

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "JRJSF_0013", viewId);
        }
    }

}