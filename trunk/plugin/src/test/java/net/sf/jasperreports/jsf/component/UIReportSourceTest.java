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

import java.sql.Connection;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.jsf.engine.ReportSource;
import net.sf.jasperreports.jsf.engine.ReportSourceFactory;
import net.sf.jasperreports.jsf.engine.source.JRDataSourceHolder;
import net.sf.jasperreports.jsf.engine.source.ConnectionHolder;
import net.sf.jasperreports.jsf.test.JMockTheories;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockJRFacesContext;
import net.sf.jasperreports.jsf.validation.IllegalReportSourceTypeException;
import net.sf.jasperreports.jsf.validation.MissingAttributeException;
import net.sf.jasperreports.jsf.validation.ValidationException;
import net.sf.jasperreports.jsf.validation.Validator;

import org.apache.shale.test.el.MockValueExpression;
import org.apache.shale.test.mock.MockExternalContext;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
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
public final class UIReportSourceTest {

    // Data set tested in different theories

    public static final String COMPONENT_ID = "reportSourceId";

    @DataPoint
    public static final String VALID_TYPE = "bean";

    @DataPoint
    public static final Object EMPTY_DATA = new Object[]{ };

    @DataPoint
    public static final Object VALID_DATA = new Object[]{
        "0001", "0002", "0003", "0004", "0005" };

    @DataPoint
    public static final String DATA_BEAN_NAME = "reportSourceBean";

    @DataPoint
    public static final MockValueExpression MYBROKER_EXPR =
            new MockValueExpression("#{" + DATA_BEAN_NAME + ".myReportSource}",
            null);
    
    @DataPoint
    public static final MockValueExpression DATA_BROKER_EXPR =
            new MockValueExpression("#{" + DATA_BEAN_NAME + ".reportSource}",
            null);

    @DataPoint
    public static final String NULL_STRING = null;

    @DataPoint
    public static final Object NULL_OBJECT = null;

    @DataPoint
    public static final MockValueExpression NULL_VALUE_EXPRESSION = null;

    // Private fields

    private Mockery mockery = new JUnit4Mockery();

    private MockFacesEnvironment facesEnv;
    private MockJRFacesContext jrContext;

    private Connection connection;
    private ReportSource reportSource;
    private ReportSourceFactory reportSourceFactory;
    private Validator reportSourceValidator;
    private JRDataSource dataSource;

    // Test lifecycle methods

    @Before
    public void init() {
        facesEnv = MockFacesEnvironment.getServletInstance();

        connection = mockery.mock(Connection.class);
        reportSource = mockery.mock(ReportSource.class);
        reportSourceFactory = mockery.mock(ReportSourceFactory.class);
        reportSourceValidator = mockery.mock(Validator.class);
        dataSource = mockery.mock(JRDataSource.class);

        jrContext = new MockJRFacesContext(facesEnv.getFacesContext());
        jrContext.getAvailableDataSourceTypes().add(VALID_TYPE);
    }

    @After
    public void dispose() {
        jrContext = null;

        connection = null;
        reportSource = null;
        reportSourceFactory = null;
        reportSourceValidator = null;
        dataSource = null;

        facesEnv.release();
        facesEnv = null;
    }

    // Theories

    @Theory
    public void invalidTypeShouldThrowEx(String type, Object data,
            MockValueExpression value) {
        assumeThat(type, is(not(nullValue())));
        assumeThat(type, instanceOf(String.class));
        assumeThat(data, is(not(nullValue())));
        assumeTrue(!jrContext.getAvailableDataSourceTypes().contains(type));

        final UIReportSource component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();

        mockery.checking(new Expectations() {{
            oneOf(reportSourceValidator).validate(facesContext, component);
            will(throwException(new IllegalReportSourceTypeException(
                    component.getType())));
        }});

        try {
            component.processDecodes(facesContext);
            component.processValidators(facesContext);
            fail("A ValidationException should be thrown.");
        } catch(ValidationException e) {
            assertThat(e, instanceOf(IllegalReportSourceTypeException.class));
            assertThat(e.getMessage(), equalTo(type));
            assertTrue("Context should have 'RenderResponse' state enabled.",
                    facesContext.getRenderResponse());
            assertTrue("Component remains valid after validation exception,",
                    !component.isValid());
        }
        
        mockery.assertIsSatisfied();
    }

    @Theory
    public void withNullValueExprSendReportSourceUsingRequest(String type, Object data,
            MockValueExpression value) {
        assumeThat(type, is(not(nullValue())));
        assumeTrue(jrContext.getAvailableDataSourceTypes().contains(type));
        assumeThat(data, is(not(nullValue())));
        assumeThat(value, is(nullValue()));
        
        final UIReportSource component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();
        final String clientId = component.getClientId(facesContext);

        mockery.checking(new Expectations(){{
            oneOf(reportSourceValidator).validate(facesContext, component);
            oneOf(reportSourceFactory).createSource(facesContext, component);
            will(returnValue(reportSource));
        }});

        component.processDecodes(facesContext);
        component.processValidators(facesContext);
        component.processUpdates(facesContext);

        mockery.assertIsSatisfied();

        assertTrue(facesContext.getExternalContext()
                .getRequestMap().containsKey(clientId));
        Object source = facesContext.getExternalContext()
                .getRequestMap().get(clientId);
        assertThat(source, is(not(nullValue())));
        assertThat(source, instanceOf(ReportSource.class));
        assertSame(reportSource, source);
    }

    @Theory
    public void updateModelUsingValueExpr(String type, Object data,
            MockValueExpression value) {
        assumeTrue(jrContext.getAvailableDataSourceTypes().contains(type));
        assumeThat(data, is(not(nullValue())));
        assumeThat(value, is(not(nullValue())));
        
        final FacesContext facesContext = facesEnv.getFacesContext();
        final ReportSourceTestBean reportSourceBean = new ReportSourceTestBean();
        final MockExternalContext context = facesEnv.getExternalContext();
        context.getRequestMap().put(DATA_BEAN_NAME, reportSourceBean);

        final UIReportSource component = createComponent(type, data, value);
        final String clientId = component.getClientId(facesContext);

        assumeTrue(!value.isReadOnly(facesContext.getELContext()));

        mockery.checking(new Expectations(){{
            oneOf(reportSourceValidator).validate(facesContext, component);
            oneOf(reportSourceFactory).createSource(facesContext, component);
            will(returnValue(reportSource));
        }});

        component.processDecodes(facesContext);
        component.processValidators(facesContext);
        component.processUpdates(facesContext);
        
        assertTrue(!facesContext.getExternalContext()
                .getRequestMap().containsKey(clientId));
        
        ReportSource actualValue = (ReportSource) value.getValue(
                facesContext.getELContext());
        assertThat(actualValue, is(not(nullValue())));
        assertThat(actualValue, sameInstance(reportSource));

        ReportSource source = (ReportSource) component.getValue();
        assertThat(source, is(not(nullValue())));
        assertThat(source, sameInstance(reportSource));

        mockery.assertIsSatisfied();
    }

    @Theory
    public void whenDataSourceProvidedSendDataSourceHolderInRequest(String type,
            Object data, MockValueExpression value) {
        assumeThat(value, is(not(nullValue())));
        assumeThat(value.getExpressionString(), containsString("myReportSource"));

        final UIReportSource component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();
        final String clientId = component.getClientId(facesContext);

        final ReportSourceTestBean reportSourceBean =
                new ReportSourceTestBean(dataSource);
        MockExternalContext context = facesEnv.getExternalContext();
        context.getRequestMap().put(DATA_BEAN_NAME, reportSourceBean);

        mockery.checking(new Expectations(){{
            never(reportSourceValidator).validate(facesContext, component);
            never(reportSourceFactory).createSource(facesContext, component);
        }});

        component.processDecodes(facesContext);
        component.processValidators(facesContext);
        component.processUpdates(facesContext);

        Object source = facesContext.getExternalContext()
                .getRequestMap().get(clientId);
        assertThat(source, is(not(nullValue())));
        assertThat(source, is(JRDataSourceHolder.class));

        mockery.assertIsSatisfied();
    }

    @Theory
    public void whenConnectionProvidedSendConnectionHolderInRequest(String type,
            Object data, MockValueExpression value) {
        assumeThat(value, is(not(nullValue())));
        assumeThat(value.getExpressionString(), containsString("myReportSource"));

        final UIReportSource component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();
        final String clientId = component.getClientId(facesContext);

        final ReportSourceTestBean reportSourceBean =
                new ReportSourceTestBean(connection);
        MockExternalContext context = facesEnv.getExternalContext();
        context.getRequestMap().put(DATA_BEAN_NAME, reportSourceBean);

        mockery.checking(new Expectations(){{
            never(reportSourceValidator).validate(facesContext, component);
            never(reportSourceFactory).createSource(facesContext, component);
        }});

        component.processDecodes(facesContext);
        component.processValidators(facesContext);
        component.processUpdates(facesContext);

        Object source = facesContext.getExternalContext()
                .getRequestMap().get(clientId);
        assertThat(source, is(not(nullValue())));
        assertThat(source, is(ConnectionHolder.class));

        mockery.assertIsSatisfied();
    }

    @Theory
    public void withNonNullAttrsUseValidator(String type, Object data,
            MockValueExpression value) {
        assumeThat(type, is(not(nullValue())));
        assumeThat(data, is(not(nullValue())));
        assumeThat(value, is(nullValue()));

        final UIReportSource component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();

        mockery.checking(new Expectations(){{
            oneOf(reportSourceValidator).validate(facesContext, component);
        }});

        component.processDecodes(facesContext);
        component.processValidators(facesContext);
        
        mockery.assertIsSatisfied();
    }

    @Theory
    public void withoutAttrsThrowValidationEx(
            String type, Object data, MockValueExpression value) {
        assumeThat(data, is(nullValue()));
        assumeThat(type, is(nullValue()));
        assumeThat(value, is(nullValue()));

        final UIReportSource component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();

        mockery.checking(new Expectations(){{
            never(reportSourceValidator).validate(facesContext, component);
        }});

        try {
            component.processDecodes(facesContext);
            component.processValidators(facesContext);
            fail("Expected a validation exception for the 'type' attribute.");
        } catch (ValidationException e) {
            assertThat(e, instanceOf(MissingAttributeException.class));
            assertThat(e.getMessage(), equalTo("type"));
            assertTrue("Context should have 'RenderResponse' state enabled.",
                    facesEnv.getFacesContext().getRenderResponse());
            assertTrue("Component remains valid after validation exception,",
                    !component.isValid());
        }

        mockery.assertIsSatisfied();
    }

    @Theory
    public void withoutDataAndValueThrowValidationEx(
            String type, Object data, MockValueExpression value) {
        assumeThat(type, is(not(nullValue())));
        assumeThat(data, is(nullValue()));
        assumeThat(value, is(nullValue()));

        final UIReportSource component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();

        mockery.checking(new Expectations(){{
            never(reportSourceValidator).validate(facesContext, component);
        }});

        try {
            component.processDecodes(facesContext);
            component.processValidators(facesContext);
            fail("Expected a validation exception for the 'data' attribute.");
        } catch (ValidationException e) {
            assertThat(e, instanceOf(MissingAttributeException.class));
            assertThat(e.getMessage(), equalTo("data"));
            assertTrue("Context should have 'RenderResponse' state enabled.",
                    facesEnv.getFacesContext().getRenderResponse());
            assertTrue("Component remains valid after validation exception,",
                    !component.isValid());
        }

        mockery.assertIsSatisfied();
    }

    // Support methods

    private UIReportSource createComponent(String type, Object data,
            MockValueExpression value) {
        UIReportSource component = new UIReportSource();

        component.setId(COMPONENT_ID);
        component.setFactory(reportSourceFactory);
        component.setValidator(reportSourceValidator);

        if (type != null) {
            component.setType(type);
        }
        if (data != null) {
            component.setData(data);
        }
        if (value != null) {
            component.setValueExpression("value", value);
        }
        return component;
    }

}
