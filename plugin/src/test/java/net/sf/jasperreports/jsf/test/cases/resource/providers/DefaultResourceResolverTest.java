/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.test.cases.resource.providers;

import net.sf.jasperreports.jsf.component.html.HtmlReportPanel;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.providers.DefaultResourceResolver;

import net.sf.jasperreports.jsf.test.framework.MockFacesTestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author antonio.alonso
 */
public class DefaultResourceResolverTest extends MockFacesTestCase{

    private HtmlReportPanel component;
    private DefaultResourceResolver resolver;

    @Before
    public void init() {
        component = new HtmlReportPanel();
    }

    @Test
    public void testURLResource() {
        String resourceName = "http://jasperreportjsf.sourceforge.net/tld/jasperreports-jsf-1_0.tld";
        Resource res = resolver.resolveResource(getFacesContext(),
                component, resourceName);
        Assert.assertNotNull(res);
    }

}
