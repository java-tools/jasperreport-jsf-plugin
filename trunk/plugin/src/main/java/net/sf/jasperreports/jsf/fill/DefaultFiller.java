/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.fill;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.resource.ReportNotFoundException;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.spi.ResourceLoader;
import net.sf.jasperreports.jsf.util.Util;

/**
 *
 * @author antonio.alonso
 */
public class DefaultFiller extends Filler {

    // Report Parameters

    /** The Constant PARAM_REPORT_CLASSLOADER. */
    public static final String PARAM_REPORT_CLASSLOADER = "REPORT_CLASS_LOADER";
    /** The Constant PARAM_REPORT_LOCALE. */
    public static final String PARAM_REPORT_LOCALE = "REPORT_LOCALE";

    /** The logger. */
    private static final Logger logger = Logger.getLogger(
            DefaultFiller.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    protected JRDataSource getReportDataSource(
            FacesContext context, UIReport report) throws FillerException {
        JRDataSource result = null;

        Object dataSourceRef = report.getDataSource();
        if (dataSourceRef == null) {
            return new JREmptyDataSource();
        } else if (dataSourceRef instanceof String) {
            String dataSourceId = Util.resolveDataSourceId(context, report,
                    (String) dataSourceRef);
            result = (JRDataSource) context.getExternalContext()
                    .getRequestMap().get(dataSourceId);
        } else if (dataSourceRef instanceof JRDataSource) {
            result = (JRDataSource) dataSourceRef;
        } else {
            throw new FillerException("Illegal data source value type: " +
                    dataSourceRef.getClass().getName());
        }
        return result;
    }

    public JasperPrint fill(FacesContext context, UIReport report)
            throws FillerException {
        final String reportName = report.getPath();

        InputStream reportStream = null;
        try {
            final Resource resource = ResourceLoader.getResource(context,
                    (UIComponent) report, reportName);
            reportStream = resource.getInputStream();
        } catch (final IOException e) {
            throw new FillerException(reportName, e);
        }

        if (reportStream == null) {
            throw new ReportNotFoundException(reportName);
        }
        logger.log(Level.FINE, "JRJSF_0003", reportName);

        try {
            final JRDataSource dataSource = getReportDataSource(context, report);
            final Map<String, Object> params = buildParamMap(context, report);
            return doFill(context, reportStream, dataSource, params);
        } finally {
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
     * @param report the report
     *
     * @return the map< string, object>
     */
    protected Map<String, Object> buildParamMap(final FacesContext context,
            final UIReport report) throws FillerException {
        // Build param map using component's child parameters
        final Map<String, Object> parameters = new HashMap<String, Object>();
        for (final UIComponent component : report.getChildren()) {
            if (!(component instanceof UIParameter)) {
                continue;
            }
            final UIParameter param = (UIParameter) component;
            parameters.put(param.getName(), param.getValue());
        }

        // Specific report parameters
        parameters.put(PARAM_REPORT_CLASSLOADER, Util.getClassLoader(report));
        parameters.put(PARAM_REPORT_LOCALE, context.getViewRoot().getLocale());

        // Subreport directory
        final String subreportDir = report.getSubreportDir();
        if (subreportDir != null) {
            Resource resource = null;
            try {
                resource = ResourceLoader.getResource(context,
                        report, subreportDir);
            } catch (final IOException e) {
                throw new FillerException(e);
            }
            parameters.put("SUBREPORT_DIR", resource.getPath());
        }

        return parameters;
    }

    /**
     * Do fill.
     *
     * @param context the context
     * @param reportStream the report stream
     * @param params the params
     *
     * @return the jasper print
     *
     * @throws FillerException the filler exception
     */
    protected JasperPrint doFill(FacesContext context,
            InputStream reportStream, JRDataSource dataSource,
            Map<String, Object> params)
            throws FillerException {
        try {
            return JasperFillManager.fillReport(reportStream, params,
                    dataSource);
        } catch (final JRException e) {
            throw new FillerException(e);
        }
    }
}
