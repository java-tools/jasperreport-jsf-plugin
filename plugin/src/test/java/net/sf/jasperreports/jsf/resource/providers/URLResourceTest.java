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

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(Parameterized.class)
public class URLResourceTest {

    @Parameters
    public static Collection<?> createData() throws Exception {
        Object[][] array = new Object[][] {
            { new URL("http://jasperreportjsf.sourceforge.net/tld/jasperreports-jsf-1_0.tld") }
        };
        return Arrays.asList(array);
    }

    private URL location;
    private URLResource resource;

    public URLResourceTest(URL location) {
        this.location = location;
    }

    @Before
    public void createResource() throws Exception {
        resource = new URLResource(location);
    }

    @Test
    public void getName() {
        String resName = resource.getName();
        assertNotNull(resName);
        assertThat(location.toString(), equalTo(resName));
    }

    @Test
    public void getLocation() throws Exception {
        URL loc = resource.getLocation();
        assertNotNull(loc);
        assertThat(location, equalTo(loc));
    }

    @Test
    public void getPath() {
        String path = resource.getPath();
        assertNotNull(path);
        assertThat(location.getPath(), equalTo(path));
    }

}
