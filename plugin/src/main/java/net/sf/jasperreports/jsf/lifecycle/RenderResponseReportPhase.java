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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.MalformedReportURLException;
import net.sf.jasperreports.jsf.UnregisteredUIReportException;
import net.sf.jasperreports.jsf.component.UIReport;

/**
 *
 * @author A. Alonso Dominguez
 */
public class RenderResponseReportPhase extends ReportPhase
        implements ContextCallback {

    private static final Logger logger = Logger.getLogger(
            RenderResponseReportPhase.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    protected RenderResponseReportPhase() { }

    @Override
    public void doBeforePhase(FacesContext context) throws FacesException {
        super.doBeforePhase(context);
        try {
            final ExternalContext extContext = context.getExternalContext();

            final String clientId = context.getExternalContext()
                    .getRequestParameterMap().get(Constants.PARAM_CLIENTID);
            if (clientId == null) {
                throw new MalformedReportURLException("Missed parameter: "
                        + Constants.PARAM_CLIENTID);
            }

            final UIReport report = (UIReport) extContext.getSessionMap()
                    .get(Constants.REPORT_COMPONENT_KEY_PREFIX + clientId);
            if (report == null) {
                throw new UnregisteredUIReportException(clientId);
            }

            UIViewRoot viewRoot = context.getViewRoot();
            if (!viewRoot.invokeOnComponent(context, clientId, this)) {
                throw new ReportLifecycleException("No report with id '"
                        + clientId + "' could be invoked for rendering.");
            }
        } finally {
            context.responseComplete();
        }
    }

    public void invokeContextCallback(FacesContext context, UIComponent target) {
        if (context == null) {
            throw new IllegalArgumentException("'context' can't be null");
        }
        if (target == null) {
            throw new IllegalArgumentException("'target' can't be null");
        }
        if (!(target instanceof UIReport)) {
            throw new IllegalArgumentException(
                    "'target' must be a report component");
        }

        String clientId = target.getClientId(context);
        UIReport report = (UIReport) target;
        logger.log(Level.FINER, "JRJSF_0016", clientId);
        try {
            report.encodeContent(context);
            report.encodeHeaders(context);
        } catch (final IOException e) {
            throw new ReportLifecycleException(e);
        } finally {
            logger.log(Level.FINER, "JRJSF_0017", clientId);
        }
    }

}
