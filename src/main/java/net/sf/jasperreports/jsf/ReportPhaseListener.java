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


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.util.ReportUtil;

public class ReportPhaseListener implements PhaseListener {
	
	public static final String BASE_URI = "/jasperreports";
	
	public static final String PARAM_CLIENTID = "clientId";
		
	public static final String REPORT_COMPONENT_KEY_PREFIX = "UIJasperReport-";
	
	private final Logger logger = Logger.getLogger(
			ReportPhaseListener.class.getPackage().getName(),
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
			
			String reportType = report.getFormat();
			if(reportType == null) reportType = "pdf";
			if(!ReportUtil.CONTENT_TYPE_MAP.containsKey(reportType)) {
				throw new FacesException("Illegal report type: " + reportType);
			}
			
			JasperPrint filledReport = ReportUtil.fillReport(context, report);
			
			try {
				HttpServletResponse response = (HttpServletResponse) extContext.getResponse();
				ByteArrayOutputStream reportData = new ByteArrayOutputStream();
				try {
					ReportUtil.exportReport(report, filledReport, reportData);
					
					response.setContentType(ReportUtil.CONTENT_TYPE_MAP.get(reportType));
					response.setContentLength(reportData.size());
					response.getOutputStream().write(reportData.toByteArray());
					
					context.responseComplete();
				} finally {
					reportData.close();
				}
			} catch(IOException e) {
				throw new FacesException(e);
			}
		}
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}
	
}
