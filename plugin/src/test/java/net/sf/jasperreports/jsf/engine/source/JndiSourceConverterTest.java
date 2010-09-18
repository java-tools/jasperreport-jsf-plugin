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

import java.sql.Connection;
import net.sf.jasperreports.jsf.component.UISource;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockFacesServletEnvironment;
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
public class JndiSourceConverterTest {

    @DataPoint
    public static final String NULL_VALUE = null;

    private MockFacesEnvironment facesEnv;

    private JndiSourceConverter converter;
    private UISource component;

    @Before
    public void init() {
        facesEnv = new MockFacesServletEnvironment();
        converter = new JndiSourceConverter();
        component = new UISource();
    }

    @After
    public void dispose() {
        facesEnv.release();
    }

    @Theory
    public void nullValueReturnsNull(String value) {
        assumeThat(value, nullValue());

        component.setValue(value);

        Connection connection = converter.getConnection(
                facesEnv.getFacesContext(), component);
        assertThat(connection, nullValue());
    }

}
