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

import java.sql.ResultSet;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRResultSetDataSource;

import net.sf.jasperreports.jsf.component.UIReportSource;
import net.sf.jasperreports.jsf.engine.ReportSource;
import net.sf.jasperreports.jsf.test.JMockTheories;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;

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
 * @author antonio.alonso
 */
@RunWith(JMockTheories.class)
public class ResultSetReportSourceFactoryTest {

    private static Mockery mockery = new JUnit4Mockery();

    @DataPoint
    public static final ResultSet NULL_RESULTSET = null;

    @DataPoint
    public static final ResultSet MOCK_RESULTSET =
            mockery.mock(ResultSet.class);

    private MockFacesEnvironment facesEnv;

    private UIReportSource component;
    private ResultSetReportSourceFactory factory;

    @Before
    public void init() {
        facesEnv = MockFacesEnvironment.getServletInstance();

        component = new UIReportSource();
        component.setId("reportSourceId");

        factory = new ResultSetReportSourceFactory();
    }

    @After
    public void dispose() {
        factory = null;
        component = null;
        
        facesEnv.release();
        facesEnv = null;
    }

    @Theory
    public void nullResultSetReturnsEmptyDataSource(ResultSet resultSet) {
        assumeThat(resultSet, is(nullValue()));

        component.setData(resultSet);

        ReportSource<JRDataSource> source = factory.createSource(
                facesEnv.getFacesContext(), component);
        assertThat(source, is(not(nullValue())));

        JRDataSource dataSource = source.get();
        assertThat(dataSource, is(not(nullValue())));
        assertThat(dataSource, is(JREmptyDataSource.class));
    }

    @Theory
    public void nonNullResultSetRetunsValidDataSource(ResultSet resultSet) {
        assumeThat(resultSet, is(not(nullValue())));

        component.setData(resultSet);

        ReportSource<JRDataSource> source = factory.createSource(
                facesEnv.getFacesContext(), component);
        assertThat(source, is(not(nullValue())));

        JRDataSource dataSource = source.get();
        assertThat(dataSource, is(not(nullValue())));
        assertThat(dataSource, is(JRResultSetDataSource.class));
    }

}
