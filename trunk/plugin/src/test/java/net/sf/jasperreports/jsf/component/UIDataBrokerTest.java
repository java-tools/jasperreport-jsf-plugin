/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.component;

import net.sf.jasperreports.jsf.test.MockFacesEnvironment;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author aalonsodominguez
 */
public class UIDataBrokerTest {

    private MockFacesEnvironment facesEnv =
            MockFacesEnvironment.getServletInstance();

    private Mockery mockery = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    private UIDataBroker component;

    public void check() {
        component.setType("bean");
        component.setData(new Object[]{ });

        String clientId = component.getClientId(facesEnv.getFacesContext());
        component.updateModel(facesEnv.getFacesContext());
        Object databroker = facesEnv.getExternalContext()
                .getRequestMap().get(clientId);
        assertThat(databroker, is(not(nullValue())));
    }

}
