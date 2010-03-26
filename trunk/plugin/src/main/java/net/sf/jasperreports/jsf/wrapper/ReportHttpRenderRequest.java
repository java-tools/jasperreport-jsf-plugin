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
package net.sf.jasperreports.jsf.wrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.faces.render.ResponseStateManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import net.sf.jasperreports.jsf.Constants;

import net.sf.jasperreports.jsf.util.Util;

/**
 *
 * @author aalonsodominguez
 */
public class ReportHttpRenderRequest extends HttpServletRequestWrapper
        implements ReportRenderRequest {

    private String pathInfo;
    private String servletPath;
    private String viewId;
    private String viewState;

    public ReportHttpRenderRequest(HttpServletRequest request,
            String viewId, String facesMapping, String viewState) {
        super(request);
        this.viewId = viewId;
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

    public String getReportClientId() {
        return getRequest().getParameter(Constants.PARAM_CLIENTID);
    }

    @Override
    public String getServletPath() {
        return servletPath;
    }

    public String getViewId() {
        return viewId;
    }

    public void release() {
        servletPath = null;
        pathInfo = null;
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
