/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.engine.databroker;

import java.util.Arrays;
import java.util.Collection;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.jsf.component.UIDataBroker;
import net.sf.jasperreports.jsf.test.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.TestConstants;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

import org.junit.Before;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(Theories.class)
public class BeanDataBrokerFactoryTest {

    private MockFacesEnvironment facesEnv;

    private UIDataBroker component;
    private BeanDataBrokerFactory factory;

    @DataPoint
    public static final Object NULL_DATA = TestConstants.UNDEFINED_VALUE;

    @DataPoint
    public static final Object[] ARR_DATA = new Object[] {
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    };

    @DataPoint
    public static final Collection<?> COLL_DATA = Arrays.asList(ARR_DATA);

    @Before
    public void init() {
        facesEnv = MockFacesEnvironment.getServletInstance();
        component = new UIDataBroker();
        factory = new BeanDataBrokerFactory();
    }
    
    @Theory
    public void arrayDataReturnsArrayDataSource(final Object data) {
        assumeTrue(data != null);
        assumeTrue(data instanceof Object[]);

        final FacesContext facesContext = facesEnv.getFacesContext();

        component.setData(data);
        DataBroker broker = factory.createDataBroker(
                facesEnv.getFacesContext(), component);
        assertNotNull(broker);

        if (!(broker instanceof JRDataSourceBroker)) {
            fail("'Returned broker is not JasperReport's data source wrapper");
        }

        JRDataSource dataSource = ((JRDataSourceBroker) broker).getDataSource();
        assertNotNull(dataSource);
        assertEquals(dataSource.getClass(), JRBeanArrayDataSource.class);

        JRBeanArrayDataSource bads = (JRBeanArrayDataSource) dataSource;
        assertThat(bads.getData(), equalTo((Object[]) data));
    }

    @Theory
    public void nullDataReturnsEmptyDataSource(Object data) {
        assumeThat(data, equalTo(TestConstants.UNDEFINED_VALUE));

        DataBroker broker = factory.createDataBroker(
                facesEnv.getFacesContext(), component);
        assertNotNull(broker);
        
        if (!(broker instanceof JRDataSourceBroker)) {
            fail("'Returned broker is not JasperReport's data source wrapper");
        }
        
        JRDataSource dataSource = ((JRDataSourceBroker) broker).getDataSource();
        assertNotNull(dataSource);
        assertEquals(dataSource.getClass(), JREmptyDataSource.class);
    }

}
