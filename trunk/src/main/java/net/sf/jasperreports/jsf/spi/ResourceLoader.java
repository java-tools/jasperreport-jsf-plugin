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
package net.sf.jasperreports.jsf.spi;

import java.io.IOException;
import java.util.Collection;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.ResourceException;
import net.sf.jasperreports.jsf.util.Util;

/**
 * The Class ResourceLoader.
 */
public final class ResourceLoader {

	private static final Collection<ResourceFactory> resourceFactoryCache = Util
			.loadServiceSet(ResourceFactory.class);

	/**
	 * Gets the resource loader.
	 * 
	 * @param context
	 *            the context
	 * @param name
	 *            the name
	 * 
	 * @return the resource loader
	 */
	public static Resource getResource(final FacesContext context,
			final String name) throws IOException, ResourceException {
		if ((name == null) || (name.length() == 0)) {
			throw new IllegalArgumentException();
		}

		final ResourceFactory factory = getResourceFactory(name);
		if (factory == null) {
			throw new ResourceFactoryNotFoundException(
					"No factory for resource: " + name);
		}
		return factory.createResource(context, name);
	}

	private static ResourceFactory getResourceFactory(final String name)
			throws ResourceException {
		for (final ResourceFactory factory : resourceFactoryCache) {
			if (factory.acceptsResource(name)) {
				return factory;
			}
		}
		return null;
	}

	private ResourceLoader() {
	}

}
