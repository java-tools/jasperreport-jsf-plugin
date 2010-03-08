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
package net.sf.jasperreports.jsf.lifecycle;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.util.ExternalContextHelper;

/**
 * The listener interface for receiving reportPhase events. The class that is
 * interested in processing a reportPhase event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addReportPhaseListener</code> method. When the reportPhase
 * event occurs, that object's appropriate method is invoked.
 *
 * @see PhaseListener
 * @author A. Alonso Dominguez
 */
public final class ReportLifecycle implements PhaseListener {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -124696216613450702L;

    /** The logger instance. */
    private static final Logger logger = Logger.getLogger(
            ReportLifecycle.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    private static final Map<PhaseId, ReportPhase> LIFECYCLE_MAP;

    static {
        Map<PhaseId, ReportPhase> lifecycleMap =
                new HashMap<PhaseId, ReportPhase>();
        lifecycleMap.put(PhaseId.RESTORE_VIEW, new RestoreViewReportPhase());
        lifecycleMap.put(PhaseId.RENDER_RESPONSE,
                new RenderResponseReportPhase());
        LIFECYCLE_MAP = Collections.unmodifiableMap(lifecycleMap);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
     */
    public void afterPhase(final PhaseEvent event) { 
        if (!isReportRequest(event.getFacesContext())) {
            return;
        }

        ReportPhase phase = LIFECYCLE_MAP.get(event.getPhaseId());
        if (phase == null) {
            throw new ReportLifecycleException("Improper lifecycle phase '"
                    + event.getPhaseId() + "' for report rendering.");
        }
        phase.doAfterPhase(event.getFacesContext());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
     */
    public void beforePhase(final PhaseEvent event) throws FacesException {
        if (!isReportRequest(event.getFacesContext())) {
            return;
        }

        ReportPhase phase = LIFECYCLE_MAP.get(event.getPhaseId());
        if (phase == null) {
            throw new ReportLifecycleException("Improper lifecycle phase '"
                    + event.getPhaseId() + "' for report rendering.");
        }
        phase.doBeforePhase(event.getFacesContext());
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.event.PhaseListener#getPhaseId()
     */
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    private boolean isReportRequest(final FacesContext context) {
        final ExternalContextHelper helper = ExternalContextHelper
                .getInstance(context.getExternalContext());

        final String uri = helper.getRequestURI(context.getExternalContext());
        return (uri != null && uri.indexOf(Constants.BASE_URI) > -1);
    }

}
