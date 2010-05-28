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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
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
public class ClasspathResourceTest {

    private static final String RESOURCE_PATH = ClasspathResourceTest.class
            .getPackage().getName().replaceAll("\\.", "/");

    @DataPoint
    public static final String RESOURCE_01 = RESOURCE_PATH +
            "/" + ClasspathResourceTest.class.getSimpleName() + ".txt";
    @DataPoint
    public static final String RESOURCE_02 = RESOURCE_PATH +
            "/" + ClasspathResourceTest.class.getSimpleName() + ".java";
    @DataPoint
    public static final String RESOURCE_03 = RESOURCE_PATH +
            "/" + ClasspathResourceTest.class.getSimpleName();

    private ClassLoader classLoader;

    @Before
    public void init() {
        classLoader = Thread.currentThread().getContextClassLoader();
    }

    @Theory
    public void checkWithValidResource(String name) throws Exception {
        InputStream expectedStream = null;
        assumeTrue(null != (expectedStream = classLoader
                .getResourceAsStream(name)));
        URL expectedLocation = classLoader.getResource(name);

        ClasspathResource resource = new ClasspathResource(name, classLoader);
        assertThat(resource.getName(), sameInstance(name));

        InputStream stream = null;
        try {
            stream = resource.getInputStream();
        } catch(Exception e) {
            assumeNoException(e);
        }
        assertThat(stream, is(not(nullValue())));

        
        URL location = null;
        try {
            location = resource.getLocation();
        } catch(Exception e) {
            assumeNoException(e);
        }
        assertThat(location, is(not(nullValue())));
        assertThat(location, equalTo(expectedLocation));
        assertThat(resource.getPath(), equalTo(expectedLocation.getPath()));

        try {
            expectedStream.close();
            stream.close();
        } catch(IOException e) { }
    }

    @Theory
    public void checkWithInvalidResource(String name) throws Exception {
        InputStream expectedStream;
        assumeTrue(null == (expectedStream = classLoader
                .getResourceAsStream(name)));

        ClasspathResource resource = new ClasspathResource(name, classLoader);
        assertThat(resource.getName(), sameInstance(name));

        try {
            resource.getLocation();
            fail("'getLocation' must throw 'LocationNotFoundException'.");
        } catch(LocationNotFoundException e) { }

        try {
            resource.getPath();
            fail("'getPath' must throw 'LocationNotFoundException'.");
        } catch(LocationNotFoundException e) { }

        try {
            resource.getInputStream();
            fail("'getInputStream' must throw 'LocationNotFoundException'.");
        } catch(LocationNotFoundException e) { }
    }

}
