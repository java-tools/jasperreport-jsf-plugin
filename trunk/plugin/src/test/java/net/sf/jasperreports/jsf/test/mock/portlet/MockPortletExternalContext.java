/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.test.mock.portlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.faces.context.ExternalContext;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

/**
 *
 * @author aalonsodominguez
 */
public class MockPortletExternalContext extends ExternalContext {

    private PortletContext portletContext;
    private PortletRequest portletRequest;
    private PortletResponse portletResponse;

    public MockPortletExternalContext(PortletContext portletContext,
            PortletRequest request, PortletResponse response) {
        this.portletContext = portletContext;
        this.portletRequest = request;
        this.portletResponse = response;
    }

    @Override
    public void dispatch(String path) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String encodeActionURL(String url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String encodeNamespace(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String encodeResourceURL(String url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, Object> getApplicationMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getAuthType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getContext() {
        return portletContext;
    }

    @Override
    public String getInitParameter(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map getInitParameterMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRemoteUser() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getRequest() {
        return portletRequest;
    }

    @Override
    public String getRequestContextPath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, Object> getRequestCookieMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, String> getRequestHeaderMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, String[]> getRequestHeaderValuesMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Locale getRequestLocale() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<Locale> getRequestLocales() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, Object> getRequestMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, String> getRequestParameterMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<String> getRequestParameterNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, String[]> getRequestParameterValuesMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRequestPathInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRequestServletPath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public URL getResource(String path) throws MalformedURLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<String> getResourcePaths(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getResponse() {
        return portletResponse;
    }

    @Override
    public Object getSession(boolean create) {
        return portletRequest.getPortletSession(create);
    }

    @Override
    public Map<String, Object> getSessionMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Principal getUserPrincipal() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isUserInRole(String role) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void log(String message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void log(String message, Throwable exception) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void redirect(String url) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
