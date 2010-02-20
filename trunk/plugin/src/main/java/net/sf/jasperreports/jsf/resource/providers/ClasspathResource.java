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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.sf.jasperreports.jsf.resource.AbstractResource;
import net.sf.jasperreports.jsf.resource.Resource;

public final class ClasspathResource extends AbstractResource
        implements Resource {

    public static final String PREFIX = "classpath:";

    private final ClassLoader classLoader;

	public ClasspathResource(final String name, final ClassLoader classLoader) {
		super(name);
		this.classLoader = classLoader;
	}

    public InputStream getInputStream() throws IOException {
        return classLoader.getResourceAsStream(getName());
    }

    public URL getLocation() throws IOException {
        final URL location = classLoader.getResource(getName());
        if (location == null) {
            throwLocationNotFoundException();
        }
        return location;
    }

    public String getPath() {
        final URL location = classLoader.getResource(getName());
        if (location == null) {
            throwLocationNotFoundException();
        }
        return location.getPath();
    }

	public boolean isRemote() {
		return false;
	}

	private void throwLocationNotFoundException() {
		throw new IllegalStateException(
				"Resource location for classpath resource '" + getName()
						+ "' couldn't be identified.");
	}

}
