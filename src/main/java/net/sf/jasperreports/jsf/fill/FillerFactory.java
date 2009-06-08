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

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.util.Util;

/**
 * A factory for creating Filler objects.
 */
public final class FillerFactory {

	private static final Logger logger = Logger.getLogger(
			FillerFactory.class.getPackage().getName(),
			"net.sf.jasperreports.jsf.LogMessages");
	
    /** The exporter cache map. */
    private static final Map<String, Class<Filler>> fillerCacheMap =
    	Util.loadServiceMap(Filler.class);

    /** The Constant DEFAULT_FILLER_INSTANCE. */
    private static final Filler DEFAULT_FILLER_INSTANCE = new StaticFiller();

    public static Set<String> getAvailableDataSourceTypes() {
    	return Collections.unmodifiableSet(fillerCacheMap.keySet());
    }
    
    /**
     * Gets the filler.
     * 
     * @param context the context
     * @param report the report
     * 
     * @return the filler
     * 
     * @throws FillerException the filler exception
     */
    public static Filler getFiller(final FacesContext context,
            final UIReport report) throws FillerException {
        if (!(report instanceof UIComponent)) {
            throw new IllegalArgumentException();
        }

        Filler result;
        final UIDataSource dataSource = getDataSourceComponent(context,
                report);
        if (dataSource == null) {
            result = DEFAULT_FILLER_INSTANCE;
        } else {
            final Class<Filler> fillerClass;
            // When 'driverClass' property has been set, 
            // override dataSource type with 'jdbc' value
            if (dataSource.getDriverClass() != null) {
            	dataSource.setType("jdbc");
            }
            fillerClass = fillerCacheMap.get(dataSource.getType());
            if (fillerClass == null) {
                throw new FillerNotFoundException(dataSource.getType());
            }

            try {
                result = fillerClass.newInstance();
            } catch (final Exception e) {
                throw new FillerException(e);
            }
            result.setDataSourceComponent(dataSource);
        }
        return result;
    }

    /**
     * Gets the data source component.
     * 
     * @param context the context
     * @param report the report
     * 
     * @return the data source component
     */
    private static UIDataSource getDataSourceComponent(
            final FacesContext context, final UIReport report) {
        UIDataSource dataSource = null;
        for (final UIComponent child : ((UIComponent) report).getChildren()) {
            if (child instanceof UIDataSource) {
                dataSource = (UIDataSource) child;
                break;
            }
        }
        if ((dataSource == null) && (report.getDataSource() != null)) {
            final String dataSourceId = report.getDataSource();
            dataSource = (UIDataSource) ((UIComponent) report)
                    .findComponent(dataSourceId);
            if (dataSource == null) {
                UIComponent container = (UIComponent) report;
                while (!(container instanceof NamingContainer)) {
                    container = container.getParent();
                }
                dataSource = (UIDataSource) container
                        .findComponent(dataSourceId);
            }
        }
        if ((dataSource != null) && logger.isLoggable(Level.FINE)) {
            final String dsClientId = dataSource.getClientId(context);
            logger.log(Level.FINE, "JRJSF_0009", dsClientId);
        }
        return dataSource;
    }

    /**
     * Instantiates a new filler factory.
     */
    private FillerFactory() {}

}
