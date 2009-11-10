/*
 * JaspertReports JSF Plugin Copyright (C) 2009 A. Alonso Dominguez
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

import java.io.File;
import java.io.IOException;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.spi.ResourceLoader;

public class FacesFileResolver implements FileResolver {

	private final FacesContext context;
	
	public FacesFileResolver(final FacesContext context) {
		super();
		if(context == null) {
			throw new IllegalArgumentException("'context' can't be null");
		}
		this.context = context;
	}

	public File resolveFile(final String name) {
		Resource resource;
		try {
			resource = ResourceLoader.getResource(context, name);
		} catch(final IOException e) {
			throw new JRFacesException(e);
		}
		return new File(resource.getPath());
	}

}
