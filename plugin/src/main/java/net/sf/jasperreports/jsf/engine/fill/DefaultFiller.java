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
package net.sf.jasperreports.jsf.engine.fill;

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
import net.sf.jasperreports.jsf.convert.Source;
import net.sf.jasperreports.jsf.engine.Filler;
import net.sf.jasperreports.jsf.engine.FillerException;
import net.sf.jasperreports.jsf.util.Util;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import java.sql.Connection;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static net.sf.jasperreports.jsf.util.ComponentUtil.getStringAttribute;

/**
 * Default Filler implementation.
 *
 * @author A. Alonso Dominguez
 */
public class DefaultFiller implements Filler {

    /** The namespace separator for subreport parameters */
    public static final String SUBREPORT_PARAMETER_SEPARATOR = ".";

    // Report Parameters

    /** the class loader to be used internally by the report. */
    public static final String PARAM_REPORT_CLASSLOADER = "REPORT_CLASS_LOADER";

    /** The locale to be used by the report */
    public static final String PARAM_REPORT_LOCALE = "REPORT_LOCALE";

    /** The <tt>ResourceBundle</tt> instance to be passed in to the report */
    public static final String PARAM_REPORT_RESOURCE_BUNDLE = "REPORT_RESOURCE_BUNDLE";

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
     * Fill the report object with data coming from the
     * submitted source object (if specified).
     *
     * @param context current faces' context.
     * @param component report component.
     * @throws FillerException if filler throws any exception.
     */
    public final void fill(final FacesContext context, final UIOutputReport component)
            throws FillerException {
        final Map<String, Object> params = buildParamMap(context, component);
        JasperPrint print = doFill(context, component, params);
        if (print == null) {
            String clientId = component.getClientId(context);
            throw new CouldNotFillReportException(
                    "No jasper print generated for component: " + clientId);
        }
    	component.setSubmittedPrint(print);
    }

    /**
     * Builds the parameter map that will be given to the JasperEngine when
     * generating the report.
     * <p>
     * The parameter map is built using
     *
     * @param context the faces' context
     * @param component the report component
     * @return the parameter map analyzed from the component parameters.
     */
    private Map<String, Object> buildParamMap(final FacesContext context,
            final UIOutputReport component)
    throws FillerException {
    	final String reportName = getStringAttribute(component, "name",
                component.getClientId(context));
        logger.log(Level.FINER, "JRJSF_0003", reportName);
    	
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
     * Provides a means for overriding classes to feed the report parameter map
     * with custom implicit values
     *
     * @param context the faces' context
     * @param component the report component
     * @param parameters the actual parameter map
     */
    protected void processCustomParameters(FacesContext context,
                                           UIOutputReport component,
                                           Map<String, Object> parameters) { }

    /**
     * Performs the internal fill operation.
     *
     * @param context current faces' context.
     * @param component report component
     * @param parameters report parameters.
     * @return the generated <tt>JasperPrint</tt> result.
     * @throws FillerException if some error happens.
     */
    private JasperPrint doFill(FacesContext context, UIOutputReport component,
            Map<String, Object> parameters)
    throws FillerException {
        JasperReport report = component.getSubmittedReport();
        if (report == null) {
        	throw new IllegalStateException("Found a null report object previous to fill it.");
        }

        JRBaseFiller jrFiller;
        try {
            jrFiller = JRFiller.createFiller(report);
        } catch (JRException e) {
            throw new FillerException(e);
        }
        
        Source reportSource = findReportSource(component);
        JasperPrint print = null;
        try {
            if (reportSource == null) {
            	if (logger.isLoggable(Level.FINE)) {
            		logger.log(Level.FINE, "JRJSF_0045", component.getClientId(context));
            	}
                print = jrFiller.fill(parameters);
            } else {
                print = printWithFiller(context, jrFiller, reportSource, parameters);
            }
        } catch (final JRException e) {
            throw new FillerException(e);
        } finally {
        	if (reportSource != null) {
                try {
                    reportSource.dispose();
                } catch (Exception e) { ; }
                reportSource = null;
            }
        }
        
        if (print == null) {
        	throw new FillerException("Couldn't fill report template with data.");
        }
        
        return print;
    }

    /**
     * Provides an extension point for extending classes to support other types of
     * sources than the ones supported by JasperReports by default.
     *
     * @param context the faces' context
     * @param filler the JasperReports filler that will perform the work
     * @param source a data source wrapper that should be translated into its appropriate form
     *               before invoking the filler
     * @param parameters the report parameter map
     * @return a <tt>JasperPrint</tt> containing the report contents or <tt>null</tt> if
     *         the source wrapper is not supported by this filler implementation
     * @throws JRException if an error happens when filling the report with data
     */
    protected JasperPrint printWithFiller(FacesContext context, 
    		JRBaseFiller filler, Source source, Map<String, Object> parameters) 
    throws JRException {
    	JasperPrint print = null;
        Object wrappedSource = source.getWrappedSource();
    	if (wrappedSource instanceof Connection) {
    		print = filler.fill(parameters, (Connection) wrappedSource);
    	} else if (wrappedSource instanceof JRDataSource) {
    		print = filler.fill(parameters, (JRDataSource) wrappedSource);
    	}
    	return print;
    }

    private void processImplicitParameters(FacesContext context,
                                           UIOutputReport component, Map<String, Object> parameters) {
        ClassLoader classLoader = Util.getClassLoader(this);
        Locale locale = context.getViewRoot().getLocale();
        
        // Specific component parameters
        parameters.put(PARAM_REPORT_CLASSLOADER, classLoader);
        parameters.put(PARAM_REPORT_LOCALE, locale);

        // Web application related implicit parameters
        parameters.put(PARAM_APPLICATION_SCOPE,
                context.getExternalContext().getApplicationMap());
        parameters.put(PARAM_SESSION_SCOPE,
                context.getExternalContext().getSessionMap());
        parameters.put(PARAM_REQUEST_SCOPE,
                context.getExternalContext().getRequestMap());

        // Resource bundle for the report
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
            /* EXPERIMENTAL: trying to feed the subreport with the unwrapped data source */

            helper = new StringBuilder(paramPrefix);
            Object paramValue = source.getWrappedSource();
            
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
    
}
