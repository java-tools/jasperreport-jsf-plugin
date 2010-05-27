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

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.ResourceException;
import net.sf.jasperreports.jsf.resource.UnresolvedResourceException;
import net.sf.jasperreports.jsf.util.Services;

/**
 * The Class ResourceLoader.
 */
public final class ResourceLoader {

    private static final ResourceResolver DEFAULT_RESOLVER =
            new DefaultResourceResolver();

    /**
     * Gets the resource loader.
     *
     * @param context the context
     * @param name the name
     *
     * @return the resource loader
     */
    public static Resource getResource(final FacesContext context,
            final UIComponent component, final String name)
    throws IOException, ResourceException {
        if ((name == null) || (name.length() == 0)) {
            throw new IllegalArgumentException();
        }

        final ResourceResolver resolver = Services.chain(ResourceResolver.class,
                DEFAULT_RESOLVER);
        Resource resource = resolver.resolveResource(context, component, name);
        if (resource == null) {
            throw new UnresolvedResourceException(name);
        }
        return resource;
    }

    private ResourceLoader() { }
    
}