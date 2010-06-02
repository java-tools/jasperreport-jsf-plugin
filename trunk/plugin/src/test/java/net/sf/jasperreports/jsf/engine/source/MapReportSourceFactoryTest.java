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
package net.sf.jasperreports.jsf.engine.source;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.jsf.component.UIReportSource;
import net.sf.jasperreports.jsf.engine.ReportSource;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;

import org.junit.After;
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
public class MapReportSourceFactoryTest {

    @DataPoint
    public static final Object NULL_DATA = null;
    @DataPoint
    public static final Map[] ARR_DATA = new Map[]{};
    @DataPoint
    public static final Collection<?> COLL_DATA = Arrays.asList(ARR_DATA);

    private MockFacesEnvironment facesEnv;
    private UIReportSource component;
    private MapReportSourceFactory factory;

    @Before
    public void init() {
        facesEnv = MockFacesEnvironment.getServletInstance();
        component = new UIReportSource();
        factory = new MapReportSourceFactory();
    }

    @After
    public void dispose() {
        factory = null;
        component = null;

        facesEnv.release();
        facesEnv = null;
    }

    @Theory
    public void arrayDataReturnsArrayDataSource(final Object data) {
        assumeThat(data, is(not(nullValue())));
        assumeTrue(data instanceof Object[]);

        final FacesContext facesContext = facesEnv.getFacesContext();

        component.setData(data);
        ReportSource reportSource = factory.createSource(facesContext, component);
        assertThat(reportSource, is(not(nullValue())));

        if (!(reportSource instanceof JRDataSourceHolder)) {
            fail("'Returned reportSource is not JasperReport's data source wrapper");
        }

        JRDataSource dataSource = ((JRDataSourceHolder) reportSource).get();
        assertThat(dataSource, is(not(nullValue())));
        assertThat(dataSource, is(JRMapArrayDataSource.class));

        JRMapArrayDataSource bads = (JRMapArrayDataSource) dataSource;
        assertThat(bads.getData(), equalTo((Object[]) data));
    }

    @Theory
    public void collectionDataReturnsCollectionDataSource(Object data) {
        assumeThat(data, is(not(nullValue())));
        assumeThat(data, is(Collection.class));

        final FacesContext facesContext = facesEnv.getFacesContext();

        component.setData(data);
        ReportSource reportSource = factory.createSource(facesContext, component);
        assertThat(reportSource, is(not(nullValue())));

        if (!(reportSource instanceof JRDataSourceHolder)) {
            fail("'Returned reportSource is not JasperReport's data source wrapper");
        }

        JRDataSource dataSource = ((JRDataSourceHolder) reportSource).get();
        assertThat(dataSource, is(not(nullValue())));
        assertThat(dataSource, is(JRMapCollectionDataSource.class));

        JRMapCollectionDataSource bacs =
                (JRMapCollectionDataSource) dataSource;
        assertThat(bacs.getData(), equalTo((Collection) data));
    }

    @Theory
    public void nullDataReturnsEmptyDataSource(Object data) {
        assumeThat(data, is(nullValue()));

        ReportSource reportSource = factory.createSource(
                facesEnv.getFacesContext(), component);
        assertThat(reportSource, is(not(nullValue())));

        if (!(reportSource instanceof JRDataSourceHolder)) {
            fail("'Returned reportSource is not JasperReport's data source wrapper");
        }

        JRDataSource dataSource = ((JRDataSourceHolder) reportSource).get();
        assertThat(dataSource, is(not(nullValue())));
        assertThat(dataSource, is(JREmptyDataSource.class));
    }
}