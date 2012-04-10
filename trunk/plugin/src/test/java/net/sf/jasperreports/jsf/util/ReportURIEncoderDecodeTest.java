/*
 * JaspertReports JSF Plugin Copyright (C) 2012 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.util;

import net.sf.jasperreports.jsf.test.JMockTheories;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockFacesServletEnvironment;
import org.junit.Before;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

@RunWith(JMockTheories.class)
public class ReportURIEncoderDecodeTest {

    @DataPoint
    public static final String INVALID_URI = "/invalid/uri/report:id";

    private MockFacesEnvironment facesEnv;

    @Before
    public void init() {
        facesEnv = new MockFacesServletEnvironment();
    }

    @Theory(nullsAccepted = false)
    public void invalidURIThrowsIllegalArgEx(String uri) {
        assumeThat(uri, startsWith("/invalid/uri"));

        try {
            ReportURIEncoder.decodeReportURI(facesEnv.getFacesContext(), uri);
            fail("An IllegalArgumentException should be thrown");
        } catch (Exception e) {
            assertThat(e, is(IllegalArgumentException.class));
        }
    }

}
