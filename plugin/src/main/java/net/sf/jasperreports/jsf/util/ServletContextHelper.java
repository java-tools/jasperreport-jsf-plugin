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
package net.sf.jasperreports.jsf.util;

import java.io.IOException;
import javax.faces.context.ExternalContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.jsf.Constants;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.config.Configuration;
import net.sf.jasperreports.jsf.renderkit.ReportRenderer;
import net.sf.jasperreports.jsf.engine.ReportHttpRenderRequest;
import net.sf.jasperreports.jsf.engine.ReportRenderRequest;

final class ServletContextHelper extends ExternalContextHelper {

    protected ServletContextHelper() { }

    @Override
    public ReportRenderRequest restoreReportRequest(ExternalContext context) {
        final Configuration config = Configuration.getInstance(context);
        final String viewId = context.getRequestParameterMap()
                .get(Constants.PARAM_VIEWID);
        final String viewState = getViewCacheMap(context).get(viewId);

        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        request = new ReportHttpRenderRequest(request, viewId,
                config.getDefaultMapping(), viewState);
        context.setRequest(request);
        return (ReportRenderRequest) request;
    }

    @Override
    public String getRequestServerName(final ExternalContext context) {
        final HttpServletRequest request = (HttpServletRequest)
                context.getRequest();
        return request.getServerName();
    }

    @Override
    public String getRequestURI(final ExternalContext context) {
        final HttpServletRequest request = (HttpServletRequest)
                context.getRequest();
        return request.getRequestURI();
    }

    @Override
    public String getResourceRealPath(final ExternalContext context, String name) {
        final ServletContext ctx = (ServletContext) context.getContext();
        return ctx.getRealPath(name);
    }

    @Override
    public void writeHeaders(final ExternalContext context,
            final ReportRenderer renderer, final UIReport report)
            throws IOException {
        final HttpServletResponse response = (HttpServletResponse)
                context.getResponse();
        response.setHeader("Cache-Type", "no-cache");
        response.setHeader("Expires", "0");

        if (report.getName() != null) {
            response.setHeader("Content-Disposition", 
                    renderer.encodeContentDisposition(report,
                    response.getCharacterEncoding()));
        }
    }

    @Override
    public void writeResponse(final ExternalContext context,
            final String contentType, final byte[] data) throws IOException {
        final HttpServletResponse response = (HttpServletResponse)
                context.getResponse();
        response.setContentType(contentType);
        response.setContentLength(data.length);
        response.getOutputStream().write(data);
    }
}
