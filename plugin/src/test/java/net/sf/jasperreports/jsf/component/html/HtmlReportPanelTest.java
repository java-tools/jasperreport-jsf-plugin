/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.component.html;

import net.sf.jasperreports.jsf.renderkit.ReportRenderer;
import net.sf.jasperreports.jsf.renderkit.html.PanelRenderer;
import net.sf.jasperreports.jsf.test.framework.MockFacesEnvironment;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author aalonsodominguez
 */
public class HtmlReportPanelTest {

    private Mockery context = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    private MockFacesEnvironment facesEnv;

    private HtmlReportPanel reportPanel;
    private ReportRenderer renderer;

    @BeforeTest
    public void initSupportClasses() {
        facesEnv = MockFacesEnvironment.getServletInstance();

        reportPanel = new HtmlReportPanel();
        renderer = context.mock(ReportRenderer.class);
        facesEnv.getRenderKit().addRenderer(reportPanel.getFamily(),
                PanelRenderer.RENDERER_TYPE, renderer);
    }

    @Test
    public void encodeContent() throws Exception {
        context.checking(new Expectations() {{
            oneOf(renderer).encodeContent(facesEnv.getFacesContext(), reportPanel);
        }});

        reportPanel.encodeContent(facesEnv.getFacesContext());

        context.assertIsSatisfied();
    }

    @Test
    public void encodeHeaders() throws Exception {
        context.checking(new Expectations() {{
            oneOf(renderer).encodeHeaders(facesEnv.getFacesContext(), reportPanel);
        }});

        reportPanel.encodeHeaders(facesEnv.getFacesContext());

        context.assertIsSatisfied();
    }

}
