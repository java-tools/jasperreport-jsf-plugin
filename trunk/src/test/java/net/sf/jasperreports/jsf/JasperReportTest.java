/*
 * JaspertReports JSF Plugin Copyright (C) 2009 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf;

import java.io.File;
import java.util.logging.Logger;

import junit.framework.TestCase;

import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

/**
 * The Class JasperReportTest.
 */
public class JasperReportTest extends TestCase {

	private static final Logger logger = Logger
			.getLogger(JasperReportTest.class.getPackage() + ".test");

	/** The context dir. */
	private File contextDir;

	/** The context path. */
	private String contextPath;

	/** The runner. */
	private transient ServletRunner runner;

	/**
	 * Instantiates a new jasper report test.
	 * 
	 * @param name
	 *            the name
	 */
	public JasperReportTest(final String name) {
		super(name);
	}

	/**
	 * Test report inline.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void testReportInline() throws Exception {
		final ServletUnitClient client = runner.newClient();

		client.getResponse("http://localhost" + contextPath
				+ "/ReportInlineTest.jsf");
	}

	/**
	 * Test report link.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void testReportLink() throws Exception {
		final ServletUnitClient client = runner.newClient();
		final WebResponse response = client.getResponse("http://localhost"
				+ contextPath + "/ReportLinkTest.jsf");
		final WebLink link = response.getLinkWithID("reportLink");
		assertNotNull("Link 'reportLink' is null", link);
		link.click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
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
	@Override
	protected void tearDown() throws Exception {
		runner.shutDown();
		runner = null;

		super.tearDown();
	}

}
