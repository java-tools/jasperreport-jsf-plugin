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

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import net.sf.jasperreports.jsf.test.TestConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author antonio.alonso
 */
@RunWith(Parameterized.class)
public class FileResourceTest {

    @Parameters
    public static Collection<?> testdata() {
        File basedir = new File(
                System.getProperty(TestConstants.PROP_BASEDIR));
        Object[][] array = new Object[][]{
            { basedir },
            { new File(basedir, "pom.xml") }
        };
        return Arrays.asList(array);
    }

    private File file;
    private FileResource resource;

    public FileResourceTest(File file) {
        this.file = file;
    }

    @Before
    public void init() {
        resource = new FileResource(file);
    }

    @Test
    public void getName() {
        String name = resource.getName();
        assertThat(name, is(not(nullValue())));
        assertThat(name, equalTo(file.getName()));
    }

    @Test
    public void getLocation() throws Exception {
        URL expectedLocation = new URL("file://" + file.getAbsolutePath());
        URL location = resource.getLocation();

        assertThat(location, is(not(nullValue())));
        assertThat(location, equalTo(expectedLocation));
    }

    @Test
    public void getPath() throws Exception {
        String expectedPath = System.getProperty(TestConstants.PROP_BASEDIR);
        String path = resource.getPath();

        assertThat(path, is(not(nullValue())));
        assertThat(path, equalTo(expectedPath));
    }

}
