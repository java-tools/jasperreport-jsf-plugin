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
package net.sf.jasperreports.jsf.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRHyperlinkProducerFactory;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.JRXmlExporterParameter;
import net.sf.jasperreports.jsf.IllegalDataSourceTypeException;
import net.sf.jasperreports.jsf.IllegalOutputFormatException;
import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.component.UIReport;

import org.w3c.dom.Document;

public final class ReportUtil {

	// Generic Exporter Parameters
	
	public static final String ATTR_END_PAGE_INDEX = "END_PAGE_INDEX";
	public static final String ATTR_PAGE_INDEX = "PAGE_INDEX";
	public static final String ATTR_START_PAGE_INDEX = "START_PAGE_INDEX";
	
	// CSV Exporter Parameters
	
	public static final String ATTR_FIELD_DELIMITER = "FIELD_DELIMITER";
	public static final String ATTR_RECORD_DELIMITER = "RECORD_DELIMITER";
	
	// Text Exporter Parameters
	
	public static final String ATTR_BETWEEN_PAGES_TEXT = "BETWEEN_PAGES_TEXT";
	public static final String ATTR_CHARACTER_HEIGHT = "CHARACTER_HEIGHT";
	public static final String ATTR_CHARACTER_WIDTH = "CHARACTER_WIDTH";
	public static final String ATTR_LINE_SEPARATOR = "LINE_SEPARATOR";
	public static final String ATTR_PAGE_HEIGHT = "PAGE_HEIGHT";
	public static final String ATTR_PAGE_WIDTH = "PAGE_WIDTH";
	
	// HTML Exporter Parameters
	
	public static final String ATTR_HTML_HEADER = "HTML_HEADER";
	public static final String ATTR_HTML_FOOTER = "HTML_FOOTER";
	public static final String ATTR_IMAGES_DIR = "IMAGES_DIR";
	public static final String ATTR_IMAGES_DIR_NAME = "IMAGES_DIR_NAME";
	public static final String ATTR_IMAGES_URI = "IMAGES_URI";
	
	// PDF Exporter Parameters
	
	public static final String ATTR_IS_COMPRESSED = "IS_COMPRESSED";
	public static final String ATTR_IS_ENCRYPTED = "IS_ENCRYPTED";

	// XML Exporter Parameters
	
	public static final String ATTR_DTD_LOCATION = "DTD_LOCATION";
	public static final String ATTR_IS_EMBEDDING_IMAGES = "IS_EMBEDDING_IMAGES";
	
	// Report Parameters
	
	public static final String PARAM_REPORT_CLASSLOADER = "REPORT_CLASS_LOADER";
	public static final String PARAM_REPORT_LOCALE = "REPORT_LOCALE";
	
	// Content-Type map
	
	public static final Map<String, String> CONTENT_TYPE_MAP;

	// Available formats
	
	public static final String FORMAT_PDF = "pdf";
	public static final String FORMAT_TEXT = "text";
	public static final String FORMAT_HTML = "html";
	public static final String FORMAT_XML = "xml";
	public static final String FORMAT_RTF = "rtf";
	public static final String FORMAT_XLS = "xls";
	public static final String FORMAT_CSV = "csv";
	
	static {
		Map<String, String> contentTypeMap = new HashMap<String, String>();
		contentTypeMap.put(FORMAT_TEXT, "text/plain");
		contentTypeMap.put(FORMAT_PDF, "application/pdf");
		contentTypeMap.put(FORMAT_HTML, "text/html");
		contentTypeMap.put(FORMAT_XML, "text/xml");
		contentTypeMap.put(FORMAT_RTF, "application/rtf");
		contentTypeMap.put(FORMAT_XLS, "application/vnd.ms-excel");
		contentTypeMap.put(FORMAT_CSV, "text/plain");
		CONTENT_TYPE_MAP = Collections.unmodifiableMap(contentTypeMap);
	}
	
	private static Logger logger = Logger.getLogger(
			ReportUtil.class.getName(), 
			"net.sf.jasperreports.jsf.LogMessages");
	
	public static UIDataSource getDataSourceComponent(
			FacesContext context, UIReport report) {
		UIDataSource dataSource = null;
		for(UIComponent child : ((UIComponent) report).getChildren()) {
			if(child instanceof UIDataSource) {
				dataSource = (UIDataSource) child;
				break;
			}
		}
		if(dataSource == null && report.getDataSource() != null) {
			String dataSourceId = report.getDataSource();
			dataSource = (UIDataSource) ((UIComponent) report).findComponent(dataSourceId);
			if(dataSource == null) {
				UIComponent container = (UIComponent) report;
				while(!(container instanceof NamingContainer)) {
					container = container.getParent();
				}
				dataSource = (UIDataSource) container.findComponent(dataSourceId);
			}
		}
		if(dataSource != null && logger.isLoggable(Level.FINE)) {
			String dsClientId = dataSource.getClientId(context);
			logger.log(Level.FINE, "JRJSF_0009", dsClientId);
		}
		return dataSource;
	}
	
	public static void exportReport(FacesContext context, UIReport report, 
			JasperPrint print, OutputStream stream)
	throws JRException {
		JRExporter exporter = null;
		UIComponent reportComp = (UIComponent) report;
		
		String format = report.getFormat();
		if(FORMAT_TEXT.equals(format)) {
			exporter = new JRTextExporter();
			exporter.setParameter(
					JRTextExporterParameter.BETWEEN_PAGES_TEXT, 
					reportComp.getAttributes().get(ATTR_BETWEEN_PAGES_TEXT));
			exporter.setParameter(
					JRTextExporterParameter.CHARACTER_HEIGHT, 
					reportComp.getAttributes().get(ATTR_CHARACTER_HEIGHT));
			exporter.setParameter(
					JRTextExporterParameter.CHARACTER_WIDTH, 
					reportComp.getAttributes().get(ATTR_CHARACTER_WIDTH));
			exporter.setParameter(
					JRTextExporterParameter.LINE_SEPARATOR, 
					reportComp.getAttributes().get(ATTR_LINE_SEPARATOR));
			exporter.setParameter(
					JRTextExporterParameter.PAGE_HEIGHT, 
					reportComp.getAttributes().get(ATTR_PAGE_HEIGHT));
			exporter.setParameter(
					JRTextExporterParameter.PAGE_WIDTH, 
					reportComp.getAttributes().get(ATTR_PAGE_WIDTH));
		} else if(FORMAT_PDF.equals(format)) {
			exporter = new JRPdfExporter();
			exporter.setParameter(
					JRPdfExporterParameter.IS_COMPRESSED, 
					reportComp.getAttributes().get(ATTR_IS_COMPRESSED));
			exporter.setParameter(
					JRPdfExporterParameter.IS_ENCRYPTED, 
					reportComp.getAttributes().get(ATTR_IS_ENCRYPTED));
		} else if(FORMAT_XLS.equals(format)) {
			exporter = new JRXlsExporter();
		} else if(FORMAT_HTML.equals(format)) {
			exporter = new JRHtmlExporter();
			exporter.setParameter(
					JRHtmlExporterParameter.HTML_HEADER, 
					reportComp.getAttributes().get(ATTR_HTML_HEADER));
			exporter.setParameter(
					JRHtmlExporterParameter.HTML_FOOTER, 
					reportComp.getAttributes().get(ATTR_HTML_FOOTER));
			exporter.setParameter(
					JRHtmlExporterParameter.IMAGES_DIR, 
					reportComp.getAttributes().get(ATTR_IMAGES_DIR));
			exporter.setParameter(
					JRHtmlExporterParameter.IMAGES_DIR_NAME, 
					reportComp.getAttributes().get(ATTR_IMAGES_DIR_NAME));
			exporter.setParameter(
					JRHtmlExporterParameter.IMAGES_URI, 
					reportComp.getAttributes().get(ATTR_IMAGES_URI));
		} else if(FORMAT_CSV.equals(format))  {
			exporter = new JRCsvExporter();
			exporter.setParameter(
					JRCsvExporterParameter.FIELD_DELIMITER, 
					reportComp.getAttributes().get(ATTR_FIELD_DELIMITER));
			exporter.setParameter(
					JRCsvExporterParameter.RECORD_DELIMITER, 
					reportComp.getAttributes().get(ATTR_RECORD_DELIMITER));
		} else if(FORMAT_RTF.equals(format)) {
			exporter = new JRRtfExporter();
		} else if(FORMAT_XML.equals(format)) {
			exporter = new JRXmlExporter();
			exporter.setParameter(
					JRXmlExporterParameter.DTD_LOCATION, 
					reportComp.getAttributes().get(ATTR_DTD_LOCATION));
			exporter.setParameter(
					JRXmlExporterParameter.IS_EMBEDDING_IMAGES, 
					reportComp.getAttributes().get(ATTR_IS_EMBEDDING_IMAGES));
		} else {
			throw new IllegalOutputFormatException(format);
		}
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
		exporter.setParameter(JRExporterParameter.CLASS_LOADER, 
				Util.getClassLoader(report));
		
		exporter.setParameter(JRExporterParameter.END_PAGE_INDEX, 
				reportComp.getAttributes().get(ATTR_END_PAGE_INDEX));
		exporter.setParameter(JRExporterParameter.PAGE_INDEX, 
				reportComp.getAttributes().get(ATTR_PAGE_INDEX));
		exporter.setParameter(JRExporterParameter.START_PAGE_INDEX, 
				reportComp.getAttributes().get(ATTR_START_PAGE_INDEX));
		
		JRHyperlinkProducerFactory hpf = new FacesHyperlinkProducerFactory(context, reportComp);
		exporter.setParameter(JRExporterParameter.HYPERLINK_PRODUCER_FACTORY, hpf);
		
		exporter.exportReport();
	}
	
	public static JasperPrint fillReport(FacesContext context, UIReport report) 
	throws JRException {
		String reportName = report.getPath();
		ResourceLoader resourceLoader = ResourceLoader.getResourceLoader(
				context, reportName);
		InputStream reportStream = resourceLoader.getResourceAsStream(reportName);
		if(reportStream == null) {
			throw new FacesException("Report file not found: " + reportName);
		}
		logger.log(Level.FINE, "JRJSF_0003", reportName);
		
		JasperPrint print = null;
		Map<String, Object> params = buildParamMap(context, report);
		
		UIDataSource dsComp = getDataSourceComponent(context, report);
		if(dsComp == null) {
			print = JasperFillManager.fillReport(reportStream, params);
		} else {
			Connection conn = null;
			ResultSet rs = null;
			JRDataSource dataSource = null;
			
			try {
				String dsType = dsComp.getType();
				if(UIDataSource.DEFAULT.equals(dsType)) {
					// We will try to obtain a Connection instance
					// using the DriverManager
					String driverClass = dsComp.getDriverClass();
					if(driverClass == null || driverClass.length() == 0) {
						throw new FacesException("DEFAULT dataSource type requires a driverClass value!");
					}
					logger.log(Level.FINE, "JRJSF_0004", driverClass);
					
					try {
						Class.forName(driverClass);
					} catch(ClassNotFoundException e) {
						throw new FacesException("Driver class not found: " + driverClass, e);
					}
					
					String connectionURL = (String) dsComp.getValue();
					String username = (String) dsComp.getAttributes().get("username");
					String password = (String) dsComp.getAttributes().get("password");
					try {
						if(username == null) {
							logger.log(Level.FINE, "JRJSF_0007", connectionURL);
							conn = DriverManager.getConnection(connectionURL);
						} else {
							if(logger.isLoggable(Level.FINE)) {
								logger.log(Level.FINE, "JRJSF_0008", 
										new Object[]{ connectionURL, username });
							}
							conn = DriverManager.getConnection(connectionURL, 
									username, password);
						}
					} catch(SQLException e) {
						throw new JRFacesException(e);
					}
					
					String query = dsComp.getQuery();
					if(query != null && query.length() > 0) {
						rs = executeQuery(conn, dsComp);
						dataSource = new JRResultSetDataSource(rs);
					}
				} else if(UIDataSource.BEAN.equals(dsType)) {
					Object value = dsComp.getValue();
					if(value instanceof Collection) {
						dataSource = new JRBeanCollectionDataSource(
								(Collection) value);
					} else {
						Object[] beanArray;
						if(!value.getClass().isArray()) {
							beanArray = new Object[]{ value };
						} else {
							beanArray = (Object[]) value;
						}
						dataSource = new JRBeanArrayDataSource(beanArray);
					}
				} else if(UIDataSource.MAP.equals(dsType)) {
					Object value = dsComp.getValue();
					if(value instanceof Collection) {
						dataSource = new JRMapCollectionDataSource(
								(Collection) value);
					} else {
						Map[] mapArray;
						if(!value.getClass().isArray()) {
							mapArray = new Map[]{ (Map) value };
						} else {
							mapArray = (Map[]) value;
						}
						dataSource = new JRMapArrayDataSource(mapArray);
					}
				} else if(UIDataSource.JNDI.equals(dsType)) {
					String dataSourceName = (String) dsComp.getValue();
					logger.log(Level.FINE, "JRJSF_0005", dataSourceName);
					try {
						Context jndi = new InitialContext();
						DataSource ds = (DataSource) jndi
							.lookup(dataSourceName);
						conn = ds.getConnection();
					} catch(NamingException e) {
						throw new JRFacesException(e);
					} catch(SQLException e) {
						throw new JRFacesException(e);
					}
					
					String query = dsComp.getQuery();
					if(query != null && query.length() > 0) {
						rs = executeQuery(conn, dsComp);
						dataSource = new JRResultSetDataSource(rs);
					}
				} else if(UIDataSource.EMPTY.equals(dsType)) {
					dataSource = new JREmptyDataSource();
				} else if(UIDataSource.CSV.equals(dsType)) {
					File file = (File) dsComp.getValue();
					try {
						dataSource = new JRCsvDataSource(file);
					} catch(FileNotFoundException e) {
						throw new JRFacesException(e);
					}
				} else if(UIDataSource.XML.equals(dsType)) {
					Document xmlDocument = (Document) dsComp.getValue();
					String query = dsComp.getQuery();
					if(query != null && query.length() > 0) {
						dataSource = new JRXmlDataSource(xmlDocument, query);
					} else {
						dataSource = new JRXmlDataSource(xmlDocument);
					}
				} else if(UIDataSource.RESULT_SET.equals(dsType)) {
					rs = (ResultSet) dsComp.getValue();
					dataSource = new JRResultSetDataSource(rs);
				}
				
				if(dataSource != null) {
					print = JasperFillManager.fillReport(
							reportStream, params, dataSource);
				} else if(conn != null) {
					print = JasperFillManager.fillReport(
							reportStream, params, conn);
				} else {
					throw new IllegalDataSourceTypeException(dsType);
				}
			} finally {
				if(rs != null) {
					try {
						rs.close();
					} catch(SQLException e) { }
				}
				if(conn != null) {
					try {
						conn.close();
					} catch(SQLException e) { }
				}
			}
		}
		
		return print;
	}
	
	private static Map<String, Object> buildParamMap(FacesContext context, UIReport report) {
		// Build param map using component's child parameters
		Map<String, Object> parameters = new HashMap<String, Object>();
		for(UIComponent component : ((UIComponent) report).getChildren()) {
			if(!(component instanceof UIParameter)) continue;
			UIParameter param = (UIParameter) component;
			parameters.put(param.getName(), param.getValue());
		}
		
		// Specific report parameters
		parameters.put(PARAM_REPORT_CLASSLOADER, Util.getClassLoader(report));
		parameters.put(PARAM_REPORT_LOCALE, context.getViewRoot().getLocale());
		
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
	
	private static ResultSet executeQuery(Connection conn, UIDataSource dataSource) 
	throws JRFacesException {
		PreparedStatement st = null;
		String query = dataSource.getQuery();
		try {
			int paramIdx = 1;
			st = conn.prepareStatement(query);
			for(UIComponent kid : dataSource.getChildren()) {
				if(!(kid instanceof UIParameter)) continue;
				UIParameter param = (UIParameter) kid;
				st.setObject(paramIdx++, param.getValue());
			}
			return st.executeQuery();
		} catch(SQLException e) {
			throw new JRFacesException(e);
		} finally {
			if(st != null) {
				try {
					st.close();
				} catch(SQLException e) { }
			}
		}
	}
	
	private ReportUtil() { }
	
}
