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
package net.sf.jasperreports.jsf.fill;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.util.Util;

/**
 * A factory for creating Filler objects.
 */
public final class FillerFactory {

    /** The Constant SERVICES_RESOURCE. */
    private static final String SERVICES_RESOURCE = "META-INF/services/"
            + Filler.class.getName();

    /** The exporter cache map. */
    private static final Map<String, Class<Filler>> fillerCacheMap =
    	Util.loadServiceMap(SERVICES_RESOURCE);

    /** The Constant DEFAULT_FILLER_INSTANCE. */
    private static final Filler DEFAULT_FILLER_INSTANCE = new StaticFiller();

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
        final UIDataSource dataSource = Util.getDataSourceComponent(context,
                report);
        if (dataSource == null) {
            result = DEFAULT_FILLER_INSTANCE;
        } else {
            final Class<Filler> fillerClass = fillerCacheMap.get(dataSource
                    .getType());
            if (fillerClass == null) {
                throw new FillerNotFoundException(dataSource.getType());
            }

            try {
                result = fillerClass.newInstance();
            } catch (final Exception e) {
                throw new FillerException(e);
            }
            validate(result);
            result.setDataSourceComponent(dataSource);
        }
        return result;
    }

    /**
     * Validate.
     * 
     * @param filler the filler
     * 
     * @throws FillerException the filler exception
     */
    private static void validate(final Filler filler) throws FillerException {
        final UIDataSource dataSource = filler.getDataSourceComponent();
        for (final String attr : filler.getRequiredDataSourceAttributes()) {
            if (null == dataSource.getAttributes().get(attr)) {
                throw new MissedDataSourceAttributeException(attr);
            }
        }
    }

    /**
     * Instantiates a new filler factory.
     */
    private FillerFactory() {}

}
