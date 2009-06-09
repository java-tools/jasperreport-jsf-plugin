/*
 * JaspertReports JSF Plugin Copyright (C) 2009 A. Alonso Dominguez
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

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.ResourceLoader;
import net.sf.jasperreports.jsf.util.Util;

/**
 * The Class Filler.
 */
public abstract class AbstractFiller implements Filler {

    // Report Parameters

    /** The Constant PARAM_REPORT_CLASSLOADER. */
    public static final String PARAM_REPORT_CLASSLOADER = "REPORT_CLASS_LOADER";

    /** The Constant PARAM_REPORT_LOCALE. */
    public static final String PARAM_REPORT_LOCALE = "REPORT_LOCALE";

    /** The logger. */
    private static final Logger logger = Logger.getLogger(AbstractFiller.class
            .getPackage().getName(), "net.sf.jasperreports.jsf.LogMessages");

    /** The data source component. */
    private UIDataSource dataSourceComponent;

    public AbstractFiller(UIDataSource dataSource) {
		super();
		this.dataSourceComponent = dataSource;
	}

	/**
     * Gets the data source component.
     * 
     * @return the data source component
     */
    public final UIDataSource getDataSourceComponent() {
        return dataSourceComponent;
    }

    /**
     * Fill.
     * 
     * @param context the context
     * @param report the report
     * 
     * @return the jasper print
     * 
     * @throws FillerException the filler exception
     */
    public final JasperPrint fill(final FacesContext context,
            final UIReport report) throws FillerException {
        final String reportName = report.getPath();
        
        InputStream reportStream = null;
        try {
        	final Resource resource = ResourceLoader.getResource(
        			context, reportName);
        	reportStream = resource.getInputStream();
        } catch(IOException e) {
        	throw new FillerException(reportName, e);
        }
        
        if (reportStream == null) {
            throw new ReportNotFoundException(reportName);
        }
        logger.log(Level.FINE, "JRJSF_0003", reportName);

        final Map<String, Object> params = buildParamMap(context, report);
        return doFill(context, reportStream, params);
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
    protected abstract JasperPrint doFill(FacesContext context,
            InputStream reportStream, Map<String, Object> params)
            throws FillerException;

    /**
     * Builds the param map.
     * 
     * @param context the context
     * @param report the report
     * 
     * @return the map< string, object>
     */
    protected final Map<String, Object> buildParamMap(final FacesContext context,
            final UIReport report) 
    throws FillerException {
        // Build param map using component's child parameters
        final Map<String, Object> parameters = new HashMap<String, Object>();
        for (final UIComponent component : ((UIComponent) report).getChildren()) {
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
            	resource = ResourceLoader.getResource(context, subreportDir);
            } catch(IOException e) {
            	throw new FillerException(e);
            }
            parameters.put("SUBREPORT_DIR", resource.getPath());
        }

        return parameters;
    }

}
