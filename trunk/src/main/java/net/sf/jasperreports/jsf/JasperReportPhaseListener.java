package net.sf.jasperreports.jsf;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.jsf.util.ResourceLoader;

public class JasperReportPhaseListener implements PhaseListener {
	
	public static final String BASE_URI = "/jasperreports";
	
	public static final String PARAM_CLIENTID = "clientId";
		
	public static final String REPORT_COMPONENT_KEY_PREFIX = "UIJasperReport-";
	
	public static final Map<String, String> CONTENT_TYPE_MAP;
	
	static {
		Map<String, String> contentTypeMap = new HashMap<String, String>();
		contentTypeMap.put("pdf", "application/pdf");
		//contentTypeMap.put("html", "text/html");
		contentTypeMap.put("xml", "text/xml");
		//contentTypeMap.put("xls", "application/ms-excel");
		//contentTypeMap.put("csv", "application/vnd.ms-excel");
		CONTENT_TYPE_MAP = Collections.unmodifiableMap(contentTypeMap);
	}
	
	private final Logger logger = Logger.getLogger(
			JasperReportPhaseListener.class.getPackage().getName(),
			"net.sf.jasperreports.jsf.LogMessages");
	
	public void afterPhase(PhaseEvent event) { }

	public void beforePhase(PhaseEvent event) 
	throws FacesException {		
		FacesContext context = event.getFacesContext();
		HttpServletRequest request = (HttpServletRequest) context
			.getExternalContext().getRequest();
		String uri = request.getRequestURI();
		if(uri != null && uri.indexOf(BASE_URI) > -1) {
			ExternalContext extContext = context.getExternalContext();
			
			String clientId = request.getParameter(PARAM_CLIENTID);
			UIReport report = (UIReport) extContext.getSessionMap()
				.remove(REPORT_COMPONENT_KEY_PREFIX + clientId);
			if(report == null) {
				throw new FacesException("UIJasperReport component not found: " + clientId);
			}
			
			String reportType = report.getType();
			if(reportType == null) reportType = "pdf";
			if(!CONTENT_TYPE_MAP.containsKey(reportType)) {
				throw new FacesException("Illegal report type: " + reportType);
			}
			
			ResourceLoader resourceLoader = ResourceLoader.getResourceLoader(
					context, report.getReport());
			String reportName = report.getReport();
			InputStream reportStream = resourceLoader.getResourceAsStream(reportName);
			if(reportStream == null) {
				throw new FacesException("Report file not found: " + reportName);
			}
			logger.log(Level.FINE, "JRJSF_0003", reportName); 
			
			Map<String, Object> parameters = buildParamMap(context, report);
			
			JasperPrint filledReport;
			Connection connection = getConnection(context, report);
			try {
				if(connection != null) {
					filledReport = JasperFillManager.fillReport(
							reportStream, parameters, connection);
				} else {
					filledReport = JasperFillManager.fillReport(
							reportStream, parameters);
				}
			} catch(JRException e) {
				throw new FacesException(e);
			}
			
			try {
				HttpServletResponse response = (HttpServletResponse) extContext.getResponse();
				ByteArrayOutputStream reportData = new ByteArrayOutputStream();
				try {
					if("pdf".equals(reportType)) {
						JasperExportManager.exportReportToPdfStream(
								filledReport, reportData);
					} else {
						 JasperExportManager.exportReportToXmlStream(
								 filledReport, reportData);
					}
					
					response.setContentType(CONTENT_TYPE_MAP.get(reportType));
					response.setContentLength(reportData.size());
					response.getOutputStream().write(reportData.toByteArray());
					
					context.responseComplete();
				} finally {
					reportData.close();
				}
			} catch(Exception e) {
				throw new FacesException(e);
			}
		}
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}
	
	private Connection getConnection(FacesContext context, UIReport report)
	throws FacesException {
		if(null == report.getDataSource()) {
			return null;
		}
		
		try {
			Context jndi = new InitialContext();
			DataSource dataSource = (DataSource) jndi
				.lookup(report.getDataSource());
			return dataSource.getConnection();
		} catch(Exception e) {
			throw new FacesException(e);
		}
	}
	
	private Map<String, Object> buildParamMap(FacesContext context, UIReport report) 
	throws FacesException {
		// Build param map using component's child parameters
		Map<String, Object> parameters = new HashMap<String, Object>();
		for(UIComponent component : report.getChildren()) {
			if(!(component instanceof UIParameter)) continue;
			UIParameter param = (UIParameter) component;
			parameters.put(param.getName(), param.getValue());
		}
		
		// Specific report parameters
		parameters.put("REPORT_CLASS_LOADER", Util.getClassLoader(this));
		
		// Subreport directory
		String subreportDir = report.getSubreportDir();
		if(subreportDir != null) {
			ResourceLoader resourceLoader = ResourceLoader.getResourceLoader(
					context, subreportDir);
			parameters.put("SUBREPORT_DIR", resourceLoader
					.getRealPath(subreportDir));
		}
		
		return parameters;
	}
	
}
