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

public final class URLResource extends AbstractResource implements Resource {

    private final URL location;

    protected URLResource(final String name, final URL location) {
        super(name);
        this.location = location;
    }

    public InputStream getInputStream() throws IOException {
        return location.openStream();
    }

    public URL getLocation() throws IOException {
        return location;
    }

    public String getPath() {
        return location.getPath();
    }
}
