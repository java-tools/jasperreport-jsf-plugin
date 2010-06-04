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
package net.sf.jasperreports.jsf.component;

import javax.faces.context.ExternalContext;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.jsf.engine.Exporter;
import net.sf.jasperreports.jsf.engine.Filler;
import net.sf.jasperreports.jsf.test.JMockTheories;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockJRFacesContext;
import net.sf.jasperreports.jsf.validation.Validator;
import org.apache.shale.test.el.MockValueExpression;
import org.apache.shale.test.mock.MockExternalContext;
import org.apache.shale.test.mock.MockFacesContext;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(JMockTheories.class)
public class UIReportTest {

    public static final String REPORT_BEAN_NAME = "reportBean";

    public static JasperReport REPORT_OBJECT;

    public static final String VALID_FORMAT = "aformat";

    @BeforeClass
    public static void initDataPoints() {

    }

    private Mockery mockery = new JUnit4Mockery();

    private MockFacesEnvironment facesEnv;
    private MockJRFacesContext jrContext;

    private UIReport component;
    private Filler filler;
    private Exporter exporter;
    private Validator validator;

    @Before
    public void init() {
        facesEnv = MockFacesEnvironment.getServletInstance();
        jrContext = new MockJRFacesContext(facesEnv.getFacesContext());
        jrContext.getAvailableExportFormats().add(VALID_FORMAT);

        filler = mockery.mock(Filler.class);
        exporter = mockery.mock(Exporter.class);
        validator = mockery.mock(Validator.class);

        jrContext.setExporter(exporter);
        jrContext.setFiller(filler);
        jrContext.setReportValidator(validator);

        //component = new UIReport();
        //component.setId("reportId");

        MockValueExpression ve = new MockValueExpression(
                "#{" + REPORT_BEAN_NAME + ".reportOrPrint}", null);
        component.setValueExpression("value", ve);
    }

    @After
    public void dispose() {
        component = null;
        filler = null;
        exporter = null;
        validator = null;

        jrContext = null;
        facesEnv.release();
        facesEnv = null;
    }

    @Theory
    public void withStringSourceLookupRequest() {

    }

    @Theory
    public void attrDataBrokerIsValueExprObtainFromBean() {

    }

    private void registerBean(Object reportOrPrint) {
        final ExternalContext context = facesEnv.getExternalContext();
        ReportTestBean reportBean = new ReportTestBean(reportOrPrint);
        context.getRequestMap().put(REPORT_BEAN_NAME, reportBean);
    }

}
