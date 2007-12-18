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
		link.click();
		
	}
	
}
