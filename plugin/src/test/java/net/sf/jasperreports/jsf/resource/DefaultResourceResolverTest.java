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

import net.sf.jasperreports.jsf.component.html.HtmlReportFrame;
import net.sf.jasperreports.jsf.test.JMockTheories;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static net.sf.jasperreports.jsf.test.Matchers.*;
import static org.junit.Assume.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author antonio.alonso
 */
@RunWith(JMockTheories.class)
public class DefaultResourceResolverTest {

    @DataPoint
    public static final String URL_RESOURCE =
            "http://jasperreportjsf.sourceforge.net/tld/" +
            "jasperreports-jsf-1_0.tld";

    @DataPoint
    public static final String CLASSPATH_RESOURCE = ClasspathResource.PREFIX +
            ClasspathResourceTest.JAVA_RESOURCE;

    private MockFacesEnvironment facesEnv;

    private HtmlReportFrame component;
    private DefaultResourceResolver resolver;

    private Mockery mockery = new JUnit4Mockery();

    @Before
    public void init() {
        facesEnv = MockFacesEnvironment.getServletInstance();

        component = new HtmlReportFrame();
        resolver = new DefaultResourceResolver();
    }

    @Theory
    public void urlResource(String resourceName) {
        assumeThat(resourceName, is(url()));

        Resource res = resolver.resolveResource(facesEnv.getFacesContext(),
                component, resourceName);
        
        assertThat(res, is(not(nullValue())));
        assertThat(res, is(URLResource.class));
        assertThat(res.getName(), equalTo(resourceName));
    }

    @Theory
    public void whenClasspathResource(String resourceName) {
        assumeThat(resourceName, is(classpathResource()));

        Resource res = resolver.resolveResource(facesEnv.getFacesContext(),
                component, resourceName);

        assertThat(res, is(not(nullValue())));
        assertThat(res, is(ClasspathResource.class));
        assertThat(res.getName(), equalTo(resourceName.substring(
                ClasspathResource.PREFIX.length())));
    }
    
}
