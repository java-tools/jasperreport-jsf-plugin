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
package net.sf.jasperreports.jsf.context;

import java.util.Set;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIDataBroker;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.engine.databroker.DataSourceFactory;
import net.sf.jasperreports.jsf.engine.databroker.DataSourceHolder;
import net.sf.jasperreports.jsf.engine.export.Exporter;
import net.sf.jasperreports.jsf.engine.fill.Filler;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.ResourceResolver;
import net.sf.jasperreports.jsf.util.Services;

/**
 *
 * @author aalonsodominguez
 */
public abstract class JRFacesContext {

    private static final String INSTANCE_KEY =
            JRFacesContext.class.getName();

    private static final JRFacesContext DEFAULT_JRFACES_CONTEXT =
            new DefaultJRFacesContext();

    public static JRFacesContext getInstance(FacesContext context) {
        JRFacesContext instance = (JRFacesContext) context.getExternalContext()
                .getApplicationMap().get(INSTANCE_KEY);
        if (instance == null) {
            instance = Services.chain(JRFacesContext.class,
                    DEFAULT_JRFACES_CONTEXT);
            context.getExternalContext().getApplicationMap()
                    .put(INSTANCE_KEY, instance);
        }
        return instance;
    }

    public abstract Set<String> getAvailableDataSourceTypes();

    public abstract Set<String> getAvailableExportFormats();

    public abstract ExternalContextHelper getExternalContextHelper(
            FacesContext context);

    public abstract DataSourceHolder<?> getDataSource(
            FacesContext context, UIDataBroker component);

    public abstract Filler getFiller(
            FacesContext context, UIReport component);

    public abstract Exporter getExporter(
            FacesContext context, UIReport component);

    public abstract Resource getResource(FacesContext context,
            UIComponent component, String name);

}
