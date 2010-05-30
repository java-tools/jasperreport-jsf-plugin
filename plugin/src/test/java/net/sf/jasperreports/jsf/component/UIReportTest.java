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

import net.sf.jasperreports.jsf.test.JMockTheories;
import net.sf.jasperreports.jsf.test.MockFacesEnvironment;
import org.apache.shale.test.mock.MockExternalContext;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
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
public class UIReportTest {

    public static final String DATA_BEAN_NAME = "dataBrokerBean";

    private MockFacesEnvironment facesEnv;

    private Mockery mockery = new JUnit4Mockery();

    private DataBrokerTestBean dataBrokerBean;

    @Before
    public void init() {
        facesEnv = MockFacesEnvironment.getServletInstance();

        dataBrokerBean = mockery.mock(DataBrokerTestBean.class);

        MockExternalContext context = facesEnv.getExternalContext();
        context.getRequestMap().put(DATA_BEAN_NAME, dataBrokerBean);
    }

    @Theory
    public void attrDataBrokerIsStringLookupRequest() {

    }

    @Theory
    public void attrDataBrokerIsValueExprObtainFromBean() {

    }

}
