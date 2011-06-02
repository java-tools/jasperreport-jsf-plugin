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
import java.util.Map;
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
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.component.UISubreport;
import net.sf.jasperreports.jsf.convert.SourceConverter;
import net.sf.jasperreports.jsf.engine.Source;
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

    public static final String ATTR_JASPER_PRINT =
            Constants.PACKAGE_PREFIX + ".JASPER_PRINT";

    public static final String SUBREPORT_PARAMETER_SEPARATOR = ".";

    // Report Parameters

    /** The Constant PARAM_REPORT_CLASSLOADER. */
    public static final String PARAM_REPORT_CLASSLOADER = "REPORT_CLASS_LOADER";
    /** The Constant PARAM_REPORT_LOCALE. */
    public static final String PARAM_REPORT_LOCALE = "REPORT_LOCALE";

    public static final String PARAM_APPLICATION_SCOPE = "APPLICATION_SCOPE";
    public static final String PARAM_SESSION_SCOPE = "SESSION_SCOPE";
    public static final String PARAM_REQUEST_SCOPE = "REQUEST_SCOPE";

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
    public final void fill(final FacesContext context, final UIReport component)
            throws FillerException {
        final String reportName = getStringAttribute(component, "name",
                component.getClientId(context));
        logger.log(Level.FINE, "JRJSF_0003", reportName);

        Source reportSource = component.getSubmittedSource();
        try {
            final Map<String, Object> params =
                    buildParamMap(context, component);
            JasperPrint print = doFill(context, component, params);
            context.getExternalContext().getRequestMap()
                    .put(ATTR_JASPER_PRINT, print);
        } finally {
            if (reportSource != null) {
                try {
                    reportSource.dispose();
                    reportSource = null;
                } catch (Exception e) { ; }  
            }
        }
    }

    /**
     * Builds the param map.
     *
     * @param context the context
     * @param component the component
     *
     * @return the map<string, object>
     */
    protected Map<String, Object> buildParamMap(final FacesContext context,
            final UIReport component)
    throws FillerException {
        final Map<String, Object> parameters = new HashMap<String, Object>();

        // Build param map using component's child parameters and subreports
        processParameterMap(context, component, parameters, null);

        // Include implicit parameters
        processImplicitParameters(context, component, parameters);

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
    protected JasperPrint doFill(FacesContext context, UIReport component,
            Map<String, Object> parameters)
    throws FillerException {
        JasperReport report = component.getSubmittedReport();
        Source reportSource = component.getSubmittedSource();
        SourceConverter converter = component.getConverter();

        JRBaseFiller jrFiller;
        try {
            jrFiller = JRFiller.createFiller(report);
        } catch (JRException e) {
            throw new FillerException(e);
        }
        
        JasperPrint print = null;
        try {
            if (reportSource == null) {
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
        }
        return print;
    }

    private void processImplicitParameters(FacesContext context,
            UIReport component, Map<String, Object> parameters) {
        // Specific component parameters
        parameters.put(PARAM_REPORT_CLASSLOADER,
                Util.getClassLoader(component));
        parameters.put(PARAM_REPORT_LOCALE,
                context.getViewRoot().getLocale());

        parameters.put(PARAM_APPLICATION_SCOPE,
                context.getExternalContext().getApplicationMap());
        parameters.put(PARAM_SESSION_SCOPE,
                context.getExternalContext().getSessionMap());
        parameters.put(PARAM_REQUEST_SCOPE,
                context.getExternalContext().getRequestMap());
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
            if (kid instanceof UISubreport) {
                final UISubreport subreport = (UISubreport) kid;
                parameters.put(subreport.getName(),
                        subreport.getSubmittedReport());
                processParameterMap(context, subreport, parameters,
                        (prefix != null && prefix.length() > 0 ?
                            prefix + SUBREPORT_PARAMETER_SEPARATOR : "")
                        + subreport.getName());
            } else if (kid instanceof UIParameter) {
                final UIParameter param = (UIParameter) kid;
                String paramName;
                if (prefix != null && prefix.length() > 0) {
                    paramName = prefix + SUBREPORT_PARAMETER_SEPARATOR +
                            param.getName();
                } else {
                    paramName = param.getName();
                }

                parameters.put(paramName, param.getValue());
            }
        }
    }

}