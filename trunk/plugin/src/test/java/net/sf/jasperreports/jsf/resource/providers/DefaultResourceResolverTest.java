/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.resource.providers;

import net.sf.jasperreports.jsf.component.html.HtmlReportPanel;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.test.framework.MockFacesEnvironment;

import org.jmock.Mockery;
import org.jmock.integration.testng.TestNGMockery;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author antonio.alonso
 */
public class DefaultResourceResolverTest {

    private MockFacesEnvironment facesEnv;

    private HtmlReportPanel component;
    private DefaultResourceResolver resolver;

    private Mockery context = new TestNGMockery();

    @BeforeTest
    public void init() {
        facesEnv = MockFacesEnvironment.getServletInstance();

        component = new HtmlReportPanel();
        resolver = new DefaultResourceResolver();
    }

    @Test
    public void urlResource() {
        String resourceName = "http://jasperreportjsf.sourceforge.net/tld/jasperreports-jsf-1_0.tld";
        Resource res = resolver.resolveResource(facesEnv.getFacesContext(),
                component, resourceName);
        assert res != null;
        assert URLResource.class.equals(res.getClass());
    }

    @Test
    public void classpathResource() {
        String resourceName = "net/sf/jasperreports/jsf/resource/providers/ClasspathResourceTest.txt";
        Resource res = resolver.resolveResource(facesEnv.getFacesContext(),
                component, resourceName);
        assert res != null;
        assert ClasspathResource.class.equals(res.getClass());
    }

}
