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
package net.sf.jasperreports.jsf;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.util.ExternalContextHelper;

/**
 * The listener interface for receiving reportPhase events. The class that is
 * interested in processing a reportPhase event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addReportPhaseListener</code> method. When the reportPhase
 * event occurs, that object's appropriate method is invoked.
 * 
 * @see ReportPhaseEvent
 */
public final class ReportPhaseListener implements PhaseListener {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -124696216613450702L;

    /** The Constant BASE_URI. */
    public static final String BASE_URI = "/" +
            ReportPhaseListener.class.getPackage().getName();

    /** The Constant PARAM_CLIENTID. */
    public static final String PARAM_CLIENTID = "clientId";

    /** The Constant REPORT_COMPONENT_KEY_PREFIX. */
    public static final String REPORT_COMPONENT_KEY_PREFIX =
            UIReport.COMPONENT_FAMILY + "/";
    
    /** The logger. */
    private static final Logger logger = Logger.getLogger(
            ReportPhaseListener.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
     */
    public void afterPhase(final PhaseEvent event) { }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
     */
    public void beforePhase(final PhaseEvent event) throws FacesException {
        final FacesContext context = event.getFacesContext();
        final ExternalContextHelper helper = ExternalContextHelper
                .getInstance(context.getExternalContext());

        final String uri = helper.getRequestURI(context.getExternalContext());
        if (uri != null && uri.indexOf(BASE_URI) > -1) {
            try {
                final ExternalContext extContext = context.getExternalContext();

                final String clientId = context.getExternalContext()
                        .getRequestParameterMap().get(PARAM_CLIENTID);
                if (clientId == null) {
                    throw new MalformedReportURLException("Missed parameter: "
                            + PARAM_CLIENTID);
                }

                final UIReport report = (UIReport) extContext.getSessionMap()
                        .get(REPORT_COMPONENT_KEY_PREFIX + clientId);
                if (report == null) {
                    throw new UnregisteredUIReportException(clientId);
                }

                logger.log(Level.FINER, "JRJSF_0016", clientId);
                try {
                    report.encodeContent(context);
                    report.encodeHeaders(context);
                } catch (final IOException e) {
                    throw new JRFacesException(e);
                } finally {
                    logger.log(Level.FINER, "JRJSF_0017", clientId);
                }
            } finally {
                context.responseComplete();
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.event.PhaseListener#getPhaseId()
     */
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

}
