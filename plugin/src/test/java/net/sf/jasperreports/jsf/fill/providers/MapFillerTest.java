/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.fill.providers;

import java.util.ArrayList;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.jsf.TestConstants;
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.test.framework.MockFacesEnvironment;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 * @author antonio.alonso
 */
public class MapFillerTest  {

    @DataProvider(name = "dsfactory")
    public Object[][] createData() {
        return new Object[][] {
            { TestConstants.UNDEFINED_VALUE, JREmptyDataSource.class },
            { new ArrayList<Object>(), JRMapCollectionDataSource.class },
            { new Map[0], JRMapArrayDataSource.class }
        };
    }

    private MockFacesEnvironment facesEnv;

    private UIDataSource component;
    private MapFiller filler;

    @BeforeTest
    public void init() throws Exception {
        facesEnv = MockFacesEnvironment.getServletInstance();

        component = new UIDataSource();
        filler = new MapFiller(component);
    }

    @Test(dataProvider = "dsfactory")
    public void createJRDataSource(Object source,
            Class<JRDataSource> expectedDS) {
        if (!TestConstants.UNDEFINED_VALUE.equals(source)) {
            component.setValue(source);
        }
        JRDataSource ds = filler.getJRDataSource(facesEnv.getFacesContext());

        assert ds != null : "Received JRDataSource was null";
        assert expectedDS.isAssignableFrom(ds.getClass())
                : "JRDataSource class is not a '" + expectedDS + "' instance.";
    }

}
