/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.fill.providers;

import java.util.ArrayList;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.jsf.TestConstants;
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.fill.AbstractJRDataSourceFiller;
import net.sf.jasperreports.jsf.fill.JRDataSourceFillerTestCase;

import org.testng.annotations.DataProvider;

/**
 *
 * @author antonio.alonso
 */
public class MapFillerTest extends JRDataSourceFillerTestCase {

    @DataProvider(name = "dsfactory")
    public Object[][] createData() {
        return new Object[][] {
            { TestConstants.UNDEFINED_VALUE, JREmptyDataSource.class },
            { new ArrayList<Object>(), JRMapCollectionDataSource.class },
            { new Map[0], JRMapArrayDataSource.class }
        };
    }

    @Override
    protected AbstractJRDataSourceFiller initFiller(UIDataSource component) {
        return new MapFiller(component);
    }

}
