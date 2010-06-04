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
package net.sf.jasperreports.jsf.engine.source;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.jsf.component.UISource;
import net.sf.jasperreports.jsf.engine.Source;
import net.sf.jasperreports.jsf.engine.SourceException;
import net.sf.jasperreports.jsf.resource.ClasspathResource;
import net.sf.jasperreports.jsf.resource.ResourceResolver;
import net.sf.jasperreports.jsf.test.JMockTheories;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockJRFacesContext;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.Matchers.*;
import static net.sf.jasperreports.jsf.test.Matchers.*;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(JMockTheories.class)
public class CsvSourceConverterTest {

    @DataPoint
    public static final Object NULL_DATA = null;

    @DataPoint
    public static final String VALID_RESOURCE = ClasspathResource.PREFIX +
            CsvSourceConverterTest.class.getName()
            .replaceAll("\\.", "/") + ".csv";

    @DataPoint
    public static final String INVALID_RESOURCE = ClasspathResource.PREFIX +
            CsvSourceConverterTest.class.getName()
            .replaceAll("\\.", "/");

    @DataPoint
    public static final String INVALID_URL_STR =
            "ftp://jasperreportjsf.sourceforge.net/invalid/location";

    @DataPoint
    public static final URL validUrl() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResource(VALID_RESOURCE);
    }

    @DataPoint
    public static final URL invalidUrl() {
        try {
            return new URL(INVALID_URL_STR);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static final Logger logger = Logger.getLogger(
            CsvSourceConverterTest.class.getPackage().getName());

    private Mockery mockery = new JUnit4Mockery();

    private MockFacesEnvironment facesEnv;

    private UISource component;
    private CsvSourceConverter factory;
    private ResourceResolver resourceResolver;

    private MockJRFacesContext jrContext;

    @Before
    public void init() {
        facesEnv = MockFacesEnvironment.getServletInstance();

        component = new UISource();
        factory = new CsvSourceConverter();
        resourceResolver = mockery.mock(ResourceResolver.class);

        jrContext = new MockJRFacesContext(facesEnv.getFacesContext());
        jrContext.setResourceResolver(resourceResolver);

        component.setId("reportSourceId");
        component.setType("csv");
    }

    @After
    public void dispose() {
        jrContext = null;

        factory = null;
        component = null;

        facesEnv.release();
        facesEnv = null;
    }

    @Theory
    public void nullDataReturnsEmptyDataSource(Object data) {
        assumeThat(data, is(nullValue()));

        logger.log(Level.INFO, "Testing null support...");

        component.setValue(data);

        Source source = factory.createSource(
                facesEnv.getFacesContext(), component, data);
        assertThat(source, is(not(nullValue())));

        JRDataSource dataSource = ((JRDataSourceHolder) source).getDataSource();
        assertThat(dataSource, is(not(nullValue())));
        assertThat(dataSource, is(JREmptyDataSource.class));
    }

    @Theory
    public void invalidUrlThrowsSourceEx(URL data) {
        assumeThat(data, is(not(nullValue())));
        assumeThat(data, not(existsURL()));

        logger.log(Level.INFO,
                "Testing invalid url support (using a java.net.URL object)...");

        component.setValue(data);

        try {
            Source source = factory.createSource(
                    facesEnv.getFacesContext(), component, data);
            fail("A report source exception should be thrown");
        } catch (SourceException e) {
            Throwable cause = e.getCause();
            assertThat(cause, is(not(nullValue())));
            assertThat(cause, is(IOException.class));
        }
    }

    @Theory
    public void unexistantFileThrowsSourceEx() {

    }

    public void whenIOExCatchItAndThrowSourceEx() {

    }

}
