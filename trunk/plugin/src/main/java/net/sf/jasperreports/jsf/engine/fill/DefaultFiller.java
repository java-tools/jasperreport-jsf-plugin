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
package net.sf.jasperreports.jsf.engine.fill;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;
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
import net.sf.jasperreports.engine.util.ContextClassLoaderObjectInputStream;
import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.engine.ReportSource;
import net.sf.jasperreports.jsf.engine.FillerException;
import net.sf.jasperreports.jsf.engine.Filler;
import net.sf.jasperreports.jsf.engine.source.JRDataSourceHolder;
import net.sf.jasperreports.jsf.engine.source.ConnectionHolder;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.UnresolvedResourceException;
import net.sf.jasperreports.jsf.util.Util;

/**
 *
 * @author antonio.alonso
 */
public class DefaultFiller implements Filler {

    public static final String ATTR_JASPER_PRINT =
            Constants.PACKAGE_PREFIX + ".JASPER_PRINT";

    // Report Parameters

    /** The Constant PARAM_REPORT_CLASSLOADER. */
    public static final String PARAM_REPORT_CLASSLOADER = "REPORT_CLASS_LOADER";
    /** The Constant PARAM_REPORT_LOCALE. */
    public static final String PARAM_REPORT_LOCALE = "REPORT_LOCALE";

    /** The logger. */
    private static final Logger logger = Logger.getLogger(
            DefaultFiller.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    public final void fill(FacesContext context, UIReport component)
            throws FillerException {
        final String reportName = component.getPath();
        JRFacesContext jrContext = JRFacesContext.getInstance(context);

        JasperReport jasperReport;
        InputStream reportStream = null;
        Resource resource;
        try {
            resource = jrContext.createResource(context,
                    (UIComponent) component, reportName);
        } catch (final UnresolvedResourceException e) {
            throw new ReportNotFoundException(reportName, e);
        }

        ReportSource reportSource = null;
        Object reportSourceRef = component.getReportSource();
        if (reportSourceRef != null) {
            if (reportSourceRef instanceof ReportSource) {
                reportSource = (ReportSource) reportSourceRef;
            } else if (reportSourceRef instanceof String) {
                String dataSourceId = Util.resolveDataSourceId(context,
                        component, (String) reportSourceRef);

                reportSource = (ReportSource) context.getExternalContext()
                        .getRequestMap().get(dataSourceId);
            } else if (reportSourceRef instanceof JRDataSource) {
                reportSource = new JRDataSourceHolder(
                        (JRDataSource) reportSourceRef);
            } else if (reportSourceRef instanceof Connection) {
                reportSource = new ConnectionHolder(
                        (Connection) reportSourceRef);
            } else {
                throw new FillerException("Illegal data source value type: "
                        + reportSourceRef.getClass().getName());
            }
        }
        try {
            jasperReport = loadReportObject(resource);
        } catch (IOException ex) {
            if (logger.isLoggable(Level.SEVERE)) {
                LogRecord record = new LogRecord(Level.SEVERE, "JRJSF_0033");
                record.setParameters(new Object[]{ resource.getName() });
                record.setThrown(ex);
                logger.log(record);
            }
            throw new FillerException(ex);
        } catch (ClassNotFoundException ex) {
            if (logger.isLoggable(Level.SEVERE)) {
                LogRecord record = new LogRecord(Level.SEVERE, "JRJSF_0034");
                record.setParameters(new Object[]{ resource.getName() });
                record.setThrown(ex);
                logger.log(record);
            }
            throw new FillerException(ex);
        }

        logger.log(Level.FINE, "JRJSF_0003", reportName);

        try {
            final Map<String, Object> params =
                    buildParamMap(context, component);
            JasperPrint print = doFill(context, jasperReport,
                    params, reportSource);
            context.getExternalContext().getRequestMap()
                    .put(ATTR_JASPER_PRINT, print);
        } finally {
            try {
                reportSource.dispose();
                reportSource = null;
            } catch (Exception e) { }
            
            try {
                reportStream.close();
                reportStream = null;
            } catch (final IOException e) {
            }
        }
    }
    
    /**
     * Builds the param map.
     *
     * @param context the context
     * @param component the component
     *
     * @return the map< string, object>
     */
    protected Map<String, Object> buildParamMap(final FacesContext context,
            final UIReport component)
    throws FillerException {
        JRFacesContext jrContext = JRFacesContext.getInstance(context);

        // Build param map using component's child parameters
        final Map<String, Object> parameters = new HashMap<String, Object>();
        for (final UIComponent kid : component.getChildren()) {
            if (!(kid instanceof UIParameter)) {
                continue;
            }
            final UIParameter param = (UIParameter) kid;
            parameters.put(param.getName(), param.getValue());
        }

        // Specific component parameters
        parameters.put(PARAM_REPORT_CLASSLOADER,
                Util.getClassLoader(component));
        parameters.put(PARAM_REPORT_LOCALE,
                context.getViewRoot().getLocale());

        // Subreport directory
        final String subreportDir = component.getSubreportDir();
        if (subreportDir != null) {
            try {
                Resource resource = jrContext.createResource(context,
                            component, subreportDir);
                parameters.put("SUBREPORT_DIR", resource.getPath());
            } catch (UnresolvedResourceException e) {
                throw new FillerException(
                        "'subreportDir' is not a valid resource path", e);
            }
        }

        return parameters;
    }

    protected JasperPrint doFill(FacesContext context, JasperReport report,
            Map<String, Object> parameters, ReportSource reportSource)
    throws FillerException {
        JRBaseFiller jrFiller;
        try {
            jrFiller = JRFiller.createFiller(report);
        } catch (JRException e) {
            throw new FillerException(e);
        }
        
        JasperPrint print = null;
        try {
            if ((reportSource == null) || (reportSource.get() == null)) {
                print = jrFiller.fill(parameters);
            } else if (reportSource instanceof JRDataSourceHolder) {
                print = jrFiller.fill(parameters,
                        (JRDataSource) reportSource.get());
            } else if (reportSource instanceof ConnectionHolder) {
                print = jrFiller.fill(parameters,
                        (Connection) reportSource.get());
            }
        } catch (final JRException e) {
            throw new FillerException(e);
        }
        return print;
    }

    protected JasperReport loadReportObject(Resource resource)
            throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ContextClassLoaderObjectInputStream(
                resource.getInputStream());
        return (JasperReport) ois.readObject();
    }

}
