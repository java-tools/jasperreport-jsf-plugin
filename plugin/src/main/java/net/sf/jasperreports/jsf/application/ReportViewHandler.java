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
package net.sf.jasperreports.jsf.application;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.component.UIOutputReport;
import net.sf.jasperreports.jsf.context.ReportRenderRequest;
import net.sf.jasperreports.jsf.lifecycle.NoSuchReportComponentInViewException;
import net.sf.jasperreports.jsf.uri.ReportURI;
import net.sf.jasperreports.jsf.util.Util;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportViewHandler extends ViewHandlerWrapper {

    private static final Logger logger = Logger.getLogger(
            ReportViewHandler.class.getPackage().getName(),
            Constants.LOG_MESSAGES_BUNDLE);

    private static final ContextCallback REPORT_RENDER_CALLBACK = new ReportRenderCallback();

    private ViewHandler delegate;

    public ReportViewHandler(ViewHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public void renderView(FacesContext context, UIViewRoot viewToRender)
            throws IOException, FacesException {
        // Invoke super when not inside a report render request
        // otherwise, render the report
        if (!Util.isReportRenderRequest()) {
            super.renderView(context, viewToRender);
            return;
        }

        ReportRenderRequest request = (ReportRenderRequest) context.getExternalContext().getRequest();
        buildView(context, request.getReportURI());
        doRenderReport(context, viewToRender, request.getReportURI());
    }

    @Override
    protected ViewHandler getWrapped() {
        return delegate;
    }

    private void buildView(FacesContext context, ReportURI reportURI)
            throws IOException, FacesException {
        String requestURI = reportURI.getViewId();

        // TODO update JSTL locale setting

        logger.log(Level.FINE, "JRJSF_0047", reportURI.getViewId());

        context.getExternalContext().dispatch(requestURI);
    }

    private void doRenderReport(FacesContext context, UIViewRoot reportView, ReportURI reportURI)
            throws IOException, FacesException {
        String clientId = reportURI.getReportClientId();
        if (!reportView.invokeOnComponent(context, clientId, REPORT_RENDER_CALLBACK)) {
            throw new NoSuchReportComponentInViewException(clientId);
        }
    }

    private static class ReportRenderCallback implements ContextCallback {

        public void invokeContextCallback(FacesContext context, UIComponent target) {
            if (context == null) {
                throw new IllegalArgumentException("'context' can't be null");
            }
            if (target == null) {
                throw new IllegalArgumentException("'target' can't be null");
            }
            if (!(target instanceof UIOutputReport)) {
                throw new IllegalArgumentException(
                        "'target' must be a report output component");
            }

            String clientId = target.getClientId(context);
            UIOutputReport report = (UIOutputReport) target;
            logger.log(Level.FINER, "JRJSF_0016", clientId);
            try {
                report.encodeContent(context);
                report.encodeHeaders(context);
            } catch (final IOException e) {
                throw new JRFacesException(e);
            } finally {
                logger.log(Level.FINER, "JRJSF_0017", clientId);
            }
        }
    }

}
