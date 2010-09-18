/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.resource;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockFacesServletEnvironment;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(JMock.class)
public class ResourceResolverWrapperTest {

    private static final String RESOURCE_NAME = "resource";

    private Mockery mockery = new JUnit4Mockery();
    
    private MockFacesEnvironment facesEnv;
    private ResourceResolver wrappedResolver;

    @Before
    public void init() {
        facesEnv = new MockFacesServletEnvironment();
        wrappedResolver = mockery.mock(ResourceResolver.class);
    }

    @Test
    public void wrappedInstanceGetsCalled() {
        final FacesContext facesContext = facesEnv.getFacesContext();
        final ResourceResolverWrapper wrapper = new DummyResourceResolver();

        mockery.checking(new Expectations() {{
            oneOf(wrappedResolver).resolveResource(
                    facesContext, null, RESOURCE_NAME);
            will(returnValue(null));
        }});

        wrapper.resolveResource(facesContext, null, RESOURCE_NAME);
    }

    private class DummyResourceResolver extends ResourceResolverWrapper {

        @Override
        public ResourceResolver getWrapped() {
            return wrappedResolver;
        }

    }

}
