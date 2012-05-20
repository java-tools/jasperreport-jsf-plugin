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
package net.sf.jasperreports.jsf.uri;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.util.Util;

import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import java.util.*;

final class ReportURIImpl implements ReportURI {

    private String facesMapping;
    private String reportClientId;
    private String viewId;

    private Map<String, List<String>> parameters =
            new HashMap<String, List<String>>();

    public void addParameter(String name, String value) {
        List<String> values = parameters.get(name);
        if (values == null) {
            values = new ArrayList<String>();
            parameters.put(name, values);
        }
        values.add(value);
    }

    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> result = new HashMap<String, String[]>();
        for (Map.Entry<String, List<String>> entry : parameters.entrySet()) {
            List<String> values = entry.getValue();
            result.put(entry.getKey(), values.toArray(new String[values.size()]));
        }
        return Collections.unmodifiableMap(result);
    }

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

    public void setParameter(String name, String value[]) {
        List<String> values = new ArrayList<String>();
        values.addAll(Arrays.asList(value));
        parameters.put(name, values);
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
        FacesContext context = FacesContext.getCurrentInstance();
        return ReportURIEncoder.encodeReportURI(context, this);
    }

}
