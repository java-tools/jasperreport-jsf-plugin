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
package net.sf.jasperreports.jsf.engine.fill;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.fill.JRBaseFiller;
import net.sf.jasperreports.engine.fill.JRFiller;
import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.component.UIOutputReport;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.component.UISource;
import net.sf.jasperreports.jsf.component.UISubreport;
import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.convert.SourceConverter;
import net.sf.jasperreports.jsf.convert.Source;
import net.sf.jasperreports.jsf.engine.FillerException;
import net.sf.jasperreports.jsf.engine.Filler;
import net.sf.jasperreports.jsf.util.Util;

import static net.sf.jasperreports.jsf.util.ComponentUtil.*;

/**
 * Default Filler implementation.
 *
 * @author A. Alonso Dominguez
 */
public class DefaultFiller implements Filler {

    public static final String SUBREPORT_PARAMETER_SEPARATOR = ".";

    // Report Parameters

    /** The Constant PARAM_REPORT_CLASSLOADER. */
    public static final String PARAM_REPORT_CLASSLOADER = "REPORT_CLASS_LOADER";
    /** The Constant PARAM_REPORT_LOCALE. */
    public static final String PARAM_REPORT_LOCALE = "REPORT_LOCALE";
    
    public static final String PARAM_REPORT_RESOURCE_BUNDLE = 
            "REPORT_RESOURCE_BUNDLE";

    public static final String PARAM_APPLICATION_SCOPE = "APPLICATION_SCOPE";
    public static final String PARAM_SESSION_SCOPE = "SESSION_SCOPE";
    public static final String PARAM_REQUEST_SCOPE = "REQUEST_SCOPE";

    public static final String PARAM_SUBREPORT_REFERENCE = "__report";
    public static final String PARAM_SUBREPORT_DATASOURCE = "__dataSource";
    public static final String PARAM_SUBREPORT_CONNECTION = "__connection";
    public static final String PARAM_SUBREPORT_SOURCE = "__source";
    
    /** The logger. */
    private static final Logger logger = Logger.getLogger(
            DefaultFiller.class.getPackage().getName(),
            Constants.LOG_MESSAGES_BUNDLE);

    /**
     * Fill the report object with data comming from the
     * submitted source object (if specified).
     *
     * @param context current faces' context.
     * @param component report component.
     * @throws FillerException if filler throws any exception.
     */
    public final void fill(final FacesContext context, final UIOutputReport component)
            throws FillerException {
        final String reportName = getStringAttribute(component, "name", 
                component.getId());
        logger.log(Level.FINE, "JRJSF_0003", reportName);

        String clientId = component.getClientId(context);
        final Map<String, Object> params = buildParamMap(context, component);
        
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "JRJSF_0046", new Object[]{ 
                clientId, params 
            });
        }
        
        JasperPrint print = doFill(context, component, params);
        if (print == null) {
            throw new CouldNotFillReportException(
                    "No jasper print generated for component: " + clientId);
        }
    	component.setSubmittedPrint(print);
    }

    /**
     * Builds the param map.
     *
     * @param context the context
     * @param component the component
     *
     * @return the map<string, object>
     */
    private Map<String, Object> buildParamMap(final FacesContext context,
            final UIOutputReport component)
    throws FillerException {
        final Map<String, Object> parameters = new HashMap<String, Object>();

        // Build param map using component's child parameters and subreports
        processParameterMap(context, component, parameters, null);

        // Include implicit parameters
        processImplicitParameters(context, component, parameters);

        // Include custom parameters that may be provided by an extension of this class
        processCustomParameters(context, component, parameters);
        
        return parameters;
    }

    /**
     * Performs the internal fill operation.
     *
     * @param context current faces' context.
     * @param component report component
     * @param parameters report parameters.
     * @return the generated <tt>JasperPrint</tt> result.
     * @throws FillerException if some error happens.
     */
    protected JasperPrint doFill(FacesContext context, UIOutputReport component,
            Map<String, Object> parameters)
    throws FillerException {
        JasperReport report = component.getSubmittedReport();
        if (report == null) {
            throw new NoSubmittedReportException(
                    component.getClientId(context));
        }

        JRBaseFiller jrFiller;
        try {
            jrFiller = JRFiller.createFiller(report);
        } catch (JRException e) {
            throw new FillerException(e);
        }
        
        Source reportSource = findReportSource(component);
        SourceConverter converter = findSourceConverter(context, component);
        JasperPrint print = null;
        try {
            if (reportSource == null) {
            	if (logger.isLoggable(Level.FINE)) {
            		logger.log(Level.FINE, "JRJSF_0045", component.getClientId(context));
            	}
                print = jrFiller.fill(parameters);
            } else {
                Object sourceObj = converter.convertFromSource(context,
                        component, reportSource);
                if (reportSource instanceof JRDataSource) {
                    print = jrFiller.fill(parameters, (JRDataSource) sourceObj);
                } else if (reportSource instanceof Connection) {
                    print = jrFiller.fill(parameters, (Connection) sourceObj);
                }
            }
        } catch (final JRException e) {
            throw new FillerException(e);
        } finally {
        	if (reportSource != null) {
                try {
                    reportSource.dispose();
                    reportSource = null;
                } catch (Exception e) { ; }  
            }
        }
        return print;
    }
    
    protected void processCustomParameters(FacesContext context,
            UIOutputReport component, Map<String, Object> parameters) { }

    private void processImplicitParameters(FacesContext context,
            UIOutputReport component, Map<String, Object> parameters) {
        ClassLoader classLoader = Util.getClassLoader(component);
        Locale locale = context.getViewRoot().getLocale();
        
        // Specific component parameters
        parameters.put(PARAM_REPORT_CLASSLOADER, classLoader);
        parameters.put(PARAM_REPORT_LOCALE, locale);

        parameters.put(PARAM_APPLICATION_SCOPE,
                context.getExternalContext().getApplicationMap());
        parameters.put(PARAM_SESSION_SCOPE,
                context.getExternalContext().getSessionMap());
        parameters.put(PARAM_REQUEST_SCOPE,
                context.getExternalContext().getRequestMap());
        
        ResourceBundle resourceBundle = null;
        Object resourceBundleValue = component.getResourceBundle();
        if (resourceBundleValue instanceof ResourceBundle) {
            resourceBundle = (ResourceBundle) resourceBundleValue;
        } else if (resourceBundleValue instanceof String) {
            try {
                resourceBundle = ResourceBundle.getBundle(
                        (String) resourceBundleValue, locale, classLoader);
            } catch (MissingResourceException e) {
                resourceBundle = context.getApplication().getResourceBundle(
                        context, (String) resourceBundleValue);
            }
        }
        
        if (resourceBundle != null) {
            parameters.put(PARAM_REPORT_RESOURCE_BUNDLE, resourceBundle);
        }
    }

    /**
     * Builds parameter map recursively through the report/subreport tree.
     *
     * @param context cuurent faces' context.
     * @param component report component.
     * @param parameters report parameters.
     * @param prefix parent's report name, used as a parameter prefix.
     */
    private void processParameterMap(FacesContext context, UIReport component,
            Map<String, Object> parameters, String prefix) {
        for (final UIComponent kid : component.getChildren()) {
            StringBuilder paramName = new StringBuilder();
            if (prefix != null && prefix.length() > 0) {
                paramName.append(prefix).append(SUBREPORT_PARAMETER_SEPARATOR);
            }
            
            if (kid instanceof UISubreport) {
                final UISubreport subreport = (UISubreport) kid;
                paramName.append(subreport.getName());
                processSubreportParameterMap(context, subreport, 
                        parameters, paramName.toString());
                processParameterMap(context, subreport, parameters,
                        paramName.toString());
            } else if (kid instanceof UIParameter) {
                final UIParameter param = (UIParameter) kid;
                paramName.append(param.getName());
                parameters.put(paramName.toString(), param.getValue());
            }
        }
    }
    
    private void processSubreportParameterMap(FacesContext context, 
            UISubreport subreport,  Map<String, Object> parameters, String prefix) {
        StringBuilder paramPrefix = new StringBuilder();
        if (prefix != null && prefix.length() > 0) {
            paramPrefix.append(prefix).append(SUBREPORT_PARAMETER_SEPARATOR);
        }
        
        StringBuilder helper = new StringBuilder(paramPrefix);
        helper.append(PARAM_SUBREPORT_REFERENCE);
        parameters.put(helper.toString(), subreport.getSubmittedReport());
        
        Source source = findReportSource(subreport);
        if (source != null) {
            helper = new StringBuilder(paramPrefix);
            SourceConverter converter = findSourceConverter(context, subreport);
            Object paramValue = converter.convertFromSource(
                    context, subreport, source);
            
            if (source instanceof JRDataSource) {
                helper.append(PARAM_SUBREPORT_DATASOURCE);
            } else if (source instanceof Connection) {
                helper.append(PARAM_SUBREPORT_CONNECTION);
            } else {
                helper.append(PARAM_SUBREPORT_SOURCE);
                paramValue = source;
            }
            
            parameters.put(helper.toString(), paramValue);
        }
    }
    
    private Source findReportSource(UIReport report) {
        Source result = report.getSubmittedSource();
        if (result == null) {
            UISource source = null;
            for (UIComponent component : report.getChildren()) {
                if (component instanceof UISource) {
                    source = (UISource) component;
                    break;
                }
            }

            if (source != null) {
                result = source.getSubmittedSource();
            }
        }
        return result;
    }
    
    private SourceConverter findSourceConverter(FacesContext context, 
            UIReport report) {
        SourceConverter result = report.getSourceConverter();
        if (result == null) {
            JRFacesContext jrContext = JRFacesContext.getInstance(context);
            result = jrContext.createSourceConverter(context, report);
        }
        return result;
    }
    
}
