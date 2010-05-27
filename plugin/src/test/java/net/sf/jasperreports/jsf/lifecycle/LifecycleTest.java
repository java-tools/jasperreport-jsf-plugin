/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.lifecycle;

import net.sf.jasperreports.jsf.test.MockFacesEnvironment;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author aalonsodominguez
 */
public class LifecycleTest {

    private Mockery mockery = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    private MockFacesEnvironment facesEnv;
    
    private RestoreViewPhaseListener restoreView;
    private RenderResponsePhaseListener renderResponse;

    @Before
    public void init() {
        facesEnv = MockFacesEnvironment.getServletInstance();

        restoreView = mockery.mock(RestoreViewPhaseListener.class);
        renderResponse = mockery.mock(RenderResponsePhaseListener.class);

        facesEnv.getLifecycle().addPhaseListener(restoreView);
        facesEnv.getLifecycle().addPhaseListener(renderResponse);
    }

    @Test
    public void processFacesRequest() {
        
    }

    @Test
    public void processReportRequest() {

    }

}
