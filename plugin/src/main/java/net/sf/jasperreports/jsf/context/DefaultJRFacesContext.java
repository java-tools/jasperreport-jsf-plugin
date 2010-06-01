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

import java.util.Map;
import java.util.Set;
import javax.faces.component.UIComponent;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReportSource;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.engine.ReportSourceFactory;
import net.sf.jasperreports.jsf.engine.ReportSource;
import net.sf.jasperreports.jsf.engine.Exporter;
import net.sf.jasperreports.jsf.engine.export.ExporterNotFoundException;
import net.sf.jasperreports.jsf.engine.fill.DefaultFiller;
import net.sf.jasperreports.jsf.engine.Filler;
import net.sf.jasperreports.jsf.resource.DefaultResourceResolver;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.ResourceException;
import net.sf.jasperreports.jsf.resource.ResourceResolver;
import net.sf.jasperreports.jsf.resource.UnresolvedResourceException;
import net.sf.jasperreports.jsf.util.Services;

/**
 *
 * @author aalonsodominguez
 */
public class DefaultJRFacesContext extends JRFacesContext {

    private static final Filler DEFAULT_FILLER = new DefaultFiller();

    private static final ResourceResolver DEFAULT_RESOURCE_RESOLVER =
            new DefaultResourceResolver();

    private ExternalContextHelper externalContext = null;

    private final Map<String, ReportSourceFactory> dataSourceFactoryMap;
    private final Map<String, Exporter> exporterMap;
    private final Filler filler;
    private final ResourceResolver resourceResolver;

    protected DefaultJRFacesContext() {
        dataSourceFactoryMap = Services.map(ReportSourceFactory.class);
        exporterMap = Services.map(Exporter.class);
        filler = Services.chain(Filler.class, DEFAULT_FILLER);
        resourceResolver = Services.chain(
                ResourceResolver.class, DEFAULT_RESOURCE_RESOLVER);
    }

    public Set<String> getAvailableDataSourceTypes() {
        return dataSourceFactoryMap.keySet();
    }

    public Set<String> getAvailableExportFormats() {
        return exporterMap.keySet();
    }

    @Override
    public ExternalContextHelper getExternalContextHelper(FacesContext context) {
        if (externalContext == null) {
            externalContext = ExternalContextHelper.newInstance(
                    context.getExternalContext());
        }
        return externalContext;
    }

    @Override
    public ReportSource<?> getDataSource(
            FacesContext context, UIReportSource component) {
        ReportSourceFactory factory = dataSourceFactoryMap.get(component.getType());
        if (factory == null) {
            throw new IllegalStateException();
        }
        return factory.createDataSource(context, component);
    }

    @Override
    public Filler getFiller(FacesContext context, UIReport component) {
        return filler;
    }

    @Override
    public Exporter getExporter(FacesContext context, UIReport component) {
        final Exporter exporter = exporterMap.get(component.getFormat());
        if (exporter == null) {
            throw new ExporterNotFoundException(component.getFormat());
        }
        return exporter;
    }

    @Override
    public Resource getResource(FacesContext context,
            UIComponent component, String name) {
        Resource resource = resourceResolver.resolveResource(context, component, name);
        if (resource == null) {
            throw new UnresolvedResourceException(name);
        }
        return resource;
    }

}
