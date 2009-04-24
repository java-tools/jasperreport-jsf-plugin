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
package net.sf.jasperreports.jsf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.export.Exporter;
import net.sf.jasperreports.jsf.export.ExporterFactory;
import net.sf.jasperreports.jsf.fill.Filler;
import net.sf.jasperreports.jsf.fill.FillerFactory;
import net.sf.jasperreports.jsf.util.Util;

/**
 * The listener interface for receiving reportPhase events. The class that is
 * interested in processing a reportPhase event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addReportPhaseListener</code> method. When the reportPhase
 * event occurs, that object's appropriate method is invoked.
 * 
 * @see ReportPhaseEvent
 */
public class ReportPhaseListener implements PhaseListener {

    // Content-Type map

    /** The Constant CONTENT_TYPE_MAP. */
    public static final Map<String, String> CONTENT_TYPE_MAP;

    // Available formats

    /** The Constant FORMAT_PDF. */
    public static final String FORMAT_PDF = "pdf";

    /** The Constant FORMAT_TEXT. */
    public static final String FORMAT_TEXT = "text";

    /** The Constant FORMAT_HTML. */
    public static final String FORMAT_HTML = "html";

    /** The Constant FORMAT_XML. */
    public static final String FORMAT_XML = "xml";

    /** The Constant FORMAT_RTF. */
    public static final String FORMAT_RTF = "rtf";

    /** The Constant FORMAT_XLS. */
    public static final String FORMAT_XLS = "xls";

    /** The Constant FORMAT_CSV. */
    public static final String FORMAT_CSV = "csv";

    static {
        final Map<String, String> contentTypeMap = new HashMap<String, String>();
        contentTypeMap.put(FORMAT_TEXT, "text/plain");
        contentTypeMap.put(FORMAT_PDF, "application/pdf");
        contentTypeMap.put(FORMAT_HTML, "text/html");
        contentTypeMap.put(FORMAT_XML, "text/xml");
        contentTypeMap.put(FORMAT_RTF, "application/rtf");
        contentTypeMap.put(FORMAT_XLS, "application/vnd.ms-excel");
        contentTypeMap.put(FORMAT_CSV, "text/plain");
        CONTENT_TYPE_MAP = Collections.unmodifiableMap(contentTypeMap);
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -124696216613450702L;

    /** The Constant BASE_URI. */
    public static final String BASE_URI = "/___jasperreportsjsf";

    /** The Constant PARAM_CLIENTID. */
    public static final String PARAM_CLIENTID = "clientId";

    /** The Constant REPORT_COMPONENT_KEY_PREFIX. */
    public static final String REPORT_COMPONENT_KEY_PREFIX = "UIJasperReport-";

    /** The logger. */
    private static final Logger logger = Logger.getLogger(
            ReportPhaseListener.class.getPackage().getName(),
    		"net.sf.jasperreports.jsf.LogMessages");

    /*
     * (non-Javadoc)
     * @see
     * javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
     */
    public void afterPhase(final PhaseEvent event) {}

    /*
     * (non-Javadoc)
     * @see
     * javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
     */
    public void beforePhase(final PhaseEvent event) throws FacesException {
        final FacesContext context = event.getFacesContext();
        final String uri = Util.getRequestURI(context);
        if (uri != null && uri.indexOf(BASE_URI) > -1) {
            final ExternalContext extContext = context.getExternalContext();

            final String clientId = context.getExternalContext()
            .getRequestParameterMap().get(PARAM_CLIENTID);
            if (clientId == null) {
                final Throwable cause = new MalformedReportURLException(
                        "Missed parameter: " + PARAM_CLIENTID);
                throw new JRFacesException(cause);
            }

            final UIReport report = (UIReport) extContext.getSessionMap()
            .remove(REPORT_COMPONENT_KEY_PREFIX + clientId);
            if (report == null) {
                throw new JRFacesException("UIReport component not found: "
                        + clientId);
            }

            String format = report.getFormat();
            if (format == null) {
                format = FORMAT_PDF;
                report.setFormat(format);
            }

            try {
                final String mimeType = CONTENT_TYPE_MAP.get(format);
                if (mimeType == null) {
                    throw new IllegalOutputFormatException(format);
                }

                logger.log(Level.FINE, "JRJSF_0006", clientId);
                final Filler filler = FillerFactory.getFiller(context, report);
                final JasperPrint filledReport = filler.fill(context,
                        report);

                final ByteArrayOutputStream reportData = new ByteArrayOutputStream();
                try {
                    if (logger.isLoggable(Level.FINE)) {
                        logger.log(Level.FINE, "JRJSF_0010", new Object[] {
                                clientId, mimeType
                        });
                    }

                    final Exporter exporter = ExporterFactory.getExporter(
                            context,
                            report);
                    exporter.export(context, filledReport, reportData);
                    Util.writeResponse(context, mimeType, reportData
                            .toByteArray());
                } finally {
                    try {
                        reportData.close();
                    } catch (final IOException e) {
                        // ignore
                    }
                }
            } catch (final IOException e) {
                throw new JRFacesException(e);
            } finally {
                context.responseComplete();
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.faces.event.PhaseListener#getPhaseId()
     */
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

}
