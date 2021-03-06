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

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.faces.render.ResponseStateManager;
import javax.portlet.ResourceRequest;
import javax.portlet.faces.Bridge;
import javax.portlet.filter.ResourceRequestWrapper;

import net.sf.jasperreports.jsf.uri.ReportURI;

/**
 *
 * @author A. Alonso Dominguez
 */
final class ReportPortletRenderRequest extends ResourceRequestWrapper
        implements ReportRenderRequest {

    private final ReportURI reportURI;
    private final String oldViewId;
    private final String viewState;
    
    private Map<String, String[]> parameterMap;

    public ReportPortletRenderRequest(ResourceRequest request,
    		ReportURI reportURI, String viewState) {
        super(request);

        this.reportURI = reportURI;
        this.oldViewId = (String) request.getAttribute(Bridge.VIEW_ID);
        request.setAttribute(Bridge.VIEW_ID, reportURI.getViewId());
        this.viewState = viewState;
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

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(getParameterMap().keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        return getParameterMap().get(name);
    }

    public ReportURI getReportURI() {
        return reportURI;
    }

    @Deprecated
    public String getReportClientId() {
        return reportURI.getReportClientId();
    }

    @Deprecated
    public String getViewId() {
        return reportURI.getViewId();
    }

    public void release() {
        getRequest().setAttribute(Bridge.VIEW_ID, oldViewId);
    }

}
