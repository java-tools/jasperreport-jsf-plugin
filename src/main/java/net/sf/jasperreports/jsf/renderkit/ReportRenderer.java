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
package net.sf.jasperreports.jsf.renderkit;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import net.sf.jasperreports.jsf.ReportPhaseListener;
import net.sf.jasperreports.jsf.component.UIReport;

/**
 * 
 * @author A. Alonso Dominguez
 *
 */
public class ReportRenderer extends AbstractReportRenderer {

	public static final String RENDERER_TYPE = 
		"net.sf.jasperreports.Report";
	
	private final Logger logger = Logger.getLogger(
			ReportRenderer.class.getPackage().getName(),
			"net.sf.jasperreports.jsf.LogMessages");
	
	@Override
	@SuppressWarnings("unused")
	public void encodeBegin(FacesContext context, UIComponent component)
	throws IOException {
		ViewHandler viewHandler = context.getApplication().getViewHandler();
		UIReport report = (UIReport) component;
		String reportURI = viewHandler.getResourceURL(context, 
				buildReportURI(context, component));
		
		logger.log(Level.FINE, "JRJSF_0002", component.getClientId(context));
		
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("iframe", component);
		renderIdAttribute(context, component);
		writer.writeURIAttribute("src", reportURI, null);
				
		renderAttributes(writer, component);
	}
	
	@Override
	public void encodeChildren(FacesContext context, UIComponent component)
	throws IOException { }
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
	throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("iframe");
		
		String clientId = component.getClientId(context);
		context.getExternalContext().getSessionMap().put(
				ReportPhaseListener.REPORT_COMPONENT_KEY_PREFIX + clientId, component);
	}
	
}
