/*
 * JaspertReports JSF Plugin Copyright (C) 2011 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.engine.interop;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.jsf.TestConstants;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.ResourceResolver;
import net.sf.jasperreports.jsf.test.dummy.DummyUIReport;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockFacesServletEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockJRFacesContext;

import org.jmock.Expectations;
import org.jmock.Mockery;
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
public class FacesFileResolverTest {

    @DataPoint
    public static String localResource() {
        String documentRoot = System.getProperty(
                TestConstants.PROP_DOCUMENT_ROOT);
        return new File(documentRoot + "/index.xhtml").getAbsolutePath();
    }

    @DataPoint
    public static URL localURL() {
        File localFile = new File(localResource());
        try {
            return localFile.toURI().toURL();
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    @DataPoint
    public static String REMOTE_RESOURCE =
            "http://jasperreportjsf.sourceforge.net/web/index.html";

    @DataPoint
    public static URL remoteURL() {
        try {
            return new URL("http", "jasperreportjsf.sourceforge.net",
                    80, "/web/index.html");
        } catch (MalformedURLException ex) {
            return null;
        }
    }
    
    private Mockery mockery = new Mockery();

    private MockFacesEnvironment facesEnv;
    private MockJRFacesContext jrContext;

    private ResourceResolver resourceResolver;
    private UIReport component;

    @Before
    public void init() throws Exception {
        facesEnv = new MockFacesServletEnvironment();
        component = new DummyUIReport();
        resourceResolver = mockery.mock(ResourceResolver.class);
        jrContext = new MockJRFacesContext(facesEnv.getFacesContext());
        jrContext.setResourceResolver(resourceResolver);
    }

    @After
    public void dispose() {
        jrContext = null;

        resourceResolver = null;
        component = null;

        facesEnv.release();
        facesEnv = null;
    }

	@Theory
	@SuppressWarnings("unused")
    public void nullReportCompThrowsIllegalArgEx() {
        try {
            FacesFileResolver fr = new FacesFileResolver(null);
            fail("An illegal argument exception should be thrown");
        } catch (Exception e) {
            assertThat(e, is(IllegalArgumentException.class));
        }
    }

    @Theory
    public void validResourceResolvesFile(
            final String resourceName, final URL expectedURL)
            throws Exception {
        assumeNotNull(resourceName, expectedURL);
        assumeTrue(resourceName.length() > 0);
        if (resourceName.startsWith("http")) {
            assumeThat(expectedURL.getProtocol(), equalTo("http"));
        } else {
            assumeThat(expectedURL.getProtocol(), equalTo("file"));
        }

        final InputStream stream = createStream();
        final Resource resource = mockery.mock(Resource.class);

        mockery.checking(new Expectations() {{
            oneOf(resourceResolver).resolveResource(facesEnv.getFacesContext(),
                    component, resourceName);
            will(returnValue(resource));
            between(1, 3).of(resource).getLocation();
            will(returnValue(expectedURL));
            atMost(1).of(resource).getName();
            will(returnValue(resourceName));
            atMost(1).of(resource).getInputStream();
            will(returnValue(stream));
        }});

        FileResolver fr = new FacesFileResolver(component);
        File file = fr.resolveFile(resourceName);

        assertThat(file, not(nullValue()));
        if (!expectedURL.getProtocol().equals("http")) {
            assertThat(file.getAbsolutePath(), equalTo(resourceName));
        }
        assertTrue("File '" + file + "' doesn't exists", file.exists());
    }

    private InputStream createStream() {
        Random random = new Random();
        byte[] buff = new byte[2048 * 2];
        random.nextBytes(buff);
        return new ByteArrayInputStream(buff);
    }

}
