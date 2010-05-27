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

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseListener;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.util.ExternalContextHelper;
import net.sf.jasperreports.jsf.engine.ReportRenderRequest;

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
abstract class AbstractReportPhaseListener implements PhaseListener {

    protected boolean isReportRequest(final FacesContext context) {
        Object request = context.getExternalContext().getRequest();
        if (request instanceof ReportRenderRequest) {
            return true;
        } else {
            final ExternalContextHelper helper = ExternalContextHelper
                    .getInstance(context.getExternalContext());

            final String uri = helper.getRequestURI(
                    context.getExternalContext());
            return (uri != null && uri.indexOf(Constants.BASE_URI) > -1);
        }
    }

}
