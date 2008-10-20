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
package net.sf.jasperreports.jsf.util;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;

// TODO: Auto-generated Javadoc
/**
 * The Class ContextResourceLoader.
 */
public class ContextResourceLoader extends ResourceLoader {

    /** The context. */
    private final ExternalContext context;

    /**
     * Instantiates a new context resource loader.
     * 
     * @param servletContext the servlet context
     */
    public ContextResourceLoader(final ExternalContext servletContext) {
        if (servletContext == null) {
            throw new IllegalArgumentException();
        }
        context = servletContext;
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.jsf.util.ResourceLoader#getRealPath(java.lang.String
     * )
     */
    @Override
    public String getRealPath(final String name) {
        final ServletContext servletContext = (ServletContext) context
                .getContext();
        return servletContext.getRealPath(name);
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.jsf.util.ResourceLoader#getResource(java.lang.String
     * )
     */
    @Override
    public URL getResource(final String name) throws MalformedURLException {
        return context.getResource(name);
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.jsf.util.ResourceLoader#getResourceAsStream(java
     * .lang.String)
     */
    @Override
    public InputStream getResourceAsStream(final String name) {
        return context.getResourceAsStream(name);
    }

}
