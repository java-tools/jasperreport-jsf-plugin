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
package net.sf.jasperreports.jsf.engine;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.resource.URLResourceTest;
import net.sf.jasperreports.jsf.test.MockFacesEnvironment;
import org.apache.shale.test.mock.MockFacesContext;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(Theories.class)
public class FacesFileResolverTest {

    @DataPoint
    public static String LOCAL_RESOURCE =
            URLResourceTest.localUrl().toString();

    @DataPoint
    public static String REMOTE_RESOURCE =
            URLResourceTest.remoteUrl().toString();
    
    private Mockery mockery = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    private MockFacesEnvironment facesEnv;

    private FacesFileResolver resolver;
    private UIReport component;

    @Before
    public void init() {
        facesEnv = MockFacesEnvironment.getServletInstance();

    }

    @After
    public void dispose() {
        facesEnv.release();
        facesEnv = null;
    }

    @Theory
    public void ifRemoteResourceDownloadAsTempFile(String resourceName) {

    }

}
