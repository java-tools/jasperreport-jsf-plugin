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
package net.sf.jasperreports.jsf.resource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.context.ExternalContextHelper;
import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.util.Util;

import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author aalonsodominguez
 */
public class DefaultResourceResolver implements ResourceResolver {

    public Resource resolveResource(FacesContext context, UIComponent component,
            String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException(
                    "Resource name must be provided.");
        }

        Resource resource = null;
        try {
            final URL url = new URL(name);
            resource = new URLResource(url);
        } catch (MalformedURLException e) {
            final ClassLoader loader = Util.getClassLoader(this);
            if (name.startsWith(ClasspathResource.PREFIX)) {
                resource = new ClasspathResource(
                        name.substring(ClasspathResource.PREFIX.length()),
                        loader);
            } else if (name.startsWith("/")) {
                resource = new ContextResource(name);
            } else if (loader.getResource(name) != null) {
                resource = new ClasspathResource(name, loader);
            } else {
                JRFacesContext jrContext = JRFacesContext.getInstance(context);
                ExternalContextHelper helper = jrContext.getExternalContextHelper(context);
                String rootPath;
                
                if ((component != null) && (component instanceof UIReport)) {
                    // If caller component is a report-based component then try to
                    // resolve the resource relative to the report resource.

                    UIReport report = (UIReport) component;
                    rootPath = helper.getResourceRealPath(
                            context.getExternalContext(),
                            FilenameUtils.getPath(report.getPath()));
                } else {
                    // If caller component is not a report-based component or
                    // there is not any component at all, resolve the resource
                    // name relative to the current view.

                    String viewId = context.getViewRoot().getViewId();
                    rootPath = helper.getResourceRealPath(
                            context.getExternalContext(),
                            FilenameUtils.getPath(viewId));
                }

                File resourceFile = new File(FilenameUtils.normalize(
                        rootPath + name));
                if (resourceFile.exists()) {
                    resource = new FileResource(resourceFile);
                }
            }
        }
        
        // If at this point 'resource' is null then the resource couldn't
        // be resolved by this ResourceResolver implementation
        return resource;
    }

}
