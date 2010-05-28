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

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.engine.databroker.DataBroker;
import net.sf.jasperreports.jsf.engine.databroker.DataBrokerFactory;
import net.sf.jasperreports.jsf.test.JMockTheories;
import net.sf.jasperreports.jsf.test.MockFacesEnvironment;
import net.sf.jasperreports.jsf.validation.DataBrokerValidator;
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
public class UIDataBrokerTest {

    private static final String DATA_RECEIVER_NAME = "dataReceiver";

    private static final String DATA_PROVIDER_NAME = "dataProvider";

    private static final String DATA_RECEIVER_EXPR =
            "#{" + DATA_RECEIVER_NAME + ".dataBroker}";

    private static final String DATA_PROVIDER_EXPR =
            "#{" + DATA_PROVIDER_NAME + ".dataBroker}";

    @DataPoint
    public static final UIDataBroker EMPTY_BROKER = new UIDataBroker();

    @DataPoint
    public static final UIDataBroker getBrokerWithBuiltinProviderVE() {
        UIDataBroker component = new UIDataBroker();
        MockValueExpression ve = new MockValueExpression(
                DATA_PROVIDER_EXPR, null);
        component.setValueExpression("value", ve);
        return component;
    }

    @DataPoint
    public static final UIDataBroker getBrokerWithBuiltinReceiverVE() {
        UIDataBroker component = new UIDataBroker();
        MockValueExpression ve = new MockValueExpression(
                DATA_RECEIVER_EXPR, null);
        component.setValueExpression("value", ve);
        return component;
    }

    @DataPoint
    public static final UIDataBroker getBrokerWithType() {
        UIDataBroker component = new UIDataBroker();
        component.setType("bean");
        return component;
    }

    @DataPoint
    public static final UIDataBroker getBrokerWithoutVE() {
        UIDataBroker component = new UIDataBroker();
        component.setType("bean");
        component.setData(new Object[]{ });
        return component;
    }

    private MockFacesEnvironment facesEnv;

    private DataBrokerFactory dataBrokerFactory;
    private DataBrokerValidator dataBrokerValidator;

    private Mockery mockery = new JUnit4Mockery();

    @Before
    public void init() {
        facesEnv = MockFacesEnvironment.getServletInstance();
        
        dataBrokerFactory = mockery.mock(DataBrokerFactory.class);
        dataBrokerValidator = mockery.mock(DataBrokerValidator.class);

        MockExternalContext context = facesEnv.getExternalContext();
        
        DataBrokerBean dataReceiver = new DataBrokerBean();
        context.getRequestMap().put(DATA_RECEIVER_NAME, dataReceiver);

        DataBrokerBean dataProvider = new DataBrokerBean();
        context.getRequestMap().put(DATA_PROVIDER_NAME, dataProvider);
    }

    @After
    public void dispose() {
        //facesEnv.release();
        facesEnv = null;
    }

    @Theory
    public void updateModelUsingRequest(final UIDataBroker component) {
        assumeThat(component.getValueExpression("value"), is(nullValue()));
        assumeThat(component.getValue(), is(nullValue()));
        component.setDataBrokerFactory(dataBrokerFactory);

        final FacesContext facesContext = facesEnv.getFacesContext();
        final DataBroker expectedBroker = new DataBroker() {};
        final String clientId = component.getClientId(facesContext);

        mockery.checking(new Expectations(){{
            one(dataBrokerFactory).createDataBroker(facesContext, component);
            will(returnValue(expectedBroker));
        }});

        component.updateModel(facesContext);
        mockery.assertIsSatisfied();

        assertTrue(facesContext.getExternalContext()
                .getRequestMap().containsKey(clientId));
        Object broker = facesContext.getExternalContext()
                .getRequestMap().get(clientId);
        assertThat(broker, is(not(nullValue())));
        assertThat(broker, instanceOf(DataBroker.class));
        assertSame(expectedBroker, broker);
    }

    @Theory
    public void updateModelUsingValueExpression(final UIDataBroker component) {
        ValueExpression ve = component.getValueExpression("value");
        assumeThat(ve, is(not(nullValue())));
        assumeThat(component.getValue(), is(nullValue()));
        component.setDataBrokerFactory(dataBrokerFactory);
        
        final FacesContext facesContext = facesEnv.getFacesContext();
        final DataBroker expectedBroker = new DataBroker() {};
        final String clientId = component.getClientId(facesContext);
        
        mockery.checking(new Expectations(){{
            one(dataBrokerFactory).createDataBroker(facesContext, component);
            will(returnValue(expectedBroker));
        }});

        component.updateModel(facesContext);
        mockery.assertIsSatisfied();
        
        assertTrue(!facesContext.getExternalContext()
                .getRequestMap().containsKey(clientId));
        
        DataBroker value = (DataBroker) ve.getValue(
                facesContext.getELContext());
        assertThat(value, is(not(nullValue())));
        assertThat(value, sameInstance(expectedBroker));

        DataBroker broker = (DataBroker) component.getValue();
        assertThat(broker, is(not(nullValue())));
        assertThat(broker, sameInstance(expectedBroker));
    }

    @Theory
    public void withNonNullAttrsUseValidator(final UIDataBroker component) {
        assumeThat(component.getData(), is(not(nullValue())));
        assumeThat(component.getType(), is(not(nullValue())));
        component.setValidator(dataBrokerValidator);

        final FacesContext facesContext = facesEnv.getFacesContext();

        mockery.checking(new Expectations(){{
            one(dataBrokerValidator).validate(facesContext, component);
        }});

        component.validate(facesContext);
        mockery.assertIsSatisfied();
    }

    @Theory
    public void withoutAttrsThrowValidationEx(
            final UIDataBroker component) {
        assumeThat(component.getData(), is(nullValue()));
        assumeThat(component.getType(), is(nullValue()));
        assumeThat(component.getValue(), is(nullValue()));

        try {
            component.validate(facesEnv.getFacesContext());
            fail("Expected a validation exception for the 'type' attribute.");
        } catch (ValidationException e) {
            assertThat(e, instanceOf(MissingAttributeException.class));
            assertThat(e.getMessage(), equalTo("type"));
        }
    }

    @Theory
    public void withoutDataAndValueThrowValidationEx(
            final UIDataBroker component) {
        assumeThat(component.getData(), is(nullValue()));
        assumeThat(component.getType(), is(not(nullValue())));
        assumeThat(component.getValue(), is(nullValue()));

        try {
            component.validate(facesEnv.getFacesContext());
            fail("Expected a validation exception for the 'data' attribute.");
        } catch (ValidationException e) {
            assertThat(e, instanceOf(MissingAttributeException.class));
            assertThat(e.getMessage(), equalTo("data"));
        }
    }

    public static class DataBrokerBean {

        private DataBroker dataBroker;

        public DataBroker getDataBroker() {
            return dataBroker;
        }

        public void setDataBroker(DataBroker dataBroker) {
            this.dataBroker = dataBroker;
        }

    }

}
