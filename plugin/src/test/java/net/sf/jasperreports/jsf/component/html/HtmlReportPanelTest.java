/*
 * JaspertReports JSF Plugin Copyright (C) 2010 A. Alonso Dominguez
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version. This library is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA A.
 *
 * Alonso Dominguez
 * alonsoft@users.sf.net
 */
package net.sf.jasperreports.jsf.component.html;

import net.sf.jasperreports.jsf.renderkit.ReportRenderer;
import net.sf.jasperreports.jsf.renderkit.html.PanelRenderer;
import net.sf.jasperreports.jsf.test.MockFacesEnvironment;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;


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

    @Before
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
