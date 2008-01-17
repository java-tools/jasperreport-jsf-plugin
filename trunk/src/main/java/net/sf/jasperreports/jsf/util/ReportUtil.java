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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.component.*;
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
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.component.UIReport;

import org.w3c.dom.Document;

public final class ReportUtil {

	public static final Map<String, String> CONTENT_TYPE_MAP;
	
	static {
		Map<String, String> contentTypeMap = new HashMap<String, String>();
		contentTypeMap.put("text", "text/plain");
		contentTypeMap.put("pdf", "application/pdf");
		contentTypeMap.put("html", "text/html");
		contentTypeMap.put("xml", "text/xml");
		contentTypeMap.put("rtf", "application/rtf");
		contentTypeMap.put("xls", "application/vnd.ms-excel");
		contentTypeMap.put("csv", "application/vnd.ms-excel");
		CONTENT_TYPE_MAP = Collections.unmodifiableMap(contentTypeMap);
	}
	
	private static Logger logger = Logger.getLogger(
			ReportUtil.class.getName(), 
			"net.sf.jasperreports.jsf.LogMessages");
	
	public static UIDataSource getDataSourceComponent(FacesContext context, UIReport report) {
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
		return dataSource;
	}
	
	public static void exportReport(UIReport report, JasperPrint print, OutputStream stream)
	throws FacesException {
		JRExporter exporter = null;
		if("text".equals(report.getFormat())) {
			exporter = new JRTextExporter();
		} else if("pdf".equals(report.getFormat())) {
			exporter = new JRPdfExporter();
		} else if("xls".equals(report.getFormat())) {
			exporter = new JRXlsExporter();
		} else if("html".equals(report.getFormat())) {
			exporter = new JRHtmlExporter();
		} else if("csv".equals(report.getFormat()))  {
			exporter = new JRCsvExporter();
		} else if("rtf".equals(report.getFormat())) {
			exporter = new JRRtfExporter();
		} else if("xml".equals(report.getFormat())) {
			exporter = new JRXmlExporter();
		} else {
			throw new FacesException("Illegar output format: " + 
					report.getFormat());
		}
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
		exporter.setParameter(JRExporterParameter.CLASS_LOADER, 
				Util.getClassLoader(report));
		
		try {
			exporter.exportReport();
		} catch(JRException e) {
			throw new FacesException(e);
		}
	}
	
	public static JasperPrint fillReport(FacesContext context, UIReport report) 
	throws FacesException {
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
			try {
				print = JasperFillManager.fillReport(reportStream, params);
			} catch(JRException e) {
				throw new FacesException(e);
			}
		} else {
			Connection conn = null;
			JRDataSource dataSource = null;
			
			if(UIDataSource.DEFAULT.equals(dsComp.getType())) {
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
				try {
					conn = DriverManager.getConnection(connectionURL);
				} catch(SQLException e) {
					throw new FacesException(e);
				}
				
				String query = dsComp.getQuery();
				if(query != null && query.length() > 0) {
					ResultSet rs = executeQuery(conn, dsComp);
					dataSource = new JRResultSetDataSource(rs);
				}
			} else if(UIDataSource.JNDI.equals(dsComp.getType())) {
				String dataSourceName = (String) dsComp.getValue();
				logger.log(Level.FINE, "JRJSF_0005", dataSourceName);
				try {
					Context jndi = new InitialContext();
					DataSource ds = (DataSource) jndi
						.lookup(dataSourceName);
					conn = ds.getConnection();
				} catch(NamingException e) {
					throw new FacesException(e);
				} catch(SQLException e) {
					throw new FacesException(e);
				}
				
				String query = dsComp.getQuery();
				if(query != null && query.length() > 0) {
					ResultSet rs = executeQuery(conn, dsComp);
					dataSource = new JRResultSetDataSource(rs);
				}
			} else if(UIDataSource.EMPTY.equals(dsComp.getType())) {
				dataSource = new JREmptyDataSource();
			} else if(UIDataSource.CSV.equals(dsComp.getType())) {
				File file = (File) dsComp.getValue();
				try {
					dataSource = new JRCsvDataSource(file);
				} catch(FileNotFoundException e) {
					throw new FacesException(e);
				}
			} else if(UIDataSource.XML.equals(dsComp.getType())) {
				Document xmlDocument = (Document) dsComp.getValue();
				String query = dsComp.getQuery();
				try {
					if(query != null && query.length() > 0) {
						dataSource = new JRXmlDataSource(xmlDocument, query);
					} else {
						dataSource = new JRXmlDataSource(xmlDocument);
					}
				} catch(JRException e) {
					throw new FacesException(e);
				}
			}
			
			try {
				if(dataSource != null) {
					logger.log(Level.FINE, "JRJSF_0006");
					print = JasperFillManager.fillReport(
							reportStream, params, dataSource);
				} else if(conn != null) {
					logger.log(Level.FINE, "JRJSF_0006");
					print = JasperFillManager.fillReport(
							reportStream, params, conn);
				} else {
					throw new FacesException("Invalid dataSource type: " + 
							dsComp.getType());
				}
			} catch(JRException e) {
				throw new FacesException(e);
			}
		}
		
		return print;
	}
	
	private static Map<String, Object> buildParamMap(FacesContext context, UIReport report) 
	throws FacesException {
		// Build param map using component's child parameters
		Map<String, Object> parameters = new HashMap<String, Object>();
		for(UIComponent component : ((UIComponent) report).getChildren()) {
			if(!(component instanceof UIParameter)) continue;
			UIParameter param = (UIParameter) component;
			parameters.put(param.getName(), param.getValue());
		}
		
		// Specific report parameters
		parameters.put("REPORT_CLASS_LOADER", Util.getClassLoader(report));
		
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
	throws FacesException {
		String query = dataSource.getQuery();
		if(query == null || query.length() == 0) {
			throw new IllegalArgumentException("Invalid query!");
		}
		
		try {
			int paramIdx = 1;
			PreparedStatement st = conn.prepareStatement(query);
			for(UIComponent kid : dataSource.getChildren()) {
				if(!(kid instanceof UIParameter)) continue;
				UIParameter param = (UIParameter) kid;
				st.setObject(paramIdx++, param.getValue());
			}
			return st.executeQuery();
		} catch(SQLException e) {
			throw new FacesException(e);
		}
	}
	
	private ReportUtil() { }
	
}
