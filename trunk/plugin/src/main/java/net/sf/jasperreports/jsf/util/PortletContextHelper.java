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
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.renderkit.ReportRenderer;

final class PortletContextHelper extends ExternalContextHelper {

	protected PortletContextHelper() {
	}

	@Override
	public String getRequestURI(final FacesContext context) {
		if ("2.0".equals(getPortletVersion())) {
			final ResourceRequest request = (ResourceRequest) context
					.getExternalContext().getRequest();
			return request.getResourceID();
		} else {
			return null;
		}
	}

	@Override
	public void writeHeaders(final FacesContext context,
			final ReportRenderer renderer, final UIReport report)
			throws IOException {
		if ("2.0".equals(getPortletVersion())) {
			final ResourceResponse response = (ResourceResponse) context
					.getExternalContext().getResponse();
			response.setProperty("Cache-Type", "no-cache");
			response.setProperty("Expires", "0");

			if (report.getName() != null) {
				response.setProperty("Content-Disposition",
						encodeContentDisposition(renderer, report, response
								.getCharacterEncoding()));
			}
		} else {
			throw new IllegalStateException(
					"Only Resource Request/Response state is allowed");
		}
	}

	@Override
	public void writeResponse(final FacesContext context,
			final String contentType, final byte[] data) throws IOException {
		if ("2.0".equals(getPortletVersion())) {
			final ResourceResponse response = (ResourceResponse) context
					.getExternalContext().getResponse();
			response.setContentType(contentType);
			response.setContentLength(data.length);
			response.getPortletOutputStream().write(data);
		} else {
			throw new IllegalStateException(
					"Only Resource Request/Response state is allowed");
		}
	}

}
