/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.resource.providers;

import net.sf.jasperreports.jsf.component.html.HtmlReport;
import net.sf.jasperreports.jsf.resource.Resource;

import org.jboss.test.faces.mock.MockFacesEnvironment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author antonio.alonso
 */
public class DefaultResourceResolverTest {

    private MockFacesEnvironment env;
    private HtmlReport component;

    private DefaultResourceResolver resolver;

    @Before
    public void initEnv() {
        env = MockFacesEnvironment.createEnvironment();
        component = env.createMock(HtmlReport.class);
    }

    @Test
    public void testURLResource() {
        String resourceName = "http://jasperreportjsf.sourceforge.net/tld/jasperreports-jsf-1_0.tld";
        Resource res = resolver.resolveResource(env.getFacesContext(),
                component, resourceName);
        Assert.assertNotNull(res);
    }

}
