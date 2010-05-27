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

import net.sf.jasperreports.jsf.resource.ClasspathResource;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author aalonsodominguez
 */
public class ClasspathResourceTest {

    private static final String RES_NAME =
            ClasspathResourceTest.class.getPackage().getName().replaceAll("\\.", "/") +
            "/" + ClasspathResourceTest.class.getSimpleName() + ".txt";

    private ClassLoader classLoader;
    private ClasspathResource resource;

    @Before
    public void createResource() {
        classLoader = Thread.currentThread().getContextClassLoader();
        resource = new ClasspathResource(RES_NAME, classLoader);
    }

    @Test
    public void getName() {
        String name = resource.getName();
        assertNotNull(name);
        assertEquals(RES_NAME, name);
    }

    @Test
    public void getLocation() throws Exception {
        URL expected = classLoader.getResource(RES_NAME);
        URL loc = resource.getLocation();
        assertNotNull(loc);
        assertEquals(expected, loc);
    }

}
