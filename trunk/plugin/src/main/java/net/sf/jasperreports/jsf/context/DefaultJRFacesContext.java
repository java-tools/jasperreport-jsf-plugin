/*
 * JaspertReports JSF Plugin Copyright (C) 2011 A. Alonso Dominguez
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

import net.sf.jasperreports.jsf.component.UISource;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.engine.source.SourceConverterBase;
import net.sf.jasperreports.jsf.convert.SourceConverter;
import net.sf.jasperreports.jsf.engine.Exporter;
import net.sf.jasperreports.jsf.engine.ExporterException;
import net.sf.jasperreports.jsf.engine.export.ExporterNotFoundException;
import net.sf.jasperreports.jsf.engine.fill.DefaultFiller;
import net.sf.jasperreports.jsf.engine.Filler;
import net.sf.jasperreports.jsf.resource.DefaultResourceResolver;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.ResourceResolver;
import net.sf.jasperreports.jsf.resource.UnresolvedResourceException;
import net.sf.jasperreports.jsf.util.Services;

import static net.sf.jasperreports.jsf.util.ComponentUtil.*;

/**
 * Default implementation for the <tt>JRFacesContext</tt> abstract class.
 *
 * @author A. Alonso Dominguez
 */
public final class DefaultJRFacesContext extends JRFacesContext {

    /** Default filler instance. */
    private static final Filler DEFAULT_FILLER = new DefaultFiller();

    /** Default resource resolved instance. */
    private static final ResourceResolver DEFAULT_RESOURCE_RESOLVER =
            new DefaultResourceResolver();

    /** Plugin external context helper. */
    private ExternalContextHelper externalContext = null;

    /** Source converter map. */
    private final Map<String, SourceConverter> sourceConverterMap;
    /** Exporter map. */
    private final Map<String, Exporter> exporterMap;
    /** Current fillter instance. */
    private final Filler filler;
    /** Current resource resolver instance. */
    private final ResourceResolver resourceResolver;

    /**
     * Default constructor.
     */
    protected DefaultJRFacesContext() {
        sourceConverterMap = Services.map(SourceConverter.class);
        exporterMap = Services.map(Exporter.class);
        filler = Services.chain(Filler.class, DEFAULT_FILLER);
        resourceResolver = Services.chain(
                ResourceResolver.class, DEFAULT_RESOURCE_RESOLVER);
    }

    /**
     * Collection of available source types.
     *
     * @return collection of available source types.
     */
    public Set<String> getAvailableSourceTypes() {
        return sourceConverterMap.keySet();
    }

    /**
     * Collection of availbale export formats.
     *
     * @return collection of available export formats.
     */
    public Set<String> getAvailableExportFormats() {
        return exporterMap.keySet();
    }

    /**
     * Obtains the external context helper instance.
     *
     * @param context current faces' context.
     * @return the external context helper.
     */
    @Override
    public ExternalContextHelper getExternalContextHelper(
            final FacesContext context) {
        if (externalContext == null) {
            externalContext = ExternalContextHelper.newInstance(
                    context.getExternalContext());
        }
        return externalContext;
    }

    /**
     * Instantiates a source converter appropiate for the given component.
     *
     * @param context current faces' context.
     * @param component the component which is asking the source converter.
     * @return a source converter instance.
     */
    @Override
    public SourceConverter createSourceConverter(
            final FacesContext context,
            final UIComponent component) {
        SourceConverter converter = null;
        if (component instanceof UISource) {
            String type = getStringAttribute(component, "type", null);
            if (type != null) {
                converter = sourceConverterMap.get(type);
            }
        }

        if (converter == null) {
            converter = new SourceConverterBase();
        }
        return converter;
    }

    /**
     * Creates a new resource instance.
     *
     * @param context current faces' context.
     * @param component a report component.
     * @param name resource name.
     * @return a resource instance.
     */
    @Override
    public Resource createResource(final FacesContext context,
            final UIComponent component, final String name) {
        Resource resource = resourceResolver.resolveResource(
                context, component, name);
        if (resource == null) {
            throw new UnresolvedResourceException(name);
        }
        return resource;
    }

    /**
     * Obtains the report's filler instance.
     *
     * @param context current faces' context.
     * @param component the report component.
     * @return a filler instance.
     */
    @Override
    public Filler getFiller(final FacesContext context,
            final UIReport component) {
        return filler;
    }

    /**
     * Obtains the report's exporter instance.
     *
     * @param context current faces' context.
     * @param component the report component
     * @return an exporter instance.
     */
    @Override
    public Exporter getExporter(final FacesContext context,
            final UIReport component) {
        String format = getStringAttribute(component, "format", null);
        if (format != null) {
            final Exporter exporter = exporterMap.get(format);
            if (exporter == null) {
                throw new ExporterNotFoundException(format);
            }
            return exporter;
        } else {
            throw new ExporterException(
                    "No exporting functionality available for component: "
                    + component.getClientId(context));
        }
    }

}