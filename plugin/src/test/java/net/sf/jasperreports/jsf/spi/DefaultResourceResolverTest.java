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
package net.sf.jasperreports.jsf.spi;

import net.sf.jasperreports.jsf.resource.URLResource;
import net.sf.jasperreports.jsf.resource.ClasspathResource;
import net.sf.jasperreports.jsf.spi.DefaultResourceResolver;
import net.sf.jasperreports.jsf.component.html.HtmlReportFrame;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.test.MockFacesEnvironment;

import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author antonio.alonso
 */
public class DefaultResourceResolverTest {

    private MockFacesEnvironment facesEnv;

    private HtmlReportFrame component;
    private DefaultResourceResolver resolver;

    private Mockery context = new Mockery();

    @Before
    public void init() {
        facesEnv = MockFacesEnvironment.getServletInstance();

        component = new HtmlReportFrame();
        resolver = new DefaultResourceResolver();
    }

    @Test
    public void urlResource() {
        String resourceName = "http://jasperreportjsf.sourceforge.net/tld/jasperreports-jsf-1_0.tld";
        Resource res = resolver.resolveResource(facesEnv.getFacesContext(),
                component, resourceName);
        assertNotNull(res);
        assertEquals(URLResource.class, res.getClass());
    }

    @Test
    public void classpathResource() {
        String resourceName = "net/sf/jasperreports/jsf/resource/providers/ClasspathResourceTest.txt";
        Resource res = resolver.resolveResource(facesEnv.getFacesContext(),
                component, resourceName);
        assertNotNull(res);
        assertEquals(ClasspathResource.class, res.getClass());
    }

}
