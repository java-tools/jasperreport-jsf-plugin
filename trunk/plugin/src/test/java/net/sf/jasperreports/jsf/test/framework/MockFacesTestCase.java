/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.test.framework;

import javax.faces.context.FacesContext;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

/**
 *
 * @author aalonsodominguez
 */
public class MockFacesTestCase {

    protected final Mockery mockery = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    public FacesContext getFacesContext() {
       FacesContext context = FacesContext.getCurrentInstance();
       if (context == null) {
           context = mockery.mock(FacesContext.class);
       }
       return context;
    }

}
