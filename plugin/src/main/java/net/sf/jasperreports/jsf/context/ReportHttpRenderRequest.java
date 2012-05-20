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
package net.sf.jasperreports.jsf.context;

import net.sf.jasperreports.jsf.uri.ReportURI;
import net.sf.jasperreports.jsf.util.Util;

import javax.faces.render.ResponseStateManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP implementation of a <tt>ReportRenderRequest</tt>.
 *
 * @author A. Alonso Dominguez
 */
final class ReportHttpRenderRequest extends HttpServletRequestWrapper
        implements ReportRenderRequest {

    /** Current request path info. */
    private String pathInfo;
    /** Current request servlet path. */
    private String servletPath;
    private ReportURI reportURI;
    /** View state to be restored. */
    private String viewState;

    private Map<String, String[]> parameterMap;
    
    /**
     * Instantiates a new HTTP report render request.
     *
     * @param request HTTP request to wrap around.
     * @param reportURI the <tt>ReportURI</tt> that triggered this report render request.
     * @param viewState view state to restore.
     */
    public ReportHttpRenderRequest(final HttpServletRequest request,
            final ReportURI reportURI,
            final String viewState) {
        super(request);
        this.reportURI = reportURI;
        this.viewState = viewState;
        reverseEngineerPaths();
    }

    /**
     * Overriden version to allow obtaining the view state
     * as a list of parameters.
     *
     * @param name the parameter name.
     * @return the parameter value.
     */
    @Override
    public String getParameter(final String name) {
        String[] values = getParameterValues(name);
        if (values == null || values.length == 0) {
            return null;
        } else {
            return values[0];
        }
    }

    /**
     * Overriden version of the parameter map containing the view state.
     *
     * @return the parameter map.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String[]> getParameterMap() {
    	if (parameterMap == null) {
    		parameterMap = new HashMap<String, String[]>();
    		parameterMap.putAll(super.getParameterMap());
            Enumeration<String> reportParamNames = reportURI.getParameterNames();
            while (reportParamNames.hasMoreElements()) {
                String paramName = reportParamNames.nextElement();
                parameterMap.put(paramName, reportURI.getParameterValues(paramName));
            }
            if (viewState != null) {
                parameterMap.put(ResponseStateManager.VIEW_STATE_PARAM,
                        new String[]{viewState});
            }
    	}
        return Collections.unmodifiableMap(parameterMap);
    }

    /**
     * Overriden version of the parameter names enumerator.
     *
     * @return an enumeration of the parameter names.
     */
    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(getParameterMap().keySet());
    }

    /**
     * Overriden version for obtaining an array of parameter values.
     *
     * @param name the parameter name.
     * @return the parameter values.
     */
    @Override
    public String[] getParameterValues(final String name) {
        return getParameterMap().get(name);
    }

    /**
     * Obtains the request path info.
     *
     * @return the request path info.
     */
    @Override
    public String getPathInfo() {
        return pathInfo;
    }

    /**
     * Obtains the report component client id.
     *
     * @return the report component client id.
     */
    @Deprecated
    public String getReportClientId() {
        return reportURI.getReportClientId();
    }

    /**
     * Obtains the request servlet path.
     *
     * @return the request servlet path.
     */
    @Override
    public String getServletPath() {
        return servletPath;
    }

    public ReportURI getReportURI() {
        return reportURI;
    }

    /**
     * Obtains the report view id.
     *
     * @return the report view id.
     */
    @Deprecated
    public String getViewId() {
        return reportURI.getViewId();
    }

    /**
     * Releases internal local variables.
     */
    public void release() {
        servletPath = null;
        pathInfo = null;
    }

    /**
     * Method designed to guess the path info & servlet path data
     * from the view id and the faces mapping.
     */
    private void reverseEngineerPaths() {
        String facesMapping = reportURI.getFacesMapping();
        String viewId = reportURI.getViewId();

        if (Util.isPrefixMapped(facesMapping)) {
            int i = facesMapping.indexOf("/*");
            if (i != -1) {
                servletPath = facesMapping.substring(0, i);
            } else {
                servletPath = facesMapping;
            }
            pathInfo = viewId;
        } else {
            servletPath = viewId.substring(0,
                    viewId.lastIndexOf('.')) + facesMapping;
            pathInfo = null;
        }
    }
}
