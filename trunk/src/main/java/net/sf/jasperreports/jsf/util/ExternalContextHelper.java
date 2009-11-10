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
import java.net.URLEncoder;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.renderkit.ReportRenderer;

public abstract class ExternalContextHelper {

	/** The Constant PORTLET_CLASS. */
	private static final String PORTLET_CLASS = "javax.portlet.Portlet";

	/** The Constant PORTLET_RESOURCEURL_CLASS. */
	private static final String PORTLET_RESOURCEURL_CLASS = "javax.portlet.ResourceURL";

	public static ExternalContextHelper getInstance(final FacesContext context) {
		ExternalContextHelper instance;
		if (isServletContext(context)) {
			instance = new ServletContextHelper();
		} else if (isPortletAvailable()) {
			instance = new PortletContextHelper();
		} else {
			throw new IllegalArgumentException(
					"Unrecognized application context");
		}
		return instance;
	}

	protected static boolean isPortletAvailable() {
		boolean portletAvailable = false;
		try {
			Class.forName(PORTLET_CLASS);
			portletAvailable = true;
		} catch (final ClassNotFoundException e) {
			portletAvailable = false;
		} catch (final NoClassDefFoundError e) {
			portletAvailable = false;
		}
		return portletAvailable;
	}

	protected static String getPortletVersion() {
		final boolean portletAvailable = isPortletAvailable();

		String portletVersion = null;
		try {
			Class.forName(PORTLET_RESOURCEURL_CLASS);
			portletVersion = "2.0";
		} catch (final ClassNotFoundException e) {
			portletVersion = portletAvailable ? "1.0" : null;
		} catch (final NoClassDefFoundError e) {
			portletVersion = portletAvailable ? "1.0" : null;
		}
		return portletVersion;
	}

	private static boolean isServletContext(final FacesContext context) {
		final Object ctx = context.getExternalContext().getContext();
		return (ctx instanceof ServletContext);
	}

	protected ExternalContextHelper() {
	}

	/**
	 * Gets the request uri.
	 * 
	 * @param context
	 *            the context
	 * 
	 * @return the request uri
	 */
	public abstract String getRequestURI(final FacesContext context);

	public abstract void writeHeaders(FacesContext context,
			ReportRenderer renderer, UIReport report) throws IOException;

	/**
	 * Write response.
	 * 
	 * @param context
	 *            the context
	 * @param contentType
	 *            the content type
	 * @param data
	 *            the data
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public abstract void writeResponse(final FacesContext context,
			final String contentType, final byte[] data) throws IOException;

	protected String encodeContentDisposition(final ReportRenderer renderer,
			final UIReport report, final String enc) throws IOException {
		final StringBuffer disposition = new StringBuffer();
		if (report.getName() != null) {
			disposition.append(renderer.getContentDisposition());
			disposition.append("; filename=");
			disposition.append(URLEncoder.encode(report.getName(), enc));
		}
		return disposition.toString();
	}

}