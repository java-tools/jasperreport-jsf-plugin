/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.fill;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.jsf.TestConstants;
import net.sf.jasperreports.jsf.component.UIDataSource;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author antonio.alonso
 */
public abstract class JRDataSourceFillerTestCase {

    private UIDataSource component;
    protected AbstractJRDataSourceFiller filler;

    @BeforeTest
    public void init() throws Exception {
        component = new UIDataSource();
        filler = initFiller(component);
        assert filler != null : "Filler not initialized";
    }

    protected abstract AbstractJRDataSourceFiller initFiller(
            UIDataSource component)
            throws Exception;

    @Test(dataProvider = "dsfactory")
    public void createJRDataSource(Object source,
            Class<JRDataSource> expectedDS) {
        if (!TestConstants.UNDEFINED_VALUE.equals(source)) {
            component.setValue(source);
        }
        JRDataSource ds = filler.getJRDataSource(null);
        
        assert ds != null : "Received JRDataSource was null";
        assert expectedDS.isAssignableFrom(ds.getClass())
                : "JRDataSource class is not a '" + expectedDS + "' instance.";
    }

}
