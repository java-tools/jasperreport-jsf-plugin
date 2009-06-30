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
package net.sf.jasperreports.jsf.util;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.renderkit.ReportRenderer;

final class ServletContextHelper extends ExternalContextHelper {

	protected ServletContextHelper() { }

	@Override
	public String getRequestURI(FacesContext context) {
		final HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		return request.getRequestURI();
	}
	
	@Override
	public void writeHeaders(FacesContext context, ReportRenderer renderer,
			UIReport report) throws IOException {
		final HttpServletResponse response = (HttpServletResponse) context
				.getExternalContext().getResponse();
		response.setHeader("Cache-Type", "no-cache");
		response.setHeader("Expires", "0");
		
		if(report.getName() != null) {
			response.setHeader("Content-Disposition", encodeContentDisposition(
					renderer, report, response.getCharacterEncoding()));
		}
	}

	@Override
	public void writeResponse(FacesContext context, String contentType,
			byte[] data) throws IOException {
		final HttpServletResponse response = (HttpServletResponse) context
				.getExternalContext().getResponse();
		response.setContentType(contentType);
		response.setContentLength(data.length);
		response.getOutputStream().write(data);	
	}
	
}
