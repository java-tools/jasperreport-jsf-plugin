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
import net.sf.jasperreports.jsf.engine.databroker.DataSourceHolder;
import net.sf.jasperreports.jsf.engine.databroker.DataSourceFactory;
import net.sf.jasperreports.jsf.engine.databroker.JRDataSourceHolder;
import net.sf.jasperreports.jsf.engine.databroker.SqlConnectionHolder;
import net.sf.jasperreports.jsf.test.JMockTheories;
import net.sf.jasperreports.jsf.test.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.MockJRFacesContext;
import net.sf.jasperreports.jsf.validation.DataBrokerValidator;
import net.sf.jasperreports.jsf.validation.IllegalDataBrokerTypeException;
import net.sf.jasperreports.jsf.validation.MissingAttributeException;
import net.sf.jasperreports.jsf.validation.ValidationException;

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
public final class UIDataBrokerTest {

    // Data set tested in different theories

    public static final String COMPONENT_ID = "dataBrokerId";

    @DataPoint
    public static final String VALID_TYPE = "bean";

    @DataPoint
    public static final Object EMPTY_DATA = new Object[]{ };

    @DataPoint
    public static final Object VALID_DATA = new Object[]{
        "0001", "0002", "0003", "0004", "0005" };

    @DataPoint
    public static final String DATA_BEAN_NAME = "dataBrokerBean";

    @DataPoint
    public static final MockValueExpression MYBROKER_EXPR =
            new MockValueExpression("#{" + DATA_BEAN_NAME + ".myBroker}",
            null);
    
    @DataPoint
    public static final MockValueExpression DATA_BROKER_EXPR =
            new MockValueExpression("#{" + DATA_BEAN_NAME + ".dataBroker}",
            null);

    @DataPoint
    public static final String NULL_STRING = null;

    @DataPoint
    public static final Object NULL_OBJECT = null;

    @DataPoint
    public static final MockValueExpression NULL_VALUE_EXPRESSION = null;

    // Private fields

    private MockFacesEnvironment facesEnv;
    private MockJRFacesContext jrContext;

    private Connection connection;
    private DataSourceHolder dataBroker;
    private DataSourceFactory dataBrokerFactory;
    private DataBrokerValidator dataBrokerValidator;
    private DataBrokerTestBean dataBrokerBean;
    private JRDataSource dataSource;

    private Mockery mockery = new JUnit4Mockery();

    // Test lifecycle methods

    @Before
    public void init() {
        facesEnv = MockFacesEnvironment.getServletInstance();

        connection = mockery.mock(Connection.class);
        dataBroker = mockery.mock(DataSourceHolder.class);
        dataBrokerBean = mockery.mock(DataBrokerTestBean.class);
        dataBrokerFactory = mockery.mock(DataSourceFactory.class);
        dataBrokerValidator = mockery.mock(DataBrokerValidator.class);
        dataSource = mockery.mock(JRDataSource.class);

        MockExternalContext context = facesEnv.getExternalContext();
        context.getRequestMap().put(DATA_BEAN_NAME, dataBrokerBean);

        jrContext = new MockJRFacesContext();
        jrContext.getAvailableDataSourceTypes().add(VALID_TYPE);
    }

    @After
    public void dispose() {
        jrContext = null;

        connection = null;
        dataBroker = null;
        dataBrokerBean = null;
        dataBrokerFactory = null;
        dataBrokerValidator = null;
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

        final UIDataBroker component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();

        mockery.checking(new Expectations() {{
            ignoring(dataBrokerBean).getMyBroker(); will(returnValue(null));
            ignoring(dataBrokerBean).getDataBroker(); will(returnValue(null));

            oneOf(dataBrokerValidator).validate(facesContext, component);
            will(throwException(new IllegalDataBrokerTypeException(
                    component.getType())));
        }});

        try {
            component.processDecodes(facesContext);
            component.processValidators(facesContext);
            fail("A ValidationException should be thrown.");
        } catch(ValidationException e) {
            assertThat(e, instanceOf(IllegalDataBrokerTypeException.class));
            assertThat(e.getMessage(), equalTo(type));
            assertTrue("Context should have 'RenderResponse' state enabled.",
                    facesContext.getRenderResponse());
            assertTrue("Component remains valid after validation exception,",
                    !component.isValid());
        }
        
        mockery.assertIsSatisfied();
    }

    @Theory
    public void withNullValueExprSendBrokerUsingRequest(String type, Object data,
            MockValueExpression value) {
        assumeThat(type, is(not(nullValue())));
        assumeTrue(jrContext.getAvailableDataSourceTypes().contains(type));
        assumeThat(data, is(not(nullValue())));
        assumeThat(value, is(nullValue()));
        
        final UIDataBroker component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();
        final String clientId = component.getClientId(facesContext);

        mockery.checking(new Expectations(){{
            oneOf(dataBrokerValidator).validate(facesContext, component);
            oneOf(dataBrokerFactory).createDataSource(facesContext, component);
            will(returnValue(dataBroker));
        }});

        component.processDecodes(facesContext);
        component.processValidators(facesContext);
        component.processUpdates(facesContext);

        mockery.assertIsSatisfied();

        assertTrue(facesContext.getExternalContext()
                .getRequestMap().containsKey(clientId));
        Object broker = facesContext.getExternalContext()
                .getRequestMap().get(clientId);
        assertThat(broker, is(not(nullValue())));
        assertThat(broker, instanceOf(DataSourceHolder.class));
        assertSame(dataBroker, broker);
    }

    @Theory
    public void updateModelUsingValueExpr(String type, Object data,
            MockValueExpression value) {
        assumeTrue(jrContext.getAvailableDataSourceTypes().contains(type));
        assumeThat(data, is(not(nullValue())));
        assumeThat(value, is(not(nullValue())));
        
        final FacesContext facesContext = facesEnv.getFacesContext();
        assumeTrue(!value.isReadOnly(facesContext.getELContext()));
        
        final UIDataBroker component = createComponent(type, data, value);        
        final String clientId = component.getClientId(facesContext);
        
        mockery.checking(new Expectations(){{
            atMost(3).of(dataBrokerBean).getDataBroker();
            will(returnValue(null));

            oneOf(dataBrokerValidator).validate(facesContext, component);
            oneOf(dataBrokerFactory).createDataSource(facesContext, component);
            will(returnValue(dataBroker));

            oneOf(dataBrokerBean).setDataBroker(dataBroker);

            // Assertions performed at end of the test
            atLeast(2).of(dataBrokerBean).getDataBroker();
            will(returnValue(dataBroker));
        }});

        component.processDecodes(facesContext);
        component.processValidators(facesContext);
        component.processUpdates(facesContext);
        
        assertTrue(!facesContext.getExternalContext()
                .getRequestMap().containsKey(clientId));
        
        DataSourceHolder actualValue = (DataSourceHolder) value.getValue(
                facesContext.getELContext());
        assertThat(actualValue, is(not(nullValue())));
        assertThat(actualValue, sameInstance(dataBroker));

        DataSourceHolder broker = (DataSourceHolder) component.getValue();
        assertThat(broker, is(not(nullValue())));
        assertThat(broker, sameInstance(dataBroker));

        mockery.assertIsSatisfied();
    }

    @Theory
    public void whenDataSourceProvidedSendDataSourceBrokerInRequest(String type,
            Object data, MockValueExpression value) {
        assumeThat(value, is(not(nullValue())));
        assumeThat(value.getExpressionString(), containsString("myBroker"));

        final UIDataBroker component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();
        final String clientId = component.getClientId(facesContext);

        mockery.checking(new Expectations(){{
            atLeast(1).of(dataBrokerBean).getMyBroker(); will(returnValue(dataSource));

            never(dataBrokerValidator).validate(facesContext, component);
            never(dataBrokerFactory).createDataSource(facesContext, component);
        }});

        component.processDecodes(facesContext);
        component.processValidators(facesContext);
        component.processUpdates(facesContext);

        Object broker = facesContext.getExternalContext()
                .getRequestMap().get(clientId);
        assertThat(broker, is(not(nullValue())));
        assertThat(broker, is(JRDataSourceHolder.class));

        mockery.assertIsSatisfied();
    }

    @Theory
    public void whenConnectionProvidedSendSqlBrokerInRequest(String type,
            Object data, MockValueExpression value) {
        assumeThat(value, is(not(nullValue())));
        assumeThat(value.getExpressionString(), containsString("myBroker"));

        final UIDataBroker component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();
        final String clientId = component.getClientId(facesContext);

        mockery.checking(new Expectations(){{
            atLeast(1).of(dataBrokerBean).getMyBroker(); will(returnValue(connection));

            never(dataBrokerValidator).validate(facesContext, component);
            never(dataBrokerFactory).createDataSource(facesContext, component);
        }});

        component.processDecodes(facesContext);
        component.processValidators(facesContext);
        component.processUpdates(facesContext);

        Object broker = facesContext.getExternalContext()
                .getRequestMap().get(clientId);
        assertThat(broker, is(not(nullValue())));
        assertThat(broker, is(SqlConnectionHolder.class));

        mockery.assertIsSatisfied();
    }

    @Theory
    public void withNonNullAttrsUseValidator(String type, Object data,
            MockValueExpression value) {
        assumeThat(type, is(not(nullValue())));
        assumeThat(data, is(not(nullValue())));
        assumeThat(value, is(nullValue()));

        final UIDataBroker component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();

        mockery.checking(new Expectations(){{
            oneOf(dataBrokerValidator).validate(facesContext, component);
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

        final UIDataBroker component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();

        mockery.checking(new Expectations(){{
            never(dataBrokerValidator).validate(facesContext, component);
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

        final UIDataBroker component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();

        mockery.checking(new Expectations(){{
            never(dataBrokerValidator).validate(facesContext, component);
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

    private UIDataBroker createComponent(String type, Object data,
            MockValueExpression value) {
        UIDataBroker component = new UIDataBroker();

        component.setId(COMPONENT_ID);
        component.setBrokerFactory(dataBrokerFactory);
        component.setValidator(dataBrokerValidator);

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
