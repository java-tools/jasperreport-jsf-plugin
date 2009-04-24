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
package net.sf.jasperreports.jsf.resource;

import java.io.IOException;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.resource.provider.ClasspathResourceFactory;
import net.sf.jasperreports.jsf.resource.provider.ContextResourceFactory;
import net.sf.jasperreports.jsf.resource.provider.URLResourceFactory;

/**
 * The Class ResourceLoader.
 */
public final class ResourceLoader {

	private static final String CLASSPATH_PREFIX = "classpath:";
	
    /**
     * Gets the resource loader.
     * 
     * @param context the context
     * @param name the name
     * 
     * @return the resource loader
     */
    public static Resource getResource(final FacesContext context,
            final String name) 
    throws IOException {
        if ((name == null) || (name.length() == 0)) {
            throw new IllegalArgumentException();
        }
        
        ResourceFactory factory = getResourceFactory(name);
        return factory.createResource(context, name);
    }

    private static ResourceFactory getResourceFactory(final String name) {
    	if(name.startsWith(CLASSPATH_PREFIX)) {
    		return new ClasspathResourceFactory();
    	} else if(name.indexOf("://") > 0) {
    		return new URLResourceFactory();
    	} else {
    		return new ContextResourceFactory();
    	}
    }

    private ResourceLoader() { }
    
}
