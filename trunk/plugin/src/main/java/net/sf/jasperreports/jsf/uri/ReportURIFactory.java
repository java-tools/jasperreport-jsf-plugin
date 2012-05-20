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
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.config.Configuration;
import net.sf.jasperreports.jsf.context.ExternalContextHelper;
import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.util.Util;

import javax.faces.context.FacesContext;
import javax.faces.render.ResponseStateManager;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ReportURIFactory {

    private static final Logger logger = Logger.getLogger(
            ReportURIFactory.class.getPackage().getName(),
            Constants.LOG_MESSAGES_BUNDLE);

    public static ReportURI createReportURI(FacesContext context, UIReport component) {
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
}
