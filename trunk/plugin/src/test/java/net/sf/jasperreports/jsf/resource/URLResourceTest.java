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

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(Theories.class)
public class URLResourceTest {

    @DataPoints
    public static Collection<URL> createData() throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String baseName = URLResourceTest.class
                .getName().replaceAll("\\.", "/");
        URL[] array = new URL[] {
            new URL("http", "jasperreportjsf.sourceforge.net",
                    "/tld/jasperreports-jsf-1_0.tld"),
            classLoader.getResource(baseName + ".java")
        };
        return Arrays.asList(array);
    }

    @Theory
    public void checkWithRemoteResource(URL location) throws Exception {
        assumeThat(location.getProtocol(), equalTo("http"));

        URLResource resource = new URLResource(location);
        assertThat(resource.getLocation(), sameInstance(location));
        assertThat(resource.getName(), equalTo(location.toString()));
        assertThat(resource.getPath(), equalTo(location.getPath()));
    }

    @Theory
    public void checkWithLocalResource(URL location) throws Exception {
        assumeThat(location.getPath(), endsWith(".java"));

        URLResource resource = new URLResource(location);
    }

}
