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
package net.sf.jasperreports.jsf.spi;

import java.io.IOException;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.resource.Resource;

/**
 * Resource instantiator.
 * <p>
 * Helper interface used internally to create <tt>Resource</tt> instances
 * 
 * @author antonio.alonso
 * 
 */
public interface ResourceFactory {

    public boolean acceptsResource(String name);

    /**
     * Creates a new <tt>Resource</tt> instance
     *
     * @param context
     *            the current <tt>FacesContext</tt>
     * @param name
     *            the resource name
     * @return a new <tt>Resource</tt> instance
     * @throws IOException
     *             if the resource can't be loaded.
     */
    public Resource createResource(FacesContext context, String name)
            throws IOException;
}
