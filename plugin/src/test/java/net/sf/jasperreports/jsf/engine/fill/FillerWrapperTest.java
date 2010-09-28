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
package net.sf.jasperreports.jsf.engine.fill;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.engine.Filler;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockFacesServletEnvironment;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(JMock.class)
public class FillerWrapperTest {

    private Mockery mockery = new JUnit4Mockery();

    private MockFacesEnvironment facesEnv;
    private UIReport component;
    private Filler wrappedInstance;

    @Before
    public void init() {
        facesEnv = new MockFacesServletEnvironment();
        wrappedInstance = mockery.mock(Filler.class);
        component = new UIReport();
    }

    @Test
    public void wrappedInstanceGetsCalled() {
        final FacesContext facesContext = facesEnv.getFacesContext();
        final FillerWrapper wrapper = new DummyFillerWrapper();

        mockery.checking(new Expectations() {{
            oneOf(wrappedInstance).fill(facesContext, component);
        }});

        wrapper.fill(facesContext, component);
    }

    private class DummyFillerWrapper extends FillerWrapper {

        @Override
        protected Filler getWrapped() {
            return wrappedInstance;
        }

    }

}
