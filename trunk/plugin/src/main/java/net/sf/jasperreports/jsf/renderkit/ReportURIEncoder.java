/*
 * JaspertReports JSF Plugin Copyright (C) 2011 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.renderkit;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.config.Configuration;
import net.sf.jasperreports.jsf.context.ExternalContextHelper;
import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.util.Util;

/**
 *
 * @author 501944227
 */
public final class ReportURIEncoder {
    
    private static final Logger logger = Logger.getLogger(
            ReportURIEncoder.class.getPackage().getName(), 
            Constants.LOG_MESSAGES_BUNDLE);
    
    public static ReportURI decodeReportURI(FacesContext context, String uri) {
        final Configuration config = Configuration.getInstance(
                context.getExternalContext());
        
        String queryString = "";
        int i = uri.indexOf("?");
        if (i >= 0) {
            queryString = uri.substring(i + 1);
            uri = uri.substring(0, i);
        }
        
        String mapping = Util.getInvocationPath(context);
        if (!config.getFacesMappings().contains(mapping)) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "JRJSF_0021", new Object[]{mapping,
                            config.getDefaultMapping()});
            }
            mapping = config.getDefaultMapping();
        }
        
        if (!(uri.startsWith(mapping) || uri.endsWith(mapping))) {
            throw new IllegalArgumentException("URI [" + uri + 
                        "] is not a faces' URL");
        }
        
        ReportURIImpl reportURI = new ReportURIImpl();
        reportURI.facesMapping = mapping;
        
        if (Util.isPrefixMapped(mapping)) {
            uri = uri.substring(mapping.length());
        } else {
            uri = uri.substring(0, uri.indexOf(mapping));
        }
        
        if (!uri.startsWith(Constants.BASE_URI)) {
            throw new IllegalArgumentException("URI [" + uri + 
                    "] is not a proper report URI.");
        }
        
        uri = uri.substring(Constants.BASE_URI.length() + 1);
        i = uri.indexOf("/");
        if (i < 0) {
            throw new IllegalArgumentException("URI [" + uri + 
                    "] doesn't contain a report clientId");
        }
        reportURI.reportClientId = uri.substring(0, i);
        uri = uri.substring(i);
        
        reportURI.viewId = uri;
        
        // TODO parse queryString
        
        return reportURI;
    }
    
    /**
     * Builds the report URL which will trigger the <code>RENDER_REPORT</code>
     * phase of the plugin's lifecycle.
     *
     * @param context the faces' context.
     * @param component the report component.
     *
     * @return the report URL.
     */
    public static ReportURI encodeReportURI(
            FacesContext context, UIComponent component) {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        if (component == null) {
            throw new IllegalArgumentException();
        }
        
        JRFacesContext jrContext = JRFacesContext.getInstance(context);
        final Configuration config = Configuration.getInstance(
                context.getExternalContext());
        final ExternalContextHelper helper = jrContext
                .getExternalContextHelper(context);
        
        ReportURIImpl reportURI = new ReportURIImpl();
        
        String mapping = Util.getInvocationPath(context);
        if (!config.getFacesMappings().contains(mapping)) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "JRJSF_0021", new Object[]{mapping,
                            config.getDefaultMapping()});
            }
            mapping = config.getDefaultMapping();
        }
        
        reportURI.facesMapping = mapping;
        reportURI.reportClientId = component.getClientId(context);
        reportURI.viewId = helper.getViewId(context.getExternalContext());
        
        return reportURI;
    }
    
    private ReportURIEncoder() { }
    
    private static class ReportURIImpl implements ReportURI {

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
        
        public String getReportClientId() {
            return reportClientId;
        }

        public String getViewId() {
            return viewId;
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
    
}
