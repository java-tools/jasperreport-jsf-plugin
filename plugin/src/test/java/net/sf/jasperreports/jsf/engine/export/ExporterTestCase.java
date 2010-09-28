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
package net.sf.jasperreports.jsf.engine.export;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockFacesServletEnvironment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author aalonsodominguez
 */
public abstract class ExporterTestCase {

    /** Properties to be tested in the exporter instance. */
    private Properties properties;
    
    private MockFacesEnvironment facesEnv;
    
    @Before
    public void initEnvironment() {
        facesEnv = new MockFacesServletEnvironment();
    }

    @After
    public void disposeEnvironment() {
        facesEnv.release();
        facesEnv = null;
    }

    @Before
    public void loadExporterProperties() throws Exception {
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        if (classLoader == null) {
            classLoader = ExporterTestCase.class.getClassLoader();
        }

        String resource = getClass().getName().replaceAll("\\.", "/")
                + ".properties";
        InputStream stream = classLoader.getResourceAsStream(resource);
        if (stream == null) {
            throw new Exception("Couldn't found test resource: " + resource);
        }

        properties = new Properties();
        properties.load(stream);
    }

    public abstract String getExpectedContentType();

    public abstract Class<? extends JRExporter> getExpectedJRExporterClass();

    public abstract Class<? extends DefaultExporter> getExporterUnderTestClass();

    @Test
    public void createJRExporterWithAttributes() throws Exception {
        Class<? extends DefaultExporter> exporterClass =
                getExporterUnderTestClass();
        DefaultExporter exporter = exporterClass.newInstance();
        UIReport component = createComponent();

        JRExporter jrExporter = exporter.createJRExporter(
                facesEnv.getFacesContext(), component);
        assertThat(jrExporter, notNullValue());
        assertThat(jrExporter, is(getExpectedJRExporterClass()));
        assertThat(jrExporter.getParameters().size(),
                equalTo(properties.size()));
    }

    @Test
    public void offersProperContentType() throws Exception {
        Class<? extends DefaultExporter> exporterClass =
                getExporterUnderTestClass();
        DefaultExporter exporter = exporterClass.newInstance();

        String contentType = exporter.getContentType();
        assertThat(contentType, notNullValue());
        assertThat(contentType, equalTo(getExpectedContentType()));
    }

    protected UIReport createComponent() {
        UIReport report = new UIReport();
        Enumeration e = properties.propertyNames();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            String value = properties.getProperty(name);
            report.getAttributes().put(name, value);
        }
        return report;
    }

}