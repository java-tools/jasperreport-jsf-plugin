/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.test.mock.portlet;

import java.io.IOException;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 *
 * @author aalonsodominguez
 */
public class MockPortletRequestDispatcher implements PortletRequestDispatcher {

    public enum Action {
        RENDER_INCLUDE, INCLUDE, FORWARD
    }

    private Action performed = null;

    public Action getActionPerformed() {
        return performed;
    }

    public void include(RenderRequest request, RenderResponse response)
            throws PortletException, IOException {
        performed = Action.RENDER_INCLUDE;
    }

    public void include(PortletRequest request, PortletResponse response)
            throws PortletException, IOException {
        performed = Action.INCLUDE;
    }

    public void forward(PortletRequest request, PortletResponse response)
            throws PortletException, IOException {
        performed = Action.FORWARD;
    }

}
