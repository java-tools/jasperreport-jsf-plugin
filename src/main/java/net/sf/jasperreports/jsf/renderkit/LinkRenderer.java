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
package net.sf.jasperreports.jsf.renderkit;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import net.sf.jasperreports.jsf.component.UIReport;

/**
 * The Class LinkRenderer.
 * 
 * @author A. Alonso Dominguez
 */
public class LinkRenderer extends AbstractReportRenderer {

	/** The Constant RENDERER_TYPE. */
	public static final String RENDERER_TYPE = "net.sf.jasperreports.Link";

	/** The logger. */
	private static final Logger logger = Logger.getLogger(LinkRenderer.class
			.getPackage().getName(), "net.sf.jasperreports.jsf.LogMessages");

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext,
	 * javax.faces.component.UIComponent)
	 */
	@Override
	@SuppressWarnings("unused")
	public void encodeBegin(final FacesContext context,
			final UIComponent component) throws IOException {
		final ViewHandler viewHandler = context.getApplication()
				.getViewHandler();
		final UIReport report = (UIReport) component;
		final String reportURI = viewHandler.getResourceURL(context,
				buildReportURI(context, component));

		final ResponseWriter writer = context.getResponseWriter();
		logger.log(Level.FINE, "JRJSF_0001", component.getClientId(context));

		writer.startElement("a", component);
		renderIdAttribute(context, component);
		writer.writeURIAttribute("href", reportURI, null);

		renderAttributes(writer, component);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext,
	 * javax.faces.component.UIComponent)
	 */
	@Override
	public void encodeEnd(final FacesContext context,
			final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		writer.endElement("a");

		registerComponent(context, component);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.jasperreports.jsf.renderkit.AbstractReportRenderer#renderAttributes
	 * (javax.faces.context.ResponseWriter, javax.faces.component.UIComponent)
	 */
	@Override
	protected void renderAttributes(final ResponseWriter writer,
			final UIComponent report) throws IOException {
		super.renderAttributes(writer, report);

		final String target = (String) report.getAttributes().get("target");
		if (target != null) {
			writer.writeAttribute("target", target, null);
		}
	}

}
