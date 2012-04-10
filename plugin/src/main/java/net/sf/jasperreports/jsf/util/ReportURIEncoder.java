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
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.config.Configuration;
import net.sf.jasperreports.jsf.context.ExternalContextHelper;
import net.sf.jasperreports.jsf.context.JRFacesContext;

import javax.faces.context.FacesContext;
import javax.faces.render.ResponseStateManager;
import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author A. Alonso Dominguez
 */
public final class ReportURIEncoder {
    
    private static final Logger logger = Logger.getLogger(
            ReportURIEncoder.class.getPackage().getName(), 
            Constants.LOG_MESSAGES_BUNDLE);

    /**
     * Decodes the URI string into a formal representation of a report URI.
     *
     * @param context the faces' context
     * @param uri the request uri
     * @return a formal representation of the report URI.
     * @throws IOException if any I/O error happens when decoding the uri string.
     */
    public static ReportURI decodeReportURI(FacesContext context, String uri) 
    throws IOException {
        if (context == null) {
            throw new IllegalArgumentException("'context' can't be null");
        }
        if (uri == null || uri.indexOf(Constants.BASE_URI) < 0) {
            throw new IllegalArgumentException("URI [" + uri +
                    "] is not a proper report URI.");
        }

        final Configuration config = Configuration.getInstance(
                context.getExternalContext());
        
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "JRJSF_0046", uri);
        }

        // Separate the uri in proper uri and query string
        String queryString = "";
        int i = uri.indexOf("?");
        if (i >= 0) {
            queryString = uri.substring(i + 1);
            uri = uri.substring(0, i);
        }

        // Now, check if there is any 'jsessionid' in the url and
        // get rid of it
        i = uri.indexOf(";jsessionid=");
        if (i >= 0) {
            uri = uri.substring(0, i);
        }

        // Remove request context path
        String contextPath = context.getExternalContext().getRequestContextPath();
        if (uri.startsWith(contextPath)) {
            uri = uri.substring(contextPath.length());
        }
        
        // Guess invocation path for the URI
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

        // Remove the faces mapping from the URI
        if (Util.isPrefixMapped(mapping)) {
            uri = uri.substring(mapping.length());
        } else {
            int mappingStart = uri.length() - mapping.length();
            uri = uri.substring(0, mappingStart);
        }

        // Remove prefix used to build the URI
        uri = uri.substring(Constants.BASE_URI.length());

        // Now start building the ReportURI instance
        ReportURIImpl reportURI = new ReportURIImpl();
        reportURI.setFacesMapping(mapping);

        i = uri.indexOf("/");
        if (i < 0) {
            throw new IllegalArgumentException("URI [" + uri +
                    "] doesn't contain a report clientId");
        }
        reportURI.setReportClientId(uri.substring(0, i));
        uri = uri.substring(i);
        
        reportURI.setViewId(uri);
        
        // parse query string and decode parameters from the URI
        String[] tokens = queryString.split("&");
        for (String token : tokens) {
            int eq = token.indexOf("=");
            if (eq == -1) {
                reportURI.addParameter(token, "");
            } else {
                reportURI.addParameter(token.substring(0, eq),
                        token.substring(eq + 1));
            }
        }
        
        return reportURI;
    }
    
    /**
     * Builds the report URL which will trigger the <code>RENDER_REPORT</code>
     * phase of the plug-in's lifecycle.
     *
     * @param context the faces' context.
     * @param component the report component.
     * @return the report URL.
     * @throws IOException if an I/O error happens when encoding the URI.
     */
    public static ReportURI encodeReportURI(
            FacesContext context, UIReport component)
    throws IOException {
        if (context == null) {
            throw new IllegalArgumentException("'context' can't be null");
        }
        if (component == null) {
            throw new IllegalArgumentException("'component' can't be null");
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
        
        reportURI.setFacesMapping(mapping);
        reportURI.setReportClientId(component.getClientId(context));
        reportURI.setViewId(helper.getViewId(context.getExternalContext()));
        
        // Encode request parameters into the report URI
        Map<String, String[]> parameterMap = context.getExternalContext()
                .getRequestParameterValuesMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            // View state should not be encoded as a parameter of the report URI
            if (ResponseStateManager.VIEW_STATE_PARAM.equals(entry.getKey())) {
                continue;
            }
            reportURI.setParameter(entry.getKey(), entry.getValue());
        }
        
        return reportURI;
    }
    
    private ReportURIEncoder() { }
    
}
