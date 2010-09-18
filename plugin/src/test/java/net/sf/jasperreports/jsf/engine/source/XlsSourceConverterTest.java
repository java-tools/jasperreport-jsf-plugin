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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRXlsDataSource;
import net.sf.jasperreports.jsf.component.UISource;
import net.sf.jasperreports.jsf.engine.Source;
import net.sf.jasperreports.jsf.engine.SourceException;
import net.sf.jasperreports.jsf.resource.ClasspathResource;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.ResourceResolver;
import net.sf.jasperreports.jsf.test.JMockTheories;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockFacesServletEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockJRFacesContext;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
public class XlsSourceConverterTest {

    @DataPoint
    public static final Object NULL_DATA = null;

    @DataPoint
    public static final Integer INVALID_DATA = 0x000;

    //@DataPoint
    //public static final InputStream STREAM_DATA =
    //        new ByteArrayInputStream(new byte[0]);

    @DataPoint
    public static final String VALID_RESOURCE = ClasspathResource.PREFIX +
            XlsSourceConverterTest.class.getName()
            .replaceAll("\\.", "/") + ".xls";

    @DataPoint
    public static final String INVALID_RESOURCE = ClasspathResource.PREFIX +
            XlsSourceConverterTest.class.getName()
            .replaceAll("\\.", "/") + "_INVALID";

    @DataPoint
    public static final File INVALID_FILE = new File(INVALID_RESOURCE);

    @DataPoint
    public static File VALID_FILE() {
        return new File(facesEnv.getDocumentRoot(), "WEB-INF/web.xml");
    }

    @DataPoint
    public static final String INVALID_URL_STR =
            "ftp://jasperreportjsf.sourceforge.net/invalid/location";

    @DataPoint
    public static URL VALID_URL() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResource(VALID_RESOURCE);
    }

    @DataPoint
    @SuppressWarnings("unused")
    public static URL INVALID_URL() {
        try {
            return new URL(INVALID_URL_STR);
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    private static MockFacesEnvironment facesEnv;

    @BeforeClass
    public static void initTest() {
        facesEnv = new MockFacesServletEnvironment();
    }

    @AfterClass
    public static void disposeTest() {
        facesEnv.release();
        facesEnv = null;
    }

    private Mockery mockery = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    private ClassLoader classLoader;
    private UISource component;
    private XlsSourceConverter factory;
    private ResourceResolver resourceResolver;

    private MockJRFacesContext jrContext;

    @Before
    public void init() {
        classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = CsvSourceConverterTest.class.getClassLoader();
        }

        component = new UISource();
        factory = new XlsSourceConverter();
        resourceResolver = mockery.mock(ResourceResolver.class);

        jrContext = new MockJRFacesContext(facesEnv.getFacesContext());
        jrContext.setResourceResolver(resourceResolver);

        component.setId("reportSourceId");
        component.setType("xls");
    }

    @After
    public void dispose() {
        jrContext = null;
        factory = null;
        component = null;
    }

    @Theory
    public void nullDataReturnsEmptyDataSource(Object data) {
        assumeThat(data, is(nullValue()));

        component.setValue(data);

        Source source = factory.createSource(
                facesEnv.getFacesContext(), component, data);
        assertThat(source, is(not(nullValue())));

        JRDataSource dataSource = ((JRDataSourceWrapper) source).getDataSource();
        assertThat(dataSource, is(not(nullValue())));
        assertThat(dataSource, is(JREmptyDataSource.class));
    }

    @Theory
    @SuppressWarnings("unused")
    public void invalidDataThrowsSourceEx(Object data) {
        assumeThat(data, notNullValue());
        assumeThat(data, not(anyOf(
                instanceOf(URL.class),
                instanceOf(String.class),
                instanceOf(InputStream.class),
                instanceOf(File.class)
        )));

        Source source;
        try {
            source = factory.createSource(facesEnv.getFacesContext(),
                    component, data);
            fail("A SourceException should be thrown");
        } catch (Exception e) {
            assertThat(e, is(SourceException.class));
        }
    }

    //@Theory
    public void validResourceReturnsJRDataSource(final Object resource)
            throws Exception {
        assumeThat(resource, is(not(nullValue())));
        assumeThat(resource, anyOf(
                instanceOf(URL.class),
                instanceOf(String.class),
                instanceOf(InputStream.class),
                instanceOf(File.class)
        ));
        if (resource instanceof String) {
            assumeThat((String) resource,
                    anyOf(not(unexistantResource(classLoader)), validURL()));
        }
        if (resource instanceof URL) {
            assumeThat((URL) resource, existsURL());
        }
        if (resource instanceof File) {
            assumeTrue(((File) resource).exists());
        }

        final Resource resourceObj = mockery.mock(Resource.class);
        final InputStream stream = new ByteArrayInputStream(new byte[0]);

        component.setValue(resource);

        final Expectations builder = new Expectations();
        if (resource instanceof String) {
            builder.oneOf(resourceResolver).resolveResource(
                    facesEnv.getFacesContext(), component, (String) resource);
            builder.will(Expectations.returnValue(resourceObj));

            builder.oneOf(resourceObj).getInputStream();
            builder.will(Expectations.returnValue(stream));
        }

        mockery.checking(builder);

        Source source;
        try {
            source = factory.createSource(facesEnv.getFacesContext(),
                    component, resource);
        } catch (SourceException e) {
            fail("No exception is expected.");
            return;
        }

        assertThat(source, is(not(nullValue())));
        assertThat(source, is(JRDataSourceWrapper.class));

        JRDataSource dataSource = ((JRDataSourceWrapper) source).getDataSource();
        assertThat(dataSource, is(not(nullValue())));
        assertThat(dataSource, is(JRXlsDataSource.class));
    }

}
