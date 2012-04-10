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

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.test.mock.MockFacesServletEnvironment;
import org.apache.shale.test.mock.MockHttpServletRequest;
import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

@RunWith(Theories.class)
public class ReportURIEncoderDecodeTest {

    private static final String REPORT_ID = "fooForm:fooReport";

    private static final String VIEW_ID = "/view.xhtml";

    private static final String PREFIX_MAPPING = "/faces";

    private static final String SUFFIX_MAPPING = ".jsf";

    @DataPoints
    public static String[] reportURIs() {
        List<String> uris = new ArrayList<String>();
        uris.add("/invalid/uri/" + REPORT_ID);
        uris.add(Constants.BASE_URI + REPORT_ID + VIEW_ID + SUFFIX_MAPPING +
                ";jsessionid=7832873287328");
        uris.add(PREFIX_MAPPING + Constants.BASE_URI + REPORT_ID + VIEW_ID);
        return uris.toArray(new String[uris.size()]);
    }

    private MockFacesServletEnvironment facesEnv;

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

    @Theory(nullsAccepted = false)
    public void findComponentAndView(String uri) throws Exception {
        assumeThat(uri, not(startsWith("/invalid/uri")));

        initServletPathElements(facesEnv.getRequest(), uri);

        ReportURI reportURI = ReportURIEncoder.decodeReportURI(facesEnv.getFacesContext(), uri);

        if (uri.startsWith(PREFIX_MAPPING)) {
            assertThat(reportURI.getFacesMapping(), equalTo(PREFIX_MAPPING));
        } else {
            assertThat(reportURI.getFacesMapping(), equalTo(SUFFIX_MAPPING));
        }
        assertThat(reportURI.getReportClientId(), equalTo(REPORT_ID));
        assertThat(reportURI.getViewId(), equalTo(VIEW_ID));
    }

    private void initServletPathElements(MockHttpServletRequest request, String uri) {
        String queryString = "";
        int i = uri.indexOf("?");
        if (i >= 0) {
            queryString = uri.substring(i + 1);
            uri = uri.substring(0, i);
        }

        i = uri.indexOf(";jsessionid=");
        if (i >= 0) {
            uri = uri.substring(0, i);
        }

        String servletPath, pathInfo = null;
        if (uri.startsWith(PREFIX_MAPPING)) {
            pathInfo = uri.substring(PREFIX_MAPPING.length());
            servletPath = PREFIX_MAPPING;
        } else {
            servletPath = uri;
        }

        request.setPathElements(System.getProperty("contextPath"),
                servletPath, pathInfo, queryString);
    }

}
