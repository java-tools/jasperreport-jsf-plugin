/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.test.mock.portlet;

import javax.portlet.PortletResponse;
import javax.servlet.http.Cookie;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

/**
 *
 * @author aalonsodominguez
 */
public abstract class MockPortletResponse implements PortletResponse {

    public void addProperty(String key, String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setProperty(String key, String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String encodeURL(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getNamespace() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addProperty(Cookie cookie) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addProperty(String key, Element element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Element createElement(String tagName) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
