/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.lifecycle;

import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockFacesServletEnvironment;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author aalonsodominguez
 */
public class LifecycleTest {

    private Mockery mockery = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    private MockFacesEnvironment facesEnv;
    
    private RestoreViewPhaseListener restoreView;
    private RenderResponsePhaseListener renderResponse;

    @Before
    public void init() {
        facesEnv = new MockFacesServletEnvironment();

        restoreView = mockery.mock(RestoreViewPhaseListener.class);
        renderResponse = mockery.mock(RenderResponsePhaseListener.class);

        facesEnv.getLifecycle().addPhaseListener(restoreView);
        facesEnv.getLifecycle().addPhaseListener(renderResponse);
    }

    @After
    public void dispose() {
        facesEnv.getLifecycle().removePhaseListener(restoreView);
        facesEnv.getLifecycle().removePhaseListener(renderResponse);

        restoreView = null;
        renderResponse = null;

        facesEnv.release();
        facesEnv = null;
    }

    @Test
    public void processFacesRequest() {
        
    }

    @Test
    public void processReportRequest() {

    }

}
