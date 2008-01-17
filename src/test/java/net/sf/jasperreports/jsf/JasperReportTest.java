/* JaspertReports JSF Plugin
 * Copyright (C) 2008 A. Alonso Dominguez
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * A. Alonso Dominguez
 * alonsoft@users.sf.net
 */
package net.sf.jasperreports.jsf;

import java.io.File;

import junit.framework.TestCase;

import com.meterware.httpunit.*;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

public class JasperReportTest extends TestCase {
	private File contextDir;
	private String contextPath;
	
	private ServletRunner runner;
	
	public JasperReportTest(String name) {
		super(name);
	}
	
	@Override
	protected void setUp() throws Exception {
		contextDir = new File(System.getProperty("context-dir"));
		contextPath = System.getProperty("context-path");
		
		File webXml = new File(contextDir, "WEB-INF/web.xml");
		runner = new ServletRunner(webXml, contextPath);
	}
	
	public void testReportInline() throws Exception {
		ServletUnitClient client = runner.newClient();
		
		client.getResponse("http://localhost" + contextPath 
				+ "/ReportInlineTest.jsf");
	}
	
	public void testReportLink() throws Exception {
		ServletUnitClient client = runner.newClient();
		WebResponse response = client.getResponse(
				"http://localhost" + contextPath 
				+ "/ReportLinkTest.jsf");
		WebLink link = response.getLinkWithID("reportLink");
		//link.click();
		
	}
	
}
