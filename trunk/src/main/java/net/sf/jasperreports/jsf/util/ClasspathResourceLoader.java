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
import java.net.URL;

// TODO: Auto-generated Javadoc
/**
 * The Class ClasspathResourceLoader.
 */
public class ClasspathResourceLoader extends ResourceLoader {

    /** The Constant CLASSPATH_PREFIX. */
    public static final String CLASSPATH_PREFIX = "classpath:";

    /** The class loader. */
    private final ClassLoader classLoader;

    /**
     * Instantiates a new classpath resource loader.
     * 
     * @param classLoader the class loader
     */
    public ClasspathResourceLoader(final ClassLoader classLoader) {
        if (classLoader == null) {
            throw new IllegalArgumentException();
        }
        this.classLoader = classLoader;
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.jsf.util.ResourceLoader#getRealPath(java.lang.String
     * )
     */
    @Override
    public String getRealPath(final String name) {
        final URL resource = getResource(name);
        if (resource == null) {
            return null;
        }
        return resource.getPath();
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.jsf.util.ResourceLoader#getResource(java.lang.String
     * )
     */
    @Override
    public URL getResource(final String name) {
        if ((name == null) || (name.length() == 0)) {
            throw new IllegalArgumentException();
        }

        String resource = name;
        if (resource.startsWith(CLASSPATH_PREFIX)) {
            resource = resource.substring(CLASSPATH_PREFIX.length());
        }
        return classLoader.getResource(resource);
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.jsf.util.ResourceLoader#getResourceAsStream(java
     * .lang.String)
     */
    @Override
    public InputStream getResourceAsStream(final String name) {
        if ((name == null) || (name.length() == 0)) {
            throw new IllegalArgumentException();
        }

        String resource = name;
        if (resource.startsWith(CLASSPATH_PREFIX)) {
            resource = resource.substring(CLASSPATH_PREFIX.length());
        }
        return classLoader.getResourceAsStream(name);
    }

}
