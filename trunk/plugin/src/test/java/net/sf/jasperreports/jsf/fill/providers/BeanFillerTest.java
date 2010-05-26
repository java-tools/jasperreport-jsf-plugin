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
package net.sf.jasperreports.jsf.fill.providers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.jsf.test.TestConstants;
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.test.MockFacesEnvironment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author antonio.alonso
 */
@RunWith(Parameterized.class)
public class BeanFillerTest {

    private MockFacesEnvironment facesEnv;

    @Parameters
    public static Collection<?> createData() {
        Object[][] array = new Object[][] {
            { TestConstants.UNDEFINED_VALUE, JREmptyDataSource.class },
            { new ArrayList<Object>(), JRBeanCollectionDataSource.class },
            { new Object[0], JRBeanArrayDataSource.class }
        };
        return Arrays.asList(array);
    }

    private UIDataSource component;
    private BeanFiller filler;

    private Object data;
    private Class<JRDataSource> expectedDataSourceClass;

    public BeanFillerTest(Object data,
            Class<JRDataSource> expectedDataSourceClass) {
        this.data = data;
        this.expectedDataSourceClass = expectedDataSourceClass;
    }

    @Before
    public void init() throws Exception {
        facesEnv = MockFacesEnvironment.getServletInstance();

        component = new UIDataSource();
        filler = new BeanFiller(component);

        if (!TestConstants.UNDEFINED_VALUE.equals(data)) {
            component.setData(data);
        }
    }

    @Test
    public void createJRDataSource() {
        JRDataSource ds = filler.getJRDataSource(facesEnv.getFacesContext());
        assertNotNull("Received JRDataSource was null", ds);
        assertEquals("JRDataSource class is not a '" + expectedDataSourceClass +
                "' instance.", expectedDataSourceClass, ds.getClass());
    }

}
