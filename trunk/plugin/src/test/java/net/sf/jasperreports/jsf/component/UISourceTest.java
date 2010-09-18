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
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;

import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.jsf.convert.SourceConverter;
import net.sf.jasperreports.jsf.engine.Source;
import net.sf.jasperreports.jsf.engine.source.JRDataSourceWrapper;
import net.sf.jasperreports.jsf.engine.source.ConnectionWrapper;
import net.sf.jasperreports.jsf.test.JMockTheories;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockFacesServletEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockJRFacesContext;
import net.sf.jasperreports.jsf.validation.IllegalSourceTypeException;
import net.sf.jasperreports.jsf.validation.MissingAttributeException;

import org.apache.shale.test.el.MockValueExpression;

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
import static net.sf.jasperreports.jsf.util.MessagesFactory.*;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(JMockTheories.class)
public final class UISourceTest {

    // Data set tested in different theories

    public static final String COMPONENT_ID = "reportSourceId";

    @DataPoint
    public static final String VALID_TYPE = "validType";

    @DataPoint
    public static final String INVALID_TYPE = "invalidType";

    @DataPoint
    public static final Object EMPTY_DATA = new Object[]{ };

    @DataPoint
    public static final Object VALID_DATA = new Object[]{
        "0001", "0002", "0003", "0004", "0005" };

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

    // Mock proxy instances
    
    private Connection connection;
    private Source reportSource;
    private SourceConverter sourceConverter;
    private Validator reportSourceValidator;
    private JRDataSource dataSource;

    // Test lifecycle methods

    @Before
    public void init() {
        facesEnv = new MockFacesServletEnvironment();

        connection = mockery.mock(Connection.class);
        reportSource = mockery.mock(Source.class);
        sourceConverter = mockery.mock(SourceConverter.class);
        reportSourceValidator = mockery.mock(Validator.class);
        dataSource = mockery.mock(JRDataSource.class);

        jrContext = new MockJRFacesContext(facesEnv.getFacesContext());
        jrContext.getAvailableSourceTypes().add(VALID_TYPE);
    }

    @After
    public void dispose() {
        jrContext = null;

        connection = null;
        reportSource = null;
        sourceConverter = null;
        reportSourceValidator = null;
        dataSource = null;

        facesEnv.release();
        facesEnv = null;
    }

    // Theories

    @Theory
    public void invalidTypeShouldThrowEx(String type, final Object data,
            MockValueExpression value) {
        assumeThat(type, is(not(nullValue())));
        assumeThat(type, instanceOf(String.class));
        assumeThat(data, is(not(nullValue())));
        assumeThat(value, is(nullValue()));
        assumeTrue(!jrContext.getAvailableSourceTypes().contains(type));

        final UISource component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();
        final FacesMessage message = createMessage(facesContext,
                    FacesMessage.SEVERITY_FATAL, "ILLEGAL_SOURCE_TYPE", type);

        mockery.checking(new Expectations() {{
            oneOf(reportSourceValidator).validate(facesContext, component, data);
            will(throwException(new IllegalSourceTypeException(message)));
        }});

        try {
            component.processDecodes(facesContext);
            component.processValidators(facesContext);
            fail("A ValidationException should be thrown.");
        } catch(ValidatorException e) {
            assertThat(e, is(IllegalSourceTypeException.class));
            assertThat(e.getFacesMessage(), equalTo(message));
            assertTrue("Context should have 'RenderResponse' state enabled.",
                    facesContext.getRenderResponse());
            assertTrue("Component remains valid after validation exception,",
                    !component.isValid());
        }        
    }

    @Theory
    public void withNullValueExprSendReportSourceUsingRequest(String type, 
            final Object data, MockValueExpression value) {
        assumeThat(type, is(not(nullValue())));
        assumeTrue(jrContext.getAvailableSourceTypes().contains(type));
        assumeThat(data, is(not(nullValue())));
        assumeThat(value, is(nullValue()));
        
        final UISource component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();
        final String clientId = component.getClientId(facesContext);

        mockery.checking(new Expectations(){{
            oneOf(reportSourceValidator).validate(facesContext, component, data);
            oneOf(sourceConverter).convertFromValue(facesContext, component, data);
            will(returnValue(reportSource));
        }});

        component.processDecodes(facesContext);
        component.processValidators(facesContext);
        component.processUpdates(facesContext);

        assertTrue(facesContext.getExternalContext()
                .getRequestMap().containsKey(clientId));
        Object source = facesContext.getExternalContext()
                .getRequestMap().get(clientId);
        assertThat(source, is(not(nullValue())));
        assertThat(source, instanceOf(Source.class));
        assertThat((Source) source, sameInstance(reportSource));
    }

    @Theory
    public void updateModelUsingValueExpr(String type, final Object data,
            MockValueExpression value) {
        assumeTrue(jrContext.getAvailableSourceTypes().contains(type));
        assumeThat(data, is(not(nullValue())));
        assumeThat(value, is(not(nullValue())));
        
        final FacesContext facesContext = facesEnv.getFacesContext();
        final SourceTestBean reportSourceBean = new SourceTestBean();
        final ExternalContext context = facesEnv.getExternalContext();
        context.getRequestMap().put(DATA_BEAN_NAME, reportSourceBean);

        final UISource component = createComponent(type, data, value);
        final String clientId = component.getClientId(facesContext);

        assumeTrue(!value.isReadOnly(facesContext.getELContext()));

        mockery.checking(new Expectations(){{
            oneOf(reportSourceValidator).validate(facesContext, component, data);
            oneOf(sourceConverter).convertFromValue(facesContext, component, data);
            will(returnValue(reportSource));
        }});

        component.processDecodes(facesContext);
        component.processValidators(facesContext);
        component.processUpdates(facesContext);
        
        assertTrue(!facesContext.getExternalContext()
                .getRequestMap().containsKey(clientId));
        
        Source actualValue = (Source) value.getValue(
                facesContext.getELContext());
        assertThat(actualValue, is(not(nullValue())));
        assertThat(actualValue, sameInstance(reportSource));

        Source source = component.getSubmittedSource();
        assertThat(source, is(not(nullValue())));
        assertThat(source, sameInstance(reportSource));
    }

    @Theory
    public void whenDataSourceProvidedSendDataSourceHolderInRequest(String type,
            final Object data, MockValueExpression value) {
        assumeThat(data, is(nullValue()));
        assumeThat(value, is(not(nullValue())));
        assumeThat(value.getExpressionString(), containsString("myReportSource"));
        assumeTrue(jrContext.getAvailableSourceTypes().contains(type));

        final UISource component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();
        final String clientId = component.getClientId(facesContext);

        final SourceTestBean reportSourceBean =
                new SourceTestBean(dataSource);
        ExternalContext context = facesEnv.getExternalContext();
        context.getRequestMap().put(DATA_BEAN_NAME, reportSourceBean);

        mockery.checking(new Expectations(){{
            oneOf(reportSourceValidator).validate(facesContext,
                    component, dataSource);
            oneOf(sourceConverter).convertFromValue(facesContext, 
                    component, dataSource);
            will(returnValue(new JRDataSourceWrapper(dataSource)));
        }});

        component.processDecodes(facesContext);
        component.processValidators(facesContext);
        component.processUpdates(facesContext);

        Object source = facesContext.getExternalContext()
                .getRequestMap().get(clientId);
        assertThat(source, is(not(nullValue())));
        assertThat(source, is(JRDataSourceWrapper.class));
    }

    @Theory
    public void whenConnectionProvidedSendConnectionHolderInRequest(String type,
            final Object data, MockValueExpression value) {
        assumeThat(data, is(nullValue()));
        assumeThat(value, is(not(nullValue())));
        assumeThat(value.getExpressionString(), containsString("myReportSource"));
        assumeTrue(jrContext.getAvailableSourceTypes().contains(type));

        final UISource component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();
        final String clientId = component.getClientId(facesContext);

        final SourceTestBean reportSourceBean =
                new SourceTestBean(connection);
        ExternalContext context = facesEnv.getExternalContext();
        context.getRequestMap().put(DATA_BEAN_NAME, reportSourceBean);

        mockery.checking(new Expectations(){{
            oneOf(reportSourceValidator).validate(facesContext,
                    component, connection);
            oneOf(sourceConverter).convertFromValue(facesContext,
                    component, connection);
            will(returnValue(new ConnectionWrapper(connection)));
        }});

        component.processDecodes(facesContext);
        component.processValidators(facesContext);
        component.processUpdates(facesContext);

        Object source = facesContext.getExternalContext()
                .getRequestMap().get(clientId);
        assertThat(source, is(not(nullValue())));
        assertThat(source, is(ConnectionWrapper.class));
    }

    @Theory
    public void withNonNullAttrsUseValidator(String type, final Object data,
            MockValueExpression value) {
        assumeThat(type, is(not(nullValue())));
        assumeThat(data, is(not(nullValue())));
        assumeThat(value, is(nullValue()));

        final UISource component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();

        mockery.checking(new Expectations(){{
            oneOf(reportSourceValidator).validate(facesContext, component, data);
            oneOf(sourceConverter).convertFromValue(facesContext, component, data);
            will(returnValue(reportSource));
        }});

        component.processDecodes(facesContext);
        component.processValidators(facesContext);
    }

    @Theory
    public void withoutAttrsThrowValidationEx(
            String type, final Object data, MockValueExpression value) {
        assumeThat(data, is(nullValue()));
        assumeThat(type, is(nullValue()));
        assumeThat(value, is(nullValue()));

        final UISource component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();

        mockery.checking(new Expectations(){{
            never(reportSourceValidator).validate(facesContext, component, data);
        }});

        try {
            component.processDecodes(facesContext);
            component.processValidators(facesContext);
            fail("Expected a validation exception for the 'type' attribute.");
        } catch (ValidatorException e) {
            assertThat(e, instanceOf(MissingAttributeException.class));
            assertTrue("Context should have 'RenderResponse' state enabled.",
                    facesEnv.getFacesContext().getRenderResponse());
            assertTrue("Component remains valid after validation exception,",
                    !component.isValid());
        }
    }

    @Theory
    public void withoutDataAndValueThrowValidationEx(
            final String type, final Object data, MockValueExpression value) {
        assumeThat(type, is(not(nullValue())));
        assumeThat(data, is(nullValue()));
        assumeThat(value, is(nullValue()));

        final UISource component = createComponent(type, data, value);
        final FacesContext facesContext = facesEnv.getFacesContext();
        final FacesMessage message = createMessage(facesContext,
                    FacesMessage.SEVERITY_FATAL, "ILLEGAL_SOURCE_TYPE", type);

        mockery.checking(new Expectations(){{
            oneOf(reportSourceValidator).validate(facesContext, component, data);
            if (!jrContext.getAvailableSourceTypes().contains(type)) {
                will(throwException(new IllegalSourceTypeException(message)));
            } else {
                oneOf(sourceConverter).convertFromValue(facesContext,
                        component, data);
            }
        }});

        try {
            component.processDecodes(facesContext);
            component.processValidators(facesContext);
            fail("Expected a validation exception for the 'data' attribute.");
        } catch (ValidatorException e) {
            assertThat(e, instanceOf(MissingAttributeException.class));
            assertTrue("Context should have 'RenderResponse' state enabled.",
                    facesEnv.getFacesContext().getRenderResponse());
            assertTrue("Component remains valid after validation exception,",
                    !component.isValid());
        }

    }

    // Support methods

    private UISource createComponent(String type, Object data,
            MockValueExpression value) {
        UISource component = new UISource();

        component.setId(COMPONENT_ID);
        component.setConverter(sourceConverter);
        component.setValidator(reportSourceValidator);

        if (type != null) {
            component.setType(type);
        }
        if (data != null) {
            component.setValue(data);
        }
        if (value != null) {
            component.setValueExpression("value", value);
        }
        return component;
    }

}
