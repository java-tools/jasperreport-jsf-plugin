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
package net.sf.jasperreports.jsf.resource.providers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.spi.ResourceResolver;
import net.sf.jasperreports.jsf.util.ExternalContextHelper;
import net.sf.jasperreports.jsf.util.Util;

/**
 *
 * @author aalonsodominguez
 */
public class DefaultResourceResolver implements ResourceResolver {

    public Resource resolveResource(FacesContext context, String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Resource name must be provided.");
        }

        Resource resource = null;
        try {
            final URL url = new URL(name);
            resource = new URLResource(name, url);
        } catch (MalformedURLException e) {
            final ClassLoader loader = Util.getClassLoader(this);
            if (name.startsWith(ClasspathResource.PREFIX)) {
                resource = new ClasspathResource(
                        name.substring(ClasspathResource.PREFIX.length()),
                        loader);
            } else if (name.startsWith("/")) {
                resource = new ContextResource(name);
            } else if (loader.getResourceAsStream(name) != null) {
                resource = new ClasspathResource(name, loader);
            } else {
                ExternalContextHelper helper = ExternalContextHelper.getInstance(
                        context.getExternalContext());
                String rootPath = resolveCurrentPath(context);
                File resourceFile = new File(helper.getResourceRealPath(
                        context.getExternalContext(), rootPath), name);
                if(resourceFile.exists()) {
                    String absName = rootPath + name;
                    resource = new ContextResource(absName);
                }
            }
        }

        // If at this point 'resource' is null then the resource couldn't
        // be resolved by this ResourceResolver implementation
        return resource;
    }

    protected String resolveCurrentPath(FacesContext context) {
        String currentPath = "";
        String viewId = context.getViewRoot().getViewId();

        int lastSlash = viewId.lastIndexOf('/');
        if (lastSlash > 0) {
            currentPath = viewId.substring(0, lastSlash);
        }

        if (!currentPath.endsWith("/")) {
            currentPath += "/";
        }
        
        return currentPath;
    }
    
}
