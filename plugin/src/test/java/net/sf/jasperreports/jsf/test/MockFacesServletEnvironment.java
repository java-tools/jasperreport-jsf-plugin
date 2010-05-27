/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.test;

import org.apache.shale.test.mock.MockExternalContext12;
import org.apache.shale.test.mock.MockHttpServletRequest;
import org.apache.shale.test.mock.MockHttpServletResponse;
import org.apache.shale.test.mock.MockHttpSession;
import org.apache.shale.test.mock.MockServletConfig;
import org.apache.shale.test.mock.MockServletContext;

/**
 *
 * @author aalonsodominguez
 */
public final class MockFacesServletEnvironment extends MockFacesEnvironment {

    private MockServletContext servletContext;
    private MockServletConfig servletConfig;
    private MockHttpSession session;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    private MockExternalContext12 externalContext;

    @Override
    protected void initializeExternalContext() {
        servletContext = new MockServletContext();
        servletConfig = new MockServletConfig(servletContext);

        session = new MockHttpSession(servletContext);
        request = new MockHttpServletRequest(session);
        response = new MockHttpServletResponse();

        externalContext = new MockExternalContext12(servletContext,
                request, response);
    }

    @Override
    protected void releaseExternalContext() {
        servletConfig = null;
        servletContext = null;
        session = null;
        request = null;
        response = null;
        externalContext = null;
    }

    public MockHttpServletRequest getRequest() {
        return request;
    }

    public MockHttpServletResponse getResponse() {
        return response;
    }

    public MockExternalContext12 getExternalContext() {
        return externalContext;
    }

    public MockServletConfig getServletConfig() {
        return servletConfig;
    }

    public MockServletContext getServletContext() {
        return servletContext;
    }

    public MockHttpSession getSession() {
        return session;
    }
   
}
