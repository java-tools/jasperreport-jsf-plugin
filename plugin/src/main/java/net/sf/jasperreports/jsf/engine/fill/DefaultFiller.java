/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.engine.fill;

import java.io.IOException;
import java.io.InputStream;
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
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.engine.datasource.DataBroker;
import net.sf.jasperreports.jsf.engine.datasource.JRDataSourceBroker;
import net.sf.jasperreports.jsf.engine.datasource.SqlConnectionBroker;
import net.sf.jasperreports.jsf.resource.ReportNotFoundException;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.UnresolvedResourceException;
import net.sf.jasperreports.jsf.resource.ResourceLoader;
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

    public void fill(FacesContext context, UIReport component)
            throws FillerException {
        final String reportName = component.getPath();

        InputStream reportStream = null;
        try {
            final Resource resource = ResourceLoader.getResource(context,
                    (UIComponent) component, reportName);
            reportStream = resource.getInputStream();
        } catch (final IOException e) {
            throw new FillerException(reportName, e);
        } catch (final UnresolvedResourceException e) {
            throw new ReportNotFoundException(reportName, e);
        }

        DataBroker dataBroker = null;
        Object dataSourceRef = component.getDataBroker();
        if (dataSourceRef != null) {
            if (dataSourceRef instanceof DataBroker) {
                dataBroker = (DataBroker) dataSourceRef;
            } else if (dataSourceRef instanceof String) {
                String dataSourceId = Util.resolveDataSourceId(context,
                        component, (String) dataSourceRef);

                dataBroker = (DataBroker) context.getExternalContext()
                        .getRequestMap().get(dataSourceId);
            } else if (dataSourceRef instanceof JRDataSource) {
                dataBroker = new JRDataSourceBroker(
                        (JRDataSource) dataSourceRef);
            } else if (dataSourceRef instanceof Connection) {
                dataBroker = new SqlConnectionBroker(
                        (Connection) dataSourceRef);
            } else {
                throw new FillerException("Illegal data source value type: "
                        + dataSourceRef.getClass().getName());
            }
        }

        logger.log(Level.FINE, "JRJSF_0003", reportName);

        try {
            final Map<String, Object> params =
                    buildParamMap(context, component);
            JasperPrint print = doFill(context, reportStream,
                    params, dataBroker);
            context.getExternalContext().getRequestMap()
                    .put(ATTR_JASPER_PRINT, print);
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
     * @param component the component
     *
     * @return the map< string, object>
     */
    protected Map<String, Object> buildParamMap(final FacesContext context,
            final UIReport component) throws FillerException {
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
            Resource resource = null;
            try {
                resource = ResourceLoader.getResource(context,
                        component, subreportDir);
            } catch (final IOException e) {
                throw new FillerException(e);
            }
            parameters.put("SUBREPORT_DIR", resource.getPath());
        }

        return parameters;
    }

    protected JasperPrint doFill(FacesContext context, InputStream reportStream,
            Map<String, Object> parameters, DataBroker dataBroker)
    throws FillerException {
        JasperPrint print = null;
        try {
            if (dataBroker instanceof JRDataSourceBroker) {
                print = JasperFillManager.fillReport(reportStream, parameters,
                        ((JRDataSourceBroker) dataBroker).getDataSource());
            } else if (dataBroker instanceof SqlConnectionBroker) {
                print = JasperFillManager.fillReport(reportStream, parameters,
                        ((SqlConnectionBroker) dataBroker).getConnection());
            }
        } catch (final JRException e) {
            throw new FillerException(e);
        }
        return print;
    }

}
