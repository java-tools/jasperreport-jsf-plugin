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
package net.sf.jasperreports.jsf.test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.xml.sax.SAXException;

import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

/**
 * The Class JasperReportTest.
 */
public abstract class ReportTestBase {

    private static final Logger logger = Logger.getLogger(
            ReportTestBase.class.getPackage().getName());

    /** The context dir. */
    private File contextDir;
    /** The context path. */
    private String contextPath;

    /** The runner. */
    private transient ServletRunner runner;
    private transient ServletUnitClient client;

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void startContainer() throws Exception {
        contextDir = new File(System.getProperty("context-dir"));
        contextPath = System.getProperty("context-path");

        logger.info("Starting web application '" + contextPath
                + "' from directory: " + contextDir);

        final File webXml = new File(contextDir, "WEB-INF/web.xml");
        runner = new ServletRunner(webXml, contextPath);
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @After
    public void stopContainer() throws Exception {
        if (client != null) {
            client.clearContents();
        }
        client = null;

        runner.shutDown();
        runner = null;
    }

    protected final ServletUnitClient getClient() {
        if (client == null) {
            client = runner.newClient();
        }
        return client;
    }

    protected WebResponse getCurrentPage() {
        final ServletUnitClient client = getClient();
        return client.getCurrentPage();
    }

    protected WebResponse getResponse(final String path)
            throws MalformedURLException, IOException, SAXException {
        final ServletUnitClient client = getClient();
        return client.getResponse("http://localhost" + contextPath + path);
    }
}
