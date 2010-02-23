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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.resource.AbstractResource;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.util.ExternalContextHelper;

/**
 * The Class ContextResourceLoader.
 */
public class ContextResource extends AbstractResource
        implements Resource {

    /**
     * Instantiates a new context resource loader.
     *
     * @param servletContext
     *            the servlet context
     */
    protected ContextResource(final String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.jsf.util.ResourceLoader#getResource(java.lang.String
     * )
     */
    public URL getLocation() throws MalformedURLException {
        return getFacesContext().getExternalContext().getResource(getName());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.jsf.util.ResourceLoader#getResourceAsStream(java
     * .lang.String)
     */
    public InputStream getInputStream() {
        return getFacesContext().getExternalContext().getResourceAsStream(getName());
    }

    public String getPath() {
        ExternalContextHelper helper = ExternalContextHelper.getInstance(
                getFacesContext().getExternalContext());
        return helper.getResourceRealPath(
                getFacesContext().getExternalContext(), getName());
    }

    protected FacesContext getFacesContext() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null) {
            throw new IllegalStateException("Context reosurces are valid just " +
                    "inside a Faces' request.");
        }
        return context;
    }

}
