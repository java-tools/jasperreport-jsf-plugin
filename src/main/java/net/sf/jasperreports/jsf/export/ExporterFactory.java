/*
 * JaspertReports JSF Plugin Copyright (C) 2008 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.export;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.util.Util;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Exporter objects.
 */
public final class ExporterFactory {

    /** The Constant SERVICES_RESOURCE. */
    private static final String SERVICES_RESOURCE = "META-INF/services/"
            + AbstractExporter.class.getName();

    /** The exporter cache map. */
    private static Map<String, Class<Exporter>> exporterCacheMap;

    /**
     * Gets the single instance of Exporter.
     * 
     * @param report the report
     * @param context the context
     * 
     * @return single instance of Exporter
     * 
     * @throws ExporterException the exporter exception
     */
    public static Exporter getExporter(final FacesContext context,
            final UIReport report) throws ExporterException {
        if (!(report instanceof UIComponent)) {
            throw new IllegalArgumentException();
        }

        if (exporterCacheMap == null) {
            exporterCacheMap = Util.loadServiceMap(SERVICES_RESOURCE);
        }

        final Class<Exporter> exporterClass = exporterCacheMap.get(report
                .getFormat());
        if (exporterClass == null) {
            throw new ExporterNotFoundException(report.getFormat());
        }

        Exporter result;
        try {
            result = exporterClass.newInstance();
        } catch (final Exception e) {
            throw new ExporterException(e);
        }
        result.setComponent((UIComponent) report);
        return result;
    }

    /**
     * Instantiates a new exporter factory.
     */
    private ExporterFactory() {}

}
