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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.render.ResponseStateManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.config.Configuration;
import net.sf.jasperreports.jsf.util.ExternalContextHelper;
import net.sf.jasperreports.jsf.util.Util;

/**
 *
 * @author aalonsodominguez
 */
public class RestoreViewPhaseListener extends AbstractReportPhaseListener {

    public RestoreViewPhaseListener() { }

    public void afterPhase(PhaseEvent event) throws FacesException {
        FacesContext context = event.getFacesContext();
        if (isReportRequest(context)) {
            if (null == context.getViewRoot()) {
                throw new ReportLifecycleException(
                        "Report holder view couldn't be restored");
            }
            context.renderResponse();
        }
    }

    public void beforePhase(PhaseEvent event) throws FacesException {
        FacesContext context = event.getFacesContext();
        if (isReportRequest(context)) {
            Configuration config = Configuration.getInstance(context);
            String viewId = context.getExternalContext()
                    .getRequestParameterMap().get(Constants.PARAM_VIEWID);
            String viewState = getViewCacheMap(context).get(viewId);

            if (ExternalContextHelper.isServletContext(
                    context.getExternalContext())) {
                HttpServletRequest request = (HttpServletRequest)
                        context.getExternalContext().getRequest();
                request = new RenderReportRequest(request, viewId,
                        config.getDefaultMapping(), viewState);
                context.getExternalContext().setRequest(request);
            }
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    private class RenderReportRequest extends HttpServletRequestWrapper {

        private String pathInfo;
        private String servletPath;
        private String viewState;

        public RenderReportRequest(HttpServletRequest request,
                String viewId, String facesMapping, String viewState) {
            super(request);
            this.viewState = viewState;
            reverseEngineerPaths(viewId, facesMapping);
        }

        @Override
        public String getParameter(String name) {
            String[] values = getParameterValues(name);
            if (values == null || values.length == 0) {
                return null;
            } else {
                return values[0];
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> paramMap = new HashMap<String, String[]>();
            paramMap.putAll(super.getParameterMap());
            paramMap.put(ResponseStateManager.VIEW_STATE_PARAM,
                    new String[]{viewState});
            return Collections.unmodifiableMap(paramMap);
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return Collections.enumeration(getParameterMap().keySet());
        }

        @Override
        public String[] getParameterValues(String name) {
            return getParameterMap().get(name);
        }

        @Override
        public String getPathInfo() {
            return pathInfo;
        }

        @Override
        public String getServletPath() {
            return servletPath;
        }

        private void reverseEngineerPaths(String viewId, String facesMapping) {
            if (Util.isPrefixMapped(facesMapping)) {
                int i = facesMapping.indexOf("/*");
                if (i != -1) {
                    servletPath = facesMapping.substring(0, i);
                } else {
                    servletPath = facesMapping;
                }
                pathInfo = viewId;
            } else {
                servletPath = viewId.substring(0, viewId.lastIndexOf('.'))
                        + facesMapping.substring(facesMapping.indexOf('.'));
                pathInfo = null;
            }
        }
    }
}
