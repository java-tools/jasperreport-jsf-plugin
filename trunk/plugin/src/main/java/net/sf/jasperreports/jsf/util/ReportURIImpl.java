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
package net.sf.jasperreports.jsf.util;

import net.sf.jasperreports.jsf.Constants;

import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import java.util.*;

final class ReportURIImpl implements ReportURI {

    private String facesMapping;
    private String reportClientId;
    private String viewId;

    private Map<String, List<String>> parameters =
            new HashMap<String, List<String>>();

    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(parameters.keySet());
    }

    public String getParameterValue(String name) {
        String result = null;
        if (parameters.containsKey(name)) {
            List<String> values = parameters.get(name);
            if (values != null && !values.isEmpty()) {
                result = values.get(0);
            }
        }
        return result;
    }

    public String[] getParameterValues(String name) {
        String[] result = null;
        if (parameters.containsKey(name)) {
            List<String> values = parameters.get(name);
            if (values != null) {
                result = values.toArray(new String[values.size()]);
            }
        }
        return result;
    }

    public String getFacesMapping() {
        return facesMapping;
    }
    
    public void setFacesMapping(String facesMapping) {
        this.facesMapping = facesMapping;
    }

    public String getReportClientId() {
        return reportClientId;
    }

    public void setReportClientId(String reportClientId) {
        this.reportClientId = reportClientId;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(Constants.BASE_URI);
        builder.append("/").append(getReportClientId());
        builder.append("/").append(getViewId());

        if (Util.isPrefixMapped(facesMapping)) {
            builder.insert(0, facesMapping);
        } else {
            builder.append(facesMapping);
        }

        if (!parameters.isEmpty()) {
            StringBuilder params = new StringBuilder();
            for (Map.Entry<String, List<String>> entry : parameters.entrySet()) {
                for (String v : entry.getValue()) {
                    if (params.length() > 0) {
                        params.append("&");
                    }
                    params.append(entry.getKey());
                    params.append("=");
                    params.append(v);
                }
            }
            builder.append("?").append(params);
        }

        FacesContext context = FacesContext.getCurrentInstance();
        ViewHandler viewHandler = context.getApplication().getViewHandler();
        return context.getExternalContext().encodeResourceURL(
                viewHandler.getResourceURL(context, builder.toString()));
    }

}
