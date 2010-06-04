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
package net.sf.jasperreports.jsf.test.mock;

import java.util.HashSet;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UISource;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.context.DefaultJRFacesContext;
import net.sf.jasperreports.jsf.context.ExternalContextHelper;
import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.convert.SourceConverter;
import net.sf.jasperreports.jsf.engine.Exporter;
import net.sf.jasperreports.jsf.engine.Filler;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.ResourceResolver;
import net.sf.jasperreports.jsf.validation.Validator;

/**
 *
 * @author aalonsodominguez
 */
public class MockJRFacesContext extends JRFacesContext {

    private DefaultJRFacesContext defaultContext;

    private Set<String> availableExportFormats = new HashSet<String>();
    private Set<String> availableDataSourceTypes = new HashSet<String>();

    private SourceConverter sourceConverter;
    private ResourceResolver resourceResolver;

    private Filler filler;
    private Exporter exporter;

    private Validator reportSourceValidator;
    private Validator reportValidator;

    public MockJRFacesContext(FacesContext context) {
        context.getExternalContext().getApplicationMap()
                .put(INSTANCE_KEY, this);
    }

    public SourceConverter getSourceConverter() {
        return sourceConverter;
    }

    public void setSourceConverter(SourceConverter sourceConverter) {
        this.sourceConverter = sourceConverter;
    }

    public ResourceResolver getResourceResolver() {
        return resourceResolver;
    }

    public void setResourceResolver(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
    }

    public Validator getReportSourceValidator() {
        return reportSourceValidator;
    }

    public void setReportSourceValidator(Validator reportSourceValidator) {
        this.reportSourceValidator = reportSourceValidator;
    }

    public Validator getReportValidator() {
        return reportValidator;
    }

    public void setReportValidator(Validator reportValidator) {
        this.reportValidator = reportValidator;
    }
    
    public Resource createResource(
            FacesContext context, UIComponent component, String name) {
        if (resourceResolver != null) {
            return resourceResolver.resolveResource(context, component, name);
        }
        return defaultContext.createResource(context, component, name);
    }

    public Filler getFiller(
            FacesContext context, UIReport component) {
        if (filler != null) {
            return filler;
        }
        return defaultContext.getFiller(context, component);
    }

    public void setFiller(Filler filler) {
        this.filler = filler;
    }

    public ExternalContextHelper getExternalContextHelper(
            FacesContext context) {
        return defaultContext.getExternalContextHelper(context);
    }

    public Exporter getExporter(
            FacesContext context, UIReport component) {
        if (exporter != null) {
            return exporter;
        }
        return defaultContext.getExporter(context, component);
    }

    public void setExporter(Exporter exporter) {
        this.exporter = exporter;
    }

    public SourceConverter createSourceConverter(
            FacesContext context, UIComponent component) {
        if (sourceConverter != null) {
            return sourceConverter;
        }
        return defaultContext.createSourceConverter(context, component);
    }

    public Set<String> getAvailableExportFormats() {
        return availableExportFormats;
    }

    public void setAvailableExportFormats(Set<String> availableExportFormats) {
        this.availableExportFormats = availableExportFormats;
    }

    public Set<String> getAvailableSourceTypes() {
        return availableDataSourceTypes;
    }

    public void setAvailableDataSourceTypes(Set<String> availableDataSourceTypes) {
        this.availableDataSourceTypes = availableDataSourceTypes;
    }

    @Override
    public Validator createValidator(FacesContext context,
            UISource component) {
        if (reportSourceValidator != null) {
            return reportSourceValidator;
        }
        return defaultContext.createValidator(context, component);
    }

    @Override
    public Validator createValidator(FacesContext context, UIReport component) {
        if (reportValidator == null) {
            return reportValidator;
        }
        return defaultContext.createValidator(context, component);
    }
    
}
